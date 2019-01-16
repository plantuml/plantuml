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
package net.sourceforge.plantuml.code;

public class SpiralOnRectangle {

	private final Spiral spiral = new Spiral();
	private final int width;
	private final int height;
	private final PairInt delta;

	public SpiralOnRectangle(int width, int height) {
		this.width = width;
		this.height = height;
		this.delta = new PairInt(width / 2, height / 2);
	}

	private boolean inside(PairInt point) {
		final int x = point.getX();
		final int y = point.getY();
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	public PairInt nextPoint() {
		do {
			final PairInt result = spiral.nextPoint().plus(delta);
			if (inside(result)) {
				return result;
			}
		} while (true);
	}
}
