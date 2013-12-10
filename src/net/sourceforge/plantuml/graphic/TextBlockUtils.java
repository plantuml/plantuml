/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 11995 $
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
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.posimo.Positionable;
import net.sourceforge.plantuml.posimo.PositionableImpl;
import net.sourceforge.plantuml.sequencediagram.MessageNumber;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TextBlockUtils {

	public static TextBlock create(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer) {
		if (texts == null) {
			return empty(0, 0);
		}
		return create(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0);
	}

	public static TextBlock create(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer, double maxMessageSize) {
		if (texts.size() > 0) {
			if (texts.get(0) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0);
			}
			if (texts.get(texts.size() - 1) instanceof Stereotype) {
				return createStereotype(texts, fontConfiguration, horizontalAlignment, spriteContainer,
						texts.size() - 1);
			}
			if (texts.get(0) instanceof MessageNumber) {
				return createMessageNumber(texts, fontConfiguration, horizontalAlignment, spriteContainer,
						maxMessageSize);
			}
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignment, spriteContainer, maxMessageSize);
	}

	private static TextBlock createMessageNumber(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer, double maxMessageSize) {
		final MessageNumber number = (MessageNumber) texts.get(0);
		return new TextBlockWithNumber(number.getNumber(), texts.subList(1, texts.size()), fontConfiguration,
				horizontalAlignment, spriteContainer, maxMessageSize);

	}

	private static TextBlock createStereotype(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignment horizontalAlignment, SpriteContainer spriteContainer, int position) {
		final Stereotype stereotype = (Stereotype) texts.get(position);
		if (stereotype.isSpotted()) {
			final CircledCharacter circledCharacter = new CircledCharacter(stereotype.getCharacter(),
					stereotype.getRadius(), stereotype.getCircledFont(), stereotype.getHtmlColor(), null,
					fontConfiguration.getColor());
			if (stereotype.getLabel() == null) {
				return new TextBlockSpotted(circledCharacter, texts.subList(1, texts.size()), fontConfiguration,
						horizontalAlignment, spriteContainer);
			}
			return new TextBlockSpotted(circledCharacter, texts, fontConfiguration, horizontalAlignment,
					spriteContainer);
		}
		return new TextBlockSimple(texts, fontConfiguration, horizontalAlignment, spriteContainer, 0);
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
		return new TextBlock() {
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

	public static UGraphic getPrinted(TextBlock tb, FileFormatOption fileFormatOption, ColorMapper colorMapper,
			double dpiFactor, HtmlColor mybackcolor, double margin) {
		final MinMax minmax = getMinMax(tb, dummyStringBounder);
		final UGraphic ug = fileFormatOption.createUGraphic(colorMapper, dpiFactor,
				Dimension2DDouble.delta(minmax.getDimension(), 2 * margin), mybackcolor, false);
		final double dx = -minmax.getMinX() + margin;
		final double dy = -minmax.getMinY() + margin;
		tb.drawU(ug.apply(new UTranslate(dx, dy)));
		return ug;
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

}
