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
 *
 */
package net.sourceforge.plantuml.ngdot.parser;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ngdot.parser.DotToken.IdKind;

/**
 * Splits a DOT text into tokens, following the lexical rules of Graphviz
 * ({@code lib/cgraph/scan.l}):
 * <ul>
 * <li>keywords ({@code strict graph digraph node edge subgraph}) are
 * case-insensitive;</li>
 * <li>an identifier is a letter, an underscore or any character above 127,
 * followed by the same or digits;</li>
 * <li>a number is {@code -?(.[0-9]+|[0-9]+(.[0-9]*)?)}; a number directly
 * followed by a letter is split in two tokens, as Graphviz does;</li>
 * <li>in a double-quoted string, only {@code \"} and an escaped newline are
 * resolved, every other backslash is kept for the label escapes;</li>
 * <li>an HTML string is delimited by balanced {@code <} and {@code >};</li>
 * <li>comments are {@code //...}, {@code /*...*&#47;} and lines starting with
 * {@code #}.</li>
 * </ul>
 */
public final class DotLexer {

	private final String text;
	private int pos;
	private int line;
	private int lineStart;
	private boolean onlySpacesSinceLineStart = true;

	private DotLexer(String text) {
		this.text = text;
	}

	public static List<DotToken> tokenize(String text) throws DotSyntaxException {
		return new DotLexer(text).run();
	}

	private List<DotToken> run() throws DotSyntaxException {
		final List<DotToken> result = new ArrayList<DotToken>();
		while (true) {
			skipSpacesAndComments();
			if (pos >= text.length()) {
				result.add(new DotToken(DotTokenType.EOF, "", null, line, column()));
				return result;
			}
			result.add(nextToken());
			onlySpacesSinceLineStart = false;
		}
	}

	private int column() {
		return pos - lineStart;
	}

	private char peek(int offset) {
		final int i = pos + offset;
		if (i < text.length())
			return text.charAt(i);
		return '\0';
	}

	private void advance() {
		if (text.charAt(pos) == '\n') {
			line++;
			lineStart = pos + 1;
			onlySpacesSinceLineStart = true;
		}
		pos++;
	}

	private void skipSpacesAndComments() throws DotSyntaxException {
		while (pos < text.length()) {
			final char c = text.charAt(pos);
			if (c == '\n' || Character.isWhitespace(c)) {
				advance();
			} else if (c == '/' && peek(1) == '/') {
				skipToEndOfLine();
			} else if (c == '#' && onlySpacesSinceLineStart) {
				skipToEndOfLine();
			} else if (c == '/' && peek(1) == '*') {
				final int startLine = line;
				final int startColumn = column();
				advance();
				advance();
				while (true) {
					if (pos >= text.length())
						throw new DotSyntaxException("Unclosed comment", startLine, startColumn);
					if (text.charAt(pos) == '*' && peek(1) == '/') {
						advance();
						advance();
						break;
					}
					advance();
				}
			} else {
				return;
			}
		}
	}

	private void skipToEndOfLine() {
		while (pos < text.length() && text.charAt(pos) != '\n')
			advance();
	}

	private DotToken nextToken() throws DotSyntaxException {
		final int startLine = line;
		final int startColumn = column();
		final char c = text.charAt(pos);

		switch (c) {
		case '{':
			return punctuation(DotTokenType.LBRACE, 1);
		case '}':
			return punctuation(DotTokenType.RBRACE, 1);
		case '[':
			return punctuation(DotTokenType.LBRACKET, 1);
		case ']':
			return punctuation(DotTokenType.RBRACKET, 1);
		case '=':
			return punctuation(DotTokenType.EQUAL, 1);
		case ';':
			return punctuation(DotTokenType.SEMICOLON, 1);
		case ',':
			return punctuation(DotTokenType.COMMA, 1);
		case ':':
			return punctuation(DotTokenType.COLON, 1);
		case '+':
			return punctuation(DotTokenType.PLUS, 1);
		case '"':
			return quotedString(startLine, startColumn);
		case '<':
			return htmlString(startLine, startColumn);
		}

		if (c == '-' && peek(1) == '>')
			return punctuation(DotTokenType.ARROW, 2);
		if (c == '-' && peek(1) == '-')
			return punctuation(DotTokenType.LINE, 2);
		if (isNumberStart())
			return number(startLine, startColumn);
		if (isIdStart(c))
			return identifier(startLine, startColumn);

		throw new DotSyntaxException("Unexpected character '" + c + "'", startLine, startColumn);
	}

	private DotToken punctuation(DotTokenType type, int length) {
		final DotToken result = new DotToken(type, text.substring(pos, pos + length), null, line, column());
		for (int i = 0; i < length; i++)
			advance();
		return result;
	}

	private static boolean isIdStart(char c) {
		return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c >= 128;
	}

	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isNumberStart() {
		int i = 0;
		if (peek(0) == '-')
			i++;
		if (isDigit(peek(i)))
			return true;
		return peek(i) == '.' && isDigit(peek(i + 1));
	}

	private DotToken number(int startLine, int startColumn) {
		final int start = pos;
		if (peek(0) == '-')
			advance();
		while (isDigit(peek(0)))
			advance();
		if (peek(0) == '.') {
			advance();
			while (isDigit(peek(0)))
				advance();
		}
		return new DotToken(DotTokenType.ID, text.substring(start, pos), IdKind.NUMBER, startLine, startColumn);
	}

	private DotToken identifier(int startLine, int startColumn) {
		final int start = pos;
		while (pos < text.length() && (isIdStart(text.charAt(pos)) || isDigit(text.charAt(pos))))
			advance();
		final String word = text.substring(start, pos);
		final DotTokenType keyword = keyword(word);
		if (keyword != null)
			return new DotToken(keyword, word, null, startLine, startColumn);
		return new DotToken(DotTokenType.ID, word, IdKind.PLAIN, startLine, startColumn);
	}

	private static DotTokenType keyword(String word) {
		if (word.equalsIgnoreCase("strict"))
			return DotTokenType.STRICT;
		if (word.equalsIgnoreCase("graph"))
			return DotTokenType.GRAPH;
		if (word.equalsIgnoreCase("digraph"))
			return DotTokenType.DIGRAPH;
		if (word.equalsIgnoreCase("node"))
			return DotTokenType.NODE;
		if (word.equalsIgnoreCase("edge"))
			return DotTokenType.EDGE;
		if (word.equalsIgnoreCase("subgraph"))
			return DotTokenType.SUBGRAPH;
		return null;
	}

	private DotToken quotedString(int startLine, int startColumn) throws DotSyntaxException {
		advance();
		final StringBuilder sb = new StringBuilder();
		while (true) {
			if (pos >= text.length())
				throw new DotSyntaxException("Unclosed string", startLine, startColumn);
			final char c = text.charAt(pos);
			if (c == '"') {
				advance();
				return new DotToken(DotTokenType.ID, sb.toString(), IdKind.QUOTED, startLine, startColumn);
			}
			if (c == '\\') {
				final char next = peek(1);
				if (next == '"') {
					sb.append('"');
					advance();
					advance();
				} else if (next == '\\') {
					sb.append("\\\\");
					advance();
					advance();
				} else if (next == '\n') {
					advance();
					advance();
				} else if (next == '\r' && peek(2) == '\n') {
					advance();
					advance();
					advance();
				} else {
					sb.append(c);
					advance();
				}
			} else {
				sb.append(c);
				advance();
			}
		}
	}

	private DotToken htmlString(int startLine, int startColumn) throws DotSyntaxException {
		advance();
		final StringBuilder sb = new StringBuilder();
		int depth = 1;
		while (true) {
			if (pos >= text.length())
				throw new DotSyntaxException("Unclosed HTML string", startLine, startColumn);
			final char c = text.charAt(pos);
			if (c == '<') {
				depth++;
			} else if (c == '>') {
				depth--;
				if (depth == 0) {
					advance();
					return new DotToken(DotTokenType.ID, sb.toString(), IdKind.HTML, startLine, startColumn);
				}
			}
			sb.append(c);
			advance();
		}
	}

}
