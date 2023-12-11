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
package net.sourceforge.plantuml.chronology;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.project.GanttStyle;
import net.sourceforge.plantuml.project.LabelPosition;
import net.sourceforge.plantuml.project.LabelStrategy;
import net.sourceforge.plantuml.project.LoadPlanable;
import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TimeHeader;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class ChronologyDiagram extends TitledDiagram implements ToTaskDraw, WithSprite, GanttStyle {

	private final Map<Task, TaskDraw> draws = new LinkedHashMap<Task, TaskDraw>();
	private final Map<TaskCode, Task> tasks = new LinkedHashMap<TaskCode, Task>();
	private final Map<String, Task> byShortName = new HashMap<String, Task>();
//	private final List<GanttConstraint> constraints = new ArrayList<>();
	private final HColorSet colorSet = HColorSet.instance();
//
//	private final OpenClose openClose = new OpenClose();
//
//	private final Map<String, Resource> resources = new LinkedHashMap<String, Resource>();
//	private final Map<Day, HColor> colorDaysToday = new HashMap<Day, HColor>();
//	private final Map<Day, HColor> colorDaysInternal = new HashMap<Day, HColor>();
//	private final Map<DayOfWeek, HColor> colorDaysOfWeek = new HashMap<DayOfWeek, HColor>();
//	private final Map<Day, String> nameDays = new HashMap<Day, String>();
	private LabelStrategy labelStrategy = new LabelStrategy(LabelPosition.LEGACY, HorizontalAlignment.LEFT);
//
//	// Let's follow ISO-8601 rules
//	private WeekNumberStrategy weekNumberStrategy = new WeekNumberStrategy(DayOfWeek.MONDAY, 4);
//
//	private PrintScale printScale = PrintScale.DAILY;
//	private double factorScale = 1.0;
	private Locale locale = Locale.ENGLISH;
//
//	private Day today;
//	private double totalHeightWithoutFooter;
	private Day min;
	private Day max;
	private TimeScaleChronology timeScale;
//
//	private Day printStart;
//	private Day printEnd;
//
	private final RealOrigin origin = RealUtils.createOrigin();
//
//	private int defaultCompletion = 100;
//
//	private Task it;
//	private Resource they;

	public CommandExecutionResult changeLanguage(String lang) {
		this.locale = new Locale(lang);
		return CommandExecutionResult.ok();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Chronology)");
	}

//	public void setWeekNumberStrategy(DayOfWeek firstDayOfWeek, int minimalDaysInFirstWeek) {
//		this.weekNumberStrategy = new WeekNumberStrategy(firstDayOfWeek, minimalDaysInFirstWeek);
//	}

	public ChronologyDiagram(UmlSource source) {
		super(source, UmlDiagramType.CHRONOLOGY, null);
	}

//	public final int getDpi(FileFormatOption fileFormatOption) {
//		return 96;
//	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

//	public void setPrintScale(PrintScale printScale) {
//		this.printScale = printScale;
//	}
//
//	public void setFactorScale(double factorScale) {
//		this.factorScale = factorScale;
//	}
//
//	private double getFactorScale() {
//		return this.printScale.getDefaultScale() * this.factorScale;
//	}
//
//	private boolean isHidden(Task task) {
//		if (printStart == null || task instanceof TaskSeparator)
//			return false;
//
//		if (task.getEnd().compareTo(min) < 0)
//			return true;
//
//		if (task.getStart().compareTo(max) > 0)
//			return true;
//
//		return false;
//	}
//
//	@Override
//	public String checkFinalError() {
//		try {
//			initMinMax();
//		} catch (ImpossibleSolvingException ex) {
//			return ex.getMessage();
//		}
//		return null;
//	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
		initMinMax();

		final TimeHeader timeHeader = new TimeHeaderChronology(stringBounder, thParam(), PrintScale.DAILY,
				this.timeScale);
		initTaskAndResourceDraws(timeHeader.getTimeScale(), timeHeader.getFullHeaderHeight(stringBounder),
				stringBounder);

		return new AbstractTextBlock() {
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(1000, 1000);
			}

			@Override
			public void drawU(UGraphic ug) {
				timeHeader.drawTimeHeader(ug, 200);
				drawTasksRect(ug);
				drawTasksTitle(ug, 0, 0);
			}
		};

	}

//
//			private double getTitlesColumnWidth(StringBounder stringBounder) {
//				if (labelStrategy.titleInside())
//					return 0;
//
//				double width = 0;
//				for (Task task : tasks.values()) {
//					if (isHidden(task))
//						continue;
//
//					width = Math.max(width, draws.get(task).getTitleWidth(stringBounder));
//				}
//				return width;
//			}
//
//			public XDimension2D calculateDimension(StringBounder stringBounder) {
//				return new XDimension2D(getTitlesColumnWidth(stringBounder) + getBarsColumnWidth(timeHeader),
//						getTotalHeight(stringBounder, timeHeader));
//			}
//
//			private double getBarsColumnWidth(final TimeHeader timeHeader) {
//				final double xmin = timeHeader.getTimeScale().getStartingPosition(min);
//				final double xmax = timeHeader.getTimeScale().getEndingPosition(max);
//				return xmax - xmin;
//			}
//
//		};
//	}
//
//	private TimeHeader getTimeHeader(StringBounder stringBounder) {
//		if (openClose.getStartingDay() == null)
//			return new TimeHeaderSimple(stringBounder, thParam(), printScale);
//		else if (printScale == PrintScale.DAILY)
//			return new TimeHeaderDaily(stringBounder, thParam(), nameDays, printStart, printEnd);
//		else if (printScale == PrintScale.WEEKLY)
//			return new TimeHeaderWeekly(stringBounder, thParam(), weekNumberStrategy, withCalendarDate);
//		else if (printScale == PrintScale.MONTHLY)
//			return new TimeHeaderMonthly(stringBounder, thParam());
//		else if (printScale == PrintScale.QUARTERLY)
//			return new TimeHeaderQuarterly(stringBounder, thParam());
//		else if (printScale == PrintScale.YEARLY)
//			return new TimeHeaderYearly(stringBounder, thParam());
//		else
//			throw new IllegalStateException();
//
//	}

	private TimeHeaderParameters thParam() {
		return new TimeHeaderParameters(null, 1, min, max, getIHtmlColorSet(), locale, null, null, null, this);
	}

//	private Map<Day, HColor> colorDays() {
//		colorDaysInternal.putAll(colorDaysToday);
//		return Collections.unmodifiableMap(colorDaysInternal);
//	}

	@Override
	public final Style getStyle(SName param) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, param)
				.getMergedStyle(getCurrentStyleBuilder());
	}

	@Override
	public final Style getStyle(SName param1, SName param2) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, param1, param2)
				.getMergedStyle(getCurrentStyleBuilder());
	}

//	private double getTotalHeight(StringBounder stringBounder, TimeHeader timeHeader) {
//		if (showFootbox)
//			return totalHeightWithoutFooter + timeHeader.getTimeFooterHeight(stringBounder);
//
//		return totalHeightWithoutFooter;
//	}

	private void drawTasksRect(UGraphic ug) {
		for (Task task : tasks.values()) {
//			if (isHidden(task))
//				continue;

			final TaskDraw draw = draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawU(ug.apply(move));
		}
	}

//	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
//		for (GanttConstraint constraint : constraints) {
//			if (printStart != null && constraint.isHidden(min, max))
//				continue;
//
//			constraint.getUDrawable(timeScale, this).drawU(ug);
//		}
//
//	}
//
//	public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
//		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.arrow);
//	}

	private void drawTasksTitle(UGraphic ug, double colTitles, double colBars) {
		for (Task task : tasks.values()) {
//			if (isHidden(task))
//				continue;

			final TaskDraw draw = draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawTitle(ug.apply(move), labelStrategy, colTitles, colBars);
		}
	}

//	private void drawResources(UGraphic ug) {
//		for (Resource res : resources.values()) {
//			final ResourceDraw draw = res.getResourceDraw();
//			final UTranslate move = UTranslate.dy(draw.getY());
//			draw.drawU(ug.apply(move));
//		}
//	}
//
//	public void closeDayOfWeek(DayOfWeek day, String task) {
//		openClose.close(day);
//	}
//
//	public void openDayOfWeek(DayOfWeek day, String task) {
//		if (task.length() == 0)
//			openClose.open(day);
//		else
//			getOpenCloseForTask(task).open(day);
//	}
//
//	public void closeDayAsDate(Day day, String task) {
//		if (task.length() == 0)
//			openClose.close(day);
//		else
//			getOpenCloseForTask(task).close(day);
//
//	}
//
//	public void openDayAsDate(Day day, String task) {
//		if (task.length() == 0)
//			openClose.open(day);
//		else
//			getOpenCloseForTask(task).open(day);
//
//	}
//
//	private OpenClose getOpenCloseForTask(String task) {
//		OpenClose except = openCloseForTask.get(task);
//		if (except == null) {
//			except = new OpenClose();
//			openCloseForTask.put(task, except);
//		}
//		return except;
//	}
//
//	private final Map<String, OpenClose> openCloseForTask = new HashMap<>();
//
	private void initTaskAndResourceDraws(TimeScale timeScale, double headerHeight, StringBounder stringBounder) {
		Real y = origin;
		y = y.addFixed(headerHeight);
		for (Task task : tasks.values()) {
			final TaskDraw draw;
			final String disp = task.getCode().getSimpleDisplay();
			draw = new TaskDrawDiamond(timeScale, y, disp, task.getStart(), task, this, task.getStyleBuilder());
			final double height = draw.getFullHeightTask(stringBounder);
			y = y.addAtLeast(height);
//			if (task instanceof TaskSeparator) {
//				final TaskSeparator taskSeparator = (TaskSeparator) task;
//				draw = new TaskDrawSeparator(taskSeparator.getName(), timeScale, y, min, max, task.getStyleBuilder(),
//						getSkinParam().getIHtmlColorSet());
//			} else if (task instanceof TaskGroup) {
//				final TaskGroup taskGroup = (TaskGroup) task;
//				draw = new TaskDrawGroup(timeScale, y, taskGroup.getCode().getSimpleDisplay(), getStart(taskGroup),
//						getEnd(taskGroup), task, this, task.getStyleBuilder());
//			} else {
//				final TaskImpl tmp = (TaskImpl) task;
//				final String disp = hideResourceName ? tmp.getCode().getSimpleDisplay() : tmp.getPrettyDisplay();
//				if (tmp.isDiamond()) {
//					draw = new TaskDrawDiamond(timeScale, y, disp, getStart(tmp), task, this, task.getStyleBuilder());
//				} else {
//					final boolean oddStart = printStart != null && min.compareTo(getStart(tmp)) == 0;
//					final boolean oddEnd = printStart != null && max.compareTo(getEnd(tmp)) == 0;
//					draw = new TaskDrawRegular(timeScale, y, disp, getStart(tmp), getEnd(tmp), oddStart, oddEnd,
//							getSkinParam(), task, this, getConstraints(task), task.getStyleBuilder());
//				}
//				draw.setColorsAndCompletion(tmp.getColors(), tmp.getCompletion(), tmp.getUrl(), tmp.getNote());
//			}
//			if (task.getRow() == null)
//				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));
//
			draws.put(task, draw);
		}
//		origin.compileNow();
//		magicPush(stringBounder);
//		double yy = lastY(stringBounder);
//		if (yy == 0) {
//			yy = headerHeight;
//		} else if (this.hideResourceFoobox == false)
//			for (Resource res : resources.values()) {
//				final ResourceDraw draw = buildResourceDraw(this, res, timeScale, yy, min, max);
//				res.setTaskDraw(draw);
//				yy += draw.getHeight(stringBounder);
//			}
//
//		this.totalHeightWithoutFooter = yy;
	}

//
//	private ResourceDraw buildResourceDraw(ChronologyDiagram gantt, Resource res, TimeScale timeScale, double y, Day min,
//			Day max) {
//		return new ResourceDrawBasic(gantt, res, timeScale, y, min, max);
//		// return new ResourceDrawVersion2(gantt, res, timeScale, y, min, max);
//	}
//
//	private Collection<GanttConstraint> getConstraints(Task task) {
//		final List<GanttConstraint> result = new ArrayList<>();
//		for (GanttConstraint constraint : constraints) {
//			if (constraint.isOn(task))
//				result.add(constraint);
//
//		}
//		return Collections.unmodifiableCollection(result);
//	}
//
//	private double lastY(StringBounder stringBounder) {
//		double result = 0;
//		for (TaskDraw td : draws.values())
//			result = Math.max(result, td.getY(stringBounder).getCurrentValue() + td.getHeightMax(stringBounder));
//
//		return result;
//	}
//
//	private void magicPush(StringBounder stringBounder) {
//		final List<TaskDraw> notes = new ArrayList<>();
//		for (TaskDraw td : draws.values()) {
//			final FingerPrint taskPrint = td.getFingerPrint(stringBounder);
//			final FingerPrint fingerPrintNote = td.getFingerPrintNote(stringBounder);
//
//			if (td.getTrueRow() == null)
//				for (TaskDraw note : notes) {
//					final FingerPrint otherNote = note.getFingerPrintNote(stringBounder);
//					final double deltaY = otherNote.overlap(taskPrint);
//					if (deltaY > 0) {
//						final Real bottom = note.getY(stringBounder).addAtLeast(note.getHeightMax(stringBounder));
//						td.getY(stringBounder).ensureBiggerThan(bottom);
//						origin.compileNow();
//					}
//
//				}
//
//			if (fingerPrintNote != null)
//				notes.add(td);
//
//		}
//	}
//
//	private Day getStart(final Task tmp) {
//		if (printStart == null)
//			return tmp.getStart();
//
//		return Day.max(min, tmp.getStart());
//	}
//
//	private Day getEnd(final Task tmp) {
//		if (printStart == null)
//			return tmp.getEnd();
//
//		return Day.min(max, tmp.getEnd());
//	}
//
	private void initMinMax() {
		if (tasks.size() == 0) {
			throw new IllegalStateException();
		}
		for (Task task : tasks.values()) {
			if (this.min == null || this.max == null) {
				this.min = task.getStart();
				this.max = task.getEnd();
				continue;

			}
			if (this.min.compareTo(task.getStart()) > 0)
				this.min = task.getStart();
			if (this.max.compareTo(task.getEnd()) < 0)
				this.max = task.getEnd();
		}

		this.min = this.min.roundDayDown();
		this.max = this.max.roundDayUp();

		this.timeScale = new TimeScaleChronology(1000);
		this.timeScale.setMin(this.min.getMillis());
		this.timeScale.setMax(this.max.getMillis());
	}

//	public Day getThenDate() {
//		Day result = getStartingDate();
//		for (Day d : colorDays().keySet())
//			if (d.compareTo(result) > 0)
//				result = d;
//
//		for (Day d : nameDays.keySet())
//			if (d.compareTo(result) > 0)
//				result = d;
//
//		return result;
//	}
//
//	public Task getExistingTask(String id) {
//		final Task result = byShortName.get(Objects.requireNonNull(id));
//		if (result != null)
//			return result;
//
//		final TaskCode code = new TaskCode(id);
//		return tasks.get(code);
//	}
//
//	public GanttConstraint forceTaskOrder(Task task1, Task task2) {
//		final TaskInstant end1 = new TaskInstant(task1, TaskAttribute.END);
//		task2.setStart(end1.getInstantPrecise());
//		final GanttConstraint result = new GanttConstraint(this.getIHtmlColorSet(),
//				getSkinParam().getCurrentStyleBuilder(), end1, new TaskInstant(task2, TaskAttribute.START));
//		addContraint(result);
//		return result;
//	}

	public Task getOrCreateTask(String codeOrShortName, String shortName, boolean linkedToPrevious) {
		Objects.requireNonNull(codeOrShortName);
		Task result = shortName == null ? null : byShortName.get(shortName);
		if (result != null)
			return result;

		result = byShortName.get(codeOrShortName);
		if (result != null)
			return result;

		final TaskCode code = new TaskCode(codeOrShortName);
		result = tasks.get(code);
		if (result == null) {

			result = new TaskChronology(getSkinParam().getCurrentStyleBuilder(), code);
			if (currentGroup != null)
				currentGroup.addTask(result);

			tasks.put(code, result);
			if (byShortName != null)
				byShortName.put(shortName, result);

		}
		return result;
	}

//	private Task getLastCreatedTask() {
//		final List<Task> all = new ArrayList<>(tasks.values());
//		for (int i = all.size() - 1; i >= 0; i--)
//			if (all.get(i) instanceof TaskImpl)
//				return all.get(i);
//
//		return null;
//	}
//
//	public void addSeparator(String comment) {
//		TaskSeparator separator = new TaskSeparator(getSkinParam().getCurrentStyleBuilder(), comment, tasks.size());
//		tasks.put(separator.getCode(), separator);
//	}
//
	private TaskGroup currentGroup = null;
//
//	public CommandExecutionResult addGroup(String name) {
//		TaskGroup group = new TaskGroup(this.currentGroup, getSkinParam().getCurrentStyleBuilder(), name);
//
//		if (this.currentGroup != null)
//			this.currentGroup.addTask(group);
//
//		this.currentGroup = group;
//		tasks.put(group.getCode(), group);
//		return CommandExecutionResult.ok();
//	}
//
//	public CommandExecutionResult endGroup() {
//		if (this.currentGroup == null)
//			return CommandExecutionResult.error("No group to be closed");
//
//		this.currentGroup = this.currentGroup.getParent();
//
//		return CommandExecutionResult.ok();
//	}
//
//	public void addContraint(GanttConstraint constraint) {
//		constraints.add(constraint);
//	}
//
//	public HColorSet getIHtmlColorSet() {
//		return colorSet;
//	}
//
//	public void setProjectStartingDate(Day start) {
//		openClose.setStartingDay(start);
//		this.min = start;
//	}
//
//	public Day getStartingDate() {
//		if (openClose.getStartingDay() == null)
//			return min;
//
//		return openClose.getStartingDay();
//	}
//
//	public Day getEndingDate() {
//		initMinMax();
//		return max;
//	}
//
//	public int daysInWeek() {
//		return openClose.daysInWeek();
//	}
//
//	public boolean isOpen(Day day) {
//		return openClose.getLoadAt(day) > 0;
//	}
//
//	public boolean affectResource(Task result, String description) {
//		final Pattern p = Pattern.compile("([^:]+)(:(\\d+))?");
//		final Matcher m = p.matcher(description);
//		if (m.find() == false)
//			throw new IllegalArgumentException();
//
//		final Resource resource = getResource(m.group(1));
//		int percentage = 100;
//		if (m.group(3) != null)
//			percentage = Integer.parseInt(m.group(3));
//
//		if (percentage == 0)
//			return false;
//
//		result.addResource(resource, percentage);
//		return true;
//	}
//
//	public Resource getResource(String resourceName) {
//		Resource resource = resources.get(resourceName);
//		if (resource == null)
//			resource = new Resource(resourceName);
//
//		resources.put(resourceName, resource);
//		return resource;
//	}
//
//	public int getLoadForResource(Resource res, Day i) {
//		int result = 0;
//		for (Task task : tasks.values()) {
//			if (task instanceof TaskSeparator)
//				continue;
//
//			final TaskImpl task2 = (TaskImpl) task;
//			result += task2.loadForResource(res, i);
//		}
//		return result;
//	}
//
//	public Moment getExistingMoment(String id) {
//		Moment result = getExistingTask(id);
//		if (result == null) {
//			Day start = null;
//			Day end = null;
//			for (Map.Entry<Day, String> ent : nameDays.entrySet()) {
//				if (ent.getValue().equalsIgnoreCase(id) == false)
//					continue;
//
//				start = min(start, ent.getKey());
//				end = max(end, ent.getKey());
//			}
//			if (start != null)
//				result = new MomentImpl(start, end);
//
//		}
//		return result;
//	}
//
//	private Day min(Day d1, Day d2) {
//		if (d1 == null)
//			return d2;
//
//		if (d1.compareTo(d2) > 0)
//			return d2;
//
//		return d1;
//	}
//
//	private Day max(Day d1, Day d2) {
//		if (d1 == null)
//			return d2;
//
//		if (d1.compareTo(d2) < 0)
//			return d2;
//
//		return d1;
//	}
//
//	public void colorDay(Day day, HColor color) {
//		colorDaysInternal.put(day, color);
//	}
//
//	public void colorDay(DayOfWeek day, HColor color) {
//		colorDaysOfWeek.put(day, color);
//	}
//
//	public void nameDay(Day day, String name) {
//		nameDays.put(day, name);
//	}
//
//	public Day getToday() {
//		if (today == null)
//			this.today = Day.today();
//
//		return today;
//	}
//
//	public void setTodayColors(CenterBorderColor colors) {
//		if (today == null)
//			this.today = Day.today();
//
//		colorDaysToday.put(today, colors.getCenter());
//	}
//
//	public CommandExecutionResult setToday(Day date) {
//		this.today = date;
//		return CommandExecutionResult.ok();
//	}
//
//	public CommandExecutionResult deleteTask(Task task) {
//		task.setColors(new CenterBorderColor(HColors.WHITE, HColors.BLACK));
//		return CommandExecutionResult.ok();
//	}
//
//	public void setPrintInterval(Day start, Day end) {
//		this.printStart = start;
//		this.printEnd = end;
//	}
//
//	public TaskDraw getTaskDraw(Task task) {
//		return draws.get(task);
//	}
//
//	public CommandExecutionResult addNote(Display note) {
//		Task last = null;
//		for (Task current : tasks.values())
//			last = current;
//		if (last == null)
//			return CommandExecutionResult.error("No task defined");
//
//		last.setNote(note);
//		return CommandExecutionResult.ok();
//	}

	public LoadPlanable getDefaultPlan() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TaskDraw getTaskDraw(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HColorSet getIHtmlColorSet() {
		return colorSet;
	}

//	private boolean showFootbox = true;
//
//	public void setShowFootbox(boolean footbox) {
//		this.showFootbox = footbox;
//
//	}
//
//	@Override
//	public ClockwiseTopRightBottomLeft getDefaultMargins() {
//		return ClockwiseTopRightBottomLeft.none();
//	}
//
//	public void setLabelStrategy(LabelStrategy strategy) {
//		this.labelStrategy = strategy;
//	}
//
//	private boolean withCalendarDate;
//
//	public void setWithCalendarDate(boolean withCalendarDate) {
//		this.withCalendarDate = withCalendarDate;
//	}
//
//	private boolean hideResourceName;
//	private boolean hideResourceFoobox;
//
//	public CommandExecutionResult hideResourceName() {
//		this.hideResourceName = true;
//		return CommandExecutionResult.ok();
//	}
//
//	public CommandExecutionResult hideResourceFootbox() {
//		this.hideResourceFoobox = true;
//		return CommandExecutionResult.ok();
//	}
//
//	private final Set<Day> verticalSeparatorBefore = new HashSet<>();
//
//	public void addVerticalSeparatorBefore(Day day) {
//		verticalSeparatorBefore.add(day);
//	}
//
//	public void setTaskDefaultCompletion(int defaultCompletion) {
//		this.defaultCompletion = defaultCompletion;
//	}
//
//	public List<TaskDrawRegular> getAllTasksForResource(Resource res) {
//		final List<TaskDrawRegular> result = new ArrayList<TaskDrawRegular>();
//		for (Task task : tasks.values())
//			if (task.isAssignedTo(res)) {
//				final TaskDrawRegular draw = (TaskDrawRegular) draws.get(task);
//				result.add(draw);
//			}
//
//		return Collections.unmodifiableList(result);
//	}
//
//	public void setIt(Task result) {
//		this.it = result;
//	}
//
//	public Task getIt() {
//		return it;
//	}
//
//	public final Resource getThey() {
//		return they;
//	}
//
//	public final void setThey(Resource they) {
//		this.they = they;
//	}

}
