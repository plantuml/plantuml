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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6104 $
 *
 */
package net.sourceforge.plantuml.project;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Day implements Comparable<Day> {

	private final int numDay;
	private final Month month;
	private final int year;
	private final WeekDay weekDay;

	private Day(int year, Month month, int numDay, WeekDay weekDay) {
		this.year = year;
		this.month = month;
		this.numDay = numDay;
		this.weekDay = weekDay;
	}

	public static boolean isValidDesc(String desc) {
		if (desc.matches("^\\d{4}/\\d{2}/\\d{2}$")) {
			return true;
		}
		if (desc.matches("^\\d{2}-[A-Za-z]{3}-\\d{4}$")) {
			return true;
		}
		return false;
	}

	public Day(String desc) {
		if (desc.matches("^\\d{4}/\\d{2}/\\d{2}$")) {
			this.year = Integer.parseInt(desc.substring(0, 4));
			this.month = Month.fromNum(Integer.parseInt(desc.substring(5, 7)));
			this.numDay = Integer.parseInt(desc.substring(8, 10));
		} else if (desc.matches("^\\d{2}-[A-Za-z]{3}-\\d{4}$")) {
			this.year = Integer.parseInt(desc.substring(7, 11));
			this.month = Month.valueOf(desc.substring(3, 6));
			this.numDay = Integer.parseInt(desc.substring(0, 2));
		} else {
			throw new IllegalArgumentException(desc);
		}
		final int wd = new GregorianCalendar(year, month.getNum() - 1, numDay).get(Calendar.DAY_OF_WEEK);
		this.weekDay = WeekDay.values()[wd - 1];
	}

	public Day next(DayClose dayClose) {
		if (dayClose == null) {
			return nextInternal();
		}
		if (dayClose.isClose(this)) {
			throw new IllegalArgumentException();
		}
		Day result = nextInternal();
		while (dayClose.isClose(result)) {
			result = result.nextInternal();
		}
		return result;
	}

	public Day prev(DayClose dayClose) {
		if (dayClose == null) {
			return prevInternal();
		}
		if (dayClose.isClose(this)) {
			throw new IllegalArgumentException();
		}
		Day result = prevInternal();
		while (dayClose.isClose(result)) {
			result = result.prevInternal();
		}
		return result;
	}

	private Day nextInternal() {
		if (numDay < month.getNbDays(year)) {
			return new Day(year, month, numDay + 1, weekDay.next());
		}
		final Month next = month.next();
		if (next == null) {
			return new Day(year + 1, Month.JAN, 1, weekDay.next());
		}
		return new Day(year, next, 1, weekDay.next());
	}

	private Day prevInternal() {
		if (numDay > 1) {
			return new Day(year, month, numDay - 1, weekDay.prev());
		}
		final Month prev = month.prev();
		if (prev == null) {
			return new Day(year - 1, Month.DEC, 31, weekDay.prev());
		}
		return new Day(year, prev, prev.getNbDays(year), weekDay.prev());
	}

	@Override
	public String toString() {
		return "" + weekDay + " " + year + "-" + month + "-" + String.format("%02d", numDay);
	}

	public final int getNumDay() {
		return numDay;
	}

	public final Month getMonth() {
		return month;
	}

	public final int getYear() {
		return year;
	}

	public int compareTo(Day other) {
		if (year > other.year) {
			return 1;
		}
		if (year < other.year) {
			return -1;
		}
		final int cmpMonth = month.compareTo(other.month);
		if (cmpMonth != 0) {
			return cmpMonth;
		}
		return numDay - other.numDay;
	}

	@Override
	public boolean equals(Object obj) {
		final Day this2 = (Day) obj;
		return this.numDay == this2.numDay && this.month == this2.month && this.year == this2.year;
	}

	@Override
	public int hashCode() {
		return numDay * 420 + year + month.hashCode();
	}

	public final WeekDay getWeekDay() {
		return weekDay;
	}

}
