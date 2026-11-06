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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.python.Builtin;
import org.luaj.vm2.python.ExecutionLimitError;
import org.luaj.vm2.python.Interp;
import org.luaj.vm2.python.PyError;
import org.luaj.vm2.python.PyModule;
import org.luaj.vm2.python.PySyntaxError;

import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.text.StringLocated;

/**
 * The Python state of one diagram. Created at the first Python chunk only
 * (TContext keeps it null otherwise), so a diagram without Python pays
 * nothing.
 * <p>
 * The text API lives in the builtin module <code>puml</code>, available
 * without <code>import</code>: <code>puml.emit(line)</code> appends a line to
 * the generated text. <code>emit</code> is also a global alias.
 * <code>puml.version</code> and <code>puml.edition</code> ("jar", "js" or "c") are
 * strings, <code>puml.args</code> is a dict of the variables defined from outside.
 */
final class PythonRunner {

	private final List<String> emitted = new ArrayList<>();
	private final Interp interp;
	private String pumlError;
	/** The diagram lines of every chunk run so far, by chunk name. */
	private final Map<String, List<StringLocated>> sources = new HashMap<>();
	private int chunkCount;

	PythonRunner(String version, Map<String, String> arguments) {
		// What print() writes is dropped: whether print is an alias of emit is
		// still open (see PYTHON_PREPROCESSOR.md), it is not for now.
		this.interp = new Interp(text -> {
		});
		this.interp.setMaxSteps(10000000L);

		final PyModule puml = new PyModule("puml");
		puml.putString("version", version == null ? "" : version);
		puml.putString("edition", TeaVM.isC() ? "c" : TeaVM.isJavaScript() ? "js" : "jar");
		puml.putStringMap("args", arguments);
		puml.put("emit", (in, args) -> emit(args));
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

	/**
	 * Runs a chunk and returns the lines it emitted. <code>lines</code> gives, for
	 * each line of the code, the diagram line it comes from (null: unknown), so
	 * that an error is reported on the line that caused it, even when it happens
	 * in a function defined by an earlier chunk.
	 */
	List<String> run(String code, List<StringLocated> lines) throws EaterExceptionMessage {
		emitted.clear();
		pumlError = null;
		final String name = lines == null ? "<diagram>" : "<python " + (++chunkCount) + ">";
		if (lines != null)
			sources.put(name, lines);
		try {
			interp.run(code, name);
		} catch (PySyntaxError e) {
			throw new EaterExceptionMessage(
					"Python syntax error: " + e.detail + " (column " + (e.col + 1) + ")", locate(lines, e.line));
		} catch (PyError e) {
			final StringLocated at = locate(sources.get(e.getFile()), e.getLine());
			// a puml.error is reported as the plain message
			if (pumlError != null && e.typeName().equals("RuntimeError") && e.text().equals(pumlError))
				throw new EaterExceptionMessage(pumlError, at);
			final String text = e.text();
			throw new EaterExceptionMessage("Python error: " + e.typeName() + (text.isEmpty() ? "" : ": " + text), at);
		} catch (ExecutionLimitError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage(), null);
		}
		return new ArrayList<>(emitted);
	}

	/** The diagram line of the line number (1-based) of a chunk, or null. */
	private static StringLocated locate(List<StringLocated> lines, int line) {
		if (lines == null || line < 1 || line > lines.size())
			return null;
		return lines.get(line - 1);
	}

	/**
	 * Replaces each <code>${expr}</code> of a text line by <code>str(expr)</code>
	 * (Python is evaluated in the state of the diagram). A doubled dollar before
	 * the brace is the escape: it gives a literal <code>${</code>. With
	 * <code>keepOnError</code>, an expression that fails stays as written in the
	 * text instead of being an error.
	 */
	String interpolate(String text, boolean keepOnError) throws EaterExceptionMessage {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < text.length()) {
			if (text.startsWith("$" + "${", i)) {
				sb.append("${");
				i += 3;
				continue;
			}
			final int close = text.startsWith("${", i) ? PythonChunks.closingBrace(text, i + 2) : -1;
			if (close == -1) {
				sb.append(text.charAt(i));
				i++;
				continue;
			}
			final String expression = text.substring(i + 2, close);
			try {
				sb.append(evaluate(expression));
			} catch (EaterExceptionMessage e) {
				if (keepOnError == false)
					throw e;
				sb.append(text, i, close + 1);
			}
			i = close + 1;
		}
		return sb.toString();
	}

	private String evaluate(String expression) throws EaterExceptionMessage {
		run("__interpolated = (" + expression + ")\n", null);
		return interp.getGlobalAsString("__interpolated");
	}

	static class EaterExceptionMessage extends Exception {
		private static final long serialVersionUID = 1L;

		/** The diagram line the error comes from, or null when unknown. */
		final StringLocated at;

		EaterExceptionMessage(String message, StringLocated at) {
			super(message);
			this.at = at;
		}
	}
}
