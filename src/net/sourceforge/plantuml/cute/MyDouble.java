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

import java.util.StringTokenizer;

public class MyDouble {

	private static final double NO_CURVE = java.lang.Double.MIN_VALUE;
	private final double value;
	private final double curvation;

	public MyDouble(String s) {
		final StringTokenizer st = new StringTokenizer(s, ",");
		this.value = java.lang.Double.parseDouble(st.nextToken());
		if (st.hasMoreTokens()) {
			this.curvation = java.lang.Double.parseDouble(st.nextToken());
		} else {
			this.curvation = NO_CURVE;
		}
	}

	@Override
	public String toString() {
		return value + "[" + curvation + "]";
	}

	private MyDouble(double value, double curvation) {
		this.value = value;
		this.curvation = curvation;
	}

	public double getCurvation(double def) {
		if (curvation == NO_CURVE) {
			return def;
		}
		return curvation;
	}

	public double getValue() {
		return value;
	}

	public boolean hasCurvation() {
		return curvation != NO_CURVE;
	}

	public MyDouble rotateZoom(RotationZoom rotationZoom) {
		final double newValue = rotationZoom.applyZoom(value);
		final double curvation = this.curvation == NO_CURVE ? NO_CURVE : rotationZoom.applyZoom(this.curvation);
		return new MyDouble(newValue, curvation);
	}

	public MyDouble toRadians() {
		return new MyDouble(Math.toRadians(value), curvation);
	}

}
