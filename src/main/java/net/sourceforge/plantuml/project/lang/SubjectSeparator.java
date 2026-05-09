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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.teavm.TeaVM;

public class SubjectSeparator implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectSeparator();

	private SubjectSeparator() {
	}

	public IRegex toRegex() {
		return new RegexLeaf(0, "SUBJECT", "separator");
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexLeaf("separator");
	}

	public Failable<GanttDiagram> getMe(GanttDiagram project, RegexResult arg) {
		return Failable.ok(project);
	}

	@Override
	public Failable<GanttDiagram> ugetMe(GanttDiagram project, UMatcher arg) {
		return Failable.ok(project);
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new JustBefore(), new JustAfter(), new Just());
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();

		result.add(new VerbPhraseAction(Verbs.just, Words.usingle(Words.BEFORE), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final LocalDate day = (LocalDate) complement;
				if (TeaVM.a())
					assert project == subject;
				project.addVerticalSeparatorBefore(day);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.just, Words.usingle(Words.AFTER), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final LocalDate day = (LocalDate) complement;
				if (TeaVM.a())
					assert project == subject;
				project.addVerticalSeparatorBefore(day.plusDays(1));
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.just, new ComplementBeforeOrAfterOrAtTaskStartOrEnd()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final TaskInstant when = (TaskInstant) complement;

				if (TeaVM.a())
					assert project == subject;
				project.addVerticalSeparatorBefore(when.getInstantPrecise().toDay());
				return CommandExecutionResult.ok();
			}
		});

		return result;

	}

	class JustBefore extends SentenceSimple<GanttDiagram> {

		public JustBefore() {
			super(SubjectSeparator.this, Verbs.just.getRegex(), Words.exactly(Words.BEFORE), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final LocalDate day = (LocalDate) complement;
			if (TeaVM.a())
				assert project == subject;
			project.addVerticalSeparatorBefore(day);
			return CommandExecutionResult.ok();
		}

	}

	class JustAfter extends SentenceSimple<GanttDiagram> {

		public JustAfter() {
			super(SubjectSeparator.this, Verbs.just.getRegex(), Words.exactly(Words.AFTER), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final LocalDate day = (LocalDate) complement;
			if (TeaVM.a())
				assert project == subject;
			project.addVerticalSeparatorBefore(day.plusDays(1));
			return CommandExecutionResult.ok();
		}

	}

	class Just extends SentenceSimple<GanttDiagram> {

		public Just() {
			super(SubjectSeparator.this, Verbs.just.getRegex(), new ComplementBeforeOrAfterOrAtTaskStartOrEnd());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
			final TaskInstant when = (TaskInstant) complement;

			if (TeaVM.a())
				assert project == subject;
			project.addVerticalSeparatorBefore(when.getInstantPrecise().toDay());
			return CommandExecutionResult.ok();
		}

	}

}
