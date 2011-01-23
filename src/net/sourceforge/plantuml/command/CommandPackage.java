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
 * Revision $Revision: 5983 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.graphic.HtmlColor;

public class CommandPackage extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandPackage(AbstractEntityDiagram diagram) {
		super(diagram,
				"(?i)^package\\s+(\"[^\"]+\"|\\S+)(?:\\s+as\\s+([\\p{L}0-9_.]+))?\\s*(#[0-9a-fA-F]{6}|#?\\w+)?\\s*\\{?$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final String code;
		final String display;
		if (arg.get(1) == null) {
			code = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0));
			display = code;
		} else {
			display = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(0));
			code = arg.get(1);
		}
		final Group currentPackage = getSystem().getCurrentGroup();
//		if (getSystem().entityExist(code)) {
//			return CommandExecutionResult.error("Package cannot have the same name as an existing class");
//		}
		final Group p = getSystem().getOrCreateGroup(code, display, null, GroupType.PACKAGE, currentPackage);
		p.setBold(true);
		final String color = arg.get(2);
		if (color != null) {
			p.setBackColor(HtmlColor.getColorIfValid(color));
		}
		return CommandExecutionResult.ok();
	}

}
