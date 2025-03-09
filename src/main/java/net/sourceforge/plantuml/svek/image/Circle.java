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
package net.sourceforge.plantuml.svek.image;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

public class Circle {

	private XPoint2D center;

	private double radius;

	public Circle() {
		this(new XPoint2D(0, 0));
	}

	public Circle(XPoint2D center) {
		this.center = center;
		this.radius = 0;
	}

	public Circle(XPoint2D p1, XPoint2D p2) {
		center = new XPoint2D((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		radius = p1.distance(center);
	}

	public static Circle getCircle(XPoint2D p1, XPoint2D p2, XPoint2D p3) {
		if (p3.getY() != p2.getY()) {
			return new Circle(p1, p2, p3);
		}
		return new Circle(p2, p1, p3);
	}

	private Circle(XPoint2D p1, XPoint2D p2, XPoint2D p3) {
		final double num1 = p3.getX() * p3.getX() * (p1.getY() - p2.getY())
				+ (p1.getX() * p1.getX() + (p1.getY() - p2.getY()) * (p1.getY() - p3.getY())) * (p2.getY() - p3.getY())
				+ p2.getX() * p2.getX() * (-p1.getY() + p3.getY());
		final double den1 = 2 * (p3.getX() * (p1.getY() - p2.getY()) + p1.getX() * (p2.getY() - p3.getY())
				+ p2.getX() * (-p1.getY() + p3.getY()));
		final double x = num1 / den1;
		final double den2 = p3.getY() - p2.getY();
		final double y = (p2.getY() + p3.getY()) / 2
				- (p3.getX() - p2.getX()) / den2 * (x - (p2.getX() + p3.getX()) / 2);
		center = new XPoint2D(x, y);
		radius = center.distance(p1);
	}

	public XPoint2D getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public boolean isOutside(XPoint2D point) {
		final double d = center.distance(point);
		if (d > radius) {
			return true;
		}
		return false;
	}

}
