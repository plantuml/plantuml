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
package net.sourceforge.plantuml.utils;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.regex.RegexResult;

public enum Direction {
	RIGHT, LEFT, DOWN, UP;

	public Direction getInv() {
		switch (this) {
		case RIGHT:
			return LEFT;
		case LEFT:
			return RIGHT;
		case DOWN:
			return UP;
		case UP:
			return DOWN;
		}
	}

	public String getShortCode() {
		return name().substring(0, 1);
	}

	public static Direction fromChar(char c) {
		switch (c) {
		case '<':
			return LEFT;
		case '>':
			return RIGHT;
		case '^':
			return UP;
		default:
			return DOWN;
		}
	}

	public static Direction getWBSDirection(RegexResult arg) {
		final String type = arg.get("TYPE", 0);
		Direction direction = type.contains("-") ? LEFT : RIGHT;
		
		final String dir = arg.getLazzy("DIRECTION", 0);
		if ("<".equals(dir))
			direction = LEFT;
		else if (">".equals(dir))
			direction = RIGHT;

		return direction;
	}

	public Direction clockwise() {
		switch (this) {
		case RIGHT:
			return DOWN;
		case LEFT:
			return UP;
		case DOWN:
			return LEFT;
		case UP:
			return RIGHT;
		}
	}

	public static Direction leftOrRight(XPoint2D p1, XPoint2D p2) {
		if (p1.getX() < p2.getX()) {
			return Direction.LEFT;
		}
		if (p1.getX() > p2.getX()) {
			return Direction.RIGHT;
		}
		throw new IllegalArgumentException();
	}

	public static Direction fromVector(XPoint2D p1, XPoint2D p2) {
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

	public static Direction lazzyValueOf(String s) {
		s = s.toUpperCase();
		if ("TOP".equals(s))
			return Direction.UP;
		if ("BOTTOM".equals(s))
			return Direction.DOWN;
		return valueOf(s);
	}
}
