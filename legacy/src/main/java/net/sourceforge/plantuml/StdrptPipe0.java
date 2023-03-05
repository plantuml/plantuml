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

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.utils.Log;

public class StdrptPipe0 implements Stdrpt {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	public void printInfo(final PrintStream output, final Diagram sys) {
		if (sys instanceof PSystemError) {
			final PSystemError err = (PSystemError) sys;
			output.println("ERROR");
			output.println(err.getLineLocation().getPosition());
			for (ErrorUml er : err.getErrorsUml()) {
				output.println(er.getError());
			}
			output.flush();
		}
	}

	public void finalMessage(ErrorStatus error) {
		if (error.hasError())
			Log.error("Some diagram description contains errors");

		if (error.isNoData())
			Log.error("No diagram found");

	}

	public void errorLine(int lineError, File file) {
		Log.error("Error line " + (lineError + 1) + " in file: " + file.getPath());
	}

}
