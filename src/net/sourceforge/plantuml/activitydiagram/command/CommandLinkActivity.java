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
package net.sourceforge.plantuml.activitydiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexPartialMatch;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandLinkActivity extends SingleLineCommand2<ActivityDiagram> {

	public CommandLinkActivity() {
		super(getRegexConcat());
	}

	private static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkActivity.class.getName(), RegexLeaf.start(), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf("STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf("CODE", "([%pLN][%pLN_.]*)"), //
								new RegexLeaf("BAR", "(?:==+)[%s]*([%pLN_.]+)[%s]*(?:==+)"), //
								new RegexLeaf("QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([%pLN_.]+))?"))), //
				StereotypePattern.optional("STEREOTYPE"), //
				ColorParser.exp2(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //

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
						new RegexLeaf("CODE2", "([%pLN][%pLN_.]*)"), //
						new RegexLeaf("BAR2", "(?:==+)[%s]*([%pLN_.]+)[%s]*(?:==+)"), //
						new RegexLeaf("QUOTED2", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([%pLN][%pLN_.]*))?"), //
						new RegexLeaf("QUOTED_INVISIBLE2", "(\\w.*?)")), //
				StereotypePattern.optional("STEREOTYPE2"), //
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
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, LineLocation location, RegexResult arg)
			throws NoSuchColorException {
		final Entity entity1 = getEntity(diagram, arg, true);

		if (entity1 == null)
			return CommandExecutionResult.error("No such activity");

		if (arg.get("STEREOTYPE", 0) != null)
			entity1.setStereotype(Stereotype.build(arg.get("STEREOTYPE", 0)));

		if (arg.get("BACKCOLOR", 0) != null) {
			String s = arg.get("BACKCOLOR", 0);
			entity1.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		}

		final Entity entity2 = getEntity(diagram, arg, false);
		if (entity2 == null)
			return CommandExecutionResult.error("No such activity");

		diagram.setLastEntityConsulted(entity2);

		if (arg.get("BACKCOLOR2", 0) != null) {
			String s = arg.get("BACKCOLOR2", 0);
			entity2.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		}
		if (arg.get("STEREOTYPE2", 0) != null)
			entity2.setStereotype(Stereotype.build(arg.get("STEREOTYPE2", 0)));

		final Display linkLabel = Display.getWithNewlines(arg.get("BRACKET", 0));

		final String arrowBody1 = CommandLinkClass.notNull(arg.get("ARROW_BODY1", 0));
		final String arrowBody2 = CommandLinkClass.notNull(arg.get("ARROW_BODY2", 0));
		final String arrowDirection = CommandLinkClass.notNull(arg.get("ARROW_DIRECTION", 0));

		final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");
		int lenght = arrow.length() - 1;
		if (arrowDirection.contains("*"))
			lenght = 2;

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if ((arrowBody1 + arrowBody2).contains("."))
			type = type.goDotted();

		final LinkArg linkArg = LinkArg.build(linkLabel, lenght, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link link = new Link(diagram.getEntityFactory(), diagram.getSkinParam().getCurrentStyleBuilder(), entity1,
				entity2, type, linkArg);
		if (arrowDirection.contains("*"))
			link.setConstraint(false);

		final Direction direction = StringUtils.getArrowDirection(arrowBody1 + arrowDirection + arrowBody2 + ">");
		if (direction == Direction.LEFT || direction == Direction.UP)
			link = link.getInv();

		if (arg.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url urlLink = urlBuilder.getUrl(arg.get("URL", 0));
			link.setUrl(urlLink);
		}

		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();

	}

	static Entity getEntity(ActivityDiagram diagram, RegexResult arg, final boolean start) {
		final String suf = start ? "" : "2";

		final String openBracket2 = arg.get("OPENBRACKET" + suf, 0);
		if (openBracket2 != null)
			return diagram.createInnerActivity();

		if (arg.get("STAR" + suf, 0) != null) {
			final String suppId = arg.get("STAR" + suf, 1);
			if (start) {
//				if (suppId != null)
//					diagram.getStart().setTop(true);
				return diagram.getStart();
			}
			return diagram.getEnd(suppId);
		}
		String partition = arg.get("PARTITION" + suf, 0);
		if (partition != null)
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);

		final String idShort = arg.get("CODE" + suf, 0);
		if (idShort != null) {
			if (partition != null) {
				final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(partition));
				diagram.gotoGroup(quark, Display.getWithNewlines(quark), GroupType.PACKAGE);
			}
			final Quark<Entity> ident = diagram.quarkInContext(true, diagram.cleanId(idShort));

			final LeafType type = getTypeIfExisting(diagram, ident);
			Entity result = ident.getData();
			if (result == null)
				result = diagram.reallyCreateLeaf(ident, Display.getWithNewlines(idShort), type, null);

			if (partition != null)
				diagram.endGroup();

			return result;
		}
		final String bar = arg.get("BAR" + suf, 0);
		if (bar != null) {
			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(bar));
			Entity result = quark.getData();
			if (result == null)
				result = diagram.reallyCreateLeaf(quark, Display.getWithNewlines(bar), LeafType.SYNCHRO_BAR, null);
			return result;
		}
		final RegexPartialMatch quoted = arg.get("QUOTED" + suf);
		if (quoted.get(0) != null) {
			final String quotedString = quoted.get(1) == null ? quoted.get(0) : quoted.get(1);
			if (partition != null) {
				final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(partition));
				diagram.gotoGroup(quark, Display.getWithNewlines(partition), GroupType.PACKAGE);
			}

			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(quotedString));

			final LeafType type = getTypeIfExisting(diagram, quark);
			Entity result = quark.getData();
			if (result == null)
				result = diagram.reallyCreateLeaf(quark, Display.getWithNewlines(quoted.get(0)), type, null);
			if (partition != null)
				diagram.endGroup();

			return result;
		}
		final String quoteInvisibleString = arg.get("QUOTED_INVISIBLE" + suf, 0);
		if (quoteInvisibleString != null) {
			if (partition != null) {
				final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(partition));
				diagram.gotoGroup(quark, Display.getWithNewlines(quark), GroupType.PACKAGE);
			}
			final Quark<Entity> identInvisible = diagram.quarkInContext(true, diagram.cleanId(quoteInvisibleString));
			Entity result = identInvisible.getData();
			if (result == null)
				result = diagram.reallyCreateLeaf(identInvisible, Display.getWithNewlines(identInvisible.getName()),
						LeafType.ACTIVITY, null);
			if (partition != null)
				diagram.endGroup();

			return result;
		}
		final String first = arg.get("FIRST" + suf, 0);
		if (first == null)
			return diagram.getLastEntityConsulted();

		return null;
	}

	private static LeafType getTypeIfExisting(ActivityDiagram system, Quark<Entity> code) {
//		if (code.getData() == null) {
//			final Quark quark = system.getPlasma().getIfExistsFromName(code.getName());
//			final IEntity ent = quark == null ? null : (ILeaf) quark.getData();
//			if (ent.getLeafType() == LeafType.BRANCH)
//				return LeafType.BRANCH;
//		}
		return LeafType.ACTIVITY;
	}

}
