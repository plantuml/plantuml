/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import net.sourceforge.plantuml.core.AbstractDiagram;
import net.sourceforge.plantuml.text.StringLocated;

public class CommandExecutionResult {

	private final String error;
	private final AbstractDiagram newDiagram;
	private final List<String> debugLines;
	private final int score;
	private final Throwable rootCause;
	private final StringLocated errorLocation;

	private CommandExecutionResult(AbstractDiagram newDiagram, String error, int score, Throwable rootCause) {
		this(newDiagram, error, score, rootCause, null);
	}

	private CommandExecutionResult(AbstractDiagram newDiagram, String error, int score, Throwable rootCause,
			StringLocated errorLocation) {
		this.errorLocation = errorLocation;
		this.error = error;
		this.rootCause = rootCause;
		this.newDiagram = newDiagram;
		this.debugLines = rootCause == null ? null : getStackTrace(rootCause);
		this.score = score;

	}

	public CommandExecutionResult withDiagram(AbstractDiagram newDiagram) {
		return new CommandExecutionResult(newDiagram, error, 0, null);
	}

	@Override
	public String toString() {
		return super.toString() + " " + error;
	}

	public static CommandExecutionResult newDiagram(AbstractDiagram result) {
		return new CommandExecutionResult(result, null, 0, null);
	}

	public static CommandExecutionResult ok() {
		return new CommandExecutionResult(null, null, 0, null);
	}

	public static CommandExecutionResult badColor() {
		return new CommandExecutionResult(null, "No such color", 10, null);
	}

	public static CommandExecutionResult error(String error) {
		return new CommandExecutionResult(null, error, 0, null);
	}

	/**
	 * An error that belongs to one line of a multi-line command, as opposed to the
	 * command as a whole (which is reported at its first line).
	 */
	public static CommandExecutionResult errorAt(String error, StringLocated location) {
		return new CommandExecutionResult(null, error, 0, null, location);
	}

	/**
	 * The line the error belongs to, or null when it belongs to the command as a whole.
	 */
	public StringLocated getErrorLocation() {
		return errorLocation;
	}

	public static CommandExecutionResult error(String error, int score) {
		return new CommandExecutionResult(null, error, score, null);
	}

	public static CommandExecutionResult error(String error, Throwable t) {
		return new CommandExecutionResult(null, error, 0, t);
	}

	public static List<String> getStackTrace(Throwable exception) {
		final List<String> result = new ArrayList<>();
		result.add(exception.toString());
		for (StackTraceElement ste : exception.getStackTrace())
			result.add("  " + ste.toString());

		if (exception.getCause() != null) {
			final Throwable cause = exception.getCause();
			result.add("  ");
			result.add("Caused by " + cause.toString());
			for (StackTraceElement ste : cause.getStackTrace())
				result.add("  " + ste.toString());

		}
		return result;
	}

	public boolean isOk() {
		return error == null;
	}

	public String getError() {
		if (isOk())
			throw new IllegalStateException();

		return error;
	}

	public int getScore() {
		return score;
	}

	public AbstractDiagram getNewDiagram() {
		return newDiagram;
	}

	public List<String> getDebugLines() {
		return debugLines;
	}

	public Throwable getRootCause() {
		return rootCause;
	}

}
