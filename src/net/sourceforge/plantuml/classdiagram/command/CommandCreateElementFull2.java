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
 * Contribution :  Hisashi Miyashita
 * 
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementFull;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateElementFull2 extends SingleLineCommand2<ClassDiagram> {

	private final Mode mode;

	public static enum Mode {
		NORMAL_KEYWORD, WITH_MIX_PREFIX
	}

	public CommandCreateElementFull2(Mode mode) {
		super(getRegexConcat(mode));
		this.mode = mode;
	}

	private static RegexConcat getRegexConcat(Mode mode) {
		String regex = "(state|" + CommandCreateElementFull.ALL_TYPES + ")";
		if (mode == Mode.WITH_MIX_PREFIX) {
			return RegexConcat.build(CommandCreateElementFull2.class.getName() + mode, //
					RegexLeaf.start(), //
					new RegexLeaf("mix_"), //
					new RegexLeaf("SYMBOL", regex), //
					RegexLeaf.spaceOneOrMore(), //
					new RegexOr(//
							new RegexLeaf("CODE1", CommandCreateElementFull.CODE_WITH_QUOTE), //
							new RegexConcat(//
									new RegexLeaf("DISPLAY2", CommandCreateElementFull.DISPLAY), //
									StereotypePattern.optional("STEREOTYPE2"), //
									new RegexLeaf("as"), //
									RegexLeaf.spaceOneOrMore(), //
									new RegexLeaf("CODE2", CommandCreateElementFull.CODE)) //
					), //
					RegexLeaf.spaceZeroOrMore(), //
					new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
					StereotypePattern.optional("STEREOTYPE"), //
					new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
					RegexLeaf.spaceZeroOrMore(), //
					UrlBuilder.OPTIONAL, //
					RegexLeaf.spaceZeroOrMore(), //
					ColorParser.exp1(), //
					RegexLeaf.end());
		}
		return RegexConcat.build(CommandCreateElementFull2.class.getName() + mode, //
				RegexLeaf.start(), //
				new RegexLeaf("SYMBOL", regex), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexLeaf("CODE1", CommandCreateElementFull.CODE_WITH_QUOTE), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", CommandCreateElementFull.DISPLAY), //
								StereotypePattern.optional("STEREOTYPE2"), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE2", CommandCreateElementFull.CODE)) //
				), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp1(), //
				RegexLeaf.end());
	}

	@Override
	final protected boolean isForbidden(CharSequence line) {
		if (line.toString().matches("^[\\p{L}0-9_.]+$"))
			return true;

		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		if (mode == Mode.NORMAL_KEYWORD && diagram.isAllowMixing() == false)
			return CommandExecutionResult.error("Use 'allowmixing' if you want to mix classes and other UML elements.");

		String codeRaw = arg.getLazzy("CODE", 0);
		final String displayRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("DISPLAY", 0));
		final char codeChar = getCharEncoding(codeRaw);
		final char codeDisplay = getCharEncoding(displayRaw);
		final String symbol;
		if (codeRaw.startsWith("()")) {
			symbol = "interface";
			codeRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(codeRaw.substring(2)));
		} else if (codeChar == '(' || codeDisplay == '(') {
			symbol = "usecase";
		} else if (codeChar == ':' || codeDisplay == ':') {
			symbol = "actor";
		} else if (codeChar == '[' || codeDisplay == '[') {
			symbol = "component";
		} else {
			symbol = arg.get("SYMBOL", 0);
		}

		final LeafType type;
		final USymbol usymbol;

		if (symbol == null) {
			type = LeafType.DESCRIPTION;
			usymbol = diagram.getSkinParam().actorStyle().toUSymbol();
		} else if (symbol.equalsIgnoreCase("port")) {
			type = LeafType.PORTIN;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("portin")) {
			type = LeafType.PORTIN;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("portout")) {
			type = LeafType.PORTOUT;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("usecase/")) {
			type = LeafType.USECASE_BUSINESS;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("state")) {
			type = LeafType.STATE;
			usymbol = null;
		} else {
			type = LeafType.DESCRIPTION;
			usymbol = USymbols.fromString(symbol, diagram.getSkinParam());
			if (usymbol == null)
				throw new IllegalStateException();
		}

		final String idShort = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw);
		final Display display = Display.getWithNewlines(displayRaw == null ? idShort : displayRaw);
		final Quark<Entity> quark = diagram.quarkInContext(true, idShort);
		Entity entity = quark.getData();
		if (entity == null)
			entity = diagram.reallyCreateLeaf(quark, display, type, usymbol);

		final String stereotype = arg.getLazzy("STEREOTYPE", 0);

		entity.setDisplay(display);
		entity.setUSymbol(usymbol);
		if (stereotype != null)
			entity.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));

		CommandCreateClassMultilines.addTags(entity, arg.getLazzy("TAGS", 0));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}
		final String s = arg.get("COLOR", 0);

		entity.setSpecificColorTOBEREMOVED(ColorType.BACK,
				s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		return CommandExecutionResult.ok();
	}

	private char getCharEncoding(final String codeRaw) {
		return codeRaw != null && codeRaw.length() > 2 ? codeRaw.charAt(0) : 0;
	}
}
