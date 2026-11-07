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
package net.sourceforge.plantuml.text;

/**
 * Tells a legacy preprocessor line from a Python line, by hand (no regex).
 * Every line given here is already known to contain a <code>!</code>; the
 * rules are those of section 11.4 of PYTHON_PREPROCESSOR.md.
 */
public final class PythonLine {

	private PythonLine() {
	}

	/**
	 * "!{a} -> !{b}": a line that starts with an interpolation is PlantUML text,
	 * never a directive nor a Python line (a Python statement starting with "{"
	 * would be a dict display thrown away, which has no use).
	 */
	public static boolean startsWithInterpolation(String s) {
		final int i = skipSpaces(s, 0);
		return s.startsWith("!{", i);
	}

	private static final String[] PYTHON_KEYWORDS = { "def", "for", "try", "pass", "lambda", "del", "raise", "global",
			"nonlocal", "elif", "except", "break", "continue" };

	/** "!$x = 1", "!local $x = 1" and "!x ?= 1" are legacy. "!x = 1" is not. */
	public static boolean isLegacyAffectation(String s) {
		int i = afterBang(s);
		if (i < 0)
			return false;
		boolean explicitScope = false;
		final int end = identifierEnd(s, i);
		final String word = s.substring(i, end);
		if (word.equals("local") || word.equals("global")) {
			final int next = skipSpaces(s, end);
			// "!global x" alone is the Python statement, "!global x = 1" would be legacy
			if (next > end && next < s.length() && (s.charAt(next) == '$' || isIdentifierStart(s.charAt(next)))) {
				explicitScope = true;
				i = next;
			}
		}
		final boolean dollar = i < s.length() && s.charAt(i) == '$';
		if (dollar)
			i++;
		final int idEnd = identifierEnd(s, i);
		if (idEnd == i)
			return false;
		i = skipSpaces(s, idEnd);
		if (i >= s.length())
			return false;
		if (s.charAt(i) == '?')
			return i + 1 < s.length() && s.charAt(i + 1) == '=';
		if (s.charAt(i) != '=')
			return false;
		return dollar || explicitScope;
	}

	/**
	 * A word that only exists in Python ("!def", "!for", ...), or a word that
	 * exists in both languages ("!if", "!while", "!else") on a line that ends
	 * with a colon.
	 */
	public static boolean isKeywordLine(String s) {
		final int i = afterBang(s);
		if (i < 0)
			return false;
		final String word = s.substring(i, identifierEnd(s, i));
		for (String k : PYTHON_KEYWORDS)
			if (k.equals(word))
				return true;
		if (word.equals("if") || word.equals("while") || word.equals("else"))
			return endsWithColon(s);
		return false;
	}

	/**
	 * "!x = 1", "!x += 1", "!f(1)", "!a.b(1)", "!a[0] = 1": an identifier
	 * directly after the bang, followed by a call, an attribute, an index or an
	 * assignment. To be tested after every legacy directive.
	 */
	public static boolean isStatementLine(String s) {
		final int i = afterBang(s);
		if (i < 0 || i != s.indexOf('!') + 1)
			return false;
		final int end = identifierEnd(s, i);
		if (end == i)
			return false;
		final char c = end < s.length() ? s.charAt(end) : '\0';
		if (c == '(' || c == '.' || c == '[')
			return true;
		return isAssignmentAt(s, skipSpaces(s, end));
	}

	/** "=" (not "=="), or an augmented assignment: "+=", "//=", "<<=", ... */
	private static boolean isAssignmentAt(String s, int i) {
		if (i >= s.length())
			return false;
		final char c = s.charAt(i);
		if (c == '=')
			return i + 1 >= s.length() || s.charAt(i + 1) != '=';
		int op = 0;
		if (c == '*' || c == '/' || c == '<' || c == '>') {
			// doubled operators: "**=", "//=", "<<=", ">>="
			if (i + 1 < s.length() && s.charAt(i + 1) == c)
				op = 2;
			else if (c == '*' || c == '/')
				op = 1;
		} else if ("+-%&|^".indexOf(c) != -1) {
			op = 1;
		}
		return op > 0 && i + op < s.length() && s.charAt(i + op) == '=';
	}

	private static boolean endsWithColon(String s) {
		int e = s.length();
		while (e > 0 && Character.isWhitespace(s.charAt(e - 1)))
			e--;
		return e > 0 && s.charAt(e - 1) == ':';
	}

	/** Index of the first character after "!" (blanks before and after skipped), or -1. */
	private static int afterBang(String s) {
		int i = skipSpaces(s, 0);
		if (i >= s.length() || s.charAt(i) != '!')
			return -1;
		return skipSpaces(s, i + 1);
	}

	private static int skipSpaces(String s, int i) {
		while (i < s.length() && Character.isWhitespace(s.charAt(i)))
			i++;
		return i;
	}

	private static boolean isIdentifierStart(char ch) {
		return Character.isLetter(ch) || ch == '_' || Character.isSurrogate(ch);
	}

	private static int identifierEnd(String s, int i) {
		if (i >= s.length() || isIdentifierStart(s.charAt(i)) == false)
			return i;
		while (i < s.length() && TLineType.isLetterOrEmojiOrUnderscoreOrDigit(s.charAt(i)))
			i++;
		return i;
	}
}
