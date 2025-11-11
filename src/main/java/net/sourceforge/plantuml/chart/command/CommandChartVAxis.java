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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.chart.ChartAxis;
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

public class CommandChartVAxis extends SingleLineCommand2<ChartDiagram> {

	public CommandChartVAxis() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartVAxis.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "AXIS", "(v2?-axis)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "TITLE", "\"([^\"]+)\"")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(2, "RANGE", "(-?[0-9.]+)\\s*-->\\s*(-?[0-9.]+)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "LABELS", "\\[([^\\]]+)\\]")), //
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
				new RegexOptional(new RegexLeaf(1, "LABELTOP", "(label-top)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "GRID", "(grid)")), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String axisType = arg.get("AXIS", 0);
		final String title = arg.getLazzy("TITLE", 0);
		final String minStr = arg.getLazzy("RANGE", 0);
		final String maxStr = arg.getLazzy("RANGE", 1);
		final String labelsStr = arg.getLazzy("LABELS", 0);
		final String ticksStr = arg.getLazzy("TICKS", 0);
		final String spacingStr = arg.getLazzy("SPACING", 0);
		final String labelTopStr = arg.getLazzy("LABELTOP", 0);
		final String gridStr = arg.getLazzy("GRID", 0);

		// If labels are provided, this is for horizontal bar chart mode
		if (labelsStr != null) {
			final List<String> labels = parseLabels(labelsStr);
			return diagram.setYAxisLabels(labels);
		}

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

		// Parse custom ticks if present
		Map<Double, String> customTicks = null;
		if (ticksStr != null) {
			customTicks = parseCustomTicks(ticksStr);
			if (customTicks == null) {
				return CommandExecutionResult.error("Invalid tick format. Expected: [value:\"label\", ...]");
			}
		}

		// Parse tick spacing if present
		Double tickSpacing = null;
		if (spacingStr != null) {
			try {
				tickSpacing = Double.parseDouble(spacingStr);
				if (tickSpacing <= 0) {
					return CommandExecutionResult.error("Tick spacing must be greater than 0");
				}
			} catch (NumberFormatException e) {
				return CommandExecutionResult.error("Invalid number format in spacing value");
			}
		}

		// Set axis properties
		final CommandExecutionResult result;
		if (axisType.startsWith("v2"))
			result = diagram.setY2Axis(title, min, max);
		else
			result = diagram.setYAxis(title, min, max);

		// Set custom ticks if parsed successfully
		if (customTicks != null) {
			if (axisType.startsWith("v2")) {
				if (diagram.getY2Axis() != null) {
					diagram.getY2Axis().setCustomTicks(customTicks);
				}
			} else {
				diagram.getYAxis().setCustomTicks(customTicks);
			}
		}

		// Set tick spacing if parsed successfully
		if (tickSpacing != null) {
			if (axisType.startsWith("v2")) {
				if (diagram.getY2Axis() != null) {
					diagram.getY2Axis().setTickSpacing(tickSpacing);
				}
			} else {
				diagram.getYAxis().setTickSpacing(tickSpacing);
			}
		}

		// Set label position if label-top option is present
		if (labelTopStr != null) {
			if (axisType.startsWith("v2")) {
				if (diagram.getY2Axis() != null) {
					diagram.getY2Axis().setLabelPosition(ChartAxis.LabelPosition.TOP);
				}
			} else {
				diagram.getYAxis().setLabelPosition(ChartAxis.LabelPosition.TOP);
			}
		}

		// Enable grid if grid option is present
		if (gridStr != null) {
			// Both primary and secondary Y-axis use the same grid mode
			diagram.setYGridMode(ChartDiagram.GridMode.MAJOR);
		}

		return result;
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

	private Map<Double, String> parseCustomTicks(String ticksStr) {
		final Map<Double, String> ticks = new LinkedHashMap<>();
		if (ticksStr == null || ticksStr.trim().isEmpty())
			return ticks;

		// Parse format: 0:"Low", 50:"Mid", 100:"High"
		final String[] pairs = ticksStr.split(",");
		for (String pair : pairs) {
			pair = pair.trim();
			// Match value:"label"
			final int colonIndex = pair.indexOf(':');
			if (colonIndex < 0)
				return null;

			final String valueStr = pair.substring(0, colonIndex).trim();
			String label = pair.substring(colonIndex + 1).trim();

			// Remove quotes from label
			if (label.startsWith("\"") && label.endsWith("\"") && label.length() > 1) {
				label = label.substring(1, label.length() - 1);
			} else {
				return null; // Invalid format
			}

			try {
				final double value = Double.parseDouble(valueStr);
				ticks.put(value, label);
			} catch (NumberFormatException e) {
				return null;
			}
		}

		return ticks;
	}
}
