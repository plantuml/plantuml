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
 * Revision $Revision: 3828 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.AbstractPSystem;

public class CommandExecutionResult {

	private final String error;
	private final AbstractPSystem newDiagram;

	private CommandExecutionResult(String error, AbstractPSystem newDiagram) {
		this.error = error;
		this.newDiagram = newDiagram;
	}

	@Override
	public String toString() {
		return super.toString() + " " + error;
	}

	public static CommandExecutionResult newDiagram(AbstractPSystem result) {
		return new CommandExecutionResult(null, result);
	}

	public static CommandExecutionResult ok() {
		return new CommandExecutionResult(null, null);
	}

	public static CommandExecutionResult error(String error) {
		return new CommandExecutionResult(error, null);
	}

	public boolean isOk() {
		return error == null;
	}

	public String getError() {
		if (isOk()) {
			throw new IllegalStateException();
		}
		return error;
	}

	public AbstractPSystem getNewDiagram() {
		return newDiagram;
	}

}
