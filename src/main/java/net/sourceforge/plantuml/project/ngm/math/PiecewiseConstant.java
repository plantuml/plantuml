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
	default Iterator<Segment> iterateSegmentsFrom(LocalDateTime instant) {
		throw new UnsupportedOperationException("Not implemented now");
	}
	
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
	default Segment segmentAt(LocalDateTime instant) {
		return iterateSegmentsFrom(instant).next();
	}
	

	/**
	 * A constant workload segment.
	 *
	 * <p>
	 * Within {@code [startInclusive, endExclusive)}, the workload is assumed to be constant.
	 * This mirrors how resource planning is often defined: a stable allocation over a
	 * time range with changes occurring only at explicit boundaries.
	 * </p>
	 *
	 * <p>
	 * The value may be zero to explicitly model periods with no assigned workload.
	 * </p>
	 */
	final class Segment {
		private final LocalDateTime startInclusive;
		private final LocalDateTime endExclusive;
		private final Fraction value;

		public Segment(LocalDateTime startInclusive, LocalDateTime endExclusive, Fraction value) {
			if (startInclusive != null && endExclusive != null && !startInclusive.isBefore(endExclusive))
				throw new IllegalArgumentException("startInclusive must be before endExclusive");

			this.startInclusive = startInclusive;
			this.endExclusive = endExclusive;
			this.value = value;
		}

		public LocalDateTime getStartInclusive() {
			return startInclusive;
		}

		public LocalDateTime getEndExclusive() {
			return endExclusive;
		}

		public Fraction getValue() {
			return value;
		}
		
		/**
		 * Splits this segment into two contiguous segments at the given instant.
		 *
		 * <p>
		 * The split instant {@code time} must lie within the bounds of this segment,
		 * that is:
		 * </p>
		 * <pre>
		 * startInclusive &lt;= time &lt; endExclusive
		 * </pre>
		 *
		 * <p>
		 * The returned array always contains exactly two segments:
		 * </p>
		 * <ul>
		 *   <li>the first segment spans from {@code startInclusive} (inclusive)
		 *       to {@code time} (exclusive),</li>
		 *   <li>the second segment spans from {@code time} (inclusive)
		 *       to {@code endExclusive} (exclusive).</li>
		 * </ul>
		 *
		 * <p>
		 * Both resulting segments carry the same {@link #getValue() value} as this
		 * segment. The two segments are guaranteed to be contiguous and non-overlapping.
		 * </p>
		 *
		 * <p>
		 * If {@code time} is equal to {@code startInclusive}, the first segment will
		 * be empty and the second segment will be identical to this segment.
		 * If {@code time} is equal to {@code endExclusive}, the second segment will
		 * be empty and the first segment will be identical to this segment.
		 * </p>
		 *
		 * @param time the instant at which to split this segment
		 * @return an array of exactly two segments resulting from the split,
		 *         in chronological order
		 * @throws IllegalArgumentException if {@code time} is {@code null} or lies
		 *         outside the bounds of this segment
		 */
		public Segment[] split(LocalDateTime time) {
			throw new UnsupportedOperationException("wip");
		}

		@Override
		public String toString() {
			return "Segment{" + "startInclusive=" + startInclusive + ", endExclusive=" + endExclusive + ", value="
					+ value + '}';
		}
	}
}
