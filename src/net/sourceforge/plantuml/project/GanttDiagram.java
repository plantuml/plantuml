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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.UDrawable;
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
import net.sourceforge.plantuml.project.draw.FingerPrint;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.TimeHeader;
import net.sourceforge.plantuml.project.draw.TimeHeaderDaily;
import net.sourceforge.plantuml.project.draw.TimeHeaderMonthly;
import net.sourceforge.plantuml.project.draw.TimeHeaderQuarterly;
import net.sourceforge.plantuml.project.draw.TimeHeaderSimple;
import net.sourceforge.plantuml.project.draw.TimeHeaderWeekly;
import net.sourceforge.plantuml.project.draw.TimeHeaderYearly;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.GraphvizCrash;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class GanttDiagram extends TitledDiagram implements ToTaskDraw, WithSprite {

	private final Map<Task, TaskDraw> draws = new LinkedHashMap<Task, TaskDraw>();
	private final Map<TaskCode, Task> tasks = new LinkedHashMap<TaskCode, Task>();
	private final Map<String, Task> byShortName = new HashMap<String, Task>();
	private final List<GanttConstraint> constraints = new ArrayList<>();
	private final HColorSet colorSet = HColorSet.instance();

	private final OpenClose openClose = new OpenClose();

	private final Map<String, Resource> resources = new LinkedHashMap<String, Resource>();
	private final Map<Day, HColor> colorDaysToday = new HashMap<Day, HColor>();
	private final Map<Day, HColor> colorDaysInternal = new HashMap<Day, HColor>();
	private final Map<DayOfWeek, HColor> colorDaysOfWeek = new HashMap<DayOfWeek, HColor>();
	private final Map<Day, String> nameDays = new HashMap<Day, String>();
	private LabelStrategy labelStrategy = new LabelStrategy(LabelPosition.LEGACY, HorizontalAlignment.LEFT);

	// Let's follow ISO-8601 rules
	private WeekNumberStrategy weekNumberStrategy = new WeekNumberStrategy(DayOfWeek.MONDAY, 4);

	private PrintScale printScale = PrintScale.DAILY;
	private double factorScale = 1.0;
	private Locale locale = Locale.ENGLISH;

	private Day today;
	private double totalHeightWithoutFooter;
	private Day min = Day.create(0);
	private Day max;

	private Day printStart;
	private Day printEnd;

	private final RealOrigin origin = RealUtils.createOrigin();

	public CommandExecutionResult changeLanguage(String lang) {
		this.locale = new Locale(lang);
		return CommandExecutionResult.ok();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Project)");
	}

	public void setWeekNumberStrategy(DayOfWeek firstDayOfWeek, int minimalDaysInFirstWeek) {
		this.weekNumberStrategy = new WeekNumberStrategy(firstDayOfWeek, minimalDaysInFirstWeek);
	}

	public GanttDiagram(UmlSource source) {
		super(source, UmlDiagramType.GANTT);
	}

	public final int getDpi(FileFormatOption fileFormatOption) {
		return 96;
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
		return createImageBuilder(fileFormatOption).drawable(getTextBlock(stringBounder)).write(os);
	}

	public void setPrintScale(PrintScale printScale) {
		this.printScale = printScale;
	}

	public void setFactorScale(double factorScale) {
		this.factorScale = factorScale;
	}

	private double getFactorScale() {
		return this.printScale.getDefaultScale() * this.factorScale;
	}

	private boolean isHidden(Task task) {
		if (printStart == null || task instanceof TaskSeparator) {
			return false;
		}
		if (task.getEnd().compareTo(min) < 0) {
			return true;
		}
		if (task.getStart().compareTo(max) > 0) {
			return true;
		}
		return false;
	}

	private TextBlockBackcolored getTextBlock(StringBounder stringBounder) {
		if (printStart == null) {
			initMinMax();
		} else {
			this.min = printStart;
			this.max = printEnd;
		}
		final TimeHeader timeHeader = getTimeHeader();
		initTaskAndResourceDraws(timeHeader.getTimeScale(), timeHeader.getFullHeaderHeight(), stringBounder);
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				try {
					final UGraphic ugOrig = ug;
					if (labelStrategy.titleInFirstColumn())
						ug = ug.apply(UTranslate.dx(getTitlesColumnWidth(ug.getStringBounder())));

					final Style timelineStyle = StyleSignature
							.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
							.getMergedStyle(getCurrentStyleBuilder());

					final HColor back = timelineStyle.value(PName.BackGroundColor)
							.asColor(getSkinParam().getThemeStyle(), getIHtmlColorSet());
					if (HColorUtils.isTransparent(back) == false) {
						final URectangle rect1 = new URectangle(calculateDimension(ug.getStringBounder()).getWidth(),
								timeHeader.getTimeHeaderHeight());
						ug.apply(back.bg()).draw(rect1);
						if (showFootbox) {
							final URectangle rect2 = new URectangle(
									calculateDimension(ug.getStringBounder()).getWidth(),
									timeHeader.getTimeFooterHeight());
							ug.apply(back.bg()).apply(UTranslate.dy(totalHeightWithoutFooter)).draw(rect2);
						}
					}

					timeHeader.drawTimeHeader(ug, totalHeightWithoutFooter);

					drawConstraints(ug, timeHeader.getTimeScale());
					drawTasksRect(ug);
					drawTasksTitle(ugOrig, getTitlesColumnWidth(ug.getStringBounder()), getBarsColumnWidth(timeHeader));
					drawResources(ug);
					if (showFootbox) {
						timeHeader.drawTimeFooter(ug.apply(UTranslate.dy(totalHeightWithoutFooter)));
					}
				} catch (Throwable t) {
					t.printStackTrace();
					final UDrawable crash = new GraphvizCrash(getSource().getPlainString(), false, t);
					crash.drawU(ug);

				}
			}

			private double getTitlesColumnWidth(StringBounder stringBounder) {
				if (labelStrategy.titleInside()) {
					return 0;
				}
				double width = 0;
				for (Task task : tasks.values()) {
					if (isHidden(task)) {
						continue;
					}
					width = Math.max(width, draws.get(task).getTitleWidth(stringBounder));
				}
				return width;
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(getTitlesColumnWidth(stringBounder) + getBarsColumnWidth(timeHeader),
						getTotalHeight(timeHeader));
			}

			private double getBarsColumnWidth(final TimeHeader timeHeader) {
				final double xmin = timeHeader.getTimeScale().getStartingPosition(min);
				final double xmax = timeHeader.getTimeScale().getEndingPosition(max);
				return xmax - xmin;
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private TimeHeader getTimeHeader() {
		if (openClose.getCalendar() == null) {
			return new TimeHeaderSimple(getTimelineStyle(), getClosedStyle(), getFactorScale(), min, max,
					getIHtmlColorSet(), getSkinParam().getThemeStyle(), colorDays());
		} else if (printScale == PrintScale.DAILY) {
			return new TimeHeaderDaily(locale, getTimelineStyle(), getClosedStyle(), getFactorScale(),
					openClose.getCalendar(), min, max, openClose, colorDays(), colorDaysOfWeek, nameDays, printStart,
					printEnd, getIHtmlColorSet(), getSkinParam().getThemeStyle());
		} else if (printScale == PrintScale.WEEKLY) {
			return new TimeHeaderWeekly(weekNumberStrategy, withCalendarDate, locale, getTimelineStyle(),
					getClosedStyle(), getFactorScale(), openClose.getCalendar(), min, max, openClose, colorDays(),
					colorDaysOfWeek, getIHtmlColorSet(), getSkinParam().getThemeStyle());
		} else if (printScale == PrintScale.MONTHLY) {
			return new TimeHeaderMonthly(locale, getTimelineStyle(), getClosedStyle(), getFactorScale(),
					openClose.getCalendar(), min, max, openClose, colorDays(), colorDaysOfWeek, getIHtmlColorSet(),
					getSkinParam().getThemeStyle());
		} else if (printScale == PrintScale.QUARTERLY) {
			return new TimeHeaderQuarterly(locale, getTimelineStyle(), getClosedStyle(), getFactorScale(),
					openClose.getCalendar(), min, max, openClose, colorDays(), colorDaysOfWeek, getIHtmlColorSet(),
					getSkinParam().getThemeStyle());
		} else if (printScale == PrintScale.YEARLY) {
			return new TimeHeaderYearly(locale, getTimelineStyle(), getClosedStyle(), getFactorScale(),
					openClose.getCalendar(), min, max, openClose, colorDays(), colorDaysOfWeek, getIHtmlColorSet(),
					getSkinParam().getThemeStyle());
		} else {
			throw new IllegalStateException();
		}
	}

	private Map<Day, HColor> colorDays() {
		colorDaysInternal.putAll(colorDaysToday);
		return Collections.unmodifiableMap(colorDaysInternal);
	}

	private Style getClosedStyle() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.closed)
				.getMergedStyle(getCurrentStyleBuilder());
	}

	private Style getTimelineStyle() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
				.getMergedStyle(getCurrentStyleBuilder());
	}

	private double getTotalHeight(TimeHeader timeHeader) {
		if (showFootbox) {
			return totalHeightWithoutFooter + timeHeader.getTimeFooterHeight();
		}
		return totalHeightWithoutFooter;
	}

	private void drawTasksRect(UGraphic ug) {
		for (Task task : tasks.values()) {
			if (isHidden(task)) {
				continue;
			}
			final TaskDraw draw = draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawU(ug.apply(move));
		}
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : constraints) {
			if (printStart != null && constraint.isHidden(min, max)) {
				continue;
			}
			constraint.getUDrawable(timeScale, this).drawU(ug);
		}

	}

	public StyleSignature getDefaultStyleDefinitionArrow() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.arrow);
	}

	private void drawTasksTitle(UGraphic ug, double colTitles, double colBars) {
		for (Task task : tasks.values()) {
			if (isHidden(task)) {
				continue;
			}
			final TaskDraw draw = draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawTitle(ug.apply(move), labelStrategy, colTitles, colBars);
		}
	}

	private void drawResources(UGraphic ug) {
		for (Resource res : resources.values()) {
			final ResourceDraw draw = res.getResourceDraw();
			final UTranslate move = UTranslate.dy(draw.getY());
			draw.drawU(ug.apply(move));
		}
	}

	public void closeDayOfWeek(DayOfWeek day) {
		openClose.close(day);
	}

	public void closeDayAsDate(Day day) {
		openClose.close(day);
	}

	public void openDayAsDate(Day day) {
		openClose.open(day);
	}

	private void initTaskAndResourceDraws(TimeScale timeScale, double headerHeight, StringBounder stringBounder) {
		Real y = origin.addFixed(headerHeight);
		for (Task task : tasks.values()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				draw = new TaskDrawSeparator(((TaskSeparator) task).getName(), timeScale, y, min, max,
						task.getStyleBuilder(), getSkinParam().getIHtmlColorSet());
			} else {
				final TaskImpl tmp = (TaskImpl) task;
				if (tmp.isDiamond()) {
					draw = new TaskDrawDiamond(timeScale, y, tmp.getPrettyDisplay(), getStart(tmp), getSkinParam(),
							task, this, task.getStyleBuilder(), getSkinParam().getIHtmlColorSet());
				} else {
					final boolean oddStart = printStart != null && min.compareTo(getStart(tmp)) == 0;
					final boolean oddEnd = printStart != null && max.compareTo(getEnd(tmp)) == 0;
					draw = new TaskDrawRegular(timeScale, y, tmp.getPrettyDisplay(), getStart(tmp), getEnd(tmp),
							oddStart, oddEnd, getSkinParam(), task, this, getConstraints(task), task.getStyleBuilder(),
							getSkinParam().getIHtmlColorSet());
				}
				draw.setColorsAndCompletion(tmp.getColors(), tmp.getCompletion(), tmp.getUrl(), tmp.getNote());
			}
			if (task.getRow() == null) {
				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));
			}
			draws.put(task, draw);
		}
		origin.compileNow();
		magicPush(stringBounder);
		double yy = lastY(stringBounder);
		if (yy == 0) {
			yy = headerHeight;
		} else {
			for (Resource res : resources.values()) {
				final ResourceDraw draw = new ResourceDraw(this, res, timeScale, yy, min, max);
				res.setTaskDraw(draw);
				yy += draw.getHeight();
			}
		}
		this.totalHeightWithoutFooter = yy;
	}

	private Collection<GanttConstraint> getConstraints(Task task) {
		final List<GanttConstraint> result = new ArrayList<>();
		for (GanttConstraint constraint : constraints) {
			if (constraint.isOn(task)) {
				result.add(constraint);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	private double lastY(StringBounder stringBounder) {
		double result = 0;
		for (TaskDraw td : draws.values()) {
			result = Math.max(result, td.getY(stringBounder).getCurrentValue() + td.getHeightMax(stringBounder));
		}
		return result;
	}

	private void magicPush(StringBounder stringBounder) {
		final List<TaskDraw> notes = new ArrayList<>();
		for (TaskDraw td : draws.values()) {
			final FingerPrint taskPrint = td.getFingerPrint(stringBounder);
			final FingerPrint fingerPrintNote = td.getFingerPrintNote(stringBounder);

			if (td.getTrueRow() == null)
				for (TaskDraw note : notes) {
					final FingerPrint otherNote = note.getFingerPrintNote(stringBounder);
					final double deltaY = otherNote.overlap(taskPrint);
					if (deltaY > 0) {
						final Real bottom = note.getY(stringBounder).addAtLeast(note.getHeightMax(stringBounder));
						td.getY(stringBounder).ensureBiggerThan(bottom);
						origin.compileNow();
					}

				}

			if (fingerPrintNote != null) {
				notes.add(td);
			}
		}
	}

	private Day getStart(final TaskImpl tmp) {
		if (printStart == null) {
			return tmp.getStart();
		}
		return Day.max(min, tmp.getStart());
	}

	private Day getEnd(final TaskImpl tmp) {
		if (printStart == null) {
			return tmp.getEnd();
		}
		return Day.min(max, tmp.getEnd());
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
				final Day start = task.getStart();
				final Day end = task.getEnd();
				// if (min.compareTo(start) > 0) {
				// min = start;
				// }
				if (max == null || max.compareTo(end) < 0) {
					max = end;
				}
			}
		}
		if (openClose.getCalendar() != null) {
			for (Day d : colorDays().keySet()) {
				if (d.compareTo(max) > 0) {
					max = d;
				}
			}
			for (Day d : nameDays.keySet()) {
				if (d.compareTo(max) > 0) {
					max = d;
				}
			}
		}
	}

	public Day getThenDate() {
		Day result = getStartingDate();
		for (Day d : colorDays().keySet()) {
			if (d.compareTo(result) > 0) {
				result = d;
			}
		}
		for (Day d : nameDays.keySet()) {
			if (d.compareTo(result) > 0) {
				result = d;
			}
		}
		return result;
	}

	public Task getExistingTask(String id) {
		final Task result = byShortName.get(Objects.requireNonNull(id));
		if (result != null) {
			return result;
		}
		final TaskCode code = new TaskCode(id);
		return tasks.get(code);
	}

	public GanttConstraint forceTaskOrder(Task task1, Task task2) {
		final TaskInstant end1 = new TaskInstant(task1, TaskAttribute.END);
		task2.setStart(end1.getInstantPrecise());
		final GanttConstraint result = new GanttConstraint(this.getIHtmlColorSet(),
				getSkinParam().getCurrentStyleBuilder(), end1, new TaskInstant(task2, TaskAttribute.START));
		addContraint(result);
		return result;
	}

	public Task getOrCreateTask(String codeOrShortName, String shortName, boolean linkedToPrevious) {
		Objects.requireNonNull(codeOrShortName);
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
			result = new TaskImpl(getSkinParam().getCurrentStyleBuilder(), code, openClose);
			tasks.put(code, result);
			if (byShortName != null) {
				byShortName.put(shortName, result);
			}
			if (previous != null) {
				forceTaskOrder(previous, result);
			}
		}
		return result;
	}

	private Task getLastCreatedTask() {
		final List<Task> all = new ArrayList<>(tasks.values());
		for (int i = all.size() - 1; i >= 0; i--) {
			if (all.get(i) instanceof TaskImpl) {
				return all.get(i);
			}
		}
		return null;
	}

	public void addSeparator(String comment) {
		TaskSeparator separator = new TaskSeparator(getSkinParam().getCurrentStyleBuilder(), comment, tasks.size());
		tasks.put(separator.getCode(), separator);
	}

	public void addContraint(GanttConstraint constraint) {
		constraints.add(constraint);
	}

	public HColorSet getIHtmlColorSet() {
		return colorSet;
	}

	public void setStartingDate(Day start) {
		openClose.setCalendar(start);
		this.min = start;
	}

	public Day getStartingDate() {
		if (openClose.getCalendar() == null) {
			return min;
		}
		return openClose.getCalendar();
	}

	public int daysInWeek() {
		return openClose.daysInWeek();
	}

	public boolean isOpen(Day day) {
		return openClose.getLoadAt(day) > 0;
	}

	public boolean affectResource(Task result, String description) {
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
		if (percentage == 0) {
			return false;
		}
		result.addResource(resource, percentage);
		return true;
	}

	public Resource getResource(String resourceName) {
		Resource resource = resources.get(resourceName);
		if (resource == null) {
			resource = new Resource(resourceName);
		}
		resources.put(resourceName, resource);
		return resource;
	}

	public int getLoadForResource(Resource res, Day i) {
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
			Day start = null;
			Day end = null;
			for (Map.Entry<Day, String> ent : nameDays.entrySet()) {
				if (ent.getValue().equalsIgnoreCase(id) == false) {
					continue;
				}
				start = min(start, ent.getKey());
				end = max(end, ent.getKey());
			}
			if (start != null) {
				result = new MomentImpl(start, end);
			}
		}
		return result;
	}

	private Day min(Day d1, Day d2) {
		if (d1 == null) {
			return d2;
		}
		if (d1.compareTo(d2) > 0) {
			return d2;
		}
		return d1;
	}

	private Day max(Day d1, Day d2) {
		if (d1 == null) {
			return d2;
		}
		if (d1.compareTo(d2) < 0) {
			return d2;
		}
		return d1;
	}

	public void colorDay(Day day, HColor color) {
		colorDaysInternal.put(day, color);
	}

	public void colorDay(DayOfWeek day, HColor color) {
		colorDaysOfWeek.put(day, color);
	}

	public void nameDay(Day day, String name) {
		nameDays.put(day, name);
	}

	public void setTodayColors(CenterBorderColor colors) {
		if (today == null) {
			this.today = Day.today();
		}
		colorDaysToday.put(today, colors.getCenter());
	}

	public CommandExecutionResult setToday(Day date) {
		this.today = date;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult deleteTask(Task task) {
		task.setColors(new CenterBorderColor(HColorUtils.WHITE, HColorUtils.BLACK));
		return CommandExecutionResult.ok();
	}

	public void setPrintInterval(Day start, Day end) {
		this.printStart = start;
		this.printEnd = end;
	}

	public TaskDraw getTaskDraw(Task task) {
		return draws.get(task);
	}

	public CommandExecutionResult addNote(Display note) {
		Task last = null;
		for (Task current : tasks.values())
			last = current;
		if (last == null) {
			return CommandExecutionResult.error("No task defined");
		}
		last.setNote(note);
		return CommandExecutionResult.ok();
	}

	public LoadPlanable getDefaultPlan() {
		return openClose;
	}

	private boolean showFootbox = true;

	public void setShowFootbox(boolean footbox) {
		this.showFootbox = footbox;

	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.none();
	}

	public void setLabelStrategy(LabelStrategy strategy) {
		this.labelStrategy = strategy;
	}

	private boolean withCalendarDate;

	public void setWithCalendarDate(boolean withCalendarDate) {
		this.withCalendarDate = withCalendarDate;

	}

}
