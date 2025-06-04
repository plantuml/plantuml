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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.WithSprite;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.crash.CrashImage;
import net.sourceforge.plantuml.crash.ReportLog;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.log.Logme;
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
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TaskDrawGroup;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.TimeHeader;
import net.sourceforge.plantuml.project.draw.TimeHeaderDaily;
import net.sourceforge.plantuml.project.draw.TimeHeaderMonthly;
import net.sourceforge.plantuml.project.draw.TimeHeaderQuarterly;
import net.sourceforge.plantuml.project.draw.TimeHeaderSimple;
import net.sourceforge.plantuml.project.draw.TimeHeaderWeekly;
import net.sourceforge.plantuml.project.draw.TimeHeaderYearly;
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.solver.ImpossibleSolvingException;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class GanttDiagram extends TitledDiagram implements ToTaskDraw, WithSprite, GanttStyle {

	private final Map<Task, TaskDraw> draws = new LinkedHashMap<Task, TaskDraw>();
	private final Map<TaskCode, Task> tasks = new LinkedHashMap<TaskCode, Task>();

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

	private int defaultCompletion = 100;

	private Task it;
	private Resource they;

	public CommandExecutionResult changeLanguage(String lang) {
		this.locale = new Locale(lang);
		return CommandExecutionResult.ok();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Gantt)");
	}

	public void setWeekNumberStrategy(DayOfWeek firstDayOfWeek, int minimalDaysInFirstWeek) {
		this.weekNumberStrategy = new WeekNumberStrategy(firstDayOfWeek, minimalDaysInFirstWeek);
	}

	public GanttDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.GANTT, null, preprocessing);
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
		this.printScale = printScale;
	}

	public void setFactorScale(double factorScale) {
		this.factorScale = factorScale;
	}

	private double getFactorScale() {
		return this.printScale.getDefaultScale() * this.factorScale;
	}

	private boolean isHidden(Task task) {
		if (printStart == null || task instanceof TaskSeparator)
			return false;

		if (task.getEnd().compareTo(min) < 0)
			return true;

		if (task.getStart().compareTo(max) > 0)
			return true;

		return false;
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
		if (printStart == null) {
			initMinMax();
		} else {
			this.min = printStart;
			this.max = printEnd;
		}
		final TimeHeader timeHeader = getTimeHeader(stringBounder);
		initTaskAndResourceDraws(timeHeader.getTimeScale(), timeHeader.getFullHeaderHeight(stringBounder),
				stringBounder);
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				try {
					final UGraphic ugOrig = ug;
					if (labelStrategy.titleInFirstColumn())
						ug = ug.apply(UTranslate.dx(getTitlesColumnWidth(ug.getStringBounder())));

					final Style timelineStyle = StyleSignatureBasic
							.of(SName.root, SName.element, SName.ganttDiagram, SName.timeline)
							.getMergedStyle(getCurrentStyleBuilder());

					final HColor back = timelineStyle.value(PName.BackGroundColor).asColor(getIHtmlColorSet());
					if (back.isTransparent() == false) {
						final URectangle rect1 = URectangle.build(calculateDimension(ug.getStringBounder()).getWidth(),
								timeHeader.getTimeHeaderHeight(ug.getStringBounder()));
						ug.apply(back.bg()).draw(rect1);
						if (showFootbox) {
							final URectangle rect2 = URectangle.build(
									calculateDimension(ug.getStringBounder()).getWidth(),
									timeHeader.getTimeFooterHeight(ug.getStringBounder()));
							ug.apply(back.bg()).apply(UTranslate.dy(totalHeightWithoutFooter)).draw(rect2);
						}
					}

					timeHeader.drawTimeHeader(ug, totalHeightWithoutFooter);

					drawConstraints(ug, timeHeader.getTimeScale());
					drawTasksRect(ug);
					drawTasksTitle(ugOrig, getTitlesColumnWidth(ug.getStringBounder()), getBarsColumnWidth(timeHeader));

					if (hideResourceFoobox == false)
						drawResources(ug);

					if (showFootbox)
						timeHeader.drawTimeFooter(ug.apply(UTranslate.dy(totalHeightWithoutFooter)));

				} catch (Throwable e) {
					Logme.error(e);

					final ReportLog report = new ReportLog();
					report.anErrorHasOccured(e, getFlashData());

					report.addProperties();
					report.addEmptyLine();
					report.youShouldSendThisDiagram();

					final CrashImage image = new CrashImage(e, getFlashData(), report);
					image.drawU(ug);

				}
			}

			private double getTitlesColumnWidth(StringBounder stringBounder) {
				if (labelStrategy.titleInside())
					return 0;

				double width = 0;
				for (Task task : tasks.values()) {
					if (isHidden(task))
						continue;

					width = Math.max(width, draws.get(task).getTitleWidth(stringBounder));
				}
				return width;
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(getTitlesColumnWidth(stringBounder) + getBarsColumnWidth(timeHeader),
						getTotalHeight(stringBounder, timeHeader));
			}

			private double getBarsColumnWidth(final TimeHeader timeHeader) {
				final double xmin = timeHeader.getTimeScale().getStartingPosition(min);
				final double xmax = timeHeader.getTimeScale().getEndingPosition(max);
				return xmax - xmin;
			}

		};
	}

	private TimeHeader getTimeHeader(StringBounder stringBounder) {
		if (openClose.getStartingDay() == null)
			return new TimeHeaderSimple(stringBounder, thParam(), printScale);
		else if (printScale == PrintScale.DAILY)
			return new TimeHeaderDaily(stringBounder, thParam(), nameDays, printStart);
		else if (printScale == PrintScale.WEEKLY)
			return new TimeHeaderWeekly(stringBounder, thParam(), weekNumberStrategy, weeklyHeaderStrategy, nameDays,
					printStart, weekStartingNumber);
		else if (printScale == PrintScale.MONTHLY)
			return new TimeHeaderMonthly(stringBounder, thParam(), nameDays, printStart);
		else if (printScale == PrintScale.QUARTERLY)
			return new TimeHeaderQuarterly(stringBounder, thParam(), printStart);
		else if (printScale == PrintScale.YEARLY)
			return new TimeHeaderYearly(stringBounder, thParam(), printStart);
		else
			throw new IllegalStateException();

	}

	private TimeHeaderParameters thParam() {
		return new TimeHeaderParameters(colorDays(), getFactorScale(), min, max, getIHtmlColorSet(), locale, openClose,
				colorDaysOfWeek, verticalSeparatorBefore, this, hideClosed);
	}

	private Map<Day, HColor> colorDays() {
		colorDaysInternal.putAll(colorDaysToday);
		return Collections.unmodifiableMap(colorDaysInternal);
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

	private double getTotalHeight(StringBounder stringBounder, TimeHeader timeHeader) {
		if (showFootbox)
			return totalHeightWithoutFooter + timeHeader.getTimeFooterHeight(stringBounder);

		return totalHeightWithoutFooter;
	}

	private void drawTasksRect(UGraphic ug) {
		for (Task task : tasks.values()) {
			if (isHidden(task))
				continue;

			final TaskDraw draw = draws.get(task);
			final UTranslate move = UTranslate.dy(draw.getY(ug.getStringBounder()).getCurrentValue());
			draw.drawU(ug.apply(move));
		}
	}

	private void drawConstraints(final UGraphic ug, TimeScale timeScale) {
		for (GanttConstraint constraint : constraints) {
			if (printStart != null && constraint.isHidden(min, max))
				continue;

			constraint.getUDrawable(timeScale, this).drawU(ug);
		}

	}

	public StyleSignatureBasic getDefaultStyleDefinitionArrow() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.ganttDiagram, SName.arrow);
	}

	private void drawTasksTitle(UGraphic ug, double colTitles, double colBars) {
		for (Task task : tasks.values()) {
			if (isHidden(task))
				continue;

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

	public void closeDayOfWeek(DayOfWeek day, String task) {
		openClose.close(day);
	}

	public void openDayOfWeek(DayOfWeek day, String task) {
		if (task.length() == 0)
			openClose.open(day);
		else
			getOpenCloseForTask(task).open(day);
	}

	public void closeDayAsDate(Day day, String task) {
		if (task.length() == 0)
			openClose.close(day);
		else
			getOpenCloseForTask(task).close(day);

	}

	public void openDayAsDate(Day day, String task) {
		if (task.length() == 0)
			openClose.open(day);
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

	private final Map<String, OpenClose> openCloseForTask = new HashMap<>();

	private void initTaskAndResourceDraws(TimeScale timeScale, double headerHeight, StringBounder stringBounder) {
		Real y = origin.addFixed(headerHeight);
		for (Task task : tasks.values()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				final TaskSeparator taskSeparator = (TaskSeparator) task;
				draw = new TaskDrawSeparator(taskSeparator.getName(), timeScale, y, min, max, task.getStyleBuilder(),
						getSkinParam());
			} else if (task instanceof TaskGroup) {
				final TaskGroup taskGroup = (TaskGroup) task;
				draw = new TaskDrawGroup(timeScale, y, taskGroup.getCode().getDisplay(), getStart(taskGroup),
						getEnd(taskGroup), task, this, task.getStyleBuilder(), getSkinParam());
			} else {
				final TaskImpl tmp = (TaskImpl) task;
				final String disp = hideResourceName ? tmp.getCode().getDisplay() : tmp.getPrettyDisplay();
				if (tmp.isDiamond()) {
					draw = new TaskDrawDiamond(timeScale, y, disp, getStart(tmp), task, this, task.getStyleBuilder(),
							getSkinParam());
				} else {
					final boolean oddStart = printStart != null && min.compareTo(getStart(tmp)) == 0;
					final boolean oddEnd = printStart != null && max.compareTo(getEnd(tmp)) == 0;
					draw = new TaskDrawRegular(timeScale, y, disp, getStart(tmp), getEnd(tmp), oddStart, oddEnd,
							getSkinParam(), task, this, getConstraints(task), task.getStyleBuilder());
				}
				draw.setColorsAndCompletion(tmp.getColors(), tmp.getCompletion(), tmp.getUrl(), tmp.getNote(),
						tmp.getNoteStereotype());
			}
			if (task.getRow() == null)
				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));

			draws.put(task, draw);
		}
		origin.compileNow();
		magicPush(stringBounder);
		double yy = lastY(stringBounder);
		if (yy == 0) {
			yy = headerHeight;
		} else if (this.hideResourceFoobox == false)
			for (Resource res : resources.values()) {
				final ResourceDraw draw = buildResourceDraw(this, res, timeScale, yy, min, max);
				res.setTaskDraw(draw);
				yy += draw.getHeight(stringBounder);
			}

		this.totalHeightWithoutFooter = yy;
	}

	private ResourceDraw buildResourceDraw(GanttDiagram gantt, Resource res, TimeScale timeScale, double y, Day min,
			Day max) {
//		if (printScale == PrintScale.DAILY || printScale == PrintScale.MONTHLY || printScale == PrintScale.QUARTERLY
//				|| printScale == PrintScale.YEARLY)
//			return new ResourceDrawHistogram(gantt, res, timeScale, y, min, max);

		return new ResourceDrawNumbers(gantt, res, timeScale, y, min, max);
		// return new ResourceDrawVersion2(gantt, res, timeScale, y, min, max);
	}

	private Collection<GanttConstraint> getConstraints(Task task) {
		final List<GanttConstraint> result = new ArrayList<>();
		for (GanttConstraint constraint : constraints)
			if (constraint.isOn(task))
				result.add(constraint);

		return Collections.unmodifiableCollection(result);
	}

	private double lastY(StringBounder stringBounder) {
		double result = 0;
		for (TaskDraw td : draws.values())
			result = Math.max(result, td.getY(stringBounder).getCurrentValue() + td.getHeightMax(stringBounder));

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

			if (fingerPrintNote != null)
				notes.add(td);

		}
	}

	private Day getStart(final Task tmp) {
		if (printStart == null)
			return tmp.getStart();

		return Day.max(min, tmp.getStart());
	}

	private Day getEnd(final Task tmp) {
		if (printStart == null)
			return tmp.getEnd();

		return Day.min(max, tmp.getEnd());
	}

	private void initMinMax() {
		if (tasks.size() == 0) {
			max = min.increment();
		} else {
			max = null;
			for (Task task : tasks.values()) {
				if (task instanceof TaskSeparator || task instanceof TaskGroup)
					continue;

				final Day start = task.getStart();
				final Day end = task.getEnd();
				// if (min.compareTo(start) > 0) {
				// min = start;
				// }
				if (max == null || max.compareTo(end) < 0)
					max = end;

			}
		}
		if (openClose.getStartingDay() != null) {
			for (Day d : colorDays().keySet())
				if (d.compareTo(max) > 0)
					max = d;

			for (Day d : nameDays.keySet())
				if (d.compareTo(max) > 0)
					max = d;

		}
	}

	public Day getThenDate() {
		Day result = getStartingDate();
		for (Day d : colorDays().keySet())
			if (d.compareTo(result) > 0)
				result = d;

		for (Day d : nameDays.keySet())
			if (d.compareTo(result) > 0)
				result = d;

		return result;
	}

	public Task getExistingTask(String id) {
		final TaskCode code = TaskCode.fromId(Objects.requireNonNull(id));
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

	public Task getOrCreateTask(TaskCode code, boolean linkedToPrevious) {
		Task result = tasks.get(Objects.requireNonNull(code));
		if (result == null) {
			Task previous = null;
			if (linkedToPrevious)
				previous = getLastCreatedTask();

			final OpenClose except = this.openCloseForTask.get(code.getId());

			result = new TaskImpl(getSkinParam().getCurrentStyleBuilder(), code, openClose.mutateMe(except),
					openClose.getStartingDay(), defaultCompletion);
			if (currentGroup != null)
				currentGroup.addTask(result);

			tasks.put(code, result);

			if (previous != null)
				forceTaskOrder(previous, result);

		}
		return result;
	}

	private Task getLastCreatedTask() {
		final List<Task> all = new ArrayList<>(tasks.values());
		for (int i = all.size() - 1; i >= 0; i--)
			if (all.get(i) instanceof TaskImpl)
				return all.get(i);

		return null;
	}

	public void addSeparator(String comment) {
		TaskSeparator separator = new TaskSeparator(getSkinParam().getCurrentStyleBuilder(), comment, tasks.size());
		tasks.put(separator.getCode(), separator);
	}

	private TaskGroup currentGroup = null;

	public CommandExecutionResult addGroup(TaskCode code) {
		TaskGroup group = new TaskGroup(this.currentGroup, getSkinParam().getCurrentStyleBuilder(), code);

		if (this.currentGroup != null)
			this.currentGroup.addTask(group);

		this.currentGroup = group;
		tasks.put(group.getCode(), group);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult endGroup() {
		if (this.currentGroup == null)
			return CommandExecutionResult.error("No group to be closed");

		this.currentGroup = this.currentGroup.getParent();

		return CommandExecutionResult.ok();
	}

	public void addContraint(GanttConstraint constraint) {
		constraints.add(constraint);
	}

	public HColorSet getIHtmlColorSet() {
		return colorSet;
	}

	public void setProjectStartingDate(Day start) {
		openClose.setStartingDay(start);
		this.min = start;
	}

	public Day getStartingDate() {
		if (openClose.getStartingDay() == null)
			return min;

		return openClose.getStartingDay();
	}

	public Day getEndingDate() {
		initMinMax();
		return max;
	}

	public int daysInWeek() {
		return openClose.daysInWeek();
	}

	public boolean isOpen(Day day) {
		return openClose.getLoadAt(day) > 0;
	}

	private static final Pattern p = Pattern.compile("([^:]+)(:(\\d+))?");

	public boolean affectResource(Task result, String description) {
		final Matcher m = p.matcher(description);
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
		Resource resource = resources.get(resourceName);
		if (resource == null)
			resource = new Resource(resourceName);

		resources.put(resourceName, resource);
		return resource;
	}

	public int getLoadForResource(Resource res, Day i) {
		int result = 0;
		for (Task task : tasks.values()) {
			if (task instanceof TaskSeparator)
				continue;

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
				if (ent.getValue().equalsIgnoreCase(id) == false)
					continue;

				start = min(start, ent.getKey());
				end = max(end, ent.getKey());
			}
			if (start != null)
				result = new MomentImpl(start, end);

		}
		return result;
	}

	private Day min(Day d1, Day d2) {
		if (d1 == null)
			return d2;

		if (d1.compareTo(d2) > 0)
			return d2;

		return d1;
	}

	private Day max(Day d1, Day d2) {
		if (d1 == null)
			return d2;

		if (d1.compareTo(d2) < 0)
			return d2;

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

	public Day getToday() {
		if (today == null)
			this.today = Day.today();

		return today;
	}

	public void setTodayColors(CenterBorderColor colors) {
		if (today == null)
			this.today = Day.today();

		colorDaysToday.put(today, colors.getCenter());
	}

	public CommandExecutionResult setToday(Day date) {
		this.today = date;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult deleteTask(Task task) {
		task.setColors(new CenterBorderColor(HColors.WHITE, HColors.BLACK));
		return CommandExecutionResult.ok();
	}

	public void setPrintInterval(Day start, Day end) {
		this.printStart = start;
		this.printEnd = end;
	}

	public TaskDraw getTaskDraw(Task task) {
		return draws.get(task);
	}

	public CommandExecutionResult addNote(Display note, Stereotype stereotype) {
		Task last = null;
		for (Task current : tasks.values())
			last = current;
		if (last == null)
			return CommandExecutionResult.error("No task defined");

		last.setNote(note, stereotype);
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

	private WeeklyHeaderStrategy weeklyHeaderStrategy;
	private int weekStartingNumber;

	public void setWeeklyHeaderStrategy(WeeklyHeaderStrategy weeklyHeaderStrategy, int weekStartingNumber) {
		this.weeklyHeaderStrategy = weeklyHeaderStrategy;
		this.weekStartingNumber = weekStartingNumber;
	}

	private boolean hideResourceName;
	private boolean hideResourceFoobox;

	public CommandExecutionResult hideResourceName() {
		this.hideResourceName = true;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult hideResourceFootbox() {
		this.hideResourceFoobox = true;
		return CommandExecutionResult.ok();
	}

	private final Set<Day> verticalSeparatorBefore = new HashSet<>();

	public void addVerticalSeparatorBefore(Day day) {
		verticalSeparatorBefore.add(day);
	}

	public void setTaskDefaultCompletion(int defaultCompletion) {
		this.defaultCompletion = defaultCompletion;
	}

	public List<TaskDrawRegular> getAllTasksForResource(Resource res) {
		final List<TaskDrawRegular> result = new ArrayList<TaskDrawRegular>();
		for (Task task : tasks.values())
			if (task.isAssignedTo(res)) {
				final TaskDrawRegular draw = (TaskDrawRegular) draws.get(task);
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

	private boolean hideClosed = false;

	public void setHideClosed(boolean hideClosed) {
		this.hideClosed = hideClosed;
	}

}
