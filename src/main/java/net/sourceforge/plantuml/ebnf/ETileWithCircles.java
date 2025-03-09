/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.klimt.CopyForegroundColorToBackgroundColor;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UEllipse;

public class ETileWithCircles extends ETile {

	private static final double SIZE = 8;

	private final double deltax = 30;
	private final ETile orig;
	private final HColor lineColor;

	public ETileWithCircles(ETile orig, HColor lineColor) {
		this.orig = orig;
		this.lineColor = lineColor;
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		final double linePos = getH1(ug.getStringBounder());
		final XDimension2D fullDim = calculateDimension(ug.getStringBounder());
		ug = ug.apply(lineColor).apply(UStroke.withThickness(1.5));
		orig.drawU(ug.apply(UTranslate.dx(deltax)));

		final UEllipse circle = UEllipse.build(SIZE, SIZE);

		ug.apply(UStroke.withThickness(2)).apply(new UTranslate(0, linePos - SIZE / 2)).draw(circle);
		ug.apply(UStroke.withThickness(1)).apply(new CopyForegroundColorToBackgroundColor())
				.apply(new UTranslate(fullDim.getWidth() - SIZE / 2, linePos - SIZE / 2)).draw(circle);

		ug = ug.apply(UStroke.withThickness(1.5));
		drawHlineDirected(ug, linePos, SIZE, deltax, 0.5, 25);
		drawHlineDirected(ug, linePos, fullDim.getWidth() - deltax, fullDim.getWidth() - SIZE / 2, 0.5, 25);
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
