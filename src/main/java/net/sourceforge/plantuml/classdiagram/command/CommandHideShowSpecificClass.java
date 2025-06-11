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

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.NameAndCodeParser;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandHideShowSpecificClass extends SingleLineCommand2<CucaDiagram> {

	public CommandHideShowSpecificClass() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandHideShowSpecificClass.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "COMMAND", "(hide|show)"), //
				RegexLeaf.spaceOneOrMore(), //
				NameAndCodeParser.codeForClass(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(CucaDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass) {

		// final String codeString = arg.get("CODE", 0);
		// if (codeString.equals("class")) {
		// diagram.hideOrShow(LeafType.CLASS, arg.get("COMMAND",
		// 0).equalsIgnoreCase("show"));
		// } else if (codeString.equals("interface")) {
		// diagram.hideOrShow(LeafType.INTERFACE, arg.get("COMMAND",
		// 0).equalsIgnoreCase("show"));
		// } else {
		// final Code code = diagram.buildCode(codeString);
		// IEntity hidden = diagram.getEntityFactory().getLeafsget(code);
		// if (hidden == null) {
		// hidden = diagram.getEntityFactory().getGroupsget(code);
		// }
		// if (hidden == null) {
		// return CommandExecutionResult.error("Class/Package does not exist : " +
		// code.getFullName());
		// }
		// diagram.hideOrShow(hidden, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		// }
		// return CommandExecutionResult.ok();
		throw new UnsupportedOperationException();
	}
}
