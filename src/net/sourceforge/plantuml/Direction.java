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
package net.sourceforge.plantuml;

import java.awt.geom.Point2D;

public enum Direction {
	RIGHT, LEFT, DOWN, UP;

	public Direction getInv() {
		if (this == RIGHT) {
			return LEFT;
		}
		if (this == LEFT) {
			return RIGHT;
		}
		if (this == DOWN) {
			return UP;
		}
		if (this == UP) {
			return DOWN;
		}
		throw new IllegalStateException();
	}
	
	public String getShortCode() {
		return name().substring(0, 1);
	}

	public static Direction fromChar(char c) {
		if (c == '<') {
			return Direction.LEFT;
		}
		if (c == '>') {
			return Direction.RIGHT;
		}
		if (c == '^') {
			return Direction.UP;
		}
		return Direction.DOWN;
	}

	public Direction clockwise() {
		if (this == RIGHT) {
			return DOWN;
		}
		if (this == LEFT) {
			return UP;
		}
		if (this == DOWN) {
			return LEFT;
		}
		if (this == UP) {
			return RIGHT;
		}
		throw new IllegalStateException();
	}

	public static Direction leftOrRight(Point2D p1, Point2D p2) {
		if (p1.getX() < p2.getX()) {
			return Direction.LEFT;
		}
		if (p1.getX() > p2.getX()) {
			return Direction.RIGHT;
		}
		throw new IllegalArgumentException();
	}

	public static Direction fromVector(Point2D p1, Point2D p2) {
		final double x1 = p1.getX();
		final double y1 = p1.getY();
		final double x2 = p2.getX();
		final double y2 = p2.getY();
		if (x1 == x2 && y1 == y2) {
			return null;
		}
		if (x1 == x2) {
			if (y2 > y1) {
				return Direction.DOWN;
			}
			return Direction.UP;
		}
		if (y1 == y2) {
			if (x2 > x1) {
				return Direction.RIGHT;
			}
			return Direction.LEFT;
		}
		throw new IllegalArgumentException("Not a H or V line!");

	}
}
