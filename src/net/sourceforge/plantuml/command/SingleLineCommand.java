/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.PSystem;
import net.sourceforge.plantuml.StringUtils;

public abstract class SingleLineCommand<S extends PSystem> implements Command {

	private static final Set<String> printed = new HashSet<String>();

	private final S system;
	private final Pattern pattern;

	public SingleLineCommand(S system, String pattern) {
		if (system == null) {
			throw new IllegalArgumentException();
		}
		if (pattern == null) {
			throw new IllegalArgumentException();
		}
		if (pattern.startsWith("(?i)^") == false || pattern.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + pattern);
		}

		if (printed.add(pattern) == true) {
			// System.out.println(pattern);
		}

		this.system = system;
		this.pattern = Pattern.compile(pattern);
	}

	final protected S getSystem() {
		return system;
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

	public final CommandExecutionResult execute(List<String> lines) {
		if (lines.size() != 1) {
			throw new IllegalArgumentException();
		}
		final String line = lines.get(0).trim();
		if (isForbidden(line)) {
			return CommandExecutionResult.error("Forbidden line "+line);
		}
		final List<String> arg = getSplit(line);
		if (arg == null) {
			return CommandExecutionResult.error("Cannot parse line "+line);
		}
		return executeArg(arg);
	}

	protected boolean isForbidden(String line) {
		return false;
	}

	protected abstract CommandExecutionResult executeArg(List<String> arg);

	final public List<String> getSplit(String line) {
		return StringUtils.getSplit(pattern, line);
	}

	final public boolean isDeprecated(List<String> lines) {
		if (lines.size() != 1) {
			return false;
		}
		return isDeprecated(lines.get(0));
	}

	public String getHelpMessageForDeprecated(List<String> lines) {
		return null;
	}

	protected boolean isDeprecated(String line) {
		return false;
	}

}
