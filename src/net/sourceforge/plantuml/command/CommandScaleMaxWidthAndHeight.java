/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ScaleMaxWidthAndHeight;

public class CommandScaleMaxWidthAndHeight extends SingleLineCommand<AbstractPSystem> {

	public CommandScaleMaxWidthAndHeight() {
		super("(?i)^scale[%s]+max[%s]+([0-9.]+)[%s]*[*x][%s]*([0-9.]+)$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractPSystem diagram, List<String> arg) {
		final double width = Double.parseDouble(arg.get(0));
		final double height = Double.parseDouble(arg.get(1));
		diagram.setScale(new ScaleMaxWidthAndHeight(width, height));
		return CommandExecutionResult.ok();
	}

}
