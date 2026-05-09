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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Resource;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;

public class SubjectResource implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectResource();

	private SubjectResource() {
	}

	public Failable<Resource> getMe(GanttDiagram gantt, RegexResult arg) {
		if (arg.get("THEY", 0) != null) {
			final Resource they = gantt.getThey();
			if (they == null)
				return Failable.error("Not sure who are you refering to?");
			return Failable.ok(they);
		}
		final String resource = arg.get("RESOURCE", 0);
		final Resource result = gantt.getResource(resource);
		gantt.setThey(result);
		return Failable.ok(result);
	}

	@Override
	public Failable<Resource> ugetMe(GanttDiagram gantt, UMatcher arg) {
		if (arg.get("THEY", 0) != null) {
			final Resource they = gantt.getThey();
			if (they == null)
				return Failable.error("Not sure who are you refering to?");
			return Failable.ok(they);
		}
		final String resource = arg.get("RESOURCE", 0);
		final Resource result = gantt.getResource(resource);
		gantt.setThey(result);
		return Failable.ok(result);
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();

		// WorksOn
		result.add(new VerbPhraseAction(Verbs.worksOn, new ComplementTask()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				final Task task = (Task) complement;
				task.addResource(resource, 100);
				return CommandExecutionResult.ok();
			}
		});

		// IsOffBeforeDate
		result.add(new VerbPhraseAction(Verbs.isOff,
				Words.uconcat(Words.usingle(Words.BEFORE), Words.uzeroOrMore(Words.THE)), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				final LocalDate when = (LocalDate) complement;
				resource.setOffBeforeDate(when);
				return CommandExecutionResult.ok();
			}
		});

		// IsOffAfterDate
		result.add(new VerbPhraseAction(Verbs.isOff,
				Words.uconcat(Words.usingle(Words.AFTER), Words.uzeroOrMore(Words.THE)), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				final LocalDate when = (LocalDate) complement;
				resource.setOffAfterDate(when);
				return CommandExecutionResult.ok();
			}
		});

		// IsOffDates
		result.add(new VerbPhraseAction(Verbs.isOff,
				Words.uzeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementIntervals()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				for (LocalDate when : (DaysAsDates) complement)
					resource.addCloseDay(when);

				return CommandExecutionResult.ok();
			}
		});

		// IsOffDayOfWeek
		result.add(new VerbPhraseAction(Verbs.isOff,
				Words.uzeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementDayOfWeek()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				resource.addCloseDay(((DayOfWeek) complement));
				return CommandExecutionResult.ok();
			}
		});

		// IsOffDate
		result.add(new VerbPhraseAction(Verbs.isOff,
				Words.uzeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				final LocalDate when = (LocalDate) complement;
				resource.addCloseDay(when);
				return CommandExecutionResult.ok();
			}
		});

		// IsOnDates
		result.add(new VerbPhraseAction(Verbs.isOn,
				Words.uzeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementIntervals()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				for (LocalDate when : (DaysAsDates) complement)
					resource.addForceOnDay(when);

				return CommandExecutionResult.ok();
			}
		});

		// IsOnDate
		result.add(new VerbPhraseAction(Verbs.isOn,
				Words.uzeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Resource resource = (Resource) subject;
				final LocalDate when = (LocalDate) complement;
				resource.addForceOnDay(when);
				return CommandExecutionResult.ok();
			}
		});

		return result;

	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new IsOffDate(), new IsOffDates(), new IsOffDayOfWeek(), new IsOnDate(), new IsOnDates(),
				new IsOffBeforeDate(), new IsOffAfterDate(), new WorksOn());
	}

	public IRegex toRegex() {
		return new RegexOr( //
				new RegexLeaf(1, "THEY", "(she|he|they)"), //
				new RegexLeaf(1, "RESOURCE", "\\{([^{}]+)\\}") //
		);
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexOr( //
				new UBrexNamed("THEY", new UBrexLeaf("【she┇he┇they】")), //
				UBrexConcat.build( //
						new UBrexLeaf("{"), //
						new UBrexNamed("RESOURCE", new UBrexLeaf("〇+「〤{}」")), //
						new UBrexLeaf("}")));
	}

	public class WorksOn extends SentenceSimple<GanttDiagram> {

		public WorksOn() {
			super(SubjectResource.this, Verbs.worksOn.getRegex(), new ComplementTask());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final Task task = (Task) complement;
			task.addResource(resource, 100);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffBeforeDate extends SentenceSimple<GanttDiagram> {

		public IsOffBeforeDate() {
			super(SubjectResource.this, Verbs.isOff.getRegex(),
					Words.concat(Words.exactly(Words.BEFORE), Words.zeroOrMore(Words.THE)), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final LocalDate when = (LocalDate) complement;
			resource.setOffBeforeDate(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffAfterDate extends SentenceSimple<GanttDiagram> {

		public IsOffAfterDate() {
			super(SubjectResource.this, Verbs.isOff.getRegex(),
					Words.concat(Words.exactly(Words.AFTER), Words.zeroOrMore(Words.THE)), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final LocalDate when = (LocalDate) complement;
			resource.setOffAfterDate(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffDate extends SentenceSimple<GanttDiagram> {

		public IsOffDate() {
			super(SubjectResource.this, Verbs.isOff.getRegex(),
					Words.zeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final LocalDate when = (LocalDate) complement;
			resource.addCloseDay(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOffDates extends SentenceSimple<GanttDiagram> {

		public IsOffDates() {
			super(SubjectResource.this, Verbs.isOff.getRegex(),
					Words.zeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementIntervals());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			for (LocalDate when : (DaysAsDates) complement)
				resource.addCloseDay(when);

			return CommandExecutionResult.ok();
		}

	}

	public class IsOffDayOfWeek extends SentenceSimple<GanttDiagram> {

		public IsOffDayOfWeek() {
			super(SubjectResource.this, Verbs.isOff.getRegex(),
					Words.zeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementDayOfWeek());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			resource.addCloseDay(((DayOfWeek) complement));
			return CommandExecutionResult.ok();
		}

	}

	public class IsOnDate extends SentenceSimple<GanttDiagram> {

		public IsOnDate() {
			super(SubjectResource.this, Verbs.isOn.getRegex(),
					Words.zeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), ComplementDate.any());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			final LocalDate when = (LocalDate) complement;
			resource.addForceOnDay(when);
			return CommandExecutionResult.ok();
		}

	}

	public class IsOnDates extends SentenceSimple<GanttDiagram> {

		public IsOnDates() {
			super(SubjectResource.this, Verbs.isOn.getRegex(),
					Words.zeroOrMore(Words.FROM, Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementIntervals());
		}

		@Override
		public CommandExecutionResult execute(GanttDiagram gantt, Object subject, Object complement) {
			final Resource resource = (Resource) subject;
			for (LocalDate when : (DaysAsDates) complement)
				resource.addForceOnDay(when);

			return CommandExecutionResult.ok();
		}

	}

}
