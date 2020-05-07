/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.DisplaySection;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SequenceDiagramFileMakerPuma2 implements FileMaker {

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final Dimension2D fullDimension;
	private final List<Page> pages;
	private final FileFormatOption fileFormatOption;
	private final StringBounder stringBounder;

	private double scale;

	public SequenceDiagramFileMakerPuma2(SequenceDiagram sequenceDiagram, Rose skin,
			FileFormatOption fileFormatOption) {
		this.diagram = sequenceDiagram;
		this.stringBounder = fileFormatOption.getDefaultStringBounder();
		this.fileFormatOption = fileFormatOption;
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox(), sequenceDiagram.getAutonewpage());

		for (Participant p : sequenceDiagram.participants()) {
			initializer.addParticipant(p, sequenceDiagram.getEnglober(p));
		}

		for (Event ev : sequenceDiagram.events()) {
			initializer.addEvent(ev);
			// if (ev instanceof Message) {
			// // TODO mieux faire
			// final Message m = (Message) ev;
			// for (LifeEvent lifeEvent : m.getLiveEvents()) {
			// if (lifeEvent.getType() == LifeEventType.DESTROY
			// /*
			// * || lifeEvent.getType() == LifeEventType.CREATE
			// */) {
			// initializer.addEvent(lifeEvent);
			// }
			// }
			// }
		}
		drawableSet = initializer.createDrawableSet(stringBounder);
		final List<Newpage> newpages = new ArrayList<Newpage>();
		for (Event ev : drawableSet.getAllEvents()) {
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(stringBounder, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox(), sequenceDiagram.getTitle().getDisplay())
				.getPages();
	}

	public int getNbPages() {
		return pages.size();
	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			Display title) {

		final double headerHeight = drawableSet.getHeadHeight(stringBounder);
		final double tailHeight = drawableSet.getTailHeight(stringBounder, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin().createComponentNewPage(drawableSet.getSkinParam())
				.getPreferredHeight(stringBounder);

		return new PageSplitter(fullDimension.getHeight(), headerHeight, positions, tailHeight, signatureHeight,
				newpageHeight, title);
	}

	public ImageData createOne(OutputStream os, final int index, boolean isWithMetadata) throws IOException {

		final Page page = pages.get(index);
		final SequenceDiagramArea area = new SequenceDiagramArea(fullDimension.getWidth(), page.getHeight());

		final TextBlock compTitle;
		final AnnotatedWorker annotatedWorker = new AnnotatedWorker(diagram, diagram.getSkinParam(), stringBounder);
		final TextBlock caption = annotatedWorker.getCaption();
		area.setCaptionArea(caption.calculateDimension(stringBounder));

		if (Display.isNull(page.getTitle())) {
			compTitle = null;
		} else {
			if (SkinParam.USE_STYLES()) {
				final Style style = StyleSignature.of(SName.root, SName.title)
						.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
				compTitle = style.createTextBlockBordered(page.getTitle(), diagram.getSkinParam().getIHtmlColorSet(),
						diagram.getSkinParam());
			} else {
				compTitle = TextBlockUtils.withMargin(
						TextBlockUtils.title(new FontConfiguration(drawableSet.getSkinParam(), FontParam.TITLE, null),
								page.getTitle(), drawableSet.getSkinParam()),
						7, 7);
			}
			final Dimension2D dimTitle = compTitle.calculateDimension(stringBounder);
			area.setTitleArea(dimTitle.getWidth(), dimTitle.getHeight());
		}
		area.initFooter(getPngTitler(FontParam.FOOTER, index), stringBounder);
		area.initHeader(getPngTitler(FontParam.HEADER, index), stringBounder);

		final DisplayPositionned legend = diagram.getLegend();
		final TextBlock legendBlock;
		if (legend.isNull()) {
			legendBlock = TextBlockUtils.empty(0, 0);
		} else {
			if (SkinParam.USE_STYLES()) {
				final Style style = StyleSignature.of(SName.root, SName.legend)
						.getMergedStyle(diagram.getSkinParam().getCurrentStyleBuilder());
				legendBlock = style.createTextBlockBordered(legend.getDisplay(),
						diagram.getSkinParam().getIHtmlColorSet(), diagram.getSkinParam());
			} else {
				legendBlock = EntityImageLegend.create(legend.getDisplay(), diagram.getSkinParam());
			}
		}
		final Dimension2D dimLegend = legendBlock.calculateDimension(stringBounder);

		scale = getScale(area.getWidth(), area.getHeight());

		final double dpiFactor = diagram.getScaleCoef(fileFormatOption);
		// System.err.println("dpiFactor=" + dpiFactor);
		// System.err.println("scale=" + scale);

		final String metadata = fileFormatOption.isWithMetadata() ? diagram.getMetadata() : null;

		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(3);
			margin2 = SkinParam.zeroMargin(10);
		} else {
			margin1 = 3;
			margin2 = 10;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildD(diagram.getSkinParam(), ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2), diagram.getAnimation(), metadata,
		null, oneOf(scale, dpiFactor));

		imageBuilder.setUDrawable(new UDrawable() {
			public void drawU(UGraphic ug) {

				double delta = 0;
				if (index > 0) {
					delta = page.getNewpage1() - page.getHeaderHeight();
				}
				if (delta < 0) {
					delta = 0;
				}

				double legendYdelta = 0;
				if (compTitle != null) {
					final StringBounder stringBounder = ug.getStringBounder();
					final double h = compTitle.calculateDimension(stringBounder).getHeight();
					legendYdelta += h;
					compTitle.drawU(ug.apply(new UTranslate(area.getTitleX(), area.getTitleY())));
				}
				legendYdelta += area.getHeaderHeightMargin();
				caption.drawU(ug.apply(new UTranslate(area.getCaptionX(), area.getCaptionY())));

				final double delta1 = Math.max(0, dimLegend.getWidth() - area.getWidth());

				final boolean legendTop = legend.isNull() == false
						&& legend.getVerticalAlignment() == VerticalAlignment.TOP;

				double sequenceAreaY = area.getSequenceAreaY();
				if (legendTop) {
					sequenceAreaY += legendBlock.calculateDimension(ug.getStringBounder()).getHeight();
				}
				final UTranslate forCore = new UTranslate(area.getSequenceAreaX() + delta1 / 2, sequenceAreaY);
				TextBlock core = drawableSet.asTextBlock(delta, fullDimension.getWidth(), page,
						diagram.isShowFootbox());
				core = annotatedWorker.addFrame(core);
				core.drawU(ug.apply(forCore));
				// drawableSet.drawU22(ug.apply(forCore), delta, fullDimension.getWidth(), page,
				// diagram.isShowFootbox());

				drawHeader(area, ug, index);
				drawFooter(area, ug, index, dimLegend.getHeight());

				if (legend.isNull() == false) {
					final double delta2;
					if (legend.getHorizontalAlignment() == HorizontalAlignment.LEFT) {
						delta2 = 0;
					} else if (legend.getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
						delta2 = Math.max(0, area.getWidth() - dimLegend.getWidth());
					} else {
						delta2 = Math.max(0, area.getWidth() - dimLegend.getWidth()) / 2;
					}
					legendBlock.drawU(ug.apply(new UTranslate(delta2, legendTop ? legendYdelta : area.getHeight())));
				}

			}
		});
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, diagram.seed(), os);
	}

	private void drawFooter(SequenceDiagramArea area, UGraphic ug, int page, double legendHeight) {
		final PngTitler pngTitler = getPngTitler(FontParam.FOOTER, page);
		final TextBlock text = pngTitler.getRibbonBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug.apply(new UTranslate(area.getFooterX(diagram.getFooter().getHorizontalAlignment()),
				area.getFooterY() + legendHeight)));
	}

	private void drawHeader(SequenceDiagramArea area, UGraphic ug, int page) {
		final PngTitler pngTitler = getPngTitler(FontParam.HEADER, page);
		final TextBlock text = pngTitler.getRibbonBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug.apply(
				new UTranslate(area.getHeaderX(diagram.getHeader().getHorizontalAlignment()), area.getHeaderY())));
	}

	private double oneOf(double a, double b) {
		if (a == 1) {
			return b;
		}
		return a;
	}

	private double getImageWidth(SequenceDiagramArea area, double dpiFactor, double legendWidth) {
		final int minsize = diagram.getMinwidth();
		final double w = Math.max(area.getWidth() * getScale(area.getWidth(), area.getHeight()) * dpiFactor,
				legendWidth);
		if (minsize == Integer.MAX_VALUE) {
			return w;
		}
		if (w >= minsize) {
			return w;
		}
		return minsize;
	}

	private double getScale(double width, double height) {
		if (diagram.getScale() == null) {
			return 1;
		}
		return diagram.getScale().getScale(width, height);
	}

	private PngTitler getPngTitler(final FontParam fontParam, int page) {
		final ISkinParam skinParam = diagram.getSkinParam();
		final HColor hyperlinkColor = skinParam.getHyperlinkColor();
		final HColor titleColor = skinParam.getFontHtmlColor(null, fontParam);
		final String fontFamily = skinParam.getFont(null, false, fontParam).getFamily(null);
		final int fontSize = skinParam.getFont(null, false, fontParam).getSize();
		final DisplaySection display = diagram.getFooterOrHeaderTeoz(fontParam).withPage(page + 1, pages.size());
		Style style = null;
		if (SkinParam.USE_STYLES()) {
			final StyleSignature def = fontParam.getStyleDefinition();
			style = def.getMergedStyle(skinParam.getCurrentStyleBuilder());
		}
		return new PngTitler(titleColor, display, fontSize, fontFamily, hyperlinkColor,
				skinParam.useUnderlineForHyperlink(), style, skinParam.getIHtmlColorSet(), skinParam);
	}

}
