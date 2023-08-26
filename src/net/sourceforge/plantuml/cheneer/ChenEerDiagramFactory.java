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
package net.sourceforge.plantuml.cheneer;

import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.cheneer.command.CommandAssociateRelationship;
import net.sourceforge.plantuml.cheneer.command.CommandCreateAttribute;
import net.sourceforge.plantuml.cheneer.command.CommandCreateEntity;
import net.sourceforge.plantuml.cheneer.command.CommandCreateRelationship;
import net.sourceforge.plantuml.cheneer.command.CommandEndGroup;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommonCommands;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;

public class ChenEerDiagramFactory extends PSystemCommandFactory {

	public ChenEerDiagramFactory() {
		super(DiagramType.CHEN_EER);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		cmds.add(new CommandCreateEntity());
		cmds.add(new CommandCreateAttribute());
		cmds.add(new CommandCreateRelationship());
		cmds.add(new CommandAssociateRelationship());
		cmds.add(new CommandEndGroup());

		CommonCommands.addTitleCommands(cmds);
		CommonCommands.addCommonCommands2(cmds);
	}

	@Override
	public AbstractPSystem createEmptyDiagram(UmlSource source, Map<String, String> skinParam) {
		return new ChenEerDiagram(source, skinParam);
	}

}
