/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 * Original Author:  Arnaud Roques, Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Immutable implementation of {@link PiecewiseConstant} that models a workload
 * pattern defined by specific dates.
 *
 * <p>
 * Each specific {@link LocalDate} value is associated with a constant
 * {@link Fraction} representing the workload for that day (for example 3/10 for
 * 30%). A zero value is meaningful and represents no allocation on that day.
 * </p>
 *
 * <p>
 * For any date not explicitly mapped, a default value is returned.
 * </p>
 */
public final class PiecewiseConstantSpecificDays implements PiecewiseConstant {
	
	/**
	 * Mapping of specific dates to their corresponding workload fractions.
	 */
	private final Map<LocalDate, Fraction> dayToFraction;
	
	/**
	 * Default workload fraction for dates not explicitly mapped.
	 */
	private final Fraction defaultValue;
	
	/**
	 * Constructs a PiecewiseConstantSpecificDays with the given default value.
	 * 
	 * @param value The default workload fraction.
	 */
	private PiecewiseConstantSpecificDays(Fraction value) {
		this(value, Map.of());
	}
	
	/**
	 * Constructs a PiecewiseConstantSpecificDays with the given default value
	 * and specific day-to-fraction mapping.
	 * 
	 * @param value         The default workload fraction.
	 * @param dayToFraction Mapping of specific dates to their workload fractions.
	 */
	private PiecewiseConstantSpecificDays(Fraction value, Map<LocalDate, Fraction> dayToFraction) {
		defaultValue = value;
		this.dayToFraction = dayToFraction;
	}


	/** (non-Javadoc)
	 * @see net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant#apply(java.time.LocalDateTime)
	 */
	@Override
	public Fraction apply(LocalDateTime instant) {
		LocalDate localDate = instant.toLocalDate();
		if(dayToFraction.containsKey(localDate)) {
			return dayToFraction.get(localDate);
		}
		
		return defaultValue;
	}
	
	/**
	 * Creates a PiecewiseConstantSpecificDays with the given default value.
	 * 
	 * @param value The default workload fraction.
	 * @return A new PiecewiseConstantSpecificDays instance.
	 */
	public static PiecewiseConstantSpecificDays of(Fraction value) {
		return new PiecewiseConstantSpecificDays(value);
	}

	/**
	 * Returns a new PiecewiseConstantSpecificDays with the specified day
	 * associated with the given fraction.
	 * 
	 * @param day   The specific date to associate.
	 * @param value The workload fraction for the specified date.
	 * @return A new PiecewiseConstantSpecificDays instance with the updated mapping.
	 */
	public PiecewiseConstantSpecificDays withDay(LocalDate day, Fraction value) {
		Map<LocalDate, Fraction> newDayToFraction = new HashMap<>(dayToFraction);
		newDayToFraction.put(day, value);
		return new PiecewiseConstantSpecificDays(defaultValue, newDayToFraction);
	}
	
	/**
	 * Returns an iterator of segments in ascending chronological order, starting from
	 * the segment that contains the given instant.
	 *
	 * <p>
	 * <strong>Implementation Detail:</strong> This particular implementation happens to return
	 * segments that represent exactly one full day each. However, <strong>callers should not
	 * rely on this detail</strong>. A conforming implementation could return segments of any
	 * duration (for example, 1-hour segments) as long as they satisfy the interface contract.
	 * Obviously, smaller segments would be less efficient for most use cases.
	 * </p>
	 *
	 * <p>
	 * <strong>Important:</strong> The first segment returned contains {@code instant},
	 * but does <strong>not necessarily start</strong> at {@code instant}. In this
	 * implementation, each segment represents a full day starting at midnight (00:00).
	 * </p>
	 *
	 * <p>
	 * For example, if {@code instant} is 2025-01-15 at 14:30, the first segment
	 * returned will be [2025-01-15 00:00, 2025-01-16 00:00), which started before the
	 * given instant but contains it.
	 * </p>
	 *
	 * <p>
	 * <strong>Optimization Note:</strong> An implementation may choose to merge consecutive
	 * segments with identical load values into a single longer segment. For instance, if
	 * 2025-01-16 and 2025-01-17 both have the same workload, they could be returned as
	 * a single segment [2025-01-16 00:00, 2025-01-18 00:00). This current implementation
	 * does not perform such optimization and always returns daily segments.
	 * </p>
	 *
	 * <p>
	 * This iterator is conceptually unbounded and generates segments lazily.
	 * </p>
	 *
	 * @param instant the instant from which to begin iteration; the first segment
	 *                      returned will be the one containing this instant
	 * @return an iterator over segments containing and following the given instant
	 * @throws NullPointerException if {@code instant} is null
	 */
	@Override
	public Iterator<Segment> iterateSegmentsFrom(LocalDateTime instant) {
		Objects.requireNonNull(instant, "instant");

		return new Iterator<Segment>() {

			private LocalDate cursorDate = firstSegmentDate(instant);

			@Override
			public boolean hasNext() {
				// The rule can be applied indefinitely.
				return true;
			}

			@Override
			public Segment next() {
				if (cursorDate == null) {
					throw new NoSuchElementException();
				}

				final LocalDateTime start = cursorDate.atStartOfDay();
				final LocalDateTime end = cursorDate.plusDays(1).atStartOfDay();
				final Fraction value = apply(start);

				cursorDate = cursorDate.plusDays(1);
				return new Segment(start, end, value);
			}
		};
	}

	
	/**
	 * Computes the date of the segment that contains {@code instant}.
	 *
	 * <p>
	 * Since each segment in this implementation represents a full day starting at midnight,
	 * this method simply extracts the date component from {@code instant}. The segment
	 * for that date will contain the given instant, regardless of the time of day.
	 * </p>
	 *
	 * @param instant the instant to find the containing segment for
	 * @return the date of the daily segment containing the given instant
	 */
	private static LocalDate firstSegmentDate(LocalDateTime instant) {
		return instant.toLocalDate();
	}

	/** (non-Javadoc)
	 * @see net.sourceforge.plantuml.project.ngm.math.AbstractPiecewiseConstant#segmentAt(java.time.LocalDateTime)
	 */
	@Override
	public Segment segmentAt(LocalDateTime instant) {
		throw new UnsupportedOperationException("Work In Progress");
		
	}

}
