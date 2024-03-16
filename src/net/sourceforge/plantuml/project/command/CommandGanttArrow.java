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
package net.sourceforge.plantuml.project.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.project.GanttConstraint;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandGanttArrow extends SingleLineCommand2<GanttDiagram> {

	public CommandGanttArrow() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandGanttArrow.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("CODE1", "([%pLN_.]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("(-+)"), //
				new RegexLeaf("ARROW_STYLE", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf("(-*)"), //
				new RegexLeaf("\\>"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("CODE2", "([%pLN_.]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram diagram, LineLocation location, RegexResult arg) {

		final String code1 = arg.get("CODE1", 0);
		final String code2 = arg.get("CODE2", 0);
		final Task task1 = diagram.getExistingTask(code1);
		if (task1 == null) {
			return CommandExecutionResult.error("No such task " + code1);
		}
		final Task task2 = diagram.getExistingTask(code2);
		if (task2 == null) {
			return CommandExecutionResult.error("No such task " + code2);
		}

		final GanttConstraint link = diagram.forceTaskOrder(task1, task2);
		link.applyStyle(arg.get("ARROW_STYLE", 0));

		return CommandExecutionResult.ok();
	}

}
