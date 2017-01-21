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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
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
import net.sourceforge.plantuml.descdiagram.command.CommandCreateElementFull;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

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

		// String regex = "(?:(actor|usecase|component)[%s]+)";
		String regex = "(?:(state|" + CommandCreateElementFull.ALL_TYPES + ")[%s]+)";
		if (mode == Mode.WITH_MIX_PREFIX) {
			regex = "mix_" + regex;
		}
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("SYMBOL", regex), //
				new RegexLeaf("[%s]*"), //
				new RegexOr(//
						new RegexLeaf("CODE1", CODE_WITH_QUOTE) //
				), //
				new RegexLeaf("STEREOTYPE", "(?:[%s]*(\\<\\<.+\\>\\>))?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp1(), //
				new RegexLeaf("$"));
	}

	private static final String CODE_CORE = "[\\p{L}0-9_.]+|\\(\\)[%s]*[\\p{L}0-9_.]+|\\(\\)[%s]*[%g][^%g]+[%g]|:[^:]+:|\\([^()]+\\)|\\[[^\\[\\]]+\\]";
	private static final String CODE = "(" + CODE_CORE + ")";
	private static final String CODE_WITH_QUOTE = "(" + CODE_CORE + "|[%g][^%g]+[%g])";

	private static final String DISPLAY_CORE = "[%g][^%g]+[%g]|:[^:]+:|\\([^()]+\\)|\\[[^\\[\\]]+\\]";
	private static final String DISPLAY = "(" + DISPLAY_CORE + ")";
	private static final String DISPLAY_WITHOUT_QUOTE = "(" + DISPLAY_CORE + "|[\\p{L}0-9_.]+)";

	@Override
	final protected boolean isForbidden(CharSequence line) {
		if (line.toString().matches("^[\\p{L}0-9_.]+$")) {
			return true;
		}
		return false;
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, RegexResult arg) {
		if (mode == Mode.NORMAL_KEYWORD && diagram.isAllowMixing() == false) {
			return CommandExecutionResult
					.error("Use 'allow_mixing' if you want to mix classes and other UML elements.");
		}
		String codeRaw = arg.getLazzy("CODE", 0);
		final String displayRaw = arg.getLazzy("DISPLAY", 0);
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
			usymbol = USymbol.ACTOR;
		} else if (symbol.equalsIgnoreCase("usecase")) {
			type = LeafType.USECASE;
			usymbol = null;
		} else if (symbol.equalsIgnoreCase("state")) {
			type = LeafType.STATE;
			usymbol = null;
		} else {
			type = LeafType.DESCRIPTION;
			usymbol = USymbol.getFoo1(symbol, diagram.getSkinParam().useUml2ForComponent());
			if (usymbol == null) {
				throw new IllegalStateException();
			}
		}

		final Code code = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(codeRaw));
		String display = displayRaw;
		if (display == null) {
			display = code.getFullName();
		}
		display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(display);
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		final IEntity entity = diagram.getOrCreateLeaf(code, type, usymbol);
		entity.setDisplay(Display.getWithNewlines(display));
		entity.setUSymbol(usymbol);
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}

		entity.setSpecificColorTOBEREMOVED(ColorType.BACK,
				diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

	private char getCharEncoding(final String codeRaw) {
		return codeRaw != null && codeRaw.length() > 2 ? codeRaw.charAt(0) : 0;
	}
}
