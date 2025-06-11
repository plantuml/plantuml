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

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class SubjectDaysAsDates implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectDaysAsDates();

	private SubjectDaysAsDates() {
	}

	public IRegex toRegex() {
		return new RegexOr(toRegexB(), toRegexE(), andRegex(), thenRegex());
	}

	private IRegex toRegexB() {
		return new RegexConcat( //
				TimeResolution.toRegexB_YYYY_MM_DD("BYEAR1", "BMONTH1", "BDAY1"), //
				Words.exactly(Words.TO), //
				RegexLeaf.spaceOneOrMore(), //
				TimeResolution.toRegexB_YYYY_MM_DD("BYEAR2", "BMONTH2", "BDAY2") //
		);
	}

	private IRegex toRegexE() {
		return new RegexConcat( //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf(1, "ECOUNT1", "([\\d]+)"), //
				Words.exactly(Words.TO), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf(1, "ECOUNT2", "([\\d]+)") //
		);
	}

	private IRegex andRegex() {
		return new RegexConcat( //
				TimeResolution.toRegexB_YYYY_MM_DD("BYEAR3", "BMONTH3", "BDAY3"), //
				Words.exactly(Words.AND), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "COUNT_AND", "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?") //

		);
	}

	private IRegex thenRegex() {
		return new RegexConcat( //
				new RegexLeaf("then"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "COUNT_THEN", "([\\d]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("days?") //

		);
	}

	public Failable<DaysAsDates> getMe(GanttDiagram project, RegexResult arg) {
		final String countAnd = arg.get("COUNT_AND", 0);
		if (countAnd != null) {
			final Day date3 = getDate(project, arg, "3");
			final int nb = Integer.parseInt(countAnd);
			return Failable.ok(new DaysAsDates(project, date3, nb));
		}
		final String countThen = arg.get("COUNT_THEN", 0);
		if (countThen != null) {
			final Day date3 = project.getThenDate();
			final int nb = Integer.parseInt(countThen);
			return Failable.ok(new DaysAsDates(project, date3, nb));
		}
		final Day date1 = getDate(project, arg, "1");
		final Day date2 = getDate(project, arg, "2");
		return Failable.ok(new DaysAsDates(date1, date2));
	}

	private Day getDate(GanttDiagram project, RegexResult arg, String suffix) {
		if (arg.get("BDAY" + suffix, 0) != null) {
			final int day = Integer.parseInt(arg.get("BDAY" + suffix, 0));
			final int month = Integer.parseInt(arg.get("BMONTH" + suffix, 0));
			final int year = Integer.parseInt(arg.get("BYEAR" + suffix, 0));
			return Day.create(year, month, day);
		}
		if (arg.get("ECOUNT" + suffix, 0) != null) {
			final int day = Integer.parseInt(arg.get("ECOUNT" + suffix, 0));
			return project.getStartingDate().addDays(day);
		}
		throw new IllegalStateException();
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new Close(), new Open(), new InColor(), new Named());
	}

	class Close extends SentenceSimple<GanttDiagram> {

		public Close() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre, new ComplementClose());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			for (Day d : (DaysAsDates) subject)
				project.closeDayAsDate(d, (String) complement);

			return CommandExecutionResult.ok();

		}
	}

	class Open extends SentenceSimple<GanttDiagram> {

		public Open() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre, new ComplementOpen());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			for (Day d : (DaysAsDates) subject)
				project.openDayAsDate(d, (String) complement);

			return CommandExecutionResult.ok();

		}

	}

	class InColor extends SentenceSimple<GanttDiagram> {

		public InColor() {
			super(SubjectDaysAsDates.this, Verbs.isOrAre, new ComplementInColors2());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final HColor color = ((CenterBorderColor) complement).getCenter();
			for (Day d : (DaysAsDates) subject)
				project.colorDay(d, color);

			return CommandExecutionResult.ok();

		}

	}

	class Named extends SentenceSimple<GanttDiagram> {

		public Named() {
			super(SubjectDaysAsDates.this, Verbs.isOrAreNamed, new ComplementNamed());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final String name = (String) complement;
			final DaysAsDates days = (DaysAsDates) subject;
			for (Day d : days) {
				project.nameDay(d, name);
			}
			return CommandExecutionResult.ok();
		}

	}

}
