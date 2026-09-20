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
package net.sourceforge.plantuml.command;

import java.util.Collection;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.FirstTokens;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.utils.BlocLines;

public interface Command<D extends Diagram> {

	CommandExecutionResult execute(D diagram, BlocLines lines, ParserPass currentPass) throws NoSuchColorException;

	default String explain(BlocLines lines) {
		return null;
	}

	CommandControl isValid(BlocLines lines);

	boolean isEligibleFor(ParserPass pass);

	default boolean isCommandForbidden(BlocLines lines) {
		return false;
	}

	/**
	 * The first tokens a line must start with for this command to have any chance of
	 * matching it, as {@code StringLocated#getFirstToken} computes it: the lower-cased run of
	 * letters the line opens with, or its first character alone for a line that opens with
	 * anything else ({@code "}"}, {@code "="} for "==", {@code "&"}, each digit...), or
	 * {@code ""} for a blank line.
	 *
	 * This is the slow, reference answer: a command built on one of the usual base classes
	 * reads it from its own pattern with {@link net.sourceforge.plantuml.regex.FirstTokens},
	 * which keeps the regex the only place the command says what it accepts. It is not the
	 * one used to dispatch lines: see {@link #mandatoryFirstTokensFast()}, which must agree
	 * with it (checked by {@link #mandatoryFirstTokens()} when assertions are enabled).
	 */
	Collection<String> mandatoryFirstTokensSlow();

	/**
	 * The same answer as {@link #mandatoryFirstTokensSlow()}, written by hand so that it costs
	 * nothing to get.
	 *
	 * Returning {@code null} ({@link net.sourceforge.plantuml.regex.FirstTokens#ANYTHING}) means
	 * "I can match any line" and keeps the command in the list tried for every token nobody
	 * claimed. That is the safe answer: a command that declares too much only costs the regex
	 * attempts it could have skipped, while one that declares too few of its tokens stops being
	 * reachable for the ones it left out. A non-null set is never empty in practice: it would
	 * mean the command matches no line at all.
	 */
	Collection<String> mandatoryFirstTokensFast();

	/**
	 * The first tokens used to dispatch lines: {@link #mandatoryFirstTokensFast()}, checked
	 * against {@link #mandatoryFirstTokensSlow()} when assertions are enabled (never under
	 * TeaVM).
	 */
	default Collection<String> mandatoryFirstTokens() {
		if (TeaVM.a())
			assert FirstTokens.same(mandatoryFirstTokensFast(), mandatoryFirstTokensSlow());
		return mandatoryFirstTokensFast();
	}

}
