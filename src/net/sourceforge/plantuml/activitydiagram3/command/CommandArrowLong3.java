/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.graphic.Rainbow;

public class CommandArrowLong3 extends CommandMultilines2<ActivityDiagram3> {

	public CommandArrowLong3() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "^(.*);$";
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandArrowLong3.class.getName(), RegexLeaf.start(), //
				new RegexOr(//
						new RegexLeaf("->"), //
						new RegexLeaf("COLOR", CommandLinkElement.STYLE_COLORS_MULTIPLES)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LABEL", "(.*)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(ActivityDiagram3 diagram, BlocLines lines) {
		lines = lines.removeEmptyColumns();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		// final HtmlColor color = diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(line0.get("COLOR", 0));
		// diagram.setColorNextArrow(Rainbow.fromColor(color));
		final String colorString = line0.get("COLOR", 0);
		if (colorString != null) {
			Rainbow rainbow = Rainbow.build(diagram.getSkinParam(), colorString, diagram.getSkinParam()
					.colorArrowSeparationSpace());
			diagram.setColorNextArrow(rainbow);
		}
		lines = lines.removeStartingAndEnding(line0.get("LABEL", 0), 1);
		diagram.setLabelNextArrow(lines.toDisplay());
		return CommandExecutionResult.ok();
	}

	private <CS extends CharSequence> void removeStarting(List<CS> lines, String data) {
		if (lines.size() == 0) {
			return;
		}
		lines.set(0, (CS) data);
	}

	private <CS extends CharSequence> void removeEnding(List<CS> lines) {
		if (lines.size() == 0) {
			return;
		}
		final int n = lines.size() - 1;
		final CharSequence s = lines.get(n);
		lines.set(n, (CS) s.subSequence(0, s.length() - 1));
	}

}
