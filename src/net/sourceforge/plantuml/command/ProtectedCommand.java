/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import net.sourceforge.plantuml.Log;

public class ProtectedCommand implements Command {

	private final Command cmd;

	public ProtectedCommand(Command cmd) {
		this.cmd = cmd;
	}

	public CommandExecutionResult execute(List<String> lines) {
		try {
			return cmd.execute(lines);
		} catch (Throwable t) {
			t.printStackTrace();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final PrintWriter pw = new PrintWriter(baos);
			t.printStackTrace(pw);
			Log.error("Error " + t);
			String msg = "You should send a mail to plantuml@gmail.com with this log";
			Log.error(msg);
			msg += " " + new String(baos.toByteArray());
			return CommandExecutionResult.error(msg);
		}
	}

	public String getHelpMessageForDeprecated(List<String> lines) {
		return cmd.getHelpMessageForDeprecated(lines);
	}

	public boolean isDeprecated(List<String> lines) {
		return cmd.isDeprecated(lines);
	}

	public CommandControl isValid(List<String> lines) {
		return cmd.isValid(lines);
	}

}
