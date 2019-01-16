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
package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Frame {

	private double x;
	private double y;

	private final int width;
	private final int height;

	public Frame(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	LineSegmentDouble getSide1() {
		return new LineSegmentDouble(x, y, x, y + height);
	}

	LineSegmentDouble getSide2() {
		return new LineSegmentDouble(x, y, x + width, y);
	}

	LineSegmentDouble getSide3() {
		return new LineSegmentDouble(x + width, y, x + width, y + height);
	}

	LineSegmentDouble getSide4() {
		return new LineSegmentDouble(x, y + height, x + width, y + height);
	}

	public Point2D getFrontierPointViewBy(Point2D point) {
		final LineSegmentDouble seg = new LineSegmentDouble(point, getCenter());
		Point2D p = seg.getSegIntersection(getSide1());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide2());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide3());
		if (p != null) {
			return p;
		}
		p = seg.getSegIntersection(getSide4());
		if (p != null) {
			return p;
		}
		return null;
	}

	private Point2D getCenter() {
		return new Point2D.Double(x + width / 2.0, y + height / 2.0);
	}

	public Point2D getMainCorner() {
		return new Point2D.Double(x, y);
	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

}
