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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;

import net.sourceforge.plantuml.project.ngm.NGMTotalEffort;

public class LoadIntegrator {

	private final PiecewiseConstant loadFunction;
	private final NGMTotalEffort totalLoad;

	/**
	 * Creates a new {@code LoadIntegrator}.
	 *
	 * @param loadFunction the piecewise constant load function to integrate; it
	 *                     defines the load rate at any given time
	 * @param totalLoad    the total amount of load to be consumed
	 */
	public LoadIntegrator(PiecewiseConstant loadFunction, NGMTotalEffort totalLoad) {
		this.loadFunction = loadFunction;
		this.totalLoad = totalLoad;
	}

	public LocalDateTime computeEnd(LocalDateTime start) {
		return integrate(start, TimeDirection.FORWARD);
	}

	/**
	 * Computes the start date-time at which the total load has been completely
	 * consumed.
	 * <p>
	 * The computation is expected to traverse the successive
	 * {@link PiecewiseConstant} segments starting at the integration start
	 * date-time, accumulating consumed load in past until the target total load is
	 * reached.
	 *
	 * @return the {@link LocalDateTime} corresponding to the start of the load
	 *         integration
	 */

	public LocalDateTime computeStart(LocalDateTime end) {
		return integrate(end, TimeDirection.BACKWARD);
	}

	/**
	 * Generic integration method that works in both time directions.
	 *
	 * @param startTime the starting time for integration
	 * @param direction the direction of time travel (FORWARD or BACKWARD)
	 * @return the computed end time after consuming the total load
	 */
	private LocalDateTime integrate(LocalDateTime startTime, TimeDirection direction) {
		Fraction remainingLoad = totalLoad.toSeconds();
		LocalDateTime currentTime = startTime;

		final Iterator<Segment> iter = loadFunction.iterateSegmentsFrom(startTime, direction);
		while (iter.hasNext()) {
			Segment segment = iter.next();

			if (remainingLoad.equals(Fraction.ZERO))
				break;

			Fraction loadRate = segment.getValue();
			if (loadRate.equals(Fraction.ZERO)) {
				currentTime = segment.endExclusive();
				continue;
			}

			// Calculate the effective start within the segment
			final LocalDateTime effectiveStart = computeEffectiveStart(currentTime, segment, direction);

			// Calculate segment duration in seconds
			final long segmentSeconds = computeSegmentDuration(effectiveStart, segment, direction);
			final Fraction segmentDuration = new Fraction(segmentSeconds, 1);

			// Load consumed in this segment = loadRate * duration (in seconds)
			final Fraction segmentLoad = loadRate.multiply(segmentDuration);

			if (segmentLoad.compareTo(remainingLoad) >= 0) {
				// This segment completes the work
				// Time needed = remainingLoad / loadRate (in seconds)
				final Fraction secondsNeeded = remainingLoad.divide(loadRate);
				return applyTimeOffset(effectiveStart, secondsNeeded.wholePart(), direction);
			}

			// Consume the entire segment
			remainingLoad = remainingLoad.subtract(segmentLoad);
			currentTime = segment.endExclusive();
		}

		return currentTime;
	}

	/**
	 * Computes the effective start time within a segment based on the current
	 * position and direction.
	 *
	 * @param currentTime the current time position
	 * @param segment     the segment being processed
	 * @param direction   the time direction
	 * @return the effective start time for this segment
	 */
	private LocalDateTime computeEffectiveStart(LocalDateTime currentTime, Segment segment, TimeDirection direction) {
		if (direction == TimeDirection.FORWARD)
			return currentTime.isAfter(segment.startExclusive()) ? currentTime : segment.startExclusive();
		else
			return currentTime.isBefore(segment.startExclusive()) ? currentTime : segment.startExclusive();
	}

	/**
	 * Computes the duration of work in a segment in seconds.
	 *
	 * @param effectiveStart the effective start time within the segment
	 * @param segment        the segment being processed
	 * @param direction      the time direction
	 * @return the duration in seconds
	 */
	private long computeSegmentDuration(LocalDateTime effectiveStart, Segment segment, TimeDirection direction) {
		if (direction == TimeDirection.FORWARD)
			return Duration.between(effectiveStart, segment.endExclusive()).toSeconds();
		else
			return Duration.between(segment.endExclusive(), effectiveStart).toSeconds();
	}

	/**
	 * Applies a time offset in the appropriate direction.
	 *
	 * @param time      the base time
	 * @param seconds   the number of seconds to offset
	 * @param direction the time direction
	 * @return the time after applying the offset
	 */
	private LocalDateTime applyTimeOffset(LocalDateTime time, long seconds, TimeDirection direction) {
		if (direction == TimeDirection.FORWARD)
			return time.plusSeconds(seconds);
		else
			return time.minusSeconds(seconds);
	}

}
