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
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandScalePixel extends SingleLineCommand2<TimingDiagram> {

	public CommandScalePixel() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandScalePixel.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("scale"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "TICK", "(\\d+)"), //
				new RegexOptional(//
					new RegexConcat( //
								RegexLeaf.spaceZeroOrOne(), //
								new RegexLeaf(1, "UNIT", "([smhdDyY])"))), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("as"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "PIXEL", "(\\d+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("pixels?"), //
				RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass) {
		final long tick = Long.parseLong(arg.get("TICK", 0)) * getUnitFactor(arg.get("UNIT", 0));
		final long pixel = Long.parseLong(arg.get("PIXEL", 0));
		if (tick <= 0 || pixel <= 0)
			return CommandExecutionResult.error("Bad value");

		diagram.scaleInPixels(tick, pixel);
		return CommandExecutionResult.ok();
	}

	private long getUnitFactor(String unit) {
		if (unit == null)
			return 1;

		switch (unit) {
		case "s":
			return 1;
		case "m":
			return 60;
		case "h":
			return 3600;
		case "D":
		case "d":
			return 3600L * 24L;
		case "Y":
		case "y":
			return 3600L * 8766L; // 24 * 365.25 = 8766;
		default:
			return 1;
		}
	}

}
