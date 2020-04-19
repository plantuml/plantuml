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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
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
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.utils.UniqueSequence;

public class CommandPackage extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackage() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackage.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("TYPE", "(package)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("NAME", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("AS", "([\\p{L}0-9_.]+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\{"), RegexLeaf.end());
	}

	@Override
	public boolean syntaxWithFinalBracket() {
		return true;
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg) {
		final String idShort;
		/* final */String display;
		final String name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		boolean override1972 = false;
		if (arg.get("AS", 0) == null) {
			if (name.length() == 0) {
				idShort = "##" + UniqueSequence.getValue();
				display = null;
			} else {
				idShort = name;
				display = idShort;
				override1972 = true;
			}
		} else {
			display = name;
			idShort = arg.get("AS", 0);
		}
		final IGroup currentPackage = diagram.getCurrentGroup();
		// final Ident ident = diagram.buildLeafIdentSpecial(idShort);
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		if (diagram.V1972() && override1972)
			display = ident.getLast();
		diagram.gotoGroup(ident, code, Display.getWithNewlines(display), GroupType.PACKAGE, currentPackage,
				NamespaceStrategy.SINGLE);
		final IEntity p = diagram.getCurrentGroup();
		final String stereotype = arg.get("STEREOTYPE", 0);
		// final USymbol type = USymbol.getFromString(arg.get("TYPE", 0),
		// diagram.getSkinParam().getActorStyle());
//		if (type == USymbol.TOGETHER) {
//			p.setUSymbol(type);
//			p.setThisIsTogether();
//		} else 
		if (stereotype != null) {
			final USymbol usymbol = USymbol.getFromString(stereotype, diagram.getSkinParam().getActorStyle());
			if (usymbol == null) {
				p.setStereotype(new Stereotype(stereotype));
			} else {
				p.setUSymbol(usymbol);
			}
		}
		CommandCreateClassMultilines.addTags(p, arg.get("TAGS", 0));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
