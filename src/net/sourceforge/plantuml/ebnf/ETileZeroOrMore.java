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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class ETileZeroOrMore extends ETile {

	private final double deltax;
	private final double deltay = 20;
	private final ETile orig;
	private final boolean specialForAlternate;

	public ETileZeroOrMore(ETile orig) {
		this.orig = orig;
		this.specialForAlternate = orig instanceof ETileAlternation;
		this.deltax = this.specialForAlternate ? 0 : 20;
		if (this.specialForAlternate)
			((ETileAlternation) orig).setInZeroOrMore(true);
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return 10;
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return 10 + orig.getH1(stringBounder) + orig.getH2(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + 2 * deltax;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D fullDim = calculateDimension(stringBounder);
		if (TRACE) {
			if (specialForAlternate)
				ug.apply(HColors.COL_B38D22).draw(URectangle.build(fullDim));
			else
				ug.apply(HColors.GREEN).draw(URectangle.build(fullDim));
		}

		final double linePos = getH1(stringBounder);

		drawHline(ug, linePos, 0, fullDim.getWidth());
		final double corner = 12;

		if (specialForAlternate) {
			CornerCurved.createNE_arrow(corner).drawU(ug.apply(new UTranslate(corner, linePos)));
			CornerCurved.createNW(corner).drawU(ug.apply(new UTranslate(corner, linePos)));

			final double posB = fullDim.getWidth() - corner;
			CornerCurved.createNW_arrow(corner).drawU(ug.apply(new UTranslate(posB, linePos)));
			CornerCurved.createNE(corner).drawU(ug.apply(new UTranslate(posB, linePos)));

		} else {

			CornerCurved.createNE_arrow(corner).drawU(ug.apply(new UTranslate(deltax - corner, linePos)));
			CornerCurved.createNW(corner).drawU(ug.apply(new UTranslate(deltax - corner, linePos)));
			drawVline(ug, deltax - corner, linePos + corner, deltay + orig.getH1(stringBounder) - corner);
			CornerCurved.createSW(corner)
					.drawU(ug.apply(new UTranslate(deltax - corner, deltay + orig.getH1(stringBounder))));

			final double posB = fullDim.getWidth() - deltax + corner;
			CornerCurved.createSE(corner).drawU(ug.apply(new UTranslate(posB, deltay + orig.getH1(stringBounder))));
			drawVline(ug, posB, linePos + corner, deltay + orig.getH1(stringBounder) - corner);
			CornerCurved.createNW_arrow(corner).drawU(ug.apply(new UTranslate(posB, linePos)));
			CornerCurved.createNE(corner).drawU(ug.apply(new UTranslate(posB, linePos)));
		}

		orig.drawU(ug.apply(new UTranslate(deltax, deltay)));
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
