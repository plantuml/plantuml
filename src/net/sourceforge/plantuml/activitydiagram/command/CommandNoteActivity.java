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
 * Revision $Revision: 5019 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.Position;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandNoteActivity extends SingleLineCommand<ActivityDiagram> {

	public CommandNoteActivity(ActivityDiagram diagram) {
		super(diagram, "(?i)^note\\s+(right|left|top|bottom)\\s*:\\s*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String pos = arg.get(0);
		final Entity note = getSystem().createNote("GN" + UniqueSequence.getValue(), arg.get(1));

		IEntity activity = getSystem().getLastEntityConsulted();
		if (activity == null) {
			activity = getSystem().getStart();
		}

		final Position position = Position.valueOf(pos.toUpperCase()).withRankdir(getSystem().getRankdir());
		final Link link;

		final LinkType type = new LinkType(LinkDecor.NONE, LinkDecor.NONE).getDashed();
		if (position == Position.RIGHT) {
			link = new Link(activity, note, type, null, 1);
		} else if (position == Position.LEFT) {
			link = new Link(note, activity, type, null, 1);
		} else if (position == Position.BOTTOM) {
			link = new Link(activity, note, type, null, 2);
		} else if (position == Position.TOP) {
			link = new Link(note, activity, type, null, 2);
		} else {
			throw new IllegalArgumentException();
		}
		getSystem().addLink(link);
		return CommandExecutionResult.ok();

	}

}
