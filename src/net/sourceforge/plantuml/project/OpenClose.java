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
package net.sourceforge.plantuml.project;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import net.sourceforge.plantuml.project.core3.Histogram;
import net.sourceforge.plantuml.project.core3.TimeLine;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public class OpenClose implements Histogram, LoadPlanable {

	private final Collection<DayOfWeek> closedDayOfWeek = EnumSet.noneOf(DayOfWeek.class);
	private final Collection<Day> closedDays = new HashSet<>();
	private final Collection<Day> openedDays = new HashSet<>();
	private Day calendar;

	public int daysInWeek() {
		return 7 - closedDayOfWeek.size();
	}

	private boolean isThereSomeChangeAfter(Day day) {
		if (closedDayOfWeek.size() > 0) {
			return true;
		}
		for (Day tmp : closedDays) {
			if (tmp.compareTo(day) >= 0)
				return true;
		}
		for (Day tmp : openedDays) {
			if (tmp.compareTo(day) >= 0)
				return true;
		}
		return false;
	}

	private boolean isThereSomeChangeBefore(Day day) {
		if (closedDayOfWeek.size() > 0) {
			return true;
		}
		for (Day tmp : closedDays) {
			if (tmp.compareTo(day) <= 0)
				return true;
		}
		for (Day tmp : openedDays) {
			if (tmp.compareTo(day) <= 0)
				return true;
		}
		return false;
	}

	public boolean isClosed(Day day) {
		if (openedDays.contains(day)) {
			return false;
		}
		final DayOfWeek dayOfWeek = day.getDayOfWeek();
		return closedDayOfWeek.contains(dayOfWeek) || closedDays.contains(day);
	}

	public void close(DayOfWeek day) {
		closedDayOfWeek.add(day);
	}

	public void close(Day day) {
		closedDays.add(day);
	}

	public void open(Day day) {
		openedDays.add(day);
	}

	public final Day getCalendar() {
		return calendar;
	}

	public final void setCalendar(Day calendar) {
		this.calendar = calendar;
	}

	public long getNext(long moment) {
		Day day = Day.create(moment);
		if (isThereSomeChangeAfter(day) == false) {
			return TimeLine.MAX_TIME;
		}

		final long current = getLoatAtInternal(day);
		System.err.println("getNext:day=" + day + " current=" + current);
		while (true) {
			day = day.increment();
			final int tmp = getLoatAtInternal(day);
			System.err.println("..day=" + day + " " + tmp);
			if (tmp != current) {
				return day.getMillis();
			}
		}
	}

	public long getPrevious(long moment) {
		Day day = Day.create(moment);
		if (isThereSomeChangeBefore(day) == false) {
			return -TimeLine.MAX_TIME;
		}

		final long current = getLoatAtInternal(day);
		System.err.println("getPrevious=" + day + " current=" + current);
		while (true) {
			day = day.decrement();
			final int tmp = getLoatAtInternal(day);
			System.err.println("..day=" + day + " " + tmp);
			if (tmp != current) {
				return day.getMillis();
			}
		}
	}

	public long getValueAt(long moment) {
		final Day day = Day.create(moment);
		if (isClosed(day)) {
			return 0;
		}
		return 100;
	}

	public int getLoadAt(Day day) {
		if (getCalendar() == null) {
			return 100;
		}
		return getLoatAtInternal(day);
	}

	private int getLoatAtInternal(Day day) {
		if (isClosed(day)) {
			return 0;
		}
		return 100;
	}

}
