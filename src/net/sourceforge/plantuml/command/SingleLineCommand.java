/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 10779 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.Diagram;

public abstract class SingleLineCommand<S extends Diagram> implements Command<S> {

	private final Pattern pattern;

	public SingleLineCommand(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.startsWith("(?i)^") == false || pattern.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern);
		}

		this.pattern = Pattern.compile(pattern);
	}

	public String[] getDescription() {
		return new String[] { pattern.pattern() };
	}

	final public CommandControl isValid(List<String> lines) {
		if (lines.size() != 1) {
			return CommandControl.NOT_OK;
		}
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final String line = lines.get(0).trim();
		final Matcher m = pattern.matcher(line);
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

	public final CommandExecutionResult execute(S system, List<String> lines) {
		if (lines.size() != 1) {
			throw new IllegalArgumentException();
		}
		final String line = lines.get(0).trim();
		if (isForbidden(line)) {
			return CommandExecutionResult.error("Forbidden line " + line);
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
