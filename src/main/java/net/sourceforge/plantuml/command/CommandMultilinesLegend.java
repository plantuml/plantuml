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
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandMultilinesLegend extends CommandMultilines2<TitledDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^end[%s]?legend$"));

	public static final CommandMultilinesLegend ME = new CommandMultilinesLegend();

	private CommandMultilinesLegend() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandMultilinesLegend.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("legend"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "VALIGN", "(top|bottom)") //
						)), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "ALIGN", "(left|right|center)") //
						)), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(TitledDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		final LineLocation location = lines.getLocation();
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final String align = line0.get("ALIGN", 0);
		final String valign = line0.get("VALIGN", 0);
		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			final VerticalAlignment valignment = VerticalAlignment.fromString(valign);
			HorizontalAlignment alignment = HorizontalAlignment.fromString(align);
			if (alignment == null)
				alignment = HorizontalAlignment.CENTER;

			diagram.setLegend(DisplayPositioned.single(location, strings.replaceBackslashT(), alignment, valignment));
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("No legend defined");
	}
}
