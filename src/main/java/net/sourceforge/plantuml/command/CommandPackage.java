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
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
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
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandPackage extends SingleLineCommand2<AbstractEntityDiagram> {

	public CommandPackage() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandPackage.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "VISIBILITY", "(" + VisibilityModifier.regexForVisibilityCharacter() + ")?" ), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "TYPE", "(package)"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(1, "NAME", "([%g][^%g]+[%g]|[^#%s{}]*)"), //
				new RegexOptional( //
						new RegexConcat( //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("as"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf(1, "AS", "([%pLN_.]+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(4, "TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf(4, "TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
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
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// 'package Name {' (or 'package "Display" as code {') opens a package
		// around the following elements, closed by '}'.
		final String name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));
		final String as = arg.get("AS", 0);
		if (as == null) {
			if (name.length() == 0)
				return "Starting a package without a name (rejected at execution: a name is required)";
			sb.append("Starting the package '").append(name).append("'");
		} else
			sb.append("Starting the package '").append(as).append("' displayed as \"").append(name).append("\"");

		final String visibility = arg.get("VISIBILITY", 0);
		if (visibility != null)
			sb.append(", with the visibility modifier '").append(visibility).append("'");

		// When the stereotype names a USymbol (like <<Rectangle>> or
		// <<Frame>>), it selects the shape of the package instead of being
		// displayed, like in executeArg.
		final String stereotype = arg.get("STEREOTYPE", 0);
		if (stereotype != null)
			sb.append(", stereotype ").append(stereotype)
					.append(" (a symbol name here selects the shape of the package)");

		final String tags = arg.getLazzy("TAGS", 0);
		if (tags != null && tags.isEmpty() == false)
			sb.append(", tagged ").append(tags);

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		if (arg.get("COLOR", 0) != null)
			sb.append(", background color ").append(arg.get("COLOR", 0));

		return sb.toString();
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram diagram, LineLocation location, RegexResult arg,
			ParserPass currentPass) throws NoSuchColorException {
		String idShort;
		String display;
		final String name = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("NAME", 0));

		if (arg.get("AS", 0) == null) {
			if (name.length() == 0) {
				idShort = diagram.getUniqueSequence("##");
				display = null;
				return CommandExecutionResult.error("Error in name");
				// throw new IllegalStateException("AS");
			} else {
				idShort = name;
				display = idShort;
			}
		} else {
			display = name;
			idShort = arg.get("AS", 0);
		}

		final Quark<Entity> quark;
		if (arg.get("AS", 0) == null) {
			quark = diagram.quarkInContext(false, diagram.cleanId(name));
			display = quark.getName();
		} else {
			quark = diagram.quarkInContext(false, diagram.cleanId(arg.get("AS", 0)));
			display = name;
		}

		final String stereotype = arg.get("STEREOTYPE", 0);
		final USymbol usymbol = USymbols.fromString(stereotype, diagram.getSkinParam().actorStyle(),
				diagram.getSkinParam().componentStyle(), diagram.getSkinParam().packageStyle());

		final CommandExecutionResult status = diagram.gotoGroup(location, quark,
				Display.getWithNewlines(diagram.getPragma(), display), GroupType.PACKAGE, usymbol);
		if (status.isOk() == false)
			return status;

		final Entity p = diagram.getCurrentGroup();

		final String visibilityString = arg.get("VISIBILITY", 0);
		if (visibilityString != null) {
			final VisibilityModifier visibilityModifier = VisibilityModifier.getVisibilityModifier(visibilityString + "FOO", false);
			p.setVisibilityModifier(visibilityModifier);
		}

		if (stereotype != null && usymbol == null)
			p.setStereotype(Stereotype.build(stereotype));

		CommandCreateClassMultilines.addTags(p, arg.getLazzy("TAGS", 0));

		final String urlString = arg.get(UrlBuilder.URL_KEY, 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			p.addUrl(url);
		}

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		p.setColors(colors);

		return CommandExecutionResult.ok();
	}

}
