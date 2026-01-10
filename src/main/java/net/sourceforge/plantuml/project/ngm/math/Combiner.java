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
 * Original Author:  Arnaud Roques, Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Utilities to combine multiple {@link PiecewiseConstant} functions.
 *
 * <p>
 * In the New Gantt Model context, {@link PiecewiseConstant} typically represents
 * a time-dependent workload allocation expressed as a {@link Fraction}.
 * </p>
 */
public class Combiner {
	/**
	 * Combines several workload functions by summing their values.
	 *
	 * <p>
	 * This operation is meant to model multiple resources assigned to the same task.
	 * For example, if two people are each allocated at 1/2 on a given day, the
	 * resulting combined workload for that day is 1 (i.e., one full-time equivalent).
	 * </p>
	 *
	 * @param functions the workload functions to add
	 * @return a new {@link PiecewiseConstant} representing the summed allocation
	 */
	public static PiecewiseConstant sum(PiecewiseConstant... functions) {
		throw new UnsupportedOperationException("Work in progress");
	}

	/**
	 * Combines several functions by multiplying their values.
	 *
	 * <p>
	 * This operation is particularly useful to combine an "assignment" with
	 * an "availability calendar".
	 * </p>
	 *
	 * <p>
	 * Availability calendars typically use only {@code 0} and {@code 1} to indicate
	 * closed/open periods. Multiplying a workload allocation by such a calendar
	 * acts as a logical AND:
	 * </p>
	 *
	 * <ul>
	 *   <li>{@code 1 * allocation = allocation} (open period)</li>
	 *   <li>{@code 0 * allocation = 0} (closed period)</li>
	 * </ul>
	 *
	 * <p>
	 * This allows you to automatically "mask" allocations during non-working times.
	 * </p>
	 *
	 * <p>
	 * It also allows combining calendars with each other. When multiple
	 * availability calendars are multiplied, the result represents the
	 * intersection of their opening periods: the combined calendar is open
	 * only when all input calendars are open.
	 * </p>
	 *
	 * <p>
	 * For example, you may have one calendar for weekday business days,
	 * another for public holidays, and another for daily working hours.
	 * Multiplying them yields a single calendar that is open only when all
	 * these constraints are simultaneously satisfied.
	 * </p>
	 *
	 * @param functions the functions to multiply
	 * @return a new {@link PiecewiseConstant} representing the combined result
	 */
	public static PiecewiseConstant product(PiecewiseConstant... functions) {
		return CombinedPiecewiseConstant.of(Fraction.PRODUCT)
				.with(functions);
	}
	
	
	/**
	 * A {@link PiecewiseConstant} that combines multiple functions using a specified
	 * operation.
	 *
	 * <p>
	 * This class allows flexible combination of workload functions by applying
	 * a user-defined operation (e.g., sum, product) to their values at each instant.
	 * </p>
	 */
	public static class CombinedPiecewiseConstant extends AbstractPiecewiseConstant {
		
		/** The functions to combine. */
		private final List<PiecewiseConstant> functions;
		
		/** The operation used to combine function values. */
		private final BiFunction<Fraction, Fraction, Fraction> valueCombiner;
		
		/** 
		 * Constructs a CombinedPiecewiseConstant with the specified functions and combiner.
		 * 
		 * @param functions the functions to combine
		 * @param valueCombiner the operation to combine function values
		 */
		private CombinedPiecewiseConstant(List<PiecewiseConstant> functions, BiFunction<Fraction, Fraction, Fraction> valueCombiner) {
			Objects.requireNonNull(functions, "functions must not be null");
			Objects.requireNonNull(valueCombiner, "valueCombiner must not be null");
			
			this.functions = functions;
			this.valueCombiner = valueCombiner;
		}
		
		/** 
		 * Constructs an empty CombinedPiecewiseConstant with the specified combiner.
		 * 
		 * @param valueCombiner the operation to combine function values
		 */
		private CombinedPiecewiseConstant(BiFunction<Fraction, Fraction, Fraction> valueCombiner) {
			this.valueCombiner = valueCombiner;
			this.functions = Collections.emptyList();
		}

		
		/**
		 * Retrieves the segment at the specified instant by combining segments
		 * from all constituent functions.
		 * 
		 * @param instant the time instant to query
		 * @return the combined segment at this instant
		 * @throws IllegalStateException if less than two functions are present
		 */
		@Override
		public Segment segmentAt(LocalDateTime instant, TimeDirection direction) {
			if(functions.size() < 2) {
				throw new IllegalStateException("At least two functions are required for combination");
			}
			
			List<Segment> segments = functions.stream()
					.map(f -> f.segmentAt(instant, direction))
					 .collect(Collectors.toList());
			
			return Segment.intersection(segments, valueCombiner);
		}
		
		/**
		 * Creates a new CombinedPiecewiseConstant with the specified value combiner but without any functions.
		 * 
		 * @param valueCombiner the operation to combine function values
		 * @return a new CombinedPiecewiseConstant instance
		 */
		public static CombinedPiecewiseConstant of(BiFunction<Fraction, Fraction, Fraction> valueCombiner) {
			return new CombinedPiecewiseConstant(valueCombiner);
		}
		
		/**
		 * Returns a new CombinedPiecewiseConstant that includes the specified functions.
		 * 
		 * @param functions the functions to add
		 * @return a new CombinedPiecewiseConstant with the added functions
		 */
		public CombinedPiecewiseConstant with(List<PiecewiseConstant> functions) {
			List<PiecewiseConstant> newFunctions = new ArrayList<>(this.functions);
			newFunctions.addAll(functions);
			return new CombinedPiecewiseConstant(newFunctions, valueCombiner);
		}
		
		/**
		 * Returns a new CombinedPiecewiseConstant that includes the specified functions.
		 * 
		 * @param functions the functions to add
		 * @return a new CombinedPiecewiseConstant with the added functions
		 */
		public CombinedPiecewiseConstant with(PiecewiseConstant... functions) {
			return with(Arrays.asList(functions));
		}
	}
}

