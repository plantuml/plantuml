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
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPackageWithUSymbol extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackageWithUSymbol() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackageWithUSymbol.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "SYMBOL",
						"(package|rectangle|hexagon|node|artifact|folder|file|frame|cloud|action|process|database|storage|component|card|queue|stack)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexConcat( //
								new RegexLeaf(1, "DISPLAY1", "([%g].+?[%g])"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf(1, "STEREOTYPE1", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "CODE1", "([^#%s{}]+)") //
						), //
						new RegexConcat( //
								new RegexLeaf(1, "CODE2", "([^#%s{}%g]+)"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf(1, "STEREOTYPE2", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "DISPLAY2", "([%g].+?[%g])") //
						), //
						new RegexConcat( //
								new RegexLeaf(1, "DISPLAY3", "([^#%s{}%g]+)"), //
								new RegexOptional( //
										new RegexConcat( //
												RegexLeaf.spaceOneOrMore(), //
												new RegexLeaf(1, "STEREOTYPE3", "(\\<\\<.+\\>\\>)") //
										)), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "CODE3", "([^#%s{}%g]+)") //
						), //
						new RegexLeaf(1, "CODE8", "([%g][^%g]+[%g])"), //
						new RegexLeaf(1, "CODE9", "([^#%s{}%g]*)") //
				), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(4, "TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf(4, "TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		final String codeArg = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("CODE", 0));
		final Quark<Entity> ident = diagram.quarkInContext(false,
				diagram.cleanId(codeArg.length() == 0 ? diagram.getUniqueSequence("##") : codeArg));

		final String displayArg = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.getLazzy("DISPLAY", 0));
		final String display;

		if (codeArg.length() == 0)
			display = null;
		else if (displayArg == null)
			display = ident.getName();
		else
			display = displayArg;

		final String symbol = arg.get("SYMBOL", 0);

		final USymbol usymbol = USymbols.fromString(symbol, diagram.getSkinParam().actorStyle(),
				diagram.getSkinParam().componentStyle(), diagram.getSkinParam().packageStyle());

		final CommandExecutionResult status = diagram.gotoGroup(location, ident,
				Display.getWithNewlines(diagram.getPragma(), display), GroupType.PACKAGE, usymbol);
		if (status.isOk() == false)
			return status;

		final Entity p = diagram.getCurrentGroup();

		final String stereotype = arg.getLazzy("STEREOTYPE", 0);
		if (stereotype != null)
			p.setStereotype(Stereotype.build(stereotype, false));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}
		CommandCreateClassMultilines.addTags(p, arg.getLazzy("TAGS", 0));
		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);
		return CommandExecutionResult.ok();
	}
}
