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
import net.sourceforge.plantuml.project.GanttConstraintMode;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Moment;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementBeforeOrAfterOrAtTaskStartOrEnd implements Something<GanttDiagram> {

	private static final int POS_NB1 = 0;
	private static final int POS_WORKING1 = 1;
	private static final int POS_DAY_OR_WEEK1 = 2;
	private static final int POS_NB2 = 3;
	private static final int POS_WORKING2 = 4;
	private static final int POS_DAY_OR_WEEK2 = 5;
	private static final int POS_BEFORE_OR_AFTER = 6;
	private static final int POS_CODE_OTHER = 7;
	private static final int POS_START_OR_END = 8;

	public IRegex toRegex(String suffix) { // "+"
		return new RegexLeaf("COMPLEMENT" + suffix, "(?:at|with|after|" + //
				"(\\d+)[%s]+(working[%s]+)?(day|week)s?" + //
				"(?:[%s]+and[%s]+(\\d+)[%s]+(working[%s]+)?(day|week)s?)?" + //
				"[%s]+(before|after))[%s]+\\[([^\\[\\]]+?)\\].?s[%s]+(start|end)");
	}

	public Failable<TaskInstant> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final String code = arg.get("COMPLEMENT" + suffix, POS_CODE_OTHER);
		final String startOrEnd = arg.get("COMPLEMENT" + suffix, POS_START_OR_END);
		final Moment task = system.getExistingMoment(code);
		if (task == null)
			return Failable.error("No such task " + code);

		TaskInstant result = new TaskInstant(task, TaskAttribute.fromString(startOrEnd));
		final String nb1 = arg.get("COMPLEMENT" + suffix, POS_NB1);
		if (nb1 != null) {
			final int factor1 = arg.get("COMPLEMENT" + suffix, POS_DAY_OR_WEEK1).startsWith("w") ? system.daysInWeek()
					: 1;
			final int days1 = Integer.parseInt(nb1) * factor1;

			final String nb2 = arg.get("COMPLEMENT" + suffix, POS_NB2);
			int days2 = 0;
			if (nb2 != null) {
				final int factor2 = arg.get("COMPLEMENT" + suffix, POS_DAY_OR_WEEK2).startsWith("w")
						? system.daysInWeek()
						: 1;
				days2 = Integer.parseInt(nb2) * factor2;
			}

			int delta = days1 + days2;
			if ("before".equalsIgnoreCase(arg.get("COMPLEMENT" + suffix, POS_BEFORE_OR_AFTER)))
				delta = -delta;

			final boolean working = arg.get("COMPLEMENT" + suffix, POS_WORKING1) != null
					|| arg.get("COMPLEMENT" + suffix, POS_WORKING2) != null;

			final GanttConstraintMode mode = working ? GanttConstraintMode.DO_NOT_COUNT_CLOSE_DAY
					: GanttConstraintMode.IGNORE_CALENDAR;

			result = result.withDelta(delta, mode, system.getDefaultPlan());
		}
		return Failable.ok(result);
	}
}
