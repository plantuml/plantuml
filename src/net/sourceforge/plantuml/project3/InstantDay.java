/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.project3;

public class InstantDay implements Instant {

	private final int numDay;

	public InstantDay(int numDay) {
		this.numDay = numDay;
	}

	@Override
	public String toString() {
		return "(day +" + numDay + ")";
	}

	public InstantDay add(Duration duration) {
		final int nbdays = ((DurationDay) duration).getDays();
		return new InstantDay(numDay + nbdays);
	}

	public InstantDay sub(Duration duration) {
		final int nbdays = ((DurationDay) duration).getDays();
		return new InstantDay(numDay - nbdays);
	}

	public InstantDay increment() {
		return new InstantDay(numDay + 1);
	}

	public InstantDay decrement() {
		return new InstantDay(numDay - 1);
	}

	final int getNumDay() {
		return numDay;
	}

	public int compareTo(Instant other) {
		return this.numDay - ((InstantDay) other).numDay;
	}

	public String toShortString() {
		return "" + (numDay + 1);
	}

}
