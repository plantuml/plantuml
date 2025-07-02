/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
package net.sourceforge.plantuml.activitydiagram;

import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkActivity;
import net.sourceforge.plantuml.activitydiagram.command.CommandLinkLongActivity;
import net.sourceforge.plantuml.activitydiagram.command.UBrexCommandElse;
import net.sourceforge.plantuml.activitydiagram.command.UBrexCommandEndPartition;
import net.sourceforge.plantuml.activitydiagram.command.UBrexCommandEndif;
import net.sourceforge.plantuml.activitydiagram.command.UBrexCommandIf;
import net.sourceforge.plantuml.activitydiagram.command.UBrexCommandPartition;
import net.sourceforge.plantuml.classdiagram.command.UBrexCommandHideShow2;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.UBrexCommandFootboxIgnored;
import net.sourceforge.plantuml.command.UBrexCommandRankDir;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteActivity;
import net.sourceforge.plantuml.command.note.CommandFactoryNoteOnLink;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class ActivityDiagramFactory extends PSystemCommandFactory {
	// ::remove folder when __HAXE__

	@Override
	public ActivityDiagram createEmptyDiagram(UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		return new ActivityDiagram(source, previous, preprocessing);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		cmds.add(new UBrexCommandFootboxIgnored());
		CommonCommands.addCommonCommands1(cmds);
		cmds.add(new UBrexCommandRankDir());

		cmds.add(new UBrexCommandPartition());
		cmds.add(new UBrexCommandEndPartition());
		cmds.add(new CommandLinkLongActivity());

		final CommandFactoryNoteActivity factoryNoteActivityCommand = new CommandFactoryNoteActivity();
		cmds.add(factoryNoteActivityCommand.createSingleLine());
		cmds.add(factoryNoteActivityCommand.createMultiLine(false));

		final CommandFactoryNoteOnLink factoryNoteOnLinkCommand = new CommandFactoryNoteOnLink(ParserPass.ONE);
		cmds.add(factoryNoteOnLinkCommand.createSingleLine());
		cmds.add(factoryNoteOnLinkCommand.createMultiLine(false));

		cmds.add(new UBrexCommandIf());
		cmds.add(new UBrexCommandElse());
		cmds.add(new UBrexCommandEndif());

		cmds.add(new CommandLinkActivity());
		cmds.add(new UBrexCommandHideShow2());
		// addCommand(new CommandInnerConcurrent(system));

	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

}
