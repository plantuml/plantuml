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

import java.time.DayOfWeek;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.project.ngm.math.PiecewiseConstant;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;

public class TimeHeaderParameters implements GanttStyle {

	private final Map<TimePoint, HColor> colorDays;
	private final double scale;
	private final TimePoint min;
	private final TimePoint maxTimePointPrintedStartOfDay;
	private final TimePoint maxTimePointPrintedEndOfDay;
	private final HColorSet colorSet;
	private final GanttStyle ganttStyle;
	private final Locale locale;
	private final OpenClose openClose;
	private final Map<DayOfWeek, HColor> colorDaysOfWeek;
	private final Set<TimePoint> verticalSeparatorBefore;
	private final boolean hideClosed;

	public TimeHeaderParameters(Map<TimePoint, HColor> colorDays, double scale, TimePoint min,
			TimePoint maxTimePointPrintedStartOfDay, TimePoint maxTimePointPrintedEndOfDay, HColorSet colorSet,
			Locale locale, OpenClose openClose, Map<DayOfWeek, HColor> colorDaysOfWeek,
			Set<TimePoint> verticalSeparatorBefore, GanttStyle ganttStyle, boolean hideClosed) {
		this.colorDays = colorDays;
		this.scale = scale;
		this.min = min;
		this.maxTimePointPrintedStartOfDay = maxTimePointPrintedStartOfDay;
		this.maxTimePointPrintedEndOfDay = maxTimePointPrintedEndOfDay;
		this.colorSet = Objects.requireNonNull(colorSet);
		this.ganttStyle = ganttStyle;
		this.locale = locale;
		this.openClose = openClose;
		this.colorDaysOfWeek = colorDaysOfWeek;
		this.verticalSeparatorBefore = verticalSeparatorBefore;
		this.hideClosed = hideClosed;
	}

	public HColor getColor(TimePoint wink) {
		return colorDays.get(wink);
	}

	public HColor getColor(DayOfWeek dayOfWeek) {
		return colorDaysOfWeek.get(dayOfWeek);
	}

	public final double getScale() {
		return scale;
	}

	public final TimePoint getMin() {
		return min;
	}

	public final TimePoint getMaxTimePointPrintedEndOfDay() {
		return maxTimePointPrintedEndOfDay;
	}

	public final HColorSet getColorSet() {
		return colorSet;
	}

	public final Style getTimelineStyle() {
		return getStyle(SName.timeline);
	}

	public final Style getClosedStyle() {
		return getStyle(SName.closed);
	}

	public final Locale getLocale() {
		return locale;
	}

	public final PiecewiseConstant getLoadPlanable() {
		return openClose.asPiecewiseConstant();
	}

	public final Set<TimePoint> getVerticalSeparatorBefore() {
		return verticalSeparatorBefore;
	}

	public final UGraphic forVerticalSeparator(UGraphic ug) {
		final Style style = getStyle(SName.verticalSeparator);
		final HColor color = style.value(PName.LineColor).asColor(getColorSet());
		final UStroke stroke = style.getStroke();
		return ug.apply(color).apply(stroke);
	}

	@Override
	public final Style getStyle(SName param1, SName param2) {
		return ganttStyle.getStyle(param1, param2);
	}

	@Override
	public Style getStyle(SName param) {
		return ganttStyle.getStyle(param);
	}

	public double getCellWidth(StringBounder stringBounder) {
		final double w = getStyle(SName.timeline, SName.day).value(PName.FontSize).asDouble();
		return w * 1.6;
	}

	public boolean isHideClosed() {
		return hideClosed;
	}

	public OpenClose getOpenClose() {
		return openClose;
	}

}
