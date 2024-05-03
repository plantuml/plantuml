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
 *
 */
package net.sourceforge.plantuml.klimt.geom;

import net.sourceforge.plantuml.activitydiagram3.ftile.RectangleCoordinates;

public class CoordinateChange {

	private final double vect_u_x;
	private final double vect_u_y;
	private final double vect_v_x;
	private final double vect_v_y;
	private final double len;
	private RectangleCoordinates rectangleCoordinates = new RectangleCoordinates(0.0, 0.0, 0.0, 0.0);

	public static CoordinateChange create(XPoint2D p1, XPoint2D p2) {
		return new CoordinateChange(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public CoordinateChange(double x1, double y1, double x2, double y2) {
		this.rectangleCoordinates.setX1(x1);
		this.rectangleCoordinates.setY1(y1);
		this.rectangleCoordinates.setX2(x2);
		this.rectangleCoordinates.setY2(y2);
		this.len = XPoint2D.distance(new RectangleCoordinates(x1, y1, x2, y2));
		if (this.len == 0) {
			throw new IllegalArgumentException();
		}

		this.vect_u_x = (x2 - x1) / len;
		this.vect_u_y = (y2 - y1) / len;

		this.vect_v_x = -this.vect_u_y;
		this.vect_v_y = this.vect_u_x;

	}

	public XPoint2D getTrueCoordinate(double a, double b) {
		final double x = a * vect_u_x + b * vect_v_x;
		final double y = a * vect_u_y + b * vect_v_y;
		return new XPoint2D(rectangleCoordinates.getX1() + x, rectangleCoordinates.getY1() + y);
	}

	public final double getLength() {
		return len;
	}

}