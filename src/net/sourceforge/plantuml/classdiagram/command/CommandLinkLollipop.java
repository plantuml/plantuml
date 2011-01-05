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
 * Revision $Revision: 5075 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.UniqueSequence;
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

final public class CommandLinkLollipop extends SingleLineCommand<AbstractClassOrObjectDiagram> {

	private static final int OFFSET = 1;

	private static final int FIRST_TYPE_AND_CLASS = 0 + OFFSET;
	private static final int FIRST_TYPE = 1 + OFFSET;
	private static final int FIRST_CLASS = 2 + OFFSET;
	private static final int FIRST_LABEL = 3 + OFFSET;

	private static final int LINK1 = 4 + OFFSET;
	private static final int LINK2 = 5 + OFFSET;

	private static final int SECOND_LABEL = 6 + OFFSET;
	private static final int SECOND_TYPE_AND_CLASS = 7 + OFFSET;
	private static final int SECOND_TYPE = 8 + OFFSET;
	private static final int SECOND_CLASS = 9 + OFFSET;
	private static final int LINK_LABEL = 10 + OFFSET;

	private final Pattern patternAssociationPoint = Pattern
			.compile("\\(\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*,\\s*(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)\\s*\\)");

	public CommandLinkLollipop(AbstractClassOrObjectDiagram diagram) {
		super(
				diagram,
				"(?i)^(?:@([\\d.])\\s+)?((?:(interface|enum|abstract\\s+class|abstract|class)\\s+)?(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\))\\s*(?:\"([^\"]+)\")?\\s*"
						// +
						// "(?:(([-=.]+)([\\]>o*+]|\\|[>\\]])?)|(([\\[<o*+]|[<\\[]\\|)?([-=.]+))|(\\<([-=.]+)([o*]))|(([o*])([-=.]+)\\>))"
						+ "(?:\\(\\)([-=.]+)|([-=.]+)\\(\\))"
						+ "\\s*(?:\"([^\"]+)\")?\\s*((?:(interface|enum|abstract\\s+class|abstract|class)\\s+)?(\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*)|\\(\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*,\\s*\\.?[\\p{L}0-9_]+(?:\\.[\\p{L}0-9_]+)*\\s*\\))\\s*(?::\\s*([^\"]+))?$");
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

		final Entity cl1;
		final Entity cl2;
		final Entity normalEntity;

		final String suffix = "lol" + UniqueSequence.getValue();
		if (arg.get(LINK1) != null) {
			cl2 = (Entity) getSystem().getOrCreateClass(arg.get(SECOND_CLASS));
			cl1 = getSystem().createEntity(cl2.getCode() + suffix, arg.get(FIRST_CLASS), EntityType.LOLLIPOP);
			normalEntity = cl2;
		} else {
			assert arg.get(LINK2) != null;
			cl1 = (Entity) getSystem().getOrCreateClass(arg.get(FIRST_CLASS));
			cl2 = getSystem().createEntity(cl1.getCode() + suffix, arg.get(SECOND_CLASS), EntityType.LOLLIPOP);
			normalEntity = cl1;
		}

		final LinkType linkType = getLinkType(arg);
		final String queue = getQueue(arg);

		int length = queue.length();
		if (length == 1 && getSystem().getNbOfHozizontalLollipop(normalEntity) > 1) {
			length++;
		}
		final Link link = new Link(cl1, cl2, linkType, arg.get(LINK_LABEL), length, arg.get(FIRST_LABEL), arg
				.get(SECOND_LABEL), getSystem().getLabeldistance(), getSystem().getLabelangle());
		getSystem().resetPragmaLabel();
		addLink(link, arg.get(0));

		return CommandExecutionResult.ok();
	}

	private String getQueue(List<String> arg) {
		if (arg.get(LINK1) != null) {
			return arg.get(LINK1);
		}
		if (arg.get(LINK2) != null) {
			return arg.get(LINK2);
		}
		throw new IllegalArgumentException();
	}

	private LinkType getLinkType(List<String> arg) {
		return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
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
			link.setWeight(Double.parseDouble(arg0));
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

}
