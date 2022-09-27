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
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileWithCircles extends AbstractTextBlock implements ETile {

	private static final double SIZE = 8;

	private final double deltax = 15;
	private final ETile orig;

	public ETileWithCircles(ETile orig) {
		this.orig = orig;
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return XDimension2D.delta(orig.calculateDimension(stringBounder), 2 * deltax, 0);
	}

	@Override
	public void drawU(UGraphic ug) {
		final double linePos = linePos(ug.getStringBounder());
		final XDimension2D fullDim = calculateDimension(ug.getStringBounder());
		orig.drawU(ug.apply(UTranslate.dx(deltax)));
		ug = ug.apply(HColors.BLACK).apply(HColors.BLACK.bg());
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		ug.apply(new UTranslate(0, linePos - SIZE / 2)).draw(circle);
		ug.apply(new UTranslate(fullDim.getWidth() - SIZE / 2, linePos - SIZE / 2)).draw(circle);
		ETileConcatenation.drawHline(ug, linePos, SIZE / 2, deltax);
		ETileConcatenation.drawHline(ug, linePos, fullDim.getWidth() - deltax, fullDim.getWidth() - SIZE / 2);
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
