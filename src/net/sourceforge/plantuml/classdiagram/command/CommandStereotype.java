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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.Stereotype;

public class CommandStereotype extends SingleLineCommand2<ClassDiagram> {

	public CommandStereotype() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandStereotype.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("NAME", "([\\p{L}0-9_.]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREO", "(\\<\\<.*\\>\\>)"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg) {
		final String name = arg.get("NAME", 0);
		final Ident ident = diagram.buildLeafIdent(name);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(name);
		final String stereotype = arg.get("STEREO", 0);
		final IEntity entity = diagram.getOrCreateLeaf(ident, code, null, null);
		entity.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
				.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
				.getIHtmlColorSet()));
		return CommandExecutionResult.ok();
	}

}
