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
import java.util.StringTokenizer;

import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskCode;
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

	private SubjectTask() {
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

}
