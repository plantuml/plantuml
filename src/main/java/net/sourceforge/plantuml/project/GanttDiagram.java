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
package net.sourceforge.plantuml.project;

import java.io.IOException;
import java.io.OutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.project.core.Moment;
import net.sourceforge.plantuml.project.core.MomentImpl;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.draw.FingerPrint;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.ResourceDrawNumbers;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderSimple;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.solver.ImpossibleSolvingException;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GanttDiagram extends TitledDiagram implements ToTaskDraw, WithSprite, GanttStyle {

	// ------------------------------------------------------------------------
	// model / prepared state
	// ------------------------------------------------------------------------
	final protected GanttPreparedModel model;

	// ------------------------------------------------------------------------
	// diagram configuration (styling / options)
	// ------------------------------------------------------------------------

	private TimePoint today;

	private int defaultCompletion = 100;

	// ------------------------------------------------------------------------
	// parsing / "current" pointers (stateful command interpretation)
	// ------------------------------------------------------------------------
	private Task it;
	private Resource they;
	private TaskGroup currentGroup = null;

	// ------------------------------------------------------------------------
	// per-task helpers / caches
	// ------------------------------------------------------------------------
	private final Map<String, OpenClose> openCloseForTask = new HashMap<>();

	// ------------------------------------------------------------------------
	// constants / patterns
	// ------------------------------------------------------------------------
	private static final Pattern RESOURCE_ASSIGNMENT_PATTERN = Pattern.compile("([^:]+)(:(\\d+))?");

	public CommandExecutionResult changeLanguage(String lang) {
		this.model.locale = new Locale(lang);
		return CommandExecutionResult.ok();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Gantt)");
	}

	public void setWeekNumberStrategy(DayOfWeek firstDayOfWeek, int minimalDaysInFirstWeek) {
		this.model.weekNumberStrategy = new WeekNumberStrategy(firstDayOfWeek, minimalDaysInFirstWeek);
	}

	public GanttDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.GANTT, null, preprocessing);
		this.model = new GanttPreparedModel(this, getSkinParam());
	}

	public final int getDpi(FileFormatOption fileFormatOption) {
		return 96;
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	public void setPrintScale(PrintScale printScale) {
		this.model.printScale = printScale;
	}

	public void setFactorScale(double factorScale) {
		model.factorScale = factorScale;
	}

	@Override
	public String checkFinalError() {
		try {
			initMinMax();
		} catch (ImpossibleSolvingException ex) {
			return ex.getMessage();
		}
		return null;
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		final StringBounder stringBounder = fileFormatOption.getDefaultStringBounder(getSkinParam());
		if (model.printStart == null) {
			initMinMax();
		} else {
			this.model.minDay = model.printStart;
			this.model.maxDay = model.printEnd;
		}
		final TimeHeader timeHeader = model.minDay.equals(TimePoint.epoch())
				? new TimeHeaderSimple(model, stringBounder)
				: model.buildTimeHeader();

		final GanttDiagramMainBlock result = new GanttDiagramMainBlock(this, timeHeader, stringBounder);
		return result;
	}

	private void initMinMax() {
		if (model.tasks.size() == 0) {
			model.maxDay = model.minDay;
		} else {
			model.maxDay = null;
			for (Task task : model.tasks.values()) {
				if (task instanceof TaskSeparator || task instanceof TaskGroup)
					continue;

				final TimePoint tmp = task.getEnd().minusOneSecond();
				if (model.maxDay == null || model.maxDay.compareTo(tmp.toDay()) < 0)
					model.maxDay = tmp.toDay();
			}
		}

		for (TimePoint d : model.colorDays().keySet())
			if (d.toDay().compareTo(model.maxDay) > 0)
				model.maxDay = d.toDay();

		for (TimePoint d : model.nameDays.keySet())
			if (d.toDay().compareTo(model.maxDay) > 0)
				model.maxDay = d.toDay();

		// maxTimePointPrintedEndOfDay =
		// maxTimePoint1.minusOneSecond().floorToDay().ofEndOfDay();
	}

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

	public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.arrow);
	}

	public void closeDayOfWeek(DayOfWeek day, String task) {
		model.openClose.close(day);
	}

	public void openDayOfWeek(DayOfWeek day, String task) {
		if (task.length() == 0)
			model.openClose.open(day);
		else
			getOpenCloseForTask(task).open(day);
	}

	public void closeDayAsDate(LocalDate day, String task) {
		if (task.length() == 0)
			model.openClose.close(day);
		else
			getOpenCloseForTask(task).close(day);

	}

	public void openDayAsDate(LocalDate day, String task) {
		if (task.length() == 0)
			model.openClose.open(day);
		else
			getOpenCloseForTask(task).open(day);

	}

	private OpenClose getOpenCloseForTask(String task) {
		OpenClose except = openCloseForTask.get(task);
		if (except == null) {
			except = new OpenClose();
			openCloseForTask.put(task, except);
		}
		return except;
	}

	public TimePoint getThenDate() {
		TimePoint result = TimePoint.ofStartOfDay(model.minDay);
		for (TimePoint d : model.colorDays().keySet())
			if (d.compareTo(result) > 0)
				result = d;

		for (TimePoint d : model.nameDays.keySet())
			if (d.compareTo(result) > 0)
				result = d;

		return result;
	}

	public Task getExistingTask(String id) {
		final TaskCode code = TaskCode.fromId(Objects.requireNonNull(id));
		return model.tasks.get(code);
	}

	public GanttConstraint forceTaskOrder(Task task1, Task task2) {
		final TaskInstant end1 = new TaskInstant(task1, TaskAttribute.END);
		task2.setStart(end1.getInstantPrecise());
		final GanttConstraint result = new GanttConstraint(this.getIHtmlColorSet(),
				getSkinParam().getCurrentStyleBuilder(), end1, new TaskInstant(task2, TaskAttribute.START));
		addContraint(result);
		return result;
	}

	public Task getOrCreateTask(TaskCode code, boolean linkedToPrevious) {
		Task result = model.tasks.get(Objects.requireNonNull(code));
		if (result == null) {
			Task previous = null;
			if (linkedToPrevious)
				previous = getLastCreatedTask();

			result = new TaskImpl(this, getSkinParam().getCurrentStyleBuilder(), code,
					TimePoint.ofStartOfDay(model.minDay), defaultCompletion);
			if (currentGroup != null)
				currentGroup.addTask(result);

			model.tasks.put(code, result);

			if (previous != null)
				forceTaskOrder(previous, result);

		}
		return result;
	}

	public PiecewiseConstant getLoadPlanableForTask(String taskId) {
		return model.openClose.mutateMe(this.openCloseForTask.get(taskId)).asPiecewiseConstant();
	}

	private Task getLastCreatedTask() {
		final List<Task> all = new ArrayList<>(model.tasks.values());
		for (int i = all.size() - 1; i >= 0; i--)
			if (all.get(i) instanceof TaskImpl)
				return all.get(i);

		return null;
	}

	public void addSeparator(String comment) {
		TaskSeparator separator = new TaskSeparator(getSkinParam().getCurrentStyleBuilder(), comment,
				model.tasks.size());
		model.tasks.put(separator.getCode(), separator);
	}

	public CommandExecutionResult addGroup(TaskCode code) {
		TaskGroup group = new TaskGroup(this.currentGroup, getSkinParam().getCurrentStyleBuilder(), code);

		if (this.currentGroup != null)
			this.currentGroup.addTask(group);

		this.currentGroup = group;
		model.tasks.put(group.getCode(), group);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult endGroup() {
		if (this.currentGroup == null)
			return CommandExecutionResult.error("No group to be closed");

		this.currentGroup = this.currentGroup.getParent();

		return CommandExecutionResult.ok();
	}

	public void addContraint(GanttConstraint constraint) {
		model.constraints.add(constraint);
	}

	public CommandExecutionResult updateStartingPoint(LocalDate start) {
		if (model.tasks.size() > 0)
			return CommandExecutionResult.error("Starting point must be set before task definition");

		this.model.minDay = start;
		return CommandExecutionResult.ok();
	}

//	public TimePoint getMinTimePoint() {
//		return minTimePoint;
//	}

	public LocalDate getMinDay() {
		return model.minDay;
	}

	public LocalDate getMaxDay() {
		initMinMax();
		return model.maxDay;
	}

	public TimePoint getMinTimePoint() {
		return TimePoint.ofStartOfDay(model.minDay);
	}

	public TimePoint getMaxTimePoint() {
		initMinMax();
		return TimePoint.ofStartOfDay(model.maxDay);
	}

	public int daysInWeek() {
		return model.openClose.daysInWeek();
	}

	public int daysInMonth() {
		return 30;
	}

	public boolean isOpen(LocalDate day) {
		return model.openClose.getLoadAtDUMMY(day) > 0;
	}

	public boolean affectResource(Task result, String description) {
		final Matcher m = RESOURCE_ASSIGNMENT_PATTERN.matcher(description);
		if (m.find() == false)
			throw new IllegalArgumentException();

		final Resource resource = getResource(m.group(1));
		int percentage = 100;
		if (m.group(3) != null)
			percentage = Integer.parseInt(m.group(3));

		if (percentage == 0)
			return false;

		result.addResource(resource, percentage);
		return true;
	}

	public Resource getResource(String resourceName) {
		Resource resource = model.resources.get(resourceName);
		if (resource == null)
			resource = new Resource(resourceName);

		model.resources.put(resourceName, resource);
		return resource;
	}

	public Moment getExistingMoment(String id) {
		Moment result = getExistingTask(id);
		if (result == null) {
			TimePoint start = null;
			TimePoint end = null;
			for (Map.Entry<TimePoint, String> ent : model.nameDays.entrySet()) {
				if (ent.getValue().equalsIgnoreCase(id) == false)
					continue;

				start = min(start, ent.getKey());
				end = max(end, ent.getKey());
			}
			if (start != null)
				result = new MomentImpl(start, end.increment());

		}
		return result;
	}

	private TimePoint min(TimePoint d1, TimePoint d2) {
		if (d1 == null)
			return d2;

		if (d1.compareTo(d2) > 0)
			return d2;

		return d1;
	}

	private TimePoint max(TimePoint d1, TimePoint d2) {
		if (d1 == null)
			return d2;

		if (d1.compareTo(d2) < 0)
			return d2;

		return d1;
	}

	public void colorDay(LocalDate day, HColor color) {
		model.colorDaysInternal.put(TimePoint.ofStartOfDay(day), color);
	}

	public void colorDay(DayOfWeek day, HColor color) {
		model.colorDaysOfWeek.put(day, color);
	}

	public void nameDay(LocalDate day, String name) {
		model.nameDays.put(TimePoint.ofStartOfDay(day), name);
	}

	public LocalDate getToday() {
		if (today == null)
			this.today = TimePoint.todayUtcAtMidnight();

		return today.toDay();
	}

	public void setTodayColors(CenterBorderColor colors) {
		if (today == null)
			this.today = TimePoint.todayUtcAtMidnight();

		model.colorDaysToday.put(today, colors.getCenter());
	}

	public CommandExecutionResult setToday(LocalDate date) {
		this.today = TimePoint.ofStartOfDay(date);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult deleteTask(Task task) {
		task.setColors(new CenterBorderColor(HColors.WHITE, HColors.BLACK));
		return CommandExecutionResult.ok();
	}

	public void setPrintInterval(LocalDate start, LocalDate end) {
		this.model.printStart = start;
		this.model.printEnd = end;
	}

	public CommandExecutionResult addNote(Display note, Stereotype stereotype) {
		Task last = null;
		for (Task current : model.tasks.values())
			last = current;
		if (last == null)
			return CommandExecutionResult.error("No task defined");

		last.setNote(note, stereotype);
		return CommandExecutionResult.ok();
	}

	public void setShowFootbox(boolean footbox) {
		this.model.showFootbox = footbox;

	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.none();
	}

	public void setLabelStrategy(LabelStrategy strategy) {
		this.model.labelStrategy = strategy;
	}

	public void setWeeklyHeaderStrategy(WeeklyHeaderStrategy weeklyHeaderStrategy, int weekStartingNumber) {
		this.model.weeklyHeaderStrategy = weeklyHeaderStrategy;
		this.model.weekStartingNumber = weekStartingNumber;
	}

	public CommandExecutionResult hideResourceName() {
		this.model.hideResourceName = true;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult hideResourceFootbox() {
		this.model.hideResourceFoobox = true;
		return CommandExecutionResult.ok();
	}

	public void addVerticalSeparatorBefore(LocalDate day) {
		model.verticalSeparatorBefore.add(TimePoint.ofStartOfDay(day));
	}

	public void setTaskDefaultCompletion(int defaultCompletion) {
		this.defaultCompletion = defaultCompletion;
	}

	public List<TaskDrawRegular> getAllTasksForResource(Resource res) {
		final List<TaskDrawRegular> result = new ArrayList<TaskDrawRegular>();
		for (Task task : model.tasks.values())
			if (task.isAssignedTo(res)) {
				final TaskDrawRegular draw = (TaskDrawRegular) model.draws.get(task);
				result.add(draw);
			}

		return Collections.unmodifiableList(result);
	}

	public void setIt(Task result) {
		this.it = result;
	}

	public Task getIt() {
		return it;
	}

	public final Resource getThey() {
		return they;
	}

	public final void setThey(Resource they) {
		this.they = they;
	}

	public void setHideClosed(boolean hideClosed) {
		model.hideClosed = hideClosed;
	}

	@Override
	public TaskDraw getTaskDraw(Task task) {
		return model.getTaskDraw(task);
	}

	@Override
	public PiecewiseConstant getDefaultPlan() {
		return model.getDefaultPlan();
	}

	@Override
	public HColorSet getIHtmlColorSet() {
		return model.getIHtmlColorSet();
	}

}
