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
package net.sourceforge.plantuml.activitydiagram.command;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;

public class CommandIf extends SingleLineCommand2<ActivityDiagram> {

	public CommandIf() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandIf.class.getName(), RegexLeaf.start(), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf("CODE", "([\\p{L}0-9_.]+)"), //
								new RegexLeaf("BAR", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
								new RegexLeaf("QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9_.]+))?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				//new RegexOptional(new RegexLeaf("ARROW", "([=-]+(?:(left|right|up|down|le?|ri?|up?|do?)(?=[-=.]))?[=-]*\\>)")), //
				new RegexOptional(new RegexConcat( // 
						new RegexLeaf("ARROW_BODY1", "([-.]+)"), //
						new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf("ARROW_BODY2", "([-.]*)"), //
						new RegexLeaf("\\>") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("BRACKET", "\\[([^\\]*]+[^\\]]*)\\]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(//
						new RegexLeaf("IF1", "if[%s]*[%g]([^%g]*)[%g][%s]*(?:as[%s]+([\\p{L}0-9_.]+)[%s]+)?"), //
						new RegexLeaf("IF2", "if[%s]+(.+?)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("then")), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg) {
		final IEntity entity1 = CommandLinkActivity.getEntity(diagram, arg, true);
		if (entity1 == null) {
			return CommandExecutionResult.error("No if possible at this point");
		}

		final String ifCode;
		final String ifLabel;
		if (arg.get("IF2", 0) == null) {
			ifCode = arg.get("IF1", 1);
			ifLabel = arg.get("IF1", 0);
		} else {
			ifCode = null;
			ifLabel = arg.get("IF2", 0);
		}
		diagram.startIf(ifCode);

		int lenght = 2;

		if (arg.get("ARROW_BODY1", 0) != null) {
//			final String arrow = StringUtils.manageArrowForCuca(arg.get("ARROW", 0));
//			lenght = arrow.length() - 1;
			final String arrowBody1 = CommandLinkClass.notNull(arg.get("ARROW_BODY1", 0));
			final String arrowBody2 = CommandLinkClass.notNull(arg.get("ARROW_BODY2", 0));
			final String arrowDirection = CommandLinkClass.notNull(arg.get("ARROW_DIRECTION", 0));

			final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");
			lenght = arrow.length() - 1;
		}

		final IEntity branch = diagram.getCurrentContext().getBranch();

		Link link = new Link(entity1, branch, new LinkType(LinkDecor.ARROW, LinkDecor.NONE),
				Display.getWithNewlines(arg.get("BRACKET", 0)), lenght, null, ifLabel, diagram.getLabeldistance(),
				diagram.getLabelangle(), diagram.getSkinParam().getCurrentStyleBuilder());
		if (arg.get("ARROW", 0) != null) {
			final Direction direction = StringUtils.getArrowDirection(arg.get("ARROW", 0));
			if (direction == Direction.LEFT || direction == Direction.UP) {
				link = link.getInv();
			}
		}

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

}
