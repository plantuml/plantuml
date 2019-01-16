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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SmallestEnclosingCircle {

	private final List<Point2D> all = new ArrayList<Point2D>();
	private Circle lastSolution;

	public void append(Point2D pt) {
		if (all.contains(pt) == false) {
			all.add(pt);
		}
		this.lastSolution = null;
	}

	public Circle getCircle() {
		if (lastSolution == null) {
			lastSolution = findSec(all.size(), all, 0, new ArrayList<Point2D>(all));
		}
		return lastSolution;
	}

	private Circle findSec(int n, List<Point2D> p, int m, List<Point2D> b) {
		Circle sec = new Circle();

		// Compute the Smallest Enclosing Circle defined by B
		if (m == 1) {
			sec = new Circle(b.get(0));
		} else if (m == 2) {
			sec = new Circle(b.get(0), b.get(1));
		} else if (m == 3) {
			return Circle.getCircle(b.get(0), b.get(1), b.get(2));
		}

		// Check if all the points in p are enclosed
		for (int i = 0; i < n; i++) {
			if (sec.isOutside(p.get(i))) {
				// Compute B <--- B union P[i].
				b.set(m, p.get(i));
				// Recurse
				sec = findSec(i, p, m + 1, b);
			}
		}

		return sec;
	}

}
