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

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.chart.ChartSeries;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * A pie, whose slices are the values of its data:
 * <pre>
 * pie "Air" [Nitrogen: 78, Oxygen: 21, Argon: 1] labels percent donut 40%
 * </pre>
 * Options, in any order: "labels" shows the value of each slice, "percent" its share,
 * "donut" (optionally followed by the size of the hole, 50% by default) draws a ring.
 */
public class CommandChartPie extends SingleLineCommand2<ChartDiagram> {

	private static final Collection<String> FIRST_TOKENS = Arrays.asList("pie");

	public CommandChartPie() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartPie.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("pie"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "STEREO", "(\\<\\<.+?\\>\\>)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(ChartSeriesName.regex()), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "DATA", "\\[(.*)\\]"), //
				new RegexLeaf(1, "OPTIONS", "((?:\\s+(?:labels|percent|donut(?:\\s+[0-9.]+%)?))*)"), //
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
		final String stereo = arg.get("STEREO", 0);
		final String name = ChartSeriesName.get(arg);
		final ChartDataItems data = ChartDataItems.parse(arg.get("DATA", 0),
				diagram.getSkinParam().getIHtmlColorSet());
		if (data.getError() != null)
			return CommandExecutionResult.error(data.getError());

		final ChartSeries series = new ChartSeries(name, ChartSeries.SeriesType.PIE, data.getValues());
		data.applyTo(series);
		if (stereo != null)
			series.setStereotype(Stereotype.build(stereo));

		final String options = arg.get("OPTIONS", 0);
		if (options != null) {
			final String[] words = options.trim().split("\\s+");
			for (int i = 0; i < words.length; i++) {
				if (words[i].equals("labels")) {
					series.setShowLabels(true);
				} else if (words[i].equals("percent")) {
					series.setShowPercent(true);
				} else if (words[i].equals("donut")) {
					double hole = 50;
					if (i + 1 < words.length && words[i + 1].endsWith("%")) {
						i++;
						try {
							hole = Double.parseDouble(words[i].substring(0, words[i].length() - 1));
						} catch (NumberFormatException e) {
							return CommandExecutionResult.error("Invalid donut size " + words[i]);
						}
					}
					if (hole <= 0 || hole > 90)
						return CommandExecutionResult.error("The donut size must be between 1% and 90%");
					series.setDonutHole(hole / 100);
				}
			}
		}

		return diagram.addSeries(series);
	}

	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return FIRST_TOKENS;
	}

}
