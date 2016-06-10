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
 * Revision $Revision: 10006 $
 *
 */
package net.sourceforge.plantuml.activitydiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram.command.CommandElse;
import net.sourceforge.plantuml.activitydiagram.command.CommandEndPartition;
import net.sourceforge.plantuml.activitydiagram.command.CommandEndif;
import net.sourceforge.plantuml.activitydiagram.command.CommandIf;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkActivity;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkLongActivity;
import net.sourceforge.plantuml.activitydiagram.command.CommandPartition;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.FactoryNoteActivityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;

public class ActivityDiagramFactory extends UmlDiagramFactory {

	@Override
	public ActivityDiagram createEmptyDiagram() {
		return new ActivityDiagram();
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());
		addCommonCommands(cmds);
		cmds.add(new CommandRankDir());

		cmds.add(new CommandPartition());
		cmds.add(new CommandEndPartition());
		cmds.add(new CommandLinkLongActivity());

		final FactoryNoteActivityCommand factoryNoteActivityCommand = new FactoryNoteActivityCommand();
		cmds.add(factoryNoteActivityCommand.createSingleLine());
		cmds.add(factoryNoteActivityCommand.createMultiLine(false));

		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));

		cmds.add(new CommandIf());
		cmds.add(new CommandElse());
		cmds.add(new CommandEndif());

		cmds.add(new CommandLinkActivity());
		// addCommand(new CommandInnerConcurrent(system));

		return cmds;

	}

}
