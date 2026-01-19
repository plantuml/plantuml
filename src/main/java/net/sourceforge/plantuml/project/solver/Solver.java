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
package net.sourceforge.plantuml.project.solver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.Value;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.ngm.NGMAllocation;
import net.sourceforge.plantuml.project.ngm.NGMTask;
import net.sourceforge.plantuml.project.ngm.NGMTotalEffort;
import net.sourceforge.plantuml.project.time.TimePoint;

public class Solver {
	// ::remove folder when __HAXE__

	private final Map<TaskAttribute, Value> values = new LinkedHashMap<TaskAttribute, Value>();

//	public Solver(NGMAllocation allocation) {
//		this.allocation = allocation;
//	}

	public final void setData(TaskAttribute attribute, Value value) {
		final Value previous = values.remove(attribute);
		if (previous != null && attribute == TaskAttribute.START) {
			final TimePoint previousInstant = (TimePoint) previous;
			if (previousInstant.compareTo((TimePoint) value) > 0)
				value = previous;

		}
		values.put(attribute, value);
		if (values.size() > 2)
			removeFirstElement();

		assert values.size() <= 2;

	}

	private void removeFirstElement() {
		final Iterator<Entry<TaskAttribute, Value>> it = values.entrySet().iterator();
		it.next();
		it.remove();
	}

	public final Value getData(NGMAllocation allocation, TaskAttribute attribute) {
		Value result = values.get(attribute);
		if (result == null) {
			if (attribute == TaskAttribute.END)
				return computeEnd(allocation);

			if (attribute == TaskAttribute.START)
				return computeStart(allocation);

			return Load.ofDays(1);
			// throw new UnsupportedOperationException(attribute.toString());
		}
		return result;
	}

	private TimePoint computeEnd(NGMAllocation allocation) {
		final TimePoint start = (TimePoint) values.get(TaskAttribute.START);
		final NGMTotalEffort fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getEffort();
		final NGMTask task = NGMTask.withFixedTotalEffort(allocation, start.toLocalDateTime(), fullLoad);
		return TimePoint.of(task.getEnd());
	}

	private TimePoint computeStart(NGMAllocation allocation) {
		final TimePoint end = (TimePoint) values.get(TaskAttribute.END);
		final NGMTotalEffort fullLoad = ((Load) values.get(TaskAttribute.LOAD)).getEffort();
		final NGMTask task = NGMTask.withFixedTotalEffort(allocation, end.toLocalDateTime(), fullLoad);
		task.setEnd(end.toLocalDateTime());
		return TimePoint.of(task.getStart());
	}

}
