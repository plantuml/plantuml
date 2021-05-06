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
package net.sourceforge.plantuml.wire;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

public class CommandComponent extends SingleLineCommand2<WireDiagram> {

	public CommandComponent() {
		super(false, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandComponent.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("INDENT", "([\\s\\t]*)"), //
				new RegexLeaf("\\*"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("NAME", "([\\w]+)"), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("\\["), //
						new RegexLeaf("WIDTH", "([\\d]+)"), //
						new RegexLeaf("[x*]"), //
						new RegexLeaf("HEIGHT", "([\\d]+)"), //
						new RegexLeaf("\\]")) //
				), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceZeroOrMore(), //
						new RegexLeaf("COLOR", "(#\\w+)?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(WireDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String indent = arg.get("INDENT", 0);
		final String name = arg.get("NAME", 0);

		int width = 0;
		int height = 0;
		final String widthString = arg.get("WIDTH", 0);
		final String heightString = arg.get("HEIGHT", 0);
		if (widthString != null) {
			width = Integer.parseInt(widthString);
			height = Integer.parseInt(heightString);
		}

		final String stringColor = arg.get("COLOR", 0);
		HColor color = null;
		if (stringColor != null) {
			color = HColorSet.instance().getColor(diagram.getSkinParam().getThemeStyle(), stringColor);
		}

		return diagram.addComponent(indent, name, width, height, color);
	}

}
