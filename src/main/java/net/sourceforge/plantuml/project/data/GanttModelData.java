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
package net.sourceforge.plantuml.project.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.time.TimePoint;

/**
 * Value object containing the core domain data of a Gantt diagram: tasks,
 * resources, and constraints.
 */
public class GanttModelData {

	private final List<GanttConstraint> constraints = new ArrayList<>();
	private final Map<TaskCode, Task> tasks = new LinkedHashMap<>();
	private final Map<String, Resource> resources = new LinkedHashMap<>();

	public Collection<Task> getTasks() {
		return Collections.unmodifiableCollection(tasks.values());
	}

	public Collection<Resource> getResources() {
		return Collections.unmodifiableCollection(resources.values());
	}

	public Collection<GanttConstraint> getConstraints() {
		return Collections.unmodifiableCollection(constraints);
	}

	// Mutators for building the model

	public void addConstraint(GanttConstraint constraint) {
		constraints.add(constraint);
	}

	public void putTask(TaskCode code, Task task) {
		tasks.put(code, task);
	}

	public Task getTask(TaskCode code) {
		return tasks.get(code);
	}

	public void putResource(String name, Resource resource) {
		resources.put(name, resource);
	}

	public Resource getResource(String name) {
		return resources.get(name);
	}

	public int getLoadForResource(Resource res, TimePoint i) {
		int result = 0;
		for (Task task : getTasks()) {
			if (task instanceof TaskSeparator)
				continue;

			final TaskImpl task2 = (TaskImpl) task;
			result += task2.loadForResource(res, i);
		}
		return result;
	}

	public Collection<GanttConstraint> getConstraintsForTask(Task task) {
		final List<GanttConstraint> result = new ArrayList<>();
		for (GanttConstraint constraint : getConstraints())
			if (constraint.isOn(task))
				result.add(constraint);

		return Collections.unmodifiableCollection(result);
	}

}
