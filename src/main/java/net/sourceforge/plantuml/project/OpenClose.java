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
package net.sourceforge.plantuml.project;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.project.core3.Histogram;
import net.sourceforge.plantuml.project.core3.TimeLine;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public class OpenClose implements Histogram, LoadPlanable {

	private final Map<DayOfWeek, DayStatus> weekdayStatus = new EnumMap<>(DayOfWeek.class);
	private final Map<Day, DayStatus> dayStatus = new HashMap<>();
	private Day startingDay;
	private Day offBefore;
	private Day offAfter;

	public int daysInWeek() {
		int result = 7;
		for (DayStatus status : weekdayStatus.values())
			if (status == DayStatus.CLOSE)
				result--;
		return result;
	}

	private boolean isThereSomeChangeAfter(Day day) {
		if (weekdayStatus.size() > 0)
			return true;

		for (Day tmp : dayStatus.keySet())
			if (tmp.compareTo(day) >= 0)
				return true;

		return false;
	}

	private boolean isThereSomeChangeBefore(Day day) {
		if (weekdayStatus.size() > 0)
			return true;

		for (Day tmp : dayStatus.keySet())
			if (tmp.compareTo(day) <= 0)
				return true;

		return false;
	}

	public boolean isClosed(Day day) {
		final DayStatus status = getLocalStatus(day);
		if (status != null)
			return status == DayStatus.CLOSE;

		return false;
	}

	private DayStatus getLocalStatus(Day day) {
		if (offBefore != null && day.compareTo(offBefore) < 0)
			return DayStatus.CLOSE;
		if (offAfter != null && day.compareTo(offAfter) > 0)
			return DayStatus.CLOSE;

		final DayStatus status1 = dayStatus.get(day);
		if (status1 != null)
			return status1;

		final DayOfWeek dayOfWeek = day.getDayOfWeek();
		final DayStatus status2 = weekdayStatus.get(dayOfWeek);
		if (status2 != null)
			return status2;

		return null;
	}

	public void close(DayOfWeek day) {
		weekdayStatus.put(day, DayStatus.CLOSE);
	}

	public void open(DayOfWeek day) {
		weekdayStatus.put(day, DayStatus.OPEN);
	}

	public void close(Day day) {
		dayStatus.put(day, DayStatus.CLOSE);
	}

	public void open(Day day) {
		dayStatus.put(day, DayStatus.OPEN);
	}

	public final Day getStartingDay() {
		return startingDay;
	}

	public final void setStartingDay(Day startingDay) {
		this.startingDay = startingDay;
	}

	public long getNext(long moment) {
		Day day = Day.create(moment);
		if (isThereSomeChangeAfter(day) == false)
			return TimeLine.MAX_TIME;

		final long current = getLoatAtInternal(day);
		System.err.println("getNext:day=" + day + " current=" + current);
		while (true) {
			day = day.increment();
			final int tmp = getLoatAtInternal(day);
			System.err.println("..day=" + day + " " + tmp);
			if (tmp != current)
				return day.getMillis();

		}
	}

	public long getPrevious(long moment) {
		Day day = Day.create(moment);
		if (isThereSomeChangeBefore(day) == false)
			return -TimeLine.MAX_TIME;

		final long current = getLoatAtInternal(day);
		System.err.println("getPrevious=" + day + " current=" + current);
		while (true) {
			day = day.decrement();
			final int tmp = getLoatAtInternal(day);
			System.err.println("..day=" + day + " " + tmp);
			if (tmp != current)
				return day.getMillis();

		}
	}

	public long getValueAt(long moment) {
		final Day day = Day.create(moment);
		if (isClosed(day))
			return 0;

		return 100;
	}

	public int getLoadAt(Day day) {
		if (getStartingDay() == null)
			return 100;

		return getLoatAtInternal(day);
	}

	public void setOffBeforeDate(Day day) {
		this.offBefore = day;
	}

	public void setOffAfterDate(Day day) {
		this.offAfter = day;
	}

	private int getLoatAtInternal(Day day) {
		if (isClosed(day))
			return 0;

		return 100;
	}

	public LoadPlanable mutateMe(final OpenClose except) {
		if (except != null)
			return new LoadPlanable() {
				@Override
				public int getLoadAt(Day instant) {
					final DayStatus exceptStatus = except.getLocalStatus(instant);
					if (exceptStatus == DayStatus.CLOSE)
						return 0;
					else if (exceptStatus == DayStatus.OPEN)
						return 100;
					return OpenClose.this.getLoadAt(instant);
				}

				@Override
				public Day getLastDayIfAny() {
					return offAfter;
				}
			};
		return this;
	}

	@Override
	public Day getLastDayIfAny() {
		return offAfter;
	}

}
