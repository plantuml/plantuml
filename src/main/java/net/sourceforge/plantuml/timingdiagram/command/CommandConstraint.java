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

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.command.CommandArrow;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.timingdiagram.Player;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandConstraint extends SingleLineCommand2<TimingDiagram> {

	public CommandConstraint() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandConstraint.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(2, "PART1", "(" + CommandTimeMessage.PLAYER_CODE + ")?"), //
				TimeTickBuilder.expressionAtWithArobase("TIME1"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\<"), //
				new RegexLeaf(1, "(-+)"), //
				new RegexLeaf(1, "ARROW_STYLE1", CommandArrow.getColorOrStylePattern()), //
				new RegexLeaf(1, "(-*)"), //
				new RegexLeaf("\\>"), //
				RegexLeaf.spaceZeroOrMore(), //
				TimeTickBuilder.expressionAtWithArobase("TIME2"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf(1, "MESSAGE", "(.*)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final String part1 = arg.get("PART1", 0);
		final Player player1;
		if (part1 == null) {
			player1 = diagram.getLastPlayer();
			if (player1 == null)
				return CommandExecutionResult.error("You have to provide a participant");

		} else {
			player1 = diagram.getPlayer(part1);
			if (player1 == null)
				return CommandExecutionResult.error("No such participant " + part1);

		}
		final TimeTick tick1 = TimeTickBuilder.parseTimeTick("TIME1", arg, diagram);
		if (tick1 == null)
			return CommandExecutionResult.error("Unknown time label");

		final TimeTick restore = diagram.getNow();
		diagram.updateNow(tick1);
		final TimeTick tick2 = TimeTickBuilder.parseTimeTick("TIME2", arg, diagram);
		diagram.updateNow(restore);
		if (tick2 == null)
			return CommandExecutionResult.error("Unknown time label");

		ArrowConfiguration config = ArrowConfiguration.withDirectionBoth();
		config = CommandArrow.applyStyle(arg.getLazzy("ARROW_STYLE", 0), config);

		player1.createConstraint(tick1, tick2, arg.get("MESSAGE", 0), config);
		return CommandExecutionResult.ok();
	}

}
