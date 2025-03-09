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
package net.sourceforge.plantuml.klimt.drawing;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Stencil;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UHorizontalLine;

public class UGraphicStencil extends AbstractUGraphicHorizontalLine {
    // ::remove file when __HAXE__

	private final Stencil stencil;
	private final UStroke defaultStroke;

	public static UGraphic create(UGraphic ug, Stencil stencil, UStroke defaultStroke) {
		return new UGraphicStencil(ug, stencil, defaultStroke);
	}

	public static UGraphic create(UGraphic ug, XDimension2D dim) {
		return new UGraphicStencil(ug, getRectangleStencil(dim), UStroke.simple());
	}

	private static Stencil getRectangleStencil(final XDimension2D dim) {
		return new Stencil() {
			public double getStartingX(StringBounder stringBounder, double y) {
				return 0;
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				return dim.getWidth();
			}
		};
	}

	private UGraphicStencil(UGraphic ug, Stencil stencil, UStroke defaultStroke) {
		super(ug);
		this.stencil = stencil;
		this.defaultStroke = defaultStroke;
	}

	@Override
	protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
		return new UGraphicStencil(ug, stencil, defaultStroke);
	}

	@Override
	protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
		line.drawLineInternal(ug, stencil, translate.getDy(), defaultStroke);
		// final UDrawable ud = stencil.convert(line, ug.getStringBounder());
		// ud.drawU(ug.apply(translate));
		// line.drawLine(ug.apply(translate), startingX, endingX, 0, defaultStroke);
	}

}
