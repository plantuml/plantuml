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
package net.sourceforge.plantuml.salt.element;

import java.util.List;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Grid2 {

	private final List<Double> rowsStart;
	private final List<Double> colsStart;
	private final TableStrategy strategy;

	public Grid2(List<Double> rowsStart, List<Double> colsStart, TableStrategy strategy) {
		this.rowsStart = rowsStart;
		this.colsStart = colsStart;
		this.strategy = strategy;
	}

	public void drawU(UGraphic ug) {
		final double xmin = colsStart.get(0);
		final double xmax = colsStart.get(colsStart.size() - 1);
		final double ymin = rowsStart.get(0);
		final double ymax = rowsStart.get(rowsStart.size() - 1);
		if (strategy == TableStrategy.DRAW_OUTSIDE || strategy == TableStrategy.DRAW_OUTSIDE_WITH_TITLE) {
			ug.apply(new UTranslate(xmin, ymin)).draw(ULine.hline(xmax - xmin));
			ug.apply(new UTranslate(xmin, ymax)).draw(ULine.hline(xmax - xmin));
			ug.apply(new UTranslate(xmin, ymin)).draw(ULine.vline(ymax - ymin));
			ug.apply(new UTranslate(xmax, ymin)).draw(ULine.vline(ymax - ymin));
		}
		if (drawHorizontal()) {
			for (Double y : rowsStart) {
				ug.apply(new UTranslate(xmin, y)).draw(ULine.hline(xmax - xmin));
			}
		}
		if (drawVertical()) {
			for (Double x : colsStart) {
				ug.apply(new UTranslate(x, ymin)).draw(ULine.vline(ymax - ymin));
			}
		}
	}

	private boolean drawHorizontal() {
		if (strategy == TableStrategy.DRAW_HORIZONTAL || strategy == TableStrategy.DRAW_ALL) {
			return true;
		}
		return false;
	}

	private boolean drawVertical() {
		if (strategy == TableStrategy.DRAW_VERTICAL || strategy == TableStrategy.DRAW_ALL) {
			return true;
		}
		return false;
	}

}
