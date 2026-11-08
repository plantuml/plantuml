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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Tells a legacy preprocessor line from a Python line, by hand (no regex).
 * Every line given here is already known to contain a <code>!</code>; the
 * rules are those of section 11.4 of PYTHON_PREPROCESSOR.md.
 */
public final class PythonLine {

	private PythonLine() {
	}

	/**
	 * The escape of a text line that starts with "!" (section 12.1 of
	 * PYTHON_PREPROCESSOR.md): "!!important" is the text "!important". The first
	 * "!" is removed when the line starts, after its blanks, with "!!" not
	 * followed by "{" ("!!{" is the escape of an interpolation, which gives
	 * "!{" by itself). Any other line is returned as it is.
	 */
	public static String unescapeBang(String s) {
		final int i = skipSpaces(s, 0);
		if (s.startsWith("!!", i) == false || s.startsWith("!!{", i))
			return s;
		return s.substring(0, i) + s.substring(i + 1);
	}

	/** True for a text line that starts with the escape "!!" (see {@link #unescapeBang}). */
	public static boolean startsWithEscapedBang(String s) {
		final int i = skipSpaces(s, 0);
		return s.startsWith("!!", i);
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

	/** "!@macro", "!@macro(\"$color\")": a decorator, on the line before a "!def" or a "!class". */
	public static boolean isDecoratorLine(String s) {
		final int i = afterBang(s);
		return i >= 0 && i < s.length() && s.charAt(i) == '@';
	}

	private static final Set<String> PYTHON_KEYWORDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"def", "for", "try", "pass", "lambda", "del", "raise", "global", "nonlocal", "elif", "except", "break",
			"continue", "class")));

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
		if (PYTHON_KEYWORDS.contains(word))
			return true;
		if (word.equals("if") || word.equals("while") || word.equals("else"))
			return endsWithColon(s);
		return false;
	}

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
	 * The words that are never Python after "!", for {@link #isStatementLine}:
	 * the Python block markers, the legacy preprocessor directives and the
	 * diagram commands that start with "!". A line that starts with one of them
	 * is not taken for Python even when its legacy pattern did not match it (for
	 * instance "!define 4K(...)", whose name starts with a digit, or
	 * "!pragma teoz true", which the diagram reads).
	 */
	private static final Set<String> NOT_PYTHON_WORDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
			"python", "endpy", "define", "definelong", "enddefinelong", "undef", "ifdef", "ifndef", "if", "elseif",
			"else", "endif", "while", "endwhile", "foreach", "endfor", "function", "procedure", "unquoted", "final",
			"end", "endfunction", "endprocedure", "return", "include", "includeurl", "include_many", "include_once",
			"include_sprites", "includedef", "includesub", "import", "startsub", "endsub", "theme", "log",
			"dump_memory", "option", "assert", "local", "pragma", "assume", "exit")));

	/**
	 * The last rule (section 11.4 of PYTHON_PREPROCESSOR.md): a "!" followed,
	 * after optional blanks, by an identifier, a digit, "(" or "[" is Python:
	 * "!x = 1", "!x == 1", "!f(1)", "!1 + 1", "!(a, b) = c", "![1, 2]". With
	 * blanks ("! x = 1") it is Python code indented by the gutter rule, so an
	 * indentation error at the top level. The words of {@link #NOT_PYTHON_WORDS}
	 * are excluded. To be tested after every legacy directive.
	 */
	public static boolean isStatementLine(String s) {
		final int i = afterBang(s);
		if (i < 0 || i >= s.length())
			return false;
		final char c = s.charAt(i);
		if (c == '(' || c == '[' || (c >= '0' && c <= '9'))
			return true;
		if (isIdentifierStart(c) == false)
			return false;
		final String word = s.substring(i, identifierEnd(s, i));
		return NOT_PYTHON_WORDS.contains(word) == false;
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
