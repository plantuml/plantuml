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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ugraphic.UPolygon;

public class Arrows {

	final static private double delta1 = 10;
	final static private double delta2 = 4;

	public static UPolygon asToUp() {
		final UPolygon polygon = new UPolygon("asToUp");
		polygon.addPoint(-delta2, delta1);
		polygon.addPoint(0, 0);
		polygon.addPoint(delta2, delta1);
		polygon.addPoint(0, delta1 - 4);
		return polygon;
	}

	public static UPolygon asToDown() {
		final UPolygon polygon = new UPolygon("asToDown");
		polygon.addPoint(-delta2, -delta1);
		polygon.addPoint(0, 0);
		polygon.addPoint(delta2, -delta1);
		polygon.addPoint(0, -delta1 + 4);
		return polygon;
	}

	public static UPolygon asToRight() {
		final UPolygon polygon = new UPolygon("asToRight");
		polygon.addPoint(-delta1, -delta2);
		polygon.addPoint(0, 0);
		polygon.addPoint(-delta1, delta2);
		polygon.addPoint(-delta1 + 4, 0);
		return polygon;
	}

	public static UPolygon asToLeft() {
		final UPolygon polygon = new UPolygon("asToLeft");
		polygon.addPoint(delta1, -delta2);
		polygon.addPoint(0, 0);
		polygon.addPoint(delta1, delta2);
		polygon.addPoint(delta1 - 4, 0);
		return polygon;
	}

	public static UPolygon asTo(Direction direction) {
		if (direction == Direction.UP) {
			return asToUp();
		}
		if (direction == Direction.DOWN) {
			return asToDown();
		}
		if (direction == Direction.LEFT) {
			return asToLeft();
		}
		if (direction == Direction.RIGHT) {
			return asToRight();
		}
		throw new IllegalArgumentException();
	}

}
