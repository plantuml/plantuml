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
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * A series of a radar chart, one value per category of the a-axis:
 * <pre>
 * radar "Team 1" [4, 3, 5] #3366CC labels
 * radar "Team 2" [QA: 2, Dev: 5, Ops: 3]
 * </pre>
 */
public class CommandChartRadar extends SingleLineCommand2<ChartDiagram> {

	private static final Collection<String> FIRST_TOKENS = Arrays.asList("radar");

	public CommandChartRadar() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartRadar.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("radar"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "STEREO", "(\\<\\<.+?\\>\\>)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(ChartSeriesName.regex()), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "DATA", "\\[(.*)\\]"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "COLOR", "#([0-9a-fA-F]{6}|[0-9a-fA-F]{3}|\\w+)")), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "LABELS", "(labels)"))), //
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
			ParserPass currentPass) throws NoSuchColorException {
		final String stereo = arg.get("STEREO", 0);
		final String name = ChartSeriesName.get(arg);
		final String colorStr = arg.get("COLOR", 0);
		final ChartDataItems data = ChartDataItems.parse(arg.get("DATA", 0),
				diagram.getSkinParam().getIHtmlColorSet());
		if (data.getError() != null)
			return CommandExecutionResult.error(data.getError());

		final String seriesName = name != null ? name : "radar" + diagram.getSeries().size();
		final ChartSeries series = new ChartSeries(seriesName, ChartSeries.SeriesType.RADAR, data.getValues());
		data.applyTo(series);
		if (stereo != null)
			series.setStereotype(Stereotype.build(stereo));
		if (colorStr != null)
			series.setColor(diagram.getSkinParam().getIHtmlColorSet().getColor("#" + colorStr));
		if (arg.get("LABELS", 0) != null)
			series.setShowLabels(true);

		return diagram.addSeries(series);
	}

	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return FIRST_TOKENS;
	}

}
