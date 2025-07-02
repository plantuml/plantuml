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

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.UnicodeBracketedExpression;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

public abstract class UBrexCommandMultilines2<S extends Diagram> implements Command<S> {

	private final UnicodeBracketedExpression starting;

	private final Trim trimEnd;

	private final MultilinesStrategy strategy;

	private final Lazy<UnicodeBracketedExpression> end;

	public UBrexCommandMultilines2(UnicodeBracketedExpression patternStart, MultilinesStrategy strategy, Trim trimEnd,
			Lazy<UnicodeBracketedExpression> end) {
		assert end != null;

		this.strategy = strategy;
		this.starting = patternStart;
		this.trimEnd = trimEnd;
		this.end = end;
	}

	public boolean syntaxWithFinalBracket() {
		return false;
	}

	public final void getPatternEndToto() {

	}

	final public CommandControl isValid(BlocLines lines) {
		lines = lines.cleanList(strategy);
		if (isCommandForbidden())
			return CommandControl.NOT_OK;

		if (syntaxWithFinalBracket()) {
			if (lines.size() == 1 && lines.getFirst().getTrimmed().getString().endsWith("{") == false) {
				final String vline = ((StringLocated) lines.getAt(0)).getString() + " {";
				if (isValid(BlocLines.singleString(vline)) == CommandControl.OK_PARTIAL)
					return CommandControl.OK_PARTIAL;

				return CommandControl.NOT_OK;
			}
			lines = lines.eventuallyMoveBracket();
		}
		final StringLocated first = lines.getFirst();
		if (first == null)
			return CommandControl.NOT_OK;

		final UMatcher result1 = starting.match(first.getTrimmed().getString());
		if (result1.exactMatch() == false)
			return CommandControl.NOT_OK;

		if (lines.size() == 1)
			return CommandControl.OK_PARTIAL;
		final UMatcher m1 = end.get().match(trimEnd.trim(lines.getLast()));
		if (m1.exactMatch() == false)
			return CommandControl.OK_PARTIAL;

		return finalVerification(lines);
	}

	public final CommandExecutionResult execute(S system, BlocLines lines, ParserPass currentPass) {
		lines = lines.cleanList(strategy);
		if (syntaxWithFinalBracket())
			lines = lines.eventuallyMoveBracket();

		try {
			return executeNow(system, lines, currentPass);
		} catch (NoSuchColorException e) {
			return CommandExecutionResult.badColor();
		}
	}

	protected abstract CommandExecutionResult executeNow(S system, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException;

	protected boolean isCommandForbidden() {
		return false;
	}

	protected CommandControl finalVerification(BlocLines lines) {
		return CommandControl.OK;
	}

	protected final UnicodeBracketedExpression getStartingPattern() {
		return starting;
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE;
	}

	public UnicodeBracketedExpression getEndPattern() {
		return end.get();
	}

}
