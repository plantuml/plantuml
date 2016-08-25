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
package net.sourceforge.plantuml.project;

class BasicInstantArithmetic implements InstantArithmetic {

	private final DayClose dayClose;

	BasicInstantArithmetic(DayClose dayClose) {
		if (dayClose == null) {
			throw new IllegalArgumentException();
		}
		this.dayClose = dayClose;
	}

	public Instant add(Instant i1, Duration duration) {
		Instant result = i1;
		final long min = duration.getMinutes();
		if (min < 0) {
			throw new IllegalArgumentException();
		}
		for (long i = 0; i < min; i += 24 * 60 * 60) {
			result = result.next(dayClose);
		}
		return result;
	}

	public Instant sub(Instant i1, Duration duration) {
		Instant result = i1;
		final long min = duration.getMinutes();
		if (min < 0) {
			throw new IllegalArgumentException();
		}
		for (long i = 0; i < min; i += 24 * 60 * 60) {
			result = result.prev(dayClose);
		}
		return result;
	}

	public Duration diff(Instant i1, Instant i2) {
		if (i2.compareTo(i1) < 0) {
			throw new IllegalArgumentException();
		}
		long minutes = 0;
		while (i2.compareTo(i1) > 0) {
			minutes += 24 * 60 * 60;
			i1 = i1.next(null);
		}
		return new Duration(minutes);
	}
}
