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
 */
package net.sourceforge.plantuml.usecasediagram.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;
import net.sourceforge.plantuml.usecasediagram.UsecaseDiagram;

public class CommandLinkUsecase extends SingleLineCommand<UsecaseDiagram> {

	private static final int FIRST_UC_AC_OR_GROUP = 0;
	private static final int LEFT_TO_RIGHT = 1;
	private static final int LEFT_TO_RIGHT_QUEUE = 2;
	private static final int LEFT_TO_RIGHT_HEAD = 3;
	private static final int RIGHT_TO_LEFT = 4;
	private static final int RIGHT_TO_LEFT_HEAD = 5;
	private static final int RIGHT_TO_LEFT_QUEUE = 6;
	private static final int SECOND_UC_AC_OR_GROUP = 7;
	private static final int LINK_LABEL = 8;

	public CommandLinkUsecase(UsecaseDiagram diagram) {
		super(
				diagram,
				"(?i)^([\\p{L}0-9_.]+|:[^:]+:|\\((?!\\*\\))[^)]+\\))\\s*"
						+ "(?:("
						+ "([=-]+(?:left|right|up|down|le?|ri?|up?|do?)?[=-]*|\\.+(?:left|right|up|down|le?|ri?|up?|do?)?\\.*)([\\]>]|\\|[>\\]])?"
						+ ")|("
						+ "([\\[<]|[<\\[]\\|)?([=-]*(?:left|right|up|down|le?|ri?|up?|do?)?[=-]+|\\.*(?:left|right|up|down|le?|ri?|up?|do?)?\\.+)"
						+ "))" + "\\s*([\\p{L}0-9_.]+|:[^:]+:|\\((?!\\*\\))[^)]+\\))\\s*(?::\\s*([^\"]+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		if (getSystem().isGroup(arg.get(FIRST_UC_AC_OR_GROUP)) && getSystem().isGroup(arg.get(SECOND_UC_AC_OR_GROUP))) {
			return executePackageLink(arg);
		}
		if (getSystem().isGroup(arg.get(FIRST_UC_AC_OR_GROUP)) || getSystem().isGroup(arg.get(SECOND_UC_AC_OR_GROUP))) {
			return CommandExecutionResult.error("Package can be only linked to other package");
		}

		final IEntity cl1 = getSystem().getOrCreateClass(arg.get(FIRST_UC_AC_OR_GROUP));
		final IEntity cl2 = getSystem().getOrCreateClass(arg.get(SECOND_UC_AC_OR_GROUP));

		final LinkType linkType = arg.get(LEFT_TO_RIGHT) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = StringUtils.manageQueueForCuca(arg.get(LEFT_TO_RIGHT) != null ? arg
				.get(LEFT_TO_RIGHT_QUEUE) : arg.get(RIGHT_TO_LEFT_QUEUE));

		Link link = new Link(cl1, cl2, linkType, arg.get(LINK_LABEL), queue.length());
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
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executePackageLink(List<String> arg) {
		final Group cl1 = getSystem().getGroup(arg.get(FIRST_UC_AC_OR_GROUP));
		final Group cl2 = getSystem().getGroup(arg.get(SECOND_UC_AC_OR_GROUP));

		final LinkType linkType = arg.get(LEFT_TO_RIGHT) != null ? getLinkTypeNormal(arg) : getLinkTypeInv(arg);
		final String queue = arg.get(LEFT_TO_RIGHT) != null ? arg.get(LEFT_TO_RIGHT_QUEUE) : arg
				.get(RIGHT_TO_LEFT_QUEUE);

		final Link link = new Link(cl1.getEntityCluster(), cl2.getEntityCluster(), linkType, arg.get(LINK_LABEL), queue
				.length());
		getSystem().addLink(link);
		return CommandExecutionResult.ok();
	}

	private LinkType getLinkTypeNormal(List<String> arg) {
		final String queue = arg.get(LEFT_TO_RIGHT_QUEUE);
		final String key = arg.get(LEFT_TO_RIGHT_HEAD);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType;
	}

	private LinkType getLinkTypeInv(List<String> arg) {
		final String queue = arg.get(RIGHT_TO_LEFT_QUEUE);
		final String key = arg.get(RIGHT_TO_LEFT_HEAD);
		LinkType linkType = getLinkTypeFromKey(key);

		if (queue.startsWith(".")) {
			linkType = linkType.getDashed();
		}
		return linkType.getInv();
	}

	private LinkType getLinkTypeFromKey(String k) {
		if (k == null) {
			return new LinkType(LinkDecor.NONE, LinkDecor.NONE);
		}
		if (k.equals("<") || k.equals(">")) {
			return new LinkType(LinkDecor.ARROW, LinkDecor.NONE);
		}
		if (k.equals("<|") || k.equals("|>")) {
			return new LinkType(LinkDecor.EXTENDS, LinkDecor.NONE);
		}
		return null;
	}

}
