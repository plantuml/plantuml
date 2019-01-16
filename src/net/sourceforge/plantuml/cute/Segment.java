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
package net.sourceforge.plantuml.cute;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Segment {

	private final Point2D a;
	private final Point2D b;
	private final double length;

	public Segment(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
		this.length = a.distance(b);
		if (length < 0.0001) {
			throw new IllegalArgumentException();
		}
	}

	public Point2D getFromAtoB(double dist) {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		final double coef = dist / length;
		final double x = dx * coef;
		final double y = dy * coef;
		return new Point2D.Double(a.getX() + x, a.getY() + y);
	}

	public Point2D getA() {
		return a;
	}

	public Point2D getB() {
		return b;
	}

	public Point2D getMiddle() {
		return new Point2D.Double((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
	}

	private Point2D orthoDirection() {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		return new Point2D.Double(-dy / length, dx / length);
	}

	public Point2D getOrthoPoint(double value) {
		final Point2D ortho = orthoDirection();
		final double dx = -ortho.getX() * value;
		final double dy = -ortho.getY() * value;
		return new Point2D.Double((a.getX() + b.getX()) / 2 + dx, (a.getY() + b.getY()) / 2 + dy);
	}


	private boolean isLeft(Point2D point) {
		return ((b.getX() - a.getX()) * (point.getY() - a.getY()) - (b.getY() - a.getY()) * (point.getX() - a.getX())) > 0;
	}

	public double getLength() {
		return length;
	}

	public void debugMe(UGraphic ug) {
		final double dx = b.getX() - a.getX();
		final double dy = b.getY() - a.getY();
		ug = ug.apply(new UTranslate(a));
		ug.draw(new ULine(dx, dy));
		
	}

}
