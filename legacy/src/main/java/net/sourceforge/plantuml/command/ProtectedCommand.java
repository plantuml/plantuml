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

import java.util.Objects;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.version.Version;

public class ProtectedCommand<S extends Diagram> implements Command<S> {

	private final Command<S> cmd;

	public ProtectedCommand(Command<S> cmd) {
		this.cmd = Objects.requireNonNull(cmd);
	}

	public CommandExecutionResult execute(S system, BlocLines lines) {
		try {
			// WasmLog.log("...running " + cmd.getClass().getName() + " ...");
			final CommandExecutionResult result = cmd.execute(system, lines);
			// if (result.isOk()) {
			// // TRACECOMMAND
			// System.err.println("CMD = " + cmd.getClass());
			// }
			return result;
		} catch (Throwable t) {
			Log.error("Error " + t);
			Logme.error(t);
			String msg = "You should send a mail to plantuml@gmail.com or post to https://plantuml.com/qa with this log (V"
					+ Version.versionString() + ")";
			Log.error(msg);
			msg += " " + t.toString();
			return CommandExecutionResult.error(msg, t);
		}
	}

	public CommandControl isValid(BlocLines lines) {
		return cmd.isValid(lines);
	}

	public String[] getDescription() {
		return cmd.getDescription();
	}

}
