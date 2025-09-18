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

public class ErrorStatus {

	private int nbErrors;
	private int nbOk;

	private ErrorStatus() {
	}

	public static ErrorStatus init() {
		return new ErrorStatus();
	}

	public synchronized void incError() {
		nbErrors++;
	}

	public synchronized void incOk() {
		nbOk++;
	}

	public synchronized boolean hasError() {
		return nbErrors > 0;
	}

	public synchronized boolean isEmpty() {
		return nbErrors == 0 && nbOk == 0;
	}

	public synchronized int getExitCode() {
		if (isEmpty())
			return 100;

		if (hasError())
			return 200;

		return 0;
	}

	@Override
	public String toString() {
		return "nbErrors=" + nbErrors + " nbOk=" + nbOk;
	}

}
