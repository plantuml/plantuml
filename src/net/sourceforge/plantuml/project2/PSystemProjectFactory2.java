/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 6002 $
 *
 */
package net.sourceforge.plantuml.project2;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandComment;
import net.sourceforge.plantuml.command.CommandMultilinesComment;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.command.UmlDiagramFactory;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.project2.command.CommandAffectation;
import net.sourceforge.plantuml.project2.command.CommandCloseWeekDay;

public class PSystemProjectFactory2 extends UmlDiagramFactory {

	public PSystemProjectFactory2() {
		super(DiagramType.PROJECT);
	}

	@Override
	protected List<Command> createCommands() {
		final List<Command> cmds = new ArrayList<Command>();
		cmds.add(new CommandNope());
		cmds.add(new CommandComment());
		cmds.add(new CommandMultilinesComment());
		cmds.add(new CommandAffectation());
		cmds.add(new CommandCloseWeekDay());
		return cmds;
	}

	@Override
	public PSystemProject2 createEmptyDiagram() {
		return new PSystemProject2();
	}

}
