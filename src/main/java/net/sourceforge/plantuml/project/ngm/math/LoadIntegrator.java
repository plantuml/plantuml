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

		Fraction remainingLoad = totalLoad.toSeconds();
		LocalDateTime currentTime = start;

		final Iterator<Segment> iter = loadFunction.iterateSegmentsFrom(start, TimeDirection.FORWARD);
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
			final LocalDateTime effectiveStart = currentTime.isAfter(segment.startExclusive()) ? currentTime
					: segment.startExclusive();

			// Calculate segment duration in seconds
			final long segmentSeconds = Duration.between(effectiveStart, segment.endExclusive()).toSeconds();
			final Fraction segmentDuration = new Fraction(segmentSeconds, 1);

			// Load consumed in this segment = loadRate * duration (in seconds)
			final Fraction segmentLoad = loadRate.multiply(segmentDuration);

			if (segmentLoad.compareTo(remainingLoad) >= 0) {
				// This segment completes the work
				// Time needed = remainingLoad / loadRate (in seconds)
				final Fraction secondsNeeded = remainingLoad.divide(loadRate);
				return effectiveStart.plusSeconds(secondsNeeded.wholePart());
			}

			// Consume the entire segment
			remainingLoad = remainingLoad.subtract(segmentLoad);
			currentTime = segment.endExclusive();
		}

		return currentTime;
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

		Fraction remainingLoad = totalLoad.toSeconds();
		LocalDateTime currentTime = end;

		final Iterator<Segment> iter = loadFunction.iterateSegmentsFrom(end, TimeDirection.BACKWARD);
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
			// For BACKWARD: currentTime is the "later" bound we're working from
			final LocalDateTime effectiveStart = currentTime.isBefore(segment.startExclusive()) ? currentTime
					: segment.startExclusive();

			// Calculate segment duration in seconds
			// For BACKWARD: duration is from endExclusive to effectiveStart (going backward)
			final long segmentSeconds = Duration.between(segment.endExclusive(), effectiveStart).toSeconds();
			final Fraction segmentDuration = new Fraction(segmentSeconds, 1);

			// Load consumed in this segment = loadRate * duration (in seconds)
			final Fraction segmentLoad = loadRate.multiply(segmentDuration);

			if (segmentLoad.compareTo(remainingLoad) >= 0) {
				// This segment completes the work
				// Time needed = remainingLoad / loadRate (in seconds)
				final Fraction secondsNeeded = remainingLoad.divide(loadRate);
				return effectiveStart.minusSeconds(secondsNeeded.wholePart());
			}

			// Consume the entire segment
			remainingLoad = remainingLoad.subtract(segmentLoad);
			currentTime = segment.endExclusive();
		}

		return currentTime;
	}

}
