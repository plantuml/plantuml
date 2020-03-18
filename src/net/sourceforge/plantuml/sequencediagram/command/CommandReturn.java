/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.EventWithDeactivate;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowBody;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class CommandReturn extends SingleLineCommand2<SequenceDiagram> {

	public CommandReturn() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandReturn.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PARALLEL", "(&[%s]*)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("return"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("COLOR", "(#\\w+)"), //
								RegexLeaf.spaceOneOrMore() //
						)), //
				new RegexLeaf("MESSAGE", "(.*)"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg) {

		AbstractMessage message1 = diagram.getActivatingMessage();
		boolean doDeactivation = true;
		if (message1 == null) {
			final EventWithDeactivate last = diagram.getLastEventWithDeactivate();
			if (last instanceof Message == false) {
				return CommandExecutionResult.error("Nowhere to return to.");
			}
			message1 = (Message) last;
			doDeactivation = false;
		}

		ArrowConfiguration arrow = message1.getArrowConfiguration().withBody(ArrowBody.DOTTED);
		final String color = arg.get("COLOR", 0);
		if (color != null) {
			arrow = arrow.withColor(HColorSet.instance().getColorIfValid(color));
		}

		final Display display = Display.getWithNewlines(arg.get("MESSAGE", 0));
		final AbstractMessage message2;
		if (message1 instanceof MessageExo) {
			final MessageExo exo1 = (MessageExo) message1;
			message2 = new MessageExo(diagram.getSkinParam().getCurrentStyleBuilder(), exo1.getParticipant(), exo1
					.getType().reverse(), display, arrow, diagram.getNextMessageNumber(), false);
		} else {
			message2 = new Message(diagram.getSkinParam().getCurrentStyleBuilder(), message1.getParticipant2(),
					message1.getParticipant1(), display, arrow, diagram.getNextMessageNumber());
			final boolean parallel = arg.get("PARALLEL", 0) != null;
			if (parallel) {
				message2.goParallel();
			}
		}
		diagram.addMessage(message2);

		if (doDeactivation) {
			final String error = diagram.activate(message1.getParticipant2(), LifeEventType.DEACTIVATE, null);
			if (error != null) {
				return CommandExecutionResult.error(error);
			}
		}
		return CommandExecutionResult.ok();

	}
}
