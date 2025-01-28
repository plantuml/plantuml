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
package net.sourceforge.plantuml.help;

import java.util.List;

import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.PSystemCommandFactory;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class HelpFactory extends PSystemCommandFactory {
	// ::comment when __CORE__
	// ::comment when __HAXE__

	@Override
	public Help createEmptyDiagram(UmlSource source, Previous previous, PreprocessingArtifact preprocessing) {
		return new Help(source, preprocessing);
	}

	@Override
	protected void initCommandsList(List<Command> cmds) {
		cmds.add(new CommandHelpColor());
		cmds.add(new CommandHelpFont());
		cmds.add(new CommandHelpKeyword());
		cmds.add(new CommandHelpType());
		cmds.add(new CommandHelpTheme());
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.HELP;
	}

}
