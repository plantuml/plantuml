/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.project.lang;

import net.sourceforge.plantuml.command.regex.*;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;

import java.time.Period;

public class ComplementSeveralDays implements Something {

	public IRegex toRegex(String suffix) {
		return new RegexConcat(
			new RegexLeaf(
				getKey(suffix),
				"(\\d+)[%s]+(day|week)s?(?:[%s]+and[%s]+(\\d+)[%s]+(day|week)s?)?"
			)
		);
	}

	public Failable<Load> getMe(GanttDiagram diagram, RegexResult arg, String suffix) {
		RegexPartialMatch partialRegexpMatch = arg.get(getKey(suffix));

		final Period period = getPeriod(diagram, partialRegexpMatch, 0);
		final Period period2 = getPeriod(diagram, partialRegexpMatch, 1);

		return Failable.ok(Load.inWinks(period.getDays() + period2.getDays()));
	}

	private static Period getPeriod(GanttDiagram diagram, RegexPartialMatch regexp, int periodIndex) {
		if (!hasPeriod(regexp, periodIndex)) {
			return Period.ZERO;
		}

		final int amount = Integer.parseInt(getAmount(regexp, periodIndex));
		final int workweekLength = getChronoUnit(regexp, periodIndex).startsWith("w") ? diagram.daysInWeek() : 1;
		final int days = amount * workweekLength;
		return Period.ofDays(days);
	}

	private static boolean hasPeriod(RegexPartialMatch regexp, int periodIndex) {
		return getAmount(regexp, periodIndex) != null;
	}

	private static String getAmount(RegexPartialMatch regexp, int periodIndex) {
		return regexp.get(indexToOffset(periodIndex));
	}

	private static String getChronoUnit(RegexPartialMatch regexp, int periodIndex) {
		return regexp.get(indexToOffset(periodIndex) + 1);
	}

	private static int indexToOffset(int periodIndex) {
		return periodIndex * 2;
	}

	private static String getKey(String suffix) {
		return "COMPLEMENT" + suffix;
	}
}
