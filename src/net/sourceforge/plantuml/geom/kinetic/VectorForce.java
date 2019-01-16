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

public class VectorForce {

	private final double x;
	private final double y;

	public VectorForce(double x, double y) {
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(x) || Double.isInfinite(y)) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
	}

	public VectorForce(Point2D src, Point2D dest) {
		this(dest.getX() - src.getX(), dest.getY() - src.getY());
	}

	public VectorForce plus(VectorForce other) {
		return new VectorForce(this.x + other.x, this.y + other.y);
	}

	public VectorForce multiply(double v) {
		return new VectorForce(x * v, y * v);
	}

	@Override
	public String toString() {
		return String.format("{%8.2f %8.2f}", x, y);
	}

	public VectorForce negate() {
		return new VectorForce(-x, -y);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public VectorForce normaliseTo(double newLength) {
		if (Double.isInfinite(newLength) || Double.isNaN(newLength)) {
			throw new IllegalArgumentException();
		}
		final double actualLength = length();
		if (actualLength == 0) {
			return this;
		}
		final double f = newLength / actualLength;
		return new VectorForce(x * f, y * f);

	}

	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
}
