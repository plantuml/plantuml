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
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public abstract class RacorderAbstract implements Racorder {

	public final DotPath getRacordOut(Rectangle2D rect, Line2D tangeante) {
		tangeante = symetric(tangeante);
		return getRacordIn(rect, tangeante).reverse();
	}

	private static Line2D symetric(Line2D line) {
		final double x1 = line.getX1();
		final double y1 = line.getY1();
		final double x2 = line.getX2();
		final double y2 = line.getY2();
		final double dx = x2 - x1;
		final double dy = y2 - y1;
		return new Line2D.Double(x1, y1, x1 - dx, y1 - dy);
	}

}
