/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5019 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;

public class CommandHideShowSpecificClass extends SingleLineCommand2<CucaDiagram> {

	public CommandHideShowSpecificClass() {
		super(getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("COMMAND", "(hide|show)"), //
				new RegexLeaf("[%s]+"), //
				new RegexLeaf("CODE", "(" + CommandCreateClass.CODE + ")"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(CucaDiagram diagram, RegexResult arg) {

		final String codeString = arg.get("CODE", 0);
		if (codeString.equals("class")) {
			diagram.hideOrShow(LeafType.CLASS, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		} else if (codeString.equals("interface")) {
			diagram.hideOrShow(LeafType.INTERFACE, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		} else {
			final Code code = Code.of(codeString);
			final ILeaf leaf = diagram.getEntityFactory().getLeafs().get(code);
			if (leaf == null) {
				return CommandExecutionResult.error("Class does not exist : " + code.getFullName());
			}
			diagram.hideOrShow(leaf, arg.get("COMMAND", 0).equalsIgnoreCase("show"));
		}
		return CommandExecutionResult.ok();
	}
}
