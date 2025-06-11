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
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines3;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandBackwardLong3 extends CommandMultilines3<ActivityDiagram3> {

	private final static IRegex END = new RegexConcat(//
			new RegexLeaf(1, "TEXT", "(.*)"), //
			new RegexLeaf(2, "END", CommandActivity3.endingGroup()), //
			RegexLeaf.end());

	public CommandBackwardLong3() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandBackwardLong3.class.getName(), RegexLeaf.start(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("backward"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "DATA", "(.*)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(ActivityDiagram3 diagram, BlocLines lines) throws NoSuchColorException {
		lines = lines.removeEmptyColumns();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final RegexResult lineLast = getEndingPattern().matcher(lines.getLast().getString());
		final String end = lineLast.get("END", 0);
		final String stereo = lineLast.get("END", 1);

		Stereotype stereotype = null;
		if (stereo != null)
			stereotype = Stereotype.build(stereo);

		final BoxStyle style = BoxStyle.fromString(end);
		BoxStyle.checkDeprecatedWarning(diagram, end);

		lines = lines.removeStartingAndEnding(line0.get("DATA", 0), end.length());

		final LinkRendering in = LinkRendering.none();
		final LinkRendering out = LinkRendering.none();

		return diagram.backward(lines.toDisplay(), style, in, out, stereotype);
	}
}
