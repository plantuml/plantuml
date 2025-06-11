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

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandArchimateMultilines extends CommandMultilines2<AbstractEntityDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^(.*)\\]$"));

	public CommandArchimateMultilines() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandArchimateMultilines.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("archimate"), //
				RegexLeaf.spaceOneOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "CODE", "([%pLN_.]+)"), //
				StereotypePattern.optionalArchimate("STEREOTYPE"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\["), //
				new RegexLeaf(1, "DESC", "(.*)"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeNow(AbstractEntityDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		lines = lines.trim();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final String codeRaw = line0.getLazzy("CODE", 0);

		final Quark<Entity> quark = diagram.quarkInContext(false, diagram.cleanId(codeRaw));
		if (quark.getData() != null)
			return CommandExecutionResult.error("Already exists " + quark.getName());

		final String icon = StereotypePattern.removeChevronBrackets(line0.getLazzy("STEREOTYPE", 0));

		final Entity entity = diagram.reallyCreateLeaf(lines.getLocation(), quark, Display.getWithNewlines(quark),
				LeafType.DESCRIPTION, USymbols.RECTANGLE);

		lines = lines.subExtract(1, 1);
		Display display = lines.toDisplay();

		entity.setDisplay(display);

		if (icon != null) {
			entity.setStereotype(
					Stereotype.build("<<$archimate/" + icon + ">>", diagram.getSkinParam().getCircledCharacterRadius(),
							diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
							diagram.getSkinParam().getIHtmlColorSet()));
		}

		final Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		entity.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
