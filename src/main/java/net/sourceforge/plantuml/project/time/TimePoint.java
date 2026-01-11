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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;
import java.util.Locale;

import net.sourceforge.plantuml.project.Value;
import net.sourceforge.plantuml.project.core.PrintScale;

public class TimePoint implements Comparable<TimePoint>, Value {

	static final public long MILLISECONDS_PER_DAY = 1000L * 3600L * 24;

	private final LocalDateTime utcDateTime;

	public static TimePoint epoch() {
		return new TimePoint(LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
	}

	public static TimePoint of(int year, String month, int dayOfMonth) {
		return new TimePoint(LocalDateTime.of(year, MonthUtils.fromString(month), dayOfMonth, 0, 0));
	}

	public static TimePoint of(LocalDateTime time) {
		return new TimePoint(time);
	}

	public static TimePoint of(LocalDate day) {
		return new TimePoint(day.atStartOfDay());
	}


	public static TimePoint of(int year, int month, int dayOfMonth) {
		return new TimePoint(LocalDateTime.of(year, month, dayOfMonth, 0, 0));
	}

	public static TimePoint create(long ms) {
		return new TimePoint(LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneOffset.UTC));
	}

	public static TimePoint nowUtc1() {
		return new TimePoint(LocalDateTime.now(ZoneOffset.UTC));
	}

	public static TimePoint todayUtcAtMidnight() {
		return new TimePoint(LocalDate.now(ZoneOffset.UTC).atStartOfDay());
	}

	private TimePoint(LocalDateTime utcDateTime) {
		this.utcDateTime = utcDateTime;
	}

	public LocalDateTime toLocalDateTime() {
		return utcDateTime;
	}

	public String toStringShort(Locale locale) {
		return YearMonthUtils.shortName(monthYear(), locale) + " " + getDayOfMonth();
	}

	public int getWeekOfYear(WeekNumberStrategy strategy) {
		final WeekFields wf = WeekFields.of(strategy.getFirstDayOfWeek(), strategy.getMinimalDaysInFirstWeek());
		return utcDateTime.toLocalDate().get(wf.weekOfYear());
	}

	public TimePoint increment() {
		return addDays(1);
	}

	public TimePoint decrement() {
		return addDays(-1);
	}

	public TimePoint addDays(int nday) {
		return new TimePoint(this.utcDateTime.toLocalDate().plusDays(nday).atStartOfDay());
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
		if (obj == null || obj.getClass() != TimePoint.class)
			return false;

		final TimePoint other = (TimePoint) obj;
		return this.utcDateTime.equals(other.utcDateTime);
	}

	@Override
	public int compareTo(TimePoint other) {
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

	public DayOfWeek toDayOfWeek() {
		return utcDateTime.getDayOfWeek();
	}

	public LocalDate toDay() {
		return utcDateTime.toLocalDate();
	}

	public static TimePoint min(TimePoint d1, TimePoint d2) {
		return d1.compareTo(d2) <= 0 ? d1 : d2;
	}

	public static TimePoint max(TimePoint d1, TimePoint d2) {
		return d1.compareTo(d2) >= 0 ? d1 : d2;
	}

	public TimePoint increment(PrintScale printScale) {
		if (printScale == PrintScale.WEEKLY)
			return this.addDays(7);
		return increment();
	}

	public TimePoint roundDayDown() {
		// Same as legacy: floor(ms / day) * day
		final long ms = getMillis();
		final long floored = Math.floorDiv(ms, MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY;
		return TimePoint.create(floored);
	}

	public TimePoint roundDayUp() {
		// Same as legacy: ceil(ms / day) * day
		final long ms = getMillis();
		final long ceiled = Math.floorDiv(ms + MILLISECONDS_PER_DAY - 1, MILLISECONDS_PER_DAY) * MILLISECONDS_PER_DAY;
		return TimePoint.create(ceiled);
	}

}
