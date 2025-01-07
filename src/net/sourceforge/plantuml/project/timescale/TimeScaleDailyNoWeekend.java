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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.DayOfWeek;

public final class TimeScaleDailyNoWeekend implements TimeScale {
	// ::remove folder when __HAXE__

	private final double cellWidth;
	private final Day startingDay;
	private final Map<Day, Double> startingPosition = new HashMap<>();

	public TimeScaleDailyNoWeekend(double size, Day startingDay, double scale) {
		this.cellWidth = size * scale;
		this.startingDay = startingDay;
	}

	public double getStartingPosition(Day instant) {
		if (instant.compareTo(this.startingDay) < 0)
			throw new IllegalArgumentException();

		final Double cached = startingPosition.get(instant);
		if (cached != null)
			return cached;

		double result = 0;
		for (Day i = startingDay; i.compareTo(instant) < 0; i = i.increment())
			result += getWidth(i);
		startingPosition.put(instant, result);
		return result;
	}

	public double getEndingPosition(Day instant) {
		return getStartingPosition(instant) + getWidth(instant);
	}

	public double getWidth(Day instant) {
		final DayOfWeek dayOfWeek = instant.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
			return 0;
		return cellWidth;
	}

	public boolean isBreaking(Day instant) {
		return true;
	}

}
