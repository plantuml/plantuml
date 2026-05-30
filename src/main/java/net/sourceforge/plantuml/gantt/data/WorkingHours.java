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
package net.sourceforge.plantuml.gantt.data;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import net.sourceforge.plantuml.gantt.core.TimeRange;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;

public class WorkingHours {

	private final List<TimeRange> workingTimeRanges;
	private final double dayWidth;
	private final double closeFactor;

	public WorkingHours(List<TimeRange> workingTimeRanges, double dayWidth, double closeFactor) {
		this.workingTimeRanges = workingTimeRanges;
		this.dayWidth = dayWidth;
		this.closeFactor = closeFactor;
	}

	@Override
	public String toString() {
		return workingTimeRanges.toString();
	}

	public double getWorkingDurationForOneDay() {
		Duration working = Duration.ZERO;
		for (TimeRange range : workingTimeRanges)
			working = working.plus(Duration.between(range.getStart(), range.getEnd()));
		final Duration pause = Duration.ofHours(24).minus(working);
		return widthForWorkingHours(working) + widthForClosedHours(pause);
	}

	public double getPartialDayWidth(LocalTime pos) {
		Duration working = Duration.ZERO;
		Duration pause = Duration.ZERO;

		LocalTime t = LocalTime.MIDNIGHT;
		for (TimeRange range : workingTimeRanges) {
			if (pos.isBefore(range.getStart())) {
				pause = pause.plus(Duration.between(t, pos));
				return widthForWorkingHours(working) + widthForClosedHours(pause);
			} else if (pos.isBefore(range.getEnd())) {
				pause = pause.plus(Duration.between(t, range.getStart()));
				working = working.plus(Duration.between(range.getStart(), pos));
				return widthForWorkingHours(working) + widthForClosedHours(pause);
			}
			pause = pause.plus(Duration.between(t, range.getStart()));
			working = working.plus(range.getDuration());
			t = range.getEnd();
		}
		pause = pause.plus(Duration.between(t, pos));
		return widthForWorkingHours(working) + widthForClosedHours(pause);
	}

	private double widthForWorkingHours(final Duration between) {
		return between.getSeconds() * dayWidth / (24.0 * 3600.0);
	}

	private double widthForClosedHours(final Duration between) {
		return closeFactor * widthForWorkingHours(between);
	}

	public void drawOneDay(UGraphic ug, double height) {
		ug = ug.apply(HColors.RED).apply(HColors.RED.bg());
		double current = getPartialDayWidth(LocalTime.MIDNIGHT);
		for (TimeRange range : this.workingTimeRanges) {
			final double start = getPartialDayWidth(range.getStart());
			ug.apply(UTranslate.dx(current)).draw(URectangle.build(start - current, height));
			current = getPartialDayWidth(range.getEnd());
		}
		final double last = getPartialDayWidth(LocalTime.MAX);
		ug.apply(UTranslate.dx(current)).draw(URectangle.build(last - current, height));
	}

}
