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
package net.sourceforge.plantuml;

import java.io.File;
import java.io.PrintStream;

import net.sourceforge.plantuml.command.PSystemAbstractFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.eggs.PSystemWelcome;
import net.sourceforge.plantuml.error.PSystemError;

public class StdrptV2 implements Stdrpt {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	public void finalMessage() {
	}

	public void finalMessage(ErrorStatus error) {
	}

	public void errorLine(int lineError, File file) {
	}

	public void printInfo(final PrintStream output, Diagram sys) {
		if (sys instanceof PSystemWelcome) {
			sys = null;
		}
		if (sys == null || sys instanceof PSystemError) {
			out(output, (PSystemError) sys);
		}
	}

	private void out(final PrintStream output, final PSystemError err) {
		final StringBuilder line = new StringBuilder();
		if (empty(err)) {
		} else {
			line.append(err.getLineLocation().getDescription());
			line.append(":");
			line.append(err.getLineLocation().getPosition() + 1);
			line.append(":");
			line.append("error");
			line.append(":");
			for (ErrorUml er : err.getErrorsUml()) {
				line.append(er.getError());
			}
		}
		output.println(line);
		output.flush();
	}

	private boolean empty(final PSystemError err) {
		if (err == null) {
			return true;
		}
		for (ErrorUml er : err.getErrorsUml()) {
			if (PSystemAbstractFactory.EMPTY_DESCRIPTION.equals(er.getError()))
				return true;
		}
		return false;
	}

}
