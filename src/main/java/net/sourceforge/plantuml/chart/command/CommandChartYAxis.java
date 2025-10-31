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
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandChartYAxis extends SingleLineCommand2<ChartDiagram> {

	public CommandChartYAxis() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartYAxis.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "AXIS", "(y2?-axis)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "TITLE", "\"([^\"]+)\"")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(2, "RANGE", "([0-9.]+)\\s*-->\\s*([0-9.]+)")), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String axisType = arg.get("AXIS", 0);
		final String title = arg.getLazzy("TITLE", 0);
		final String minStr = arg.getLazzy("RANGE", 0);
		final String maxStr = arg.getLazzy("RANGE", 1);

		Double min = null;
		Double max = null;

		if (minStr != null && maxStr != null) {
			try {
				min = Double.parseDouble(minStr);
				max = Double.parseDouble(maxStr);
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid number format in axis range");
			}
		}

		if (axisType.startsWith("y2"))
			return diagram.setY2Axis(title, min, max);
		else
			return diagram.setYAxis(title, min, max);
	}
}
