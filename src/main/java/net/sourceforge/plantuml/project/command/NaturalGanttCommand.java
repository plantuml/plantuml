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
package net.sourceforge.plantuml.project.command;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import com.plantuml.ubrex.TextNavigator;
import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.ulang.VerbPhraseAction;
import net.sourceforge.plantuml.project.ulang.VerbPhraseMatcher;
import net.sourceforge.plantuml.utils.BlocLines;

public class NaturalGanttCommand implements Command<GanttDiagram> {

	private static final UBrexPart AND = new UBrexLeaf("〇+〴s and 〇+〴s");

	private final Subject<GanttDiagram> subject;

	public NaturalGanttCommand(Subject<GanttDiagram> subject) {
		this.subject = subject;
	}

	private Collection<VerbPhraseAction> getVerbPhrases() {
		return subject.getVerbPhrases();
	}

	@Override
	public CommandExecutionResult execute(GanttDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {

		final TextNavigator tn = TextNavigator.build(lines.getFirst().getString());
		VerbPhraseAction.skipSpaces(tn);

		final UMatcher matcherSubject = subject.toUnicodeBracketedExpressionSubject().match(tn);
		tn.jump(matcherSubject.getAcceptedMatch().length());
		VerbPhraseAction.skipSpaces(tn);

		CommandExecutionResult result = null;

		final Deque<VerbPhraseAction> candidates = new ArrayDeque<VerbPhraseAction>(getVerbPhrases());

		final Failable<? extends Object> subjectResult = subject.ugetMe(diagram, matcherSubject);

		while (!candidates.isEmpty()) {
			final VerbPhraseAction verbPhrase = candidates.removeFirst();
			if (verbPhrase.check(tn) == false)
				continue;

			final VerbPhraseMatcher args = verbPhrase.parse(tn);

			final Failable<? extends Object> complement = verbPhrase.getComplement(diagram, args);

			result = verbPhrase.execute(diagram, subjectResult.get(), complement.get());
			if (result.isOk() == false)
				return result;

			final UMatcher andMatcher = AND.match(tn);
			final String andMatch = andMatcher.getAcceptedMatch();
			if (andMatch.length() == 0)
				return result;

			result = null;
			tn.jump(andMatch.length());

			candidates.clear();
			candidates.addAll(getVerbPhrases());

		}

		return CommandExecutionResult.error("No matching verb phrase");

	}

	@Override
	public CommandControl isValid(BlocLines lines) {
		final TextNavigator tn = TextNavigator.build(lines.getFirst().getString());
		VerbPhraseAction.skipSpaces(tn);
		System.out.println("1 TN = " + tn);

		final UMatcher matcherSubject = subject.toUnicodeBracketedExpressionSubject().match(tn);
		final String v1 = matcherSubject.getAcceptedMatch();
		if (v1.length() == 0)
			return CommandControl.NOT_OK;

		tn.jump(v1.length());
		VerbPhraseAction.skipSpaces(tn);
		System.out.println("2 TN = " + tn);

		for (VerbPhraseAction verbPhrase : getVerbPhrases()) {
			System.out.println("3 VP = " + verbPhrase);
			if (verbPhrase.check(tn)) {
				System.out.println("4 OK! " + subject.getClass() + " " + verbPhrase.getClass());
				return CommandControl.OK;
			}
		}

		// TODO: manage "AND"

		System.out.println("4 NOT_OK!");
		return CommandControl.NOT_OK;
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE;
	}

}
