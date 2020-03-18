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
package net.sourceforge.plantuml.help;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class CommandHelpColor extends SingleLineCommand2<Help> {

	public CommandHelpColor() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandHelpColor.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("help"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("colors?"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(Help diagram, LineLocation location, RegexResult arg) {
		diagram.add("<b>Help on colors");
		diagram.add(" ");
		diagram.add("The code of this command is located in <i>net.sourceforge.plantuml.help</i> package.");
		diagram.add("You may improve it on <i>https://github.com/plantuml/plantuml/tree/master/src/net/sourceforge/plantuml/help</i>");
		diagram.add(" ");
		diagram.add(" The possible colors are :");
		for (String type : HColorSet.instance().names()) {
			diagram.add("* " + type);
		}

		return CommandExecutionResult.ok();
	}
}
