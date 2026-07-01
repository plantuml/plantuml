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
package net.sourceforge.plantuml.gantt.lang;

import com.plantuml.ubrex.CaptureLookup;

import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.gantt.GanttConstraintMode;
import net.sourceforge.plantuml.gantt.GanttDiagram;
import net.sourceforge.plantuml.gantt.core.Moment;
import net.sourceforge.plantuml.gantt.core.TaskAttribute;
import net.sourceforge.plantuml.gantt.core.TaskInstant;

abstract class AbstractComplementTaskInstant implements Something<GanttDiagram> {

	final public Failable<TaskInstant> getComplementTaskInstant(GanttDiagram gantt, CaptureLookup arg) {
		final String code = arg.findFirstValueByKey("COMPLEMENT_CODE_OTHER");
		final String startOrEnd = arg.findFirstValueByKey("COMPLEMENT_START_OR_END");

		final Moment task = gantt.getExistingMoment(code);
		if (task == null)
			return Failable.error("No such task " + code);

		TaskInstant result = new TaskInstant(task, TaskAttribute.fromString(startOrEnd));
		final String nb1 = arg.findFirstValueByKey("COMPLEMENT_NB1");
		if (nb1 != null) {
			final int factor1 = arg.findFirstValueByKey("COMPLEMENT_DAY_OR_WEEK1").startsWith("w") ? gantt.daysInWeek() : 1;
			final int days1 = Integer.parseInt(nb1) * factor1;

			final String nb2 = arg.findFirstValueByKey("COMPLEMENT_NB2");
			int days2 = 0;
			if (nb2 != null) {
				final int factor2 = arg.findFirstValueByKey("COMPLEMENT_DAY_OR_WEEK2").startsWith("w") ? gantt.daysInWeek()
						: 1;
				days2 = Integer.parseInt(nb2) * factor2;
			}

			int delta = days1 + days2;
			if ("before".equalsIgnoreCase(arg.findFirstValueByKey("COMPLEMENT_BEFORE_OR_AFTER")))
				delta = -delta;

			final boolean working = arg.findFirstValueByKey("COMPLEMENT_WORKING1") != null
					|| arg.findFirstValueByKey("COMPLEMENT_WORKING2") != null;

			final GanttConstraintMode mode = working ? GanttConstraintMode.DO_NOT_COUNT_CLOSE_DAY
					: GanttConstraintMode.IGNORE_CALENDAR;

			result = result.withDelta(delta, mode, gantt.getDefaultPlan());
		}
		return Failable.ok(result);
	}

}
