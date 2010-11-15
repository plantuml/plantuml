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
 * Revision $Revision: 5024 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandLinkActivity2 extends SingleLineCommand<ActivityDiagram> {

	public CommandLinkActivity2(ActivityDiagram diagram) {
		super(
				diagram,
				"(?i)^(?:(\\(\\*\\))|([\\p{L}0-9_.]+)|(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)|\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?)?"
						+ "\\s*([=-]+(?:left|right|up|down|le?|ri?|up?|do?)?[=-]*\\>)\\s*(?:\\[([^\\]*]+[^\\]]*)\\])?\\s*"
						+ "(?:(\\(\\*\\)|\\{)|([\\p{L}0-9_.]+)|(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)|\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?)$");
	}

	@Override
	protected void actionIfCommandValid() {
		getSystem().setAcceptOldSyntaxForBranch(false);
	}

	static IEntity getEntity(ActivityDiagram system, List<String> arg, final boolean start) {
		if ("{".equals(arg.get(0))) {
			return system.createInnerActivity();
		}
		if ("(*)".equals(arg.get(0))) {
			if (start) {
				return system.getStart();
			}
			return system.getEnd();
		}
		if (arg.get(1) != null) {
			return system.getOrCreate(arg.get(1), arg.get(1), EntityType.ACTIVITY);
		}
		if (arg.get(2) != null) {
			return system.getOrCreate(arg.get(2), arg.get(2), EntityType.SYNCHRO_BAR);
		}
		if (arg.get(3) != null) {
			final String code = arg.get(4) == null ? arg.get(3) : arg.get(4);
			return system.getOrCreate(code, arg.get(3), EntityType.ACTIVITY);
		}
		if (start && arg.get(0) == null && arg.get(1) == null && arg.get(2) == null && arg.get(3) == null
				&& arg.get(4) == null) {
			return system.getLastEntityConsulted();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final IEntity entity1 = getEntity(getSystem(), arg, true);
		final IEntity entity2 = getEntity(getSystem(), arg.subList(7, 12), false);
		final String linkLabel = arg.get(6);

		final String arrow = StringUtils.manageArrowForCuca(arg.get(5));
		final int lenght = arrow.length() - 1;

		Link link = new Link(entity1, entity2, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), linkLabel, lenght);
		final Direction direction = StringUtils.getArrowDirection(arg.get(5));
		if (direction == Direction.LEFT || direction == Direction.UP) {
			link = link.getInv();
		}

		getSystem().addLink(link);

		return CommandExecutionResult.ok();

	}

	static EntityType getTypeFromString(String type, final EntityType circle) {
		if (type == null) {
			return EntityType.ACTIVITY;
		}
		if (type.equals("*")) {
			return circle;
		}
		if (type.startsWith("=")) {
			return EntityType.SYNCHRO_BAR;
		}
		throw new IllegalArgumentException();
	}

}
