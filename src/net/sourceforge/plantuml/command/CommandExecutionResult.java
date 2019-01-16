/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;

public class CommandExecutionResult {

	private final String error;
	private final AbstractPSystem newDiagram;
	private final List<String> debugLines;

	private CommandExecutionResult(String error, AbstractPSystem newDiagram, List<String> debugLines) {
		this.error = error;
		this.newDiagram = newDiagram;
		this.debugLines = debugLines;
	}

	public CommandExecutionResult withDiagram(AbstractPSystem newDiagram) {
		return new CommandExecutionResult(error, newDiagram, null);
	}

	@Override
	public String toString() {
		return super.toString() + " " + error;
	}

	public static CommandExecutionResult newDiagram(AbstractPSystem result) {
		return new CommandExecutionResult(null, result, null);
	}

	public static CommandExecutionResult ok() {
		return new CommandExecutionResult(null, null, null);
	}

	public static CommandExecutionResult error(String error) {
		return new CommandExecutionResult(error, null, null);
	}

	public static CommandExecutionResult error(String error, Throwable t) {
		return new CommandExecutionResult(error, null, getStackTrace(t));
	}

	public static List<String> getStackTrace(Throwable exception) {
		final List<String> result = new ArrayList<String>();
		result.add(exception.toString());
		for (StackTraceElement ste : exception.getStackTrace()) {
			result.add("  " + ste.toString());
		}
		if (exception.getCause() != null) {
			final Throwable cause = exception.getCause();
			result.add("  ");
			result.add("Caused by " + cause.toString());
			for (StackTraceElement ste : cause.getStackTrace()) {
				result.add("  " + ste.toString());
			}

		}
		return result;
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

	public List<String> getDebugLines() {
		return debugLines;
	}

}
