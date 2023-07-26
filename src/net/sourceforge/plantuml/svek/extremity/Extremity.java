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
package net.sourceforge.plantuml.svek.extremity;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;

public abstract class Extremity implements UDrawable {

	protected double manageround(double angle) {
		final double deg = angle * 180.0 / Math.PI;
		if (isCloseTo(0, deg))
			return 0;

		if (isCloseTo(90, deg))
			return 90.0 * Math.PI / 180.0;

		if (isCloseTo(180, deg))
			return 180.0 * Math.PI / 180.0;

		if (isCloseTo(270, deg))
			return 270.0 * Math.PI / 180.0;

		if (isCloseTo(360, deg))
			return 0;

		return angle;
	}

	private boolean isCloseTo(double value, double variable) {
		if (Math.abs(value - variable) < 0.05)
			return true;

		return false;
	}

	public abstract XPoint2D somePoint();

//	public XPoint2D isTooSmallSoGiveThePointCloserToThisOne(XPoint2D pt) {
//		return null;
//	}

	public UTranslate getDeltaForKal() {
		return UTranslate.none();
	}

	// public abstract double getDecorationLength();

	public double getDecorationLength() {
		return 8;
	}

//	public double getDecorationLength() {
//	// return 15;
//	System.err.println("CLASS=" + getClass().getName());
//	Thread.dumpStack();
//	System.exit(0);
//	throw new UnsupportedOperationException(getClass().getName());
//}

}
