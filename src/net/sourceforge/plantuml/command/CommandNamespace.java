/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 3979 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;

public class CommandNamespace extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandNamespace() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^namespace\\s+"), //
				new RegexLeaf("NAME", "([\\p{L}0-9_][\\p{L}0-9_.:]*)"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("COLOR", "(#\\w+[-\\\\|/]?\\w+)?"), //
				new RegexLeaf("\\s*\\{?$"));
	}


	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, RegexResult arg) {
		final Code code = Code.of(arg.get("NAME", 0));
		final IGroup currentPackage = diagram.getCurrentGroup();
		final IEntity p = diagram.getOrCreateGroup(code, Display.getWithNewlines(code), code.getCode(),
				GroupType.PACKAGE, currentPackage);
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype));
		}
		final String color = arg.get("COLOR", 0);
		if (color != null) {
			p.setSpecificBackcolor(HtmlColorUtils.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
