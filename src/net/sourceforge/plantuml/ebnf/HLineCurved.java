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

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;

public class HLineCurved implements UDrawable {

	private final double height;
	private final double delta;

	public HLineCurved(double height, double delta) {
		this.height = height;
		this.delta = delta;
	}

	@Override
	public void drawU(UGraphic ug) {
		if (delta == 0) {
			ug.draw(ULine.vline(height));
			return;
		}
		final UPath path = new UPath();
		path.moveTo(-delta, 0);

		final double a = delta / 4;
		path.cubicTo(-a, 0, 0, Math.abs(a), 0, Math.abs(delta));
		// path.lineTo(0, delta);

		path.lineTo(0, height - Math.abs(delta));

		path.cubicTo(0, height - a, a, height, delta, height);
		// path.lineTo(delta, height);

		ug.draw(path);
	}

}
