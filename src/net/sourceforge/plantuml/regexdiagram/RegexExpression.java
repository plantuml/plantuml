/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.regexdiagram;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.utils.CharInspector;

public class RegexExpression {
	// ::remove folder when __HAXE__

	public static List<ReToken> parse(CharInspector it) throws RegexParsingException {
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
				it.jump();
				it.jump();
			} else if (current == '|') {
				result.add(new ReToken(ReTokenType.ALTERNATIVE, "|"));
				it.jump();
			} else if (isStartPosixGroup(it)) {
				final String s = readGroupPosix(it);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (current == '[') {
				final String s = readGroup(it);
				result.add(new ReToken(ReTokenType.GROUP, s));
			} else if (isStartComment(it)) {
				skipComment(it);
			} else if (isStartLookAhead(it)) {
				final ReToken token = readLookAhead(it);
				result.add(token);
				result.add(new ReToken(ReTokenType.PARENTHESIS_OPEN, "("));
			} else if (isStartLookBehind(it)) {
				final ReToken token = readLookBehind(it);
				result.add(token);
				result.add(new ReToken(ReTokenType.PARENTHESIS_OPEN, "("));
			} else if (isStartNamedCapturingGroup(it)) {
				final ReToken token = readNamedGroup(it);
				result.add(token);
				result.add(new ReToken(ReTokenType.PARENTHESIS_OPEN, "("));
			} else if (isStartOpenParenthesis(it)) {
				final ReToken token = readOpenParenthesis(it);
				result.add(token);
			} else if (current == ')') {
				result.add(new ReToken(ReTokenType.PARENTHESIS_CLOSE, ")"));
				it.jump();
			} else if (isStartQuantifier(it)) {
				final String s = readQuantifier(it);
				result.add(new ReToken(ReTokenType.QUANTIFIER, s));
			} else if (isStartOctalEscape(it)) {
				final String s = readUnicodeOrOctalEscape(it, 4);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (isStartUnicodeEscape(it)) {
				final String s = readUnicodeOrOctalEscape(it, 5);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (isStartUnicodeClass(it)) {
				final String s = readUnicodeClass(it);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (isStartClass(it)) {
				final String s = readClass(it);
				result.add(new ReToken(ReTokenType.CLASS, s));
			} else if (isSimpleLetter(current)) {
				result.add(new ReToken(ReTokenType.SIMPLE_CHAR, "" + current));
				it.jump();
			} else {
				throw new IllegalStateException();
			}
		}
		// System.err.println("result=" + result);
		return result;

	}

	private static boolean isStartLookAhead(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '(' && it.peek(1) == '?' && (it.peek(2) == '=' || it.peek(2) == '!'))
			return true;
		return false;
	}

	private static boolean isStartLookBehind(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '(' && it.peek(1) == '?' && it.peek(2) == '<' && (it.peek(3) == '=' || it.peek(3) == '!'))
			return true;
		return false;
	}

	private static boolean isStartOpenParenthesis(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '(')
			return true;
		return false;
	}

	private static boolean isStartPosixGroup(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '[' && it.peek(1) == '[' && it.peek(2) == ':')
			return true;
		return false;
	}

	private static boolean isStartNamedCapturingGroup(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '(' && it.peek(1) == '?' && it.peek(2) == '<') {
			int i = 3;
			while (it.peek(i) != 0) {
				if (it.peek(i) == '>' && i == 3)
					return false;
				if (it.peek(i) == '>')
					return true;
				if (Character.isLetter(it.peek(i)) == false)
					return false;
				i++;
			}

		}
		return false;
	}

	private static boolean isStartComment(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '(' && it.peek(1) == '?' && it.peek(2) == '#')
			return true;
		return false;
	}

	private static void skipComment(CharInspector it) throws RegexParsingException {
		it.jump();
		it.jump();
		it.jump();
		final StringBuilder comment = new StringBuilder();
		while (true) {
			if (it.peek(0) == 0)
				throw new RegexParsingException("Unclosed comment");
			if (it.peek(0) == ')') {
				it.jump();
				return;
			}
			comment.append(it.peek(0));
			it.jump();
		}
	}

	private static ReToken readLookAhead(CharInspector it) throws RegexParsingException {
		it.jump();
		it.jump();
		final char ch = it.peek(0);
		it.jump();
		return new ReToken(ReTokenType.LOOK_AHEAD, "?" + ch);
	}

	private static ReToken readLookBehind(CharInspector it) throws RegexParsingException {
		it.jump();
		it.jump();
		it.jump();
		final char ch = it.peek(0);
		it.jump();
		return new ReToken(ReTokenType.LOOK_BEHIND, "?<" + ch);
	}

	private static ReToken readNamedGroup(CharInspector it) throws RegexParsingException {
		it.jump();
		it.jump();
		it.jump();
		final StringBuilder namedGroup = new StringBuilder();
		while (true) {
			if (it.peek(0) == 0)
				throw new RegexParsingException("Unclosed named capturing group");
			if (it.peek(0) == '>') {
				it.jump();
				return new ReToken(ReTokenType.NAMED_GROUP, namedGroup.toString());
			}
			namedGroup.append(it.peek(0));
			it.jump();
		}
	}

	private static ReToken readOpenParenthesis(CharInspector it) {
		final char current0 = it.peek(0);
		it.jump();
		final StringBuilder result = new StringBuilder();
		result.append(current0);
		if (it.peek(0) == '?' && it.peek(1) == ':') {
			it.jump();
			it.jump();
			result.append("?:");
		}
		if (it.peek(0) == '?' && it.peek(1) == '!') {
			it.jump();
			it.jump();
			result.append("?!");
		}
		return new ReToken(ReTokenType.PARENTHESIS_OPEN, result.toString());
	}

	private static boolean isStartQuantifier(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '*' || current0 == '+' || current0 == '?' || current0 == '{')
			return true;
		return false;
	}

	private static String readQuantifier(CharInspector it) throws RegexParsingException {
		final char current0 = it.peek(0);
		it.jump();
		final StringBuilder tmp = new StringBuilder();
		tmp.append(current0);
		if (current0 == '{')
			while (it.peek(0) != 0) {
				final char ch = it.peek(0);
				tmp.append(ch);
				it.jump();
				if (ch == '}')
					break;
			}
		if (it.peek(0) == '?') {
			tmp.append('?');
			it.jump();
		}
		// System.err.println("RESULT=" + tmp);
		final String result = tmp.toString();
		if (result.startsWith("{") && result.matches("^\\{[0-9,]+\\}$") == false)
			throw new RegexParsingException("Bad quantifier " + result);
		return result;
	}

	private static boolean isEscapedChar(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '\\') {
			final char current1 = it.peek(1);
			if (current1 == '.' || current1 == '*' || current1 == '\\' || current1 == '?' || current1 == '^'
					|| current1 == '$' || current1 == '|' || current1 == '(' || current1 == ')' || current1 == '['
					|| current1 == ']' || current1 == '{' || current1 == '}' || current1 == '<' || current1 == '>')
				return true;
		}
		return false;
	}

	private static String readGroupPosix(CharInspector it) {
		it.jump();
		it.jump();
		it.jump();
		final StringBuilder result = new StringBuilder(":");
		while (it.peek(0) != 0) {
			char ch = it.peek(0);
			it.jump();
			result.append(ch);
			if (ch == ':')
				break;
		}
		it.jump();
		it.jump();
		return result.toString();
	}

	private static String readGroup(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 != '[')
			throw new IllegalStateException();
		it.jump();
		final StringBuilder result = new StringBuilder();
		while (it.peek(0) != 0) {
			char ch = it.peek(0);
			it.jump();
			if (ch == ']')
				break;
			result.append(ch);
			if (ch == '\\') {
				ch = it.peek(0);
				it.jump();
				result.append(ch);
			}

		}
		return result.toString();
	}

	private static String readUnicodeClass(CharInspector it) throws RegexParsingException {
		final char current0 = it.peek(0);
		if (current0 != '\\')
			throw new IllegalStateException();
		it.jump();
		final StringBuilder result = new StringBuilder();
		result.append(current0);
		while (it.peek(0) != 0) {
			final char ch = it.peek(0);
			it.jump();
			result.append(ch);
			if (ch == '}')
				return result.toString();
		}
		throw new RegexParsingException("Unexpected end of data");
	}

	private static String readUnicodeOrOctalEscape(CharInspector it, int nb) throws RegexParsingException {
		final char current0 = it.peek(0);
		if (current0 != '\\')
			throw new IllegalStateException();
		it.jump();
		final StringBuilder result = new StringBuilder();
		result.append(current0);
		for (int i = 0; i < nb; i++) {
			final char ch = it.peek(0);
			if (ch == 0)
				throw new RegexParsingException("Unexpected end of data");
			result.append(ch);
			it.jump();
		}
		return result.toString();
	}

	private static String readClass(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '.') {
			it.jump();
			return "" + current0;
		}
		if (current0 == '\\') {
			it.jump();
			final String result = "" + current0 + it.peek(0);
			it.jump();
			return result;
		}
		throw new IllegalStateException();
	}

	private static boolean isStartClass(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '.')
			return true;
		if (current0 == '\\')
			return true;
		return false;
	}

	private static boolean isStartUnicodeClass(CharInspector it) {
		if (it.peek(0) == '\\' && it.peek(1) == 'p' && it.peek(2) == '{')
			return true;
		if (it.peek(0) == '\\' && it.peek(1) == 'x' && it.peek(2) == '{')
			return true;
		return false;
	}

	private static boolean isStartUnicodeEscape(CharInspector it) {
		if (it.peek(0) == '\\' && it.peek(1) == 'u')
			return true;
		return false;
	}

	private static boolean isStartOctalEscape(CharInspector it) {
		if (it.peek(0) == '\\' && it.peek(1) == '0')
			return true;
		return false;
	}

	private static boolean isSimpleLetter(char ch) {
		if (ch == '\\' || ch == '.')
			return false;
		return true;
	}

	private static boolean isStartAnchor(CharInspector it) {
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

	private static String readAnchor(CharInspector it) {
		final char current0 = it.peek(0);
		if (current0 == '^' || current0 == '$') {
			it.jump();
			return "" + current0;
		}
		if (current0 == '\\') {
			it.jump();
			final String result = "" + current0 + it.peek(0);
			it.jump();
			return result;
		}
		throw new IllegalStateException();
	}

}
