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
package net.sourceforge.plantuml.gantt.core;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TimeRanges implements Iterable<TimeRange> {

	private final List<TimeRange> timeRanges = new ArrayList<>();

	public TimeRanges(TimeRange... data) {
		for (TimeRange tr : data)
			timeRanges.add(tr);
	}

	public boolean hasWorkingTimeRanges() {
		return timeRanges.size() > 0;
	}

	public void add(TimeRange timeRange) {
		this.timeRanges.add(timeRange);
	}

	@Override
	public String toString() {
		return timeRanges.toString();
	}

	@Override
	public Iterator<TimeRange> iterator() {
		return Collections.unmodifiableCollection(timeRanges).iterator();
	}

	public Duration totalWorkingDuration() {
		Duration working = Duration.ZERO;
		for (TimeRange range : this)
			working = working.plus(Duration.between(range.getStart(), range.getEnd()));
		return working;
	}

	public Duration durationOfDays(int totalDays) {
		if (hasWorkingTimeRanges())
			return totalWorkingDuration().multipliedBy(totalDays);

		return Duration.ofDays(totalDays);
	}

}
