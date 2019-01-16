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
package net.sourceforge.plantuml.descdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagram;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;

public class CommandArchimate extends SingleLineCommand2<DescriptionDiagram> {

	public CommandArchimate() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("SYMBOL", "archimate"), //
				new RegexLeaf("[%s]+"), //
				color().getRegex(), //
				new RegexLeaf("[%s]+"), //
				new RegexOr(//
						new RegexLeaf("CODE1", CommandCreateElementFull.CODE_WITH_QUOTE), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", CommandCreateElementFull.DISPLAY), //
								new RegexLeaf("STEREOTYPE2", "(?:[%s]+(?:\\<\\<([-\\w]+)\\>\\>))?"), //
								new RegexLeaf("[%s]*as[%s]+"), //
								new RegexLeaf("CODE2", CommandCreateElementFull.CODE)), //
						new RegexConcat(//
								new RegexLeaf("CODE3", CommandCreateElementFull.CODE), //
								new RegexLeaf("STEREOTYPE3", "(?:[%s]+(?:\\<\\<([-\\w]+)\\>\\>))?"), //
								new RegexLeaf("[%s]+as[%s]*"), //
								new RegexLeaf("DISPLAY3", CommandCreateElementFull.DISPLAY)), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY4", CommandCreateElementFull.DISPLAY_WITHOUT_QUOTE), //
								new RegexLeaf("STEREOTYPE4", "(?:[%s]+(?:\\<\\<([-\\w]+)\\>\\>))?"), //
								new RegexLeaf("[%s]*as[%s]+"), //
								new RegexLeaf("CODE4", CommandCreateElementFull.CODE)) //
				), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(?:[%s]*(?:\\<\\<([-\\w]+)\\>\\>))?"), //
				new RegexLeaf("$"));
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(DescriptionDiagram diagram, RegexResult arg) {
		final String codeRaw = arg.getLazzy("CODE", 0);

		final Code code = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw));
		final String icon = arg.getLazzy("STEREOTYPE", 0);

		final IEntity entity = diagram.getOrCreateLeaf(code, LeafType.DESCRIPTION, USymbol.RECTANGLE);
		
		final String displayRaw = arg.getLazzy("DISPLAY", 0);

		String display = displayRaw;
		if (display == null) {
			display = code.getFullName();
		}
		display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(display);

		entity.setDisplay(Display.getWithNewlines(display));
		entity.setUSymbol(USymbol.RECTANGLE);
		if (icon != null) {
			entity.setStereotype(new Stereotype("<<$archimate/" + icon + ">>", diagram.getSkinParam()
					.getCircledCharacterRadius(), diagram.getSkinParam().getFont(null, false,
					FontParam.CIRCLED_CHARACTER), diagram.getSkinParam().getIHtmlColorSet()));
		}

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		entity.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
