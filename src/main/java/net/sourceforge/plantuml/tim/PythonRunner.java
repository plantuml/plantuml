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

import org.luaj.vm2.python.Builtin;
import org.luaj.vm2.python.ExecutionLimitError;
import org.luaj.vm2.python.Interp;
import org.luaj.vm2.python.PyError;
import org.luaj.vm2.python.PyModule;
import org.luaj.vm2.python.PySyntaxError;

/**
 * The Python state of one diagram. Created at the first Python chunk only
 * (TContext keeps it null otherwise), so a diagram without Python pays
 * nothing.
 * <p>
 * The text API lives in the builtin module <code>puml</code>, available
 * without <code>import</code>: <code>puml.emit(line)</code> appends a line to
 * the generated text. <code>emit</code> is also a global alias.
 */
final class PythonRunner {

	private final List<String> emitted = new ArrayList<>();
	private final Interp interp;
	private String pumlError;

	PythonRunner() {
		// What print() writes is dropped: whether print is an alias of emit is
		// still open (see PYTHON_PREPROCESSOR.md), it is not for now.
		this.interp = new Interp(text -> {
		});
		this.interp.setMaxSteps(10000000L);

		final PyModule puml = new PyModule("puml");
		puml.put("emit", (in, args) -> emit(args));
		puml.put("quote", (in, args) -> PumlText.quote(string("quote", args)));
		puml.put("id", (in, args) -> PumlText.id(string("id", args)));
		puml.put("error", (in, args) -> error(string("error", args)));
		this.interp.addModule(puml);
		this.interp.setGlobal("puml", puml);
		this.interp.setGlobal("emit", puml.get("emit"));
	}

	/** The only argument of a puml function, which must be a str. */
	private static String string(String function, Object[] args) {
		if (args.length != 1)
			throw Builtin.typeError(function + "() takes exactly 1 argument (" + args.length + " given)");
		if (args[0] instanceof String == false)
			throw Builtin.typeError(function + "() argument must be str");
		return (String) args[0];
	}

	private Object emit(Object[] args) {
		// A string with several lines is several lines of PlantUML text.
		for (String line : string("emit", args).split("\\r?\\n", -1))
			emitted.add(line);
		return Builtin.none();
	}

	/**
	 * puml.error(msg): stops the diagram with msg, reported on the diagram
	 * line. It is a RuntimeError, so a script may catch it like any exception.
	 */
	private Object error(String message) {
		pumlError = message;
		throw Builtin.runtimeError(message);
	}

	/** Runs a chunk and returns the lines it emitted. */
	List<String> run(String code, String name) throws EaterExceptionMessage {
		emitted.clear();
		pumlError = null;
		try {
			interp.run(code, name);
		} catch (PySyntaxError e) {
			throw new EaterExceptionMessage("Python syntax error: " + e.getMessage());
		} catch (PyError e) {
			// a puml.error is reported as the plain message, without traceback
			if (pumlError != null && e.typeName().equals("RuntimeError") && e.text().equals(pumlError))
				throw new EaterExceptionMessage(pumlError);
			throw new EaterExceptionMessage("Python error: " + e.getMessage());
		} catch (ExecutionLimitError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage());
		}
		return new ArrayList<>(emitted);
	}

	static class EaterExceptionMessage extends Exception {
		private static final long serialVersionUID = 1L;

		EaterExceptionMessage(String message) {
			super(message);
		}
	}
}
