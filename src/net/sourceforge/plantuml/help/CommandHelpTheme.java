/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
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
 * Original Author:  Matthew Leather
 *
 *
 */
package net.sourceforge.plantuml.help;

import java.io.IOException;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.theme.ThemeUtils;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.Log;

public class CommandHelpTheme extends SingleLineCommand2<Help> {

	public CommandHelpTheme() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandHelpTheme.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("help"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("themes?"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(Help diagram, LineLocation location, RegexResult arg) {
		diagram.add("<b>Help on themes");
		diagram.add(" ");
		diagram.add(" The possible themes are :");

		try {
			for (String theme : ThemeUtils.getAllThemeNames()) {
				diagram.add("* " + theme);
			}
		} catch (IOException e) {
			final String message = "Unexpected error listing themes: " + e.getMessage();
			Log.error(message);
			Logme.error(e);
			return CommandExecutionResult.error(message);
		}

		return CommandExecutionResult.ok();
	}
}
