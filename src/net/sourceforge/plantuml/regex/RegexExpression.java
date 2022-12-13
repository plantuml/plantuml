/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.regex;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ebnf.CharIterator;

public class RegexExpression {

	public static List<ReToken> parse(CharIterator it) {
		final List<ReToken> result = new ArrayList<>();
		while (true) {
			final char current = it.peek(0);
			if (current == '\0')
				break;
			// System.err.println("current=" + current);
			if (isStartAnchor(it)) {
				final String s = readAnchor(it);
				result.add(new ReToken(ReTokenType.ANCHOR, s));
			} else if (isEscapedChar(it)) {
				result.add(new ReToken(ReTokenType.ESCAPED_CHAR, "" + it.peek(1)));
				it.next();
				it.next();
			} else if (current == '|') {
				result.add(new ReToken(ReTokenType.ALTERNATIVE, "|"));
				it.next();
			} else if (current == '[') {
				final String s = readGroup(it);
				result.add(new ReToken(ReTokenType.GROUP, s));
			} else if (isStartOpenParenthesis(it)) {
				final String s = readOpenParenthesis(it);
				result.add(new ReToken(ReTokenType.PARENTHESIS_OPEN, s));
			} else if (current == ')') {
				result.add(new ReToken(ReTokenType.PARENTHESIS_CLOSE, ")"));
				it.next();
			} else if (isStartQuantifier(it)) {
				final String s = readQuantifier(it);
				result.add(new ReToken(ReTokenType.QUANTIFIER, s));
			} else if (isStartClass(it)) {
				final String s = readClass(it);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (isSimpleLetter(current)) {
				result.add(new ReToken(ReTokenType.SIMPLE_CHAR, "" + current));
				it.next();
			} else {
				throw new IllegalStateException();
			}
		}
		// System.err.println("result=" + result);
		return result;

	}

	private static boolean isStartOpenParenthesis(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '(')
			return true;
		return false;
	}

	private static String readOpenParenthesis(CharIterator it) {
		final char current0 = it.peek(0);
		it.next();
		final StringBuilder result = new StringBuilder();
		result.append(current0);
		return result.toString();
	}

	private static boolean isStartQuantifier(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '*' || current0 == '+' || current0 == '?' || current0 == '{')
			return true;
		return false;
	}

	private static String readQuantifier(CharIterator it) {
		final char current0 = it.peek(0);
		it.next();
		final StringBuilder result = new StringBuilder();
		result.append(current0);
		if (current0 == '{')
			while (it.peek(0) != 0) {
				final char ch = it.peek(0);
				result.append(ch);
				it.next();
				if (ch == '}')
					break;
			}
		if (it.peek(0) == '?') {
			result.append('?');
			it.next();
		}
		return result.toString();
	}

	private static boolean isEscapedChar(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '\\') {
			final char current1 = it.peek(1);
			if (current1 == '.' || current1 == '*' || current1 == '\\' || current1 == '?' || current1 == '^'
					|| current1 == '$' || current1 == '|' || current1 == '(' || current1 == ')' || current1 == '['
					|| current1 == ']' || current1 == '{' || current1 == '}')
				return true;
		}
		return false;
	}

	private static String readGroup(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 != '[')
			throw new IllegalStateException();
		it.next();
		final StringBuilder result = new StringBuilder();
		while (it.peek(0) != 0) {
			char ch = it.peek(0);
			it.next();
			if (ch == ']')
				break;
			result.append(ch);
			if (ch == '\\') {
				ch = it.peek(0);
				it.next();
				result.append(ch);
			}

		}
		return result.toString();
	}

	private static String readClass(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '.') {
			it.next();
			return "" + current0;
		}
		if (current0 == '\\') {
			it.next();
			final String result = "" + current0 + it.peek(0);
			it.next();
			return result;
		}
		throw new IllegalStateException();
	}

	private static boolean isStartClass(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '.')
			return true;
		if (current0 == '\\')
			return true;
		return false;
	}

	private static boolean isSimpleLetter(char ch) {
		if (ch == '\\' || ch == '.')
			return false;
		return true;
	}

	private static boolean isStartAnchor(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '^' || current0 == '$')
			return true;
		if (current0 == '\\') {
			final char current1 = it.peek(1);
			if (current1 == 'A' || current1 == 'Z' || current1 == 'z' || current1 == 'G' || current1 == 'b'
					|| current1 == 'B')
				return true;
		}
		return false;
	}

	private static String readAnchor(CharIterator it) {
		final char current0 = it.peek(0);
		if (current0 == '^' || current0 == '$') {
			it.next();
			return "" + current0;
		}
		if (current0 == '\\') {
			it.next();
			final String result = "" + current0 + it.peek(0);
			it.next();
			return result;
		}
		throw new IllegalStateException();
	}

}
