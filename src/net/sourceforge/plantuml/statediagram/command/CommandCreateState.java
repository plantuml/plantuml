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
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.classdiagram.command.CommandCreateClassMultilines;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
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
import net.sourceforge.plantuml.stereo.Stereotag;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateState extends SingleLineCommand2<StateDiagram> {

	public CommandCreateState() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateState.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("state"), //
				RegexLeaf.spaceOneOrMore(), //

				new RegexOr(//
						new RegexConcat(//
								new RegexLeaf("CODE1", "([%pLN_.]+)"), //
								RegexLeaf.spaceOneOrMore(), new RegexLeaf("as"), RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("DISPLAY1", "[%g]([^%g]+)[%g]")), //
						new RegexConcat(//
								new RegexLeaf("DISPLAY2", "[%g]([^%g]+)[%g]"), //
								RegexLeaf.spaceOneOrMore(), new RegexLeaf("as"), RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("CODE2", "([%pLN_.]+)")), //
						new RegexLeaf("CODE3", "([%pLN_.]+)"), //
						new RegexLeaf("CODE4", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("TAGS1", Stereotag.pattern() + "?"), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf("TAGS2", Stereotag.pattern() + "?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("LINECOLOR", "##(?:\\[(dotted|dashed|bold)\\])?(\\w+)?")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("ADDFIELD", "(.*)") //
						)), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(StateDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final String idShort = arg.getLazzy("CODE", 0);

		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(idShort));

		String display = arg.getLazzy("DISPLAY", 0);
		if (display == null)
			display = quark.getName();

		final String stereotype = arg.get("STEREOTYPE", 0);
		LeafType type = getTypeFromStereotype(stereotype);
		if (type == null)
			type = LeafType.STATE;
		if (diagram.checkConcurrentStateOk(quark) == false)
			return CommandExecutionResult.error("The state " + quark.getName()
					+ " has been created in a concurrent state : it cannot be used here.");

		Entity ent = quark.getData();
		if (ent == null)
			ent = diagram.reallyCreateLeaf(quark, Display.getWithNewlines(display), type, null);

		ent.setDisplay(Display.getWithNewlines(display));

		if (stereotype != null)
			ent.setStereotype(Stereotype.build(stereotype));

		final String urlString = arg.get("URL", 0);
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			ent.addUrl(url);
		}

		Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());
		final String s = arg.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (arg.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(arg.get("LINECOLOR", 0));

		ent.setColors(colors);

		// ent.setSpecificColorTOBEREMOVED(ColorType.BACK,
		// diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR",
		// 0)));
		// ent.setSpecificColorTOBEREMOVED(ColorType.LINE,
		// diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("LINECOLOR",
		// 1)));
		// ent.applyStroke(arg.get("LINECOLOR", 0));

		final String addFields = arg.get("ADDFIELD", 0);
		if (addFields != null)
			ent.getBodier().addFieldOrMethod(addFields);

		CommandCreateClassMultilines.addTags(ent, arg.getLazzy("TAGS", 0));

		return CommandExecutionResult.ok();
	}

	private LeafType getTypeFromStereotype(String stereotype) {
		if ("<<choice>>".equalsIgnoreCase(stereotype))
			return LeafType.STATE_CHOICE;

		if ("<<fork>>".equalsIgnoreCase(stereotype))
			return LeafType.STATE_FORK_JOIN;

		if ("<<join>>".equalsIgnoreCase(stereotype))
			return LeafType.STATE_FORK_JOIN;

		if ("<<start>>".equalsIgnoreCase(stereotype))
			return LeafType.CIRCLE_START;

		if ("<<end>>".equalsIgnoreCase(stereotype))
			return LeafType.CIRCLE_END;

		if ("<<history>>".equalsIgnoreCase(stereotype))
			return LeafType.PSEUDO_STATE;

		if ("<<history*>>".equalsIgnoreCase(stereotype))
			return LeafType.DEEP_HISTORY;

		return null;
	}

}