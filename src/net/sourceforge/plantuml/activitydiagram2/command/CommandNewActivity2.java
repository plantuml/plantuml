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
package net.sourceforge.plantuml.activitydiagram2.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram2.ActivityDiagram2;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;

public class CommandNewActivity2 extends SingleLineCommand<ActivityDiagram2> {

	public CommandNewActivity2(ActivityDiagram2 diagram) {
		super(diagram, "(?i)^\\s*([-*<>^])\\s*([^\"\\s].*|\\s*\"[^\"\\s].*\")$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		if (getSystem().entities().size() == 0) {
			return CommandExecutionResult.error("Missing start keyword");
		}

		if (getSystem().isReachable() == false) {
			return CommandExecutionResult.error("Unreachable statement");
		}

		getSystem().newActivity(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(1).trim()),
				Direction.fromChar(arg.get(0).charAt(0)));
		return CommandExecutionResult.ok();
	}
}
