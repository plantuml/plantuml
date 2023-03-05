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
package net.sourceforge.plantuml.wire;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandWLink extends SingleLineCommand2<WireDiagram> {

	public CommandWLink() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandWLink.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("NAME1", "([\\w][.\\w]*)"), //
				new RegexOptional(new RegexConcat(//
						new RegexLeaf("\\("), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("X1", "(-?\\d+(%|%[-+]\\d+)?)"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf(","), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("Y1", "(-?\\d+(%|%[-+]\\d+)?)"), //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("\\)") //
				)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STYLE", "(\\<?[-=]{1,2}\\>|\\<?[-=]{1,2})"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NAME2", "([\\w][.\\w]*)"), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("COLOR", "(#\\w+)?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("MESSAGE", "(?::[%s]*(.*))?"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(WireDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {

		final String name1 = arg.get("NAME1", 0);
		final String x1 = arg.get("X1", 0);
		final String y1 = arg.get("Y1", 0);

		final String name2 = arg.get("NAME2", 0);
		final String style = arg.get("STYLE", 0);
		final WLinkType type = WLinkType.from(style);
		final WArrowDirection direction = WArrowDirection.from(style);
		final WOrientation orientation = WOrientation.from(style);

		final String stringColor = arg.get("COLOR", 0);
		HColor color = null;
		if (stringColor != null)
			color = HColorSet.instance().getColor(stringColor);

		final Display label;
		if (arg.get("MESSAGE", 0) == null) {
			label = Display.NULL;
		} else {
			final String message = arg.get("MESSAGE", 0);
			label = Display.getWithNewlines(message);
		}

		if (orientation == WOrientation.VERTICAL)
			return diagram.vlink(name1, x1, y1, name2, type, direction, color, label);

		return diagram.hlink(name1, x1, y1, name2, type, direction, color, label);
	}

}
