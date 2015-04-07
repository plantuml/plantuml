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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.StringUtils;

public class CommandNoteLong3 extends CommandMultilines2<ActivityDiagram3> {

	public CommandNoteLong3() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	public String getPatternEnd() {
		return "(?i)^end[%s]?note$";
	}

	public CommandExecutionResult executeNow(final ActivityDiagram3 diagram, List<String> lines) {
		// final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		final List<String> in = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());
		final NotePosition position = getPosition(line0.get("POSITION", 0));
		final Display note = Display.create(in);
		return diagram.addNote(note, position);
	}

	private NotePosition getPosition(String s) {
		if (s == null) {
			return NotePosition.LEFT;
		}
		return NotePosition.valueOf(StringUtils.goUpperCase(s));
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("note"), //
				new RegexLeaf("POSITION", "[%s]*(left|right)?"), //
				new RegexLeaf("$"));
	}

}
