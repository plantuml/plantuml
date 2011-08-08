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
 * Revision $Revision: 6551 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandGrouping extends SingleLineCommand<SequenceDiagram> {

	public CommandGrouping(SequenceDiagram sequenceDiagram) {
		super(
				sequenceDiagram,
				"(?i)^(opt|alt|loop|par|break|critical|else|end|group)((?<!else)(?<!end)#\\w+)?(?:\\s+(#\\w+))?(?:\\s+(.*?))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String type = arg.get(0).toLowerCase();
		final HtmlColor backColorElement = HtmlColor.getColorIfValid(arg.get(1));
		final HtmlColor backColorGeneral = HtmlColor.getColorIfValid(arg.get(2));
		String comment = arg.get(3);
		if ("group".equals(type) && StringUtils.isEmpty(comment)) {
			comment = "group";
		}
		final boolean result = getSystem().grouping(type, comment,
				GroupingType.getType(type), backColorGeneral, backColorElement);
		if (result == false) {
			return CommandExecutionResult.error("Cannot create group");
		}
		return CommandExecutionResult.ok();
	}
}
