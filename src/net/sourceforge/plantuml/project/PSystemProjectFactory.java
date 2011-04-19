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
 * Revision $Revision: 6002 $
 *
 */
package net.sourceforge.plantuml.project;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.PSystem;
import net.sourceforge.plantuml.command.AbstractUmlSystemCommandFactory;
import net.sourceforge.plantuml.command.CommandComment;
import net.sourceforge.plantuml.command.CommandNope;
import net.sourceforge.plantuml.project.command.CommandAffectation;
import net.sourceforge.plantuml.project.command.CommandCloseWeekDay;

public class PSystemProjectFactory extends AbstractUmlSystemCommandFactory {

	private PSystemProject systemProject;

	public PSystemProjectFactory() {
		super(DiagramType.PROJECT);
	}

	public PSystem getSystem() {
		return systemProject;
	}

	Project getProject() {
		return systemProject.getProject();
	}

	@Override
	protected void initCommands() {
		systemProject = new PSystemProject();
		addCommand(new CommandNope(systemProject));
		addCommand(new CommandComment(systemProject));
		addCommand(new CommandAffectation(systemProject));
		addCommand(new CommandCloseWeekDay(systemProject));
	}

}
