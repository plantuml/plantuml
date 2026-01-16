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

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;
import java.util.Locale;

import net.sourceforge.plantuml.project.Value;
import net.sourceforge.plantuml.project.core.PrintScale;

public class Day implements Comparable<Day>, Value {

	static final public long MILLISECONDS_PER_DAY = 1000L * 3600L * 24;

	private final LocalDateTime utcDateTime;

	public static Day epoch() {
		return new Day(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
	}

	public static Day create(int year, String month, int dayOfMonth) {
		return new Day(LocalDateTime.of(year, MonthUtils.fromString(month), dayOfMonth, 0, 0));
	}

	public static Day create(int year, int month, int dayOfMonth) {
		return new Day(LocalDateTime.of(year, month, dayOfMonth, 0, 0));
	}

	public static Day create(long ms) {
		return new Day(LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneOffset.UTC));
	}

	public static Day today() {
		return new Day(LocalDateTime.now(ZoneOffset.UTC));
	}

	private Day(LocalDateTime utcDateTime) {
		this.utcDateTime = utcDateTime;
	}

	public String toStringShort(Locale locale) {
		return YearMonthUtils.shortName(monthYear(), locale) + " " + getDayOfMonth();
	}

	public int getWeekOfYear(WeekNumberStrategy strategy) {
		final WeekFields wf = WeekFields.of(strategy.getFirstDayOfWeek(), strategy.getMinimalDaysInFirstWeek());
		return utcDateTime.toLocalDate().get(wf.weekOfYear());
	}

	public Day increment() {
		return addDays(1);
	}

	public Day decrement() {
		return addDays(-1);
	}

	public Day addDays(int nday) {
		return new Day(this.utcDateTime.toLocalDate().plusDays(nday).atStartOfDay());
	}

	public final int getAbsoluteDayNum() {
		return (int) Math.floorDiv(getMillis(), MILLISECONDS_PER_DAY);
	}

	public final long getMillis() {
		return utcDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	public int year() {
		return monthYear().getYear();
	}

	@Override
	public String toString() {
		return monthYear().toString() + "/" + getDayOfMonth();
	}

	@Override
	public int hashCode() {
		return utcDateTime.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != Day.class)
			return false;

		final Day other = (Day) obj;
		return this.utcDateTime.equals(other.utcDateTime);
	}

	@Override
	public int compareTo(Day other) {
		return this.utcDateTime.compareTo(other.utcDateTime);
	}

	public final int getDayOfMonth() {
		return utcDateTime.getDayOfMonth();
	}

	public Month month() {
		return utcDateTime.getMonth();
	}

	public YearMonth monthYear() {
		return YearMonth.from(utcDateTime);
	}

	public DayOfWeek getDayOfWeek() {
		return utcDateTime.getDayOfWeek();
	}

	public static Day min(Day d1, Day d2) {
		return d1.compareTo(d2) <= 0 ? d1 : d2;
	}

	public static Day max(Day d1, Day d2) {
		return d1.compareTo(d2) >= 0 ? d1 : d2;
	}

	public Day increment(PrintScale printScale) {
		if (printScale == PrintScale.WEEKLY)
			return this.addDays(7);
		return increment();
	}

	public Day roundDayDown() {
		// Same as legacy: floor(ms / day) * day
		final long ms = getMillis();
		final long floored = Math.floorDiv(ms, MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY;
		return Day.create(floored);
	}

	public Day roundDayUp() {
		// Same as legacy: ceil(ms / day) * day
		final long ms = getMillis();
		final long ceiled = Math.floorDiv(ms + MILLISECONDS_PER_DAY - 1, MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY;
		return Day.create(ceiled);
	}

}
