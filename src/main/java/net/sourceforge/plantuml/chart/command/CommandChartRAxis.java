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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import net.sourceforge.plantuml.annotation.Explain;
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

/**
 * The radial axis of a radar: its scale, and the rings drawn at its ticks.
 * <pre>
 * r-axis 0 --> 5 spacing 1 grid circle
 * r-axis 0 --> 100 ticks [50: "Mid", 100: "Max"]
 * </pre>
 * The rings are always drawn: "grid polygon" (the default) or "grid circle" gives their shape.
 */
public class CommandChartRAxis extends SingleLineCommand2<ChartDiagram> {

	private static final Collection<String> FIRST_TOKENS = Arrays.asList("r");

	public CommandChartRAxis() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartRAxis.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("r-axis"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(2, "RANGE", "(-?[0-9.]+)\\s*-?->\\s*(-?[0-9.]+)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("ticks"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "TICKS", "\\[(.*)\\]"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("spacing"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "SPACING", "([0-9.]+)"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("grid"), //
						new RegexOptional(new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "GRID", "(circle|polygon)"))))), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		return null;
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String minStr = arg.get("RANGE", 0);
		final String maxStr = arg.get("RANGE", 1);
		final String ticksStr = arg.get("TICKS", 0);
		final String spacingStr = arg.get("SPACING", 0);
		final String gridStr = arg.get("GRID", 0);

		Double min = null;
		Double max = null;
		if (minStr != null && maxStr != null)
			try {
				min = Double.parseDouble(minStr);
				max = Double.parseDouble(maxStr);
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid number format in axis range");
			}

		Map<Double, String> customTicks = null;
		if (ticksStr != null) {
			customTicks = CommandChartVAxis.parseCustomTicks(ticksStr);
			if (customTicks == null)
				return CommandExecutionResult.error("Invalid tick format. Expected: [value:\"label\", ...]");
		}

		Double tickSpacing = null;
		if (spacingStr != null)
			try {
				tickSpacing = Double.parseDouble(spacingStr);
				if (tickSpacing <= 0)
					return CommandExecutionResult.error("Tick spacing must be greater than 0");
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid number format in spacing value");
			}

		final Boolean circleGrid = gridStr == null ? null : Boolean.valueOf(gridStr.equals("circle"));
		return diagram.setRadialAxis(min, max, customTicks, tickSpacing, circleGrid);
	}

	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return FIRST_TOKENS;
	}

}
