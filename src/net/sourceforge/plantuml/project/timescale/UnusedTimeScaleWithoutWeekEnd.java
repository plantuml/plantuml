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
package net.sourceforge.plantuml.project.timescale;

import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.GCalendar;
import net.sourceforge.plantuml.project.time.Wink;

public class UnusedTimeScaleWithoutWeekEnd implements TimeScale {

	private final double scale = 16.0;
	private final GCalendar calendar;

	public UnusedTimeScaleWithoutWeekEnd(GCalendar calendar) {
		if (calendar == null) {
			throw new IllegalArgumentException();
		}
		this.calendar = calendar;
	}

	public double getStartingPosition(Wink instant) {
		double result = 0;
		Wink current = (Wink) instant;
		while (current.getWink() > 0) {
			current = current.decrement();
			result += getWidth(current);
		}
		return result;
	}

	public double getWidth(Wink instant) {
		final Day day = calendar.toDayAsDate((Wink) instant);
		final DayOfWeek dayOfWeek = day.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			return 1;
		}
		return scale;
	}

	public double getEndingPosition(Wink instant) {
		throw new UnsupportedOperationException();
	}

}
