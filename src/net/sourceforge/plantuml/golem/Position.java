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
package net.sourceforge.plantuml.golem;

public class Position {

	private final int xmin;
	private final int ymin;
	private final int xmax;
	private final int ymax;

	public Position(int xmin, int ymin, int xmax, int ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	@Override
	public boolean equals(Object o) {
		final Position other = (Position) o;
		return this.xmin == other.xmin && this.xmax == other.xmax && this.ymin == other.ymin && this.ymax == other.ymax;
	}

	@Override
	public int hashCode() {
		return xmin + ymin << 8 + xmax << 16 + ymax << 24;
	}

	@Override
	public String toString() {
		return "(" + xmin + "," + ymin + ")-(" + xmax + "," + ymax + ")";
	}

	public Position move(TileGeometry position, int sizeMove) {
		if (position == null || position == TileGeometry.CENTER) {
			throw new IllegalArgumentException();
		}
		switch (position) {
		case NORTH:
			return new Position(xmin, ymin - sizeMove, xmax, ymax - sizeMove);
		case SOUTH:
			return new Position(xmin, ymin + sizeMove, xmax, ymax + sizeMove);
		case WEST:
			return new Position(xmin - sizeMove, ymin, xmax - sizeMove, ymax);
		case EAST:
			return new Position(xmin + sizeMove, ymin, xmax + sizeMove, ymax);
		default:
			throw new IllegalStateException();
		}
	}

	public int getXmin() {
		return xmin;
	}

	public int getXmax() {
		return xmax;
	}

	public int getYmin() {
		return ymin;
	}

	public int getYmax() {
		return ymax;
	}

	public int getCenterX() {
		if ((xmin + xmax + 1) % 2 != 0) {
			throw new IllegalStateException();
		}
		return (xmin + xmax + 1) / 2;
	}

	public int getCenterY() {
		if ((ymin + ymax + 1) % 2 != 0) {
			throw new IllegalStateException();
		}
		return (ymin + ymax + 1) / 2;
	}
}
