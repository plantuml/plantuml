/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3828 $
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
}
