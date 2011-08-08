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
 * Revision $Revision: 5751 $
 *
 */
package net.sourceforge.plantuml.activitydiagram2.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram2.ActivityDiagram2;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines;

public class CommandNewMultilinesActivity2 extends CommandMultilines<ActivityDiagram2> {

	public CommandNewMultilinesActivity2(final ActivityDiagram2 system) {
		super(system, "(?i)^\\s*[-*<>^]\\s*\"\\s*.*$", "(?i)^.*\\s*\"\\s*$");
	}

	public final CommandExecutionResult execute(List<String> lines) {
		if (getSystem().entities().size() == 0) {
			return CommandExecutionResult.error("Missing start keyword");
		}

		if (getSystem().isReachable() == false) {
			return CommandExecutionResult.error("Unreachable statement");
		}
		String s = StringUtils.getMergedLines(lines);
		s = s.trim();
		assert s.startsWith("-") || s.startsWith("*") || s.startsWith("<") || s.startsWith(">") || s.startsWith("^");
		final Direction direction = Direction.fromChar(s.charAt(0));
		s = s.substring(1);
		s = s.trim();
		assert s.startsWith("\"");
		assert s.endsWith("\"");
		s = s.substring(1, s.length() - 1);

		getSystem().newActivity(s, direction);
		return CommandExecutionResult.ok();
	}

}
