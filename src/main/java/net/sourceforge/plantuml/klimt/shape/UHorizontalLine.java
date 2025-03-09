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

import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class UHorizontalLine implements UShape {
    // ::remove file when __HAXE__

	private final double skipAtStart;
	private final double skipAtEnd;
	private final TextBlock title;
	private final boolean blankTitle;
	private final char style;
	private final double defaultThickness;

	private UHorizontalLine(double defaultThickness, double skipAtStart, double skipAtEnd, TextBlock title,
			boolean blankTitle, char style) {
		this.defaultThickness = defaultThickness;
		this.skipAtEnd = skipAtEnd;
		this.skipAtStart = skipAtStart;
		this.title = title;
		this.blankTitle = blankTitle;
		this.style = style;
	}

	public static UHorizontalLine infinite(double defaultThickness, double skipAtStart, double skipAtEnd, char style) {
		return new UHorizontalLine(defaultThickness, skipAtStart, skipAtEnd, null, false, style);
	}

	public static UHorizontalLine infinite(double defaultThickness, double skipAtStart, double skipAtEnd,
			TextBlock title, char style) {
		return new UHorizontalLine(defaultThickness, skipAtStart, skipAtEnd, title, false, style);
	}

	public boolean isDouble() {
		return style == '=';
	}

	// static public UHorizontalLine infinite(UStroke stroke) {
	// return new UHorizontalLine(0, 0, null, false, stroke);
	// }

	public void drawLineInternal(final UGraphic ug, Stencil stencil, double y, UStroke defaultStroke) {
		stencil = addSkip(stencil);
		final UStroke strokeToUse = style == '\0' ? defaultStroke : getStroke();
		final UGraphic ugStroke = ug.apply(strokeToUse);
		if (title == null) {
			drawHLine(stencil, y, ugStroke);
			return;
		}
		final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		drawHLine(firstHalf(stencil, dimTitle.getWidth()), y, ugStroke);
		final double startingX = stencil.getStartingX(ug.getStringBounder(), y);
		final double endingX = stencil.getEndingX(ug.getStringBounder(), y);
		drawTitleInternal(ug, startingX, endingX, y, false);
		drawHLine(secondHalf(stencil, dimTitle.getWidth()), y, ugStroke);
	}

	private Stencil addSkip(final Stencil stencil) {
		return new Stencil() {
			public double getStartingX(StringBounder stringBounder, double y) {
				return stencil.getStartingX(stringBounder, y) + skipAtStart;
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				return stencil.getEndingX(stringBounder, y) - skipAtEnd;
			}
		};
	}

	private static Stencil firstHalf(final Stencil stencil, final double widthTitle) {
		return new Stencil() {
			public double getStartingX(StringBounder stringBounder, double y) {
				return stencil.getStartingX(stringBounder, y);
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				final double start = stencil.getStartingX(stringBounder, y);
				final double end = stencil.getEndingX(stringBounder, y);
				final double len = (end - start - widthTitle) / 2;
				return start + len;
			}
		};
	}

	private static Stencil secondHalf(final Stencil stencil, final double widthTitle) {
		return new Stencil() {
			public double getStartingX(StringBounder stringBounder, double y) {
				final double start = stencil.getStartingX(stringBounder, y);
				final double end = stencil.getEndingX(stringBounder, y);
				final double len = (end - start - widthTitle) / 2;
				return end - len;
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				return stencil.getEndingX(stringBounder, y);
			}
		};
	}

	private void drawHLine(Stencil stencil, double y, final UGraphic ug) {
		drawSimpleHline(ug, stencil, y);
		if (style == '=')
			drawSimpleHline(ug, stencil, y + 2);

	}

	private static void drawSimpleHline(UGraphic ug, Stencil stencil, double y) {
		final double startingX = stencil.getStartingX(ug.getStringBounder(), y);
		final double endingX = stencil.getEndingX(ug.getStringBounder(), y);
		ug.apply(new UTranslate(startingX, y)).draw(ULine.hline(endingX - startingX));
	}

	public void drawTitleInternal(UGraphic ug, double startingX, double endingX, double y, boolean clearArea) {
		if (title == null || blankTitle) {
			return;
		}
		final double widthToUse = endingX - startingX;
		final XDimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double space = (widthToUse - dimTitle.getWidth()) / 2;
		final double x1 = startingX + space;
		final double y1 = y - dimTitle.getHeight() / 2 - 0.5;
		ug = ug.apply(new UTranslate(x1, y1));
		if (clearArea) {
			ug.apply(getStroke()).draw(URectangle.build(dimTitle));
		}
		title.drawU(ug);
	}

	public void drawMe(UGraphic ug) {
		ug.draw(this);
	}

	public UStroke getStroke() {
		if (style == '\0')
			throw new IllegalStateException();
		else if (style == '=')
			return UStroke.simple();
		else if (style == '.')
			return new UStroke(1, 2, 1);
		else if (style == '-')
			return UStroke.simple();
		else
			return UStroke.withThickness(defaultThickness);

	}

}
