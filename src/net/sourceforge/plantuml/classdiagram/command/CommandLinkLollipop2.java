/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5436 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.UniqueSequence;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

final public class CommandLinkLollipop2 extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkLollipop2(AbstractClassOrObjectDiagram diagram) {
		super(diagram, getRegexConcat(diagram.getUmlDiagramType()));
	}

	static RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return new RegexConcat(new RegexLeaf("HEADER", "^(?:@([\\d.]+)\\s+)?"), //
				new RegexLeaf("ENT1", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|\"[^\"]+\")\\s*(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("FIRST_LABEL", "(?:\"([^\"]+)\")?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr(new RegexLeaf("LOL_THEN_ENT", "\\(\\)([-=.]+)"), //
						new RegexLeaf("ENT_THEN_LOL", "([-=.]+)\\(\\)")), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("SECOND_LABEL", "(?:\"([^\"]+)\")?"), //
				new RegexLeaf("\\s*"), //
				new RegexLeaf("ENT2", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*|\"[^\"]+\")\\s*(\\<\\<.*\\>\\>)?"), //
				new RegexLeaf("\\s*"), //
				new RegexOr(null, true, //
						new RegexLeaf("LABEL_LINK", ":\\s*([^\"]+)"), //
						new RegexLeaf("LABEL_LINK_XT", ":\\s*(\"[^\"]*\")?\\s*([^\"]*)\\s*(\"[^\"]*\")?")), //
				new RegexLeaf("$"));
	}

	private static String optionalKeywords(UmlDiagramType type) {
		if (type == UmlDiagramType.CLASS) {
			return "(interface|enum|abstract\\s+class|abstract|class)";
		}
		if (type == UmlDiagramType.OBJECT) {
			return "(object)";
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {

		final String ent1 = arg.get("ENT1").get(1);
		final String ent2 = arg.get("ENT2").get(1);

		final Entity cl1;
		final Entity cl2;
		final Entity normalEntity;

		final String suffix = "lol" + UniqueSequence.getValue();
		if (arg.get("LOL_THEN_ENT").get(0) != null) {
			cl2 = (Entity) getSystem().getOrCreateClass(ent2);
			cl1 = getSystem().createEntity(cl2.getCode() + suffix, ent1, EntityType.LOLLIPOP);
			normalEntity = cl2;
		} else {
			assert arg.get("ENT_THEN_LOL").get(0) != null;
			cl1 = (Entity) getSystem().getOrCreateClass(ent1);
			cl2 = getSystem().createEntity(cl1.getCode() + suffix, ent2, EntityType.LOLLIPOP);
			normalEntity = cl1;
		}

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		int length = queue.length();
		if (length == 1 && getSystem().getNbOfHozizontalLollipop(normalEntity) > 1) {
			length++;
		}

		String firstLabel = arg.get("FIRST_LABEL").get(0);
		String secondLabel = arg.get("SECOND_LABEL").get(0);

		String labelLink = null;

		if (arg.get("LABEL_LINK").get(0) != null) {
			labelLink = arg.get("LABEL_LINK").get(0);
		} else if (arg.get("LABEL_LINK_XT").get(0) != null || arg.get("LABEL_LINK_XT").get(1) != null
				|| arg.get("LABEL_LINK_XT").get(2) != null) {
			labelLink = arg.get("LABEL_LINK_XT").get(1);
			firstLabel = merge(firstLabel, arg.get("LABEL_LINK_XT").get(0));
			secondLabel = merge(arg.get("LABEL_LINK_XT").get(2), secondLabel);
		}

		final Link link = new Link(cl1, cl2, linkType, labelLink, length, firstLabel, secondLabel, getSystem()
				.getLabeldistance(), getSystem().getLabelangle());
		getSystem().resetPragmaLabel();
		addLink(link, arg.get("HEADER").get(0));

		return CommandExecutionResult.ok();
	}

	private String merge(String a, String b) {
		if (a == null && b == null) {
			return null;
		}
		if (a == null && b != null) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(b);
		}
		if (b == null && a != null) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(a);
		}
		return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(a) + "\\n"
				+ StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(b);
	}

	private void addLink(Link link, String weight) {
		getSystem().addLink(link);
		if (weight == null) {
			final LinkType type = link.getType();
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

	private LinkType getLinkType(Map<String, RegexPartialMatch> arg) {
		return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
	}

	private String getQueue(Map<String, RegexPartialMatch> arg) {
		if (arg.get("LOL_THEN_ENT").get(0) != null) {
			return arg.get("LOL_THEN_ENT").get(0).trim();
		}
		if (arg.get("ENT_THEN_LOL").get(0) != null) {
			return arg.get("ENT_THEN_LOL").get(0).trim();
		}
		throw new IllegalArgumentException();
	}

}
