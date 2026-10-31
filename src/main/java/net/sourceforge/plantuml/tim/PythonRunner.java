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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsFlags;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.tim.builtin.Feature;
import net.sourceforge.plantuml.tim.builtin.Getenv;
import net.sourceforge.plantuml.tim.builtin.RandomFunction;
import net.sourceforge.plantuml.warning.Warning;
import sanskrit.Builtin;
import sanskrit.ExecutionLimitError;
import sanskrit.Interp;
import sanskrit.PyError;
import sanskrit.PyModule;
import sanskrit.PySyntaxError;
import net.sourceforge.plantuml.text.StringLocated;

/**
 * The Python state of one diagram. Created at the first Python chunk only
 * (TContext keeps it null otherwise), so a diagram without Python pays
 * nothing.
 * <p>
 * The text API lives in the builtin module <code>puml</code>, available
 * without <code>import</code>: <code>puml.emit(line)</code> appends a line to
 * the generated text. <code>emit</code> is also a global alias.
 * <code>math</code>, <code>json</code>, <code>time</code>, <code>datetime</code> and <code>color</code> are bound as globals too (no import
 * needed; <code>import math</code> stays accepted).
 * <code>puml.version</code> and <code>puml.edition</code> ("jar", "js" or "c") are
 * strings, so are <code>puml.filename</code>, <code>puml.dirpath</code> and
 * <code>puml.filedate</code> (the values of <code>%filename()</code>,
 * <code>%dirpath()</code> and <code>%filedate()</code>, empty when unknown),
 * <code>puml.theme()</code> is a dict (the metadata of the current theme), <code>puml.args</code> is a dict of the variables defined from outside,
 * <code>puml.newline()</code>, <code>n()</code>, <code>breakline()</code>, <code>tab()</code>, <code>left_align()</code>,
 * <code>right_align()</code> and <code>backslash()</code> return the special characters of the legacy functions of the same name,
 * <code>puml.metadata</code> a dict read from the YAML header of the diagram
 * (<code>---</code> lines after <code>@startuml</code>; every scalar is a str).
 */
final class PythonRunner {

	private final List<String> emitted = new ArrayList<>();
	private final Interp interp;
	private String pumlError;
	/** The diagram lines of every chunk run so far, by chunk name. */
	private final Map<String, List<StringLocated>> sources = new HashMap<>();
	private int chunkCount;
	private final PythonMacros macros;

	PythonRunner(String version, Map<String, String> arguments, Map<String, Object> metadata,
			Map<String, String> diagram,
			Supplier<Map<String, String>> theme, Function<String, String> resources, Consumer<Warning> warnings,
			PythonMacros.LegacyNames legacyNames) {
		// What print() writes is dropped: whether print is an alias of emit is
		// still open (see PYTHON_PREPROCESSOR.md), it is not for now.
		this.interp = new Interp(text -> {
		});
		this.interp.setMaxSteps(10000000L);
		this.macros = new PythonMacros(interp, emitted, legacyNames);

		final PyModule puml = new PyModule("puml");
		puml.putString("version", version == null ? "" : version);
		puml.putString("edition", TeaVM.isC() ? "c" : TeaVM.isJavaScript() ? "js" : "jar");
		puml.putStringMap("args", arguments);
		puml.putPlain("metadata", metadata);
		// where the diagram comes from: "" when unknown (stdin), as %filename() and the others
		puml.putString("filename", diagram.get("filename"));
		puml.putString("dirpath", diagram.get("dirpath"));
		puml.putString("filedate", diagram.get("filedate"));
		// the first line of the diagram, @startuml included ("@startuml foo bar"): what %xargs() gives
		puml.putString("xargs", diagram.get("xargs"));
		puml.put("feature", (in, args) -> Boolean.valueOf(Feature.isAvailable(string("feature", args))));
		// the same rules as %getenv: a system property first, then the environment, and the
		// security profile decides what can be read; "" (or the default) when there is none
		puml.put("getenv", (in, args) -> {
			if (args.length != 1 && args.length != 2)
				throw Builtin.typeError("getenv() takes 1 or 2 arguments (" + args.length + " given)");
			if (args[0] instanceof String == false || args.length == 2 && args[1] instanceof String == false)
				throw Builtin.typeError("getenv() arguments must be str");
			final String value = Getenv.read((String) args[0]);
			if (value != null)
				return value;
			return args.length == 2 ? args[1] : "";
		});
		// the same rules as %file_exists: SFile, so nothing in the SANDBOX profile
		puml.put("file_exists", (in, args) -> Boolean.valueOf(new SFile(string("file_exists", args)).exists()));
		// what !log does: a line on the standard error, "[Log] " in front of it
		puml.put("log", (in, args) -> {
			EaterLog.write(string("log", args));
			return Builtin.none();
		});
		// a warning of the diagram, like the ones of the deprecated syntaxes: puml.warning(line, ...)
		// has one line per argument
		puml.put("warning", (in, args) -> {
			if (args.length == 0)
				throw Builtin.typeError("warning() takes at least 1 argument (0 given)");
			final String[] lines = new String[args.length];
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof String == false)
					throw Builtin.typeError("warning() arguments must be str");
				lines[i] = (String) args[i];
			}
			warnings.accept(new Warning(lines));
			return Builtin.none();
		});
		// the special characters of %newline(), %n(), %breakline(), %tab(), %left_align(),
		// %right_align() and %backslash(), with the same names and the same values
		final String newline = JawsFlags.USE_BLOCK_E1_IN_NEWLINE_FUNCTION ? "" + Jaws.BLOCK_E1_NEWLINE : "\n";
		final String breakline = JawsFlags.USE_BLOCK_E1_IN_NEWLINE_FUNCTION ? "" + Jaws.BLOCK_E1_BREAKLINE : "\n";
		putConstant(puml, "newline", newline);
		putConstant(puml, "n", newline);
		putConstant(puml, "breakline", breakline);
		putConstant(puml, "tab", "" + Jaws.BLOCK_E1_REAL_TABULATION);
		putConstant(puml, "left_align", "" + Jaws.BLOCK_E1_NEWLINE_LEFT_ALIGN);
		putConstant(puml, "right_align", "" + Jaws.BLOCK_E1_NEWLINE_RIGHT_ALIGN);
		putConstant(puml, "backslash", "" + Jaws.BLOCK_E1_REAL_BACKSLASH);
		// the same numbers as %random: puml.random() is 0 or 1, puml.random(n) is in [0, n),
		// puml.random(a, b) is in [a, b); not the same from one run to the next
		puml.put("random", (in, args) -> random(args));
		// the text of a file found like an included one (same finder, same security profile)
		puml.put("read", (in, args) -> {
			final String name = string("read", args);
			final String text = resources.apply(name);
			if (text == null)
				throw Builtin.runtimeError("cannot read '" + name + "'");
			return text;
		});
		// a function, not a constant: a !theme line may come after the interpreter is created
		puml.put("theme", (in, args) -> {
			if (args.length != 0)
				throw Builtin.typeError("theme() takes no argument (" + args.length + " given)");
			return PyModule.stringDict(theme.get());
		});
		puml.put("emit", (in, args) -> emit(args));
		puml.put("error", (in, args) -> error(string("error", args)));
		puml.put("macro", (in, args) -> macros.macro(args));
		// a text line of a Python block (PythonChunks): its macros are expanded, then it is emitted
		puml.put("_text", (in, args) -> emitLines(macros.expand(string("_text", args))));
		// a text line directly in the body of a class (PythonChunks): section 4 of PYTHON_PREPROCESSOR.md
		puml.put("_text_in_class", (in, args) -> {
			throw Builtin.runtimeError("a text line cannot be directly in the body of a class: put it in a method");
		});
		this.interp.addModule(puml);
		this.interp.addModule(PythonColorModule.create());
		this.interp.setGlobal("puml", puml);
		// The builtin modules need no import in a diagram ("!import" is the legacy directive).
		this.interp.setGlobal("math", this.interp.getModule("math"));
		this.interp.setGlobal("json", this.interp.getModule("json"));
		this.interp.setGlobal("color", this.interp.getModule("color"));
		this.interp.setGlobal("time", this.interp.getModule("time"));
		this.interp.setGlobal("datetime", this.interp.getModule("datetime"));
		this.interp.setGlobal("emit", puml.get("emit"));
		this.interp.setGlobal("macro", puml.get("macro"));
	}

	/** The only argument of a puml function, which must be a str. */
	// puml.<name>(): a function without argument that returns the same string every time
	private static void putConstant(PyModule puml, String name, String value) {
		puml.put(name, (in, args) -> {
			if (args.length != 0)
				throw Builtin.typeError(name + "() takes no argument (" + args.length + " given)");
			return value;
		});
	}

	private static String string(String function, Object[] args) {
		if (args.length != 1)
			throw Builtin.typeError(function + "() takes exactly 1 argument (" + args.length + " given)");
		if (args[0] instanceof String == false)
			throw Builtin.typeError(function + "() argument must be str");
		return (String) args[0];
	}

	private static Object random(Object[] args) {
		if (args.length > 2)
			throw Builtin.typeError("random() takes at most 2 arguments (" + args.length + " given)");
		final int[] n = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof Integer == false)
				throw Builtin.typeError("random() arguments must be int");
			n[i] = ((Integer) args[i]).intValue();
		}
		if (args.length == 0)
			return Integer.valueOf(RandomFunction.nextInt(2));
		if (args.length == 1) {
			if (n[0] <= 0)
				throw Builtin.valueError("random(n) needs n > 0");
			return Integer.valueOf(RandomFunction.nextInt(n[0]));
		}
		if (n[1] <= n[0])
			throw Builtin.valueError("random(a, b) needs a < b");
		// the range may not fit in an int: (b - a) would overflow
		final long range = (long) n[1] - (long) n[0];
		if (range > Integer.MAX_VALUE)
			throw Builtin.valueError("random(a, b): the range is too large");
		return Integer.valueOf(RandomFunction.nextInt((int) range) + n[0]);
	}

	private Object emit(Object[] args) {
		return emitLines(string("emit", args));
	}

	private Object emitLines(String text) {
		// A string with several lines is several lines of PlantUML text.
		for (String line : text.split("\\r?\\n", -1))
			emitted.add(line);
		return Builtin.none();
	}

	/** True if a macro of this name is declared. */
	boolean isMacro(String name) {
		return macros.contains(name);
	}

	/** True once a macro is declared: until then, text lines are not scanned for macros. */
	boolean hasMacros() {
		return macros.isEmpty() == false;
	}

	/**
	 * Replaces the macro calls of a text line outside Python by their result
	 * (several lines are separated by a newline). An error is reported on the
	 * line of the macro that caused it, or on <code>at</code>.
	 */
	String expandMacros(String text, StringLocated at) throws EaterExceptionMessage {
		pumlError = null;
		try {
			return macros.expand(text);
		} catch (PyError e) {
			throw translate(e, at);
		} catch (ExecutionLimitError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage(), at);
		}
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
			throw translate(e, null);
		} catch (ExecutionLimitError e) {
			throw new EaterExceptionMessage("Python error: " + e.getMessage(), null);
		}
		return new ArrayList<>(emitted);
	}

	/**
	 * A Python exception as a message on the diagram line that caused it (found
	 * through the chunk and line of the innermost frame), or on
	 * <code>fallback</code> when it is not known.
	 */
	private EaterExceptionMessage translate(PyError e, StringLocated fallback) {
		StringLocated at = locate(sources.get(e.getFile()), e.getLine());
		if (at == null)
			at = fallback;
		// a puml.error is reported as the plain message
		if (pumlError != null && e.typeName().equals("RuntimeError") && e.text().equals(pumlError))
			return new EaterExceptionMessage(pumlError, at);
		final String text = e.text();
		return new EaterExceptionMessage("Python error: " + e.typeName() + (text.isEmpty() ? "" : ": " + text), at);
	}

	/** The diagram line of the line number (1-based) of a chunk, or null. */
	private static StringLocated locate(List<StringLocated> lines, int line) {
		if (lines == null || line < 1 || line > lines.size())
			return null;
		return lines.get(line - 1);
	}

	/**
	 * Replaces each <code>!{expr}</code> of a text line by <code>str(expr)</code>
	 * (Python is evaluated in the state of the diagram); as in an f-string, a
	 * field may also be <code>!{expr!r}</code>, <code>!{expr!s}</code> or
	 * <code>!{expr:spec}</code> (see {@link PythonChunks#interpolationCode}). A doubled bang before
	 * the brace is the escape: it gives a literal <code>!{</code>. With
	 * <code>keepOnError</code>, an expression that fails stays as written in the
	 * text instead of being an error.
	 */
	String interpolate(String text, boolean keepOnError) throws EaterExceptionMessage {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < text.length()) {
			if (text.startsWith(PythonChunks.ESCAPE, i)) {
				sb.append(PythonChunks.OPEN);
				i += PythonChunks.ESCAPE.length();
				continue;
			}
			final int close = text.startsWith(PythonChunks.OPEN, i) ? PythonChunks.closingBrace(text, i + 2) : -1;
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

	/** The text of an interpolation: an expression, with an optional conversion and format spec. */
	private String evaluate(String expression) throws EaterExceptionMessage {
		run("__interpolated = " + PythonChunks.interpolationCode(expression) + "\n", null);
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
