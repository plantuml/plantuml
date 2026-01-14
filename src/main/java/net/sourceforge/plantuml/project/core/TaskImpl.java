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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.ngm.NGMAllocation;
import net.sourceforge.plantuml.project.ngm.math.Combiner;
import net.sourceforge.plantuml.project.ngm.math.Fraction;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantSpecificDays;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantUtils;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstantWeekday;
import net.sourceforge.plantuml.project.solver.Solver;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.url.Url;

public class TaskImpl extends AbstractTask implements Task {

	private final SortedSet<LocalDate> pausedDay = new TreeSet<>();
	private final EnumSet<DayOfWeek> pausedDayOfWeek = EnumSet.noneOf(DayOfWeek.class);

	private final Solver solver;
	private final Map<Resource, Integer> resources = new LinkedHashMap<Resource, Integer>();
	private boolean diamond;
	private final GanttDiagram gantt;

	private int completion;
	private Display note;
	private Stereotype noteStereotype;

	private Url url;
	private CenterBorderColor[] colors;

	public void setUrl(Url url) {
		this.url = url;
	}

	public TaskImpl(GanttDiagram gantt, StyleBuilder styleBuilder, TaskCode code, TimePoint startingDay,
			int completion) {
		super(styleBuilder, code);
		this.gantt = gantt;
		this.completion = completion;

		this.solver = new Solver();
		setStart(startingDay);
		setLoad(Load.ofDays(1));
	}

	public PiecewiseConstant getDefaultPlan() {
		return gantt.getLoadPlanableForTask(getCode().getId());
	}

	private PiecewiseConstant localPause() {
		PiecewiseConstantWeekday weekPattern = PiecewiseConstantWeekday.of(Fraction.ONE);

		for (DayOfWeek day : pausedDayOfWeek)
			weekPattern = weekPattern.with(day, Fraction.ZERO);

		PiecewiseConstant result = weekPattern;

		if (pausedDay.isEmpty() == false) {
			PiecewiseConstantSpecificDays specificDaysClosed = PiecewiseConstantSpecificDays.of(Fraction.ONE);

			for (LocalDate date : pausedDay)
				specificDaysClosed = specificDaysClosed.withDay(date, Fraction.ZERO);

			result = Combiner.product(weekPattern, specificDaysClosed);
		}

		return result;
	}

//	@Override
//	public int getLoadAtDUMMY(TimePoint instant) {
//		if (isPaused(instant))
//			return 0;
//
//		LoadPlanable result = defaultPlan;
//		if (resources.size() > 0)
//			result = PlanUtils.multiply(defaultPlan, getResourcePlan());
//
//		return result.getLoadAtDUMMY(instant);
//	}

	public PiecewiseConstant asPiecewiseConstant() {
		if (resources.size() > 0)
			return Combiner.product(getDefaultPlan(), allRessources());
		return Combiner.product(getDefaultPlan(), localPause());
	}

	private boolean isPaused(TimePoint instant) {
		if (pausedDay.contains(instant.toDay()))
			return true;

		if (pausedDayOfWeek(instant))
			return true;

		return false;
	}

	private boolean pausedDayOfWeek(TimePoint instant) {
		for (DayOfWeek dayOfWeek : pausedDayOfWeek)
			if (instant.toDayOfWeek() == dayOfWeek)
				return true;

		return false;
	}

	public int loadForResource(Resource res, TimePoint instant) {
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
	public void addPause(LocalDate pause) {
		this.pausedDay.add(pause);
	}

	@Override
	public void addPause(DayOfWeek pause) {
		this.pausedDayOfWeek.add(pause);
	}

	public PiecewiseConstant allRessources() {

		final List<PiecewiseConstant> contributions = new ArrayList<>();

		for (Map.Entry<Resource, Integer> ent : resources.entrySet()) {
			final Resource res = ent.getKey();
			final PiecewiseConstant availability = res.asPiecewiseConstant();

			final PiecewiseConstant percentageFraction = PiecewiseConstantSpecificDays
					.of(new Fraction(ent.getValue(), 100));

			// Multiply availability by percentage to get the contribution
			final PiecewiseConstant contribution = Combiner.product(availability, percentageFraction);

			contributions.add(contribution);
		}

		return Combiner.product(localPause(), Combiner.sum(contributions.toArray(new PiecewiseConstant[0])));
	}

	public String getPrettyDisplay() {
		if (resources.size() > 0) {
			final StringBuilder result = new StringBuilder(getCode().getDisplay());
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
		return getCode().getDisplay();
	}

	@Override
	public String toString() {
		return getCode().toString();
	}

	public String debug() {
		return "" + getStart() + " ---> " + getEnd() + "   [" + getLoad() + "]";
	}

	@Override
	public TimePoint getStart() {
		final NGMAllocation allocation = NGMAllocation.of(this.asPiecewiseConstant());
		TimePoint result = (TimePoint) solver.getData(allocation, TaskAttribute.START);
		if (diamond == false) {
			final PiecewiseConstant cal = asPiecewiseConstant();
			while (PiecewiseConstantUtils.isZeroOnDay(cal, result.toDay()))
				result = result.increment();
		}
		return result;
	}

	@Override
	public TimePoint getEnd() {
		final NGMAllocation allocation = NGMAllocation.of(this.asPiecewiseConstant());
		final TimePoint result = (TimePoint) solver.getData(allocation, TaskAttribute.END);
		return result.decrement();
	}

	@Override
	public Load getLoad() {
		final NGMAllocation allocation = NGMAllocation.of(this.asPiecewiseConstant());
		return (Load) solver.getData(allocation, TaskAttribute.LOAD);
	}

	@Override
	public void setLoad(Load load) {
		solver.setData(TaskAttribute.LOAD, load);
	}

	@Override
	public void setStart(TimePoint start) {
		solver.setData(TaskAttribute.START, start);
	}

	@Override
	public void setEnd(TimePoint end) {
		solver.setData(TaskAttribute.END, end.increment());
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

	public final Collection<LocalDate> getAllPaused() {
		final SortedSet<LocalDate> result = new TreeSet<>(pausedDay);
		for (DayOfWeek dayOfWeek : pausedDayOfWeek)
			addAll(result, dayOfWeek);

		return Collections.unmodifiableCollection(result);
	}

	private void addAll(SortedSet<LocalDate> result, DayOfWeek dayOfWeek) {
		final TimePoint start = getStart();
		final TimePoint end = getEnd();
		for (TimePoint current = start; current.compareTo(end) <= 0; current = current.increment())
			if (current.toDayOfWeek() == dayOfWeek)
				result.add(current.toDay());

	}

	@Override
	public void setNote(Display note, Stereotype stereotype) {
		this.note = note;
		this.noteStereotype = stereotype;
	}

	public Display getNote() {
		return note;
	}

	public Stereotype getNoteStereotype() {
		return noteStereotype;
	}

	@Override
	public boolean isAssignedTo(Resource res) {
		return resources.containsKey(res);
	}

}
