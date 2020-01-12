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
package net.sourceforge.plantuml.activitydiagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandPartition extends SingleLineCommand2<ActivityDiagram> {

	public CommandPartition() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPartition.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("partition"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|\\S+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(//
						color().getRegex(), //
						new RegexLeaf("LEGACYCOLORIGNORED", "(#[0-9a-fA-F]{6}|#?\\w+)?")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{?"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg) {
		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		final IGroup currentPackage = diagram.getCurrentGroup();
		diagram.gotoGroup(ident, code, Display.getWithNewlines(code), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		if (colors.isEmpty() == false) {
			p.setColors(colors);
		}
		if (arg.get("STEREOTYPE", 0) != null) {
			p.setStereotype(new Stereotype(arg.get("STEREOTYPE", 0)));
		}

		return CommandExecutionResult.ok();
	}

}
