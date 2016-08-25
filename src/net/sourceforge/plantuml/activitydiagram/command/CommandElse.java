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
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.IEntity;

public class CommandElse extends SingleLineCommand<ActivityDiagram> {

	public CommandElse() {
		super("(?i)^else$");
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram system, List<String> arg) {
		if (system.getLastEntityConsulted() == null) {
			return CommandExecutionResult.error("No if for this else");
		}
		if (system.getCurrentContext() == null) {
			return CommandExecutionResult.error("No if for this else");
		}
		final IEntity branch = system.getCurrentContext().getBranch();

		system.setLastEntityConsulted(branch);

		return CommandExecutionResult.ok();
	}

}
