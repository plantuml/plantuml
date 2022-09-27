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
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileRepetition extends AbstractTextBlock implements ETile {

	private final double deltax = 20;
	private final double deltay = 10;
	private final ETile orig;

	public ETileRepetition(ETile orig) {
		this.orig = orig;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return XDimension2D.delta(orig.calculateDimension(stringBounder), 2 * deltax, deltay);
	}

	@Override
	public void drawU(UGraphic ug) {
		final XDimension2D fullDim = calculateDimension(ug.getStringBounder());
		ug = ug.apply(HColors.BLACK);
		final double linePos = linePos(ug.getStringBounder());

		final double posA = 7;
		final double posB = fullDim.getWidth() - 7;
		
		final double corner = 12;

		CornerCurved.createNW(corner).drawU(ug.apply(new UTranslate(posA, linePos)));
		ETileConcatenation.drawVline(ug, posA, linePos + corner, fullDim.getHeight() - 1 - corner);
		CornerCurved.createSW(corner).drawU(ug.apply(new UTranslate(posA, fullDim.getHeight() - 1)));

		CornerCurved.createNE(corner).drawU(ug.apply(new UTranslate(posB, linePos)));
		ETileConcatenation.drawVline(ug, posB, linePos + corner, fullDim.getHeight() - 1 - corner);
		CornerCurved.createSE(corner).drawU(ug.apply(new UTranslate(posB, fullDim.getHeight() - 1)));

		ETileConcatenation.drawHline(ug, fullDim.getHeight() - 1, posA + corner, posB - corner);

		ETileConcatenation.drawHline(ug, linePos, 0, deltax);
		ETileConcatenation.drawHline(ug, linePos, fullDim.getWidth() - deltax, fullDim.getWidth());

		orig.drawU(ug.apply(new UTranslate(deltax, 0)));
	}

	@Override
	public double linePos(StringBounder stringBounder) {
		return orig.linePos(stringBounder);
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
