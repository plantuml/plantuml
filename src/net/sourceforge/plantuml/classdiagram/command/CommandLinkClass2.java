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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

final public class CommandLinkClass2 extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	public CommandLinkClass2(AbstractClassOrObjectDiagram diagram) {
		super(diagram, getRegexConcat(diagram.getUmlDiagramType()));
	}

	static RegexConcat getRegexConcat(UmlDiagramType umlDiagramType) {
		return new RegexConcat(
				new RegexLeaf("HEADER", "^(?:@(\\d+)\\s+)?"),
				new RegexOr(
						new RegexLeaf("ENT1", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
								+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)"),
						new RegexLeaf("COUPLE1",
								"\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)")),
				new RegexLeaf("\\s*"),
				new RegexLeaf("FIRST_LABEL", "(?:\"([^\"]+)\")?"),
				new RegexLeaf("\\s*"),
				new RegexOr(new RegexLeaf("LEFT_TO_RIGHT",
						"(([-=.]+)(left|right|up|down|le?|ri?|up?|do?)?([-=.]*)(o +|[\\]>*+]|\\|[>\\]])?)"),
						new RegexLeaf("RIGHT_TO_LEFT",
								"(( +o|[\\[<*+]|[<\\[]\\|)?([-=.]*)(left|right|up|down|le?|ri?|up?|do?)?([-=.]+))"),
						new RegexLeaf("NAV_AGREG_OR_COMPO_INV",
								"(\\<([-=.]*)(left|right|up|down|le?|ri?|up?|do?[-=.]+)?([-=.]+)(o +|\\*))"),
						new RegexLeaf("NAV_AGREG_OR_COMPO",
								"(( +o|\\*)([-=.]+)(left|right|up|down|le?|ri?|up?|do?)?([-=.]*)\\>)")),
				new RegexLeaf("\\s*"),
				new RegexLeaf("SECOND_LABEL", "(?:\"([^\"]+)\")?"),
				new RegexLeaf("\\s*"),
				new RegexOr(
						new RegexLeaf("ENT2", "(?:" + optionalKeywords(umlDiagramType) + "\\s+)?"
								+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)"),
						new RegexLeaf("COUPLE2",
								"\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)")),
				new RegexLeaf("\\s*"), new RegexLeaf("LABEL_LINK", "(?::\\s*([^\"]+))?$"));
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
		if (ent1 == null) {
			return executeArgSpecial1(arg);
		}
		if (ent2 == null) {
			return executeArgSpecial2(arg);
		}
		if (getSystem().isGroup(ent1) && getSystem().isGroup(ent2)) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(ent1) || getSystem().isGroup(ent2)) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final Entity cl1 = (Entity) getSystem().getOrCreateClass(ent1);
		final Entity cl2 = (Entity) getSystem().getOrCreateClass(ent2);

		if (arg.get("ENT1").get(0) != null) {
			final EntityType type = EntityType.getEntityType(arg.get("ENT1").get(0));
			if (type != EntityType.OBJECT) {
				cl1.muteToType(type);
			}
		}
		if (arg.get("ENT2").get(0) != null) {
			final EntityType type = EntityType.getEntityType(arg.get("ENT2").get(0));
			if (type != EntityType.OBJECT) {
				cl2.muteToType(type);
			}
		}

		final LinkType linkType = getLinkType(arg);
		Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}
		if (dir != null && linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
			dir = dir.getInv();
		}

		Link link = new Link(cl1, cl2, linkType, arg.get("LABEL_LINK").get(0), queue.length(), arg.get("FIRST_LABEL")
				.get(0), arg.get("SECOND_LABEL").get(0), getSystem().getLabeldistance(), getSystem().getLabelangle());

		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}

		//		getSystem().resetPragmaLabel();
		addLink(link, arg.get("HEADER").get(0));

		return CommandExecutionResult.ok();
	}

	private void addLink(Link link, String arg0) {
		getSystem().addLink(link);
		if (arg0 == null) {
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
			link.setWeight(Integer.parseInt(arg0));
		}
	}

	private CommandExecutionResult executePackageLink(Map<String, RegexPartialMatch> arg) {
		final String ent1 = arg.get("ENT1").get(1);
		final String ent2 = arg.get("ENT2").get(1);
		final Group cl1 = getSystem().getGroup(ent1);
		final Group cl2 = getSystem().getGroup(ent2);

		final LinkType linkType = getLinkType(arg);
		final Direction dir = getDirection(arg);
		final String queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			queue = "-";
		} else {
			queue = getQueue(arg);
		}

		Link link = new Link(cl1.getEntityCluster(), cl2.getEntityCluster(), linkType, arg.get("LABEL_LINK").get(
				0), queue.length(), arg.get("FIRST_LABEL").get(0), arg.get("SECOND_LABEL").get(0), getSystem()
				.getLabeldistance(), getSystem().getLabelangle());
		if (dir == Direction.LEFT || dir == Direction.UP) {
			link = link.getInv();
		}

		getSystem().resetPragmaLabel();
		addLink(link, arg.get("HEADER").get(0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(Map<String, RegexPartialMatch> arg) {
		final String clName1 = arg.get("COUPLE1").get(0);
		final String clName2 = arg.get("COUPLE1").get(1);
		if (getSystem().entityExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().entityExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}
		final Entity node = createAssociationPoint(clName1, clName2);

		final String ent2 = arg.get("ENT2").get(1);
		final IEntity cl2 = getSystem().getOrCreateClass(ent2);

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		final Link link = new Link(node, cl2, linkType, arg.get("LABEL_LINK").get(0), queue.length());
		addLink(link, arg.get("HEADER").get(0));

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(Map<String, RegexPartialMatch> arg) {
		final String clName1 = arg.get("COUPLE2").get(0);
		final String clName2 = arg.get("COUPLE2").get(1);
		if (getSystem().entityExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().entityExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}
		final Entity node = createAssociationPoint(clName1, clName2);

		final String ent1 = arg.get("ENT1").get(1);
		final IEntity cl1 = getSystem().getOrCreateClass(ent1);

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		final Link link = new Link(cl1, node, linkType, arg.get("LABEL_LINK").get(0), queue.length());
		addLink(link, arg.get("HEADER").get(0));

		return CommandExecutionResult.ok();
	}

	private Entity createAssociationPoint(final String clName1, final String clName2) {
		final IEntity entity1 = getSystem().getOrCreateClass(clName1);
		final IEntity entity2 = getSystem().getOrCreateClass(clName2);

		final Entity node = getSystem().createEntity(clName1 + "," + clName2, "node", EntityType.POINT_FOR_ASSOCIATION);

		getSystem().insertBetween(entity1, entity2, node);
		return node;
	}

	private LinkType getLinkTypeNormal(RegexPartialMatch regexPartialMatch) {
		final String queue = regexPartialMatch.get(1).trim() + regexPartialMatch.get(3).trim();
		final String key = regexPartialMatch.get(4);
		return getLinkType(queue, key);
	}

	private LinkType getLinkTypeInv(RegexPartialMatch regexPartialMatch) {
		final String queue = regexPartialMatch.get(2).trim() + regexPartialMatch.get(4).trim();
		final String key = regexPartialMatch.get(1);
		return getLinkType(queue, key).getInv();
	}

	private LinkType getLinkType(String queue, String key) {
		if (key != null) {
			key = key.trim();
		}
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkType(Map<String, RegexPartialMatch> arg) {
		if (arg.get("LEFT_TO_RIGHT").get(0) != null) {
			return getLinkTypeNormal(arg.get("LEFT_TO_RIGHT"));
		}
		if (arg.get("RIGHT_TO_LEFT").get(0) != null) {
			return getLinkTypeInv(arg.get("RIGHT_TO_LEFT"));
		}
		if (arg.get("NAV_AGREG_OR_COMPO_INV").get(0) != null) {
			final String type = arg.get("NAV_AGREG_OR_COMPO_INV").get(4).trim();
			final String queue = arg.get("NAV_AGREG_OR_COMPO_INV").get(1).trim()
					+ arg.get("NAV_AGREG_OR_COMPO_INV").get(3).trim();
			LinkType result;
			if (type.equals("*")) {
				result = new LinkType(LinkDecor.COMPOSITION, LinkDecor.ARROW);
			} else if (type.equals("o")) {
				result = new LinkType(LinkDecor.AGREGATION, LinkDecor.ARROW);
			} else {
				throw new IllegalArgumentException();
			}
			if (queue.startsWith(".")) {
				result = result.getDashed();
			}
			return result;
		}
		if (arg.get("NAV_AGREG_OR_COMPO").get(0) != null) {
			final String type = arg.get("NAV_AGREG_OR_COMPO").get(1).trim();
			final String queue = arg.get("NAV_AGREG_OR_COMPO").get(2).trim()
					+ arg.get("NAV_AGREG_OR_COMPO").get(4).trim();
			LinkType result;
			if (type.equals("*")) {
				result = new LinkType(LinkDecor.ARROW, LinkDecor.COMPOSITION);
			} else if (type.equals("o")) {
				result = new LinkType(LinkDecor.ARROW, LinkDecor.AGREGATION);
			} else {
				throw new IllegalArgumentException();
			}
			if (queue.startsWith(".")) {
				result = result.getDashed();
			}
			return result;
		}
		throw new IllegalArgumentException();
	}

	private String getQueue(Map<String, RegexPartialMatch> arg) {
		if (arg.get("LEFT_TO_RIGHT").get(1) != null) {
			return arg.get("LEFT_TO_RIGHT").get(1).trim() + arg.get("LEFT_TO_RIGHT").get(3).trim();
		}
		if (arg.get("RIGHT_TO_LEFT").get(2) != null) {
			return arg.get("RIGHT_TO_LEFT").get(2).trim() + arg.get("RIGHT_TO_LEFT").get(4).trim();
		}
		if (arg.get("NAV_AGREG_OR_COMPO_INV").get(1) != null) {
			return arg.get("NAV_AGREG_OR_COMPO_INV").get(1).trim() + arg.get("NAV_AGREG_OR_COMPO_INV").get(3).trim();
		}
		if (arg.get("NAV_AGREG_OR_COMPO").get(2) != null) {
			return arg.get("NAV_AGREG_OR_COMPO").get(2).trim() + arg.get("NAV_AGREG_OR_COMPO").get(4).trim();
		}
		throw new IllegalArgumentException();
	}

	private Direction getDirection(Map<String, RegexPartialMatch> arg) {
		if (arg.get("LEFT_TO_RIGHT").get(2) != null) {
			return StringUtils.getQueueDirection(arg.get("LEFT_TO_RIGHT").get(2));
		}
		if (arg.get("RIGHT_TO_LEFT").get(3) != null) {
			return StringUtils.getQueueDirection(arg.get("RIGHT_TO_LEFT").get(3)).getInv();
		}
		if (arg.get("NAV_AGREG_OR_COMPO").get(3) != null) {
			return StringUtils.getQueueDirection(arg.get("NAV_AGREG_OR_COMPO").get(3)).getInv();
		}
		if (arg.get("NAV_AGREG_OR_COMPO_INV").get(2) != null) {
			return StringUtils.getQueueDirection(arg.get("NAV_AGREG_OR_COMPO_INV").get(2));
		}
		return null;
	}

	private LinkType getLinkTypeFromKey(String k) {
		if (k == null) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		if (k.equals("+")) {
			return new LinkType(LinkDecor.PLUS, LinkDecor.NONE);
		}
		if (k.equals("*")) {
			return new LinkType(LinkDecor.COMPOSITION, LinkDecor.NONE);
		}
		if (k.equalsIgnoreCase("o")) {
			return new LinkType(LinkDecor.AGREGATION, LinkDecor.NONE);
		}
		if (k.equals("<") || k.equals(">")) {
			return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		}
		if (k.equals("<|") || k.equals("|>")) {
			return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
		}
		// return null;
		throw new IllegalArgumentException(k);
	}

}
