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
package net.sourceforge.plantuml.project3;

import java.util.Map;
import java.util.TreeMap;

public class TimeScaleBasic2 implements TimeScale {

	private final GCalendar calendar;
	private final GCalendar calendarAllOpen;
	private final TimeScaleBasic basic = new TimeScaleBasic();
	private final Map<Instant, Instant> cache = new TreeMap<Instant, Instant>();

	public TimeScaleBasic2(GCalendarSimple calendar) {
		this.calendar = calendar;
		this.calendarAllOpen = calendar;
	}

	private Instant changeInstantSlow(Instant instant) {
		final DayAsDate day = calendar.toDayAsDate((InstantDay) instant);
		return calendarAllOpen.fromDayAsDate(day);
	}

	private Instant changeInstant(Instant instant) {
		Instant result = cache.get(instant);
		if (result == null) {
			result = changeInstantSlow(instant);
			cache.put(instant, result);
		}
		return result;
	}

	public double getStartingPosition(Instant instant) {
		return basic.getStartingPosition(changeInstant(instant));
	}

	public double getEndingPosition(Instant instant) {
		return basic.getEndingPosition(changeInstant(instant));
	}

	public double getWidth(Instant instant) {
		return basic.getWidth(changeInstant(instant));
	}

}
