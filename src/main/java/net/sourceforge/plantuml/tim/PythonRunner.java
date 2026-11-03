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
 */
package net.sourceforge.plantuml.tim;

import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.python.ExecutionLimitError;
import org.luaj.vm2.python.Interp;
import org.luaj.vm2.python.PyError;
import org.luaj.vm2.python.PySyntaxError;

/**
 * The Python state of one diagram. Created at the first Python chunk only
 * (TContext keeps it null otherwise), so a diagram without Python pays
 * nothing.
 */
final class PythonRunner {

	private static final String PRELUDE = "def emit(line):\n    print(line)\n";

	private final StringBuilder output = new StringBuilder();
	private final Interp interp;

	PythonRunner() {
		this.interp = new Interp(text -> output.append(text));
		this.interp.setMaxSteps(10000000L);
		this.interp.run(PRELUDE, "<prelude>");
	}

	/** Runs a chunk and returns the lines it emitted. */
	List<String> run(String code, String name) throws EaterExceptionMessage {
		output.setLength(0);
		try {
			interp.run(code, name);
		} catch (PySyntaxError e) {
			throw new EaterExceptionMessage("Python syntax error: " + e.getMessage());
		} catch (PyError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage());
		} catch (ExecutionLimitError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage());
		}
		final List<String> lines = new ArrayList<>();
		if (output.length() > 0) {
			final String text = output.toString();
			final String[] split = text.split("\\n", -1);
			// the text ends with a newline: the last item is empty
			for (int i = 0; i < split.length - 1; i++)
				lines.add(split[i]);
		}
		return lines;
	}

	static class EaterExceptionMessage extends Exception {
		EaterExceptionMessage(String message) {
			super(message);
		}
	}
}
