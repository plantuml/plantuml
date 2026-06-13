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

import java.math.BigDecimal;

import net.sourceforge.plantuml.annotation.Explain;
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
								new RegexLeaf(1, "COMPACT", "(compact)"), //
								RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(0, "TYPE", "clock"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOptional(new RegexConcat( //
						new RegexLeaf(1, "FULL", "[%g]([^%g]+)[%g]"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("as"), //
						RegexLeaf.spaceOneOrMore())), //
				new RegexLeaf(1, "CODE", "([%pLN_.@]+)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("with"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("period"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "PERIOD", "([0-9]+(?:\\.[0-9]+)?)"), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("pulse"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "PULSE", "([0-9]+(?:\\.[0-9]+)?)") //
				)), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("offset"), //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf(1, "OFFSET", "([0-9]+(?:\\.[0-9]+)?)") //
				)), //
				RegexLeaf.spaceZeroOrMore(), //
				StereotypePattern.optional("STEREO"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final String compact = arg.get("COMPACT", 0);
		final String code = arg.get("CODE", 0);
		final String full = arg.get("FULL", 0);
		final String period = arg.get("PERIOD", 0);
		final String pulse = arg.get("PULSE", 0);
		final String offset = arg.get("OFFSET", 0);
		final String stereotype = arg.get("STEREO", 0);

		final StringBuilder sb = new StringBuilder();
		if (compact != null)
			sb.append("Creating a compact clock '");
		else
			sb.append("Creating a clock '");
		sb.append(code).append("'");
		if (full != null)
			sb.append(" displayed as \"").append(full).append("\"");
		sb.append(" with period ").append(period);
		// PULSE and OFFSET default to 0 when omitted
		if (pulse != null)
			sb.append(" pulse ").append(pulse);
		if (offset != null)
			sb.append(" offset ").append(offset);
		if (stereotype != null)
			sb.append(" stereotyped ").append(stereotype);
		return sb.toString();
	}

	@Override
	final protected CommandExecutionResult executeArg(TimingDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		final String compact = arg.get("COMPACT", 0);
		final String code = arg.get("CODE", 0);
		final BigDecimal period = new BigDecimal(arg.get("PERIOD", 0));
		final BigDecimal pulse = getBigDecimal(arg.get("PULSE", 0));
		final BigDecimal offset = getBigDecimal(arg.get("OFFSET", 0));
		String full = arg.get("FULL", 0);
		if (full == null)
			full = "";
		final String stereotypeString = arg.get("STEREO", 0);
		Stereotype stereotype = null;
		if (stereotypeString != null)
			stereotype = Stereotype.build(stereotypeString);
		diagram.createPlayerClock(code, full, period, pulse, offset, compact != null, stereotype);
		return CommandExecutionResult.ok();
	}

	private BigDecimal getBigDecimal(String value) {
		if (value == null)
			return BigDecimal.ZERO;
		return new BigDecimal(value);
	}

}
