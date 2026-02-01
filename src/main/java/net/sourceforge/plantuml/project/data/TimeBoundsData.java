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
package net.sourceforge.plantuml.project.data;

import java.time.LocalDate;

import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskGroup;
import net.sourceforge.plantuml.project.core.TaskSeparator;
import net.sourceforge.plantuml.project.draw.header.TimeHeader;
import net.sourceforge.plantuml.project.time.TimePoint;

/**
 * Value object containing the time bounds of a Gantt diagram.
 */
public class TimeBoundsData {

	private LocalDate minDay = TimePoint.epoch();
	private LocalDate maxDay;
	private LocalDate printStart;
	private LocalDate printEnd;

	public LocalDate getMinDay() {
		return minDay;
	}

	public LocalDate getMaxDay() {
		return maxDay;
	}

	public LocalDate getPrintStart() {
		return printStart;
	}

	public LocalDate getPrintEnd() {
		return printEnd;
	}

	// Setters

	public void setMinDay(LocalDate minDay) {
		this.minDay = minDay;
	}

	public void setMaxDay(LocalDate maxDay) {
		this.maxDay = maxDay;
	}

	public void setPrintStart(LocalDate printStart) {
		this.printStart = printStart;
	}

	public void setPrintEnd(LocalDate printEnd) {
		this.printEnd = printEnd;
	}

	public TimePoint getStartForDrawing(final Task tmp) {
		TimePoint result;
		if (getPrintStart() == null)
			result = tmp.getStart();
		else
			result = TimePoint.max(TimePoint.ofStartOfDay(getMinDay()), tmp.getStart());

		return result;
	}

	public TimePoint getEndForDrawing(final Task tmp) {
		TimePoint result;
		if (getPrintStart() == null)
			result = tmp.getEnd();
		else
			result = TimePoint.min(TimePoint.ofStartOfDay(getMaxDay().plusDays(1)), tmp.getEnd());

		return result;
	}

	public boolean isHidden(Task task) {
		if (this.getPrintStart() == null || task instanceof TaskSeparator)
			return false;

		if (task.getEndMinusOneDayTOBEREMOVED().compareTo(TimePoint.ofStartOfDay(this.getMinDay())) < 0)
			return true;

		if (task.getStart().compareTo(TimePoint.ofEndOfDayMinusOneSecond(this.getMaxDay())) > 0)
			return true;

		return false;
	}

	public double getBarsColumnWidth(TimeHeader timeHeader) {
		final double xmin = timeHeader.getTimeScale().getPosition(TimePoint.ofStartOfDay(getMinDay()));
		final double xmax = timeHeader.getTimeScale().getPosition(TimePoint.ofEndOfDayMinusOneSecond(getMaxDay()));
		return xmax - xmin;
	}

	public void initMinMax(GanttModelData modelData, DayCalendarData dayCalendar) {
		if (modelData.getTasks().isEmpty()) {
			this.setMaxDay(this.getMinDay());
		} else {
			this.setMaxDay(null);
			for (Task task : modelData.getTasks()) {
				if (task instanceof TaskSeparator || task instanceof TaskGroup)
					continue;

				final TimePoint tmp = task.getEnd().minusOneSecond();
				if (this.getMaxDay() == null || this.getMaxDay().compareTo(tmp.toDay()) < 0)
					this.setMaxDay(tmp.toDay());
			}
		}

		for (TimePoint d : dayCalendar.getColorDays())
			if (d.toDay().compareTo(this.getMaxDay()) > 0)
				this.setMaxDay(d.toDay());

		for (TimePoint d : dayCalendar.getNameDays().keySet())
			if (d.toDay().compareTo(this.getMaxDay()) > 0)
				this.setMaxDay(d.toDay());
	}

}
