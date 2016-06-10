/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 7272 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.EventWithDeactivate;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowBody;
import net.sourceforge.plantuml.skin.ArrowConfiguration;

public class CommandReturn extends SingleLineCommand<SequenceDiagram> {

	public CommandReturn() {
		super("(?i)^return[%s]*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram sequenceDiagram, List<String> arg) {

		Message message = sequenceDiagram.getActivatingMessage();
		boolean doDeactivation = true;
		if (message == null) {
			final EventWithDeactivate last = sequenceDiagram.getLastEventWithDeactivate();
			if (last instanceof Message == false) {
				return CommandExecutionResult.error("Nowhere to return to.");
			}
			message = (Message) last;
			doDeactivation = false;
		}

		final ArrowConfiguration arrow = message.getArrowConfiguration().withBody(ArrowBody.DOTTED);

		sequenceDiagram.addMessage(
				new Message(message.getParticipant2(), message.getParticipant1(), Display.getWithNewlines(arg
				.get(0)), arrow, sequenceDiagram.getNextMessageNumber()));

		if (doDeactivation) {
			final String error = sequenceDiagram.activate(message.getParticipant2(), LifeEventType.DEACTIVATE, null);
			if (error != null) {
				return CommandExecutionResult.error(error);
			}
		}
		return CommandExecutionResult.ok();

	}

}
