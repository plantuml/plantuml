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
package net.sourceforge.plantuml.project.timescale;

import java.time.DayOfWeek;
import java.time.LocalDate;

import net.sourceforge.plantuml.project.time.TimePoint;

public class TimeScaleCompressed implements TimeScale {

	private final TimeScale daily;

	public TimeScaleCompressed(double size, TimePoint calendar, double scale, LocalDate zeroDay) {
		this.daily = new TimeScaleDaily(size, calendar, scale, zeroDay);
	}

	@Override
	public double getStartingPosition(TimePoint instant) {
		return daily.getStartingPosition(instant);
	}

	@Override
	public double getStartingPositionPlusOneDayWidth(TimePoint instant) {
		return daily.getStartingPositionPlusOneDayWidth(instant);
	}

	@Override
	public double getWidth(TimePoint instant) {
		return daily.getWidth(instant);
	}

	@Override
	public boolean isBreaking(TimePoint instant) {
		return instant.toDayOfWeek() == DayOfWeek.SUNDAY;
	}

}
