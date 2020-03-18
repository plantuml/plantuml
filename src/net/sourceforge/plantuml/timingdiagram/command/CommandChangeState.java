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
package net.sourceforge.plantuml.timingdiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.timingdiagram.Player;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;

abstract class CommandChangeState extends SingleLineCommand2<TimingDiagram> {

	CommandChangeState(IRegex pattern) {
		super(pattern);
	}

	static final String STATE_CODE = "([\\p{L}0-9_][\\p{L}0-9_.]*)";

	static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	protected CommandExecutionResult addState(TimingDiagram diagram, RegexResult arg, final Player player,
			final TimeTick now) {
		final String comment = arg.get("COMMENT", 0);
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		player.setState(now, comment, colors, getStates(arg));
		return CommandExecutionResult.ok();
	}

	private String[] getStates(RegexResult arg) {
		if (arg.get("STATE7", 0) != null) {
			final String state1 = arg.get("STATE7", 0);
			final String state2 = arg.get("STATE7", 1);
			return new String[] { state1, state2 };
		}
		return new String[] { arg.getLazzy("STATE", 0) };
	}

	static IRegex getStateOrHidden() {
		return new RegexOr(//
				new RegexLeaf("STATE1", "[%g]([^%g]*)[%g]"), //
				new RegexLeaf("STATE2", STATE_CODE), //
				new RegexLeaf("STATE3", "(\\{hidden\\})"), //
				new RegexLeaf("STATE4", "(\\{\\.\\.\\.\\})"), //
				new RegexLeaf("STATE5", "(\\{-\\})"), //
				new RegexLeaf("STATE6", "(\\{\\?\\})"), //
				new RegexLeaf("STATE7", "(?:\\{" + STATE_CODE + "," + STATE_CODE + "\\})") //
		);
	}

}
