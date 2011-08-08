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
 * Revision $Revision: 5884 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandReferenceMultilinesOverSeveral extends CommandMultilines<SequenceDiagram> {

	public CommandReferenceMultilinesOverSeveral(final SequenceDiagram sequenceDiagram) {
		super(
				sequenceDiagram,
				"(?i)^ref(#\\w+)?\\s+over\\s+((?:[\\p{L}0-9_.]+|\"[^\"]+\")(?:\\s*,\\s*(?:[\\p{L}0-9_.]+|\"[^\"]+\"))*)\\s*(#\\w+)?$",
				"(?i)^end ?(ref)?$");
	}

	public CommandExecutionResult execute(List<String> lines) {
		final List<String> line0 = StringUtils.getSplit(getStartingPattern(), lines.get(0).trim());
		final HtmlColor backColorElement = HtmlColor.getColorIfValid(line0.get(0));
		// final HtmlColor backColorGeneral = HtmlColor.getColorIfValid(line0.get(1));
		final HtmlColor backColorGeneral = null;

		final List<String> participants = StringUtils.splitComma(line0.get(1));
		final List<Participant> p = new ArrayList<Participant>();
		for (String s : participants) {
			p.add(getSystem().getOrCreateParticipant(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(s)));
		}

		List<String> strings = StringUtils.removeEmptyColumns(lines.subList(1, lines.size() - 1));

		Url u = null;
		if (strings.size() > 0) {
			u = Note.extractUrl(strings.get(0));
		}
		if (u != null) {
			strings = strings.subList(1, strings.size());
		}

		final Reference ref = new Reference(p, u, strings, backColorGeneral, backColorElement);
		getSystem().addReference(ref);
		return CommandExecutionResult.ok();
	}

}
