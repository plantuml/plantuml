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
import java.util.Map;

import net.sourceforge.plantuml.project.TimeHeaderParameters;
import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.project.timescale.TimeScaleCompressed;
import net.sourceforge.plantuml.project.timescale.TimeScaleDaily;
import net.sourceforge.plantuml.project.timescale.TimeScaleDailyHideClosed;
import net.sourceforge.plantuml.project.timescale.TimeScaleWink;

public class TimeHeaderContext {

	private final PrintScale printScale;
	private final TimeHeaderParameters thParam;
	private final Map<TimePoint, String> nameDays;

	private final WeekNumberStrategy weekNumberStrategy;
	private final WeeklyHeaderStrategy headerStrategy;
	private final LocalDate printStart;
	private final int weekStartingNumber;

	public TimeHeaderContext(PrintScale printScale, TimeHeaderParameters thParam, Map<TimePoint, String> nameDays,
			WeekNumberStrategy weekNumberStrategy, WeeklyHeaderStrategy headerStrategy, LocalDate printStart,
			int weekStartingNumber) {
		this.weekStartingNumber = weekStartingNumber;
		this.printScale = printScale;
		this.thParam = thParam;
		this.nameDays = nameDays;
		this.weekNumberStrategy = weekNumberStrategy;
		this.headerStrategy = headerStrategy;
		this.printStart = printStart;
	}

	public int getWeekStartingNumber() {
		return weekStartingNumber;
	}

	public PrintScale getPrintScale() {
		return printScale;
	}

	public TimeHeaderParameters thParam() {
		return thParam;
	}

	public Map<TimePoint, String> getNameDays() {
		return nameDays;
	}

	public WeekNumberStrategy getWeekNumberStrategy() {
		return weekNumberStrategy;
	}

	public WeeklyHeaderStrategy getHeaderStrategy() {
		return headerStrategy;
	}

	public LocalDate getPrintStart() {
		return printStart;
	}

	// TimeScale Builder

	public TimeScale simple() {
		return new TimeScaleWink(thParam().getCellWidth(), thParam().getScale(), getPrintScale());
	}

	public TimeScale daily() {
		return thParam().isHideClosed()
				? new TimeScaleDailyHideClosed(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
						thParam().getScale(), thParam().getOpenClose())
				: new TimeScaleDaily(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
						thParam().getScale(), getPrintStart());
	}

	public TimeScale weekly() {
		return new TimeScaleCompressed(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
				thParam().getScale(), getPrintStart());
	}

	public TimeScale monthly() {
		return new TimeScaleCompressed(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
				thParam().getScale(), getPrintStart());
	}

	public TimeScale quaterly() {
		return new TimeScaleCompressed(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
				thParam().getScale(), getPrintStart());
	}

	public TimeScale yearly() {
		return new TimeScaleCompressed(thParam().getCellWidth(), TimePoint.ofStartOfDay(thParam().getMinDay()),
				thParam().getScale(), getPrintStart());
	}

	public TimeHeader buildTimeHeader() {
		if (printScale == PrintScale.DAILY)
			return new TimeHeaderDaily(this);
		else if (printScale == PrintScale.WEEKLY)
			return new TimeHeaderWeekly(this);
		else if (printScale == PrintScale.MONTHLY)
			return new TimeHeaderMonthly(this);
		else if (printScale == PrintScale.QUARTERLY)
			return new TimeHeaderQuarterly(this);
		else if (printScale == PrintScale.YEARLY)
			return new TimeHeaderYearly(this);
		else
			throw new IllegalStateException();
	}

}
