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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.PlanUtils;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.solver.Solver;
import net.sourceforge.plantuml.project.solver.SolverImpl;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.url.Url;

public class TaskImpl extends AbstractTask implements Task, LoadPlanable {

	private final SortedSet<Day> pausedDay = new TreeSet<>();
	private final Set<DayOfWeek> pausedDayOfWeek = new HashSet<>();
	private final Solver solver;
	private final Map<Resource, Integer> resources = new LinkedHashMap<Resource, Integer>();
	private final LoadPlanable defaultPlan;
	private boolean diamond;

	private int completion;
	private Display note;

	private Url url;
	private CenterBorderColor[] colors;

	public void setUrl(Url url) {
		this.url = url;
	}

	public TaskImpl(StyleBuilder styleBuilder, TaskCode code, LoadPlanable plan, Day startingDay, int completion) {
		super(styleBuilder, code);
		this.completion = completion;
		this.defaultPlan = plan;
		this.solver = new SolverImpl(this);
		if (startingDay == null)
			setStart(Day.create(0));
		else
			setStart(startingDay);

		setLoad(Load.inWinks(1));
	}

	@Override
	public int getLoadAt(Day instant) {
		if (isPaused(instant))
			return 0;

		LoadPlanable result = defaultPlan;
		if (resources.size() > 0)
			result = PlanUtils.multiply(defaultPlan, getResourcePlan());

		return result.getLoadAt(instant);
	}

	private boolean isPaused(Day instant) {
		if (pausedDay.contains(instant))
			return true;

		if (pausedDayOfWeek(instant))
			return true;

		return false;
	}

	private boolean pausedDayOfWeek(Day instant) {
		for (DayOfWeek dayOfWeek : pausedDayOfWeek)
			if (instant.getDayOfWeek() == dayOfWeek)
				return true;

		return false;
	}

	public int loadForResource(Resource res, Day instant) {
		if (resources.keySet().contains(res) && instant.compareTo(getStart()) >= 0
				&& instant.compareTo(getEnd()) <= 0) {
			if (isPaused(instant))
				return 0;

			if (res.isClosedAt(instant))
				return 0;

			return resources.get(res);
		}
		return 0;
	}

	@Override
	public void addPause(Day pause) {
		this.pausedDay.add(pause);
	}

	@Override
	public void addPause(DayOfWeek pause) {
		this.pausedDayOfWeek.add(pause);
	}

	private LoadPlanable getResourcePlan() {
		if (resources.size() == 0)
			throw new IllegalStateException();

		return new LoadPlanable() {
			public int getLoadAt(Day instant) {
				int result = 0;
				for (Map.Entry<Resource, Integer> ent : resources.entrySet()) {
					final Resource res = ent.getKey();
					if (res.isClosedAt(instant))
						continue;

					final int percentage = ent.getValue();
					result += percentage;
				}
				return result;
			}

			@Override
			public Day getLastDayIfAny() {
				return TaskImpl.this.getLastDayIfAny();
			}
		};
	}

	@Override
	public Day getLastDayIfAny() {
		Day result = null;

		for (Resource res : resources.keySet()) {
			if (res.getLastDayIfAny() == null)
				return null;

			if (result == null || result.compareTo(res.getLastDayIfAny()) < 0)
				result = res.getLastDayIfAny();
		}

		return result;
	}

	public String getPrettyDisplay() {
		if (resources.size() > 0) {
			final StringBuilder result = new StringBuilder(getCode().getSimpleDisplay());
			result.append(" ");
			for (Iterator<Map.Entry<Resource, Integer>> it = resources.entrySet().iterator(); it.hasNext();) {
				final Map.Entry<Resource, Integer> ent = it.next();
				result.append("{");
				result.append(ent.getKey().getName());
				final int percentage = ent.getValue();
				if (percentage != 100)
					result.append(":" + percentage + "%");

				result.append("}");
				if (it.hasNext())
					result.append(" ");

			}
			return result.toString();
		}
		return getCode().getSimpleDisplay();
	}

	@Override
	public String toString() {
		return getCode().toString();
	}

	public String debug() {
		return "" + getStart() + " ---> " + getEnd() + "   [" + getLoad() + "]";
	}

	@Override
	public Day getStart() {
		Day result = (Day) solver.getData(TaskAttribute.START);
		if (diamond == false)
			while (getLoadAt(result) == 0)
				result = result.increment();

		return result;
	}

	@Override
	public Day getEnd() {
		return (Day) solver.getData(TaskAttribute.END);
	}

	@Override
	public Load getLoad() {
		return (Load) solver.getData(TaskAttribute.LOAD);
	}

	@Override
	public void setLoad(Load load) {
		solver.setData(TaskAttribute.LOAD, load);
	}

	@Override
	public void setStart(Day start) {
		solver.setData(TaskAttribute.START, start);
	}

	@Override
	public void setEnd(Day end) {
		solver.setData(TaskAttribute.END, end);
	}

	@Override
	public void setColors(CenterBorderColor... colors) {
		this.colors = colors;
	}

	@Override
	public void addResource(Resource resource, int percentage) {
		this.resources.put(resource, percentage);
	}

	@Override
	public void setDiamond(boolean diamond) {
		this.diamond = diamond;
	}

	@Override
	public boolean isDiamond() {
		return this.diamond;
	}

	@Override
	public void setCompletion(int completion) {
		this.completion = completion;
	}

	public final Url getUrl() {
		return url;
	}

	public final CenterBorderColor getColors() {
		if (colors == null)
			return null;

		if (colors.length == 1)
			return colors[0];

		return colors[0].unlinearTo(colors[1], completion);
	}

	public final int getCompletion() {
		return completion;
	}

	public final Collection<Day> getAllPaused() {
		final SortedSet<Day> result = new TreeSet<>(pausedDay);
		for (DayOfWeek dayOfWeek : pausedDayOfWeek)
			addAll(result, dayOfWeek);

		return Collections.unmodifiableCollection(result);
	}

	private void addAll(SortedSet<Day> result, DayOfWeek dayOfWeek) {
		final Day start = getStart();
		final Day end = getEnd();
		for (Day current = start; current.compareTo(end) <= 0; current = current.increment())
			if (current.getDayOfWeek() == dayOfWeek)
				result.add(current);

	}

	@Override
	public void setNote(Display note) {
		this.note = note;
	}

	public Display getNote() {
		return note;
	}

	public LoadPlanable getDefaultPlan() {
		return defaultPlan;
	}

	@Override
	public boolean isAssignedTo(Resource res) {
		return resources.containsKey(res);
	}

}
