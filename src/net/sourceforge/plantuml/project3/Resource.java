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
package net.sourceforge.plantuml.project3;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

public class Resource implements Subject {

	private final String name;
	private ResourceDraw draw;
	private final Set<Instant> closed = new TreeSet<Instant>();
	private final Set<Instant> forcedOn = new TreeSet<Instant>();
	private final GCalendar calendar;

	private final Collection<DayOfWeek> closedDayOfWeek = EnumSet.noneOf(DayOfWeek.class);

	public Resource(String name, LoadPlanable loadPlanable, GCalendar calendar) {
		this.name = name;
		this.calendar = calendar;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		final Resource other = (Resource) obj;
		return this.name.equals(other.name);
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public ResourceDraw getResourceDraw() {
		return draw;
	}

	public void setTaskDraw(ResourceDraw draw) {
		this.draw = draw;
	}

	public boolean isClosedAt(Instant instant) {
		if (this.forcedOn.contains(instant)) {
			return false;
		}
		if (closedDayOfWeek.size() > 0 && calendar != null) {
			final DayAsDate d = calendar.toDayAsDate((InstantDay) instant);
			if (closedDayOfWeek.contains(d.getDayOfWeek())) {
				return true;
			}
		}
		return this.closed.contains(instant);
	}

	public void addCloseDay(Instant instant) {
		this.closed.add(instant);
	}

	public void addForceOnDay(Instant instant) {
		this.forcedOn.add(instant);
	}

	public void addCloseDay(DayOfWeek dayOfWeek) {
		closedDayOfWeek.add(dayOfWeek);
	}
}
