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
package net.sourceforge.plantuml.project.ulang;

import com.plantuml.ubrex.TextNavigator;
import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.lang.Something;
import net.sourceforge.plantuml.project.lang.Subject;
import net.sourceforge.plantuml.project.lang.Verbs;

public abstract class UbrexSentence<D extends Diagram> {

	private static final UBrexPart SPACES = new UBrexLeaf("〇+〴s");

	private final Subject<D> subject;
	private final Verbs verb;
	private final UBrexPart ignored;
	private final Something<D> complement;

	public UbrexSentence(Subject<D> subject, Verbs verb, Something<D> complement) {
		this(subject, verb, null, complement);
	}

	@Override
	public String toString() {
		return subject.getClass().getSimpleName() + " " + verb + " " + complement.getClass().getSimpleName();
	}

	public UbrexSentence(Subject<D> subject, Verbs verb, UBrexPart ignored, Something<D> complement) {
		this.subject = subject;
		this.verb = verb;
		this.ignored = ignored;
		this.complement = complement;
	}

	public abstract CommandExecutionResult execute(D project, Object subject, Object complement);

	public Failable<? extends Object> getComplement(D project, GanttParseResult result) {
		return complement.ugetMe(project, result.getComplementMatcher());
	}

	public Failable<? extends Object> getSubject(D project, GanttParseResult result) {
		return subject.ugetMe(project, result.getMatcherSubject());
	}

	public GanttParseResult parse(String line) {
		final TextNavigator tn = TextNavigator.build(line);
		System.out.println("UbrexSentence::parse " + tn);

		final UMatcher matcherSubject = subject.toUnicodeBracketedExpressionSubject().match(tn);
		final String v1 = matcherSubject.getAcceptedMatch();
		if (v1.length() == 0)
			throw new IllegalStateException(line);

		tn.jump(v1.length());
		skipSpaces(tn);

		final UMatcher verbMatch = verb.toUnicodeBracketedExpression().match(tn);
		final String v2 = verbMatch.getAcceptedMatch();
		if (v2.length() == 0)
			throw new IllegalStateException(line);

		tn.jump(v2.length());
		skipSpaces(tn);

		if (ignored != null) {
			tn.jump(ignored.match(tn).getAcceptedMatch().length());
			skipSpaces(tn);
		}

		final UMatcher complementMatcher = complement.toUnicodeBracketedExpressionComplement().match(tn);
		final String v3 = complementMatcher.getAcceptedMatch();
		if (v3.length() == 0)
			throw new IllegalStateException(line);

		System.out.println("m1=" + matcherSubject);
		System.out.println("m2=" + verbMatch);
		System.out.println("m3=" + complementMatcher);

		return new GanttParseResult(matcherSubject, verbMatch, complementMatcher);
	}

	public boolean check(TextNavigator tn) {
		tn = tn.copy();
		final UMatcher matcherSubject = subject.toUnicodeBracketedExpressionSubject().match(tn);
		final String v1 = matcherSubject.getAcceptedMatch();
		if (v1.length() == 0)
			return false;

		tn.jump(v1.length());
		skipSpaces(tn);

		final UMatcher verbMatch = verb.toUnicodeBracketedExpression().match(tn);
		final String v2 = verbMatch.getAcceptedMatch();
		if (v2.length() == 0)
			return false;

		tn.jump(v2.length());
		skipSpaces(tn);

		if (ignored != null) {
			tn.jump(ignored.match(tn).getAcceptedMatch().length());
			skipSpaces(tn);
		}

		final UMatcher complementMatcher = complement.toUnicodeBracketedExpressionComplement().match(tn);
		final String v3 = complementMatcher.getAcceptedMatch();
		if (v3.length() == 0)
			return false;

		System.out.println("<" + v1 + ">");
		System.out.println("[" + v2 + "] " + verbMatch);
		System.out.println("{" + v3 + "} " + complementMatcher);

		return true;

	}

	public static void skipSpaces(final TextNavigator tn) {
		final UMatcher space1 = SPACES.match(tn);
		tn.jump(space1.getAcceptedMatch().length());
	}

}
