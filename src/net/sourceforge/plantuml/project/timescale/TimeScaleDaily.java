/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.time.GCalendar;
import net.sourceforge.plantuml.project.time.Wink;

public final class TimeScaleDaily implements TimeScale {

	private final TimeScaleWink basic;
	private final double delta;

	public TimeScaleDaily(GCalendar calendar, Day zeroDay) {
		this.basic = new TimeScaleWink();
		if (zeroDay == null) {
			this.delta = 0;
		} else {
			this.delta = basic.getStartingPosition(calendar.fromDayAsDate(zeroDay));
		}

	}

	public double getStartingPosition(Wink instant) {
		return basic.getStartingPosition(instant) - delta;
	}

	public double getEndingPosition(Wink instant) {
		return basic.getEndingPosition(instant) - delta;
	}

	public double getWidth(Wink instant) {
		return basic.getWidth(instant);
	}

}
