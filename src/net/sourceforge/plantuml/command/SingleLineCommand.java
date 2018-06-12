/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.core.Diagram;

public abstract class SingleLineCommand<S extends Diagram> implements Command<S> {

	private final Pattern2 pattern;

	public SingleLineCommand(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.startsWith("(?i)^") == false || pattern.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern);
		}

		this.pattern = MyPattern.cmpile(pattern);
	}

	public String[] getDescription() {
		return new String[] { pattern.pattern() };
	}

	final public CommandControl isValid(BlocLines lines) {
		if (lines.size() != 1) {
			return CommandControl.NOT_OK;
		}
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final String line = StringUtils.trin(lines.getFirst499());
		final Matcher2 m = pattern.matcher(line);
		final boolean result = m.find();
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
		final String line = StringUtils.trin(lines.getFirst499());
		if (isForbidden(line)) {
			return CommandExecutionResult.error("Syntax error: " + line);
		}
		final List<String> arg = getSplit(line);
		if (arg == null) {
			return CommandExecutionResult.error("Cannot parse line " + line);
		}
		return executeArg(system, arg);
	}

	protected boolean isForbidden(String line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(S system, List<String> arg);

	final public List<String> getSplit(String line) {
		return StringUtils.getSplit(pattern, line);
	}

}
