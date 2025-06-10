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

import net.sourceforge.plantuml.abel.Entity;
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
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAssociate extends SingleLineCommand2<ChenEerDiagram> {

	public CommandAssociate() {
		super(getRegexConcat());
	}

	protected static IRegex getRegexConcat() {
		return RegexConcat.build(CommandCreateEntity.class.getName(), RegexLeaf.start(), //
				new RegexLeaf(1, "NAME1", "([%pLN_.-]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "PARTICIPATION", "([-=])"), //
				new RegexOptional( //
						new RegexLeaf(1, "CARDINALITY", "([%pLN]+|\\([%pLN]+,[%s]*[%pLN]+\\))")), //
				new RegexLeaf(1, "PARTICIPATION2", "([-=])"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(1, "NAME2", "([%pLN_.-]+)"), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	@Override
	protected CommandExecutionResult executeArg(ChenEerDiagram diagram, LineLocation location, RegexResult arg, ParserPass currentPass)
			throws NoSuchColorException {
		final String name1 = diagram.cleanId(arg.get("NAME1", 0));
		final String name2 = diagram.cleanId(arg.get("NAME2", 0));
		final boolean isDouble = arg.get("PARTICIPATION", 0).equals("=");
		final String cardinality = arg.get("CARDINALITY", 0);

		final Quark<Entity> quark1 = diagram.quarkInContext(true, name1);
		final Entity entity1 = quark1.getData();
		if (entity1 == null) {
			return CommandExecutionResult.error("No such entity: " + name1);
		}

		final Quark<Entity> quark2 = diagram.quarkInContext(true, name2);
		final Entity entity2 = quark2.getData();
		if (entity2 == null) {
			return CommandExecutionResult.error("No such entity: " + name2);
		}

		LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		if (isDouble) {
			linkType = linkType.goBold();
		}
		final Link link = new Link(location, diagram, diagram.getCurrentStyleBuilder(), entity1, entity2, linkType,
				LinkArg.build(Display.getWithNewlines(diagram.getPragma(), cardinality), 3));
		link.setPortMembers(diagram.getPortId(entity1.getName()), diagram.getPortId(entity2.getName()));
		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

}
