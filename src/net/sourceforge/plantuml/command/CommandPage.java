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
 * Revision $Revision: 10778 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.List;

import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;

public class CommandPage extends SingleLineCommand<AbstractEntityDiagram> {

	public CommandPage() {
		super("(?i)^page\\s+(\\d+)\\s*x\\s*(\\d+)$");
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractEntityDiagram classDiagram, List<String> arg) {

		final int horizontal = Integer.parseInt(arg.get(0));
		final int vertical = Integer.parseInt(arg.get(1));
		if (horizontal <= 0 || vertical <= 0) {
			return CommandExecutionResult.error("Argument must be positive");
		}
		classDiagram.setHorizontalPages(horizontal);
		classDiagram.setVerticalPages(vertical);
		return CommandExecutionResult.ok();
	}

}
