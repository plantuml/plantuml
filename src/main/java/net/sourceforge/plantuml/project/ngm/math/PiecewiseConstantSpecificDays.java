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
public final class PiecewiseConstantSpecificDays extends AbstractPiecewiseConstant {
	
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
	
	/** (non-Javadoc)
	 * @see net.sourceforge.plantuml.project.ngm.math.AbstractPiecewiseConstant#segmentAt(java.time.LocalDateTime)
	 */
	@Override
	public Segment segmentAt(LocalDateTime instant) {
		final LocalDateTime start = instant.toLocalDate().atStartOfDay();
		final LocalDateTime end = start.plusDays(1);
		final Fraction value = apply(start);

		return new Segment(start, end, value);

	}

}
