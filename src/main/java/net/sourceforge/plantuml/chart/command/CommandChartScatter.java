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
import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandChartScatter extends SingleLineCommand2<ChartDiagram> {

	public CommandChartScatter() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartScatter.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("scatter"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "STEREO", "(\\<\\<.+?\\>\\>)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "NAME", "\"([^\"]+)\"")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "DATA", "\\[(.*)\\]"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "COLOR", "#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|\\w+)")), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(0, "Y2", "y2"))), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(0, "LABELS", "labels"))), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "MARKER", "<<(circle|square|triangle)>>"))), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ChartDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
				final String stereo = arg.getLazzy("STEREO", 0);
		final String name = arg.getLazzy("NAME", 0);
		final String data = arg.get("DATA", 0);
		final String colorStr = arg.getLazzy("COLOR", 0);
		final String markerStr = arg.getLazzy("MARKER", 0);

		final List<Double> values = parseValues(data);
		if (values == null)
			return CommandExecutionResult.error("Invalid number format in scatter data");

		final String seriesName = name != null ? name : "scatter" + diagram.getSeries().size();
		final ChartSeries series = new ChartSeries(seriesName, ChartSeries.SeriesType.SCATTER, values);

		if (stereo != null) {
			series.setStereotype(Stereotype.build(stereo));
		}

		if (colorStr != null) {
			final HColor color = diagram.getSkinParam().getIHtmlColorSet().getColor("#" + colorStr);
			series.setColor(color);
		}

		// Set marker shape if specified
		if (markerStr != null) {
			switch (markerStr.toLowerCase()) {
			case "square":
				series.setMarkerShape(ChartSeries.MarkerShape.SQUARE);
				break;
			case "triangle":
				series.setMarkerShape(ChartSeries.MarkerShape.TRIANGLE);
				break;
			case "circle":
			default:
				series.setMarkerShape(ChartSeries.MarkerShape.CIRCLE);
				break;
			}
		}

		// Check if this scatter should use the secondary y-axis
		final String y2Str = arg.getLazzy("Y2", 0);
		if (y2Str != null) {
			series.setUseSecondaryAxis(true);
		}

		// Check if labels keyword was present
		final String labelsStr = arg.getLazzy("LABELS", 0);
		if (labelsStr != null) {
			series.setShowLabels(true);
		}

		return diagram.addSeries(series);
	}

	private List<Double> parseValues(String data) {
		final List<Double> result = new ArrayList<>();
		if (data == null || data.trim().isEmpty())
			return result;

		final String[] parts = data.split(",");
		for (String part : parts) {
			try {
				result.add(Double.parseDouble(part.trim()));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return result;
	}
}
