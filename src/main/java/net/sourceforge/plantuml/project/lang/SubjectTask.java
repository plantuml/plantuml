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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.Load;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.core.TaskCode;
import net.sourceforge.plantuml.project.core.TaskInstant;
import net.sourceforge.plantuml.project.ulang.UbrexSentence;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;

public class SubjectTask implements Subject<GanttDiagram> {

	public static final Subject<GanttDiagram> ME = new SubjectTask();
	public static final String REGEX_TASK_CODE = "\\[([^\\[\\]]+?)\\]";
	public static final String UBREX_TASK_CODE = "[〇+「〤[]」]";

	private SubjectTask() {
	}

	@Override
	public Collection<UbrexSentence<GanttDiagram>> getUSentences() {
		final List<UbrexSentence<GanttDiagram>> result = new ArrayList<>();
		result.add(new UbrexSentence<GanttDiagram>(this, Verbs.requires,
				Words.uzeroOrMore(Words.ON, Words.FOR, Words.THE, Words.AT), new ComplementDuration()) {
			@Override
			public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
				final Task task = (Task) subject;
				final Load duration = (Load) complement;
				task.setLoad(duration);
				return CommandExecutionResult.ok();
			}
		});
		result.add(
				new UbrexSentence<GanttDiagram>(this, Verbs.starts, new ComplementBeforeOrAfterOrAtTaskStartOrEnd()) {
					@Override
					public CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement) {
						final Task task = (Task) subject;
						final TaskInstant when;
						HColor color = null;
						when = (TaskInstant) complement;
						task.setStart(when.getInstantPrecise());
						if (when.isTask())
							project.addContraint(
									new GanttConstraint(project.getIHtmlColorSet(), project.getCurrentStyleBuilder(),
											when, new TaskInstant(task, TaskAttribute.START), color));

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
		return new UBrexNamed("SHORTNAME", new UBrexLeaf(UBREX_TASK_CODE));
	}

	@Override
	public Failable<? extends Object> ugetMe(GanttDiagram gantt, UMatcher arg) {
		final String subject = null;
		final String shortName = arg.getCapture("SHORTNAME").get(0);
		final String then = null;
//		final String stereotype = arg.get("STEREOTYPE", 0);

		final TaskCode code = TaskCode.fromIdAndDisplay(shortName, subject);
		final Task result;
		result = gantt.getOrCreateTask(code, then != null);

//		if (stereotype != null)
//			result.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

		gantt.setIt(result);
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
