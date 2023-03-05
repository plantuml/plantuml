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
package net.sourceforge.plantuml.ebnf;

import java.util.Collections;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandEBnfSingleLine extends SingleLineCommand2<PSystemEbnf> {

	public CommandEBnfSingleLine() {
		super(true, getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandEBnfSingleLine.class.getName(), RegexLeaf.start(), //

				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("\\(\\*"), //
								new RegexLeaf("COMMENTA", "(.*[^%s].*)"), //
								new RegexLeaf("\\*\\)"), //
								RegexLeaf.spaceZeroOrMore())), //

				new RegexLeaf("ID", "(\\w[-\\w]*)"), //

				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("\\(\\*"), //
								new RegexLeaf("COMMENTB", "(.*[^%s].*)"), //
								new RegexLeaf("\\*\\)"), //
								RegexLeaf.spaceZeroOrMore())), //

				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("EQUALS", "(=)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("LINE", "(.*;)"), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(PSystemEbnf diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String id = arg.get("ID", 0);
		final String equals = arg.get("EQUALS", 0);
		final String line = arg.get("LINE", 0);
		final String full = id + equals + line;

		final String commentAbove = arg.get("COMMENTA", 0);
		final String commentBelow = arg.get("COMMENTB", 0);

		final StringLocated string = new StringLocated(full, location);
		return diagram.addBlocLines(BlocLines.from(Collections.singletonList(string)), commentAbove, commentBelow);
	}
}
