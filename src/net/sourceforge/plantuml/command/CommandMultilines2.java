/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 5041 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;
import java.util.regex.Matcher;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.core.Diagram;

public abstract class CommandMultilines2<S extends Diagram> implements Command<S> {

	private final RegexConcat starting;

	private final MultilinesStrategy strategy;

	public CommandMultilines2(RegexConcat patternStart, MultilinesStrategy strategy) {
		if (patternStart.getPattern().startsWith("^") == false || patternStart.getPattern().endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + patternStart.getPattern());
		}
		this.strategy = strategy;
		this.starting = patternStart;
	}

	public abstract String getPatternEnd();

	public String[] getDescription() {
		return new String[] { "START: " + starting.getPattern(), "END: " + getPatternEnd() };
	}

	final public CommandControl isValid(List<String> lines) {
		lines = strategy.filter(lines);
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final boolean result1 = starting.match(StringUtils.trin(lines.get(0)));
		if (result1 == false) {
			return CommandControl.NOT_OK;
		}
		if (lines.size() == 1) {
			return CommandControl.OK_PARTIAL;
		}

		final Matcher m1 = MyPattern.cmpile(getPatternEnd()).matcher(StringUtils.trinNoTrace(lines.get(lines.size() - 1)));
		if (m1.matches() == false) {
			return CommandControl.OK_PARTIAL;
		}

		actionIfCommandValid();
		return CommandControl.OK;
	}

	public final CommandExecutionResult execute(S system, List<String> lines) {
		return executeNow(system, strategy.filter(lines));
	}

	public abstract CommandExecutionResult executeNow(S system, List<String> lines);

	protected boolean isCommandForbidden() {
		return false;
	}

	protected void actionIfCommandValid() {
	}

	protected final RegexConcat getStartingPattern() {
		return starting;
	}

}
