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
 * Revision $Revision: 5424 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandArrow extends SingleLineCommand<SequenceDiagram> {

	public CommandArrow(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram,
				"(?i)^([\\p{L}0-9_.]+)\\s*([=-]+[>\\]]{1,2}|[<\\[]{1,2}[=-]+)\\s*([\\p{L}0-9_.]+)\\s*(?::\\s*(.*))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		Participant p1;
		Participant p2;

		final String arrow = StringUtils.manageArrowForSequence(arg.get(1));

		if (arrow.endsWith(">")) {
			p1 = getSystem().getOrCreateParticipant(arg.get(0));
			p2 = getSystem().getOrCreateParticipant(arg.get(2));
		} else if (arrow.startsWith("<")) {
			p2 = getSystem().getOrCreateParticipant(arg.get(0));
			p1 = getSystem().getOrCreateParticipant(arg.get(2));
		} else {
			throw new IllegalStateException(arg.toString());
		}
		
		final boolean full = (arrow.endsWith(">>") || arrow.startsWith("<<"))==false;

		final boolean dotted = arrow.contains("--");

		final List<String> labels;
		if (arg.get(3) == null) {
			labels = Arrays.asList("");
		} else {
			labels = StringUtils.getWithNewlines(arg.get(3));
		}

		getSystem().addMessage(new Message(p1, p2, labels, dotted, full, getSystem().getNextMessageNumber()));
		return CommandExecutionResult.ok();
	}

}
