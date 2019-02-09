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

import java.util.Date;

public class DayAsDate implements Complement, Comparable<DayAsDate>, Subject {

	private final int year;
	private final int dayOfMonth;
	private final Month month;

	public static DayAsDate create(int year, String month, int dayOfMonth) {
		return new DayAsDate(year, Month.fromString(month), dayOfMonth);
	}

	public static DayAsDate create(int year, int month, int dayOfMonth) {
		return new DayAsDate(year, Month.values()[month - 1], dayOfMonth);
	}

	public static DayAsDate today() {
		final Date now = new Date();
		final int year = now.getYear() + 1900;
		final int month = now.getMonth() + 1;
		final int dayOfMonth = now.getDate();
		return create(year, month, dayOfMonth);
	}

	private DayAsDate(int year, Month month, int dayOfMonth) {
		this.year = year;
		this.dayOfMonth = dayOfMonth;
		this.month = month;
	}

	private int internalNumber() {
		return year * 100 * 100 + month.ordinal() * 100 + dayOfMonth;
	}

	@Override
	public String toString() {
		return "" + year + "/" + month + "/" + dayOfMonth;
	}

	@Override
	public int hashCode() {
		return year * 113 + dayOfMonth * 17 + month.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final DayAsDate other = (DayAsDate) obj;
		return other.internalNumber() == this.internalNumber();
	}

	public final int getDayOfMonth() {
		return dayOfMonth;
	}

	private int daysPerMonth() {
		return month.getDaysPerMonth(year);
	}

	public DayAsDate next() {
		int newDayOfMonth = dayOfMonth + 1;
		if (newDayOfMonth <= daysPerMonth()) {
			return new DayAsDate(year, month, newDayOfMonth);
		}
		assert newDayOfMonth > daysPerMonth();
		newDayOfMonth = 1;
		final Month newMonth = month.next();
		final int newYear = newMonth == Month.JANUARY ? year + 1 : year;
		return new DayAsDate(newYear, newMonth, newDayOfMonth);
	}

	public Month getMonth() {
		return month;
	}

	// https://en.wikipedia.org/wiki/Zeller%27s_congruence
	public DayOfWeek getDayOfWeek() {
		final int q = dayOfMonth;
		final int m = month.m();
		final int y = m >= 13 ? year - 1 : year;
		final int k = y % 100;
		final int j = y / 100;
		final int h = ((q + 13 * (m + 1) / 5) + k + k / 4 + j / 4 + 5 * j) % 7;
		return DayOfWeek.fromH(h);
	}

	public InstantDay asInstantDay(DayAsDate reference) {
		// if (this.compareTo(reference) < 0) {
		// throw new IllegalArgumentException();
		// }
		int cmp = 0;
		DayAsDate current = reference;
		while (current.compareTo(this) < 0) {
			current = current.next();
			cmp++;
		}
		return new InstantDay(cmp);
	}

	public int compareTo(DayAsDate other) {
		return this.internalNumber() - other.internalNumber();
	}

}
