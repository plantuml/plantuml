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
package net.sourceforge.plantuml.activitydiagram3.command;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines3;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandActivityLong3 extends CommandMultilines3<ActivityDiagram3> {

	private final static IRegex END = new RegexConcat(//
			new RegexLeaf(1, "TEXT", "(.*)"), //
			new RegexLeaf(2, "END", CommandActivity3.endingGroup()), //
			RegexLeaf.end());

	public CommandActivityLong3() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.NONE, END);
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandActivityLong3.class.getName(), RegexLeaf.start(), //
				color().getRegex(), //
				new RegexLeaf(":"), //
				new RegexLeaf(1, "DATA", "(.*)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(ActivityDiagram3 diagram, BlocLines lines) throws NoSuchColorException {
		lines = lines.removeEmptyColumns();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());

		final RegexResult lineLast = getEndingPattern().matcher(lines.getLast().getString());

		final String end = lineLast.get("END", 0);

		Stereotype stereotype = null;
		String stereo = lineLast.get("END", 1);
		if (stereo != null)
			stereotype = Stereotype.build(stereo);

		final BoxStyle style = BoxStyle.fromString(end);
		BoxStyle.checkDeprecatedWarning(diagram, end);

		lines = lines.removeStartingAndEnding(line0.get("DATA", 0), end.length());
		return diagram.addActivity(lines.toDisplay(), style, null, colors, stereotype);
	}
}
