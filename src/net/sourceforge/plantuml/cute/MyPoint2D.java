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
import java.util.StringTokenizer;

public class MyPoint2D extends Point2D {

	public static final double NO_CURVE = 0;
	private final double x;
	private final double y;
	private final double curvation;

	public MyPoint2D(StringTokenizer st) {
		this.x = java.lang.Double.parseDouble(st.nextToken());
		this.y = java.lang.Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens()) {
			this.curvation = java.lang.Double.parseDouble(st.nextToken());
		} else {
			this.curvation = NO_CURVE;
		}
	}

	@Override
	public boolean equals(Object arg0) {
		final MyPoint2D other = (MyPoint2D) arg0;
		return this.x == other.x && this.y == other.y && this.curvation == other.curvation;
	}

	public static MyPoint2D from(double x, double y) {
		return new MyPoint2D(x, y, NO_CURVE);
	}

	public MyPoint2D withCurvation(double curvation) {
		if (curvation == NO_CURVE) {
			return this;
		}
		return new MyPoint2D(x, y, curvation);
	}

	private MyPoint2D(Point2D p, double curvation) {
		this.x = p.getX();
		this.y = p.getY();
		this.curvation = curvation;
	}

	private MyPoint2D(double x, double y, double curvation) {
		this.x = x;
		this.y = y;
		this.curvation = curvation;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public double getCurvation(double def) {
		if (curvation == NO_CURVE) {
			return def;
		}
		return curvation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setLocation(double arg0, double arg1) {
		throw new UnsupportedOperationException();
	}

	public MyPoint2D rotateZoom(RotationZoom rotationZoom) {
		final Point2D p = rotationZoom.getPoint(x, y);
		final double curvation = this.curvation == NO_CURVE ? NO_CURVE : rotationZoom.applyZoom(this.curvation);
		return new MyPoint2D(p, curvation);
	}

	public boolean hasCurvation() {
		return curvation != NO_CURVE;
	}

}
