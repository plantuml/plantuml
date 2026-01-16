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

import net.sourceforge.plantuml.project.core.PrintScale;
import net.sourceforge.plantuml.project.time.Day;

public final class TimeScaleDaily implements TimeScale {
	// ::remove folder when __HAXE__

	private final TimeScaleWink basic;
	private final double delta;

	public TimeScaleDaily(double size, Day startingDay, double scale, Day zeroDay) {
		this.basic = new TimeScaleWink(size, scale, PrintScale.DAILY);
		if (zeroDay == null)
			this.delta = basic.getStartingPosition(startingDay);
		else
			this.delta = basic.getStartingPosition(zeroDay);

	}

	@Override
	public double getStartingPosition(Day instant) {
		return basic.getStartingPosition(instant) - delta;
	}

	@Override
	public double getEndingPosition(Day instant) {
		return basic.getEndingPosition(instant) - delta;
	}

	@Override
	public double getWidth(Day instant) {
		return basic.getWidth(instant);
	}

	@Override
	public boolean isBreaking(Day instant) {
		return true;
	}

}
