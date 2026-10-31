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
import java.util.Map;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.text.PythonLine;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;

/**
 * Groups the Python lines of a diagram into chunks, before the legacy
 * preprocessor looks at them (one single pass afterwards).
 * <ul>
 * <li>A <code>!python</code> ... <code>!endpy</code> block is taken as it is:
 * the legacy preprocessor is not applied inside, so <code>'</code> is a Python
 * string delimiter, not a PlantUML comment.</li>
 * <li>A run of consecutive <code>!</code> lines of type
 * {@link TLineType#PYTHON} is one chunk; the <code>!</code> is a one-column
 * gutter.</li>
 * </ul>
 * A chunk is one {@link StringLocated} whose text is
 * {@link TLineType#PYTHON_MARKER} followed by the Python code.
 */
public final class PythonChunks {

	private static final Pattern START = Pattern.compile("^\\s*!python\\s*$");
	private static final Pattern END = Pattern.compile("^\\s*!endpy\\s*$");

	private PythonChunks() {
	}

	/**
	 * Replaces the Python lines of a body by chunks. It is idempotent: it runs
	 * again on the body of a legacy function or procedure when this one is
	 * executed, and a chunk it already built is kept as it is, never merged
	 * with its neighbours.
	 * <p>
	 * <code>sources</code> receives, for each chunk, the line of the diagram
	 * that every line of its code comes from, to report an error on the right
	 * line.
	 */
	public static List<StringLocated> collapse(List<StringLocated> body, Map<StringLocated, List<StringLocated>> sources) {
		// Fast path: a diagram without Python pays nothing.
		boolean found = false;
		for (StringLocated sl : body)
			if (sl.containsExclamationMark()) {
				found = true;
				break;
			}
		if (found == false)
			return body;

		final List<StringLocated> result = new ArrayList<>();
		int i = 0;
		while (i < body.size()) {
			final StringLocated first = body.get(i);
			final String s = first.getString();
			if (START.matcher(s).matches()) {
				final StringBuilder code = new StringBuilder();
				final List<StringLocated> lines = new ArrayList<>();
				// an indented "!python" indents its block: that indentation is removed from each line
				final int indent = leadingSpaces(s);
				i++;
				while (i < body.size() && END.matcher(body.get(i).getString()).matches() == false) {
					code.append(removeIndent(body.get(i).getString(), indent)).append('\n');
					lines.add(body.get(i));
					i++;
				}
				if (i >= body.size()) {
					result.add(error(first, "!python without !endpy", sources));
				} else {
					i++; // skips !endpy
					result.add(chunk(first, code, lines, sources));
				}
			} else if (END.matcher(s).matches()) {
				result.add(error(first, "!endpy without !python", sources));
				i++;
			} else if (isGutterLine(first)) {
				final StringBuilder code = new StringBuilder();
				final List<StringLocated> lines = new ArrayList<>();
				// the lines that opened a block (they end with a colon)
				final List<Header> headers = new ArrayList<>();
				while (i < body.size()) {
					final StringLocated sl = body.get(i);
					if (isGutterLine(sl) || isIndentedGutterLine(sl)) {
						final String python = gutter(sl.getString());
						code.append(python).append('\n');
						lines.add(sl);
						trackHeaders(headers, python);
						i++;
						continue;
					}
					if (headers.isEmpty())
						break;
					// A text line deeper than the open block belongs to it (blank lines are ignored).
					int j = i;
					while (j < body.size() && body.get(j).getString().trim().length() == 0)
						j++;
					if (j >= body.size())
						break;
					final String text = body.get(j).getString();
					// The gutter takes one column: a text line is Python code indented by one column less.
					final int indent = leadingSpaces(text) - 1;
					if (text.charAt(0) == '\t' && isBangLine(text) == false) {
						// tabs are not allowed in the indentation: the Python lexer reports it
						code.append('\t').append(textToPython(text.trim())).append('\n');
						lines.add(body.get(j));
						i = j + 1;
						continue;
					}
					while (headers.isEmpty() == false && headers.get(headers.size() - 1).indent >= indent)
						headers.remove(headers.size() - 1);
					if (headers.isEmpty() || isBangLine(text))
						break;
					if (body.get(j).getType() != TLineType.COMMENT_SIMPLE) {
						for (int k = 0; k < indent; k++)
							code.append(' ');
						// directly in the body of a class, a text line would emit when the class is defined
						if (headers.get(headers.size() - 1).isClass)
							code.append("puml._text_in_class()\n");
						else
							code.append(textToPython(text.trim())).append('\n');
						lines.add(body.get(j));
					}
					i = j + 1;
				}
				result.add(chunk(first, code, lines, sources));
			} else {
				result.add(first);
				i++;
			}
		}
		return result;
	}

	/** Removes up to <code>indent</code> leading spaces: the indentation of the "!python" line. */
	private static String removeIndent(String line, int indent) {
		int n = 0;
		while (n < indent && n < line.length() && line.charAt(n) == ' ')
			n++;
		return line.substring(n);
	}

	/**
	 * A chunk that only reports an error on its line, when it runs: a misplaced
	 * <code>!python</code> or <code>!endpy</code>. The message is given as it is,
	 * like <code>puml.error</code>.
	 */
	private static StringLocated error(StringLocated at, String message,
			Map<StringLocated, List<StringLocated>> sources) {
		final List<StringLocated> lines = new ArrayList<>();
		lines.add(at);
		return chunk(at, new StringBuilder("puml.error(\"" + message + "\")\n"), lines, sources);
	}

	/** A line that opened a block: its indentation, and whether it is a class statement. */
	private static final class Header {
		final int indent;
		final boolean isClass;

		Header(int indent, boolean isClass) {
			this.indent = indent;
			this.isClass = isClass;
		}
	}

	/** Remembers the blocks opened so far: a line that ends with a colon opens one. */
	private static void trackHeaders(List<Header> headers, String python) {
		if (python.trim().length() == 0)
			return;
		final int indent = leadingSpaces(python);
		while (headers.isEmpty() == false && headers.get(headers.size() - 1).indent >= indent)
			headers.remove(headers.size() - 1);
		if (python.trim().endsWith(":"))
			headers.add(new Header(indent, python.trim().startsWith("class ")));
	}

	private static int leadingSpaces(String s) {
		int n = 0;
		while (n < s.length() && (s.charAt(n) == ' ' || s.charAt(n) == '\t'))
			n++;
		return n;
	}

	/** Opens an interpolation in a text line: <code>!{expr}</code>. */
	static final String OPEN = "!{";

	/** Gives a literal <code>!{</code> in a text line. */
	static final String ESCAPE = "!!{";

	/**
	 * A PlantUML text line inside a Python block, as the Python statement that
	 * emits it: <code>A -&gt; !{b}</code> becomes
	 * <code>puml._text("A -&gt; " + str(b))</code>. <code>!!{</code> gives a literal
	 * <code>!{</code>, as in a text line outside a block. <code>puml._text</code>
	 * expands the macros of the line, then emits it.
	 */
	static String textToPython(String text) {
		// "!!text" is the text "!text", as outside a block
		text = PythonLine.unescapeBang(text);
		final StringBuilder sb = new StringBuilder("puml._text(\"\"");
		final StringBuilder literal = new StringBuilder();
		int i = 0;
		while (i < text.length()) {
			if (text.startsWith(ESCAPE, i)) {
				literal.append(OPEN);
				i += ESCAPE.length();
				continue;
			}
			final int close = text.startsWith(OPEN, i) ? closingBrace(text, i + 2) : -1;
			if (close == -1) {
				literal.append(text.charAt(i));
				i++;
				continue;
			}
			appendLiteral(sb, literal);
			sb.append(" + ").append(interpolationCode(text.substring(i + 2, close)));
			i = close + 1;
		}
		appendLiteral(sb, literal);
		return sb.append(')').toString();
	}

	private static void appendLiteral(StringBuilder sb, StringBuilder literal) {
		if (literal.length() == 0)
			return;
		sb.append(" + \"");
		for (int i = 0; i < literal.length(); i++) {
			final char ch = literal.charAt(i);
			if (ch == '\\' || ch == '"')
				sb.append('\\');
			sb.append(ch);
		}
		sb.append('"');
		literal.setLength(0);
	}

	/**
	 * The Python expression that gives the text of an interpolation, read as a
	 * field of an f-string: <code>expr</code>, <code>expr!r</code>,
	 * <code>expr!s</code>, <code>expr:spec</code> or <code>expr!r:spec</code>.
	 * The conversion and the colon are found at the top level only, outside
	 * brackets and strings (<code>l[1:2]</code>, <code>d["a:b"]</code> and
	 * <code>a != b</code> are expressions), as in CPython, where a lambda must
	 * be parenthesized for the same reason. Nested format specs
	 * (<code>x:{width}</code>) are not supported. A mistake gives a call of
	 * <code>puml.error</code>, reported on the line when it runs.
	 */
	static String interpolationCode(String inside) {
		int depth = 0;
		char quote = 0;
		int end = inside.length();
		for (int i = 0; i < inside.length(); i++) {
			final char ch = inside.charAt(i);
			if (quote != 0) {
				if (ch == '\\')
					i++;
				else if (ch == quote)
					quote = 0;
			} else if (ch == '"' || ch == '\'') {
				quote = ch;
			} else if (ch == '(' || ch == '[' || ch == '{') {
				depth++;
			} else if (ch == ')' || ch == ']' || ch == '}') {
				depth--;
			} else if (depth == 0 && (ch == ':' || (ch == '!' && isNotEqualOperator(inside, i) == false))) {
				end = i;
				break;
			}
		}
		final String expression = inside.substring(0, end);
		if (expression.trim().isEmpty())
			return interpolationError("empty expression in !{}");
		char conversion = 0;
		String spec = null;
		String rest = inside.substring(end);
		if (rest.startsWith("!")) {
			if (rest.length() < 2 || (rest.charAt(1) != 'r' && rest.charAt(1) != 's'))
				return interpolationError("invalid conversion in !{" + inside + "}: use !r or !s");
			conversion = rest.charAt(1);
			rest = rest.substring(2);
			if (rest.isEmpty() == false && rest.startsWith(":") == false)
				return interpolationError("invalid conversion in !{" + inside + "}: use !r or !s");
		}
		if (rest.startsWith(":")) {
			spec = rest.substring(1);
			if (spec.indexOf('{') != -1 || spec.indexOf('}') != -1)
				return interpolationError("nested format specs are not supported in !{" + inside + "}");
		}
		String value = "(" + expression + ")";
		if (conversion == 'r')
			value = "repr(" + value + ")";
		else if (conversion == 's')
			value = "str(" + value + ")";
		if (spec != null)
			return "format(" + value + ", " + pythonString(spec) + ")";
		return conversion == 0 ? "str(" + value + ")" : value;
	}

	private static boolean isNotEqualOperator(String s, int bang) {
		return bang + 1 < s.length() && s.charAt(bang + 1) == '=';
	}

	private static String interpolationError(String message) {
		return "puml.error(" + pythonString(message) + ")";
	}

	/** A Python string literal for a text. */
	private static String pythonString(String text) {
		final StringBuilder sb = new StringBuilder("\"");
		for (int i = 0; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (ch == '\\' || ch == '"')
				sb.append('\\');
			sb.append(ch);
		}
		return sb.append('"').toString();
	}

	/** Index of the "}" that closes a "!{", ignoring braces inside Python strings, or -1. */
	static int closingBrace(String s, int start) {
		int depth = 0;
		char quote = 0;
		for (int i = start; i < s.length(); i++) {
			final char ch = s.charAt(i);
			if (quote != 0) {
				if (ch == '\\')
					i++;
				else if (ch == quote)
					quote = 0;
			} else if (ch == '"' || ch == '\'') {
				quote = ch;
			} else if (ch == '{') {
				depth++;
			} else if (ch == '}') {
				if (depth == 0)
					return i;
				depth--;
			}
		}
		return -1;
	}

	/** A chunk built by {@link #collapse}: its text starts with {@link TLineType#PYTHON_MARKER}. */
	private static boolean isChunk(StringLocated sl) {
		return sl.getString().startsWith(TLineType.PYTHON_MARKER);
	}

	/** A raw "!" line of type Python (a chunk is not one). */
	private static boolean isGutterLine(StringLocated sl) {
		return isChunk(sl) == false && sl.getType() == TLineType.PYTHON;
	}

	/** "!    return x": inside a run, an indented line after the gutter continues the block. */
	private static boolean isIndentedGutterLine(StringLocated sl) {
		final String s = sl.getString();
		if (isChunk(sl) || s.length() < 2 || s.charAt(0) != '!' || s.charAt(1) != ' ')
			return false;
		return sl.getType() == TLineType.PLAIN;
	}

	/**
	 * A line whose first non-blank character is a <code>!</code>: a Python line or
	 * a directive, which ends the text lines of a block. A line that starts with
	 * an interpolation, <code>!{expr}</code>, or with the escape <code>!!</code> is
	 * a text line.
	 */
	private static boolean isBangLine(String text) {
		return text.trim().startsWith("!") && PythonLine.startsWithInterpolation(text) == false
				&& PythonLine.startsWithEscapedBang(text) == false;
	}

	/** Removes the one-column gutter: the text after the first <code>!</code>. */
	private static String gutter(String s) {
		return s.substring(s.indexOf('!') + 1);
	}

	private static StringLocated chunk(StringLocated first, StringBuilder code, List<StringLocated> lines,
			Map<StringLocated, List<StringLocated>> sources) {
		final StringLocated chunk = new StringLocated(TLineType.PYTHON_MARKER + code, first.getLocation(),
				first.getPreprocessorError());
		sources.put(chunk, lines);
		return chunk;
	}

	/** The Python code of a chunk. */
	public static String code(StringLocated chunk) {
		final String s = chunk.getString();
		if (s.startsWith(TLineType.PYTHON_MARKER))
			return s.substring(TLineType.PYTHON_MARKER.length());
		return gutter(s) + "\n";
	}
}
