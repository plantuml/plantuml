/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.ScaleMaxHeight;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandScaleMaxHeight extends SingleLineCommand<UmlDiagram> {

	public CommandScaleMaxHeight() {
		super("(?i)^scale[%s]+max[%s]+([0-9.]+)[%s]+height$");
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram diagram, List<String> arg) {
		final double height = Double.parseDouble(arg.get(0));
		diagram.setScale(new ScaleMaxHeight(height));
		return CommandExecutionResult.ok();
	}

}
