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
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.utils.LineLocation;

final public class CommandLinkLollipop extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkLollipop(UmlDiagramType umlDiagramType) {
		super(getRegexConcat(umlDiagramType));
	}

	static RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return RegexConcat.build(CommandLinkLollipop.class.getName() + umlDiagramType, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "HEADER", "@([\\d.]+)"), //
								RegexLeaf.spaceOneOrMore() //
						)), //
				new RegexLeaf(3, "ENT1",
						"(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
								+ "(\\.?[%pLN_]+(?:\\.[%pLN_]+)*|[%g][^%g]+[%g])[%s]*(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "FIRST_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOr(new RegexLeaf(2, "LOL_THEN_ENT", "([()]\\))([-=.]+)"), //
						new RegexLeaf(2, "ENT_THEN_LOL", "([-=.]+)(\\([()])")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional(new RegexLeaf(1, "SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexLeaf(3, "ENT2",
						"(?:" + optionalKeywords(umlDiagramType) + "[%s]+)?"
								+ "(\\.?[%pLN_]+(?:\\.[%pLN_]+)*|[%g][^%g]+[%g])[%s]*(\\<\\<.*\\>\\>)?"), //
				RegexLeaf.spaceZeroOrMore(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf(1, "LABEL_LINK", "(.+)") //
						)), RegexLeaf.end());
	}

	private static String optionalKeywords(UmlDiagramType type) {
		if (type == UmlDiagramType.CLASS)
			return "(interface|enum|annotation|abstract[%s]+class|abstract|class|entity|protocol|struct|exception|metaclass|stereotype|dataclass|record)";

		if (type == UmlDiagramType.OBJECT)
			return "(object)";

		throw new IllegalArgumentException();
	}

	private LeafType getType(String desc) {
		if (desc.charAt(0) == desc.charAt(1))
			return LeafType.LOLLIPOP_HALF;

		return LeafType.LOLLIPOP_FULL;
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, LineLocation location,
			RegexResult arg, ParserPass currentPass) {

		final String ent1 = arg.get("ENT1", 1);
		final String ent2 = arg.get("ENT2", 1);

		final Entity cl1;
		final Entity cl2;
		final Entity normalEntity;

		final String suffix = diagram.getUniqueSequence("lol");
		if (arg.get("LOL_THEN_ENT", 1) == null) {

			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(ent1));
			cl1 = quark.getData();
			if (cl1 == null)
				return CommandExecutionResult.error("No class " + quark.getName());

			final Quark<Entity> idNewLong = diagram.quarkInContext(true, diagram.cleanId(ent1) + suffix);
			final LeafType type = getType(arg.get("ENT_THEN_LOL", 1));
			cl2 = diagram.reallyCreateLeaf(location, idNewLong, Display.getWithNewlines(diagram.getPragma(), ent2),
					type, null);
			normalEntity = cl1;

//			assert arg.get("ENT_THEN_LOL", 0) != null;
//			final Quark ident1 = diagram.buildFromName(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(ent1));
//			final Quark ent1code = diagram.buildFromFullPath(ent1);
//			cl1 = diagram.getOrCreateLeaf(ident1, ent1code, null, null);
//			final Quark idNewLong = diagram.quarkInContext(diagram.cleanIdForQuark(ent1) + suffix);
//			cl2 = diagram.reallyCreateLeaf(idNewLong, Display.getWithNewlines(ent2),
//					getType(arg.get("ENT_THEN_LOL", 1)), null);
//			normalEntity = cl1;
		} else {
			final Quark<Entity> quark = diagram.quarkInContext(true, diagram.cleanId(ent2));
			cl2 = quark.getData();
			if (cl2 == null)
				return CommandExecutionResult.error("No class " + quark.getName());

			final Quark<Entity> idNewLong = diagram.quarkInContext(true, diagram.cleanId(ent2) + suffix);
			final LeafType type = getType(arg.get("LOL_THEN_ENT", 0));
			cl1 = diagram.reallyCreateLeaf(location, idNewLong, Display.getWithNewlines(diagram.getPragma(), ent1),
					type, null);
			normalEntity = cl2;
		}

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		int length = queue.length();
		if (length == 1 && diagram.getNbOfHozizontalLollipop(normalEntity) > 1)
			length++;

		String firstLabel = arg.get("FIRST_LABEL", 0);
		String secondLabel = arg.get("SECOND_LABEL", 0);

		String labelLink = null;

		if (arg.get("LABEL_LINK", 0) != null) {
			labelLink = arg.get("LABEL_LINK", 0);
			if (firstLabel == null && secondLabel == null) {
				final Pattern2 p1 = Pattern2.cmpile("^\"([^\"]+)\"([^\"]+)\"([^\"]+)\"$");
				final Matcher2 m1 = p1.matcher(labelLink);
				if (m1.matches()) {
					firstLabel = m1.group(1);
					labelLink = StringUtils.trin(
							StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m1.group(2))));
					secondLabel = m1.group(3);
				} else {
					final Pattern2 p2 = Pattern2.cmpile("^\"([^\"]+)\"([^\"]+)$");
					final Matcher2 m2 = p2.matcher(labelLink);
					if (m2.matches()) {
						firstLabel = m2.group(1);
						labelLink = StringUtils.trin(StringUtils
								.eventuallyRemoveStartingAndEndingDoubleQuote(StringUtils.trin(m2.group(2))));
						secondLabel = null;
					} else {
						final Pattern2 p3 = Pattern2.cmpile("^([^\"]+)\"([^\"]+)\"$");
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
		}
		final LinkArg linkArg = LinkArg.build(Display.getWithNewlines(diagram.getPragma(), labelLink), length,
				diagram.getSkinParam().classAttributeIconSize() > 0);
		final Link link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2,
				linkType, linkArg.withQuantifier(firstLabel, secondLabel).withDistanceAngle(diagram.getLabeldistance(),
						diagram.getLabelangle()));
		diagram.resetPragmaLabel();
		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

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
		if (arg.get("LOL_THEN_ENT", 1) != null)
			return StringUtils.trin(arg.get("LOL_THEN_ENT", 1));

		if (arg.get("ENT_THEN_LOL", 0) != null)
			return StringUtils.trin(arg.get("ENT_THEN_LOL", 0));

		throw new IllegalArgumentException();
	}

}
