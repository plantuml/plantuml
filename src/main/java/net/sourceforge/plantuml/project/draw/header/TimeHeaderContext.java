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
import net.sourceforge.plantuml.project.draw.WeeklyHeaderStrategy;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.time.WeekNumberStrategy;
import net.sourceforge.plantuml.project.timescale.TimeScale;

public class TimeHeaderContext {

	private final TimeScale timeScale;
	private final TimeHeaderParameters thParam;
	private final Map<TimePoint, String> nameDays;

	private final WeekNumberStrategy weekNumberStrategy;
	private final WeeklyHeaderStrategy headerStrategy;
	private final LocalDate printStart;

	public TimeHeaderContext(TimeScale timeScale, TimeHeaderParameters thParam, Map<TimePoint, String> nameDays,
			WeekNumberStrategy weekNumberStrategy, WeeklyHeaderStrategy headerStrategy, LocalDate printStart) {
		this.timeScale = timeScale;
		this.thParam = thParam;
		this.nameDays = nameDays;
		this.weekNumberStrategy = weekNumberStrategy;
		this.headerStrategy = headerStrategy;
		this.printStart = printStart;
	}

	public TimeScale getTimeScale() {
		return timeScale;
	}

	public TimeHeaderParameters getTimeHeaderParameters() {
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
}
