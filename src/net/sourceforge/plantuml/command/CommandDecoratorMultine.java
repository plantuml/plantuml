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
 * Revision $Revision: 12235 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.core.Diagram;

public class CommandDecoratorMultine<D extends Diagram> implements Command<D> {

	private final SingleLineCommand2<D> cmd;

	public CommandDecoratorMultine(SingleLineCommand2<D> cmd) {
		this.cmd = cmd;
	}

	public CommandExecutionResult execute(D diagram, List<String> lines) {
		final String concat = concat(lines);
		return cmd.execute(diagram, Collections.singletonList(concat));
	}

	public CommandControl isValid(List<String> lines) {
		if (cmd.isCommandForbidden()) {
			return CommandControl.NOT_OK;
		}
		final String concat = concat(lines);
		if (cmd.isForbidden(concat)) {
			return CommandControl.NOT_OK;
		}
		final CommandControl tmp = cmd.isValid(Collections.singletonList(concat));
		if (tmp == CommandControl.OK_PARTIAL) {
			throw new IllegalStateException();
		}
		if (tmp == CommandControl.OK) {
			return tmp;
		}
		return CommandControl.OK_PARTIAL;
	}

	private String concat(List<String> lines) {
		final StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line);
			sb.append(StringUtils.hiddenNewLine());
		}
		return sb.substring(0, sb.length() - 1);
	}

	public String[] getDescription() {
		return cmd.getDescription();
	}

}
