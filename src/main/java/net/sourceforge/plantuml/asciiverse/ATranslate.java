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
package net.sourceforge.plantuml.asciiverse;

public class ATranslate {
	private final int dx;
	private final int dy;

	@Override
	public String toString() {
		return "translate dx=" + dx + " dy=" + dy;
	}

	public static ATranslate none() {
		return new ATranslate(0, 0);
	}

	public ATranslate(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public static ATranslate dx(int dx) {
		return new ATranslate(dx, 0);
	}

	public static ATranslate dy(int dy) {
		return new ATranslate(0, dy);
	}

	public double getDx() {
		return dx;
	}

	public double getDy() {
		return dy;
	}

	public boolean isAlmostSame(ATranslate other) {
		return this.dx == other.dx || this.dy == other.dy;
	}

	public ATranslate compose(ATranslate other) {
		return new ATranslate(dx + other.dx, dy + other.dy);
	}

	public ATranslate reverse() {
		return new ATranslate(-dx, -dy);
	}

}
