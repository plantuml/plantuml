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
package net.sourceforge.plantuml.timingdiagram.command;

import java.util.StringTokenizer;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.timingdiagram.Player;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandDefineStateShort extends SingleLineCommand2<TimingDiagram> {

	public CommandDefineStateShort() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandDefineStateShort.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("PLAYER", "([%pLN_.@]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("has"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("STATE", "([%pLN_.@]+)"), //
				new RegexLeaf("STATES", "((,([%pLN_.@]+))*)"), RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg) {
		final String playerCode = arg.get("PLAYER", 0);
		final Player player = diagram.getPlayer(playerCode);
		if (player == null) {
			return CommandExecutionResult.error("Unknown " + playerCode);
		}
		final String stateCode = arg.get("STATE", 0);
		player.defineState(stateCode, stateCode);
		final String states = arg.get("STATES", 0);
		if (states != null) {
			for (StringTokenizer st = new StringTokenizer(states, ","); st.hasMoreTokens();) {
				final String token = st.nextToken();
				player.defineState(token, token);
			}
		}

		return CommandExecutionResult.ok();
	}
}
