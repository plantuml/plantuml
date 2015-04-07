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
 * Revision $Revision: 14321 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.StringUtils;

public class CommandMultilinesTitle extends CommandMultilines<UmlDiagram> {

	public CommandMultilinesTitle() {
		super("(?i)^title$");
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^end[%s]?title$";
	}

	public CommandExecutionResult execute(final UmlDiagram diagram, List<String> lines) {
		final Display strings = Display.create(lines.subList(1, lines.size() - 1)).removeEmptyColumns();
		if (strings.size() > 0) {
			diagram.setTitle(strings);
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("No title defined");
	}

}
