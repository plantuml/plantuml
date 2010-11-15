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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.statediagram.command;

import java.util.List;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandCreatePackageState extends SingleLineCommand<StateDiagram> {

	public CommandCreatePackageState(StateDiagram diagram) {
		super(diagram, "(?i)^state\\s+(?:\"([^\"]+)\"\\s+as\\s+)?([\\p{L}0-9_.]+)(?:\\s*\\{|\\s+begin)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final Group currentPackage = getSystem().getCurrentGroup();
		String display = arg.get(0);
		final String code = arg.get(1);
		if (display == null) {
			display = code;
		}
		final Group p = getSystem().getOrCreateGroup(code, display, null, GroupType.STATE, currentPackage);
		p.setRounded(true);
		return CommandExecutionResult.ok();
	}

}
