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
 * Revision $Revision: 5441 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandLinkState extends SingleLineCommand<StateDiagram> {

	public CommandLinkState(StateDiagram diagram) {
		super(diagram, "(?i)^([\\p{L}0-9_.]+|\\[\\*\\])\\s*" + "(-+(?:left|right|up|down|le?|ri?|up?|do?)?-*[\\]>])"
				+ "\\s*([\\p{L}0-9_.]+|\\[\\*\\])\\s*(?::\\s*([^\"]+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final IEntity cl1 = getEntityStart(arg.get(0));
		final IEntity cl2 = getEntityEnd(arg.get(2));

		final String queueRaw = arg.get(1).substring(0, arg.get(1).length() - 1);
		final Direction direction = StringUtils.getQueueDirection(queueRaw);
		final String queue = StringUtils.manageQueueForCuca(queueRaw);
		final int lenght = queue.length();

		Link link = new Link(cl1, cl2, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), arg.get(3), lenght);
		getSystem().addLink(link);
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		return CommandExecutionResult.ok();
	}

	private IEntity getEntityStart(String code) {
		if (code.startsWith("[*]")) {
			return getSystem().getStart();
		}
		return getSystem().getOrCreateClass(code);
	}

	private IEntity getEntityEnd(String code) {
		if (code.startsWith("[*]")) {
			return getSystem().getEnd();
		}
		return getSystem().getOrCreateClass(code);
	}

}
