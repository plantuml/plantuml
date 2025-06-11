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

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;

public abstract class CommandMultilines3<S extends Diagram> implements Command<S> {

	private final IRegex starting;

	private final MultilinesStrategy strategy;

	private final Trim trimEnd;

	private final IRegex patternEnd;

	public CommandMultilines3(IRegex patternStart, MultilinesStrategy strategy, Trim trimEnd, IRegex patternEnd) {
		assert patternStart.getPatternAsString().startsWith("^") && patternStart.getPatternAsString().endsWith("$");

		this.strategy = strategy;
		this.starting = patternStart;
		this.trimEnd = trimEnd;
		this.patternEnd = patternEnd;
	}

	final public CommandControl isValid(BlocLines lines) {
		lines = lines.cleanList(strategy);
		if (isCommandForbidden())
			return CommandControl.NOT_OK;

		final StringLocated first = lines.getFirst();
		if (first == null)
			return CommandControl.NOT_OK;

		final boolean result1 = starting.match(first.getTrimmed());
		if (result1 == false)
			return CommandControl.NOT_OK;

		if (lines.size() == 1)
			return CommandControl.OK_PARTIAL;

		final StringLocated potentialLast;
		if (trimEnd == Trim.NONE)
			potentialLast = lines.getLast();
		else if (trimEnd == Trim.BOTH)
			potentialLast = lines.getLast().getTrimmed();
		else
			throw new IllegalStateException();

		final boolean m1 = patternEnd.match(potentialLast);
		if (m1 == false)
			return CommandControl.OK_PARTIAL;

		return finalVerification();
	}

	public final CommandExecutionResult execute(S system, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		lines = lines.cleanList(strategy);
		return executeNow(system, lines);
	}

	protected abstract CommandExecutionResult executeNow(S system, BlocLines lines) throws NoSuchColorException;

	protected boolean isCommandForbidden() {
		return false;
	}

	protected CommandControl finalVerification() {
		return CommandControl.OK;
	}

	protected final IRegex getStartingPattern() {
		return starting;
	}

	protected final IRegex getEndingPattern() {
		return patternEnd;
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE;
	}

}
