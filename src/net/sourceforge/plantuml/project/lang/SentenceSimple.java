/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;

public abstract class SentenceSimple implements Sentence {

	protected final Subject subjectii;
	private final IRegex verb;
	protected final Something complementii;

	public SentenceSimple(Subject subject, IRegex verb, Something complement) {
		this.subjectii = subject;
		this.verb = verb;
		this.complementii = complement;
	}

	public final IRegex toRegex() {
		if (complementii instanceof ComplementEmpty) {
			return new RegexConcat(//
					RegexLeaf.start(), //
					subjectii.toRegex(), //
					RegexLeaf.spaceOneOrMore(), //
					verb, //
					RegexLeaf.end());

		}
		return new RegexConcat(//
				RegexLeaf.start(), //
				subjectii.toRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				verb, //
				RegexLeaf.spaceOneOrMore(), //
				complementii.toRegex("0"), //
				RegexLeaf.end());
	}

	public final CommandExecutionResult execute(GanttDiagram project, RegexResult arg) {
		final Failable<? extends Object> subject = subjectii.getMe(project, arg);
		if (subject.isFail()) {
			return CommandExecutionResult.error(subject.getError());
		}
		final Failable<? extends Object> complement = complementii.getMe(project, arg, "0");
		if (complement.isFail()) {
			return CommandExecutionResult.error(complement.getError());
		}
		return execute(project, subject.get(), complement.get());

	}

	public abstract CommandExecutionResult execute(GanttDiagram project, Object subject, Object complement);

	public final String getVerbPattern() {
		return verb.getPattern();
	}

	public IRegex getVerbRegex() {
		return verb;
	}

}
