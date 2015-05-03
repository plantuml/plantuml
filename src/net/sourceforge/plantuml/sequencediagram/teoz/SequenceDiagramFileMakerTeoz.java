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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.VerticalAlignment;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.utils.MathUtils;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormatOption fileFormatOption;
	private final Skin skin;

	public SequenceDiagramFileMakerTeoz(SequenceDiagram sequenceDiagram, Skin skin, FileFormatOption fileFormatOption) {
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		this.skin = skin;

	}

	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();

		final ISkinParam skinParam = diagram.getSkinParam();

		final Real origin = RealUtils.createOrigin();
		Real currentPos = origin.addAtLeast(0);
		LivingSpace last = null;
		LivingSpaces livingSpaces = new LivingSpaces();
		for (Participant p : diagram.participants().values()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, skinParam, currentPos,
					diagram.events());
			last = livingSpace;
			((LivingSpaces) livingSpaces).put(p, livingSpace);
			currentPos = livingSpace.getPosD(stringBounder).addAtLeast(0);
		}

		final MainTile mainTile = new MainTile(diagram, skin, last.getPosD(stringBounder).addAtLeast(0), livingSpaces,
				origin);
		mainTile.addConstraints(stringBounder);
		origin.compile();

		final double mainHeight = mainTile.getPreferredHeight(stringBounder) + 2
				* livingSpaces.getHeadHeight(stringBounder);

		final Real min1 = mainTile.getMinX(stringBounder);
		final Real max1 = mainTile.getMaxX(stringBounder);
		// System.err.println("min1=" + min1.getCurrentValue());
		// System.err.println("max1=" + max1.getCurrentValue());

		final Component compTitle = getCompTitle();

		final double mainWidth = max1.getCurrentValue() - min1.getCurrentValue();

		Dimension2D dimTitle = new Dimension2DDouble(0, 0);
		if (compTitle != null) {
			dimTitle = compTitle.getPreferredDimension(stringBounder);
		}

		final PngTitler footer = getFooter();
		Dimension2D dimFooter = new Dimension2DDouble(0, 0);
		if (footer != null && footer.getTextBlock() != null) {
			dimFooter = footer.getTextBlock().calculateDimension(stringBounder);
		}

		final PngTitler header = getHeader();
		Dimension2D dimHeader = new Dimension2DDouble(0, 0);
		if (header != null && header.getTextBlock() != null) {
			dimHeader = header.getTextBlock().calculateDimension(stringBounder);
		}

		final double totalWidth = MathUtils.max(mainWidth, dimTitle.getWidth(), dimFooter.getWidth(),
				dimHeader.getWidth());
		final double totalHeight = mainHeight + dimTitle.getHeight() + dimHeader.getHeight() + dimFooter.getHeight();
		final Dimension2D dim = new Dimension2DDouble(totalWidth, totalHeight);
		final UGraphic2 ug2 = (UGraphic2) fileFormatOption.createUGraphic(skinParam.getColorMapper(),
				diagram.getDpiFactor(fileFormatOption), dim, skinParam.getBackgroundColor(), false).apply(
				new UTranslate(-min1.getCurrentValue(), 0));

		UGraphic ug = diagram.getSkinParam().handwritten() ? new UGraphicHandwritten(ug2) : ug2;

		if (footer != null && footer.getTextBlock() != null) {
			double dx = 0;
			if (diagram.getFooterAlignment() == HorizontalAlignment.RIGHT) {
				dx = totalWidth - dimFooter.getWidth();
			} else if (diagram.getFooterAlignment() == HorizontalAlignment.CENTER) {
				dx = (totalWidth - dimFooter.getWidth()) / 2;
			}
			footer.getTextBlock().drawU(
					ug.apply(new UTranslate(dx, mainHeight + dimTitle.getHeight() + dimHeader.getHeight())));
		}
		if (header != null && header.getTextBlock() != null) {
			double dx = 0;
			if (diagram.getHeaderAlignment() == HorizontalAlignment.RIGHT) {
				dx = totalWidth - dimHeader.getWidth();
			} else if (diagram.getHeaderAlignment() == HorizontalAlignment.CENTER) {
				dx = (totalWidth - dimHeader.getWidth()) / 2;
			}
			header.getTextBlock().drawU(ug.apply(new UTranslate(dx, 0)));
		}
		if (compTitle != null) {
			compTitle.drawU(ug.apply(new UTranslate((totalWidth - dimTitle.getWidth()) / 2, 0)), new Area(dimTitle),
					new SimpleContext2D(false));
			ug = ug.apply(new UTranslate((totalWidth - mainWidth) / 2, dimTitle.getHeight() + dimHeader.getHeight()));
		}

		drawMainTile(ug, mainTile, livingSpaces);

		ug2.writeImageTOBEMOVED(os, isWithMetadata ? diagram.getMetadata() : null, diagram.getDpi(fileFormatOption));
		final Dimension2D info = new Dimension2DDouble(dim.getWidth(), dim.getHeight());

		// if (fileFormatOption.getFileFormat() == FileFormat.PNG && ug instanceof UGraphicG2d) {
		// final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
		// if (urls.size() > 0) {
		// if (scale == 0) {
		// throw new IllegalStateException();
		// }
		// final CMapData cmap = CMapData.cmapString(urls, scale);
		// return new ImageDataComplex(info, cmap, null);
		// }
		// }
		return new ImageDataSimple(info);
	}

	private Component getCompTitle() {
		final Display title = diagram.getTitle();

		final Component compTitle;
		if (title == null) {
			compTitle = null;
		} else {
			compTitle = skin.createComponent(ComponentType.TITLE, null, diagram.getSkinParam(), title);
		}
		return compTitle;
	}

	private PngTitler getFooter() {
		if (diagram.getFooter() == null) {
			return null;
		}
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.FOOTER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.FOOTER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getFooter(), fontSize, fontFamily,
				diagram.getFooterAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		return pngTitler;
	}

	private PngTitler getHeader() {
		final HtmlColor hyperlinkColor = diagram.getSkinParam().getHyperlinkColor();
		final HtmlColor titleColor = diagram.getSkinParam().getFontHtmlColor(FontParam.HEADER, null);
		final String fontFamily = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getFamily(null);
		final int fontSize = diagram.getSkinParam().getFont(FontParam.HEADER, null, false).getSize();
		final PngTitler pngTitler = new PngTitler(titleColor, diagram.getHeader(), fontSize, fontFamily,
				diagram.getHeaderAlignment(), hyperlinkColor, diagram.getSkinParam().useUnderlineForHyperlink());
		return pngTitler;
	}

	private void drawMainTile(final UGraphic ug, final MainTile mainTile, LivingSpaces livingSpaces) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);

		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		mainTile.drawU(ug.apply(new UTranslate(0, headHeight)));
		livingSpaces.drawLifeLines(ug.apply(new UTranslate(0, headHeight)), mainTile.getPreferredHeight(stringBounder),
				context);
		livingSpaces.drawHeads(ug.apply(new UTranslate(0, mainTile.getPreferredHeight(stringBounder) + headHeight)),
				context, VerticalAlignment.TOP);
		mainTile.drawForeground(ug.apply(new UTranslate(0, headHeight)));
	}

	public int getNbPages() {
		return 1;
	}

}
