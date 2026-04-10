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

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOptional;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.annotation.DuplicateCode;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementBeforeOrAfterOrAtTaskStartOrEnd extends AbstractComplementTaskInstant {

	@DuplicateCode(reference = "ComplementIntervalsSmart")
	public IRegex toRegex(String suffix) { // "+"
		return new RegexConcat( //
				new RegexOptional(new RegexOr( //
						Words.single(Words.AT), //
						Words.single(Words.WITH), //
						Words.single(Words.AFTER), //
						new RegexConcat( //
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
								Words.namedOneOf("COMPLEMENT_BEFORE_OR_AFTER" + suffix, Words.BEFORE, Words.AFTER)))), //
				//
				RegexLeaf.spaceOneOrMore(),
				//
				new RegexLeaf(1, "COMPLEMENT_CODE_OTHER" + suffix, SubjectTask.REGEX_TASK_CODE + ".?s"), //
				RegexLeaf.spaceOneOrMore(), //
				Words.namedOneOf("COMPLEMENT_START_OR_END" + suffix, Words.START, Words.END));
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionComplement() {
		final UBrexPart durationBeforeOrAfter = UBrexConcat.build( //
				new UBrexNamed("COMPLEMENT_NB1", new UBrexLeaf("〇+〴d")), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexOptional(UBrexConcat.build(new UBrexNamed("COMPLEMENT_WORKING1", new UBrexLeaf("working")),
						UBrexLeaf.spaceOneOrMore())), //
				new UBrexNamed("COMPLEMENT_DAY_OR_WEEK1", new UBrexLeaf("【day┇week】")), //
				new UBrexLeaf("〇?s"), //
				new UBrexOptional(UBrexConcat.build( //
						UBrexLeaf.spaceOneOrMore(), //
						new UBrexLeaf("and"), //
						UBrexLeaf.spaceOneOrMore(), //
						new UBrexNamed("COMPLEMENT_NB2", new UBrexLeaf("〇+〴d")), //
						UBrexLeaf.spaceOneOrMore(), //

						new UBrexOptional(
								UBrexConcat.build(new UBrexNamed("COMPLEMENT_WORKING2", new UBrexLeaf("working")),
										UBrexLeaf.spaceOneOrMore())), //
						new UBrexNamed("COMPLEMENT_DAY_OR_WEEK2", new UBrexLeaf("【day┇week】")), //
						new UBrexLeaf("〇?s"))), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexNamed("COMPLEMENT_BEFORE_OR_AFTER", Words.uoneOf(Words.BEFORE, Words.AFTER)));

		return UBrexConcat.build( //
				new UBrexOptional(new UBrexOr( //
						new UBrexLeaf(Words.AT), //
						new UBrexLeaf(Words.WITH), //
						new UBrexLeaf(Words.AFTER), //
						durationBeforeOrAfter)), //
				UBrexLeaf.spaceZeroOrMore(), //
				UBrexConcat.build( //
						SubjectTask.taskCode("COMPLEMENT_CODE_OTHER"), //
						new UBrexLeaf("〴.s")), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexNamed("COMPLEMENT_START_OR_END", Words.uoneOf(Words.START, Words.END)));
	}

	@Override
	public Failable<TaskInstant> ugetMe(GanttDiagram system, UMatcher arg) {
		return getComplementTaskInstant(system, arg);
	}

	public Failable<TaskInstant> getMe(GanttDiagram system, RegexResult arg, String suffix) {
		return getComplementTaskInstant(system, arg, suffix);
	}
}
