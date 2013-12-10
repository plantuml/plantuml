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
 * Revision $Revision: 10446 $
 *
 */
package net.sourceforge.plantuml.statediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.FactoryNoteCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnEntityCommand;
import net.sourceforge.plantuml.command.note.FactoryNoteOnLinkCommand;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.statediagram.command.CommandAddField;
import net.sourceforge.plantuml.statediagram.command.CommandConcurrentState;
import net.sourceforge.plantuml.statediagram.command.CommandCreatePackageState;
import net.sourceforge.plantuml.statediagram.command.CommandCreateState;
import net.sourceforge.plantuml.statediagram.command.CommandCreateState2;
import net.sourceforge.plantuml.statediagram.command.CommandEndState;
import net.sourceforge.plantuml.statediagram.command.CommandHideEmptyDescription;
import net.sourceforge.plantuml.statediagram.command.CommandLinkState;

public class StateDiagramFactory extends UmlDiagramFactory {

	@Override
	public StateDiagram createEmptyDiagram() {
		return new StateDiagram();
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandRankDir());
		cmds.add(new CommandCreateState());
		cmds.add(new CommandCreateState2());
		// addCommand(new CommandLinkState());
		cmds.add(new CommandLinkState());
		cmds.add(new CommandCreatePackageState());
		cmds.add(new CommandEndState());
		cmds.add(new CommandAddField());
		cmds.add(new CommandConcurrentState());

		final FactoryNoteOnEntityCommand factoryNoteOnEntityCommand = new FactoryNoteOnEntityCommand(new RegexOr(
				"ENTITY", new RegexLeaf("[\\p{L}0-9_.]+"), //
				new RegexLeaf("\"[^\"]+\"") //
				));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine());

		cmds.add(new CommandHideEmptyDescription());

		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		final FactoryNoteOnLinkCommand factoryNoteOnLinkCommand = new FactoryNoteOnLinkCommand();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine());
		cmds.add(new CommandUrl());

		final FactoryNoteCommand factoryNoteCommand = new FactoryNoteCommand();
		cmds.add(factoryNoteCommand.createSingleLine());
		cmds.add(factoryNoteCommand.createMultiLine());

		addCommonCommands(cmds);

		return cmds;
	}

}
