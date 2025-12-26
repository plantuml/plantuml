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

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.function.Function;

/**
 * A piecewise constant function defined on a {@link LocalDateTime} axis.
 *
 * <p>
 * This abstraction is intended to model human workload allocation over time.
 * In many planning scenarios, a person's effective contribution is constant
 * over a given period (for example, a week, a sprint, or a contractual interval),
 * and may change only at discrete boundaries.
 * </p>
 *
 * <p>
 * The function returns a {@link Fraction} rather than an integer because
 * real-world allocations are often partial. For example, a person may be
 * assigned at 30% to a task, which can be represented as a fraction.
 * </p>
 *
 * <p>
 * A zero value is meaningful and represents no allocation during a segment.
 * </p>
 *
 * <p>
 * Implementations will likely not store an explicit list of segments.
 * Instead, segments may be computed dynamically, which is why this API
 * exposes an {@link Iterator} rather than a materialized collection.
 * </p>
 *
 * <p>
 * For example, if a resource works on weekdays from 08:00 to 12:00 and from
 * 14:00 to 18:00, and this rule applies across the whole time scale supported
 * by Java, an explicit representation would lead to an almost unbounded number
 * of segments. A lazy, on-demand traversal is therefore more appropriate.
 * </p>
 *
 * <p>
 * Segments do not overlap and are sorted by their start instant.
 * </p>
 */
public interface PiecewiseConstant extends Function<LocalDateTime, Fraction> {

	/**
	 * Returns the workload value at the given instant.
	 *
	 * <p>
	 * Typical semantics in this model:
	 * </p>
	 * <ul>
	 *   <li>A positive fraction represents an active allocation (e.g., 3/10 for 30%).</li>
	 *   <li>Zero represents no allocation for that instant.</li>
	 * </ul>
	 *
	 * <p>
	 * Implementations decide whether to return {@code null} or throw an exception
	 * when the function is undefined at that instant.
	 * </p>
	 */
	@Override
	Fraction apply(LocalDateTime instant);

	/**
	 * Returns an iterator over segments in ascending chronological order, starting from
	 * the segment that contains the given instant.
	 *
	 * <p>
	 * <strong>Important:</strong> The first segment returned by this iterator is the segment
	 * that contains {@code instant}, but this segment does <strong>not necessarily start</strong>
	 * at {@code instant}. The segment may have started before {@code instant} and
	 * extends beyond it. Subsequent segments follow in chronological order.
	 * </p>
	 *
	 * <p>
	 * This method is intended for efficient forward traversal when computing or aggregating
	 * workload over time windows without materializing all segments.
	 * </p>
	 *
	 * <p>
	 * This iterator may represent a large or conceptually unbounded sequence,
	 * depending on the underlying workload rules.
	 * </p>
	 *
	 * @param instant the instant from which to begin iteration; the first segment
	 *                      returned will be the one containing this instant
	 * @return an iterator over segments containing and following the given instant
	 */
	Iterator<Segment> iterateSegmentsFrom(LocalDateTime instant);
	
	/**
	 * Returns an iterator over segments in descending chronological order, starting from
	 * the segment that contains the given instant.
	 *
	 * <p>
	 * <strong>Important:</strong> The first segment returned by this iterator is the segment
	 * that contains {@code instant}, but this segment does <strong>not necessarily start</strong>
	 * at {@code instant}. The segment may have started before {@code instant} and
	 * extends beyond it. Subsequent segments follow in chronological order in past.
	 * </p>
	 *
	 * <p>
	 * This method is intended for efficient backward traversal when computing or aggregating
	 * workload over time windows without materializing all segments.
	 * </p>
	 *
	 * <p>
	 * This iterator may represent a large or conceptually unbounded sequence,
	 * depending on the underlying workload rules.
	 * </p>
	 *
	 * @param instant the instant from which to begin iteration; the first segment
	 *                      returned will be the one containing this instant
	 * @return an iterator over segments containing and preceding the given instant
	 */
	Iterator<Segment> iterateSegmentsBackwardFrom(LocalDateTime instant);
	
	/**
	 * Returns the segment containing the given instant.
	 *
	 * <p>
	 * This is a convenience method equivalent to calling {@code iterateSegmentsFrom(instant).next()}.
	 * The returned segment has boundaries {@code [startInclusive, endExclusive)} that contain
	 * the given instant, meaning:
	 * </p>
	 * <ul>
	 *   <li>{@code startInclusive <= instant}</li>
	 *   <li>{@code instant < endExclusive}</li>
	 * </ul>
	 *
	 * <p>
	 * The segment's start time will typically be before or equal to the given instant,
	 * not necessarily at the instant itself.
	 * </p>
	 *
	 * @param instant the instant to query
	 * @return the segment containing this instant
	 * @throws java.util.NoSuchElementException if the iterator is empty (should not occur in normal usage)
	 */
	Segment segmentAt(LocalDateTime instant);

}
