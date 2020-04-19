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

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackageWithUSymbol extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageWithUSymbol() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackageWithUSymbol.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("SYMBOL",
						"(package|rectangle|node|artifact|folder|file|frame|cloud|database|storage|component|card|together|queue|stack)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexConcat( //
								new RegexLeaf("DISPLAY1", "([%g].+?[%g])"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf("STEREOTYPE1", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE1", "([^#%s{}]+)") //
						), //
						new RegexConcat( //
								new RegexLeaf("CODE2", "([^#%s{}%g]+)"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf("STEREOTYPE2", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("DISPLAY2", "([%g].+?[%g])") //
						), //
						new RegexConcat( //
								new RegexLeaf("DISPLAY3", "([^#%s{}%g]+)"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf("STEREOTYPE3", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE3", "([^#%s{}%g]+)") //
						), //
						new RegexLeaf("CODE8", "([%g][^%g]+[%g])"), //
						new RegexLeaf("CODE9", "([^#%s{}%g]*)") //
				), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg) {
		final String codeRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("CODE", 0));
		final String displayRaw = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("DISPLAY", 0));
		final String display;
		final String idShort;
		if (codeRaw.length() == 0) {
			idShort = UniqueSequence.getString("##");
			display = null;
		} else {
			idShort = codeRaw;
			if (displayRaw == null) {
				display = idShort;
			} else {
				display = displayRaw;
			}
		}

		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		final IGroup currentPackage = diagram.getCurrentGroup();
		diagram.gotoGroup(ident, code, Display.getWithNewlines(display), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		final String symbol = arg.get("SYMBOL", 0);
		if ("together".equalsIgnoreCase(symbol)) {
			p.setThisIsTogether();
		}
		p.setUSymbol(USymbol.getFromString(symbol, diagram.getSkinParam().getActorStyle()));
		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		if (stereotype != null) {
			p.setStereotype(new Stereotype(stereotype, false));
		}
		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}
		CommandCreateClassMultilines.addTags(p, arg.get("TAGS", 0));
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);
		return CommandExecutionResult.ok();
	}
}
