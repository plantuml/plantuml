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
package net.sourceforge.plantuml.geom.kinetic;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class Quadrant {

	static final private int SIZE = 100;

	private final int x;
	private final int y;

	public Quadrant(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Quadrant(Point2DCharge pt) {
		this((int) pt.getX() / SIZE, (int) pt.getY() / SIZE);
	}

	@Override
	public boolean equals(Object obj) {
		final Quadrant other = (Quadrant) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public int hashCode() {
		return x * 3571 + y;
	}

	@Override
	public String toString() {
		return "" + x + "-" + y;
	}

	public Collection<Quadrant> neighbourhood() {
		final Collection<Quadrant> result = Arrays.asList(new Quadrant(x - 1, y - 1), new Quadrant(x, y - 1),
				new Quadrant(x + 1, y - 1), new Quadrant(x - 1, y), this, new Quadrant(x + 1, y), new Quadrant(x - 1,
						y + 1), new Quadrant(x, y + 1), new Quadrant(x + 1, y + 1));
		assert new HashSet<Quadrant>(result).size() == 9;
		return result;
	}

}
