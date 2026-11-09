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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.python.Builtin;
import org.luaj.vm2.python.Interp;

/**
 * The macros of text Python (section 5 of PYTHON_PREPROCESSOR.md): Python
 * functions declared with <code>@macro</code> (or <code>@puml.macro</code>) and
 * called by name from the text lines of the diagram, the way the legacy
 * <code>!define</code> and <code>!unquoted procedure</code> are called.
 * <p>
 * The arguments of a call are text, split as the legacy preprocessor does for
 * an unquoted procedure: on the commas outside double quotes and parentheses,
 * trimmed, without their double quotes. <code>name=value</code> and
 * <code>$name=value</code> are keyword arguments. The call is replaced by what
 * the function emits, followed by <code>str()</code> of its return value when
 * it is not <code>None</code>; the result is not scanned again.
 */
final class PythonMacros {

	/** Tells whether a name is already a legacy function, procedure or !define of the diagram. */
	interface LegacyNames {
		boolean exists(String name);
	}

	private final Interp interp;
	/** The lines emitted by Python: what a macro emits is taken back from the end of it. */
	private final List<String> emitted;
	private final LegacyNames legacy;
	private final Map<String, Object> macros = new LinkedHashMap<>();

	PythonMacros(Interp interp, List<String> emitted, LegacyNames legacy) {
		this.interp = interp;
		this.emitted = emitted;
		this.legacy = legacy;
	}

	boolean isEmpty() {
		return macros.isEmpty();
	}

	boolean contains(String name) {
		return macros.containsKey(name);
	}

	/**
	 * The builtin <code>macro</code>: <code>@macro</code> declares the function
	 * under its own name, <code>@macro("name")</code> returns the decorator that
	 * declares it under that name. The function is returned unchanged.
	 */
	Object macro(Object[] args) {
		if (args.length != 1)
			throw Builtin.typeError("macro() takes exactly 1 argument (" + args.length + " given)");
		final Object arg = args[0];
		if (arg instanceof String) {
			final String name = (String) arg;
			checkName(name);
			return Builtin.of("macro", (in, f) -> {
				if (f.length != 1 || Interp.isCallable(f[0]) == false)
					throw Builtin.typeError("macro(\"" + name + "\") decorates a function");
				declare(name, f[0]);
				return f[0];
			});
		}
		if (Interp.isCallable(arg)) {
			final String name = Interp.functionName(arg);
			if (name == null)
				throw Builtin.typeError("macro() needs a name for this object: use @macro(\"name\")");
			checkName(name);
			declare(name, arg);
			return arg;
		}
		throw Builtin.typeError("macro() argument must be a function or a name");
	}

	private static void checkName(String name) {
		int i = name.startsWith("$") ? 1 : 0;
		boolean ok = i < name.length() && isIdentifierStart(name.charAt(i));
		for (i++; ok && i < name.length(); i++)
			ok = isIdentifierPart(name.charAt(i));
		if (ok == false)
			throw Builtin.valueError("invalid macro name '" + name
					+ "': a macro name is an identifier, optionally preceded by $");
	}

	private void declare(String name, Object function) {
		if (macros.containsKey(name))
			throw Builtin.valueError("macro '" + name + "' is already declared");
		if (legacy.exists(name))
			throw Builtin.valueError(
					"macro '" + name + "' is also a legacy !define, !function or !procedure of the diagram");
		macros.put(name, function);
	}

	/**
	 * Replaces the macro calls of a text line by their result, which may hold
	 * several lines. Python exceptions are thrown as they are.
	 */
	String expand(String text) {
		if (macros.isEmpty() || text.indexOf('(') == -1)
			return text;
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < text.length()) {
			final char ch = text.charAt(i);
			final boolean wordStart = (ch == '$' || isIdentifierStart(ch))
					&& (i == 0 || isIdentifierPart(text.charAt(i - 1)) == false);
			if (wordStart == false) {
				sb.append(ch);
				i++;
				continue;
			}
			int end = ch == '$' ? i + 1 : i;
			while (end < text.length() && isIdentifierPart(text.charAt(end)))
				end++;
			final String name = text.substring(i, end);
			final Object function = macros.get(name);
			final int close = function != null && end < text.length() && text.charAt(end) == '('
					? closingParenthesis(text, end)
					: -1;
			if (close == -1) {
				sb.append(name);
				i = Math.max(end, i + 1);
				continue;
			}
			sb.append(call(function, text.substring(end + 1, close)));
			i = close + 1;
		}
		return sb.toString();
	}

	private String call(Object function, String inside) {
		final List<Object> positional = new ArrayList<>();
		final Map<String, Object> keywords = new LinkedHashMap<>();
		for (String argument : splitArguments(inside)) {
			final int equal = keywordEnd(argument);
			if (equal == -1) {
				positional.add(unquote(argument));
			} else {
				String key = argument.substring(0, equal).trim();
				if (key.startsWith("$"))
					key = key.substring(1);
				keywords.put(key, unquote(argument.substring(equal + 1).trim()));
			}
		}
		final int before = emitted.size();
		final Object result = interp.callFunction(function, positional.toArray(), keywords);
		final StringBuilder sb = new StringBuilder();
		while (emitted.size() > before) {
			if (sb.length() > 0)
				sb.append('\n');
			sb.append(emitted.remove(before));
		}
		if (Interp.isNone(result) == false) {
			if (sb.length() > 0)
				sb.append('\n');
			sb.append(Interp.str(result));
		}
		return sb.toString();
	}

	/** The arguments of a call, trimmed: split on the commas outside double quotes and parentheses. */
	static List<String> splitArguments(String inside) {
		final List<String> result = new ArrayList<>();
		if (inside.trim().isEmpty())
			return result;
		int depth = 0;
		boolean quoted = false;
		int start = 0;
		for (int i = 0; i < inside.length(); i++) {
			final char ch = inside.charAt(i);
			if (ch == '"')
				quoted = quoted == false;
			else if (quoted == false && ch == '(')
				depth++;
			else if (quoted == false && ch == ')')
				depth--;
			else if (quoted == false && depth == 0 && ch == ',') {
				result.add(inside.substring(start, i).trim());
				start = i + 1;
			}
		}
		result.add(inside.substring(start).trim());
		return result;
	}

	/** The index of the "=" of "name=value" or "$name=value", or -1 for a positional argument. */
	private static int keywordEnd(String argument) {
		int i = argument.startsWith("$") ? 1 : 0;
		if (i >= argument.length() || isIdentifierStart(argument.charAt(i)) == false)
			return -1;
		while (i < argument.length() && isIdentifierPart(argument.charAt(i)))
			i++;
		while (i < argument.length() && argument.charAt(i) == ' ')
			i++;
		if (i < argument.length() && argument.charAt(i) == '='
				&& (i + 1 >= argument.length() || argument.charAt(i + 1) != '='))
			return i;
		return -1;
	}

	private static String unquote(String argument) {
		if (argument.length() >= 2 && argument.startsWith("\"") && argument.endsWith("\""))
			return argument.substring(1, argument.length() - 1);
		return argument;
	}

	/** The index of the ")" that closes the "(" at open, ignoring the parentheses inside double quotes, or -1. */
	static int closingParenthesis(String text, int open) {
		int depth = 0;
		boolean quoted = false;
		for (int i = open; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (ch == '"') {
				quoted = quoted == false;
			} else if (quoted == false && ch == '(') {
				depth++;
			} else if (quoted == false && ch == ')') {
				depth--;
				if (depth == 0)
					return i;
			}
		}
		return -1;
	}

	private static boolean isIdentifierStart(char ch) {
		return Character.isLetter(ch) || ch == '_';
	}

	private static boolean isIdentifierPart(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_';
	}
}
