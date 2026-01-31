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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.project.ngm.math.Combiner;
import net.sourceforge.plantuml.project.ngm.math.Fraction;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantSpecificDays;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantTimeWindow;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantWeekday;

public class OpenClose {

	private final Map<DayOfWeek, DayStatus> weekdayStatus = new EnumMap<>(DayOfWeek.class);
	private final Map<LocalDate, DayStatus> dayStatus = new HashMap<>();
	private LocalDate offBefore;
	private LocalDate offAfter;

	public OpenClose mutateMe(final OpenClose except) {
		final OpenClose result = new OpenClose();
		result.weekdayStatus.putAll(this.weekdayStatus);
		result.dayStatus.putAll(this.dayStatus);
		result.offBefore = this.offBefore;
		result.offAfter = this.offAfter;
		if (except != null) {
			result.weekdayStatus.putAll(except.weekdayStatus);
			result.dayStatus.putAll(except.dayStatus);
		}
		return result;

	}

	public int daysInWeek() {
		int result = 7;
		for (DayStatus status : weekdayStatus.values())
			if (status == DayStatus.CLOSE)
				result--;
		return result;
	}

	private boolean isThereSomeChangeAfter(LocalDate day) {
		if (weekdayStatus.size() > 0)
			return true;

		for (LocalDate tmp : dayStatus.keySet())
			if (tmp.compareTo(day) >= 0)
				return true;

		return false;
	}

	private boolean isThereSomeChangeBefore(LocalDate day) {
		if (weekdayStatus.size() > 0)
			return true;

		for (LocalDate tmp : dayStatus.keySet())
			if (tmp.compareTo(day) <= 0)
				return true;

		return false;
	}

	public boolean isClosed(LocalDate day) {
		final DayStatus status = getLocalStatus(day);
		if (status != null)
			return status == DayStatus.CLOSE;

		return false;
	}

	private DayStatus getLocalStatus(LocalDate day) {
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

	public void close(LocalDate day) {
		dayStatus.put(day, DayStatus.CLOSE);
	}

	public void open(LocalDate day) {
		dayStatus.put(day, DayStatus.OPEN);
	}

	public int getLoadAtDUMMY(LocalDate day) {
		return getLoatAtInternal(day);
	}

	public void setOffBeforeDate(LocalDate day) {
		this.offBefore = day;
	}

	public void setOffAfterDate(LocalDate day) {
		this.offAfter = day;
	}

	private int getLoatAtInternal(LocalDate day) {
		if (isClosed(day))
			return 0;

		return 100;
	}

	public LocalDate getLastDayIfAny() {
		return offAfter;
	}

	public PiecewiseConstant asPiecewiseConstant() {
		PiecewiseConstantWeekday weekPattern = PiecewiseConstantWeekday.of(Fraction.ONE);

		for (DayOfWeek day : weekdayStatus.keySet())
			if (weekdayStatus.get(day) == DayStatus.CLOSE)
				weekPattern = weekPattern.with(day, Fraction.ZERO);

		PiecewiseConstant result = weekPattern;

		if (dayStatus.isEmpty() == false) {
			PiecewiseConstantSpecificDays specificDaysClosed = PiecewiseConstantSpecificDays.of(Fraction.ONE);
			PiecewiseConstantSpecificDays specificDaysOpen = PiecewiseConstantSpecificDays.of(Fraction.ZERO);

			for (Map.Entry<LocalDate, DayStatus> entry : dayStatus.entrySet()) {
				final LocalDate date = entry.getKey();

				if (entry.getValue() == DayStatus.CLOSE)
					specificDaysClosed = specificDaysClosed.withDay(date, Fraction.ZERO);
				else
					specificDaysOpen = specificDaysOpen.withDay(date, Fraction.ONE);
			}

			result = Combiner.max(specificDaysOpen, Combiner.product(weekPattern, specificDaysClosed));
		}

		if (offBefore == null && offAfter == null)
			return result;

		if (offBefore == null)
			return Combiner.product(result,
					new PiecewiseConstantTimeWindow(LocalDateTime.MIN, offAfter.plusDays(1).atStartOfDay()));

		if (offAfter == null)
			return Combiner.product(result,
					new PiecewiseConstantTimeWindow(offBefore.atStartOfDay(), LocalDateTime.MAX));

		return Combiner.product(result,
				new PiecewiseConstantTimeWindow(offBefore.atStartOfDay(), offAfter.plusDays(1).atStartOfDay()));

	}

}
