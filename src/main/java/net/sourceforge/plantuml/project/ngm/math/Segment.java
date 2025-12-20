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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

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
public final class Segment {
	/**
	 * The start of the segment, inclusive. Can not be null.
	 */
	private final LocalDateTime startInclusive;
	
	/**
	 * The end of the segment, exclusive. Can not be null. Can not be before {@link #startInclusive}.
	 */
	private final LocalDateTime endExclusive;
	
	/**
	 * The constant workload value over this segment.
	 */
	private final Fraction value;

	/**
	 * Constructs a new {@code Segment} with the specified bounds and value.
	 *
	 * @param startInclusive the start of the segment (inclusive); must not be {@code null}
	 * @param endExclusive the end of the segment (exclusive); must not be {@code null}
	 *                     and must be after {@code startInclusive}
	 * @param value the constant workload value for this segment; must not be {@code null}
	 * @throws NullPointerException if any argument is {@code null}
	 * @throws IllegalArgumentException if {@code startInclusive} is not before {@code endExclusive}
	 */
	public Segment(LocalDateTime startInclusive, LocalDateTime endExclusive, Fraction value) {
		Objects.requireNonNull(startInclusive, "startInclusive must not be null");
		Objects.requireNonNull(endExclusive, "endExclusive must not be null");
		Objects.requireNonNull(value, "value must not be null");
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
	 * @throws NullPointerException if {@code time} is {@code null}
	 * @throws IllegalArgumentException if {@code time} is lies
	 *         outside the bounds of this segment
	 */
	public Segment[] split(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");
		if (!includes(time)) {
			throw new IllegalArgumentException("time must be within the segment bounds");
		}
		
		Segment first = new Segment(startInclusive, time, value);
		Segment second = new Segment(time, endExclusive, value);
		return new Segment[] { first, second };
	}
	
	/**
	 * Checks whether the given instant lies within this segment.
	 *
	 * <p>
	 * The check is inclusive of {@link #getStartInclusive() startInclusive}
	 * and exclusive of {@link #getEndExclusive() endExclusive}.
	 * </p>
	 *
	 * @param time the instant to check
	 * @return {@code true} if {@code time} lies within this segment,
	 *         {@code false} otherwise
	 * @throws NullPointerException if {@code time} is {@code null}
	 */
	public boolean includes(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");
		
		return (time.isEqual(startInclusive) || time.isAfter(startInclusive))
				&& time.isBefore(endExclusive);
	}

	@Override
	public String toString() {
		return "Segment{" + "startInclusive=" + startInclusive + ", endExclusive=" + endExclusive + ", value="
				+ value + '}';
	}
	
	/**
	 * Computes the intersection of multiple segments using multiplication
	 * to combine their values.
	 *
	 * <p>
	 * The intersection is defined as the overlapping time range shared by all
	 * provided segments. If no such overlapping range exists, an exception
	 * is thrown.
	 * </p>
	 *
	 * <p>
	 * The value of the resulting segment is computed by multiplying the values
	 * of all intersecting segments.
	 * </p>
	 *
	 * @param segments the list of segments to intersect
	 * @return a new {@link Segment} representing the intersection of the input
	 *         segments with the combined value
	 * @throws IllegalArgumentException if {@code segments} is empty or if no
	 *         overlapping range exists among the segments
	 * @throws NullPointerException if {@code segments} is {@code null}
	 */
	public static Segment intersection(List<Segment> segments) {
		return intersection(segments, Fraction.PRODUCT);
	}
	
	/**
	 * Computes the intersection of multiple segments.
	 *
	 * <p>
	 * The intersection is defined as the overlapping time range shared by all
	 * provided segments. If no such overlapping range exists, an exception
	 * is thrown.
	 * </p>
	 *
	 * <p>
	 * The value of the resulting segment is computed by applying the provided
	 * {@code valueFunction} to the values of all intersecting segments.
	 * </p>
	 *
	 * @param segments the list of segments to intersect
	 * @param valueFunction a function that combines two {@link Fraction} values
	 *                      into one; this function is applied iteratively to
	 *                      combine the values of all intersecting segments
	 * @return a new {@link Segment} representing the intersection of the input
	 *         segments with the combined value
	 * @throws IllegalArgumentException if {@code segments} is empty or if no
	 *         overlapping range exists among the segments
	 * @throws NullPointerException if {@code segments} or {@code valueFunction}
	 *         is {@code null}
	 */
	public static Segment intersection(List<Segment> segments, BiFunction<Fraction, Fraction, Fraction> valueFunction) {
		Objects.requireNonNull(segments, "segments must not be null");
		Objects.requireNonNull(valueFunction, "valueFunction must not be null");
		
		if(segments.isEmpty()) {
			throw new IllegalArgumentException("No segments to intersect");
		} else if(segments.size() == 1) {
			return segments.get(0);
		}
		
		LocalDateTime maxStart = null;
		LocalDateTime minEnd = null;
		Fraction combinedValue = null;
		for(Segment segment : segments) {
			if(maxStart == null || segment.getStartInclusive().isAfter(maxStart)) {
				maxStart = segment.getStartInclusive();
			}
			
			if(minEnd == null || segment.getEndExclusive().isBefore(minEnd)) {
				minEnd = segment.getEndExclusive();
			}
			
			// Combine values using the provided function
			if(combinedValue == null) {
				combinedValue = segment.getValue();
			} else {
				combinedValue = valueFunction.apply(combinedValue, segment.getValue());
			}
		}
		
		if(maxStart.isBefore(minEnd)) {
			// Overlapping segments exist between maxStart (inclusive) and minEnd (exclusive)
			return new Segment(maxStart, minEnd, combinedValue);
		} else {
			// No overlapping segments
			throw new IllegalArgumentException("Segments do not overlap");
		}
	}

	/** 
	 * Convenience overloads for array inputs.
	 * 
	 * It calls method with List argument {@link #intersection(List)}.
	 * 
	 * @throws NullPointerException if {@code segments} is {@code null}
	 */
	public static Segment intersection(Segment[] segments) {
		Objects.requireNonNull(segments, "segments must not be null");
		
		return intersection(Arrays.asList(segments));
	}
	
	/** 
	 * Convenience overloads for array inputs.
	 * 
	 * It calls method with List argument {@link #intersection(List, BiFunction)}.
	 * 
	 * @throws NullPointerException if {@code segments} is {@code null}
	 */
	public static Segment intersection(Segment[] segments, BiFunction<Fraction, Fraction, Fraction> valueFunction) {
		Objects.requireNonNull(segments, "segments must not be null");
		
		return intersection(Arrays.asList(segments), valueFunction);
	}
}