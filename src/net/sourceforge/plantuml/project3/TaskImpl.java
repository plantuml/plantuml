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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class TaskImpl implements Task, LoadPlanable {

	private final TaskCode code;
	private final Solver3 solver;
	private final Set<Resource> resources = new LinkedHashSet<Resource>();
	private final LoadPlanable defaultPlan;

	public TaskImpl(TaskCode code, LoadPlanable defaultPlan) {
		this.code = code;
		this.defaultPlan = defaultPlan;
		this.solver = new Solver3(this);
		setStart(new InstantDay(0));
		setLoad(LoadInDays.inDay(1));
	}

	public int getLoadAt(Instant instant) {
		LoadPlanable plan1 = defaultPlan;
		if (resources.size() > 0) {
			plan1 = PlanUtils.minOf(plan1, getRessourcePlan());
		}
		return PlanUtils.minOf(getLoad(), plan1).getLoadAt(instant);
	}

	public int loadForResource(Resource res, Instant i) {
		if (resources.contains(res) && i.compareTo(getStart()) >= 0 && i.compareTo(getEnd()) <= 0) {
			if (res.getLoadAt(i) == 0) {
				return 0;
			}
			int size = 0;
			for (Resource r : resources) {
				if (r.getLoadAt(i) > 0) {
					size++;
				}
			}
			return getLoadAt(i) / size;
		}
		return 0;
	}

	private LoadPlanable getRessourcePlan() {
		if (resources.size() == 0) {
			throw new IllegalStateException();
		}
		return new LoadPlanable() {

			public int getLoadAt(Instant instant) {
				int result = 0;
				for (Resource res : resources) {
					result += res.getLoadAt(instant);
				}
				return result;
			}
		};
	}

	public String getPrettyDisplay() {
		if (resources.size() > 0) {
			final StringBuilder result = new StringBuilder(code.getSimpleDisplay());
			result.append(" ");
			for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
				result.append("{");
				result.append(it.next().getName());
				result.append("}");
				if (it.hasNext()) {
					result.append(" ");
				}
			}
			return result.toString();
		}
		return code.getSimpleDisplay();
	}

	@Override
	public String toString() {
		return code.toString();
	}

	public String debug() {
		return "" + getStart() + " ---> " + getEnd() + "   [" + getLoad() + "]";
	}

	public TaskCode getCode() {
		return code;
	}

	public Instant getStart() {
		Instant result = (Instant) solver.getData(TaskAttribute.START);
		while (getLoadAt(result) == 0) {
			result = result.increment();
		}
		return result;
	}

	public Instant getEnd() {
		return (Instant) solver.getData(TaskAttribute.END);
	}

	public Load getLoad() {
		return (Load) solver.getData(TaskAttribute.LOAD);
	}

	public void setLoad(Load load) {
		solver.setData(TaskAttribute.LOAD, load);
	}

	public void setStart(Instant start) {
		solver.setData(TaskAttribute.START, start);
	}

	public void setEnd(Instant end) {
		solver.setData(TaskAttribute.END, end);
	}

	private TaskDraw taskDraw;
	private ComplementColors colors;

	public void setTaskDraw(TaskDraw taskDraw) {
		taskDraw.setColors(colors);
		this.taskDraw = taskDraw;
	}

	public TaskDraw getTaskDraw() {
		return taskDraw;
	}

	public void setColors(ComplementColors colors) {
		this.colors = colors;
	}

	public void addResource(Resource resource) {
		this.resources.add(resource);
	}

}
