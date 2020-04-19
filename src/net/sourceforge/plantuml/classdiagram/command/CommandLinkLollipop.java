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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOptional;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.utils.UniqueSequence;

final public class CommandLinkLollipop extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkLollipop(UmlDiagramType umlDiagramType) {
		super(getRegexConcat(umlDiagramType));
	}

	static RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return RegexConcat.build(CommandLinkLollipop.class.getName() + umlDiagramType, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf("HEADER", "@([\\d.]+)"), //
								RegexLeaf.spaceOneOrMore() //
						)), //
				new RegexLeaf("ENT1", "(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|[%g][^%g]+[%g])[%s]*(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("FIRST_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(new RegexLeaf("LOL_THEN_ENT", "([()]\\))([-=.]+)"), //
						new RegexLeaf("ENT_THEN_LOL", "([-=.]+)(\\([()])")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf("SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf("ENT2", "(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|[%g][^%g]+[%g])[%s]*(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf("LABEL_LINK", "(.+)") //
						)), RegexLeaf.end());
	}

	private static String optionalKeywords(UmlDiagramType type) {
		if (type == UmlDiagramType.CLASS) {
			return "(interface|enum|annotation|abstract[%s]+class|abstract|class|entity)";
		}
		if (type == UmlDiagramType.OBJECT) {
			return "(object)";
		}
		throw new IllegalArgumentException();
	}

	private LeafType getType(String desc) {
		if (desc.charAt(0) == desc.charAt(1)) {
			return LeafType.LOLLIPOP_HALF;
		}
		return LeafType.LOLLIPOP_FULL;
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, LineLocation location,
			RegexResult arg) {

		final String ent1 = arg.get("ENT1", 1);
		final String ent2 = arg.get("ENT2", 1);

		final IEntity cl1;
		final IEntity cl2;
		final IEntity normalEntity;

		final String suffix = "lol" + UniqueSequence.getValue();
		if (arg.get("LOL_THEN_ENT", 1) == null) {
			assert arg.get("ENT_THEN_LOL", 0) != null;
			final Ident ident1 = diagram.buildLeafIdent(ent1);
			final Code ent1code = diagram.V1972() ? ident1 : diagram.buildCode(ent1);
			cl1 = getFoo1(diagram, ent1code, ident1);
			final Ident idNewLong = diagram.buildLeafIdent(ent1 + suffix);
			cl2 = diagram.createLeaf(idNewLong, idNewLong.toCode(diagram), Display.getWithNewlines(ent2),
					getType(arg.get("ENT_THEN_LOL", 1)), null);
			normalEntity = cl1;
		} else {
			final Ident ident2 = diagram.buildLeafIdent(ent2);
			final Code ent2code = diagram.V1972() ? ident2 : diagram.buildCode(ent2);
			cl2 = getFoo1(diagram, ent2code, ident2);
			final Ident idNewLong = diagram.buildLeafIdent(ent2 + suffix);
			cl1 = diagram.createLeaf(idNewLong, idNewLong.toCode(diagram), Display.getWithNewlines(ent1),
					getType(arg.get("LOL_THEN_ENT", 0)), null);
			normalEntity = cl2;
		}

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		int length = queue.length();
		if (length == 1 && diagram.getNbOfHozizontalLollipop(normalEntity) > 1) {
			length++;
		}

		String firstLabel = arg.get("FIRST_LABEL", 0);
		String secondLabel = arg.get("SECOND_LABEL", 0);

		String labelLink = null;

		if (arg.get("LABEL_LINK", 0) != null) {
			labelLink = arg.get("LABEL_LINK", 0);
			if (firstLabel == null && secondLabel == null) {
				final Pattern2 p1 = MyPattern.cmpile("^\"([^\"]+)\"([^\"]+)\"([^\"]+)\"$");
				final Matcher2 m1 = p1.matcher(labelLink);
				if (m1.matches()) {
					firstLabel = m1.group(1);
					labelLink = StringUtils.trin(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils
							.trin(m1.group(2))));
					secondLabel = m1.group(3);
				} else {
					final Pattern2 p2 = MyPattern.cmpile("^\"([^\"]+)\"([^\"]+)$");
					final Matcher2 m2 = p2.matcher(labelLink);
					if (m2.matches()) {
						firstLabel = m2.group(1);
						labelLink = StringUtils.trin(StringUtils
								.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m2.group(2))));
						secondLabel = null;
					} else {
						final Pattern2 p3 = MyPattern.cmpile("^([^\"]+)\"([^\"]+)\"$");
						final Matcher2 m3 = p3.matcher(labelLink);
						if (m3.matches()) {
							firstLabel = null;
							labelLink = StringUtils.trin(StringUtils
									.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m3.group(1))));
							secondLabel = m3.group(2);
						}
					}
				}
			}
			labelLink = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(labelLink);
		} /*
		 * else if (arg.get("LABEL_LINK_XT").get(0) != null || arg.get("LABEL_LINK_XT").get(1) != null ||
		 * arg.get("LABEL_LINK_XT").get(2) != null) { labelLink = arg.get("LABEL_LINK_XT").get(1); firstLabel =
		 * merge(firstLabel, arg.get("LABEL_LINK_XT").get(0)); secondLabel = merge(arg.get("LABEL_LINK_XT").get(2),
		 * secondLabel); }
		 */

		final Link link = new Link(cl1, cl2, linkType, Display.getWithNewlines(labelLink), length, firstLabel,
				secondLabel, diagram.getLabeldistance(), diagram.getLabelangle(), diagram.getSkinParam()
						.getCurrentStyleBuilder());
		diagram.resetPragmaLabel();
		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

	private IEntity getFoo1(AbstractClassOrObjectDiagram diagram, final Code code, final Ident ident) {
		if (diagram.V1972()) {
			final IEntity result = ident.size() == 1 ? diagram.getLeafVerySmart(ident) : diagram.getLeafStrict(ident);
			if (result != null) {
				return result;
			}
		}
		return diagram.getOrCreateLeaf(ident, code, null, null);
	}

	// private String merge(String a, String b) {
	// if (a == null && b == null) {
	// return null;
	// }
	// if (a == null && b != null) {
	// return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(b);
	// }
	// if (b == null && a != null) {
	// return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(a);
	// }
	// return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(a) +
	// BackSlash.VV1
	// + StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(b);
	// }

	private void addLink(AbstractClassOrObjectDiagram diagram, Link link, String weight) {
		diagram.addLink(link);
		if (weight == null) {
			// final LinkType type = link.getType();
			// --|> highest
			// --*, -->, --o normal
			// ..*, ..>, ..o lowest
			// if (type.isDashed() == false) {
			// if (type.contains(LinkDecor.EXTENDS)) {
			// link.setWeight(3);
			// }
			// if (type.contains(LinkDecor.ARROW) ||
			// type.contains(LinkDecor.COMPOSITION)
			// || type.contains(LinkDecor.AGREGATION)) {
			// link.setWeight(2);
			// }
			// }
		} else {
			link.setWeight(Double.parseDouble(weight));
		}
	}

	private LinkType getLinkType(RegexResult arg) {
		return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
	}

	private String getQueue(RegexResult arg) {
		if (arg.get("LOL_THEN_ENT", 1) != null) {
			return StringUtils.trin(arg.get("LOL_THEN_ENT", 1));
		}
		if (arg.get("ENT_THEN_LOL", 0) != null) {
			return StringUtils.trin(arg.get("ENT_THEN_LOL", 0));
		}
		throw new IllegalArgumentException();
	}

}
