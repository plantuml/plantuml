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
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandClock extends SingleLineCommand2<TimingDiagram> {

	public CommandClock() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandClock.class.getName(), RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf("TYPE", "clock"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf("FULL", "[%g]([^%g]+)[%g]"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf("CODE", "([%pLN_.@]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("with"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("period"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("PERIOD", "([0-9]+)"), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("pulse"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("PULSE", "([0-9]+)") //
				)), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("offset"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("OFFSET", "([0-9]+)") //
				)), //
				RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg) {
		final String compact = arg.get("COMPACT", 0);
		final String code = arg.get("CODE", 0);
		final int period = Integer.parseInt(arg.get("PERIOD", 0));
		final int pulse = getInt(arg.get("PULSE", 0));
		final int offset = getInt(arg.get("OFFSET", 0));
		String full = arg.get("FULL", 0);
		if (full == null)
			full = "";
		return diagram.createClock(code, full, period, pulse, offset, compact != null);
	}

	private int getInt(String value) {
		if (value == null)
			return 0;
		return Integer.parseInt(value);
	}

}
