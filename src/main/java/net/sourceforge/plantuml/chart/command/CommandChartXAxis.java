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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandChartXAxis extends SingleLineCommand2<ChartDiagram> {

	public CommandChartXAxis() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartXAxis.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("x-axis"), //
				RegexLeaf.spaceZeroOrMore(), //
				new net.sourceforge.plantuml.regex.RegexOptional(new RegexLeaf(1, "TITLE", "\"([^\"]+)\"")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "DATA", "\\[(.*)\\]"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String title = arg.getLazzy("TITLE", 0);
		final String data = arg.get("DATA", 0);
		final List<String> labels = parseLabels(data);
		diagram.setXAxisTitle(title);
		return diagram.setXAxisLabels(labels);
	}

	private List<String> parseLabels(String data) {
		final List<String> result = new ArrayList<>();
		if (data == null || data.trim().isEmpty())
			return result;

		// Split by comma, handling quoted strings
		final String[] parts = data.split(",");
		for (String part : parts) {
			String label = part.trim();
			// Remove quotes if present
			if (label.startsWith("\"") && label.endsWith("\""))
				label = label.substring(1, label.length() - 1);
			result.add(label);
		}
		return result;
	}
}
