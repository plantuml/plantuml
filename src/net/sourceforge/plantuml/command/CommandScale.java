/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.ScaleSimple;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandScale extends SingleLineCommand<UmlDiagram> {

	public CommandScale() {
		super("(?i)^scale\\s+([0-9.]+)(?:\\s*/\\s*([0-9.]+))?$");
	}

	@Override
	protected CommandExecutionResult executeArg(UmlDiagram diagram, List<String> arg) {
		double scale = Double.parseDouble(arg.get(0));
		if (arg.get(1) != null) {
			scale /= Double.parseDouble(arg.get(1));
		}
		diagram.setScale(new ScaleSimple(scale));
		return CommandExecutionResult.ok();
	}

}
