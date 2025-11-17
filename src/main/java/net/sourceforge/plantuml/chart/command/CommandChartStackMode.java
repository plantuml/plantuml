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
 * Original Author:  David Fyfe
 *
 */
package net.sourceforge.plantuml.chart.command;

import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandChartStackMode extends SingleLineCommand2<ChartDiagram> {

	public CommandChartStackMode() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartStackMode.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("stackMode"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "MODE", "(grouped|stacked)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String mode = arg.get("MODE", 0);

		ChartDiagram.StackMode stackMode;
		switch (mode.toLowerCase()) {
		case "grouped":
			stackMode = ChartDiagram.StackMode.GROUPED;
			break;
		case "stacked":
			stackMode = ChartDiagram.StackMode.STACKED;
			break;
		default:
			return CommandExecutionResult.error("Invalid stack mode: " + mode);
		}

		return diagram.setStackMode(stackMode);
	}
}
