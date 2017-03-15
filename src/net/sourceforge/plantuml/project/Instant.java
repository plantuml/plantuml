/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.project;

public class Instant implements Numeric {

	private final Day value;

	public Instant(Day d) {
		this.value = d;
	}

	public Numeric add(Numeric other) {
		throw new UnsupportedOperationException();
	}

	public NumericType getNumericType() {
		return NumericType.INSTANT;
	}

	public Day getDay() {
		return value;
	}

	public Instant next(DayClose dayClose) {
		return new Instant(value.next(dayClose));
	}

	public Instant prev(DayClose dayClose) {
		return new Instant(value.prev(dayClose));
	}

	@Override
	public String toString() {
		return "Instant:" + value;
	}

	public int compareTo(Numeric other) {
		final Instant this2 = (Instant) other;
		return value.compareTo(this2.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Instant other = (Instant) obj;
		return value.equals(other.value);
	}

}
