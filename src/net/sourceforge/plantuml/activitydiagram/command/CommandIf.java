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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandIf extends SingleLineCommand<ActivityDiagram> {

	public CommandIf(ActivityDiagram diagram) {
		super(
				diagram,
				"(?i)^(?:(\\(\\*\\))|([\\p{L}0-9_.]+)|(?:==+)\\s*([\\p{L}0-9_.]+)\\s*(?:==+)|\"([^\"]+)\"(?:\\s+as\\s+([\\p{L}0-9_.]+))?)?"
						+ "\\s*([=-]+(?:left|right|up|down|le?|ri?|up?|do?)?[=-]*\\>)?\\s*(?:\\[([^\\]*]+[^\\]]*)\\])?\\s*if\\s*\"([^\"]*)\"\\s*then$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final IEntity entity1 = CommandLinkActivity2.getEntity(getSystem(), arg, true);

		getSystem().startIf();

		int lenght = 2;

		if (arg.get(5) != null) {
			final String arrow = StringUtils.manageArrowForCuca(arg.get(5));
			lenght = arrow.length() - 1;
		}

		final IEntity branch = getSystem().getCurrentContext().getBranch();


		Link link = new Link(entity1, branch, new LinkType(LinkDecor.ARROW, LinkDecor.NONE), arg.get(6), lenght, null,
				arg.get(7), getSystem().getLabeldistance(), getSystem().getLabelangle());
		if (arg.get(5) != null) {
			final Direction direction = StringUtils.getArrowDirection(arg.get(5));
			if (direction == Direction.LEFT || direction == Direction.UP) {
				link = link.getInv();
			}
		}

		getSystem().addLink(link);

		getSystem().setAcceptOldSyntaxForBranch(false);
		return CommandExecutionResult.ok();
	}

}
