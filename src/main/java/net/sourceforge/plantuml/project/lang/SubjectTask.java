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
import java.util.StringTokenizer;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOneOrMore;
import com.plantuml.ubrex.builder.UBrexOptional;
import com.plantuml.ubrex.builder.UBrexOr;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.project.Completion;
import net.sourceforge.plantuml.project.DaysAsDates;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.time.TimePoint;
import net.sourceforge.plantuml.project.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;

public class SubjectTask implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectTask();
	public static final String REGEX_TASK_CODE = "\\[([^\\[\\]]+?)\\]";

	public static UBrexPart taskCode(String name) {
		return UBrexConcat.build( //
				new UBrexLeaf("["), //
				new UBrexNamed(name, new UBrexLeaf("〇+「〤[]」")), //
				new UBrexLeaf("]"));
	}

	private SubjectTask() {
	}

	@Override
	public Collection<VerbPhraseAction> getVerbPhrases() {
		final List<VerbPhraseAction> result = new ArrayList<>();
		result.add(new VerbPhraseAction(Verbs.requires, Words.uzeroOrMore(Words.ON, Words.FOR, Words.THE, Words.AT),
				new ComplementDuration()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final Load duration = (Load) complement;
				task.setLoad(duration);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.starts,
				new PairOfSomething<>(new ComplementBeforeOrAfterOrAtTaskStartOrEnd(), new ComplementWithColorLink())) {
			@Override
			public CommandExecutionResult execute(GanttDiagram diagram, Object subject, Object complement) {
				final Task task = (Task) subject;
				final TaskInstant when;

				final Object[] pairs = (Object[]) complement;
				when = (TaskInstant) pairs[0];
				final CenterBorderColor complement22 = (CenterBorderColor) pairs[1];

				task.setStart(when.getInstantPrecise());
				if (when.isTask()) {
					final HColor color = complement22.getCenter();
					final GanttConstraint link = new GanttConstraint(diagram.getIHtmlColorSet(),
							diagram.getCurrentStyleBuilder(), when, new TaskInstant(task, TaskAttribute.START), color);
					link.applyStyle(complement22.getStyle());
					diagram.addContraint(link);
				}
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.starts, new ComplementBeforeOrAfterOrAtTaskStartOrEnd()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final TaskInstant when;
				HColor color = null;
				when = (TaskInstant) complement;
				task.setStart(when.getInstantPrecise());
				if (when.isTask())
					project.addContraint(new GanttConstraint(project.getIHtmlColorSet(),
							project.getCurrentStyleBuilder(), when, new TaskInstant(task, TaskAttribute.START), color));

				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.starts, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				ComplementDate.onlyRelative()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final LocalDate start = (LocalDate) complement;

				task.setStart(TimePoint.ofStartOfDay(start));
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.starts, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final LocalDate start = (LocalDate) complement;
				if (project.getMinDay().equals(TimePoint.epoch()))
					return CommandExecutionResult.error("No starting date for the project");

				task.setStart(TimePoint.ofStartOfDay(start));
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isColored, new ComplementInColors()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final CenterBorderColor colors = (CenterBorderColor) complement;
				task.setColors(colors);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isColored, Words.uexactly2(Words.FOR, Words.COMPLETION),
				new ComplementInColorsFromTo()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final CenterBorderColor[] colors = (CenterBorderColor[]) complement;
				task.setColors(colors);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.happens, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				task.setLoad(Load.ofDays(1));
				final LocalDate start = (LocalDate) complement;
				task.setStart(TimePoint.ofStartOfDay(start));
				task.setDiamond(true);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.happens, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				new ComplementBeforeOrAfterOrAtTaskStartOrEnd()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				task.setLoad(Load.ofDays(1));
				task.setDiamond(true);
				final TaskInstant when = (TaskInstant) complement;
				if (when.getAttribute() == TaskAttribute.END)
					task.setStart(when.getInstantPrecise().decrement());
				else
					task.setStart(when.getInstantPrecise());
				return CommandExecutionResult.ok();

			}
		});

		result.add(new VerbPhraseAction(Verbs.occurs, new ComplementFromTo()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final TwoNames bothNames = (TwoNames) complement;
				final String name1 = bothNames.getName1();
				final String name2 = bothNames.getName2();
				final Task from = project.getExistingTask(name1);
				if (from == null)
					return CommandExecutionResult.error("No such " + name1 + " task");

				final Task to = project.getExistingTask(name2);
				if (to == null)
					return CommandExecutionResult.error("No such " + name2 + " task");

				task.setStart(from.getStart());
				task.setEnd(to.getEnd());
				project.addContraint(new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
						new TaskInstant(from, TaskAttribute.START), new TaskInstant(task, TaskAttribute.START)));
				project.addContraint(new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
						new TaskInstant(to, TaskAttribute.END), new TaskInstant(task, TaskAttribute.END)));
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.ends, new ComplementBeforeOrAfterOrAtTaskStartOrEnd()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final TaskInstant when = (TaskInstant) complement;
				task.setEnd(when.getInstantPrecise());
				project.addContraint(new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
						when, new TaskInstant(task, TaskAttribute.END)));
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.ends, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				ComplementDate.onlyRelative()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final LocalDate end = (LocalDate) complement;

				task.setEnd(TimePoint.ofEndOfDayMinusOneSecond(end).increment());
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.ends, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT),
				ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final LocalDate end = (LocalDate) complement;
				if (project.getMinDay().equals(TimePoint.epoch()))
					return CommandExecutionResult.error("No starting date for the project");
				task.setEnd(TimePoint.ofStartOfDay(end).increment());

				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.displayOnSameRowAs, new ComplementNamed()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task1 = (Task) subject;
				final Task task2 = project.getExistingTask((String) complement);
				if (task2 == null)
					return CommandExecutionResult.error("No such task " + task2);

				task1.putInSameRowAs(task2);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.is, new ComplementDeleted()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				return project.deleteTask(task);
			}
		});

		result.add(new VerbPhraseAction(Verbs.is, new ComplementCompleted()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final Completion completed = (Completion) complement;
				task.setCompletion(completed.getCompletion());
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.pauses, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT, Words.FROM),
				new ComplementIntervals()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final DaysAsDates pauses = (DaysAsDates) complement;
				for (LocalDate day : pauses)
					task.addPause(day);

				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.pauses, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT, Words.FROM),
				new ComplementIntervalsSmart()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final DaysAsDates pauses = (DaysAsDates) complement;
				for (LocalDate day : pauses)
					task.addPause(day);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.pauses, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT, Words.FROM),
				ComplementDate.any()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final LocalDate pause = (LocalDate) complement;
				task.addPause(pause);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.pauses, Words.uzeroOrMore(Words.THE, Words.ON, Words.AT, Words.FROM),
				new ComplementDayOfWeek()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final DayOfWeek pause = (DayOfWeek) complement;
				task.addPause(pause);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.linksTo, new ComplementUrl()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final Url url = (Url) complement;
				task.setUrl(url);
				return CommandExecutionResult.ok();
			}
		});

		result.add(new VerbPhraseAction(Verbs.isDisplayedAs, new ComplementAnything()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final String displayString = (String) complement;
				task.setDisplay(displayString);
				return CommandExecutionResult.ok();
			}
		});

		return result;

	}

	public IRegex toRegex() {
		return new RegexOr( //
				new RegexLeaf(1, "IT", "(it)"), //
				new RegexConcat(new RegexLeaf(1, "THEN", "(then[%s]+)?"), //
						new RegexLeaf(1, "SUBJECT", REGEX_TASK_CODE), //
						StereotypePattern.optional("STEREOTYPE"), //
						new RegexOptional(new RegexConcat(//
								Words.exactly(Words.AS), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "SHORTNAME", REGEX_TASK_CODE))), //
						new RegexOptional(new RegexConcat( //
								Words.exactly(Words.ON), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "RESOURCE", "((?:\\{[^{}]+\\}[%s]*)+)") //
						))));
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionSubject() {
		return new UBrexOr( //
				new UBrexNamed("IT", Words.usingle(Words.IT)), //
				UBrexConcat.build( //
						new UBrexOptional(UBrexConcat.build( //
								new UBrexNamed("THEN", Words.usingle(Words.THEN)), //
								UBrexLeaf.spaceOneOrMore())), //
						SubjectTask.taskCode("SUBJECT"), //
						StereotypePattern.uoptional("STEREOTYPE"), //
						new UBrexOptional(UBrexConcat.build( //
								Words.uexactly(Words.AS), //
								UBrexLeaf.spaceOneOrMore(), //
								SubjectTask.taskCode("SHORTNAME"))), //
						new UBrexOptional(UBrexConcat.build( //
								Words.uexactly(Words.ON), //
								UBrexLeaf.spaceOneOrMore(), //
								new UBrexNamed("RESOURCE", new UBrexOneOrMore( //
										UBrexConcat.build( //
												new UBrexLeaf("{〇+「〤{}」}"), //
												UBrexLeaf.spaceZeroOrMore() //
										)))) //
						) //
				));
	}

	@Override
	public Failable<Task> ugetMe(GanttDiagram gantt, UMatcher arg) {
		final Task result;
		if (arg.get("IT", 0) != null) {
			result = gantt.getIt();
			if (result == null)
				return Failable.error("Not sure what are you refering to?");
		} else {
			final String subject = arg.get("SUBJECT", 0);
			final String shortName = arg.get("SHORTNAME", 0);
			final String then = arg.get("THEN", 0);
			final String stereotype = arg.get("STEREOTYPE", 0);

			final TaskCode code = TaskCode.fromIdAndDisplay(shortName, subject);
			result = gantt.getOrCreateTask(code, then != null);

			if (stereotype != null)
				result.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

			gantt.setIt(result);
		}

		if (result == null)
			throw new IllegalStateException();

		final String resource = arg.get("RESOURCE", 0);
		if (resource != null) {
			for (final StringTokenizer st = new StringTokenizer(resource, "{}"); st.hasMoreTokens();) {
				final String part = st.nextToken().trim();
				if (part.length() > 0) {
					final boolean ok = gantt.affectResource(result, part);
					if (ok == false)
						return Failable.error("Bad argument for resource");

				}
			}

		}
		return Failable.ok(result);

	}

	public Failable<Task> getMe(GanttDiagram gantt, RegexResult arg) {
		final Task result;
		if (arg.get("IT", 0) != null) {
			result = gantt.getIt();
			if (result == null)
				return Failable.error("Not sure what are you refering to?");
		} else {
			final String subject = arg.get("SUBJECT", 0);
			final String shortName = arg.get("SHORTNAME", 0);
			final String then = arg.get("THEN", 0);
			final String stereotype = arg.get("STEREOTYPE", 0);

			final TaskCode code = TaskCode.fromIdAndDisplay(shortName, subject);
			result = gantt.getOrCreateTask(code, then != null);

			if (stereotype != null)
				result.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

			gantt.setIt(result);
		}

		if (result == null)
			throw new IllegalStateException();

		final String resource = arg.get("RESOURCE", 0);
		if (resource != null) {
			for (final StringTokenizer st = new StringTokenizer(resource, "{}"); st.hasMoreTokens();) {
				final String part = st.nextToken().trim();
				if (part.length() > 0) {
					final boolean ok = gantt.affectResource(result, part);
					if (ok == false)
						return Failable.error("Bad argument for resource");

				}
			}

		}
		return Failable.ok(result);
	}

	public Collection<? extends SentenceSimple<GanttDiagram>> getSentences() {
		return Arrays.asList(new SentenceRequire(), new SentenceTaskStarts(), new SentenceTaskStartsWithColor(),
				new SentenceTaskStartsOnlyRelative(), new SentenceTaskStartsAbsolute(), new SentenceHappens(),
				new SentenceHappensDate(), new SentenceEnds(), new SentenceTaskEndsOnlyRelative(),
				new SentenceTaskEndsAbsolute(), new SentenceIsColored(), new SentenceIsColoredForCompletion(),
				new SentenceIsDeleted(), new SentenceIsForTask(), new SentenceLinksTo(), new SentenceOccurs(),
				new SentenceDisplayOnSameRowAs(), new SentencePausesAbsoluteDate(),
				new SentencePausesAbsoluteIntervals(), new SentencePausesAbsoluteIntervalsSmart(),
				new SentencePausesDayOfWeek(), new SentenceIsDisplayedAs());
	}

}
