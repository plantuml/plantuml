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
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Today;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class SubjectToday implements Subject<GanttDiagram> {
	// ::remove folder when __HAXE__

	public static final Subject<GanttDiagram> ME = new SubjectToday();

	private SubjectToday() {
	}

	public IRegex toRegex() {
		return new RegexConcat( //
				new RegexLeaf("today") //
		);
	}

	public Failable<Today> getMe(GanttDiagram project, RegexResult arg) {
		return Failable.ok(new Today());
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new InColor(), new IsDate());
	}

	class InColor extends SentenceSimple<GanttDiagram> {

		public InColor() {
			super(SubjectToday.this, Verbs.isColored, new ComplementInColors());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Today task = (Today) subject;
			final CenterBorderColor colors = (CenterBorderColor) complement;
			project.setTodayColors(colors);
			return CommandExecutionResult.ok();

		}

	}

	class IsDate extends SentenceSimple<GanttDiagram> {

		public IsDate() {
			super(SubjectToday.this, Verbs.is, ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Day date = (Day) complement;
			return project.setToday(date);
		}

	}

}
