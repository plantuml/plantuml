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
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandArchimatePackage extends SingleLineCommand2<DescriptionDiagram> {

	public CommandArchimatePackage() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandArchimatePackage.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(0, "SYMBOL", "archimate"), //
				RegexLeaf.spaceOneOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexLeaf(1, "CODE1", CommandCreateElementFull.CODE_WITH_QUOTE), //
						new RegexConcat(//
								new RegexLeaf(1, "DISPLAY2", CommandCreateElementFull.DISPLAY), //
								StereotypePattern.optionalArchimate("STEREOTYPE2"), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "CODE2", CommandCreateElementFull.CODE)), //
						new RegexConcat(//
								new RegexLeaf(1, "CODE3", CommandCreateElementFull.CODE), //
								StereotypePattern.optionalArchimate("STEREOTYPE3"), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf(1, "DISPLAY3", CommandCreateElementFull.DISPLAY)), //
						new RegexConcat(//
								new RegexLeaf(1, "DISPLAY4", CommandCreateElementFull.DISPLAY_WITHOUT_QUOTE), //
								StereotypePattern.optionalArchimate("STEREOTYPE4"), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "CODE4", CommandCreateElementFull.CODE)) //
				), //
				StereotypePattern.optionalArchimate("STEREOTYPE"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(DescriptionDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String codeRaw = arg.getLazzy("CODE", 0);

		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(codeRaw));

		String display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("DISPLAY", 0));
		if (display == null)
			display = quark.getName();
		
		final CommandExecutionResult status = diagram.gotoGroup(location, quark, Display.getWithNewlines(diagram.getPragma(), display),
				GroupType.PACKAGE, USymbols.ARCHIMATE);
		if (status.isOk() == false)
			return status;

		final Entity p = diagram.getCurrentGroup();


		final String icon = StereotypePattern.removeChevronBrackets(arg.getLazzy("STEREOTYPE", 0));

		p.setDisplay(Display.getWithNewlines(diagram.getPragma(), display));

		if (icon != null)
			p.setStereotype(
					Stereotype.build("<<$archimate/" + icon + ">>", diagram.getSkinParam().getCircledCharacterRadius(),
							diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
							diagram.getSkinParam().getIHtmlColorSet()));

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);

		return CommandExecutionResult.ok();
	}
}
