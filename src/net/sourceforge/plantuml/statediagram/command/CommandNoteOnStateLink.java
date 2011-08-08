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
 * Revision $Revision: 6923 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandNoteOnStateLink extends SingleLineCommand<StateDiagram> {

	public CommandNoteOnStateLink(StateDiagram diagram) {
		super(diagram, "(?i)^note\\s+(right|left|top|bottom)?\\s*on\\s+link\\s*:\\s*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final Link link = getSystem().getLastStateLink();
		if (link == null) {
			return CommandExecutionResult.error("No link defined");
		}
		Position position = Position.BOTTOM;
		if (arg.get(0) != null) {
			position = Position.valueOf(arg.get(0).toUpperCase());
		}

		link.addNote(arg.get(1), position);
		return CommandExecutionResult.ok();
	}

}
