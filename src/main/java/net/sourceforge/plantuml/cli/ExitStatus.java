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
package net.sourceforge.plantuml.cli;

public class ExitStatus {

	public static final int OK = 0;
	public static final int ERROR_NO_FILE_FOUND = 50;
	public static final int ERROR_NO_DIAGRAM_FOUND = 100;
	public static final int ERROR_SOME_DIAGRAMS_HAVE_ERROR = 200;

	private static String formatExitCode(int code, String description) {
		return String.format("  %-4d %s", code, description);
	}

	public static void printExitCodes() {
		final String[] all = {
			formatExitCode(OK, "Success"),
			formatExitCode(ERROR_NO_FILE_FOUND, "No file found"),
			formatExitCode(ERROR_NO_DIAGRAM_FOUND, "No diagram found in file(s)"),
			formatExitCode(ERROR_SOME_DIAGRAMS_HAVE_ERROR, "Some diagrams have syntax errors")
		};
		for (String line : all)
			System.out.println(line);
	}

	private volatile boolean hasErrors;
	private volatile boolean hasSuccess;
	private volatile boolean hasBlocks;

	private ExitStatus() {
	}

	public static ExitStatus init() {
		return new ExitStatus();
	}

	public void goesHasBlocks() {
		hasBlocks = true;
	}

	public void goesHasErrors() {
		hasErrors = true;
	}

	public void goesHasSuccess() {
		hasSuccess = true;
	}

	public boolean hasErrors() {
		return hasErrors;
	}

	public boolean noDiagramFound() {
		return hasBlocks == false;
	}

	private int getExitCode() {
		if (hasErrors)
			return ERROR_SOME_DIAGRAMS_HAVE_ERROR;

		if (hasBlocks == false)
			return ERROR_NO_FILE_FOUND;

		if (hasSuccess == false)
			return ERROR_NO_DIAGRAM_FOUND;

		return OK;
	}

	public void eventuallyExit() {
		final int code = getExitCode();
		if (code != ExitStatus.OK)
			Exit.exit(code);
		
	}

}
