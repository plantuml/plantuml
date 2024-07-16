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

import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Moment;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementIntervalsSmart implements Something<GanttDiagram> {

	public IRegex toRegex(String suffix) {
		return toRegexB(suffix);
	}

	private IRegex toRegexB(String suffix) {
		final DayPattern dayPattern1 = new DayPattern("1");
		return new RegexConcat( //
				dayPattern1.toRegex(), //
				Words.exactly(Words.TO), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("TASKREF", "\\[([^\\[\\]]+?)\\]"), //
				new RegexLeaf("TASKBOUND", ".?s[%s]+(start|end)") //
		);
	}

	public Failable<DaysAsDates> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final Day d1 = new DayPattern("1").getDay(arg);

		final String code = arg.get("TASKREF", 0);
		final Moment task = system.getExistingMoment(code);
		if (task == null)
			return Failable.error("No such task " + code);

		final String startOrEnd = arg.get("TASKBOUND", 0);
		final TaskInstant result = new TaskInstant(task, TaskAttribute.fromString(startOrEnd));
		return Failable.ok(new DaysAsDates(d1, result.getInstantTheorical()));

	}

}
