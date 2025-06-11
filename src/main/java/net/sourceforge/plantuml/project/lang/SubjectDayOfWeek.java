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
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.time.DayOfWeek;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class SubjectDayOfWeek implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectDayOfWeek();

	private SubjectDayOfWeek() {
	}

	public IRegex toRegex() {
		return new RegexLeaf(1, "SUBJECT", "(" + DayOfWeek.getRegexString() + ")");
	}

	public Failable<? extends Object> getMe(GanttDiagram project, RegexResult arg) {
		final String s = arg.get("SUBJECT", 0);
		return Failable.ok(DayOfWeek.fromString(s));
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new AreClose(), new AreOpen(), new InColor());
	}

	class AreOpen extends SentenceSimple<GanttDiagram> {
		public AreOpen() {
			super(SubjectDayOfWeek.this, Verbs.are, new ComplementOpen());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final DayOfWeek day = (DayOfWeek) subject;
			project.openDayOfWeek(day, (String) complement);
			return CommandExecutionResult.ok();
		}
	}

	class AreClose extends SentenceSimple<GanttDiagram> {

		public AreClose() {
			super(SubjectDayOfWeek.this, Verbs.are, new ComplementClose());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final DayOfWeek day = (DayOfWeek) subject;
			project.closeDayOfWeek(day, (String) complement);
			return CommandExecutionResult.ok();
		}

	}

	class InColor extends SentenceSimple<GanttDiagram> {

		public InColor() {
			super(SubjectDayOfWeek.this, Verbs.isOrAre, new ComplementInColors2());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final HColor color = ((CenterBorderColor) complement).getCenter();
			final DayOfWeek day = (DayOfWeek) subject;
			project.colorDay(day, color);

			return CommandExecutionResult.ok();
		}

	}

}
