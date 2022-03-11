/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.*;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.valueOf;

public class ComplementSeveralDays implements Something {

	public IRegex toRegex(String suffix) {
		return new RegexConcat(
			new RegexLeaf(
				getKey(suffix),
				"(\\d+)[%s]+(day|week|month|quarter)s?(?:[%s]+and[%s]+(\\d+)[%s]+(day|week|month|quarter)s?)?"
			)
		);
	}

	public Failable<Load> getMe(GanttDiagram diagram, RegexResult arg, String suffix) {
		RegexPartialMatch partialRegexpMatch = arg.get(getKey(suffix));

		final Period period = getPeriod(diagram, partialRegexpMatch, 0);
		final Period period2 = getPeriod(diagram, partialRegexpMatch, 1);

		return Failable.ok(Load.inWinks(period.getDays() + period2.getDays()));
	}

	private static Period getPeriod(GanttDiagram diagram, RegexPartialMatch regexp, int periodIndex) {
		if (!hasPeriod(regexp, periodIndex)) {
			return Period.ZERO;
		}

		AmountUnit duration = mapDuration(regexp, periodIndex);
		int amount = duration.getAmount();
		ChronoUnit chronoUnit = duration.getUnit();

		if (chronoUnit.compareTo(DAYS) < 0) {
			throw new IllegalArgumentException("Lowest resolution of supported chrono units is " + DAYS + " but was " + chronoUnit);
		}

		if (chronoUnit.equals(DAYS)) {
			return Period.ofDays(amount);
		}

		// Using ceiling with implicit assumption that the required time (in days) should be rounded, i.e.
		//  when we have 2.1 days, we'd rather have 2 day slot on the chart
		return Period.ofDays((int) Math.round(
			chronoUnit.getDuration()
				.multipliedBy(amount).toDays()
				/ (double) 7
				* diagram.daysInWeek()
		));
	}

	private static AmountUnit mapDuration(RegexPartialMatch regexp, int periodIndex) {
		final int amount = Integer.parseInt(getAmount(regexp, periodIndex));
		String untypedChronoUnit = getChronoUnit(regexp, periodIndex);

		if (untypedChronoUnit.toLowerCase(Locale.ROOT).equals("quarter")) {
			return convertQuartersToMonths(amount);
		}

		return AmountUnit.of(amount, toTypedChronoUnit(untypedChronoUnit));
	}

	private static AmountUnit convertQuartersToMonths(int amount) {
		return AmountUnit.of(amount * 3, ChronoUnit.MONTHS);
	}

	private static boolean hasPeriod(RegexPartialMatch regexp, int periodIndex) {
		return getAmount(regexp, periodIndex) != null;
	}

	private static String getAmount(RegexPartialMatch regexp, int periodIndex) {
		return regexp.get(indexToOffset(periodIndex));
	}

	private static String getChronoUnit(RegexPartialMatch regexp, int periodIndex) {
		return regexp.get(indexToOffset(periodIndex) + 1);
	}

	private static ChronoUnit toTypedChronoUnit(String chronoUnit) {
		return valueOf(
			(chronoUnit + "S")
				.toUpperCase(Locale.ROOT)
		);
	}

	private static int indexToOffset(int periodIndex) {
		return periodIndex * 2;
	}

	private static String getKey(String suffix) {
		return "COMPLEMENT" + suffix;
	}

	private static class AmountUnit {
		final int amount;
		final ChronoUnit unit;

		private AmountUnit(int amount, ChronoUnit unit) {
			this.amount = amount;
			this.unit = unit;
		}

		public static AmountUnit of(int amount, ChronoUnit unit) {
			return new AmountUnit(amount, unit);
		}

		public int getAmount() {
			return amount;
		}

		public ChronoUnit getUnit() {
			return unit;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			AmountUnit that = (AmountUnit) o;
			return amount == that.amount && unit == that.unit;
		}

		@Override
		public int hashCode() {
			return Objects.hash(amount, unit);
		}

		@Override
		public String toString() {
			return "AmountUnit{" +
				"amount=" + amount +
				", unit=" + unit +
				'}';
		}
	}
}
