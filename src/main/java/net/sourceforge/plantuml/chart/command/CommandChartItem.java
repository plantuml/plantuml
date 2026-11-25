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

import java.util.Collection;

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.chart.ChartDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

/**
 * A value of a series written without data, on a line of its own, as a Markdown list item or
 * as in Mermaid:
 * <pre>
 * pie "Air"
 *   * Nitrogen : 78
 *   - Oxygen : 21 #DC3912
 *   "Argon": 1
 * bar "Sales"
 *   * Jan : 10
 *   * Feb : 20
 * </pre>
 * The keys of a bar, a line, an area or a scatter are categories of the h-axis, as when they
 * are written between brackets; their values take no color, and coordinate pairs cannot be
 * written as list items.
 * A line starting with '*' or '-' may hold a value alone; a line without them must hold a key.
 * The item is read as an item between brackets (see {@link ChartDataItems}), except that a
 * key may hold a comma without quotes. This command comes after every other one, so that a
 * line such as "title Air: composition" keeps its meaning.
 */
public class CommandChartItem extends SingleLineCommand2<ChartDiagram> {

	public CommandChartItem() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandChartItem.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr( //
						new RegexConcat( //
								new RegexLeaf("[*-]"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "BULLETED", "(.*?)")), //
						new RegexLeaf(1, "KEYED", "([^*\\-%s].*:.*?)")), //
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
		final String bulleted = arg.get("BULLETED", 0);
		final String item = bulleted != null ? bulleted : arg.get("KEYED", 0);
		final ChartDataItems data = ChartDataItems.parseSingle(item, diagram.getSkinParam().getIHtmlColorSet());
		if (data.getError() != null) {
			if (startsWithPair(item))
				return CommandExecutionResult
						.error("A coordinate pair cannot be written as a list item: write [(x, y), ...]");
			return CommandExecutionResult.error(data.getError());
		}
		return diagram.addItem(data.getKey(0), data.getValues().get(0), data.getColor(0));
	}

	private static boolean startsWithPair(String item) {
		final String trimmed = item.trim();
		return trimmed.isEmpty() == false && ChartDataItems.isOpening(trimmed.charAt(0));
	}

	// A key may start with almost anything: the command is tried on every line
	@Override
	public Collection<String> mandatoryFirstTokensFast() {
		return null;
	}

}
