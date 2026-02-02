package net.sourceforge.plantuml.project;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.draw.TaskDraw;
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderDaily;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderMonthly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderQuarterly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderWeekly;
import net.sourceforge.plantuml.project.draw.header.TimeHeaderYearly;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.project.timescale.TimeScaleDailyHideClosed;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.Value;

public class GanttPreparedModel {

	final Map<Task, TaskDraw> draws = new LinkedHashMap<Task, TaskDraw>();
	double totalHeightWithoutFooter;
	public LocalDate minDay = TimePoint.epoch();
	public LocalDate maxDay;

	final Map<TimePoint, HColor> colorDaysToday = new HashMap<TimePoint, HColor>();
	final Map<TimePoint, HColor> colorDaysInternal = new HashMap<TimePoint, HColor>();
	public final Map<DayOfWeek, HColor> colorDaysOfWeek = new HashMap<DayOfWeek, HColor>();
	public final Map<TimePoint, String> nameDays = new HashMap<TimePoint, String>();

	public PrintScale printScale = PrintScale.DAILY;

	public WeeklyHeaderStrategy weeklyHeaderStrategy;
	public int weekStartingNumber;

	// Let's follow ISO-8601 rules
	public WeekNumberStrategy weekNumberStrategy = new WeekNumberStrategy(DayOfWeek.MONDAY, 4);

	public LocalDate printStart;
	public boolean hideClosed;
	public final Set<TimePoint> verticalSeparatorBefore = new HashSet<>();

	public final OpenClose openClose = new OpenClose();

	public Locale locale = Locale.ENGLISH;

	public final HColorSet colorSet = HColorSet.instance();

	public double factorScale = 1.0;
	public final GanttStyle ganttStyle;

	public GanttPreparedModel(GanttStyle ganttStyle) {
		this.ganttStyle = ganttStyle;
	}

	public Map<TimePoint, HColor> colorDays() {
		colorDaysInternal.putAll(colorDaysToday);
		return Collections.unmodifiableMap(colorDaysInternal);
	}

	public double getFactorScale() {
		return printScale.getDefaultScale() * factorScale;
	}
	
	public Value getFontSizeDay() {
		return getStyleDay().value(PName.FontSize);
	}

	public Value getFontSizeMonth() {
		return getStyleTOTO3(SName.timeline, SName.month).value(PName.FontSize);
	}

	public Value getFontSizeYear() {
		return getStyleTOTO3(SName.timeline, SName.year).value(PName.FontSize);
	}

	public Style getStyleDay() {
		return getStyleTOTO3(SName.timeline, SName.day);
	}


	// TimeScale Builder

	final public double getCellWidth() {
		final double w = getStyleTOTO3(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		return w * 1.6;
	}

	public final Style getStyleTOTO3(SName param1, SName param2) {
		return this.ganttStyle.getStyle(param1, param2);
	}

	public Style getStyleTOTO4(SName param) {
		return this.ganttStyle.getStyle(param);
	}

	public final UGraphic forVerticalSeparator(UGraphic ug) {
		final Style style = getStyleTOTO4(SName.verticalSeparator);
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

}
