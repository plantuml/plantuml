/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.timingdiagram;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandChangeState1 extends SingleLineCommand2<TimingDiagram> {

	private static final String STATE_CODE = "([\\p{L}0-9_][\\p{L}0-9_.]*)";

	public CommandChangeState1() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("CODE", CommandTimeMessage.PLAYER_CODE), //
				new RegexLeaf("[%s]*is[%s]*"), //
				getStateOrHidden(), //
				new RegexLeaf("[%s]*"), //
				color().getRegex(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("COMMENT", "(?:[%s]*:[%s]*(.*?))?"), //
				new RegexLeaf("[%s]*$"));
	}

	static IRegex getStateOrHidden() {
		return new RegexOr(//
				new RegexLeaf("STATE", STATE_CODE), //
				new RegexLeaf("\\{[^\\}]*?\\}"), //
				new RegexLeaf("[^:\\w]*?") //
		);
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, RegexResult arg) {
		final String code = arg.get("CODE", 0);
		final Player player = diagram.getPlayer(code);
		if (player == null) {
			return CommandExecutionResult.error("Unkown \"" + code + "\"");
		}
		final String comment = arg.get("COMMENT", 0);
		final TimeTick now = diagram.getNow();
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		player.setState(now, arg.get("STATE", 0), comment, colors);
		return CommandExecutionResult.ok();
	}

}
