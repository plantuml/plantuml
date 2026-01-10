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
package net.sourceforge.plantuml.project.ngm.math;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Immutable implementation of {@link PiecewiseConstant} that models a weekly
 * workload pattern.
 *
 * <p>
 * Each of the 7 {@link DayOfWeek} values is associated with a constant
 * {@link Fraction} representing the workload for that day (for example 3/10 for
 * 30%). A zero value is meaningful and represents no allocation on that day.
 * </p>
 *
 * <p>
 * This implementation intentionally avoids storing a precomputed list of time
 * segments. Instead, it generates one-day {@link Segment} instances lazily as
 * the caller iterates forward in time. This design scales well for rules that
 * conceptually apply across the whole {@link LocalDateTime} timeline.
 * </p>
 *
 * <p>
 * Segments produced by {@link #iterateSegmentsFrom(LocalDateTime)} are daily
 * intervals of the form {@code [dayStart, nextDayStart)}.
 * </p>
 */
public final class PiecewiseConstantWeekday extends AbstractPiecewiseConstant {

	private static final EnumSet<DayOfWeek> ALL_DAYS = EnumSet.allOf(DayOfWeek.class);

	/**
	 * Immutable day-to-workload mapping.
	 */
	private final EnumMap<DayOfWeek, Fraction> workloadByDay;

	/**
	 * Private constructor expecting a fully validated, complete mapping containing
	 * all 7 days.
	 */
	private PiecewiseConstantWeekday(EnumMap<DayOfWeek, Fraction> workloadByDay) {
		this.workloadByDay = workloadByDay;
	}

	/**
	 * Creates a weekly pattern where all days share the same workload fraction.
	 *
	 * @param sameWorkload workload applied to every day of the week
	 * @return an immutable weekly pattern
	 */
	public static PiecewiseConstantWeekday of(Fraction sameWorkload) {
		Objects.requireNonNull(sameWorkload, "sameWorkload");

		final EnumMap<DayOfWeek, Fraction> map = new EnumMap<>(DayOfWeek.class);
		for (DayOfWeek day : ALL_DAYS)
			map.put(day, sameWorkload);

		return new PiecewiseConstantWeekday(map);
	}

	/**
	 * Returns a new instance with the workload of one day replaced.
	 *
	 * <p>
	 * This method supports a persistent/functional style of updates: the current
	 * instance is never modified.
	 * </p>
	 *
	 * @param day         day of week to update
	 * @param newWorkload new workload fraction for that day
	 * @return a new immutable instance
	 * @throws NullPointerException if any argument is null
	 */
	public PiecewiseConstantWeekday with(DayOfWeek day, Fraction newWorkload) {
		Objects.requireNonNull(day, "day");
		Objects.requireNonNull(newWorkload, "newWorkload");

		final EnumMap<DayOfWeek, Fraction> copy = new EnumMap<>(this.workloadByDay);
		copy.put(day, newWorkload);
		return new PiecewiseConstantWeekday(copy);
	}

	public Segment segmentAt(LocalDateTime instant, TimeDirection direction) {
		final LocalDateTime midnight = instant.toLocalDate().atStartOfDay();

		if (direction == TimeDirection.FORWARD) {
			final Fraction value = workloadByDay.get(midnight.getDayOfWeek());
			return Segment.forward(midnight, midnight.plusDays(1), value);
		}

		// BACKWARD: determine which day contains the instant
		final LocalDateTime end;
		if (instant.toLocalTime().equals(LocalTime.MIDNIGHT))
			// Midnight belongs to the previous day in backward iteration
			end = midnight.minusDays(1);
		else
			end = midnight;

		final Fraction value = workloadByDay.get(end.getDayOfWeek());
		return Segment.backward(end.plusDays(1), end, value);
	}
}
