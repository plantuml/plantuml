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
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.ScaleHeight;
import net.sourceforge.plantuml.ScaleWidth;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandScaleWidthOrHeight extends SingleLineCommand<UmlDiagram> {

	public CommandScaleWidthOrHeight(UmlDiagram diagram) {
		super(diagram, "(?i)^scale\\s+([0-9.]+)\\s+(width|height)$");
	}

	@Override
	protected CommandExecutionResult executeArg(List<String> arg) {
		final double size = Double.parseDouble(arg.get(0));
		final boolean width = "width".equalsIgnoreCase(arg.get(1));
		if (width) {
			getSystem().setScale(new ScaleWidth(size));
		} else {
			getSystem().setScale(new ScaleHeight(size));
		}
		return CommandExecutionResult.ok();
	}

}
