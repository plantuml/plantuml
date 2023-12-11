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

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.MyPattern;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;

public class CommandCreateElementMultilines extends CommandMultilines2<AbstractEntityDiagram> {

	private final int type;

	enum Mode {
		EXTENDS, IMPLEMENTS
	};

	public CommandCreateElementMultilines(int type) {
		super(getRegexConcat(type), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH);
		this.type = type;
	}

	@Override
	public String getPatternEnd() {
		if (type == 0)
			return "^(.*)[%g]$";

		if (type == 1)
			return "^([^\\[\\]]*)\\]$";

		throw new IllegalArgumentException();
	}

	private static RegexConcat getRegexConcat(int type) {
		if (type == 0)
			return RegexConcat.build(CommandCreateElementMultilines.class.getName() + type, RegexLeaf.start(), //
					new RegexLeaf("TYPE", "(" + CommandCreateElementFull.ALL_TYPES + ")[%s]+"), //
					new RegexLeaf("CODE", "([%pLN_.]+)"), //
					StereotypePattern.optional("STEREO"), //
					UrlBuilder.OPTIONAL, //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("as"), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("[%g]"), //
					new RegexLeaf("DESC", "([^%g]*)"), //
					RegexLeaf.end());

		if (type == 1)
			return RegexConcat.build(CommandCreateElementMultilines.class.getName() + type, RegexLeaf.start(), //
					new RegexLeaf("TYPE", "(" + CommandCreateElementFull.ALL_TYPES + ")[%s]+"), //
					new RegexLeaf("CODE", "([%pLN_.]+)"), //
					StereotypePattern.optional("STEREO"), //
					UrlBuilder.OPTIONAL, //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("\\["), //
					new RegexLeaf("DESC", "(.*)"), //
					RegexLeaf.end());

		throw new IllegalArgumentException();
	}

	@Override
	protected CommandExecutionResult executeNow(AbstractEntityDiagram diagram, BlocLines lines)
			throws NoSuchColorException {
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final String symbol = StringUtils.goUpperCase(line0.get("TYPE", 0));
		final LeafType type;
		USymbol usymbol;

		if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("usecase/")) {
			type = LeafType.USECASE_BUSINESS;
			usymbol = null;
		} else {
			usymbol = USymbols.fromString(symbol, diagram.getSkinParam().actorStyle(),
					diagram.getSkinParam().componentStyle(), diagram.getSkinParam().packageStyle());
			if (usymbol == null)
				throw new IllegalStateException();

			type = LeafType.DESCRIPTION;
		}

		final String idShort = line0.get("CODE", 0);
		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()),
				lines.getLast().getTrimmed().getString());
		lines = lines.subExtract(1, 1);
		Display display = lines.toDisplay();
		final String descStart = line0.get("DESC", 0);
		if (StringUtils.isNotEmpty(descStart))
			display = display.addFirst(descStart);

		if (StringUtils.isNotEmpty(lineLast.get(0)))
			display = display.add(lineLast.get(0));

		final String stereotype = line0.get("STEREO", 0);

		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(idShort));
		Entity result = quark.getData();
		if (quark.getData() == null)
			result = diagram.reallyCreateLeaf(quark, display, type, usymbol);

		if (CommandCreateElementFull.existsWithBadType3(diagram, quark, type, usymbol))
			return CommandExecutionResult.error("This element (" + quark.getName() + ") is already defined");

		result.setUSymbol(usymbol);
		if (stereotype != null)
			result.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));

		final String urlString = line0.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			result.addUrl(url);
		}

		final Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		result.setColors(colors);

		return CommandExecutionResult.ok();
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

}
