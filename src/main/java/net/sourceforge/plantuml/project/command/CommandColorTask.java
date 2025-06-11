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
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandColorTask extends SingleLineCommand2<GanttDiagram> {

	public CommandColorTask() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandColorTask.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "CODE", "\\[([%pLN_.]+)\\]"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(2, "COLORS", "#(\\w+)(?:/(#?\\w+))?"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(GanttDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {

		final String code = arg.get("CODE", 0);
		final Task task = diagram.getExistingTask(code);
		if (task == null) {
			return CommandExecutionResult.error("No such task " + code);
		}

		final String color1 = arg.get("COLORS", 0);
		final String color2 = arg.get("COLORS", 1);
		final HColor col1 = color1 == null ? null : diagram.getIHtmlColorSet().getColor(color1);
		final HColor col2 = color2 == null ? null : diagram.getIHtmlColorSet().getColor(color2);
		task.setColors(new CenterBorderColor(col1, col2));

		return CommandExecutionResult.ok();
	}

}
