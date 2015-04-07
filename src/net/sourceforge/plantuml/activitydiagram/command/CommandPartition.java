/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 14661 $
 *
 */
package net.sourceforge.plantuml.activitydiagram.command;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram.ActivityDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.graphic.HtmlColorSet;
import net.sourceforge.plantuml.StringUtils;

public class CommandPartition extends SingleLineCommand<ActivityDiagram> {

	public CommandPartition() {
		super("(?i)^partition[%s]+([%g][^%g]+[%g]|\\S+)[%s]*(#[0-9a-fA-F]{6}|#?\\w+)?[%s]*\\{?$");
	}

	@Override
	protected CommandExecutionResult executeArg(ActivityDiagram diagram, List<String> arg) {
		final Code code = Code.of(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0)));
		final IGroup currentPackage = diagram.getCurrentGroup();
		final IEntity p = diagram.getOrCreateGroup(code, Display.getWithNewlines(code), GroupType.PACKAGE,
				currentPackage);
		final String color = arg.get(1);
		if (color != null) {
			p.setSpecificBackcolor(diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
