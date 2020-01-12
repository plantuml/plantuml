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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
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
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;

public class CommandLinkActivity extends SingleLineCommand2<ActivityDiagram> {

	public CommandLinkActivity() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkActivity.class.getName(), RegexLeaf.start(), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf("CODE", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
								new RegexLeaf("BAR", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
								new RegexLeaf("QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9_.]+))?"))), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp2(), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("URL", "(" + UrlBuilder.getRegexp() + ")?"), //

				new RegexLeaf("ARROW_BODY1", "([-.]+)"), //
				new RegexLeaf("ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf("ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
				new RegexLeaf("ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf("ARROW_BODY2", "([-.]*)"), //
				new RegexLeaf("\\>"), //

				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("BRACKET", "\\[([^\\]*]+[^\\]]*)\\]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr("FIRST2", //
						new RegexLeaf("STAR2", "(\\(\\*(top|\\d+)?\\))"), //
						new RegexLeaf("OPENBRACKET2", "(\\{)"), //
						new RegexLeaf("CODE2", "([\\p{L}0-9][\\p{L}0-9_.]*)"), //
						new RegexLeaf("BAR2", "(?:==+)[%s]*([\\p{L}0-9_.]+)[%s]*(?:==+)"), //
						new RegexLeaf("QUOTED2", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([\\p{L}0-9][\\p{L}0-9_.]*))?"), //
						new RegexLeaf("QUOTED_INVISIBLE2", "(\\w.*?)")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("STEREOTYPE2", "(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("in"), //
								RegexLeaf.spaceOneOrMore(), //
								new RegexLeaf("PARTITION2", "([%g][^%g]+[%g]|\\S+)") //
						)), //
				RegexLeaf.spaceZeroOrMore(), //
				ColorParser.exp3(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg) {
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
			type = type.goDotted();
		}

		Link link = new Link(entity1, entity2, type, linkLabel, lenght, diagram.getSkinParam().getCurrentStyleBuilder());
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

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();

	}

	static IEntity getEntity(ActivityDiagram diagram, RegexResult arg, final boolean start) {
		final String suf = start ? "" : "2";

		final String openBracket2 = arg.get("OPENBRACKET" + suf, 0);
		if (openBracket2 != null) {
			return diagram.createInnerActivity();
		}
		if (arg.get("STAR" + suf, 0) != null) {
			final String suppId = arg.get("STAR" + suf, 1);
			if (start) {
				if (suppId != null) {
					diagram.getStart().setTop(true);
				}
				return diagram.getStart();
			}
			return diagram.getEnd(suppId);
		}
		String partition = arg.get("PARTITION" + suf, 0);
		if (partition != null) {
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		final String idShort = arg.get("CODE" + suf, 0);
		if (idShort != null) {
			if (partition != null) {
				final Ident idNewLong = diagram.buildLeafIdent(partition);
				final Code codeP = diagram.V1972() ? idNewLong : diagram.buildCode(partition);
				diagram.gotoGroup(idNewLong, codeP, Display.getWithNewlines(partition), GroupType.PACKAGE,
						diagram.getRootGroup(), NamespaceStrategy.SINGLE);
			}
			final Ident ident = diagram.buildLeafIdent(idShort);
			final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
			final LeafType type = diagram.V1972() ? getTypeIfExistingSmart(diagram, ident) : getTypeIfExisting(diagram,
					code);
			IEntity result;
			if (diagram.V1972()) {
				result = diagram.getLeafVerySmart(ident);
				if (result == null)
					result = diagram.getOrCreate(ident, code, Display.getWithNewlines(code), type);
			} else
				result = diagram.getOrCreate(ident, code, Display.getWithNewlines(code), type);
			if (partition != null) {
				diagram.endGroup();
			}
			return result;
		}
		final String bar = arg.get("BAR" + suf, 0);
		if (bar != null) {
			final Ident identBar = diagram.buildLeafIdent(bar);
			final Code codeBar = diagram.V1972() ? identBar : diagram.buildCode(bar);
			if (diagram.V1972()) {
				final ILeaf result = diagram.getLeafVerySmart(identBar);
				if (result != null) {
					return result;
				}
			}
			return diagram.getOrCreate(identBar, codeBar, Display.getWithNewlines(bar), LeafType.SYNCHRO_BAR);
		}
		final RegexPartialMatch quoted = arg.get("QUOTED" + suf);
		if (quoted.get(0) != null) {
			final String quotedString = quoted.get(1) == null ? quoted.get(0) : quoted.get(1);
			if (partition != null) {
				final Ident idNewLong = diagram.buildLeafIdent(partition);
				final Code codeP = diagram.V1972() ? idNewLong : diagram.buildCode(partition);
				diagram.gotoGroup(idNewLong, codeP, Display.getWithNewlines(partition), GroupType.PACKAGE,
						diagram.getRootGroup(), NamespaceStrategy.SINGLE);
			}
			final Ident quotedIdent = diagram.buildLeafIdent(quotedString);
			final Code quotedCode = diagram.V1972() ? quotedIdent : diagram.buildCode(quotedString);
			final LeafType type = diagram.V1972() ? getTypeIfExistingSmart(diagram, quotedIdent) : getTypeIfExisting(
					diagram, quotedCode);
			final IEntity result = diagram.getOrCreate(quotedIdent, quotedCode, Display.getWithNewlines(quoted.get(0)),
					type);
			if (partition != null) {
				diagram.endGroup();
			}
			return result;
		}
		final String quoteInvisibleString = arg.get("QUOTED_INVISIBLE" + suf, 0);
		if (quoteInvisibleString != null) {
			if (partition != null) {
				final Ident idNewLong = diagram.buildLeafIdent(partition);
				final Code codeP = diagram.V1972() ? idNewLong : diagram.buildCode(partition);
				diagram.gotoGroup(idNewLong, codeP, Display.getWithNewlines(partition), GroupType.PACKAGE,
						diagram.getRootGroup(), NamespaceStrategy.SINGLE);
			}
			final Ident identInvisible = diagram.buildLeafIdent(quoteInvisibleString);
			final Code quotedInvisible = diagram.V1972() ? identInvisible : diagram.buildCode(quoteInvisibleString);
			final IEntity result = diagram.getOrCreate(identInvisible, quotedInvisible,
					Display.getWithNewlines(quotedInvisible), LeafType.ACTIVITY);
			if (partition != null) {
				diagram.endGroup();
			}
			return result;
		}
		final String first = arg.get("FIRST" + suf, 0);
		if (first == null) {
			return diagram.getLastEntityConsulted();
		}

		return null;
	}

	private static LeafType getTypeIfExistingSmart(ActivityDiagram system, Ident ident) {
		final IEntity ent = system.getLeafSmart(ident);
		if (ent != null) {
			if (ent.getLeafType() == LeafType.BRANCH) {
				return LeafType.BRANCH;
			}
		}
		return LeafType.ACTIVITY;
	}

	private static LeafType getTypeIfExisting(ActivityDiagram system, Code code) {
		if (system.leafExist(code)) {
			final IEntity ent = system.getLeaf(code);
			if (ent.getLeafType() == LeafType.BRANCH) {
				return LeafType.BRANCH;
			}
		}
		return LeafType.ACTIVITY;
	}

}
