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
package net.sourceforge.plantuml.project.time;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.plantuml.project.Value;
import net.sourceforge.plantuml.project.core.PrintScale;

public class Day implements Comparable<Day>, Value {

	static final public long MILLISECONDS_PER_DAY = 1000L * 3600L * 24;
	static final private Calendar gmt = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

	private final int dayOfMonth;
	private final MonthYear monthYear;
	private final long milliseconds;

	public static Day create(int year, String month, int dayOfMonth) {
		return new Day(year, Month.fromString(month), dayOfMonth);
	}

	public static Day create(int year, int month, int dayOfMonth) {
		return new Day(year, Month.values()[month - 1], dayOfMonth);
	}

	public static Day create(long ms) {
		return new Day(ms);
	}

	public static Day today() {
		return create(System.currentTimeMillis());
	}

	public String toStringShort(Locale locale) {
		return monthYear.shortName(locale) + " " + dayOfMonth;
	}

	public int getWeekOfYear(WeekNumberStrategy strategy) {
		synchronized (gmt) {
			gmt.clear();
			gmt.setTimeInMillis(milliseconds);
			gmt.setFirstDayOfWeek(strategy.getFirstDayOfWeekAsLegacyInt());
			gmt.setMinimalDaysInFirstWeek(strategy.getMinimalDaysInFirstWeek());
			return gmt.get(Calendar.WEEK_OF_YEAR);
		}
	}

	private Day(int year, Month month, int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
		this.monthYear = MonthYear.create(year, month);
		synchronized (gmt) {
			gmt.clear();
			gmt.set(year, month.ordinal(), dayOfMonth);
			this.milliseconds = gmt.getTimeInMillis();
		}
	}

	private Day(long ms) {
		this.milliseconds = ms;
		synchronized (gmt) {
			gmt.clear();
			gmt.setTimeInMillis(ms);
			final int year = gmt.get(Calendar.YEAR);
			final int month = gmt.get(Calendar.MONTH);
			final int dayOfMonth = gmt.get(Calendar.DAY_OF_MONTH);
			this.dayOfMonth = dayOfMonth;
			this.monthYear = MonthYear.create(year, Month.values()[month]);
		}

	}

	public Day increment() {
		return addDays(1);
	}

	public Day decrement() {
		return addDays(-1);
	}

	public Day addDays(int nday) {
		return create(MILLISECONDS_PER_DAY * (getAbsoluteDayNum() + nday));
	}

	public final int getAbsoluteDayNum() {
		return (int) (milliseconds / MILLISECONDS_PER_DAY);
	}

	public final long getMillis() {
		return milliseconds;
	}

	public int year() {
		return monthYear.year();
	}

	private int internalNumber() {
		return year() * 100 * 100 + month().ordinal() * 100 + dayOfMonth;
	}

	@Override
	public String toString() {
		return monthYear.toString() + "/" + dayOfMonth;
	}

	@Override
	public int hashCode() {
		return monthYear.hashCode() + dayOfMonth * 17;
	}

	@Override
	public boolean equals(Object obj) {
		final Day other = (Day) obj;
		return other.internalNumber() == this.internalNumber();
	}

	public final int getDayOfMonth() {
		return dayOfMonth;
	}

	private int daysPerMonth() {
		return month().getDaysPerMonth(year());
	}

	public Month month() {
		return monthYear.month();
	}

	public MonthYear monthYear() {
		return monthYear;
	}

	// https://en.wikipedia.org/wiki/Zeller%27s_congruence
	public DayOfWeek getDayOfWeek() {
		final int q = dayOfMonth;
		final int m = month().m();
		final int y = m >= 13 ? year() - 1 : year();
		final int k = y % 100;
		final int j = y / 100;
		final int h = ((q + 13 * (m + 1) / 5) + k + k / 4 + j / 4 + 5 * j) % 7;
		return DayOfWeek.fromH(h);
	}

	public int compareTo(Day other) {
		return this.internalNumber() - other.internalNumber();
	}

	public static Day min(Day wink1, Day wink2) {
		if (wink2.internalNumber() < wink1.internalNumber())
			return wink2;

		return wink1;
	}

	public static Day max(Day wink1, Day wink2) {
		if (wink2.internalNumber() > wink1.internalNumber())
			return wink2;

		return wink1;
	}

	public Day increment(PrintScale printScale) {
		if (printScale == PrintScale.WEEKLY)
			return this.addDays(7);
		return increment();
	}

	public Day roundDayDown() {
		return new Day((milliseconds / MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY);
	}

	public Day roundDayUp() {
		return new Day(((milliseconds + MILLISECONDS_PER_DAY - 1) / MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY);
	}

}
