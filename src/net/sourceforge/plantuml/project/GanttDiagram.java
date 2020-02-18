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
package net.sourceforge.plantuml.project;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorSetSimple;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.IHtmlColorSet;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.core.Moment;
import net.sourceforge.plantuml.project.core.MomentImpl;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.core.Wink;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.TimeHeader;
import net.sourceforge.plantuml.project.draw.TimeHeaderDaily;
import net.sourceforge.plantuml.project.draw.TimeHeaderSimple;
import net.sourceforge.plantuml.project.draw.TimeHeaderWeekly;
import net.sourceforge.plantuml.project.lang.ComplementColors;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GanttDiagram extends TitledDiagram implements Subject {

	private final Map<TaskCode, Task> tasks = new LinkedHashMap<TaskCode, Task>();
	private final Map<String, Task> byShortName = new HashMap<String, Task>();
	private final List<GanttConstraint> constraints = new ArrayList<GanttConstraint>();
	private final IHtmlColorSet colorSet = new HtmlColorSetSimple();

	private final Collection<DayOfWeek> closedDayOfWeek = EnumSet.noneOf(DayOfWeek.class);
	private final Collection<DayAsDate> closedDayAsDate = new HashSet<DayAsDate>();
	private final Collection<DayAsDate> openedDayAsDate = new HashSet<DayAsDate>();

	private final Map<String, Resource> resources = new LinkedHashMap<String, Resource>();
	private final Map<DayAsDate, HtmlColor> colorDays = new HashMap<DayAsDate, HtmlColor>();
	private final Map<DayAsDate, String> nameDays = new HashMap<DayAsDate, String>();

	private PrintScale printScale = PrintScale.DAILY;
	private DayAsDate today;
	private GCalendar calendar;
	private double totalHeight;
	private Wink min = new Wink(0);
	private Wink max;

	private DayAsDate printStart;
	private DayAsDate printEnd;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Project)");
	}

	private int horizontalPages = 1;
	private int verticalPages = 1;

	final public int getHorizontalPages() {
		return horizontalPages;
	}

	final public void setHorizontalPages(int horizontalPages) {
		this.horizontalPages = horizontalPages;
	}

	final public int getVerticalPages() {
		return verticalPages;
	}

	final public void setVerticalPages(int verticalPages) {
		this.verticalPages = verticalPages;
	}

	@Override
	public int getNbImages() {
		return this.horizontalPages * this.verticalPages;
	}

	public final int getDpi(FileFormatOption fileFormatOption) {
		return 96;
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption, long seed)
			throws IOException {
		final double margin = 10;

		final Scale scale = getScale();

		final double dpiFactor = scale == null ? 1 : scale.getScale(100, 100);
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), dpiFactor, null, "", "", 0, 0,
				null, false);
		final SkinParam skinParam = SkinParam.create(UmlDiagramType.TIMING);

		TextBlock result = getTextBlock();
		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder()).addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, seed, os);
	}

	public void setPrintScale(PrintScale printScale) {
		this.printScale = printScale;
	}

	private TextBlockBackcolored getTextBlock() {
		if (printStart == null) {
			initMinMax();
		} else {
			this.min = calendar.fromDayAsDate(printStart);
			this.max = calendar.fromDayAsDate(printEnd);
		}
		final TimeHeader timeHeader;
		if (calendar == null) {
			timeHeader = new TimeHeaderSimple(min, max);
		} else if (printScale == PrintScale.WEEKLY) {
			timeHeader = new TimeHeaderWeekly(calendar, min, max, getDefaultPlan(), colorDays, nameDays);
		} else {
			timeHeader = new TimeHeaderDaily(calendar, min, max, getDefaultPlan(), colorDays, nameDays, printStart,
					printEnd);
		}
		initTaskAndResourceDraws(timeHeader.getTimeScale(), timeHeader.getFullHeaderHeight());
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				timeHeader.drawTimeHeader(ug, totalHeight);
				drawConstraints(ug, timeHeader.getTimeScale());
				drawTasksRect(ug);
				drawTasksTitle(ug);
				drawResources(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double xmin = timeHeader.getTimeScale().getStartingPosition(min);
				final double xmax = timeHeader.getTimeScale().getEndingPosition(max);
				return new Dimension2DDouble(xmax - xmin, totalHeight);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HtmlColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawTasksRect(UGraphic ug) {
		for (Task task : tasks.values()) {
			final TaskDraw draw = task.getTaskDraw();
			final UTranslate move = new UTranslate(0, draw.getY());
			draw.drawU(ug.apply(move));
		}
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : constraints) {
			constraint.getUDrawable(timeScale).drawU(ug);
		}
	}

	private void drawTasksTitle(final UGraphic ug1) {
		for (Task task : tasks.values()) {
			final TaskDraw draw = task.getTaskDraw();
			final UTranslate move = new UTranslate(0, draw.getY());
			draw.drawTitle(ug1.apply(move));
		}
	}

	private void drawResources(UGraphic ug) {
		for (Resource res : resources.values()) {
			final ResourceDraw draw = res.getResourceDraw();
			final UTranslate move = new UTranslate(0, draw.getY());
			draw.drawU(ug.apply(move));
		}
	}

	public final LoadPlanable getDefaultPlan() {
		return new LoadPlanable() {
			public int getLoadAt(Wink instant) {
				if (calendar == null) {
					return 100;
				}
				final DayAsDate day = calendar.toDayAsDate((Wink) instant);
				if (isClosed(day)) {
					return 0;
				}
				return 100;
			}
		};
	}

	private boolean isClosed(final DayAsDate day) {
		if (openedDayAsDate.contains(day)) {
			return false;
		}
		final DayOfWeek dayOfWeek = day.getDayOfWeek();
		return closedDayOfWeek.contains(dayOfWeek) || closedDayAsDate.contains(day);
	}

	public void closeDayOfWeek(DayOfWeek day) {
		closedDayOfWeek.add(day);
	}

	public void closeDayAsDate(DayAsDate day) {
		closedDayAsDate.add(day);
	}

	public void openDayAsDate(DayAsDate day) {
		openedDayAsDate.add(day);
	}

	private void initTaskAndResourceDraws(TimeScale timeScale, double headerHeight) {
		double y = headerHeight;
		for (Task task : tasks.values()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				draw = new TaskDrawSeparator((TaskSeparator) task, timeScale, y, min, max);
			} else {
				draw = new TaskDrawRegular((TaskImpl) task, timeScale, y);
			}
			task.setTaskDraw(draw);
			y += draw.getHeight();

		}
		for (Resource res : resources.values()) {
			final ResourceDraw draw = new ResourceDraw(this, res, timeScale, y, min, max);
			res.setTaskDraw(draw);
			y += draw.getHeight();

		}
		this.totalHeight = y;
	}

	private void initMinMax() {
		if (tasks.size() == 0) {
			max = min.increment();
		} else {
			max = null;
			for (Task task : tasks.values()) {
				if (task instanceof TaskSeparator) {
					continue;
				}
				final Wink start = task.getStart();
				final Wink end = task.getEnd();
				// if (min.compareTo(start) > 0) {
				// min = start;
				// }
				if (max == null || max.compareTo(end) < 0) {
					max = end;
				}
			}
		}
		if (calendar != null) {
			for (DayAsDate d : colorDays.keySet()) {
				final Wink instant = calendar.fromDayAsDate(d);
				if (instant.compareTo(max) > 0) {
					max = instant;
				}
			}
			for (DayAsDate d : nameDays.keySet()) {
				final Wink instant = calendar.fromDayAsDate(d);
				if (instant.compareTo(max) > 0) {
					max = instant;
				}
			}
		}
	}

	public DayAsDate getThenDate() {
		DayAsDate result = getStartingDate();
		for (DayAsDate d : colorDays.keySet()) {
			if (d.compareTo(result) > 0) {
				result = d;
			}
		}
		for (DayAsDate d : nameDays.keySet()) {
			if (d.compareTo(result) > 0) {
				result = d;
			}
		}
		return result;
	}

	public Task getExistingTask(String id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		Task result = byShortName.get(id);
		if (result != null) {
			return result;
		}
		final TaskCode code = new TaskCode(id);
		return tasks.get(code);
	}

	public void setTaskOrder(final Task task1, final Task task2) {
		final TaskInstant end1 = new TaskInstant(task1, TaskAttribute.END);
		task2.setStart(end1.getInstantPrecise());
		addContraint(new GanttConstraint(end1, new TaskInstant(task2, TaskAttribute.START)));
	}

	public Task getOrCreateTask(String codeOrShortName, String shortName, boolean linkedToPrevious) {
		if (codeOrShortName == null) {
			throw new IllegalArgumentException();
		}
		Task result = shortName == null ? null : byShortName.get(shortName);
		if (result != null) {
			return result;
		}
		result = byShortName.get(codeOrShortName);
		if (result != null) {
			return result;
		}
		final TaskCode code = new TaskCode(codeOrShortName);
		result = tasks.get(code);
		if (result == null) {
			Task previous = null;
			if (linkedToPrevious) {
				previous = getLastCreatedTask();
			}
			result = new TaskImpl(code, getDefaultPlan());
			tasks.put(code, result);
			if (byShortName != null) {
				byShortName.put(shortName, result);
			}
			if (previous != null) {
				setTaskOrder(previous, result);
			}
		}
		return result;
	}

	private Task getLastCreatedTask() {
		final List<Task> all = new ArrayList<Task>(tasks.values());
		for (int i = all.size() - 1; i >= 0; i--) {
			if (all.get(i) instanceof TaskImpl) {
				return all.get(i);
			}
		}
		return null;
	}

	public void addSeparator(String comment) {
		TaskSeparator separator = new TaskSeparator(comment, tasks.size());
		tasks.put(separator.getCode(), separator);
	}

	public void addContraint(GanttConstraint constraint) {
		constraints.add(constraint);
	}

	public IHtmlColorSet getIHtmlColorSet() {
		return colorSet;
	}

	public void setStartingDate(DayAsDate start) {
		this.calendar = new GCalendar(start);
	}

	public DayAsDate getStartingDate() {
		if (this.calendar == null) {
			return null;
		}
		return this.calendar.getStartingDate();
	}

	public DayAsDate getStartingDate(int nday) {
		if (this.calendar == null) {
			return null;
		}
		return this.calendar.toDayAsDate(new Wink(nday));
	}

	public int daysInWeek() {
		return 7 - closedDayOfWeek.size();
	}

	public Wink convert(DayAsDate day) {
		return calendar.fromDayAsDate(day);
	}

	public boolean isOpen(DayAsDate day) {
		return getDefaultPlan().getLoadAt(convert(day)) > 0;
	}

	public void affectResource(Task result, String description) {
		final Pattern p = Pattern.compile("([^:]+)(:(\\d+))?");
		final Matcher m = p.matcher(description);
		if (m.find() == false) {
			throw new IllegalArgumentException();
		}
		final Resource resource = getResource(m.group(1));
		int percentage = 100;
		if (m.group(3) != null) {
			percentage = Integer.parseInt(m.group(3));
		}
		result.addResource(resource, percentage);
	}

	public Resource getResource(String resourceName) {
		Resource resource = resources.get(resourceName);
		if (resource == null) {
			resource = new Resource(resourceName, getDefaultPlan(), calendar);
		}
		resources.put(resourceName, resource);
		return resource;
	}

	public int getLoadForResource(Resource res, Wink i) {
		int result = 0;
		for (Task task : tasks.values()) {
			if (task instanceof TaskSeparator) {
				continue;
			}
			final TaskImpl task2 = (TaskImpl) task;
			result += task2.loadForResource(res, i);
		}
		return result;
	}

	public Moment getExistingMoment(String id) {
		Moment result = getExistingTask(id);
		if (result == null) {
			DayAsDate start = null;
			DayAsDate end = null;
			for (Map.Entry<DayAsDate, String> ent : nameDays.entrySet()) {
				if (ent.getValue().equalsIgnoreCase(id) == false) {
					continue;
				}
				start = min(start, ent.getKey());
				end = max(end, ent.getKey());
			}
			if (start != null) {
				result = new MomentImpl(convert(start), convert(end));
			}
		}
		return result;
	}

	private DayAsDate min(DayAsDate d1, DayAsDate d2) {
		if (d1 == null) {
			return d2;
		}
		if (d1.compareTo(d2) > 0) {
			return d2;
		}
		return d1;
	}

	private DayAsDate max(DayAsDate d1, DayAsDate d2) {
		if (d1 == null) {
			return d2;
		}
		if (d1.compareTo(d2) < 0) {
			return d2;
		}
		return d1;
	}

	public void colorDay(DayAsDate day, HtmlColor color) {
		colorDays.put(day, color);
	}

	public void nameDay(DayAsDate day, String name) {
		nameDays.put(day, name);
	}

	public void setTodayColors(ComplementColors colors) {
		if (today == null) {
			this.today = DayAsDate.today();
		}
		colorDay(today, colors.getCenter());
	}

	public CommandExecutionResult setToday(DayAsDate date) {
		this.today = date;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult deleteTask(Task task) {
		task.setColors(new ComplementColors(HtmlColorUtils.WHITE, HtmlColorUtils.BLACK));
		return CommandExecutionResult.ok();
	}

	public void setPrintInterval(DayAsDate start, DayAsDate end) {
		this.printStart = start;
		this.printEnd = end;
	}

}
