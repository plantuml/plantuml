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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.project.time.DayOfWeek;

public class Slice {

	private final long start;
	private final long end;
	private final int workLoad;

	public Slice(long start, long end, int workLoad) {
		if (end <= start) {
			throw new IllegalArgumentException();
		}
		this.start = start;
		this.end = end;
		this.workLoad = workLoad;
	}

	@Override
	public String toString() {
		return DayOfWeek.timeToString(start) + " --> " + DayOfWeek.timeToString(end) + " <" + workLoad + ">";
	}

	public boolean containsTime(long time) {
		return time >= start && time <= end;
	}

	public final long getStart() {
		return start;
	}

	public final long getEnd() {
		return end;
	}

	public final int getWorkLoad() {
		return workLoad;
	}

	public List<Slice> intersectWith(HolesList holes) {
		final List<Slice> result = new ArrayList<Slice>();
		for (Hole hole : holes) {
			final Slice inter = intersectWith(hole);

		}
		return Collections.unmodifiableList(result);
	}

	private Slice intersectWith(Hole hole) {
		if (hole.getEnd() <= start || hole.getStart() <= end) {
			return null;
		}
		return new Slice(Math.max(start, hole.getStart()), Math.min(end, hole.getEnd()), workLoad);
	}

}
