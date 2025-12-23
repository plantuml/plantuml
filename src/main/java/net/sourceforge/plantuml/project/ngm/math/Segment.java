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
 * A segment represents a time interval where the workload value remains constant.
 * The boundary convention is: <strong>both bounds are exclusive</strong>,
 * denoted as {@code ]start, end[} (or equivalently {@code (start, end)} in some
 * notations). This symmetric design simplifies bidirectional iteration.
 * </p>
 *
 * <p>
 * Segments can be either {@link TimeDirection#FORWARD} or
 * {@link TimeDirection#BACKWARD}:
 * </p>
 * <ul>
 * <li><strong>FORWARD</strong>: {@code start} is chronologically before {@code end}
 * (used for forward iteration through time)</li>
 * <li><strong>BACKWARD</strong>: {@code start} is chronologically after {@code end}
 * (used for backward iteration through time)</li>
 * </ul>
 *
 * <p>
 * In both cases, both {@code start} and {@code end} are exclusive bounds.
 * This symmetric convention ensures that boundary instants are never "inside"
 * a segment, which simplifies reasoning about segment adjacency and splitting.
 * </p>
 *
 * <p>
 * Think of it as a time traveler: they travel from {@code start} toward
 * {@code end}, but never actually stand on either boundary instant. The segment
 * contains all instants strictly between the two bounds. For FORWARD segments,
 * travel is into the future. For BACKWARD segments, travel is into the past.
 * </p>
 *
 * <p>
 * The value may be zero to explicitly model periods with no assigned workload.
 * </p>
 *
 * <p>
 * Instances are created via the factory methods
 * {@link #forward(LocalDateTime, LocalDateTime, Fraction)} and
 * {@link #backward(LocalDateTime, LocalDateTime, Fraction)}.
 * </p>
 *
 * @see TimeDirection
 * @see Fraction
 */
public final class Segment {

	private final LocalDateTime start;

	private final LocalDateTime end;

	/**
	 * The constant workload value over this segment.
	 */
	private final Fraction value;

	private final TimeDirection direction;

	/**
	 * Private constructor. Use {@link #forward} or {@link #backward} factory
	 * methods.
	 */
	private Segment(TimeDirection direction, LocalDateTime start, LocalDateTime end, Fraction value) {
		Objects.requireNonNull(value);

		this.direction = direction;
		this.start = start;
		this.end = end;
		this.value = value;
	}

	/**
	 * Creates a forward segment where {@code start} is chronologically before
	 * {@code end}.
	 *
	 * <p>
	 * The segment represents the time interval {@code ]start, end[} for forward
	 * iteration. Both bounds are exclusive.
	 * </p>
	 *
	 * @param start the exclusive start bound (chronologically earlier)
	 * @param end   the exclusive end bound (chronologically later)
	 * @param value the constant workload value for this segment
	 * @return a new forward segment
	 * @throws NullPointerException     if any argument is {@code null}
	 * @throws IllegalArgumentException if {@code start} is not chronologically
	 *                                  before {@code end}
	 */
	public static Segment forward(LocalDateTime start, LocalDateTime end, Fraction value) {
		if (start.isBefore(end) == false)
			throw new IllegalArgumentException("end must be after start");
		return new Segment(TimeDirection.FORWARD, start, end, value);
	}

	/**
	 * Creates a backward segment where {@code start} is chronologically after
	 * {@code end}.
	 *
	 * <p>
	 * The segment represents the time interval {@code ]start, end[} for backward
	 * iteration, where {@code start} is the later instant and {@code end} is the
	 * earlier instant. Both bounds are exclusive.
	 * </p>
	 *
	 *
	 * @param start the exclusive start bound (chronologically later)
	 * @param end   the exclusive end bound (chronologically earlier)
	 * @param value the constant workload value for this segment
	 * @return a new backward segment
	 * @throws NullPointerException     if any argument is {@code null}
	 * @throws IllegalArgumentException if {@code end} is not chronologically
	 *                                  before {@code start}
	 */
	public static Segment backward(LocalDateTime start, LocalDateTime end, Fraction value) {
		if (end.isBefore(start) == false)
			throw new IllegalArgumentException("end must be before start");
		return new Segment(TimeDirection.BACKWARD, start, end, value);
	}

	/**
	 * Returns the direction of this segment.
	 *
	 * @return {@link TimeDirection#FORWARD} if {@code start} is chronologically
	 *         before {@code end}, {@link TimeDirection#BACKWARD} if {@code start}
	 *         is chronologically after {@code end}
	 */
	public TimeDirection getTimeDirection() {
		return direction;
	}

	/**
	 * Returns the exclusive start bound of this segment.
	 *
	 * <p>
	 * For {@link TimeDirection#FORWARD} segments, this is the chronologically
	 * earlier instant. For {@link TimeDirection#BACKWARD} segments, this is the
	 * chronologically later instant.
	 * </p>
	 *
	 * <p>
	 * This bound is exclusive: the returned instant is not considered part of
	 * the segment.
	 * </p>
	 *
	 * @return the exclusive start bound
	 */
	public LocalDateTime startExclusive() {
		return start;
	}

	/**
	 * Returns the exclusive end bound of this segment.
	 *
	 * <p>
	 * For {@link TimeDirection#FORWARD} segments, this is the chronologically
	 * later instant. For {@link TimeDirection#BACKWARD} segments, this is the
	 * chronologically earlier instant.
	 * </p>
	 *
	 * <p>
	 * This bound is exclusive: the returned instant is not considered part of
	 * the segment.
	 * </p>
	 *
	 * @return the exclusive end bound
	 */
	public LocalDateTime endExclusive() {
		return end;
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
	 * segment (exclusive of both {@code start} and {@code end}).
	 * </p>
	 *
	 * <p>
	 * The returned array always contains exactly two segments with the same
	 * {@link #getTimeDirection() direction} as this segment:
	 * </p>
	 * <ul>
	 * <li>the first segment spans from {@code start} (exclusive) to {@code time}
	 * (exclusive),</li>
	 * <li>the second segment spans from {@code time} (exclusive) to {@code end}
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
	 *                                  this segment or is equal to {@code start}
	 *                                  or {@code end}
	 */
	public Segment[] split(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");
		if (includes(time) == false)
			throw new IllegalArgumentException("time must be within the segment bounds");

		if (direction == TimeDirection.FORWARD) {
			final Segment first = Segment.forward(start, time, value);
			final Segment second = Segment.forward(time, end, value);
			return new Segment[] { first, second };
		} else {
			final Segment first = Segment.backward(start, time, value);
			final Segment second = Segment.backward(time, end, value);
			return new Segment[] { first, second };
		}
	}

	/**
	 * Checks whether the given instant lies strictly within this segment.
	 *
	 * <p>
	 * This method excludes both boundaries. The check is {@code (start, end)} —
	 * exclusive on both ends.
	 * </p>
	 * <ul>
	 * <li>{@link TimeDirection#FORWARD}: {@code start < time < end}</li>
	 * <li>{@link TimeDirection#BACKWARD}: {@code end < time < start}</li>
	 * </ul>
	 *
	 * @param time the instant to check
	 * @return {@code true} if {@code time} lies strictly between {@code start} and
	 *         {@code end} (exclusive on both ends), {@code false} otherwise
	 * @throws NullPointerException if {@code time} is {@code null}
	 */
	public boolean includes(LocalDateTime time) {
		Objects.requireNonNull(time, "time must not be null");

		if (direction == TimeDirection.FORWARD)
			return time.isAfter(start) && time.isBefore(end);
		else
			return time.isBefore(start) && time.isAfter(end);

	}

	/**
	 * Returns a string representation of this segment.
	 *
	 * <p>
	 * The format is: {@code DIRECTION ]start, end[ value=V} where DIRECTION is
	 * either FORWARD or BACKWARD.
	 * </p>
	 *
	 * @return a string representation of this segment
	 */
	@Override
	public String toString() {
		return getTimeDirection() + " ]" + start + ", " + end + "[ value=" + value;
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

		LocalDateTime start = segments.get(0).startExclusive();
		LocalDateTime end = segments.get(0).endExclusive();
		Fraction combinedValue = segments.get(0).getValue();

		for (int i = 1; i < segments.size(); i++) {
			final Segment segment = segments.get(i);

			if (segment.getTimeDirection() != direction)
				throw new IllegalArgumentException("All segments must have the same direction");

			if (direction == TimeDirection.FORWARD) {
				if (segment.startExclusive().isAfter(start))
					start = segment.startExclusive();
				if (segment.endExclusive().isBefore(end))
					end = segment.endExclusive();
			} else {
				if (segment.startExclusive().isBefore(start))
					start = segment.startExclusive();
				if (segment.endExclusive().isAfter(end))
					end = segment.endExclusive();
			}

			combinedValue = valueFunction.apply(combinedValue, segment.getValue());
		}

		if (direction == TimeDirection.FORWARD) {
			if (start.isBefore(end) == false)
				throw new IllegalArgumentException("Segments do not overlap");
			return Segment.forward(start, end, combinedValue);
		} else {
			if (end.isBefore(start) == false)
				throw new IllegalArgumentException("Segments do not overlap");
			return Segment.backward(start, end, combinedValue);
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