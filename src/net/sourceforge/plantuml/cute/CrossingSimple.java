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
import java.util.ArrayList;
import java.util.List;

public class CrossingSimple {

	// http://mathworld.wolfram.com/Circle-LineIntersection.html

	private final double radius;
	private final InfiniteLine line;

	public CrossingSimple(double radius, InfiniteLine line) {
		this.radius = radius;
		this.line = line;
	}

	private double pow2(double x) {
		return x * x;
	}

	private double sgn(double x) {
		if (x < 0) {
			return -1;
		}
		return 1;
	}

	public List<Point2D> intersection() {
		final List<Point2D> result = new ArrayList<Point2D>();
		final double delta = pow2(radius * line.getDr()) - pow2(line.getDiscriminant());

		if (delta < 0) {
			return result;
		}

		double x;
		double y;

		x = (line.getDiscriminant() * line.getDeltaY() + sgn(line.getDeltaY()) * line.getDeltaX() * Math.sqrt(delta))
				/ pow2(line.getDr());
		y = (-line.getDiscriminant() * line.getDeltaX() + Math.abs(line.getDeltaY()) * Math.sqrt(delta))
				/ pow2(line.getDr());
		result.add(new Point2D.Double(x, y));

		x = (line.getDiscriminant() * line.getDeltaY() - sgn(line.getDeltaY()) * line.getDeltaX() * Math.sqrt(delta))
				/ pow2(line.getDr());
		y = (-line.getDiscriminant() * line.getDeltaX() - Math.abs(line.getDeltaY()) * Math.sqrt(delta))
				/ pow2(line.getDr());
		result.add(new Point2D.Double(x, y));

		return result;
	}

}
