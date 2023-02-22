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
package net.sourceforge.plantuml.project.core;

import net.sourceforge.plantuml.project.GanttConstraintMode;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.time.Day;

public class TaskInstant {

	private final Moment task;
	private final TaskAttribute attribute;
	private final int delta;
	private final GanttConstraintMode mode;
	private final LoadPlanable calendar;

	public TaskInstant(Moment task, TaskAttribute attribute) {
		this(task, attribute, 0, GanttConstraintMode.IGNORE_CALENDAR, null);
	}

	private TaskInstant(Moment task, TaskAttribute attribute, int delta, GanttConstraintMode mode,
			LoadPlanable calendar) {
		this.task = task;
		this.attribute = attribute;
		this.delta = delta;
		this.mode = mode;
		this.calendar = calendar;
		if (attribute != TaskAttribute.START && attribute != TaskAttribute.END)
			throw new IllegalArgumentException();

	}

	public TaskInstant withDelta(int newDelta, GanttConstraintMode mode, LoadPlanable calendar) {
		return new TaskInstant(task, attribute, newDelta, mode, calendar);
	}

	private Day manageDelta(Day value) {
		if (delta > 0)
			for (int i = 0; i < delta; i++) {
				if (mode == GanttConstraintMode.DO_NOT_COUNT_CLOSE_DAY) {
					int tmp = 0;
					while (calendar.getLoadAt(value) == 0 && tmp++ < 1000)
						value = value.increment();
				}

				value = value.increment();
			}

		if (delta < 0)
			for (int i = 0; i < -delta; i++)
				value = value.decrement();

		return value;
	}

	public Day getInstantPrecise() {
		if (attribute == TaskAttribute.START)
			return manageDelta(task.getStart());

		if (attribute == TaskAttribute.END)
			return manageDelta(task.getEnd().increment());

		throw new IllegalStateException();
	}

	public Day getInstantTheorical() {
		if (attribute == TaskAttribute.START)
			return manageDelta(task.getStart());

		if (attribute == TaskAttribute.END)
			return manageDelta(task.getEnd());

		throw new IllegalStateException();
	}

	@Override
	public String toString() {
		return attribute.toString() + " of " + task;
	}

	public final Moment getMoment() {
		return task;
	}

	public final boolean isTask() {
		return task instanceof AbstractTask;
	}

	public final TaskAttribute getAttribute() {
		return attribute;
	}

	public boolean sameRowAs(TaskInstant dest) {
		if (this.isTask() && dest.isTask()) {
			final AbstractTask t1 = (AbstractTask) this.getMoment();
			final AbstractTask t2 = (AbstractTask) dest.getMoment();
			if (t1 == t2.getRow() || t2 == t1.getRow())
				return true;

		}
		return false;
	}

}
