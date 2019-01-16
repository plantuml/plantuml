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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

public class Box2D {

	final private double x1;
	final private double y1;
	final private double x2;
	final private double y2;

	private Box2D(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static Box2D create(double x, double y, Dimension2D dim) {
		return new Box2D(x, y, x + dim.getWidth(), y + dim.getHeight());
	}

	@Override
	public String toString() {
		return "Box [" + x1 + "," + y1 + "] [" + x2 + "," + y2 + "]";
	}

	public boolean doesIntersect(LineSegmentDouble seg) {
		if (seg.doesIntersect(new LineSegmentDouble(x1, y1, x2, y1))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y1, x2, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x2, y2, x1, y2))) {
			return true;
		}
		if (seg.doesIntersect(new LineSegmentDouble(x1, y2, x1, y1))) {
			return true;
		}
		return false;
	}

}
