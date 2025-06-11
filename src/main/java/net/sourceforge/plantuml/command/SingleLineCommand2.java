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

import java.util.Objects;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public abstract class SingleLineCommand2<S extends Diagram> implements Command<S> {

	private final IRegex pattern;
	private final boolean doTrim;

	public SingleLineCommand2(IRegex pattern) {
		this(true, pattern);
	}

	public SingleLineCommand2(boolean doTrim, IRegex pattern) {
		this.doTrim = doTrim;
		this.pattern = Objects.requireNonNull(pattern);
	}

	public boolean syntaxWithFinalBracket() {
		return false;
	}

	private String myTrim(StringLocated s) {
		if (doTrim)
			return s.getTrimmed().getString();

		return s.getString();
	}

	private StringLocated myTrim2(StringLocated s) {
		if (doTrim)
			return s.getTrimmed();
		return s;

	}

	final public CommandControl isValid(BlocLines lines) {
		if (lines.size() == 2 && syntaxWithFinalBracket())
			return isValidBracket(lines);

		if (lines.size() != 1)
			return CommandControl.NOT_OK;

		if (isCommandForbidden())
			return CommandControl.NOT_OK;

		final StringLocated line2 = myTrim2(lines.getFirst());
		if (syntaxWithFinalBracket() && line2.getString().endsWith("{") == false) {
			final String vline = lines.getAt(0).getString() + " {";
			if (isValid(BlocLines.singleString(vline)) == CommandControl.OK)
				return CommandControl.OK_PARTIAL;

			return CommandControl.NOT_OK;
		}

		final boolean result = pattern.match(line2);
		if (result)
			return finalVerification();

		return CommandControl.NOT_OK;
	}

	private CommandControl isValidBracket(BlocLines lines) {
		assert lines.size() == 2;
		assert syntaxWithFinalBracket();
		if (myTrim(lines.getAt(1)).equals("{") == false)
			return CommandControl.NOT_OK;

		final String vline = lines.getAt(0).getString() + " {";
		return isValid(BlocLines.singleString(vline));
	}

	protected boolean isCommandForbidden() {
		return false;
	}

	protected CommandControl finalVerification() {
		return CommandControl.OK;
	}

	public final CommandExecutionResult execute(S system, BlocLines lines, ParserPass currentPass) {
		if (syntaxWithFinalBracket() && lines.size() == 2) {
			assert myTrim(lines.getAt(1)).equals("{");
			lines = BlocLines.singleString(lines.getFirst().getString() + " {");
		}
		if (lines.size() != 1)
			throw new IllegalArgumentException();

		final StringLocated first = lines.getFirst();
		final String line = myTrim(first);
		if (isForbidden(line))
			return CommandExecutionResult.error("Syntax error: " + line);

		final RegexResult arg = pattern.matcher(line);
		if (arg == null)
			return CommandExecutionResult.error("Cannot parse line " + line);

		if (system instanceof PSystemError)
			return CommandExecutionResult.error("PSystemError cannot be cast");

		try {
			return executeArg(system, first.getLocation(), arg, currentPass);
		} catch (NoSuchColorException e) {
			return CommandExecutionResult.badColor();
		}
	}

	protected boolean isForbidden(CharSequence line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(S system, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException;

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE;
	}

}
