/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 5041 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.core.Diagram;

public abstract class SingleLineCommand2<S extends Diagram> implements Command<S> {

	private final RegexConcat pattern;

	public SingleLineCommand2(RegexConcat pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.getPattern().startsWith("^") == false || pattern.getPattern().endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern.getPattern());
		}

		this.pattern = pattern;
	}

	public String[] getDescription() {
		return new String[] { pattern.getPattern() };
	}

	final public CommandControl isValid(BlocLines lines) {
		if (lines.size() != 1) {
			return CommandControl.NOT_OK;
		}
		lines = lines.removeInnerComments();
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final String line = StringUtils.trin(lines.getFirst499());
		final boolean result = pattern.match(line);
		if (result) {
			actionIfCommandValid();
		}
		return result ? CommandControl.OK : CommandControl.NOT_OK;
	}

	protected boolean isCommandForbidden() {
		return false;
	}

	protected void actionIfCommandValid() {
	}

	public final CommandExecutionResult execute(S system, BlocLines lines) {
		if (lines.size() != 1) {
			throw new IllegalArgumentException();
		}
		lines = lines.removeInnerComments();
		final String line = StringUtils.trin(lines.getFirst499());
		if (isForbidden(line)) {
			return CommandExecutionResult.error("Forbidden line " + line);
		}

		final RegexResult arg = pattern.matcher(line);
		if (arg == null) {
			return CommandExecutionResult.error("Cannot parse line " + line);
		}
		if (system instanceof PSystemError) {
			return CommandExecutionResult.error("PSystemError cannot be cast");
		}
		// System.err.println("lines="+lines);
		// System.err.println("pattern="+pattern.getPattern());
		return executeArg(system, arg);
	}

	protected boolean isForbidden(CharSequence line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(S system, RegexResult arg);

}
