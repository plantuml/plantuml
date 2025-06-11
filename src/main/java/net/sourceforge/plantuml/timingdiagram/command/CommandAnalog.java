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
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.timingdiagram.PlayerAnalog;
import net.sourceforge.plantuml.timingdiagram.TimingDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAnalog extends SingleLineCommand2<TimingDiagram> {

	public CommandAnalog() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAnalog.class.getName(), RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf("analog"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "FULL", "[%g]([^%g]+)[%g]"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexOptional(//
						new RegexConcat( //
								new RegexLeaf("between"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "START", "(-?[0-9]*\\.?[0-9]+)"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("and"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "END", "(-?[0-9]*\\.?[0-9]+)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf("as"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "CODE", "([%pLN_.@]+)"), //
				StereotypePattern.optional("STEREOTYPE2"), //
				RegexLeaf.end());
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass) {
		final String compact = arg.get("COMPACT", 0);
		final String code = arg.get("CODE", 0);
		final String full = arg.get("FULL", 0);

		Stereotype stereotype = null;
		if (arg.get("STEREOTYPE", 0) != null)
			stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
		else if (arg.get("STEREOTYPE2", 0) != null)
			stereotype = Stereotype.build(arg.get("STEREOTYPE2", 0));

		final PlayerAnalog player = diagram.createAnalog(code, full, compact != null, stereotype);
		final String start = arg.get("START", 0);
		final String end = arg.get("END", 0);
		if (start != null && end != null) {
			player.setStartEnd(Double.parseDouble(start), Double.parseDouble(end));
		}
		return CommandExecutionResult.ok();
	}

}
