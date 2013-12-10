/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.MessageExoType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;

abstract class CommandExoArrowAny extends SingleLineCommand2<SequenceDiagram> {

	public CommandExoArrowAny(RegexConcat pattern) {
		super(pattern);
	}

	@Override
	final protected CommandExecutionResult executeArg(SequenceDiagram sequenceDiagram, RegexResult arg2) {
		final String body = arg2.getLazzy("ARROW_BODYA", 0) + arg2.getLazzy("ARROW_BODYB", 0);
		final String dressing = arg2.getLazzy("ARROW_DRESSING", 0);
		final Participant p = sequenceDiagram.getOrCreateParticipant(StringUtils
				.eventuallyRemoveStartingAndEndingDoubleQuote(arg2.get("PARTICIPANT", 0)));

		final boolean sync = dressing.length() == 2;
		final boolean dotted = body.contains("--");

		final Display labels;
		if (arg2.get("LABEL", 0) == null) {
			labels = Display.asList("");
		} else {
			labels = Display.getWithNewlines(arg2.get("LABEL", 0));
		}

		final boolean bothDirection = arg2.get("ARROW_BOTHDRESSING", 0) != null;

		ArrowConfiguration config = bothDirection ? ArrowConfiguration.withDirectionBoth() : ArrowConfiguration
				.withDirectionNormal();
		if (dotted) {
			config = config.withDotted();
		}
		if (sync) {
			config = config.withHead(ArrowHead.ASYNC);
		}
		config = config.withPart(getArrowPart(dressing));
		config = CommandArrow.applyStyle(arg2.getLazzy("ARROW_STYLE", 0), config);

		final String error = sequenceDiagram.addMessage(new MessageExo(p, getMessageExoType(arg2), labels, config,
				sequenceDiagram.getNextMessageNumber(), isShortArrow(arg2)));
		if (error != null) {
			return CommandExecutionResult.error(error);
		}
		return CommandExecutionResult.ok();
	}

	private ArrowPart getArrowPart(String dressing) {
		if (dressing.contains("/")) {
			return ArrowPart.BOTTOM_PART;
		}
		if (dressing.contains("\\")) {
			return ArrowPart.TOP_PART;
		}
		return ArrowPart.FULL;
	}

	abstract MessageExoType getMessageExoType(RegexResult arg2);

	private boolean isShortArrow(RegexResult arg2) {
		final String s = arg2.getLazzy("SHORT", 0);
		if (s != null && s.contains("?")) {
			return true;
		}
		return false;
	}

}
