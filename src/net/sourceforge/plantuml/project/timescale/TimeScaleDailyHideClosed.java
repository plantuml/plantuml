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

import net.sourceforge.plantuml.project.OpenClose;
import net.sourceforge.plantuml.project.time.Day;

public final class TimeScaleDailyHideClosed implements TimeScale {
	// ::remove folder when __HAXE__

	private final double cellWidth;
	private final Day startingDay;
	private Day biggest;
	private final OpenClose openClose;
	private final Map<Day, Integer> startingInt = new HashMap<>();

	public TimeScaleDailyHideClosed(double size, Day startingDay, double scale, OpenClose openClose) {
		this.cellWidth = size * scale;
		this.startingDay = startingDay;
		this.biggest = startingDay;
		this.openClose = openClose;
		this.startingInt.put(startingDay, 0);
	}

	@Override
	public double getStartingPosition(Day instant) {
		if (instant.compareTo(this.startingDay) < 0)
			throw new IllegalArgumentException();

		computeUpTo(instant);

		return startingInt.get(instant) * cellWidth;
	}

	private void computeUpTo(Day dest) {
		if (dest.compareTo(biggest) <= 0)
			return;

		Day i = biggest;
		int current = startingInt.get(i);
		do {
			if (getWidth(i) > 0)
				current++;
			i = i.increment();
			startingInt.put(i, current);
		} while (i.compareTo(dest) < 0);
	}

	@Override
	public double getEndingPosition(Day instant) {
		return getStartingPosition(instant) + getWidth(instant);
	}

	@Override
	public double getWidth(Day instant) {
		if (openClose.isClosed(instant))
			return 0;
		return cellWidth;
	}

	@Override
	public boolean isBreaking(Day instant) {
		return true;
	}

}
