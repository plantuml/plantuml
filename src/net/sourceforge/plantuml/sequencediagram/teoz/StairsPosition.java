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
package net.sourceforge.plantuml.sequencediagram.teoz;

public class StairsPosition implements Comparable<StairsPosition> {

	private final double value;
	private final boolean destroy;

	public StairsPosition(double value, boolean destroy) {
		this.value = value;
		this.destroy = destroy;
	}

	@Override
	public String toString() {
		return "" + value + "-(" + destroy + ")";
	}

	@Override
	public int hashCode() {
		return new Double(value).hashCode() + (destroy ? 17 : 37);
	}

	@Override
	public boolean equals(Object obj) {
		final StairsPosition other = (StairsPosition) obj;
		return this.value == other.value && this.destroy == other.destroy;
	}

	public double getValue() {
		return value;
	}

	public int compareTo(StairsPosition other) {
		if (this.value > other.value) {
			return 1;
		}
		if (this.value < other.value) {
			return -1;
		}
		return 0;
	}

	public boolean isDestroy() {
		return destroy;
	}

}
