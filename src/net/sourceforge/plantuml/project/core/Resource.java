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
package net.sourceforge.plantuml.project.core;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public class Resource {

	private final String name;
	private ResourceDraw draw;
	private final Set<Day> closed = new TreeSet<Day>();
	private final Set<Day> forcedOn = new TreeSet<Day>();

	private final Collection<DayOfWeek> closedDayOfWeek = EnumSet.noneOf(DayOfWeek.class);

	public Resource(String name, LoadPlanable loadPlanable) {
		this.name = name;
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

	public boolean isClosedAt(Day instant) {
		if (this.forcedOn.contains(instant)) {
			return false;
		}
		if (closedDayOfWeek.size() > 0) {
			if (closedDayOfWeek.contains(instant.getDayOfWeek())) {
				return true;
			}
		}
		return this.closed.contains(instant);
	}

	public void addCloseDay(Day instant) {
		this.closed.add(instant);
	}

	public void addForceOnDay(Day instant) {
		this.forcedOn.add(instant);
	}

	public void addCloseDay(DayOfWeek dayOfWeek) {
		closedDayOfWeek.add(dayOfWeek);
	}
}
