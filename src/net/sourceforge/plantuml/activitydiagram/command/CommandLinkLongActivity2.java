/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5031 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.StringUtils;

public class CommandLinkLongActivity2 extends CommandMultilines2<ActivityDiagram> {

	public CommandLinkLongActivity2() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^[%s]*([^%g]*)[%g](?:[%s]+as[%s]+([\\p{L}0-9][\\p{L}0-9_.]*))?[%s]*(\\<\\<.*\\>\\>)?[%s]*(?:in[%s]+([%g][^%g]+[%g]|\\S+))?[%s]*(#\\w+)?$";
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf("CODE", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
								new RegexLeaf("BAR", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
								new RegexLeaf("QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9_.]+))?"))), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("BACKCOLOR", "(#\\w+)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //

				new RegexLeaf("ARROW_BODY1", "([-.]+)"), //
				new RegexLeaf("ARROW_STYLE1",
						"(?:\\[((?:#\\w+|dotted|dashed|bold|hidden)(?:,#\\w+|,dotted|,dashed|,bold|,hidden)*)\\])?"), //
				new RegexLeaf("ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
				new RegexLeaf("ARROW_STYLE2",
						"(?:\\[((?:#\\w+|dotted|dashed|bold|hidden)(?:,#\\w+|,dotted|,dashed|,bold|,hidden)*)\\])?"), //
				new RegexLeaf("ARROW_BODY2", "([-.]*)\\>"), //

				new RegexLeaf("[%s]*"), //
				new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("DESC", "[%g]([^%g]*?)"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("$"));
	}

	public CommandExecutionResult executeNow(final ActivityDiagram diagram, List<String> lines) {
		StringUtils.trim(lines, false);
		final RegexResult line0 = getStartingPattern().matcher(lines.get(0).trim());

		final IEntity entity1 = CommandLinkActivity.getEntity(diagram, line0, true);

		if (line0.get("STEREOTYPE", 0) != null) {
			entity1.setStereotype(new Stereotype(line0.get("STEREOTYPE", 0)));
		}
		if (line0.get("BACKCOLOR", 0) != null) {
			entity1.setSpecificBackcolor(diagram.getSkinParam().getIHtmlColorSet()
					.getColorIfValid(line0.get("BACKCOLOR", 0)));
		}
		final StringBuilder sb = new StringBuilder();

		final String desc0 = line0.get("DESC", 0);
		Url urlActivity = null;
		if (StringUtils.isNotEmpty(desc0)) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			urlActivity = urlBuilder.getUrl(desc0);
			if (urlActivity == null) {
				sb.append(desc0);
				sb.append("\\n");
			}
		}
		for (int i = 1; i < lines.size() - 1; i++) {
			if (i == 1 && urlActivity == null) {
				final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
				urlActivity = urlBuilder.getUrl(lines.get(i));
				if (urlActivity != null) {
					continue;
				}
			}
			sb.append(lines.get(i));
			if (i < lines.size() - 2) {
				sb.append("\\n");
			}
		}

		final List<String> lineLast = StringUtils.getSplit(MyPattern.cmpile(getPatternEnd()),
				lines.get(lines.size() - 1));
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			if (sb.length() > 0 && sb.toString().endsWith("\\n") == false) {
				sb.append("\\n");
			}
			sb.append(lineLast.get(0));
		}

		final String display = sb.toString();
		final Code code = Code.of(lineLast.get(1) == null ? display : lineLast.get(1));

		String partition = null;
		if (lineLast.get(3) != null) {
			partition = lineLast.get(3);
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		if (partition != null) {
			diagram.getOrCreateGroup(Code.of(partition), Display.getWithNewlines(partition), GroupType.PACKAGE,
					null);
		}
		final IEntity entity2 = diagram.createLeaf(code, Display.getWithNewlines(display), LeafType.ACTIVITY, null);
		if (partition != null) {
			diagram.endGroup();
		}
		if (urlActivity != null) {
			entity2.addUrl(urlActivity);
		}

		if (lineLast.get(2) != null) {
			entity2.setStereotype(new Stereotype(lineLast.get(2)));
		}
		if (lineLast.get(4) != null) {
			entity2.setSpecificBackcolor(diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(lineLast.get(4)));
		}

		if (entity1 == null || entity2 == null) {
			return CommandExecutionResult.error("No such entity");
		}

		final String arrowBody1 = CommandLinkClass.notNull(line0.get("ARROW_BODY1", 0));
		final String arrowBody2 = CommandLinkClass.notNull(line0.get("ARROW_BODY2", 0));
		final String arrowDirection = CommandLinkClass.notNull(line0.get("ARROW_DIRECTION", 0));

		final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");

		final int lenght = arrow.length() - 1;

		final Display linkLabel = Display.getWithNewlines(line0.get("BRACKET", 0));

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if (arrow.contains(".")) {
			type = type.getDotted();
		}
		Link link = new Link(entity1, entity2, type, linkLabel, lenght);
		final Direction direction = StringUtils.getArrowDirection(arrowBody1 + arrowDirection + arrowBody2 + ">");
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		if (line0.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url urlLink = urlBuilder.getUrl(line0.get("URL", 0));
			link.setUrl(urlLink);
		}

		CommandLinkClass.applyStyle(line0.getLazzy("ARROW_STYLE", 0), link);
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

}
