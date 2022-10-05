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
package net.sourceforge.plantuml.ebnf;

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileOneOrMore extends ETile {

	private final double deltax = 15;
	private final double deltay = 12;
	private final ETile orig;

	public ETileOneOrMore(ETile orig) {
		this.orig = orig;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return deltay + orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public void drawU(UGraphic ug) {
		final XDimension2D fullDim = calculateDimension(ug.getStringBounder());
		if (TRACE)
			ug.apply(HColors.RED).draw(new URectangle(fullDim));

		final double linePos = getH1(ug.getStringBounder());
		CornerCurved.createSW(8).drawU(ug.apply(new UTranslate(8, linePos)));
		drawVline(ug, 8, 8 + 5, linePos - 8);
		CornerCurved.createNW(8).drawU(ug.apply(new UTranslate(8, 5)));

		drawHlineAntiDirected(ug, 5, deltax, fullDim.getWidth() - deltax, 0.6);

		CornerCurved.createSE(8).drawU(ug.apply(new UTranslate(fullDim.getWidth() - 8, linePos)));
		drawVline(ug, fullDim.getWidth() - 8, 8 + 5, linePos - 8);
		CornerCurved.createNE(8).drawU(ug.apply(new UTranslate(fullDim.getWidth() - 8, 5)));

		drawHline(ug, linePos, 0, deltax);
		drawHline(ug, linePos, fullDim.getWidth() - deltax, fullDim.getWidth());

		orig.drawU(ug.apply(new UTranslate(deltax, deltay)));
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
