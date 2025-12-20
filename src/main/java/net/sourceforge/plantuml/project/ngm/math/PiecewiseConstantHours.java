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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class PiecewiseConstantHours extends AbstractPiecewiseConstant {

	/**
	 * Default workload fraction for dates not explicitly mapped.
	 */
	private final Fraction defaultValue;
	
	/**
	 * List of time segments with their associated workload fractions.
	 */
	private List<LocalTimeSegment> segments;

	/**
	 * Constructs a PiecewiseConstantSpecificDays with the given default workload.
	 * 
	 * @param defaultValue the default workload fraction
	 */
	private PiecewiseConstantHours(Fraction defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Constructs a PiecewiseConstantSpecificDays with the given default workload
	 * and specific time mappings.
	 * 
	 * @param defaultValue the default workload fraction
	 * @param segments the list of time segments with their associated workload fractions
	 */
	private PiecewiseConstantHours(Fraction defaultValue, List<LocalTimeSegment> segments) {
		this.defaultValue = defaultValue;
		this.segments = segments;
	}
	
	/**
	 * Returns the workload fraction at the given instant.
	 * The workload is determined by the time of day, ignoring the date.
	 * 
	 * @param instant the time instant to query
	 * @return the workload fraction at this instant
	 */
	@Override
	public Fraction apply(LocalDateTime instant) {
		for(LocalTimeSegment segment : segments) {
			if(segment.includes(instant.toLocalTime())) {
				return segment.getWorkload();
			}
		}
		
		return defaultValue;
	}

	/**
	 * Creates a new PiecewiseConstantHours with the same workload for all hours of the day.
	 * 
	 * @param sameWorkload the constant workload fraction to apply to all hours
	 * @return a new PiecewiseConstantHours instance
	 */
	public static PiecewiseConstantHours of(Fraction sameWorkload) {
		return new PiecewiseConstantHours(sameWorkload, List.of());
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
		List<LocalTimeSegment> newSegments = new ArrayList<>(this.segments);
		newSegments.add(new LocalTimeSegment(start, end, newWorkload));
		return new PiecewiseConstantHours(this.defaultValue, newSegments);
	}

	/** (non-Javadoc)
	 * @see net.sourceforge.plantuml.project.ngm.math.AbstractPiecewiseConstant#segmentAt(java.time.LocalDateTime)
	 */
	@Override
	public Segment segmentAt(LocalDateTime instant) {
		throw new UnsupportedOperationException("Work In Progress");
	}
	
	private static class LocalTimeSegment {
		
		private final LocalTime start;
		private final LocalTime end;
		private final Fraction workload;
		
		public LocalTimeSegment(LocalTime start, LocalTime end, Fraction workload) {
			this.start = start;
			this.end = end;
			this.workload = workload;
		}
		
		public boolean includes(LocalTime time) {
			return !time.isBefore(start) && time.isBefore(end);
		}
		
		public Fraction getWorkload() {
			return workload;
		}
		
	}

}
