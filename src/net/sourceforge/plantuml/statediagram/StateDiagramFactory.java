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
package net.sourceforge.plantuml.statediagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.classdiagram.command.CommandHideShow2;
import net.sourceforge.plantuml.classdiagram.command.CommandNamespaceSeparator;
import net.sourceforge.plantuml.classdiagram.command.CommandRemoveRestore;
import net.sourceforge.plantuml.classdiagram.command.CommandUrl;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandFootboxIgnored;
import net.sourceforge.plantuml.command.CommandRankDir;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.command.note.CommandFactoryNote;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteOnEntity;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteOnLink;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.statediagram.command.CommandAddField;
import net.sourceforge.plantuml.statediagram.command.CommandConcurrentState;
import net.sourceforge.plantuml.statediagram.command.CommandCreatePackageState;
import net.sourceforge.plantuml.statediagram.command.CommandCreateState;
import net.sourceforge.plantuml.statediagram.command.CommandEndState;
import net.sourceforge.plantuml.statediagram.command.CommandLinkState;

public class StateDiagramFactory extends UmlDiagramFactory {

	private final ISkinSimple skinParam;

	public StateDiagramFactory(ISkinSimple skinParam) {
		this.skinParam = skinParam;
	}

	@Override
	public StateDiagram createEmptyDiagram() {
		return new StateDiagram(skinParam);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandFootboxIgnored());
		cmds.add(new CommandRankDir());
		cmds.add(new CommandRemoveRestore());
		cmds.add(new CommandCreateState());
		cmds.add(new CommandLinkState());
		cmds.add(new CommandCreatePackageState());
		cmds.add(new CommandEndState());
		cmds.add(new CommandAddField());
		cmds.add(new CommandConcurrentState());

		final CommandFactoryNoteOnEntity factoryNoteOnEntityCommand = new CommandFactoryNoteOnEntity("state",
				new RegexOr("ENTITY", new RegexLeaf("[\\p{L}0-9_.]+"), //
						new RegexLeaf("[%g][^%g]+[%g]") //
				));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(true));
		cmds.add(factoryNoteOnEntityCommand.createMultiLine(false));

		cmds.add(factoryNoteOnEntityCommand.createSingleLine());
		final CommandFactoryNoteOnLink factoryNoteOnLinkCommand = new CommandFactoryNoteOnLink();
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));
		cmds.add(new CommandUrl());

		final CommandFactoryNote factoryNoteCommand = new CommandFactoryNote();
		cmds.add(factoryNoteCommand.createSingleLine());
		cmds.add(factoryNoteCommand.createMultiLine(false));

		addCommonCommands1(cmds);
		cmds.add(new CommandHideShow2());
		cmds.add(new CommandNamespaceSeparator());

		return cmds;
	}

}
