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
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class SubjectSeparator implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectSeparator();

	private SubjectSeparator() {
	}

	public IRegex toRegex() {
		return new RegexLeaf("SUBJECT", "separator");
	}

	public Failable<GanttDiagram> getMe(GanttDiagram project, RegexResult arg) {
		return Failable.ok(project);
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new JustBefore(), new JustAfter(), new Just());
	}

	class JustBefore extends SentenceSimple<GanttDiagram> {

		public JustBefore() {
			super(SubjectSeparator.this, Verbs.just, Words.exactly(Words.BEFORE), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Day day = (Day) complement;
			assert project == subject;
			project.addVerticalSeparatorBefore(day);
			return CommandExecutionResult.ok();
		}

	}

	class JustAfter extends SentenceSimple<GanttDiagram> {

		public JustAfter() {
			super(SubjectSeparator.this, Verbs.just, Words.exactly(Words.AFTER), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final Day day = (Day) complement;
			assert project == subject;
			project.addVerticalSeparatorBefore(day.increment());
			return CommandExecutionResult.ok();
		}

	}

	class Just extends SentenceSimple<GanttDiagram> {

		public Just() {
			super(SubjectSeparator.this, Verbs.just, new ComplementBeforeOrAfterOrAtTaskStartOrEnd());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final TaskInstant when = (TaskInstant) complement;

			assert project == subject;
			project.addVerticalSeparatorBefore(when.getInstantPrecise());
			return CommandExecutionResult.ok();
		}

	}

}
