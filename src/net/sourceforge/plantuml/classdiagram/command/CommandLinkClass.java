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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;

final public class CommandLinkClass extends SingleLineCommand<AbstractClassOrObjectDiagram> {

	private static final int OFFSET = 1;

	private static final int FIRST_TYPE_AND_CLASS = 0 + OFFSET;
	private static final int FIRST_TYPE = 1 + OFFSET;
	private static final int FIRST_CLASS = 2 + OFFSET;
	private static final int FIRST_LABEL = 3 + OFFSET;
	private static final int LEFT_TO_RIGHT = 4 + OFFSET;
	private static final int LEFT_TO_RIGHT_QUEUE = 5 + OFFSET;
	private static final int LEFT_TO_RIGHT_HEAD = 6 + OFFSET;
	private static final int RIGHT_TO_LEFT = 7 + OFFSET;
	private static final int RIGHT_TO_LEFT_HEAD = 8 + OFFSET;
	private static final int RIGHT_TO_LEFT_QUEUE = 9 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO_INV = 10 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO_INV_QUEUE = 11 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO_INV_HEAD = 12 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO = 13 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO_HEAD = 14 + OFFSET;
	private static final int NAV_AGREG_OR_COMPO_QUEUE = 15 + OFFSET;
	private static final int SECOND_LABEL = 16 + OFFSET;
	private static final int SECOND_TYPE_AND_CLASS = 17 + OFFSET;
	private static final int SECOND_TYPE = 18 + OFFSET;
	private static final int SECOND_CLASS = 19 + OFFSET;
	private static final int LINK_LABEL = 20 + OFFSET;

	private final Pattern patternAssociationPoint = Pattern
			.compile("\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)");

	// [\\p{L}0-9_.]+
	// \\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*

	public CommandLinkClass(AbstractClassOrObjectDiagram diagram) {
		super(
				diagram,
				"(?i)^(?:@(\\d+)\\s+)?((?:"
						+ optionalKeywords(diagram.getUmlDiagramType())
						+ "\\s+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\)"
						+ ")\\s*(?:\"([^\"]+)\")?\\s*"
						// split here
						+ "(?:"
						+ "(([-=.]+(?:left|right|up|down|le?|ri?|up?|do?)?[-=.]*)(o +|[\\]>*+]|\\|[>\\]])?)"
						+ "|"
						+ "(( +o|[\\[<*+]|[<\\[]\\|)?([-=.]*(?:left|right|up|down|le?|ri?|up?|do?)?[-=.]+))"
						+ "|"
						+ "(\\<([-=.]*(?:left|right|up|down|le?|ri?|up?|do?[-=.]+)?[-=.]+)(o +|\\*))"
						+ "|"
						+ "(( +o|\\*)([-=.]+(?:left|right|up|down|le?|ri?|up?|do?)?[-=.]*)\\>)"
						+ ")"
						// split here
						+ "\\s*(?:\"([^\"]+)\")?\\s*((?:"
						+ optionalKeywords(diagram.getUmlDiagramType())
						+ "\\s+)?"
						+ "(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\)"
						+ ")\\s*(?::\\s*([^\"]+))?$");
		// "(?i)^(?:@(\\d+)\\s+)?((?:(interface|enum|abstract\\s+class|abstract|class)\\s+)?(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\))\\s*(?:\"([^\"]+)\")?\\s*"
		// +
		// "(?:(([-=.]+)([\\]>o*+]|\\|[>\\]])?)|(([\\[<o*+]|[<\\[]\\|)?([-=.]+))|(\\<([-=.]+)([o*]))|(([o*])([-=.]+)\\>))"
		// +
		// "\\s*(?:\"([^\"]+)\")?\\s*((?:(interface|enum|abstract\\s+class|abstract|class)\\s+)?(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\))\\s*(?::\\s*([^\"]+))?$");
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
	protected CommandExecutionResult executeArg(List<String> arg) {
		if (arg.get(FIRST_TYPE_AND_CLASS).startsWith("(")) {
			return executeArgSpecial1(arg);
		}
		if (arg.get(SECOND_TYPE_AND_CLASS).startsWith("(")) {
			return executeArgSpecial2(arg);
		}
		if (getSystem().isGroup(arg.get(FIRST_CLASS)) && getSystem().isGroup(arg.get(SECOND_CLASS))) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(arg.get(FIRST_CLASS)) || getSystem().isGroup(arg.get(SECOND_CLASS))) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final Entity cl1 = (Entity) getSystem().getOrCreateClass(arg.get(FIRST_CLASS));
		final Entity cl2 = (Entity) getSystem().getOrCreateClass(arg.get(SECOND_CLASS));

		if (arg.get(FIRST_TYPE) != null) {
			final EntityType type = EntityType.getEntityType(arg.get(FIRST_TYPE));
			if (type != EntityType.OBJECT) {
				cl1.muteToType(type);
			}
		}
		if (arg.get(SECOND_TYPE) != null) {
			final EntityType type = EntityType.getEntityType(arg.get(SECOND_TYPE));
			if (type != EntityType.OBJECT) {
				cl2.muteToType(type);
			}
		}

		final LinkType linkType = getLinkType(arg);
		final String queueRaw = getQueue(arg);
		final String queue = StringUtils.manageQueueForCuca(queueRaw);

		Link link = new Link(cl1, cl2, linkType, arg.get(LINK_LABEL), queue.length(), arg.get(FIRST_LABEL), arg
				.get(SECOND_LABEL), getSystem().getLabeldistance(), getSystem().getLabelangle());

		if (queueRaw.matches(".*\\w.*")) {
			if (arg.get(LEFT_TO_RIGHT) != null) {
				Direction direction = StringUtils.getQueueDirection(arg.get(LEFT_TO_RIGHT_QUEUE));
				if (linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
					direction = direction.getInv();
				}
				if (direction == Direction.LEFT || direction == Direction.UP) {
					link = link.getInv();
				}
			}
			if (arg.get(RIGHT_TO_LEFT) != null) {
				Direction direction = StringUtils.getQueueDirection(arg.get(RIGHT_TO_LEFT_QUEUE));
				if (linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
					direction = direction.getInv();
				}
				if (direction == Direction.RIGHT || direction == Direction.DOWN) {
					link = link.getInv();
				}
			}
			if (arg.get(NAV_AGREG_OR_COMPO) != null) {
				Direction direction = StringUtils.getQueueDirection(arg.get(NAV_AGREG_OR_COMPO_QUEUE));
				if (linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
					direction = direction.getInv();
				}
				if (direction == Direction.RIGHT || direction == Direction.DOWN) {
					link = link.getInv();
				}
			}
			if (arg.get(NAV_AGREG_OR_COMPO_INV) != null) {
				Direction direction = StringUtils.getQueueDirection(arg.get(NAV_AGREG_OR_COMPO_INV_QUEUE));
				if (linkType.isExtendsOrAgregationOrCompositionOrPlus()) {
					direction = direction.getInv();
				}
				if (direction == Direction.LEFT || direction == Direction.UP) {
					link = link.getInv();
				}
			}
		}

		getSystem().resetPragmaLabel();
		addLink(link, arg.get(0));

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

	private CommandExecutionResult executePackageLink(List<String> arg) {
		final Group cl1 = getSystem().getGroup(arg.get(FIRST_CLASS));
		final Group cl2 = getSystem().getGroup(arg.get(SECOND_CLASS));

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		final Link link = new Link(cl1.getEntityCluster(), cl2.getEntityCluster(), linkType, arg.get(LINK_LABEL), queue
				.length(), arg.get(FIRST_LABEL), arg.get(SECOND_LABEL), getSystem().getLabeldistance(), getSystem()
				.getLabelangle());
		getSystem().resetPragmaLabel();
		addLink(link, arg.get(0));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial1(List<String> arg) {
		final Matcher m = patternAssociationPoint.matcher(arg.get(FIRST_TYPE_AND_CLASS));
		if (m.matches() == false) {
			throw new IllegalStateException();
		}
		final String clName1 = m.group(1);
		final String clName2 = m.group(2);
		if (getSystem().entityExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().entityExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}
		final IEntity entity1 = getSystem().getOrCreateClass(clName1);
		final IEntity entity2 = getSystem().getOrCreateClass(clName2);

		final Entity node = getSystem().createEntity(arg.get(FIRST_TYPE_AND_CLASS), "node",
				EntityType.POINT_FOR_ASSOCIATION);

		getSystem().insertBetween(entity1, entity2, node);
		final IEntity cl2 = getSystem().getOrCreateClass(arg.get(SECOND_CLASS));

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		final Link link = new Link(node, cl2, linkType, arg.get(LINK_LABEL), queue.length());
		addLink(link, arg.get(0));

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(List<String> arg) {
		final Matcher m = patternAssociationPoint.matcher(arg.get(SECOND_TYPE_AND_CLASS));
		if (m.matches() == false) {
			throw new IllegalStateException();
		}
		final String clName1 = m.group(1);
		final String clName2 = m.group(2);
		if (getSystem().entityExist(clName1) == false) {
			return CommandExecutionResult.error("No class " + clName1);
		}
		if (getSystem().entityExist(clName2) == false) {
			return CommandExecutionResult.error("No class " + clName2);
		}
		final IEntity entity1 = getSystem().getOrCreateClass(clName1);
		final IEntity entity2 = getSystem().getOrCreateClass(clName2);

		final IEntity node = getSystem().createEntity(arg.get(SECOND_TYPE_AND_CLASS), "node",
				EntityType.POINT_FOR_ASSOCIATION);

		getSystem().insertBetween(entity1, entity2, node);
		final IEntity cl1 = getSystem().getOrCreateClass(arg.get(FIRST_TYPE_AND_CLASS));

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		final Link link = new Link(cl1, node, linkType, arg.get(LINK_LABEL), queue.length());
		addLink(link, arg.get(0));

		return CommandExecutionResult.ok();
	}

	private LinkType getLinkTypeNormal(List<String> arg) {
		final String queue = arg.get(LEFT_TO_RIGHT_QUEUE).trim();
		String key = arg.get(LEFT_TO_RIGHT_HEAD);
		if (key != null) {
			key = key.trim();
		}
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeInv(List<String> arg) {
		final String queue = arg.get(RIGHT_TO_LEFT_QUEUE).trim();
		String key = arg.get(RIGHT_TO_LEFT_HEAD);
		if (key != null) {
			key = key.trim();
		}
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType.getInv();
	}

	private LinkType getLinkType(List<String> arg) {
		if (arg.get(LEFT_TO_RIGHT) != null) {
			return getLinkTypeNormal(arg);
		}
		if (arg.get(RIGHT_TO_LEFT) != null) {
			return getLinkTypeInv(arg);
		}
		if (arg.get(NAV_AGREG_OR_COMPO_INV) != null) {
			final String type = arg.get(NAV_AGREG_OR_COMPO_INV_HEAD).trim();
			final String queue = arg.get(NAV_AGREG_OR_COMPO_INV_QUEUE).trim();
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
		if (arg.get(NAV_AGREG_OR_COMPO) != null) {
			final String type = arg.get(NAV_AGREG_OR_COMPO_HEAD).trim();
			final String queue = arg.get(NAV_AGREG_OR_COMPO_QUEUE).trim();
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

	private String getQueue(List<String> arg) {
		if (arg.get(LEFT_TO_RIGHT) != null) {
			return arg.get(LEFT_TO_RIGHT_QUEUE).trim();
		}
		if (arg.get(RIGHT_TO_LEFT) != null) {
			return arg.get(RIGHT_TO_LEFT_QUEUE).trim();
		}
		if (arg.get(NAV_AGREG_OR_COMPO_INV) != null) {
			return arg.get(NAV_AGREG_OR_COMPO_INV_QUEUE).trim();
		}
		if (arg.get(NAV_AGREG_OR_COMPO) != null) {
			return arg.get(NAV_AGREG_OR_COMPO_QUEUE).trim();
		}
		throw new IllegalArgumentException();
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
