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
package net.sourceforge.plantuml.cheneer.command;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.cheneer.ChenEerDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandMultiSubclass extends SingleLineCommand2<ChenEerDiagram> {

	public CommandMultiSubclass() {
		super(getRegexConcat());
	}

	protected static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateEntity.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "SUPERCLASS", "([%pLN_.-]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "PARTICIPATION", "([-=])"), //
				new RegexLeaf(1, "(>)"), //
				new RegexLeaf(1, "PARTICIPATION2", "([-=])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "SYMBOL", "([doU])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("\\{"), //
				new RegexLeaf(1, "SUBCLASSES", "(.+)"), //
				new RegexLeaf("\\}"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.BACK);
	}

	@Override
	protected CommandExecutionResult executeArg(ChenEerDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String superclass = diagram.cleanId(arg.get("SUPERCLASS", 0));
		final String subclasses = arg.get("SUBCLASSES", 0);

		final List<String> subclassIds = new ArrayList<String>();

		for (String subclass : subclasses.split(","))
			subclassIds.add(diagram.cleanId(subclass.trim()));

		final boolean isDouble = arg.get("PARTICIPATION", 0).equals("=");
		final String symbol = arg.get("SYMBOL", 0);

		final Colors colors = color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet());

		final Quark<Entity> centerQuark = diagram.quarkInContext(false,
				superclass + "/" + symbol + subclasses + "/center");
		final Entity centerEntity = diagram.reallyCreateLeaf(location, centerQuark, Display.create(symbol), LeafType.CHEN_CIRCLE,
				null);

		centerEntity.setColors(colors);

		final Quark<Entity> superclassQuark = diagram.quarkInContext(true, superclass);
		final Entity superclassEntity = superclassQuark.getData();
		if (superclassEntity == null) {
			return CommandExecutionResult.error("No such entity: " + superclass);
		}

		LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		if (isDouble) {
			linkType = linkType.goBold();
		}
		if (symbol.equals("U")) {
			linkType = linkType.withMiddleSuperset();
		}
		final Link link = new Link(location, diagram, diagram.getCurrentStyleBuilder(), superclassEntity,
				centerEntity, linkType, LinkArg.build(Display.NULL, 2));
		link.setPortMembers(diagram.getPortId(superclassEntity.getName()), diagram.getPortId(centerEntity.getName()));
		link.setColors(colors);
		diagram.addLink(link);

		for (String subclass : subclassIds) {
			final Quark<Entity> subclassQuark = diagram.quarkInContext(true, subclass);
			final Entity subclassEntity = subclassQuark.getData();
			if (subclassEntity == null) {
				return CommandExecutionResult.error("No such entity: " + subclass);
			}

			LinkType subclassLinkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
			if (!symbol.equals("U")) {
				subclassLinkType = subclassLinkType.withMiddleSuperset();
			}
			final Link subclassLink = new Link(location, diagram, diagram.getCurrentStyleBuilder(), centerEntity,
					subclassEntity, subclassLinkType, LinkArg.build(Display.NULL, 3));
			subclassLink.setColors(colors);
			diagram.addLink(subclassLink);
		}

		return CommandExecutionResult.ok();
	}

}
