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

public class CommandChartLegend extends SingleLineCommand2<ChartDiagram> {

	public CommandChartLegend() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartLegend.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("legend"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(0, "POSITION", "(left|right|top|bottom)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		System.err.println("DEBUG CommandChartLegend: CALLED!");
		final String position = arg.get("POSITION", 1);
		System.err.println("DEBUG CommandChartLegend: position = '" + position + "'");

		ChartDiagram.LegendPosition legendPosition;
		switch (position.toLowerCase()) {
		case "left":
			legendPosition = ChartDiagram.LegendPosition.LEFT;
			break;
		case "right":
			legendPosition = ChartDiagram.LegendPosition.RIGHT;
			break;
		case "top":
			legendPosition = ChartDiagram.LegendPosition.TOP;
			break;
		case "bottom":
			legendPosition = ChartDiagram.LegendPosition.BOTTOM;
			break;
		default:
			return CommandExecutionResult.error("Invalid legend position: " + position);
		}

		return diagram.setLegendPosition(legendPosition);
	}
}
