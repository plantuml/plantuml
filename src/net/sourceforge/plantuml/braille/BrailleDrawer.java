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
package net.sourceforge.plantuml.braille;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class BrailleDrawer implements UDrawable {

	private final BrailleGrid grid;
	private final double step = 9;
	private final double spotSize = 5;

	public BrailleDrawer(BrailleGrid grid) {
		this.grid = grid;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColorSet.instance().getColorIfValid("#F0F0F0"));
		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			ug.apply(UTranslate.dx(x * step + spotSize + 1)).draw(
					ULine.vline((grid.getMaxY() - grid.getMinY()) * step));
		}
		for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
			ug.apply(UTranslate.dy(y * step + spotSize + 1)).draw(
					ULine.hline((grid.getMaxX() - grid.getMinX()) * step));
		}
		ug = ug.apply(HColorUtils.BLACK).apply(HColorUtils.BLACK.bg());
		for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
			for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
				if (grid.getState(x, y)) {
					drawCircle(ug, x, y);
				}
			}
		}
	}

	private void drawCircle(UGraphic ug, int x, int y) {
		final double cx = x * step;
		final double cy = y * step;
		ug.apply(new UTranslate(cx, cy)).draw(new UEllipse(spotSize, spotSize));
	}

}
