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

import java.util.List;

import net.sourceforge.plantuml.Lazy;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.GroupType;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.classdiagram.command.CommandLinkClass;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.Trim;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.Direction;

public class CommandLinkLongActivity extends CommandMultilines2<ActivityDiagram> {

	private final static Lazy<Pattern2> END = new Lazy<>(() -> Pattern2.cmpile("^[%s]*([^%g]*)[%g](?:[%s]+as[%s]+([%pLN][%pLN_.]*))?[%s]*(\\<\\<.*\\>\\>)?[%s]*(?:in[%s]+([%g][^%g]+[%g]|\\S+))?[%s]*(#\\w+)?$"));

	public CommandLinkLongActivity() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE, Trim.BOTH, END);
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandLinkLongActivity.class.getName(), RegexLeaf.start(), //
				new RegexOptional(//
						new RegexOr("FIRST", //
								new RegexLeaf(2, "STAR", "(\\(\\*(top)?\\))"), //
								new RegexLeaf(1, "CODE", "([%pLN][%pLN_.]*)"), //
								new RegexLeaf(1, "BAR", "(?:==+)[%s]*([%pLN_.]+)[%s]*(?:==+)"), //
								new RegexLeaf(2, "QUOTED", "[%g]([^%g]+)[%g](?:[%s]+as[%s]+([%pLN_.]+))?"))), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexLeaf(1, "BACKCOLOR", "(#\\w+)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //

				new RegexLeaf(1, "ARROW_BODY1", "([-.]+)"), //
				new RegexLeaf(1, "ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf(1, "ARROW_DIRECTION", "(\\*|left|right|up|down|le?|ri?|up?|do?)?"), //
				new RegexLeaf(1, "ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
				new RegexLeaf(1, "ARROW_BODY2", "([-.]*)"), //
				new RegexLeaf("\\>"), //

				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "BRACKET", "\\[([^\\]*]+[^\\]]*)\\]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("[%g]"), //
				new RegexLeaf(1, "DESC", "([^%g]*?)"), //
				RegexLeaf.spaceZeroOrMore(), //
				RegexLeaf.end());
	}

	@Override
	protected CommandExecutionResult executeNow(final ActivityDiagram diagram, BlocLines lines, ParserPass currentPass)
			throws NoSuchColorException {
		lines = lines.trim();
		final RegexResult line0 = getStartingPattern().matcher(lines.getFirst().getTrimmed().getString());

		final Entity entity1 = CommandLinkActivity.getEntity(lines.getLocation(), diagram, line0, true);
		if (entity1 == null)
			return CommandExecutionResult.error("No such entity");

		if (line0.get("STEREOTYPE", 0) != null)
			entity1.setStereotype(Stereotype.build(line0.get("STEREOTYPE", 0)));

		final String stringColor = line0.get("BACKCOLOR", 0);
		if (stringColor != null) {
			entity1.setSpecificColorTOBEREMOVED(ColorType.BACK,
					diagram.getSkinParam().getIHtmlColorSet().getColor(stringColor));
		}
		final StringBuilder sb = new StringBuilder();

		final String desc0 = line0.get("DESC", 0);
		Url urlActivity = null;
		if (StringUtils.isNotEmpty(desc0)) {
			urlActivity = extractUrlString(diagram, desc0);
			if (urlActivity == null) {
				sb.append(desc0);
				sb.append(Jaws.BLOCK_E1_NEWLINE);
			}
		}
		int i = 0;
		for (StringLocated cs : lines.subExtract(1, 1)) {
			i++;
			if (i == 1 && urlActivity == null) {
				urlActivity = extractUrl(diagram, cs);
				if (urlActivity != null)
					continue;

			}
			sb.append(cs.getString());
			if (i < lines.size() - 2)
				sb.append(Jaws.BLOCK_E1_NEWLINE);

		}

		final List<String> lineLast = StringUtils.getSplit(getEndPattern(), lines.getLast().getString());
		if (StringUtils.isNotEmpty(lineLast.get(0))) {
			if (sb.length() > 0 && sb.toString().endsWith("" + Jaws.BLOCK_E1_NEWLINE) == false)
				sb.append(Jaws.BLOCK_E1_NEWLINE);

			sb.append(lineLast.get(0));
		}

		final String displayString = sb.toString();
		final String idShort = lineLast.get(1) == null ? displayString : lineLast.get(1);

		String partition = null;
		if (lineLast.get(3) != null) {
			partition = lineLast.get(3);
			partition = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(partition);
		}
		if (partition != null) {
			final Quark<Entity> idNewLong = diagram.quarkInContext(true, diagram.cleanId(partition));
			diagram.gotoGroup(lines.getLocation(), idNewLong, Display.getWithNewlines(diagram.getPragma(), partition),
					GroupType.PACKAGE);
		}
		final Quark<Entity> ident = diagram.quarkInContext(true, diagram.cleanId(idShort));

		Entity entity2 = ident.getData();
		if (entity2 == null)
			entity2 = diagram.reallyCreateLeaf(lines.getLocation(), ident,
					Display.getWithNewlines(diagram.getPragma(), displayString), LeafType.ACTIVITY, null);

		diagram.setLastEntityConsulted(entity2);

		if (partition != null)
			diagram.endGroup();

		if (urlActivity != null)
			entity2.addUrl(urlActivity);

		if (lineLast.get(2) != null)
			entity2.setStereotype(Stereotype.build(lineLast.get(2)));

		if (lineLast.get(4) != null) {
			String s = lineLast.get(4);
			entity2.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColor(s));
		}

		final String arrowBody1 = CommandLinkClass.notNull(line0.get("ARROW_BODY1", 0));
		final String arrowBody2 = CommandLinkClass.notNull(line0.get("ARROW_BODY2", 0));
		final String arrowDirection = CommandLinkClass.notNull(line0.get("ARROW_DIRECTION", 0));

		final String arrow = StringUtils.manageArrowForCuca(arrowBody1 + arrowDirection + arrowBody2 + ">");

		final int lenght = arrow.length() - 1;

		final Display linkLabel = Display.getWithNewlines(diagram.getPragma(), line0.get("BRACKET", 0));

		LinkType type = new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		if (arrow.contains("."))
			type = type.goDotted();

		final LinkArg linkArg = LinkArg.build(linkLabel, lenght, diagram.getSkinParam().classAttributeIconSize() > 0);
		Link link = new Link(lines.getLocation(), diagram, diagram.getSkinParam().getCurrentStyleBuilder(), entity1,
				entity2, type, linkArg);
		final Direction direction = StringUtils.getArrowDirection(arrowBody1 + arrowDirection + arrowBody2 + ">");
		if (direction == Direction.LEFT || direction == Direction.UP)
			link = link.getInv();

		if (line0.get("URL", 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url urlLink = urlBuilder.getUrl(line0.get("URL", 0));
			link.setUrl(urlLink);
		}

		link.applyStyle(line0.getLazzy("ARROW_STYLE", 0));
		diagram.addLink(link);

		return CommandExecutionResult.ok();
	}

	public Url extractUrl(final ActivityDiagram diagram, StringLocated string) {
		return extractUrlString(diagram, string.getString());
	}

	public Url extractUrlString(final ActivityDiagram diagram, String string) {
		final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
		return urlBuilder.getUrl(string);
	}

}
