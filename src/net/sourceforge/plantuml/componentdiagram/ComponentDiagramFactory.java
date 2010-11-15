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
 * Revision $Revision: 5463 $
 *
 */
package net.sourceforge.plantuml.componentdiagram;

import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandCreateNote;
import net.sourceforge.plantuml.command.CommandEndPackage;
import net.sourceforge.plantuml.command.CommandMultilinesStandaloneNote;
import net.sourceforge.plantuml.command.CommandNoteEntity;
import net.sourceforge.plantuml.command.CommandPackage;
import net.sourceforge.plantuml.command.CommandPage;
import net.sourceforge.plantuml.componentdiagram.command.CommandCreateActorInComponent;
import net.sourceforge.plantuml.componentdiagram.command.CommandCreateCircleInterface;
import net.sourceforge.plantuml.componentdiagram.command.CommandCreateComponent;
import net.sourceforge.plantuml.componentdiagram.command.CommandLinkComponent2;
import net.sourceforge.plantuml.componentdiagram.command.CommandMultilinesComponentNoteEntity;
import net.sourceforge.plantuml.usecasediagram.command.CommandRankDirUsecase;

public class ComponentDiagramFactory extends AbstractUmlSystemCommandFactory {

	private ComponentDiagram system;

	public ComponentDiagram getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ComponentDiagram();

		addCommand(new CommandRankDirUsecase(system));
		addCommonCommands(system);

		addCommand(new CommandPage(system));
		//addCommand(new CommandLinkComponent(system));
		addCommand(new CommandLinkComponent2(system));

		addCommand(new CommandPackage(system));
		addCommand(new CommandEndPackage(system));
		addCommand(new CommandNoteEntity(system));

		addCommand(new CommandCreateNote(system));
		addCommand(new CommandCreateComponent(system));
		addCommand(new CommandCreateCircleInterface(system));
		addCommand(new CommandCreateActorInComponent(system));

		addCommand(new CommandMultilinesComponentNoteEntity(system));
		addCommand(new CommandMultilinesStandaloneNote(system));

	}
}
