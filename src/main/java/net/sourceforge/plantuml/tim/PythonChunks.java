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
				i++;
				while (i < body.size() && END.matcher(body.get(i).getString()).matches() == false) {
					code.append(body.get(i).getString()).append('\n');
					lines.add(body.get(i));
					i++;
				}
				i++; // skips !endpy (a missing one ends the block at the end of the text)
				result.add(chunk(first, code, lines, sources));
			} else if (isGutterLine(first)) {
				final StringBuilder code = new StringBuilder();
				final List<StringLocated> lines = new ArrayList<>();
				// indentation of the lines that opened a block (they end with a colon)
				final List<Integer> headers = new ArrayList<>();
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
					while (headers.isEmpty() == false && headers.get(headers.size() - 1) >= indent)
						headers.remove(headers.size() - 1);
					if (headers.isEmpty() || isBangLine(text))
						break;
					if (body.get(j).getType() != TLineType.COMMENT_SIMPLE) {
						for (int k = 0; k < indent; k++)
							code.append(' ');
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

	/** Remembers the blocks opened so far: a line that ends with a colon opens one. */
	private static void trackHeaders(List<Integer> headers, String python) {
		if (python.trim().length() == 0)
			return;
		final int indent = leadingSpaces(python);
		while (headers.isEmpty() == false && headers.get(headers.size() - 1) >= indent)
			headers.remove(headers.size() - 1);
		if (python.trim().endsWith(":"))
			headers.add(indent);
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
			sb.append(" + str(").append(text, i + 2, close).append(')');
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
	 * an interpolation, <code>!{expr}</code>, is a text line.
	 */
	private static boolean isBangLine(String text) {
		return text.trim().startsWith("!") && PythonLine.startsWithInterpolation(text) == false;
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
