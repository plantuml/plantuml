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
 * Revision $Revision: 5751 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.cucadiagram.Link;

public class CommandMultilinesNoteActivityLink extends CommandMultilines<ActivityDiagram> {

	public CommandMultilinesNoteActivityLink(final ActivityDiagram system) {
		super(system, "(?i)^note\\s+on\\s+link$", "(?i)^end ?note$");
	}

	public final CommandExecutionResult execute(List<String> lines) {

		final List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));
		final String s = StringUtils.getMergedLines(strings);

		final Link link = getSystem().getLastActivityLink();
		if (link == null) {
			return CommandExecutionResult.error("Nothing to note");
		}
		link.setNote(s);
		return CommandExecutionResult.ok();
	}

}
