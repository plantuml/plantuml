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
package net.sourceforge.plantuml.project.draw.header;

import java.time.LocalDate;

import net.sourceforge.plantuml.project.data.DayCalendarData;
import net.sourceforge.plantuml.project.data.TimeBoundsData;
import net.sourceforge.plantuml.project.data.TimeScaleConfigData;
import net.sourceforge.plantuml.project.data.TimelineStyleData;
import net.sourceforge.plantuml.project.data.WeekConfigData;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.project.timescale.TimeScaleDailyHideClosed;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;

public class TimeHeaderFactory {

	private final TimeBoundsData timeBounds;
	private final DayCalendarData dayCalendar;
	private final TimelineStyleData timelineStyle;
	private final TimeScaleConfigData scaleConfig;
	private final WeekConfigData weekConfig;

	public TimeHeaderFactory(WeekConfigData weekConfig, DayCalendarData dayCalendar, TimeBoundsData timeBounds,
			TimeScaleConfigData scaleConfig, TimelineStyleData timelineStyle) {
		this.weekConfig = weekConfig;
		this.timeBounds = timeBounds;
		this.dayCalendar = dayCalendar;
		this.scaleConfig = scaleConfig;
		this.timelineStyle = timelineStyle;
	}

	// ========================================================================
	// Public API
	// ========================================================================

	public TimeHeader createTimeHeader() {
		if (timeBounds.getMinDay().equals(TimePoint.epoch()))
			return new TimeHeaderSimple(createSimpleScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		switch (scaleConfig.getPrintScale()) {
		case DAILY:
			return new TimeHeaderDaily(createDailyScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		case WEEKLY:
			return new TimeHeaderWeekly(createCompressedScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		case MONTHLY:
			return new TimeHeaderMonthly(createCompressedScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		case QUARTERLY:
			return new TimeHeaderQuarterly(createCompressedScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		case YEARLY:
			return new TimeHeaderYearly(createCompressedScale(), weekConfig, dayCalendar, timeBounds, scaleConfig,
					timelineStyle);

		default:
			throw new IllegalStateException("Unsupported print scale: " + scaleConfig.getPrintScale());
		}
	}

	// ========================================================================
	// TimeScale builders
	// ========================================================================

	private TimeScale createSimpleScale() {
		return new TimeScaleWink(getCellWidth(), getEffectiveScale(), scaleConfig.getPrintScale());
	}

	private TimeScale createDailyScale() {
		if (scaleConfig.isHideClosed())
			return new TimeScaleDailyHideClosed(getCellWidth(), getStart(), getEffectiveScale(),
					dayCalendar.getOpenClose());

		return new TimeScaleDaily(getCellWidth(), getStart(), getEffectiveScale(), getPrintStart());
	}

	private TimeScale createCompressedScale() {
		return new TimeScaleCompressed(getCellWidth(), getStart(), getEffectiveScale(), getPrintStart());
	}

	// ========================================================================
	// Helpers
	// ========================================================================

	private TimePoint getStart() {
		return TimePoint.ofStartOfDay(timeBounds.getMinDay());
	}

	private LocalDate getPrintStart() {
		return timeBounds.getPrintStart();
	}

	private double getEffectiveScale() {
		return scaleConfig.getEffectiveScale();
	}

	private double getCellWidth() {
		return timelineStyle.getCellWidth();
	}

}
