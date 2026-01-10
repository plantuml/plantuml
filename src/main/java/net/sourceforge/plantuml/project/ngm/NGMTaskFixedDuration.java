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
 * Original Author:  Mario Kušek
 * 
 *
 */
package net.sourceforge.plantuml.project.ngm;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a fixed-duration task in the New Gantt Model (NGM).
 *
 * <p>In a fixed-duration task, the calendar span between start and end is
 * intrinsic and does not change. The load becomes a derived quantity that
 * depends on the workload allocation.</p>
 *
 * <p>Example: "Crossing the Atlantic takes 7 days regardless of crew size".</p>
 *
 * @see NGMTask
 */
public class NGMTaskFixedDuration extends NGMTask {

	private final Duration duration;
	private LocalDateTime start;

	/**
	 * Constructs a fixed-duration task with the specified workload and duration.
	 *
	 * @param workload the workload allocation for the task
	 * @param duration the intrinsic duration of the task
	 */
	public NGMTaskFixedDuration(NGMAllocation workload, Duration duration) {
		super(workload);
		this.duration = duration;
	}

	@Override
	public LocalDateTime getStart() {
		return start;
	}

	@Override
	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	@Override
	public LocalDateTime getEnd() {
		return start.plus(duration);
	}

	@Override
	public void setEnd(LocalDateTime end) {
		start = end.minus(duration);
	}

	@Override
	public Duration getDuration() {
		return duration;
	}

	@Override
	public NGMTotalEffort getTotalEffort() {
		throw new UnsupportedOperationException("Work In Progress");
	}

}
