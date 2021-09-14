/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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

import java.util.Arrays;
import java.util.Collection;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class SubjectDayAsDate implements Subject {

	public Failable<Day> getMe(GanttDiagram project, RegexResult arg) {
		if (arg.get("BDAY", 0) != null) {
			return Failable.ok(resultB(arg));
		}
		if (arg.get("ECOUNT", 0) != null) {
			return Failable.ok(resultE(project, arg));
		}
		throw new IllegalStateException();

	}

	private Day resultB(RegexResult arg) {
		final int day = Integer.parseInt(arg.get("BDAY", 0));
		final int month = Integer.parseInt(arg.get("BMONTH", 0));
		final int year = Integer.parseInt(arg.get("BYEAR", 0));
		return Day.create(year, month, day);
	}

	private Day resultE(GanttDiagram system, RegexResult arg) {
		final int day = Integer.parseInt(arg.get("ECOUNT", 0));
		return system.getStartingDate().addDays(day);
	}

	public Collection<? extends SentenceSimple> getSentences() {
		return Arrays.asList(new Close(), new Open(), new InColor());
	}

	class Close extends SentenceSimple {

		public Close() {
			super(SubjectDayAsDate.this, Verbs.isOrAre(), new ComplementClose());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			project.closeDayAsDate((Day) subject);
			return CommandExecutionResult.ok();
		}
	}

	class Open extends SentenceSimple {
		public Open() {
			super(SubjectDayAsDate.this, Verbs.isOrAre(), new ComplementOpen());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			project.openDayAsDate((Day) subject);
			return CommandExecutionResult.ok();
		}
	}

	class InColor extends SentenceSimple {

		public InColor() {
			super(SubjectDayAsDate.this, Verbs.isOrAre(), new ComplementInColors2());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final HColor color = ((CenterBorderColor) complement).getCenter();
			project.colorDay((Day) subject, color);
			return CommandExecutionResult.ok();
		}

	}

	public IRegex toRegex() {
		return new RegexOr(toRegexB(), toRegexE());
	}

	private IRegex toRegexB() {
		return new RegexConcat( //
				new RegexLeaf("BYEAR", "([\\d]{4})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("BMONTH", "([\\d]{1,2})"), //
				new RegexLeaf("\\D"), //
				new RegexLeaf("BDAY", "([\\d]{1,2})"));
	}

	private IRegex toRegexE() {
		return new RegexConcat( //
				new RegexLeaf("[dD]\\+"), //
				new RegexLeaf("ECOUNT", "([\\d]+)") //
		);
	}

}
