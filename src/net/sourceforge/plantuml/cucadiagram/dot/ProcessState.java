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

import java.io.IOException;

public class ProcessState {

	private final String name;
	private final IOException cause;

	private ProcessState(String name, IOException cause) {
		this.name = name;
		this.cause = cause;
	}

	@Override
	public String toString() {
		if (cause == null) {
			return name;
		}
		return name + " " + cause.toString();
	}

	private final static ProcessState INIT = new ProcessState("INIT", null);
	private final static ProcessState RUNNING = new ProcessState("RUNNING", null);
	private final static ProcessState TERMINATED_OK = new ProcessState("TERMINATED_OK", null);
	private final static ProcessState TIMEOUT = new ProcessState("TIMEOUT", null);

	// INIT, RUNNING, TERMINATED_OK, TIMEOUT, IO_EXCEPTION1, IO_EXCEPTION2;

	public static ProcessState INIT() {
		return INIT;
	}

	public static ProcessState RUNNING() {
		return RUNNING;
	}

	public static ProcessState TERMINATED_OK() {
		return TERMINATED_OK;
	}

	public static ProcessState TIMEOUT() {
		return TIMEOUT;
	}

	public static ProcessState IO_EXCEPTION1(IOException e) {
		return new ProcessState("IO_EXCEPTION1", e);
	}

	public static ProcessState IO_EXCEPTION2(IOException e) {
		return new ProcessState("IO_EXCEPTION2", e);
	}

	public boolean differs(ProcessState other) {
		return name.equals(other.name) == false;
	}

	@Override
	public boolean equals(Object o) {
		final ProcessState other = (ProcessState) o;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public Throwable getCause() {
		return cause;
	}
}
