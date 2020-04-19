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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageEmpty extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageEmpty() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackageEmpty.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("package"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("DISPLAY", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE", "([\\p{L}0-9_.]+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("COLOR", "(#[0-9a-fA-F]{6}|#?\\w+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\}"), //
				RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg) {
		final String idShort;
		final String display;
		if (arg.get("CODE", 0) == null) {
			if (StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0)).length() == 0) {
				idShort = "##" + UniqueSequence.getValue();
				display = null;
			} else {
				idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
				display = idShort;
			}
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("DISPLAY", 0));
			idShort = arg.get("CODE", 0);
		}
		final IGroup currentPackage = diagram.getCurrentGroup();
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		diagram.gotoGroup(ident, code, Display.getWithNewlines(display), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		final String color = arg.get("COLOR", 0);
		if (color != null) {
			p.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(color));
		}
		diagram.endGroup();
		return CommandExecutionResult.ok();
	}

}
