/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 9591 $
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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
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
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SequenceDiagramFileMakerPuma2 implements FileMaker {

	private static final StringBounder dummyStringBounder = TextBlockUtils.getDummyStringBounder();

	private final SequenceDiagram diagram;
	private final DrawableSet drawableSet;
	private final Dimension2D fullDimension;
	private final List<Page> pages;
	private final FileFormatOption fileFormatOption;

	private double scale;

	public SequenceDiagramFileMakerPuma2(SequenceDiagram sequenceDiagram, Skin skin, FileFormatOption fileFormatOption) {
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		final DrawableSetInitializer initializer = new DrawableSetInitializer(skin, sequenceDiagram.getSkinParam(),
				sequenceDiagram.isShowFootbox(), sequenceDiagram.getAutonewpage());

		for (Participant p : sequenceDiagram.participants().values()) {
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
		drawableSet = initializer.createDrawableSet(dummyStringBounder);
		final List<Newpage> newpages = new ArrayList<Newpage>();
		for (Event ev : drawableSet.getAllEvents()) {
			if (ev instanceof Newpage) {
				newpages.add((Newpage) ev);
			}
		}
		fullDimension = drawableSet.getDimension();
		final Map<Newpage, Double> positions = new LinkedHashMap<Newpage, Double>();
		for (Newpage n : newpages) {
			positions.put(n, initializer.getYposition(dummyStringBounder, n));
		}
		pages = create(drawableSet, positions, sequenceDiagram.isShowFootbox(), sequenceDiagram.getTitle()).getPages();
	}

	public int getNbPages() {
		return pages.size();
	}

	private PageSplitter create(DrawableSet drawableSet, Map<Newpage, Double> positions, boolean showFootbox,
			Display title) {

		final double headerHeight = drawableSet.getHeadHeight(dummyStringBounder);
		final double tailHeight = drawableSet.getTailHeight(dummyStringBounder, showFootbox);
		final double signatureHeight = 0;
		final double newpageHeight = drawableSet.getSkin()
				.createComponent(ComponentType.NEWPAGE, null, drawableSet.getSkinParam(), Display.create(""))
				.getPreferredHeight(dummyStringBounder);

		return new PageSplitter(fullDimension.getHeight(), headerHeight, positions, tailHeight, signatureHeight,
				newpageHeight, title);
	}

	public ImageData createOne(OutputStream os, final int index, boolean isWithMetadata) throws IOException {

		final Page page = pages.get(index);
		final SequenceDiagramArea area = new SequenceDiagramArea(fullDimension.getWidth(), page.getHeight());

		final Component compTitle;
		if (page.getTitle() == null) {
			compTitle = null;
		} else {
			compTitle = drawableSet.getSkin().createComponent(ComponentType.TITLE, null, drawableSet.getSkinParam(),
					page.getTitle());
			area.setTitleArea(compTitle.getPreferredWidth(dummyStringBounder),
					compTitle.getPreferredHeight(dummyStringBounder));
		}
		addFooter2(area);
		addHeader2(area);

		// final FileFormat fileFormat = fileFormatOption.getFileFormat();

		final Display legend = diagram.getLegend();
		final TextBlock legendBlock;
		if (legend == null) {
			legendBlock = TextBlockUtils.empty(0, 0);
		} else {
			legendBlock = EntityImageLegend.create(legend, diagram.getSkinParam());
		}
		final Dimension2D dimLegend = TextBlockUtils.getDimension(legendBlock);

		scale = getScale(area.getWidth(), area.getHeight());

		final double dpiFactor = diagram.getDpiFactor(fileFormatOption);
		// System.err.println("dpiFactor=" + dpiFactor);
		// System.err.println("scale=" + scale);

		final ImageBuilder imageBuilder = new ImageBuilder(diagram.getSkinParam().getColorMapper(), oneOf(scale,
				dpiFactor), diagram.getSkinParam().getBackgroundColor(), null, null, 3, 10, diagram.getAnimation(),
				diagram.getSkinParam().handwritten());

		imageBuilder.addUDrawable(new UDrawable() {
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
					final double h = compTitle.getPreferredHeight(stringBounder);
					legendYdelta += h;
					final double w = compTitle.getPreferredWidth(stringBounder);
					compTitle.drawU(ug.apply(new UTranslate(area.getTitleX(), area.getTitleY())), new Area(
							new Dimension2DDouble(w, h)), new SimpleContext2D(false));
				}

				final double delta1 = Math.max(0, dimLegend.getWidth() - area.getWidth());

				final boolean legendTop = legend != null
						&& diagram.getLegendVerticalAlignment() == VerticalAlignment.TOP;

				double sequenceAreaY = area.getSequenceAreaY();
				if (legendTop) {
					sequenceAreaY += legendBlock.calculateDimension(ug.getStringBounder()).getHeight();
				}
				drawableSet.drawU22(ug.apply(new UTranslate(area.getSequenceAreaX() + delta1 / 2, sequenceAreaY)),
						delta, fullDimension.getWidth(), page, diagram.isShowFootbox());

				addHeader3(area, ug);
				addFooter3(area, ug);

				if (legend != null) {
					final double delta2;
					if (diagram.getLegendAlignment() == HorizontalAlignment.LEFT) {
						delta2 = 0;
					} else if (diagram.getLegendAlignment() == HorizontalAlignment.RIGHT) {
						delta2 = Math.max(0, area.getWidth() - dimLegend.getWidth());
					} else {
						delta2 = Math.max(0, area.getWidth() - dimLegend.getWidth()) / 2;
					}
					legendBlock.drawU(ug.apply(new UTranslate(delta2, legendTop ? legendYdelta : legendYdelta
							+ area.getHeight())));
				}

			}
		});
		return imageBuilder.writeImageTOBEMOVED(fileFormatOption.getFileFormat(), os);
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

	private void addFooter2(SequenceDiagramArea area) {
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily,
				diagram.getFooterAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setFooterArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addHeader2(SequenceDiagramArea area) {
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily,
				diagram.getHeaderAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		final Dimension2D dim = pngTitler.getTextDimension(dummyStringBounder);
		if (dim != null) {
			area.setHeaderArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	private void addFooter3(SequenceDiagramArea area, UGraphic ug) {
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily,
				diagram.getFooterAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug.apply(new UTranslate(area.getFooterX(diagram.getFooterAlignment()), area.getFooterY())));
	}

	private void addHeader3(SequenceDiagramArea area, UGraphic ug) {
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily,
				diagram.getHeaderAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		final TextBlock text = pngTitler.getTextBlock();
		if (text == null) {
			return;
		}
		text.drawU(ug.apply(new UTranslate(area.getHeaderX(diagram.getHeaderAlignment()), area.getHeaderY())));
	}

}
