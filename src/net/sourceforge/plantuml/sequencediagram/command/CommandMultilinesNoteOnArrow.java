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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandMultilinesNoteOnArrow extends CommandMultilines<SequenceDiagram> {

	public CommandMultilinesNoteOnArrow(final SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, "(?i)^note\\s+(right|left)\\s*(#\\w+)?$", "(?i)^end ?note$");
	}

	public CommandExecutionResult execute(List<String> lines) {
		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0).trim());

		final NotePosition position = NotePosition.valueOf(line0.get(0).toUpperCase());
		final AbstractMessage m = getSystem().getLastMessage();
		if (m != null) {
			final List<CharSequence> strings = new ArrayList<CharSequence>();
			final Iterator<String> it = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1)).iterator();
			while (it.hasNext()) {
				CharSequence s = it.next();
				if (s.equals("{{")) {
					final List<String> other = new ArrayList<String>();
					other.add("@startuml");
					while (it.hasNext()) {
						String s2 = it.next();
						if (s2.equals("}}")) {
							break;
						}
						other.add(s2);
					}
					other.add("@enduml");
					s = new EmbededDiagram(other);
				}
				strings.add(s);
			}
			m.setNote(strings, position, line0.get(1), null);
		}

		return CommandExecutionResult.ok();
	}

}
