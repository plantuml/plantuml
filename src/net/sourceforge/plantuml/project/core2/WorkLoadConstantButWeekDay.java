/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.project.core2;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import net.sourceforge.plantuml.project.time.DayOfWeek;

public class WorkLoadConstantButWeekDay implements WorkLoad {

	private final int value;
	private final Set<DayOfWeek> excepts = EnumSet.noneOf(DayOfWeek.class);

	public WorkLoadConstantButWeekDay(int value, DayOfWeek... butThisDays) {
		this.value = value;
		this.excepts.addAll(Arrays.asList(butThisDays));
	}

	private static final long dayDuration = 1000L * 24 * 3600;

	public IteratorSlice slices(final long timeBiggerThan) {
		final Slice first = getNext(timeBiggerThan);
		return new MyIterator(first);
	}

	class MyIterator implements IteratorSlice {

		private Slice current;

		public MyIterator(Slice first) {
			this.current = first;
		}

		public Slice next() {
			final Slice result = current;
			current = getNext(current.getEnd());
			return result;
		}
	}

	private Slice getNext(final long limit) {
		long start = limit;
		long end;
		if (isClose(start)) {
			start = round(start);
			while (isClose(start))
				start += dayDuration;
			end = start + dayDuration;
		} else {
			end = round(start) + dayDuration;
		}
		assert !isClose(start);
		while (isClose(end) == false)
			end += dayDuration;
		assert isClose(end);

		return new Slice(start, end, value);
	}

	private boolean isClose(long start) {
		return excepts.contains(DayOfWeek.fromTime(start));
	}

	private long round(long start) {
		return dayDuration * (start / dayDuration);
	}

}
