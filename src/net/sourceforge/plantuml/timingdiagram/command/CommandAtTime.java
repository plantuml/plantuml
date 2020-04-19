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

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.timingdiagram.TimeTick;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;

public class CommandAtTime extends SingleLineCommand2<TimingDiagram> {

	public CommandAtTime() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAtTime.class.getName(), RegexLeaf.start(), //
				TimeTickBuilder.expressionAtWithArobase("TIME"), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(":"), //
						new RegexLeaf("CODE", "([\\p{L}0-9_.]+)") //
				)), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg) {
		final TimeTick timeTick = TimeTickBuilder.parseTimeTick("TIME", arg, diagram);
		if (timeTick == null) {
			return CommandExecutionResult.error("What time?");
		}
		final String code = arg.get("CODE", 0);
		diagram.addTime(timeTick, code);
		return CommandExecutionResult.ok();
	}

}
