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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementDuration implements Something<GanttDiagram> {

	public IRegex toRegex(String suffix) {
		return new RegexConcat( //
				new RegexLeaf(4, "COMPLEMENT" + suffix, "(\\d+)[%s]+(hour|day|week|month)s?" + //
						"(?:[%s]+and[%s]+(\\d+)[%s]+(hour|day|week|month)s?)?" //
				)); //
	}

	@Override
	public Failable<Load> getMe(GanttDiagram system, RegexResult arg, String suffix) {

		final String prefix = "COMPLEMENT" + suffix;

		final int firstValue = Integer.parseInt(arg.get(prefix, 0));
		final String firstUnit = arg.get(prefix, 1);
		final int[] firstDaysAndHours = toDaysAndHours(system, firstValue, firstUnit);

		int[] secondDaysAndHours = {0, 0};
		final String secondValue = arg.get(prefix, 2);
		if (secondValue != null) {
			final int value = Integer.parseInt(secondValue);
			final String unit = arg.get(prefix, 3);
			secondDaysAndHours = toDaysAndHours(system, value, unit);
		}

		final int totalDays = firstDaysAndHours[0] + secondDaysAndHours[0];
		final int totalHours = firstDaysAndHours[1] + secondDaysAndHours[1];

		return Failable.ok(Load.ofDaysAndHours(totalDays, totalHours));
	}

	private int[] toDaysAndHours(GanttDiagram system, int value, String unit) {
		switch (unit.charAt(0)) {
		case 'H':
		case 'h':
			return new int[] {0, value};
		case 'D':
		case 'd':
			return new int[] {value, 0};
		case 'W':
		case 'w':
			return new int[] {value * system.daysInWeek(), 0};
		case 'M':
		case 'm':
			return new int[] {value * system.daysInMonth(), 0};
		default:
			throw new IllegalArgumentException("unknown time unit: " + unit);
		}
	}
}
