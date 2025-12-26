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
 * A constant workload segment over a time interval.
 *
 * <p>
 * A segment represents a time interval {@code [a, b)} where the workload value
 * remains constant. The boundary convention is always: {@code a} is inclusive,
 * {@code b} is exclusive.
 * </p>
 *
 * <p>
 * Segments can be either {@link TimeDirection#FORWARD} or
 * {@link TimeDirection#BACKWARD}:
 * </p>
 * <ul>
 * <li><strong>FORWARD</strong>: {@code a} is chronologically before {@code b}
 * (used for forward iteration through time)</li>
 * <li><strong>BACKWARD</strong>: {@code a} is chronologically after {@code b}
 * (used for backward iteration through time)</li>
 * </ul>
 *
 * <p>
 * In both cases, {@code a} is the inclusive bound and {@code b} is the exclusive
 * bound. This allows seamless chaining of segments: the exclusive bound of one
 * segment becomes the inclusive bound of the next.
 * </p>
 *
 * <p>
 * The value may be zero to explicitly model periods with no assigned workload.
 * </p>
 *
 * <p>
 * Instances are created via the factory methods {@link #forward(LocalDateTime,
 * LocalDateTime, Fraction)} and {@link #backward(LocalDateTime, LocalDateTime,
 * Fraction)}.
 * </p>
 *
 * @see TimeDirection
 * @see Fraction
 */
public final class Segment {

	private final LocalDateTime aInclusive;

	private final LocalDateTime bExclusive;

	/**
	 * The constant workload value over this segment.
	 */
	private final Fraction value;

	private final TimeDirection direction;

	/**
	 * Private constructor. Use {@link #forward} or {@link #backward} factory methods.
	 */
	private Segment(TimeDirection direction, LocalDateTime aInclusive, LocalDateTime bExclusive, Fraction value) {
		Objects.requireNonNull(value);

		this.direction = direction;
		this.aInclusive = aInclusive;
		this.bExclusive = bExclusive;
		this.value = value;
	}

	/**
	 * Creates a forward segment where {@code a} is chronologically before {@code b}.
	 *
	 * <p>
	 * The segment represents the time interval {@code [a, b)} for forward iteration.
	 * </p>
	 *
	 * @param a     the inclusive start bound (chronologically earlier)
	 * @param b     the exclusive end bound (chronologically later)
	 * @param value the constant workload value for this segment
	 * @return a new forward segment
	 * @throws NullPointerException     if any argument is {@code null}
	 * @throws IllegalArgumentException if {@code a} is not chronologically before {@code b}
	 */
	public static Segment forward(LocalDateTime a, LocalDateTime b, Fraction value) {
		if (a.isBefore(b) == false)
			throw new IllegalArgumentException("b must be after a");
		return new Segment(TimeDirection.FORWARD, a, b, value);
	}

	/**
	 * Creates a backward segment where {@code a} is chronologically after {@code b}.
	 *
	 * <p>
	 * The segment represents the time interval {@code [a, b)} for backward iteration,
	 * where {@code a} is the later instant and {@code b} is the earlier instant.
	 * </p>
	 *
	 * <p>
	 * Think of it as a time traveler moving backward through time: they start at
	 * {@code a} (the later moment) and travel toward {@code b} (the earlier moment).
	 * </p>
	 *
	 * @param a     the inclusive start bound (chronologically later)
	 * @param b     the exclusive end bound (chronologically earlier)
	 * @param value the constant workload value for this segment
	 * @return a new backward segment
	 * @throws NullPointerException     if any argument is {@code null}
	 * @throws IllegalArgumentException if {@code b} is not chronologically before {@code a}
	 */
	public static Segment backward(LocalDateTime a, LocalDateTime b, Fraction value) {
		if (b.isBefore(a) == false)
			throw new IllegalArgumentException("b must be before a");
		return new Segment(TimeDirection.BACKWARD, a, b, value);
	}

	/**
	 * Returns the direction of this segment.
	 *
	 * @return {@link TimeDirection#FORWARD} if {@code a < b} chronologically,
	 *         {@link TimeDirection#BACKWARD} if {@code a > b} chronologically
	 */
	public TimeDirection getTimeDirection() {
		return direction;
	}

	/**
	 * Returns the inclusive bound of this segment.
	 *
	 * <p>
	 * For {@link TimeDirection#FORWARD} segments, this is the chronologically
	 * earlier instant. For {@link TimeDirection#BACKWARD} segments, this is the
	 * chronologically later instant.
	 * </p>
	 *
	 * @return the inclusive bound {@code a}
	 */
	public LocalDateTime aInclusive() {
		return aInclusive;
	}

	/**
	 * Returns the exclusive bound of this segment.
	 *
	 * <p>
	 * For {@link TimeDirection#FORWARD} segments, this is the chronologically
	 * later instant. For {@link TimeDirection#BACKWARD} segments, this is the
	 * chronologically earlier instant.
	 * </p>
	 *
	 * @return the exclusive bound {@code b}
	 */
	public LocalDateTime bExclusive() {
		return bExclusive;
	}

	/**
	 * Returns the constant workload value of this segment.
	 *
	 * @return the workload value, never {@code null}
	 */
	public Fraction getValue() {
		return value;
	}

	/**
	 * Splits this segment into two contiguous segments at the given instant.
	 *
	 * <p>
	 * The split instant {@code time} must lie strictly within the bounds of this
	 * segment (exclusive of both {@code a} and {@code b}).
	 * </p>
	 *
	 * <p>
	 * The returned array always contains exactly two segments with the same
	 * {@link #getTimeDirection() direction} as this segment:
	 * </p>
	 * <ul>
	 * <li>the first segment spans from {@code a} (inclusive) to {@code time}
	 * (exclusive),</li>
	 * <li>the second segment spans from {@code time} (inclusive) to {@code b}
	 * (exclusive).</li>
	 * </ul>
	 *
	 * <p>
	 * Both resulting segments carry the same {@link #getValue() value} as this
	 * segment. The two segments are guaranteed to be contiguous and
	 * non-overlapping.
	 * </p>
	 *
	 * @param time the instant at which to split this segment
	 * @return an array of exactly two segments resulting from the split
	 * @throws NullPointerException     if {@code time} is {@code null}
	 * @throws IllegalArgumentException if {@code time} lies outside the bounds of
	 *                                  this segment or is equal to {@code a} or
	 *                                  {@code b}
	 */
	public Segment[] split(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");
		if (strictIncludes(time) == false)
			throw new IllegalArgumentException("time must be within the segment bounds");

		if (direction == TimeDirection.FORWARD) {
			Segment first = Segment.forward(aInclusive, time, value);
			Segment second = Segment.forward(time, bExclusive, value);
			return new Segment[] { first, second };
		} else {
			Segment first = Segment.backward(aInclusive, time, value);
			Segment second = Segment.backward(time, bExclusive, value);
			return new Segment[] { first, second };
		}
	}

	/**
	 * Checks whether the given instant lies within this segment.
	 *
	 * <p>
	 * The boundary convention is always {@code [a, b)} — inclusive of {@code a},
	 * exclusive of {@code b} — regardless of the segment's direction.
	 * </p>
	 * <ul>
	 * <li>{@link TimeDirection#FORWARD}: {@code a} is chronologically before
	 * {@code b}</li>
	 * <li>{@link TimeDirection#BACKWARD}: {@code a} is chronologically after
	 * {@code b}</li>
	 * </ul>
	 *
	 * @param time the instant to check
	 * @return {@code true} if {@code time} lies within this segment, {@code false}
	 *         otherwise
	 * @throws NullPointerException if {@code time} is {@code null}
	 */
	public boolean includes(LocalDateTime time) {
		return time.isEqual(aInclusive) || strictIncludes(time);
	}

	/**
	 * Checks whether the given instant lies strictly within this segment.
	 *
	 * <p>
	 * Unlike {@link #includes(LocalDateTime)}, this method excludes both
	 * boundaries. The check is always {@code (a, b)} — exclusive on both ends.
	 * </p>
	 * <ul>
	 * <li>{@link TimeDirection#FORWARD}: {@code a < time < b}</li>
	 * <li>{@link TimeDirection#BACKWARD}: {@code b < time < a}</li>
	 * </ul>
	 *
	 * @param time the instant to check
	 * @return {@code true} if {@code time} lies strictly between {@code a} and
	 *         {@code b} (exclusive on both ends), {@code false} otherwise
	 * @throws NullPointerException if {@code time} is {@code null}
	 */
	public boolean strictIncludes(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");

		if (direction == TimeDirection.FORWARD)
			return time.isAfter(aInclusive) && time.isBefore(bExclusive);
		else
			return time.isBefore(aInclusive) && time.isAfter(bExclusive);

	}

	/**
	 * Returns a string representation of this segment.
	 *
	 * <p>
	 * The format is: {@code DIRECTION [a, b) value=V} where DIRECTION is either
	 * FORWARD or BACKWARD.
	 * </p>
	 *
	 * @return a string representation of this segment
	 */
	@Override
	public String toString() {
		return getTimeDirection() + " [" + aInclusive + ", " + bExclusive + ") value=" + value;
	}

	/**
	 * Computes the intersection of multiple segments using multiplication to
	 * combine their values.
	 *
	 * <p>
	 * The intersection is defined as the overlapping time range shared by all
	 * provided segments. If no such overlapping range exists, an exception is
	 * thrown.
	 * </p>
	 *
	 * <p>
	 * The value of the resulting segment is computed by multiplying the values of
	 * all intersecting segments.
	 * </p>
	 *
	 * @param segments the list of segments to intersect
	 * @return a new {@link Segment} representing the intersection of the input
	 *         segments with the combined value
	 * @throws IllegalArgumentException if {@code segments} is empty or if no
	 *                                  overlapping range exists among the segments
	 * @throws NullPointerException     if {@code segments} is {@code null}
	 */
	public static Segment intersection(List<Segment> segments) {
		return intersection(segments, Fraction.PRODUCT);
	}

	/**
	 * Computes the intersection of multiple segments.
	 *
	 * <p>
	 * The intersection is defined as the overlapping time range shared by all
	 * provided segments. If no such overlapping range exists, an exception is
	 * thrown.
	 * </p>
	 *
	 * <p>
	 * All segments must have the same {@link TimeDirection}. The resulting segment
	 * will have the same direction as the input segments.
	 * </p>
	 *
	 * <p>
	 * The value of the resulting segment is computed by applying the provided
	 * {@code valueFunction} to the values of all intersecting segments.
	 * </p>
	 *
	 * @param segments      the list of segments to intersect
	 * @param valueFunction a function that combines two {@link Fraction} values
	 *                      into one; this function is applied iteratively to
	 *                      combine the values of all intersecting segments
	 * @return a new {@link Segment} representing the intersection of the input
	 *         segments with the combined value
	 * @throws IllegalArgumentException if {@code segments} is empty, if segments
	 *                                  have different directions, or if no
	 *                                  overlapping range exists among the segments
	 * @throws NullPointerException     if {@code segments} or {@code valueFunction}
	 *                                  is {@code null}
	 */
	public static Segment intersection(List<Segment> segments, BiFunction<Fraction, Fraction, Fraction> valueFunction) {
		Objects.requireNonNull(valueFunction, "valueFunction must not be null");

		if (segments.isEmpty())
			throw new IllegalArgumentException("No segments to intersect");

		if (segments.size() == 1)
			return segments.get(0);

		final TimeDirection direction = segments.get(0).getTimeDirection();

		LocalDateTime a = segments.get(0).aInclusive();
		LocalDateTime b = segments.get(0).bExclusive();
		Fraction combinedValue = segments.get(0).getValue();

		for (int i = 1; i < segments.size(); i++) {
			final Segment segment = segments.get(i);

			if (segment.getTimeDirection() != direction)
				throw new IllegalArgumentException("All segments must have the same direction");

			if (direction == TimeDirection.FORWARD) {
				if (segment.aInclusive().isAfter(a))
					a = segment.aInclusive();
				if (segment.bExclusive().isBefore(b))
					b = segment.bExclusive();
			} else {
				if (segment.aInclusive().isBefore(a))
					a = segment.aInclusive();
				if (segment.bExclusive().isAfter(b))
					b = segment.bExclusive();
			}

			combinedValue = valueFunction.apply(combinedValue, segment.getValue());
		}

		if (direction == TimeDirection.FORWARD) {
			if (a.isBefore(b) == false)
				throw new IllegalArgumentException("Segments do not overlap");
			return Segment.forward(a, b, combinedValue);
		} else {
			if (b.isBefore(a) == false)
				throw new IllegalArgumentException("Segments do not overlap");
			return Segment.backward(a, b, combinedValue);
		}
	}

	/**
	 * Computes the intersection of multiple segments using multiplication to
	 * combine their values.
	 *
	 * <p>
	 * Convenience overload for array inputs. Delegates to
	 * {@link #intersection(List)}.
	 * </p>
	 *
	 * @param segments the array of segments to intersect
	 * @return a new {@link Segment} representing the intersection
	 * @throws IllegalArgumentException if {@code segments} is empty, if segments
	 *                                  have different directions, or if no
	 *                                  overlapping range exists
	 * @throws NullPointerException     if {@code segments} is {@code null}
	 * @see #intersection(List)
	 */
	public static Segment intersection(Segment[] segments) {
		Objects.requireNonNull(segments, "segments must not be null");

		return intersection(Arrays.asList(segments));
	}

	/**
	 * Computes the intersection of multiple segments with a custom value function.
	 *
	 * <p>
	 * Convenience overload for array inputs. Delegates to
	 * {@link #intersection(List, BiFunction)}.
	 * </p>
	 *
	 * @param segments      the array of segments to intersect
	 * @param valueFunction a function that combines two {@link Fraction} values
	 * @return a new {@link Segment} representing the intersection
	 * @throws IllegalArgumentException if {@code segments} is empty, if segments
	 *                                  have different directions, or if no
	 *                                  overlapping range exists
	 * @throws NullPointerException     if {@code segments} or {@code valueFunction}
	 *                                  is {@code null}
	 * @see #intersection(List, BiFunction)
	 */
	public static Segment intersection(Segment[] segments, BiFunction<Fraction, Fraction, Fraction> valueFunction) {
		Objects.requireNonNull(segments, "segments must not be null");

		return intersection(Arrays.asList(segments), valueFunction);
	}

}