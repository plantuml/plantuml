/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.style.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.style.AutomaticCounter;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleScheme;
import net.sourceforge.plantuml.style.Value;
import net.sourceforge.plantuml.style.ValueImpl;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.CharInspector;
import net.sourceforge.plantuml.utils.Inspector;
import net.sourceforge.plantuml.utils.InspectorUtils;

public class StyleParser {

	public static Collection<Style> parse(BlocLines lines, AutomaticCounter counter) throws StyleParsingException {

		if (lines.size() == 0)
			return Collections.emptyList();

		final List<StyleToken> tokens = parse(lines.inspectorWithNewlines());

		final List<Style> result = new ArrayList<>();
		final CssVariables variables = new CssVariables();
		StyleScheme scheme = StyleScheme.REGULAR;

		Context context = new Context();

//		System.err.println("tokens=" + tokens.size());
//		if (tokens.size() < 100)
//			for (StyleToken t : tokens)
//				System.err.println(t);

		for (Inspector<StyleToken> ins = InspectorUtils.inspector(tokens); ins.peek(0) != null;) {
			final StyleToken token = ins.peek(0);
			ins.jump();
			if (token.getType() == StyleTokenType.NEWLINE)
				continue;
			if (token.getType() == StyleTokenType.SEMICOLON)
				continue;

			if (ins.peek(0).getType() == StyleTokenType.COMMA) {
				final String full = token.getData() + readWithComma(ins);
				skipNewLines(ins);
				if (ins.peek(0).getType() == StyleTokenType.OPEN_BRACKET) {
					context = context.push(full);
					ins.jump();
					continue;
				}
				throw new IllegalStateException();
			}
			if (token.getType() == StyleTokenType.STRING) {
				String full = token.getData();
				if (ins.peek(0).getType() == StyleTokenType.STAR) {
					ins.jump();
					full += "*";
				}
				skipNewLines(ins);
				if (ins.peek(0).getType() == StyleTokenType.OPEN_BRACKET) {
					context = context.push(full);
					ins.jump();
					continue;
				}
				skipColon(ins);
				if (token.getData().startsWith("--")) {
					variables.learn(token.getData(), readValue(ins));
				} else if (ins.peek(0).getType() == StyleTokenType.STRING) {
					final String valueString = variables.value(readValue(ins));
					final String keyString = token.getData();
					final PName key = PName.getFromName(keyString, scheme);
					if (key == null) {
						// System.err.println("Error with key " + keyString);
					} else {
						final Value value = scheme == StyleScheme.REGULAR ? //
								ValueImpl.regular(valueString, counter) : ValueImpl.dark(valueString, counter);
						context.putInContext(key, value);
					}
				} else {
					throw new StyleParsingException("parsing");
				}

			} else if (token.getType() == StyleTokenType.CLOSE_BRACKET) {
				for (Style st : context.toStyles())
					result.add(st);
				if (context.size() > 0)
					context = context.pop();

			} else if (token.getType() == StyleTokenType.AROBASE_MEDIA) {
				scheme = StyleScheme.DARK;
				continue;
			} else if (token.getType() == StyleTokenType.COLON && ins.peek(0).getType() == StyleTokenType.STRING
					&& ins.peek(1).getType() == StyleTokenType.OPEN_BRACKET) {
				final String full = token.getData() + ins.peek(0).getData();
				context = context.push(full);
				ins.jump();
				ins.jump();
				continue;
			} else if (token.getType() == StyleTokenType.COLON && ins.peek(0).getType() == StyleTokenType.STRING
					&& ins.peek(1).getType() == StyleTokenType.STAR
					&& ins.peek(2).getType() == StyleTokenType.OPEN_BRACKET) {
				final String full = token.getData() + ins.peek(0).getData() + ins.peek(1).getData();
				context = context.push(full);
				ins.jump();
				ins.jump();
				ins.jump();
				continue;
			} else if (token.getType() == StyleTokenType.OPEN_BRACKET) {
				throw new StyleParsingException("Invalid open bracket");
			} else {
				throw new IllegalStateException(token.toString());
			}
		}
		return Collections.unmodifiableList(result);
	}

	private static String readWithComma(Inspector<StyleToken> ins) {
		final StringBuilder result = new StringBuilder();
		while (ins.peek(0) != null) {
			final StyleToken current = ins.peek(0);
			if (current.getType() != StyleTokenType.STRING && current.getType() != StyleTokenType.COMMA)
				return result.toString();
			result.append(current.getData());
			ins.jump();
		}
		return result.toString();
	}

	private static String readValue(Inspector<StyleToken> ins) throws StyleParsingException {
		final StringBuilder result = new StringBuilder();
		while (ins.peek(0) != null) {
			final StyleToken current = ins.peek(0);
			if (current.getType() == StyleTokenType.NEWLINE || current.getType() == StyleTokenType.SEMICOLON
					|| current.getType() == StyleTokenType.CLOSE_BRACKET)
				return result.toString();

			if (current.getType() == StyleTokenType.STRING) {
				if (result.length() > 0)
					result.append(' ');
				result.append(current.getData());
				ins.jump();
			} else if (current.getType() == StyleTokenType.COLON) {
				result.append(current.getData());
				ins.jump();
				if (ins.peek(0).getType() == StyleTokenType.STRING) {
					result.append(ins.peek(0).getData());
					ins.jump();
				} else
					throw new StyleParsingException("bad definition");
			} else
				throw new StyleParsingException("bad definition");

		}
		return result.toString();
	}

	private static void skipNewLines(Inspector<StyleToken> ins) {
		while (true) {
			final StyleToken token = ins.peek(0);
			if (token == null || token.getType() != StyleTokenType.NEWLINE)
				return;
			ins.jump();
		}
	}

	private static void skipColon(Inspector<StyleToken> ins) {
		while (true) {
			final StyleToken token = ins.peek(0);
			if (token == null || token.getType() != StyleTokenType.COLON)
				return;
			ins.jump();
		}
	}

	private static List<StyleToken> parse(CharInspector ins) throws StyleParsingException {
		final List<StyleToken> result = new ArrayList<>();
		while (true) {
			final char current = ins.peek(0);
			if (current == '\0')
				break;
			// System.err.println("current=" + current);
			if (current == ' ' || current == '\t') {
				ins.jump();
				// Skipping
			} else if (current == '/' && ins.peek(1) == '/') {
				jumpUntil(ins, '\n');
			} else if (current == '/' && ins.peek(1) == '\'') {
				jumpUntil(ins, '\'', '/');
			} else if (current == '/' && ins.peek(1) == '*') {
				jumpUntil(ins, '*', '/');
			} else if (current == ',') {
				result.add(new StyleToken(StyleTokenType.COMMA, ","));
				ins.jump();
			} else if (current == ';') {
				result.add(new StyleToken(StyleTokenType.SEMICOLON, ";"));
				ins.jump();
			} else if (current == '\n' || current == '\r') {
				result.add(new StyleToken(StyleTokenType.NEWLINE, "NEWLINE"));
				ins.jump();
			} else if (current == '*') {
				result.add(new StyleToken(StyleTokenType.STAR, "*"));
				ins.jump();
			} else if (current == ':') {
				result.add(new StyleToken(StyleTokenType.COLON, ":"));
				ins.jump();
			} else if (current == '{') {
				result.add(new StyleToken(StyleTokenType.OPEN_BRACKET, "{"));
				ins.jump();
			} else if (current == '}') {
				result.add(new StyleToken(StyleTokenType.CLOSE_BRACKET, "}"));
				ins.jump();
			} else if (current == '@') {
				result.add(new StyleToken(StyleTokenType.AROBASE_MEDIA, readArobaseMedia(ins)));
			} else if (current == '\"') {
				final String s = readQuotedString(ins);
				result.add(new StyleToken(StyleTokenType.STRING, s));
			} else {
				final String s = readString(ins);
				if (s.startsWith("<"))
					throw new StyleParsingException("Cannot understand <");
				result.add(new StyleToken(StyleTokenType.STRING, s));
			}
		}
		return result;

	}

	private static void jumpUntil(CharInspector ins, char ch1) {
		while (ins.peek(0) != 0) {
			if (ins.peek(0) == ch1) {
				ins.jump();
				return;
			}
			ins.jump();
		}
	}

	private static void jumpUntil(CharInspector ins, char ch1, char ch2) {
		while (ins.peek(0) != 0) {
			if (ins.peek(0) == ch1 && ins.peek(1) == ch2) {
				ins.jump();
				ins.jump();
				return;
			}
			ins.jump();
		}
	}

	private static String readArobaseMedia(CharInspector ins) {
		final char current0 = ins.peek(0);
		if (current0 != '@')
			throw new IllegalStateException();
		ins.jump();
		final StringBuilder result = new StringBuilder();
		while (ins.peek(0) != 0) {
			char ch = ins.peek(0);
			ins.jump();
			if (ch == '{' || ch == '}' || ch == ';')
				break;
			result.append(ch);
		}
		return result.toString();
	}

	private static String readQuotedString(CharInspector ins) {
		final StringBuilder result = new StringBuilder();
		if (ins.peek(0) != '\"')
			throw new IllegalStateException();
		ins.jump();
		while (ins.peek(0) != 0 && ins.peek(0) != '\"') {
			char ch = ins.peek(0);
			ins.jump();
			result.append(ch);
		}
		if (ins.peek(0) == '\"')
			ins.jump();
		return result.toString();
	}

	private static String readString(CharInspector ins) {
		final StringBuilder result = new StringBuilder();
		while (ins.peek(0) != 0) {
			char ch = ins.peek(0);
			if (ch == '\n' || ch == '\r')
				break;
			if (ch == ' ' && result.charAt(0) != '.')
				break;
			if (ch == '{' || ch == '}' || ch == ';' || ch == ',' || ch == ':' || ch == '\t')
				break;
			ins.jump();
			// System.err.println("ch=" + ch);
			result.append(ch);
		}
		if (result.charAt(0) == '.')
			return result.toString().trim();
		return result.toString();
	}

}