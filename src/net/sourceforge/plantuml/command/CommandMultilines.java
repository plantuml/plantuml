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
 * Revision $Revision: 6186 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.PSystem;

public abstract class CommandMultilines<S extends PSystem> implements Command {

	private final S system;

	private final Pattern starting;
	private final Pattern ending;

	public CommandMultilines(final S system, String patternStart, String patternEnd) {
		if (patternStart.startsWith("(?i)^") == false || patternStart.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + patternStart);
		}
		if (patternEnd.startsWith("(?i)^") == false || patternEnd.endsWith("$") == false) {
			throw new IllegalArgumentException("Bad pattern " + patternEnd);
		}
		this.system = system;
		this.starting = Pattern.compile(patternStart);
		this.ending = Pattern.compile(patternEnd);
	}
	
	public String[] getDescription() {
		return new String[] { "START: " + starting.pattern(), "END: " + ending.pattern() };
	}

	final public CommandControl isValid(List<String> lines) {
		if (isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		Matcher m1 = starting.matcher(lines.get(0).trim());
		if (m1.matches() == false) {
			return CommandControl.NOT_OK;
		}
		if (lines.size() == 1) {
			return CommandControl.OK_PARTIAL;
		}

		m1 = ending.matcher(lines.get(lines.size() - 1).trim());
		if (m1.matches() == false) {
			return CommandControl.OK_PARTIAL;
		}

		actionIfCommandValid();
		return CommandControl.OK;
	}

	protected boolean isCommandForbidden() {
		return false;
	}

	protected void actionIfCommandValid() {
	}

	protected S getSystem() {
		return system;
	}

	protected final Pattern getStartingPattern() {
		return starting;
	}

	protected final Pattern getEnding() {
		return ending;
	}

	public boolean isDeprecated(List<String> line) {
		return false;
	}

	public String getHelpMessageForDeprecated(List<String> lines) {
		return null;
	}

}
