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
 * Revision $Revision: 6485 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandNoteOnArrow extends SingleLineCommand<SequenceDiagram> {

	public CommandNoteOnArrow(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, "(?i)^note\\s+(right|left)\\s*(#\\w+)?\\s*:\\s*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {

		final AbstractMessage m = getSystem().getLastMessage();
		if (m != null) {
			final NotePosition position = NotePosition.valueOf(arg.get(0).toUpperCase());
			final List<String> strings = StringUtils.getWithNewlines(arg.get(2));
			m.setNote(strings, position, arg.get(1), null);
		}

		return CommandExecutionResult.ok();
	}

}
