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

import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementIntervalsSmart extends AbstractComplementTaskInstant {

	@DuplicateCode(reference = "ComplementBeforeOrAfterOrAtTaskStartOrEnd")
	public IRegex toRegex(String suffix) {
		final DayPattern dayPattern1 = new DayPattern("1");
		return new RegexConcat( //
				dayPattern1.toRegex(), //
				Words.exactly(Words.TO), //
				RegexLeaf.spaceZeroOrMore(), //
				//
				new RegexOptional(new RegexConcat( //
						new RegexLeaf(1, "COMPLEMENT_NB1" + suffix, "(\\d+)"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "COMPLEMENT_WORKING1" + suffix, "(working[%s]+)?"),
						new RegexLeaf(1, "COMPLEMENT_DAY_OR_WEEK1" + suffix, "(day|week)s?"),
						new RegexOptional(new RegexConcat(//
								Words.exactly(Words.AND), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "COMPLEMENT_NB2" + suffix, "(\\d+)"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "COMPLEMENT_WORKING2" + suffix, "(working[%s]+)?"),
								new RegexLeaf(1, "COMPLEMENT_DAY_OR_WEEK2" + suffix, "(day|week)s?"))),
						RegexLeaf.spaceOneOrMore(), //
						Words.namedOneOf("COMPLEMENT_BEFORE_OR_AFTER" + suffix, Words.BEFORE, Words.AFTER))),
				//
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "COMPLEMENT_CODE_OTHER" + suffix, SubjectTask.REGEX_TASK_CODE + ".?s"), //
				RegexLeaf.spaceOneOrMore(), //
				Words.namedOneOf("COMPLEMENT_START_OR_END" + suffix, Words.START, Words.END));
	}

	public Failable<DaysAsDates> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		final Day d1 = new DayPattern("1").getDay(arg);

		final Failable<TaskInstant> i2 = getComplementTaskInstant(system, arg, suffix);

		if (i2.isFail())
			return Failable.error(i2.getError());

		return Failable.ok(new DaysAsDates(d1, i2.get().getInstantTheorical()));

	}

}
