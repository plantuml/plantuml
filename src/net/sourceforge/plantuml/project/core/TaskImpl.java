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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.PlanUtils;
import net.sourceforge.plantuml.project.Solver3;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.time.Wink;

public class TaskImpl extends AbstractTask implements Task, LoadPlanable {

	private final Solver3 solver;
	private final Map<Resource, Integer> resources2 = new LinkedHashMap<Resource, Integer>();
	private final LoadPlanable defaultPlan;
	private boolean diamond;

	private Url url;
	private ComplementColors colors;

	public void setUrl(Url url) {
		this.url = url;
	}

	public TaskImpl(TaskCode code, LoadPlanable defaultPlan) {
		super(code);
		this.defaultPlan = defaultPlan;
		this.solver = new Solver3(this);
		setStart(new Wink(0));
		setLoad(Load.inWinks(1));
	}

	public int getLoadAt(Wink instant) {
		LoadPlanable result = defaultPlan;
		if (resources2.size() > 0) {
			result = PlanUtils.multiply(defaultPlan, getRessourcePlan());
		}
		return result.getLoadAt(instant);
		// return PlanUtils.minOf(getLoad(), plan1).getLoadAt(instant);
	}

	public int loadForResource(Resource res, Wink instant) {
		if (resources2.keySet().contains(res) && instant.compareTo(getStart()) >= 0
				&& instant.compareTo(getEnd()) <= 0) {
			if (res.isClosedAt(instant)) {
				return 0;
			}
			// int size = 0;
			return resources2.get(res);
			// for (Resource r : resources) {
			// if (r.getLoadAt(i) > 0) {
			// size++;
			// }
			// }
			// return getLoadAt(instant) / size;
		}
		return 0;
	}

	private LoadPlanable getRessourcePlan() {
		if (resources2.size() == 0) {
			throw new IllegalStateException();
		}
		return new LoadPlanable() {

			public int getLoadAt(Wink instant) {
				int result = 0;
				for (Map.Entry<Resource, Integer> ent : resources2.entrySet()) {
					final Resource res = ent.getKey();
					if (res.isClosedAt(instant)) {
						continue;
					}
					final int percentage = ent.getValue();
					result += percentage;
				}
				return result;
			}
		};
	}

	public String getPrettyDisplay() {
		if (resources2.size() > 0) {
			final StringBuilder result = new StringBuilder(code.getSimpleDisplay());
			result.append(" ");
			for (Iterator<Map.Entry<Resource, Integer>> it = resources2.entrySet().iterator(); it.hasNext();) {
				final Map.Entry<Resource, Integer> ent = it.next();
				result.append("{");
				result.append(ent.getKey().getName());
				final int percentage = ent.getValue();
				if (percentage != 100) {
					result.append(":" + percentage + "%");
				}
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

	public Wink getStart() {
		Wink result = (Wink) solver.getData(TaskAttribute.START);
		while (getLoadAt(result) == 0) {
			result = result.increment();
		}
		return result;
	}

	public Wink getEnd() {
		return (Wink) solver.getData(TaskAttribute.END);
	}

	public Load getLoad() {
		return (Load) solver.getData(TaskAttribute.LOAD);
	}

	public void setLoad(Load load) {
		solver.setData(TaskAttribute.LOAD, load);
	}

	public void setStart(Wink start) {
		solver.setData(TaskAttribute.START, start);
	}

	public void setEnd(Wink end) {
		solver.setData(TaskAttribute.END, end);
	}

	public void setColors(ComplementColors colors) {
		this.colors = colors;
	}

	public void addResource(Resource resource, int percentage) {
		this.resources2.put(resource, percentage);
	}

	public void setDiamond(boolean diamond) {
		this.diamond = diamond;
	}

	public boolean isDiamond() {
		return this.diamond;
	}

	private int completion = 100;

	public void setCompletion(int completion) {
		this.completion = completion;
	}

	public final Url getUrl() {
		return url;
	}

	public final ComplementColors getColors() {
		return colors;
	}

	public final int getCompletion() {
		return completion;
	}

}
