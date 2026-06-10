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
package net.sourceforge.plantuml.sequencediagram.command;

import java.text.DecimalFormat;

import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.sequencediagram.DottedNumber;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAutonumber extends SingleLineCommand2<SequenceDiagram> {

	public CommandAutonumber() {
		super(getConcat());
	}

	private static RegexConcat getConcat() {
		return RegexConcat.build(CommandAutonumber.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("autonumber"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "START", "(\\d(?:(?:[^%pLN%s]+|\\d+)*\\d)?)?"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "STEP", "(\\d+)") //
						)), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "FORMAT", "[%g]([^%g]+)[%g]") //
						)), //
				RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// Starts (or restarts) the automatic numbering of messages. Defaults
		// in executeArg: start at 1, increment by 1, format "<b>0</b>".
		sb.append("Starting automatic numbering of messages");

		// The starting number may be dotted, like '1.1.1' (see DottedNumber).
		final String start = arg.get("START", 0);
		if (start != null)
			sb.append(" from ").append(start);

		final String step = arg.get("STEP", 0);
		if (step != null)
			sb.append(", incrementing by ").append(step);

		// The format is a java.text.DecimalFormat pattern, possibly with
		// creole markup, like "<b>(0)</b>".
		final String format = arg.get("FORMAT", 0);
		if (format != null)
			sb.append(", with format \"").append(format).append("\"");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(SequenceDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {
		DottedNumber start = DottedNumber.create("1");
		final String arg0 = arg.get("START", 0);
		// System.err.println("arg0=" + arg0);
		if (arg0 != null) {
			start = DottedNumber.create(arg0);
		}
		// System.err.println("start=" + start);
		int inc = 1;
		final String arg1 = arg.get("STEP", 0);
		if (arg1 != null) {
			inc = Integer.parseInt(arg1);
		}

		final String arg2 = arg.get("FORMAT", 0);
		final String df = arg2 == null ? "<b>0</b>" : arg2;
		final DecimalFormat decimalFormat;
		try {
			decimalFormat = new DecimalFormat(df);
		} catch (IllegalArgumentException e) {
			return CommandExecutionResult.error("Error in pattern : " + df);
		}

		diagram.autonumberGo(start, inc, decimalFormat);
		return CommandExecutionResult.ok();
	}

}
