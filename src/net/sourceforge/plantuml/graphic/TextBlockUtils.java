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
 * Revision $Revision: 16278 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.creole.CreoleParser;
import net.sourceforge.plantuml.creole.Sheet;
import net.sourceforge.plantuml.creole.SheetBlock1;
import net.sourceforge.plantuml.creole.SheetBlock2;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.posimo.PositionableImpl;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class TextBlockUtils {

	public static TextBlock create(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer) {
		return create(texts, fontConfiguration, horizontalAlignment, spriteContainer, false);
	}

	public static TextBlock create(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer, boolean modeSimpleLine) {
		if (texts == null) {
			return empty(0, 0);
		}
		return create(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0, modeSimpleLine, null, null);
	}

	public static TextBlock create(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer, double maxMessageSize,
			boolean modeSimpleLine, UFont fontForStereotype, HtmlColor htmlColorForStereotype) {
		if (texts.getNaturalHorizontalAlignment() != null) {
			horizontalAlignment = texts.getNaturalHorizontalAlignment();
		}
		if (texts.size() > 0) {
			if (texts.get(0) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0,
						fontForStereotype, htmlColorForStereotype);
			}
			if (texts.get(texts.size() - 1) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignment, spriteContainer,
						texts.size() - 1, fontForStereotype, htmlColorForStereotype);
			}
			if (texts.get(0) instanceof MessageNumber) {
				return createMessageNumber(texts, fontConfiguration, horizontalAlignment, spriteContainer,
						maxMessageSize);
			}
		}

		return getCreole(texts, fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize, modeSimpleLine);
	}

	private static TextBlock getCreole(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer, double maxMessageSize,
			boolean modeSimpleLine) {
		final Sheet sheet = new CreoleParser(fontConfiguration, horizontalAlignment, spriteContainer, modeSimpleLine)
				.createSheet(texts);
		final SheetBlock1 sheetBlock1 = new SheetBlock1(sheet, maxMessageSize, spriteContainer == null ? 0
				: spriteContainer.getPadding());
		return new SheetBlock2(sheetBlock1, sheetBlock1, new UStroke(1.5));
	}

	private static TextBlock createMessageNumber(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, ISkinSimple spriteContainer, double maxMessageSize) {
		TextBlock tb1 = getCreole(texts.subList(0, 1), fontConfiguration, horizontalAlignment, spriteContainer,
				maxMessageSize, false);
		tb1 = TextBlockUtils.withMargin(tb1, 0, 4, 0, 0);
		final TextBlock tb2 = getCreole(texts.subList(1, texts.size()), fontConfiguration, horizontalAlignment,
				spriteContainer, maxMessageSize, false);
		return TextBlockUtils.mergeLR(tb1, tb2, VerticalAlignment.CENTER);

	}

	private static TextBlock createStereotype(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer, int position,
			UFont fontForStereotype, HtmlColor htmlColorForStereotype) {
		final Stereotype stereotype = (Stereotype) texts.get(position);
		if (stereotype.isSpotted()) {
			final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(),
					stereotype.getRadius(), stereotype.getCircledFont(), stereotype.getHtmlColor(), null,
					fontConfiguration.getColor());
			if (stereotype.getLabel(false) == null) {
				return new TextBlockSpotted(circledCharacter, texts.subList(1, texts.size()), fontConfiguration,
						horizontalAlignment, spriteContainer);
			}
			return new TextBlockSpotted(circledCharacter, texts, fontConfiguration, horizontalAlignment,
					spriteContainer);
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0,
				fontForStereotype, htmlColorForStereotype);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX, double marginY) {
		return new TextBlockMarged(textBlock, marginX, marginX, marginY, marginY);
	}

	public static TextBlock withMinWidth(TextBlock textBlock, double minWidth, HorizontalAlignment horizontalAlignment) {
		return new TextBlockMinWidth(textBlock, minWidth, horizontalAlignment);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX1, double marginX2, double marginY1,
			double marginY2) {
		return new TextBlockMarged(textBlock, marginX1, marginX2, marginY1, marginY2);
	}

	public static TextBlock empty(final double width, final double height) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

	public static Positionable asPositionable(TextBlock textBlock, StringBounder stringBounder, Point2D pt) {
		return new PositionableImpl(pt, textBlock.calculateDimension(stringBounder));
	}

	public static TextBlock mergeLR(TextBlock b1, TextBlock b2, VerticalAlignment verticallAlignment) {
		return new TextBlockHorizontal(b1, b2, verticallAlignment);
	}

	public static TextBlock mergeTB(TextBlock b1, TextBlock b2, HorizontalAlignment horizontalAlignment) {
		return new TextBlockVertical2(b1, b2, horizontalAlignment);
	}

	public static MinMax getMinMax(TextBlock tb, StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, false);
		tb.drawU(limitFinder);
		return limitFinder.getMinMax();
	}

	private static final Graphics2D gg;
	private static final StringBounder dummyStringBounder;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		gg = imDummy.createGraphics();
		dummyStringBounder = StringBounderUtils.asStringBounder(gg);
	}

	public static StringBounder getDummyStringBounder() {
		return dummyStringBounder;
	}

	public static FontRenderContext getFontRenderContext() {
		return gg.getFontRenderContext();
	}

	public static MinMax getMinMax(TextBlock tb) {
		return getMinMax(tb, dummyStringBounder);
	}

	public static Dimension2D getDimension(TextBlock tb) {
		return tb.calculateDimension(dummyStringBounder);
	}

	public static LineMetrics getLineMetrics(UFont font, String text) {
		return font.getLineMetrics(gg, text);
	}

	public static FontMetrics getFontMetrics(Font font) {
		return gg.getFontMetrics(font);
	}

	public static TextBlock fullInnerPosition(final TextBlock bloc, final String display) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				bloc.drawU(ug);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return bloc.calculateDimension(stringBounder);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder) {
				if (display.startsWith(member)) {
					final Dimension2D dim = calculateDimension(stringBounder);
					return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
				}
				return null;
			}
		};
	}

}
