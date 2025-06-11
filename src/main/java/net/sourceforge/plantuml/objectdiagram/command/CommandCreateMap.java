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
package net.sourceforge.plantuml.objectdiagram.command;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.cucadiagram.BodierMap;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontParam;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandCreateMap extends CommandMultilines2<AbstractEntityDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(
			() -> Pattern2.cmpile("^[%s]*\\}[%s]*$"));

	public CommandCreateMap() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateMap.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(0, "TYPE", "map"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(2, "NAME", "(?:[%g]([^%g]+)[%g][%s]+as[%s]+)?([%pLN_.]+)"), //
				StereotypePattern.optional("STEREO"), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexConcat(new RegexLeaf("##"), new RegexLeaf(2, "LINECOLOR", "(?:\\[(dotted|dashed|bold)\\])?(\\w+)?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeNow(AbstractEntityDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		lines = lines.trim().removeEmptyLines();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());
		final Entity entity1 = executeArg0(lines.getLocation(), diagram, line0);
		if (entity1 == null)
			return CommandExecutionResult.error("No such entity");

		lines = lines.subExtract(1, 1);
		for (StringLocated sl : lines) {
			final String line = sl.getString();
			assert line.length() > 0;
			final boolean ok = entity1.getBodier().addFieldOrMethod(line);
			if (ok == false)
				return CommandExecutionResult.error("Map definition should contains key => value");

			if (BodierMap.getLinkedEntry(line) != null) {
				final String linkStr = BodierMap.getLinkedEntry(line);
				final int x = line.indexOf(linkStr);
				final String key = line.substring(0, x).trim();
				final String dest = line.substring(x + linkStr.length()).trim();
				final Quark<Entity> ident2 = diagram.quarkInContext(true, dest);
				final Entity entity2 = ident2.getData();
				if (entity2 == null)
					return CommandExecutionResult.error("No such entity " + ident2.getName());

				final LinkType linkType = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
				final int length = linkStr.length() - 2;
				final Link link = new Link(lines.getLocation(), diagram,
						diagram.getSkinParam().getCurrentStyleBuilder(), entity1, entity2, linkType,
						LinkArg.noDisplay(length));
				link.setPortMembers(key, null);
				diagram.addLink(link);
			}
		}
		return CommandExecutionResult.ok();
	}

	private Entity executeArg0(LineLocation location, AbstractEntityDiagram diagram, RegexResult line0)
			throws NoSuchColorException {
		final String name = line0.get("NAME", 1);

		final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(name));
		final String displayString = line0.get("NAME", 0);
		final String stereotype = line0.get("STEREO", 0);

		if (quark.getData() != null)
			return null;

		Display display = Display.getWithNewlines(diagram.getPragma(), displayString);
		if (Display.isNull(display))
			display = Display.getWithNewlines(diagram.getPragma(), name).withCreoleMode(CreoleMode.SIMPLE_LINE);

		final Entity entity = diagram.reallyCreateLeaf(location, quark, display, LeafType.MAP, null);
		if (stereotype != null)
			entity.setStereotype(Stereotype.build(stereotype, diagram.getSkinParam().getCircledCharacterRadius(),
					diagram.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER),
					diagram.getSkinParam().getIHtmlColorSet()));

		Colors colors = color().getColor(line0, diagram.getSkinParam().getIHtmlColorSet());
		final String s = line0.get("LINECOLOR", 1);

		final HColor lineColor = s == null ? null : diagram.getSkinParam().getIHtmlColorSet().getColor(s);
		if (lineColor != null)
			colors = colors.add(ColorType.LINE, lineColor);

		if (line0.get("LINECOLOR", 0) != null)
			colors = colors.addLegacyStroke(line0.get("LINECOLOR", 0));

		entity.setColors(colors);

		return entity;
	}

}
