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
 */
package net.sourceforge.plantuml.nwdiag;

import java.util.Set;

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UDrawable;

public class VerticalLine implements UDrawable {

	private final double y1;
	private final double y2;
	private final Set<Double> skip;

	public VerticalLine(double y1, double y2, Set<Double> skip) {
		this.y1 = Math.min(y1, y2);
		this.y2 = Math.max(y1, y2);
		this.skip = skip;
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(HColors.none().bg());
		boolean drawn = false;
		double current = y1;
		UPath path = UPath.none();
		path.moveTo(0, current);
		for (Double step : skip) {
			if (step < y1) {
				continue;
			}
			assert step >= y1;
			drawn = true;
			if (step == y2) {
				path.lineTo(0, y2);
			} else {
				path.lineTo(0, Math.min(y2, step - 3));
				if (y2 > step) {
					path.arcTo(4, 4, 0, 0, 1, 0, step + 9);
					continue;
				}
			}
			ug.draw(path);
			path = UPath.none();
			current = step + 9;
			path.moveTo(0, current);
			if (current >= y2) {
				break;
			}
		}
		if (drawn == false) {
			path.lineTo(0, y2);
			ug.draw(path);
		}

	}

}
