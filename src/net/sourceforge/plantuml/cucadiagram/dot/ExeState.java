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
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;

public enum ExeState {

	NULL_UNDEFINED, OK, DOES_NOT_EXIST, IS_A_DIRECTORY, NOT_A_FILE, CANNOT_BE_READ;

	public static ExeState checkFile(File dotExe) {
		if (dotExe == null) {
			return NULL_UNDEFINED;
		} else if (dotExe.exists() == false) {
			return DOES_NOT_EXIST;
		} else if (dotExe.isDirectory()) {
			return IS_A_DIRECTORY;
		} else if (dotExe.isFile() == false) {
			return NOT_A_FILE;
		} else if (dotExe.canRead() == false) {
			return CANNOT_BE_READ;
		}
		return OK;
	}

	public String getTextMessage() {
		switch (this) {
		case OK:
			return "File OK";
		case NULL_UNDEFINED:
			return "No dot executable found";
		case DOES_NOT_EXIST:
			return "File does not exist";
		case IS_A_DIRECTORY:
			return "It should be an executable, not a directory";
		case NOT_A_FILE:
			return "Not a valid file";
		case CANNOT_BE_READ:
			return "File cannot be read";
		}
		throw new IllegalStateException();
	}

	public String getTextMessage(File exe) {
		switch (this) {
		case OK:
			return "File " + exe.getAbsolutePath() + " OK";
		case NULL_UNDEFINED:
			return NULL_UNDEFINED.getTextMessage();
		case DOES_NOT_EXIST:
			return "File " + exe.getAbsolutePath() + " does not exist";
		case IS_A_DIRECTORY:
			return "File " + exe.getAbsolutePath() + " should be an executable, not a directory";
		case NOT_A_FILE:
			return "File " + exe.getAbsolutePath() + " is not a valid file";
		case CANNOT_BE_READ:
			return "File " + exe.getAbsolutePath() + " cannot be read";
		}
		throw new IllegalStateException();
	}

}
