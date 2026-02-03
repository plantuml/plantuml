package net.sourceforge.plantuml.project;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.core.TaskImpl;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.draw.FingerPrint;
import net.sourceforge.plantuml.project.draw.ResourceDraw;
import net.sourceforge.plantuml.project.draw.ResourceDrawNumbers;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.TaskDrawDiamond;
import net.sourceforge.plantuml.project.draw.TaskDrawGroup;
import net.sourceforge.plantuml.project.draw.TaskDrawRegular;
import net.sourceforge.plantuml.project.draw.TaskDrawSeparator;
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderDaily;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderMonthly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderQuarterly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderWeekly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderYearly;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.project.timescale.TimeScaleDailyHideClosed;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;

public class GanttPreparedModel implements ToTaskDraw {

	// ------------------------------------------------------------------------
	// core domain data
	// ------------------------------------------------------------------------
	public final List<GanttConstraint> constraints = new ArrayList<>();
	protected final Map<TaskCode, Task> tasks = new LinkedHashMap<>();
	public final Map<String, Resource> resources = new LinkedHashMap<>();

	// ------------------------------------------------------------------------
	// layout / origin
	// ------------------------------------------------------------------------
	public final RealOrigin origin = RealUtils.createOrigin();

	// ------------------------------------------------------------------------
	// inputs / configuration
	// ------------------------------------------------------------------------
	public final GanttStyle ganttStyle;

	public Locale locale = Locale.ENGLISH;

	public PrintScale printScale = PrintScale.DAILY;
	public double factorScale = 1.0;

	public boolean hideClosed;
	public LocalDate printStart;
	public LocalDate printEnd;

	public WeeklyHeaderStrategy weeklyHeaderStrategy;
	public int weekStartingNumber;

	// Let's follow ISO-8601 rules
	public WeekNumberStrategy weekNumberStrategy = new WeekNumberStrategy(DayOfWeek.MONDAY, 4);

	public LabelStrategy labelStrategy = new LabelStrategy(LabelPosition.LEGACY, HorizontalAlignment.LEFT);

	public boolean showFootbox = true;
	public boolean hideResourceName;
	public boolean hideResourceFoobox;

	// ------------------------------------------------------------------------
	// model bounds / computed scalars
	// ------------------------------------------------------------------------
	public LocalDate minDay = TimePoint.epoch();
	public LocalDate maxDay;

	double totalHeightWithoutFooter;

	// ------------------------------------------------------------------------
	// prepared drawing / layout artifacts
	// ------------------------------------------------------------------------
	final Map<Task, TaskDraw> draws = new LinkedHashMap<>();
	public final Set<TimePoint> verticalSeparatorBefore = new HashSet<>();

	// ------------------------------------------------------------------------
	// timeline labels and colors (prepared caches)
	// ------------------------------------------------------------------------
	public final Map<TimePoint, String> nameDays = new HashMap<>();

	public final Map<TimePoint, HColor> colorDaysToday = new HashMap<>();
	final Map<TimePoint, HColor> colorDaysInternal = new HashMap<>();
	public final Map<DayOfWeek, HColor> colorDaysOfWeek = new HashMap<>();

	// ------------------------------------------------------------------------
	// internal helpers / shared infrastructure
	// ------------------------------------------------------------------------
	public final OpenClose openClose = new OpenClose();
	public final HColorSet colorSet = HColorSet.instance();
	private final ISkinParam skinParam;

	public GanttPreparedModel(GanttStyle ganttStyle, ISkinParam skinParam) {
		this.ganttStyle = ganttStyle;
		this.skinParam = skinParam;
	}

	public Map<TimePoint, HColor> colorDays() {
		colorDaysInternal.putAll(colorDaysToday);
		return Collections.unmodifiableMap(colorDaysInternal);
	}

	public double getFactorScale() {
		return printScale.getDefaultScale() * factorScale;
	}

	public double getFontSizeDay() {
		return getStyleDay().value(PName.FontSize).asDouble();
	}

	public double getFontSizeMonth() {
		return ganttStyle.getStyle(SName.timeline, SName.month).value(PName.FontSize).asDouble();
	}

	public double getFontSizeYear() {
		return ganttStyle.getStyle(SName.timeline, SName.year).value(PName.FontSize).asDouble();
	}

	public Style getStyleDay() {
		return ganttStyle.getStyle(SName.timeline, SName.day);
	}

	public final HColor closedBackgroundColor() {
		return ganttStyle.getStyle(SName.closed).value(PName.BackGroundColor).asColor(this.colorSet);
	}

	public final HColor closedFontColor() {
		return ganttStyle.getStyle(SName.closed).value(PName.FontColor).asColor(this.colorSet);
	}

	public final HColor openFontColor() {
		return ganttStyle.getStyle(SName.timeline).value(PName.FontColor).asColor(this.colorSet);
	}

	public final HColor getLineColor() {
		return ganttStyle.getStyle(SName.timeline).value(PName.LineColor).asColor(this.colorSet);
	}

	// TimeScale Builder

	final public double getCellWidth() {
		final double w = getStyleDay().value(PName.FontSize).asDouble();
		return w * 1.6;
	}

	public UFont getStyleUFont(SName param) {
		return ganttStyle.getStyle(SName.timeline, param).getUFont();
	}

	public final UGraphic forVerticalSeparator(UGraphic ug) {
		final Style style = ganttStyle.getStyle(SName.verticalSeparator);
		final HColor color = style.value(PName.LineColor).asColor(this.colorSet);
		final UStroke stroke = style.getStroke();
		return ug.apply(color).apply(stroke);
	}

	public TimeScale simple() {
		return new TimeScaleWink(getCellWidth(), this.getFactorScale(), this.printScale);
	}

	public TimeScale daily() {
		return this.hideClosed
				? new TimeScaleDailyHideClosed(getCellWidth(), TimePoint.ofStartOfDay(this.minDay),
						this.getFactorScale(), this.openClose)
				: new TimeScaleDaily(getCellWidth(), TimePoint.ofStartOfDay(this.minDay), this.getFactorScale(),
						this.printStart);
	}

	public TimeScale weekly() {
		return new TimeScaleCompressed(getCellWidth(), TimePoint.ofStartOfDay(this.minDay), this.getFactorScale(),
				this.printStart);
	}

	public TimeScale monthly() {
		return new TimeScaleCompressed(getCellWidth(), TimePoint.ofStartOfDay(this.minDay), this.getFactorScale(),
				this.printStart);
	}

	public TimeScale quaterly() {
		return new TimeScaleCompressed(getCellWidth(), TimePoint.ofStartOfDay(this.minDay), this.getFactorScale(),
				this.printStart);
	}

	public TimeScale yearly() {
		return new TimeScaleCompressed(getCellWidth(), TimePoint.ofStartOfDay(this.minDay), this.getFactorScale(),
				this.printStart);
	}

	public TimeHeader buildTimeHeader() {
		if (this.printScale == PrintScale.DAILY)
			return new TimeHeaderDaily(this);
		else if (this.printScale == PrintScale.WEEKLY)
			return new TimeHeaderWeekly(this);
		else if (this.printScale == PrintScale.MONTHLY)
			return new TimeHeaderMonthly(this);
		else if (this.printScale == PrintScale.QUARTERLY)
			return new TimeHeaderQuarterly(this);
		else if (this.printScale == PrintScale.YEARLY)
			return new TimeHeaderYearly(this);
		else
			throw new IllegalStateException();
	}

	protected TimePoint getStartForDrawing(final Task tmp) {
		TimePoint result;
		if (printStart == null)
			result = tmp.getStart();
		else
			result = TimePoint.max(TimePoint.ofStartOfDay(minDay), tmp.getStart());

		return result;
	}

	protected TimePoint getEndForDrawing(final Task tmp) {
		TimePoint result;
		if (printStart == null)
			result = tmp.getEnd();
		else
			result = TimePoint.min(TimePoint.ofStartOfDay(maxDay.plusDays(1)), tmp.getEnd());

		return result;
	}

	public void magicPush(StringBounder stringBounder) {
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

	public double lastY(StringBounder stringBounder) {
		double result = 0;
		for (TaskDraw td : draws.values())
			result = Math.max(result, td.getY(stringBounder).getCurrentValue() + td.getHeightMax(stringBounder));

		return result;
	}

	@Override
	public TaskDraw getTaskDraw(Task task) {
		return draws.get(task);
	}

	@Override
	public PiecewiseConstant getDefaultPlan() {
		return openClose.asPiecewiseConstant();
	}

	@Override
	public HColorSet getIHtmlColorSet() {
		return colorSet;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}

	protected TaskDraw createTaskDrawRegular(TimeScale timeScale, Real y, final Task task, final String display) {
		final boolean oddEnd;
		final boolean oddStart;
		final TimePoint startForDrawing = getStartForDrawing(task);
		final TimePoint endForDrawing = getEndForDrawing(task);
		if (printStart != null) {
			oddStart = TimePoint.ofStartOfDay(minDay).compareTo(startForDrawing) == 0;
			oddEnd = TimePoint.ofStartOfDay(maxDay.plusDays(1)).compareTo(endForDrawing) == 0;
		} else {
			oddStart = false;
			oddEnd = false;
		}
		return new TaskDrawRegular(timeScale, y, display, startForDrawing, endForDrawing, oddStart, oddEnd,
				getSkinParam(), task, this, getConstraints(task), task.getStyleBuilder());
	}

	private Collection<GanttConstraint> getConstraints(Task task) {
		final List<GanttConstraint> result = new ArrayList<>();
		for (GanttConstraint constraint : constraints)
			if (constraint.isOn(task))
				result.add(constraint);

		return Collections.unmodifiableCollection(result);
	}

	public Pragma getPragma() {
		return skinParam.getPragma();
	}

	public int getLoadForResource(Resource res, TimePoint i) {
		int result = 0;
		for (Task task : tasks.values()) {
			if (task instanceof TaskSeparator)
				continue;

			final TaskImpl task2 = (TaskImpl) task;
			result += task2.loadForResource(res, i);
		}
		return result;
	}

	protected ResourceDraw buildResourceDraw(Resource res, TimeScale timeScale, double y) {
		return new ResourceDrawNumbers(this, res, timeScale, y, TimePoint.ofStartOfDay(minDay),
				TimePoint.ofEndOfDayMinusOneSecond(maxDay));
	}

	public void initTaskAndResourceDraws(StringBounder stringBounder, TimeHeader timeHeader) {
		final TimeScale timeScale = timeHeader.getTimeScale();
		final double fullHeaderHeight = timeHeader.getFullHeaderHeight(stringBounder);
		Real y = origin.addFixed(fullHeaderHeight);
		for (Task task : tasks.values()) {
			final TaskDraw draw;
			if (task instanceof TaskSeparator) {
				final TaskSeparator taskSeparator = (TaskSeparator) task;
				draw = new TaskDrawSeparator(taskSeparator.getName(), timeScale, y, minDay, maxDay,
						task.getStyleBuilder(), getSkinParam());
			} else if (task instanceof TaskGroup) {
				final TaskGroup taskGroup = (TaskGroup) task;
				draw = new TaskDrawGroup(timeScale, y, taskGroup.getCode().getDisplay(), getStartForDrawing(taskGroup),
						getEndForDrawing(taskGroup), task, this, task.getStyleBuilder(), getSkinParam());
			} else {
				final TaskImpl taskImpl = (TaskImpl) task;
				final String display = hideResourceName ? taskImpl.getCode().getDisplay() : taskImpl.getPrettyDisplay();
				if (taskImpl.isDiamond())
					draw = new TaskDrawDiamond(timeScale, y, display, getStartForDrawing(taskImpl), taskImpl, this,
							task.getStyleBuilder(), getSkinParam());
				else
					draw = createTaskDrawRegular(timeScale, y, taskImpl, display);

				draw.setColorsAndCompletion(taskImpl.getColors(), taskImpl.getCompletion(), taskImpl.getUrl(),
						taskImpl.getNote(), taskImpl.getNoteStereotype());
			}
			if (task.getRow() == null)
				y = y.addAtLeast(draw.getFullHeightTask(stringBounder));

			draws.put(task, draw);
		}
		origin.compileNow();
		magicPush(stringBounder);
		double yy = lastY(stringBounder);
		if (yy == 0) {
			yy = fullHeaderHeight;
		} else if (hideResourceFoobox == false)
			for (Resource res : resources.values()) {
				final ResourceDraw draw = buildResourceDraw(res, timeScale, yy);
				res.setTaskDraw(draw);
				yy += draw.getHeight(stringBounder);
			}

		totalHeightWithoutFooter = yy;
	}

	protected boolean isHidden(Task task) {
		if (printStart == null || task instanceof TaskSeparator)
			return false;

		if (task.getEndMinusOneDayTOBEREMOVED().compareTo(TimePoint.ofStartOfDay(minDay)) < 0)
			return true;

		if (task.getStart().compareTo(TimePoint.ofEndOfDayMinusOneSecond(maxDay)) > 0)
			return true;

		return false;
	}

}
