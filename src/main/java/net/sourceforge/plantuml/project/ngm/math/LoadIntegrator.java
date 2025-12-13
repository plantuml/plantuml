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

import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant.Segment;

/**
 * Integrates a time-varying load function in order to determine the date and
 * time at which a given total load is fully consumed.
 * <p>
 * The load is described by a {@link PiecewiseConstant} function, expressing a
 * constant load rate over successive time intervals. Starting from a given
 * {@link LocalDateTime}, this class conceptually integrates the load over time
 * until the specified {@link Fraction} of total load has been reached.
 * <p>
 * This class does not represent a duration or a fixed end date by itself; it
 * resolves an end date by consuming load according to the provided load
 * function.
 * <p>
 * Typical use cases include task scheduling where the workload varies over time
 * (for example, working hours, calendar constraints, or changing resource
 * allocations).
 * <p>
 * The implementation of the integration is expected to iterate over successive
 * load segments starting from the given start date-time. In particular, the
 * {@code computeEnd()} method will typically rely on an iterator obtained via
 * {@code PiecewiseConstant#segmentsStartingAt(LocalDateTime)} in order to
 * process the load segments in chronological order.
 */
public class LoadIntegrator {

	private static final int ONE_DAY_IN_SECONDS = 86400;
	private final PiecewiseConstant loadFunction;
	private final LocalDateTime start;
	private final Fraction totalLoad;

	/**
	 * Creates a new {@code LoadIntegrator}.
	 *
	 * @param loadFunction the piecewise constant load function to integrate; it
	 *                     defines the load rate at any given time
	 * @param start        the start date-time from which the load integration
	 *                     begins
	 * @param totalLoad    the total amount of load to be consumed
	 */
	public LoadIntegrator(PiecewiseConstant loadFunction, LocalDateTime start, Fraction totalLoad) {
		this.loadFunction = loadFunction;
		this.start = start;
		this.totalLoad = totalLoad;
	}

	/**
	 * Computes the end date-time at which the total load has been completely
	 * consumed.
	 * <p>
	 * The computation is expected to traverse the successive
	 * {@link PiecewiseConstant} segments starting at the integration start
	 * date-time, accumulating consumed load until the target total load is reached.
	 *
	 * @return the {@link LocalDateTime} corresponding to the completion of the load
	 *         integration
	 */
	public LocalDateTime computeEnd() {
		Fraction remainingLoad = totalLoad;
		LocalDateTime currentTime = start;

		
		Iterator<Segment> iter = loadFunction.segmentsStartingAt(start);
		while (iter.hasNext()) {
			Segment segment = iter.next();
			
			if (remainingLoad.equals(Fraction.ZERO)) {
				break;
			}

			Fraction loadRate = segment.getValue();
			if (loadRate.equals(Fraction.ZERO)) {
				currentTime = segment.getEndExclusive();
				continue;
			}

			Duration segmentDurationRaw = Duration.between(currentTime, segment.getEndExclusive());
			Fraction segmentDurationInDays = new Fraction(segmentDurationRaw.toSeconds(), ONE_DAY_IN_SECONDS); // duration in days

			Fraction loadInSegment = loadRate.multiply(segmentDurationInDays);

			if (loadInSegment.compareTo(remainingLoad) >= 0) {
				// The remaining load can be consumed within this segment
				Fraction timeNeeded = remainingLoad.divide(loadRate);
				long secondsNeeded = timeNeeded.multiply(Fraction.of(86400)).wholePart();
				currentTime = currentTime.plusSeconds(secondsNeeded);
				remainingLoad = Fraction.ZERO;
			} else {
				// Consume the entire segment load and move to the next segment
				remainingLoad = remainingLoad.subtract(loadInSegment);
				currentTime = segment.getEndExclusive();
			}
		}

		return currentTime;
	}

}
