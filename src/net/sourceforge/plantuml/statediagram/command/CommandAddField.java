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
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.statediagram.StateDiagram;

public class CommandAddField extends SingleLineCommand2<StateDiagram> {

	public CommandAddField() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAddField.class.getName(), RegexLeaf.start(), //
				new RegexOr( //
						new RegexLeaf("CODE3", "([\\p{L}0-9_.]+)"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("FIELD", "(.*)"), RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg) {
		final String codeString = arg.getLazzy("CODE", 0);
		final String field = arg.get("FIELD", 0);

		Ident ident = diagram.buildLeafIdent(codeString);
		if (diagram.V1972()) {
			// This is very bad. xi04 xc06
			if (ident.parent().getLast().equals(codeString)) {
				ident = ident.parent();
			}
		}
		final Code code = diagram.V1972() ? ident : diagram.buildCode(codeString);
		final IEntity entity = diagram.getOrCreateLeaf(ident, code, null, null);

		entity.getBodier().addFieldOrMethod(field);
		return CommandExecutionResult.ok();
	}

}
