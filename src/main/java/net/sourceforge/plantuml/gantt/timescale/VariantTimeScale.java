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
package net.sourceforge.plantuml.gantt.timescale;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.sourceforge.plantuml.gantt.OpenClose;
import net.sourceforge.plantuml.gantt.data.WorkingHours;
import net.sourceforge.plantuml.gantt.time.TimePoint;

public class VariantTimeScale implements TimeScale {

	private final LocalDate min;
	private final LocalDate max;
	private final WorkingHours workingHours;
	private final OpenClose openClose;

	public VariantTimeScale(LocalDate min, LocalDate max, WorkingHours workingHours, OpenClose openClose) {
		this.min = min;
		this.max = max;
		this.workingHours = workingHours;
		this.openClose = openClose;
	}

	@Override
	public double getPosition(TimePoint instant) {
		final LocalDateTime timeOnly = instant.toLocalDateTime();
		final LocalDate dateOnly = instant.toDay();
		double tmp = 0;
		for (LocalDate dd = min; dd.isBefore(dateOnly); dd = dd.plusDays(1))
			tmp += workingHours.getWorkingDurationForOneDay(openClose.isClosed(dd));

		return tmp + workingHours.getPartialDayWidth(timeOnly.toLocalTime());
	}

	@Override
	public double getWidth(TimePoint instant) {
		System.err.println("VariantTimeScale::instant=" + instant + " " + instant.toDay());
		final boolean isClosed = openClose.isClosed(instant.toDay());
		return workingHours.getWorkingDurationForOneDay(isClosed);
	}

	@Override
	public boolean isBreaking(TimePoint instant) {
		return false;
	}

}
