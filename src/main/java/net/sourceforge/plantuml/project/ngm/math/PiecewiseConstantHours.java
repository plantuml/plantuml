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
import java.time.LocalTime;

public final class PiecewiseConstantHours implements PiecewiseConstant {

	/**
	 * Returns the workload fraction at the given instant.
	 * The workload is determined by the time of day, ignoring the date.
	 * 
	 * @param instant the time instant to query
	 * @return the workload fraction at this instant
	 */
	@Override
	public Fraction apply(LocalDateTime instant) {
		throw new UnsupportedOperationException("wip");
	}

	/**
	 * Creates a new PiecewiseConstantHours with the same workload for all hours of the day.
	 * 
	 * @param sameWorkload the constant workload fraction to apply to all hours
	 * @return a new PiecewiseConstantHours instance
	 */
	public static PiecewiseConstantHours of(Fraction sameWorkload) {
		throw new UnsupportedOperationException("wip");
	}

	/**
	 * Returns a new PiecewiseConstantHours with the specified workload for the given time range.
	 * This method is immutable and returns a new instance.
	 * 
	 * @param start the start time of the range (inclusive)
	 * @param end the end time of the range (exclusive)
	 * @param newWorkload the workload fraction to apply to this time range
	 * @return a new PiecewiseConstantHours instance with the updated time range
	 */
	public PiecewiseConstantHours with(LocalTime start, LocalTime end, Fraction newWorkload) {
		throw new UnsupportedOperationException("wip");
	}

}
