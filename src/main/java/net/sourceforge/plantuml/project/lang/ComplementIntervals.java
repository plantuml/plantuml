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

import java.time.LocalDate;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementIntervals implements Something<GanttDiagram> {

	public IRegex toRegex(String suffix) {
		return new RegexOr(toRegexB(suffix), toRegexE(suffix));
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionComplement() {
		return new UBrexOr(toRegexB(), toRegexE());
	}

	private IRegex toRegexB(String suffix) {
		final DayPattern dayPattern1 = new DayPattern("1");
		final DayPattern dayPattern2 = new DayPattern("2");
		return new RegexConcat( //
				dayPattern1.toRegex(), //
				Words.exactly(Words.TO), //
				Words.zeroOrMore(Words.THE), //
				RegexLeaf.spaceOneOrMore(), //
				dayPattern2.toRegex() //
		);
	}

	private UBrexPart toRegexB() {
		final DayPattern dayPattern1 = new DayPattern("1");
		final DayPattern dayPattern2 = new DayPattern("2");
		return UBrexConcat.build( //
				dayPattern1.toUbrex(), //
				Words.uexactly(Words.TO), //
				Words.uzeroOrMore(Words.THE), //
				UBrexLeaf.spaceOneOrMore(), //
				dayPattern2.toUbrex() //
		);
	}

	private IRegex toRegexE(String suffix) {
		return new RegexConcat( //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf(1, "ECOUNT1" + suffix, "([\\d]+)"), //
				Words.exactly(Words.TO), //
				Words.zeroOrMore(Words.THE), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf(1, "ECOUNT2" + suffix, "([\\d]+)") //
		);
	}

	private UBrexPart toRegexE() {
		return UBrexConcat.build( //
				new UBrexLeaf("「dD」+"), //
				new UBrexNamed("ECOUNT1", new UBrexLeaf("〇+〴d")), //
				Words.uexactly(Words.TO), //
				Words.uzeroOrMore(Words.THE), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexLeaf("「dD」+"), //
				new UBrexNamed("ECOUNT2", new UBrexLeaf("〇+〴d")));
	}

	@Override
	public Failable<DaysAsDates> ugetMe(GanttDiagram project, UMatcher arg) {
		final LocalDate d1 = new DayPattern("1").getDay(arg);
		if (d1 != null) {
			final LocalDate d2 = new DayPattern("2").getDay(arg);
			return Failable.ok(new DaysAsDates(d1, d2));
		}

		if (arg.get("ECOUNT1", 0) != null)
			return Failable.ok(resultE(project, arg));

		throw new IllegalStateException();
	}

	public Failable<DaysAsDates> getMe(GanttDiagram project, RegexResult arg, String suffix) {
		final LocalDate d1 = new DayPattern("1").getDay(arg);
		if (d1 != null) {
			final LocalDate d2 = new DayPattern("2").getDay(arg);
			return Failable.ok(new DaysAsDates(d1, d2));
		}

		if (arg.get("ECOUNT1" + suffix, 0) != null)
			return Failable.ok(resultE(project, arg, suffix));

		throw new IllegalStateException();

	}

	private DaysAsDates resultE(GanttDiagram project, RegexResult arg, String suffix) {
		final int day1 = Integer.parseInt(arg.get("ECOUNT1" + suffix, 0));
		final TimePoint date1 = project.getMinTimePoint().addDays(day1);

		final int day2 = Integer.parseInt(arg.get("ECOUNT2" + suffix, 0));
		final TimePoint date2 = project.getMinTimePoint().addDays(day2);

		return new DaysAsDates(date1.toDay(), date2.toDay());
	}

	private DaysAsDates resultE(GanttDiagram project, UMatcher arg) {
		final int day1 = Integer.parseInt(arg.get("ECOUNT1", 0));
		final TimePoint date1 = project.getMinTimePoint().addDays(day1);

		final int day2 = Integer.parseInt(arg.get("ECOUNT2", 0));
		final TimePoint date2 = project.getMinTimePoint().addDays(day2);

		return new DaysAsDates(date1.toDay(), date2.toDay());
	}

}
