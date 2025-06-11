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
package net.sourceforge.plantuml.statediagram.command;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.statediagram.StateDiagram;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreatePackage2 extends SingleLineCommand2<StateDiagram> {

	public CommandCreatePackage2() {
		super(getRegexConcat());
	}

	@Override
	public boolean isEligibleFor(ParserPass pass) {
		return pass == ParserPass.ONE || pass == ParserPass.TWO || pass == ParserPass.THREE;
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreatePackage2.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("frame"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf(1, "CODE1", "([%pLN_.]+)"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "DISPLAY1", "[%g]([^%g]+)[%g]")), //
						new RegexConcat(//
								new RegexOptional(new RegexConcat( //
										new RegexLeaf(1, "DISPLAY2", "[%g]([^%g]+)[%g]"), //
										RegexLeaf.spaceOneOrMore(), //
										new RegexLeaf("as"), //
										RegexLeaf.spaceOneOrMore() //
								)), //
								new RegexLeaf(1, "CODE2", "([%pLN_.]+)"))), //
				StereotypePattern.optional("STEREOTYPE"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(2, "LINECOLOR", "##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?")),
				new RegexLeaf("(?:[%s]*\\{|[%s]+begin)"), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	private String getNotNull(RegexResult arg, String v1, String v2) {
		if (arg.get(v1, 0) == null)
			return arg.get(v2, 0);

		return arg.get(v1, 0);
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {

		final String idShort = getNotNull(arg, "CODE1", "CODE2");
		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(idShort));
		String display = getNotNull(arg, "DISPLAY1", "DISPLAY2");
		if (display == null)
			display = quark.getName();

		diagram.gotoGroup(location, quark, Display.getWithNewlines(diagram.getPragma(), display), GroupType.PACKAGE,
				USymbols.FRAME);
		final Entity p = diagram.getCurrentGroup();
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null)
			p.setStereotype(Stereotype.build(stereotype));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final String s = arg.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (arg.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));

		p.setColors(colors);
		return CommandExecutionResult.ok();
	}

}
