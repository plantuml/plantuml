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
package net.sourceforge.plantuml.classdiagram.command;

import com.plantuml.ubrex.UnicodeBracketedExpression;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.UBrexSingleLineCommand2;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class UBrexCommandHideShow2 extends UBrexSingleLineCommand2<CucaDiagram> {

	public UBrexCommandHideShow2() {
		super(getRegexConcat());
	}

	static UnicodeBracketedExpression getRegexConcat() {
		return UBrexConcat.build( //
				new UBrexNamed("COMMAND", //
						new UBrexLeaf("【hide-class┇hide┇show-class┇show】")), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexNamed("WHAT", //
						new UBrexLeaf("【 << 〇*「〤<>」>> ┇ 〇+〴S 】 ")), //
				UBrexLeaf.end());
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// UBREX twin of CommandHideShow2: hides or shows whole elements,
		// designated by their name or by a stereotype (see
		// CucaDiagram.hideOrShow2).
		final char tmp = arg.get("COMMAND", 0).charAt(0);
		final boolean show = tmp == 's' || tmp == 'S';
		sb.append(show ? "Showing" : "Hiding");

		final String what = arg.get("WHAT", 0).trim();
		if (what.startsWith("<<"))
			sb.append(" the elements stereotyped ").append(what);
		else
			sb.append(" the elements matching '").append(what).append("'");

		// Only the first letter of COMMAND is read by executeArg, so the
		// '-class' variants behave like the plain keywords.
		if (arg.get("COMMAND", 0).contains("-"))
			sb.append(" (the '-class' suffix has no specific effect)");

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(CucaDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) {

		final char tmp = arg.get("COMMAND", 0).charAt(0);
		final boolean show = tmp == 's' || tmp == 'S';
		final String what = arg.get("WHAT", 0).trim();
		diagram.hideOrShow2(what, show);
		return CommandExecutionResult.ok();
	}
}
