/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
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
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandLinkActivity extends SingleLineCommand2<ActivityDiagram> {

	public CommandLinkActivity() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
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
				ColorParser.exp2(), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //

				new RegexLeaf("ARROW_BODY1", "([-.]+)"), //
				new RegexLeaf("ARROW_STYLE1",
						"(?:\\[((?:#\\w+|dotted|dashed|plain|bold|hidden)(?:,#\\w+|,dotted|,dashed|,plain|,bold|,hidden)*)\\])?"), //
				new RegexLeaf("ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
				new RegexLeaf("ARROW_STYLE2",
						"(?:\\[((?:#\\w+|dotted|dashed|plain|bold|hidden)(?:,#\\w+|,dotted|,dashed|,plain|,bold|,hidden)*)\\])?"), //
				new RegexLeaf("ARROW_BODY2", "([-.]*)\\>"), //

				new RegexLeaf("[%s]*"), //
				new RegexLeaf("BRACKET", "(?:\\[([^\\]*]+[^\\]]*)\\])?"), //
				new RegexLeaf("[%s]*"), //
				new RegexOr("FIRST2", //
						new RegexLeaf("STAR2", "(\\(\\*(top|\\d+)?\\))"), //
						new RegexLeaf("OPENBRACKET2", "(\\{)"), //
						new RegexLeaf("CODE2", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
						new RegexLeaf("BAR2", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
						new RegexLeaf("QUOTED2", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9][\\p{L}0-9_.]*))?"), //
						new RegexLeaf("QUOTED_INVISIBLE2", "(\\w.*?)")), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("STEREOTYPE2", "(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("PARTITION2", "(?:in[%s]+([%g][^%g]+[%g]|\\S+))?"), //
				new RegexLeaf("[%s]*"), //
				ColorParser.exp3(), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, RegexResult arg) {
		final IEntity entity1 = getEntity(diagram, arg, true);
		if (entity1 == null) {
			return CommandExecutionResult.error("No such activity");
		}
		if (arg.get("STEREOTYPE", 0) != null) {
			entity1.setStereotype(new Stereotype(arg.get("STEREOTYPE", 0)));
		}
		if (arg.get("BACKCOLOR", 0) != null) {
			entity1.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet()
					.getColorIfValid(arg.get("BACKCOLOR", 0)));
		}

		final IEntity entity2 = getEntity(diagram, arg, false);
		if (entity2 == null) {
			return CommandExecutionResult.error("No such activity");
		}
		if (arg.get("BACKCOLOR2", 0) != null) {
			entity2.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet()
					.getColorIfValid(arg.get("BACKCOLOR2", 0)));
		}
		if (arg.get("STEREOTYPE2", 0) != null) {
			entity2.setStereotype(new Stereotype(arg.get("STEREOTYPE2", 0)));
		}

		final Display linkLabel = Display.getWithNewlines(arg.get("BRACKET", 0));

		final String arrowBody1 = CommandLinkClass.notNull(arg.get("ARROW_BODY1", 0));
		final String arrowBody2 = CommandLinkClass.notNull(arg.get("ARROW_BODY2", 0));
		final String arrowDirection = CommandLinkClass.notNull(arg.get("ARROW_DIRECTION", 0));

		final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");
		int lenght = arrow.length() - 1;
		if (arrowDirection.contains("*")) {
			lenght = 2;
		}

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if ((arrowBody1 + arrowBody2).contains(".")) {
			type = type.getDotted();
		}

		Link link = new Link(entity1, entity2, type, linkLabel, lenght);
		if (arrowDirection.contains("*")) {
			link.setConstraint(false);
		}
		final Direction direction = StringUtils.getArrowDirection(arrowBody1 + arrowDirection + arrowBody2 + ">");
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}
		if (arg.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url urlLink = urlBuilder.getUrl(arg.get("URL", 0));
			link.setUrl(urlLink);
		}

		CommandLinkClass.applyStyle(arg.getLazzy("ARROW_STYLE", 0), link);
		diagram.addLink(link);

		return CommandExecutionResult.ok();

	}

	static IEntity getEntity(ActivityDiagram system, RegexResult arg, final boolean start) {
		final String suf = start ? "" : "2";

		final String openBracket2 = arg.get("OPENBRACKET" + suf, 0);
		if (openBracket2 != null) {
			return system.createInnerActivity();
		}
		if (arg.get("STAR" + suf, 0) != null) {
			final String suppId = arg.get("STAR" + suf, 1);
			if (start) {
				if (suppId != null) {
					system.getStart().setTop(true);
				}
				return system.getStart();
			}
			return system.getEnd(suppId);
		}
		String partition = arg.get("PARTITION" + suf, 0);
		if (partition != null) {
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		final Code code = Code.of(arg.get("CODE" + suf, 0));
		if (code != null) {
			if (partition != null) {
				system.getOrCreateGroup(Code.of(partition), Display.getWithNewlines(partition), GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(code, Display.getWithNewlines(code),
					CommandLinkActivity.getTypeIfExisting(system, code));
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final String bar = arg.get("BAR" + suf, 0);
		if (bar != null) {
			return system.getOrCreate(Code.of(bar), Display.getWithNewlines(bar), LeafType.SYNCHRO_BAR);
		}
		final RegexPartialMatch quoted = arg.get("QUOTED" + suf);
		if (quoted.get(0) != null) {
			final Code quotedCode = Code.of(quoted.get(1) == null ? quoted.get(0) : quoted.get(1));
			if (partition != null) {
				system.getOrCreateGroup(Code.of(partition), Display.getWithNewlines(partition), GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(quotedCode, Display.getWithNewlines(quoted.get(0)),
					CommandLinkActivity.getTypeIfExisting(system, quotedCode));
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final Code quotedInvisible = Code.of(arg.get("QUOTED_INVISIBLE" + suf, 0));
		if (quotedInvisible != null) {
			if (partition != null) {
				system.getOrCreateGroup(Code.of(partition), Display.getWithNewlines(partition), GroupType.PACKAGE,
						system.getRootGroup());
			}
			final IEntity result = system.getOrCreate(quotedInvisible, Display.getWithNewlines(quotedInvisible),
					LeafType.ACTIVITY);
			if (partition != null) {
				system.endGroup();
			}
			return result;
		}
		final String first = arg.get("FIRST" + suf, 0);
		if (first == null) {
			return system.getLastEntityConsulted();
		}

		return null;
	}

	static LeafType getTypeIfExisting(ActivityDiagram system, Code code) {
		if (system.leafExist(code)) {
			final IEntity ent = system.getLeafsget(code);
			if (ent.getEntityType() == LeafType.BRANCH) {
				return LeafType.BRANCH;
			}
		}
		return LeafType.ACTIVITY;
	}

}
