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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.graphic.UDrawable;

public abstract class Extremity implements UDrawable {


	protected double manageround(double angle) {
		final double deg = angle * 180.0 / Math.PI;
		if (isCloseTo(0, deg)) {
			return 0;
		}
		if (isCloseTo(90, deg)) {
			return 90.0 * Math.PI / 180.0;
		}
		if (isCloseTo(180, deg)) {
			return 180.0 * Math.PI / 180.0;
		}
		if (isCloseTo(270, deg)) {
			return 270.0 * Math.PI / 180.0;
		}
		if (isCloseTo(360, deg)) {
			return 0;
		}
		return angle;
	}

	private boolean isCloseTo(double value, double variable) {
		if (Math.abs(value - variable) < 0.05) {
			return true;
		}
		return false;
	}
	
	public abstract Point2D somePoint();
	
	public Point2D isTooSmallSoGiveThePointCloserToThisOne(Point2D pt) {
		return null;
	}

}
