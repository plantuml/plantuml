/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.klimt.shape;

import net.atmp.InnerStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.LimitFinder;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.Positionable;
import net.sourceforge.plantuml.klimt.geom.PositionableImpl;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;

public class TextBlockUtils {
	// ::remove file when __HAXE__

	public static final TextBlock EMPTY_TEXT_BLOCK = TextBlockUtils.empty(0, 0);

	public static TextBlock bordered(TextBlock textBlock, UStroke stroke, HColor borderColor, HColor backgroundColor,
			double cornersize, ClockwiseTopRightBottomLeft margins, String id) {
		return new TextBlockBordered(textBlock, stroke, borderColor, backgroundColor, cornersize, margins, id);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX, double marginY) {
		if (marginX == 0 && marginY == 0)
			return textBlock;

		return new TextBlockMarged(textBlock, marginY, marginX, marginY, marginX);
	}

	public static TextBlock withMargin(TextBlock textBlock, ClockwiseTopRightBottomLeft margins) {
		return new TextBlockMarged(textBlock, margins);
	}

	public static TextBlock withMargin(TextBlock textBlock, double marginX1, double marginX2, double marginY1,
			double marginY2) {
		return new TextBlockMarged(textBlock, marginY1, marginX2, marginY2, marginX1);
	}

	public static TextBlock withMinWidth(TextBlock textBlock, double minWidth,
			HorizontalAlignment horizontalAlignment) {
		return new TextBlockMinWidth(textBlock, minWidth, horizontalAlignment);
	}

	public static TextBlock empty(final double width, final double height) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(width, height);
			}

			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

		};
	}

	public static Positionable asPositionable(TextBlock textBlock, StringBounder stringBounder, XPoint2D pt) {
		return PositionableImpl.create(pt, textBlock.calculateDimension(stringBounder));
	}

	public static Positionable asPositionable(XDimension2D dim, StringBounder stringBounder, XPoint2D pt) {
		return PositionableImpl.create(pt, dim);
	}

	public static TextBlock mergeLR(TextBlock b1, TextBlock b2, VerticalAlignment verticallAlignment) {
		if (b1 == EMPTY_TEXT_BLOCK)
			return b2;

		if (b2 == EMPTY_TEXT_BLOCK)
			return b1;

		return new TextBlockHorizontal(b1, b2, verticallAlignment);
	}

	public static TextBlock mergeTB(TextBlock b1, TextBlock b2, HorizontalAlignment horizontalAlignment) {
		if (b1 == EMPTY_TEXT_BLOCK)
			return b2;

		if (b2 == EMPTY_TEXT_BLOCK)
			return b1;

		return new TextBlockVertical2(b1, b2, horizontalAlignment);
	}

	public static TextBlock mergeTB(TextBlock b1, UImage image, HorizontalAlignment horizontalAlignment) {
		if (b1 == EMPTY_TEXT_BLOCK)
			throw new IllegalArgumentException();

		return new TextBlockVertical2(b1, image, horizontalAlignment);
	}

	// public static TextBlockBackcolored mergeColoredTB(TextBlockBackcolored b1,
	// TextBlockBackcolored b2,
	// HorizontalAlignment horizontalAlignment) {
	// return addBackcolor(mergeTB(b1, b2, horizontalAlignment), b1.getBackcolor());
	// }

	public static MinMax getMinMax(UDrawable tb, StringBounder stringBounder, boolean initToZero) {
		final LimitFinder limitFinder = LimitFinder.create(stringBounder, initToZero);
		tb.drawU(limitFinder);
		return limitFinder.getMinMax();
	}

	public static boolean isEmpty(TextBlock text, StringBounder dummyStringBounder) {
		if (text == null || text == EMPTY_TEXT_BLOCK)
			return true;

		final XDimension2D dim = text.calculateDimension(dummyStringBounder);
		return dim.getHeight() == 0 && dim.getWidth() == 0;
	}

	public static TextBlock fullInnerPosition(final TextBlock bloc, final String display) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				bloc.drawU(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return bloc.calculateDimension(stringBounder);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return bloc.getMinMax(stringBounder);
			}

			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				if (strategy.check(display, member)) {
					final XDimension2D dim = calculateDimension(stringBounder);
					return new XRectangle2D(0, 0, dim.getWidth(), dim.getHeight());
				}
				return null;
			}

			public MagneticBorder getMagneticBorder() {
				return bloc.getMagneticBorder();
			}

			public HColor getBackcolor() {
				return bloc.getBackcolor();
			}

		};
	}

	public static TextBlock addBackcolor(final TextBlock text, final HColor backColor) {
		return new TextBlock() {
			public void drawU(UGraphic ug) {
				text.drawU(ug);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return text.getMinMax(stringBounder);
			}

			public XRectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return text.getInnerPosition(member, stringBounder, strategy);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return text.calculateDimension(stringBounder);
			}

			public HColor getBackcolor() {
				return backColor;
			}

			public MagneticBorder getMagneticBorder() {
				return text.getMagneticBorder();
			}
		};
	}

	public static TextBlock fromUImage(final UImage image) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				ug.draw(image);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(image.getWidth(), image.getHeight());
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return MinMax.fromMax(image.getWidth(), image.getHeight());
			}

		};
	}

}
