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
package net.sourceforge.plantuml.project;

import net.sourceforge.plantuml.project.time.Day;

public class PlanUtils {

	private PlanUtils() {

	}

	public static LoadPlanable minOf(final LoadPlanable p1, final LoadPlanable p2) {
		return new LoadPlanable() {
			public int getLoadAt(Day instant) {
				return Math.min(p1.getLoadAt(instant), p2.getLoadAt(instant));
			}

			public Day getLastDayIfAny() {
				return lastOf(p1.getLastDayIfAny(), p2.getLastDayIfAny());
			}
		};
	}

	public static LoadPlanable multiply(final LoadPlanable p1, final LoadPlanable p2) {
		return new LoadPlanable() {
			public int getLoadAt(Day instant) {
				return p1.getLoadAt(instant) * p2.getLoadAt(instant) / 100;
			}

			public Day getLastDayIfAny() {
				return lastOf(p1.getLastDayIfAny(), p2.getLastDayIfAny());
			}
		};
	}

	private static Day lastOf(Day day1, Day day2) {
		if (day1 == null)
			return day2;
		if (day2 == null)
			return day1;
		if (day1.compareTo(day2) > 0)
			return day1;
		return day2;
	}

}
