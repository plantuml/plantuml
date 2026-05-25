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
package net.sourceforge.plantuml.gantt.core;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeRange {

	private final LocalTime start;
	private final LocalTime end;

	public TimeRange(String start, String end) {
		this(parse(start), parse(end));
	}

	public TimeRange(LocalTime start, LocalTime end) {
		if (start == null || end == null)
			throw new IllegalArgumentException("start and end must not be null");

		if (end.isBefore(start))
			throw new IllegalArgumentException("end must not be before start");

		this.start = start;
		this.end = end;
	}

	private static LocalTime parse(String time) {
		if (time == null)
			throw new IllegalArgumentException("time must not be null");

		final int index = time.indexOf(':');
		if (index == -1)
			throw new IllegalArgumentException("Invalid time: " + time);

		try {
			final int hour = Integer.parseInt(time.substring(0, index).trim());
			final int minute = Integer.parseInt(time.substring(index + 1).trim());
			return LocalTime.of(hour, minute);
		} catch (NumberFormatException | DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid time: " + time, e);
		}
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public boolean contains(LocalTime time) {
		if (time == null)
			return false;

		return time.isBefore(start) == false && time.isBefore(end);
	}

	@Override
	public String toString() {
		return start + "-" + end;
	}

}
