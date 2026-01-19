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
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm.math;

import java.time.LocalDateTime;
import java.util.Objects;

import net.sourceforge.plantuml.project.time.TimePoint;

/**
 * Immutable implementation of {@link PiecewiseConstant} that models a time
 * window where work is enabled (value = 1) only within a specific period, and
 * disabled (value = 0) before and after that period.
 *
 * <p>
 * This represents a function that:
 * </p>
 * <ul>
 * <li>Returns {@link Fraction#ZERO} for all times before {@code offBefore}</li>
 * <li>Returns {@link Fraction#ONE} for all times between {@code offBefore} and
 * {@code offAfter}</li>
 * <li>Returns {@link Fraction#ZERO} for all times after {@code offAfter}</li>
 * </ul>
 *
 * <p>
 * This is useful for modeling tasks or resources that are only available during
 * a specific time window.
 * </p>
 */
public final class PiecewiseConstantTimeWindow extends AbstractPiecewiseConstant {

	/**
	 * The instant before which work is disabled (exclusive). Before this instant,
	 * the function value is 0.
	 */
	private final LocalDateTime offBefore;

	/**
	 * The instant after which work is disabled (exclusive). After this instant, the
	 * function value is 0.
	 */
	private final LocalDateTime offAfter;

	/**
	 * Constructs a PiecewiseConstantOffBeforeOffAfter with the given time window.
	 *
	 * @param offBefore The time point before which work is disabled.
	 * @param offAfter  The time point after which work is disabled.
	 * @throws NullPointerException     if either parameter is null
	 * @throws IllegalArgumentException if offAfter is not after offBefore
	 */
	public PiecewiseConstantTimeWindow(LocalDateTime offBefore, LocalDateTime offAfter) {

		this.offBefore = offBefore;
		this.offAfter = offAfter;

		if (this.offBefore.isBefore(this.offAfter) == false)
			throw new IllegalArgumentException("offAfter must be after offBefore");
	}

	/**
	 * Returns the segment containing the given instant.
	 *
	 * <p>
	 * This method implements the core logic of the piecewise constant function by
	 * determining which of the three regions (before, during, or after the active
	 * window) contains the given instant, and returns an appropriate segment.
	 * </p>
	 *
	 * @param instant   The instant for which to find the containing segment.
	 * @param direction The direction of iteration (FORWARD or BACKWARD).
	 * @return A segment containing the instant with the appropriate value (0 or 1).
	 */
	@Override
	public Segment segmentAt(LocalDateTime instant, TimeDirection direction) {
		if (direction == TimeDirection.FORWARD)
			return segmentAtForward(instant);
		else
			return segmentAtBackward(instant);
	}

	/**
	 * Returns the segment containing the given instant for forward iteration.
	 *
	 * @param instant The instant for which to find the containing segment.
	 * @return A forward segment containing the instant.
	 */
	private Segment segmentAtForward(LocalDateTime instant) {
		// Before the active window: return a segment from instant to offBefore with
		// value 0
		if (instant.isBefore(offBefore))
			return Segment.forward(instant, offBefore, Fraction.ZERO);

		// After the active window: return an unbounded segment from instant onwards
		// with value 0
		if (instant.isBefore(offAfter) == false)
			return Segment.forward(instant, LocalDateTime.MAX, Fraction.ZERO);

		// Within the active window: return a segment from instant to offAfter with
		// value 1
		return Segment.forward(instant, offAfter, Fraction.ONE);
	}

	/**
	 * Returns the segment containing the given instant for backward iteration.
	 *
	 * @param instant The instant for which to find the containing segment.
	 * @return A backward segment containing the instant.
	 */
	private Segment segmentAtBackward(LocalDateTime instant) {
		// After the active window: return a segment from instant back to offAfter with
		// value 0
		if (instant.isAfter(offAfter))
			return Segment.backward(instant, offAfter, Fraction.ZERO);

		// Before the active window: return an unbounded segment from instant backwards
		// with value 0
		if (instant.isAfter(offBefore) == false)
			return Segment.backward(instant, LocalDateTime.MIN, Fraction.ZERO);

		// Within the active window: return a segment from instant back to offBefore
		// with value 1
		return Segment.backward(instant, offBefore, Fraction.ONE);
	}

}
