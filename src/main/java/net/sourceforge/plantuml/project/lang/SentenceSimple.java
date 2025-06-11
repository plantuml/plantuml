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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public abstract class SentenceSimple<D extends Diagram> implements Sentence<D> {

	private final Subject<D> subject;
	private final IRegex verb;
	private final IRegex adverbialOrPropositon;
	private final Something<D> complement;

	public SentenceSimple(Subject<D> subject, IRegex verb, Something<D> complement) {
		this(subject, verb, new RegexLeaf(""), complement);
	}

	public SentenceSimple(Subject<D> subject, IRegex verb, IRegex adverbialOrPropositon, Something<D> complement) {
		this.subject = subject;
		this.verb = verb;
		this.adverbialOrPropositon = adverbialOrPropositon;
		this.complement = complement;
	}

	public String getSignature() {
		return subject.getClass() + "/" + verb.getPatternAsString() + "/" + complement.getClass();
	}

	public final IRegex toRegex() {
		if (complement instanceof ComplementEmpty)
			return new RegexConcat(//
					RegexLeaf.start(), //
					subject.toRegex(), //
					RegexLeaf.spaceOneOrMore(), //
					verb, //
					adverbialOrPropositon, //
					OPTIONAL_FINAL_DOT);

		return new RegexConcat(//
				RegexLeaf.start(), //
				subject.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				verb, //
				adverbialOrPropositon, //
				RegexLeaf.spaceOneOrMore(), //
				complement.toRegex("0"), //
				OPTIONAL_FINAL_DOT);
	}

	public final CommandExecutionResult execute(D project, RegexResult arg) {
		final Failable<? extends Object> currentSubject = subject.getMe(project, arg);
		if (currentSubject.isFail())
			return CommandExecutionResult.error(currentSubject.getError());

		final Failable<? extends Object> currentComplement = complement.getMe(project, arg, "0");
		if (currentComplement.isFail())
			return CommandExecutionResult.error(currentComplement.getError());

		return execute(project, currentSubject.get(), currentComplement.get());

	}

	public abstract CommandExecutionResult execute(D project, Object subject, Object complement);

	public IRegex getVerbRegex() {
		return verb;
	}

	protected final IRegex getAdverbialOrPropositon() {
		return adverbialOrPropositon;
	}

	protected final Subject<D> getSubject() {
		return subject;
	}

	protected final Something<D> getComplement() {
		return complement;
	}

}
