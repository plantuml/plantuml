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
package net.sourceforge.plantuml.logo;

import java.util.HashMap;
import java.util.Map;

class LogoScanner {
	// ::remove folder when CORE
	final private Map<String, Integer> keywordTable = new HashMap<String, Integer>();
	private char sourceString[];
	private int sourceLength;
	private int i;

	public LogoScanner() {
		keywordTable.put("forward", LogoToken.FORWARD);
		keywordTable.put("fd", LogoToken.FORWARD);
		keywordTable.put("back", LogoToken.BACK);
		keywordTable.put("bk", LogoToken.BACK);
		keywordTable.put("right", LogoToken.RIGHT);
		keywordTable.put("rt", LogoToken.RIGHT);
		keywordTable.put("left", LogoToken.LEFT);
		keywordTable.put("lt", LogoToken.LEFT);
		keywordTable.put("penup", LogoToken.PENUP);
		keywordTable.put("pu", LogoToken.PENUP);
		keywordTable.put("pendown", LogoToken.PENDOWN);
		keywordTable.put("pd", LogoToken.PENDOWN);
		keywordTable.put("hideturtle", LogoToken.HIDETURTLE);
		keywordTable.put("ht", LogoToken.HIDETURTLE);
		keywordTable.put("showturtle", LogoToken.SHOWTURTLE);
		keywordTable.put("st", LogoToken.SHOWTURTLE);
		keywordTable.put("clearscreen", LogoToken.CLEARSCREEN);
		keywordTable.put("cs", LogoToken.CLEARSCREEN);
		keywordTable.put("repeat", LogoToken.REPEAT);
		keywordTable.put("rep", LogoToken.REPEAT);
		keywordTable.put("to", LogoToken.TO);
		keywordTable.put("setpc", LogoToken.SETPC);
		keywordTable.put("pc", LogoToken.SETPC);
	}

	public int getPosition() {
		return i;
	}

	public void setPosition(int newPosition) {
		if (i >= 0 && i <= sourceLength) {
			i = newPosition;
		} else {
			i = sourceLength;
		}
	}

	public void setSourceString(String newSourceString) {
		sourceLength = newSourceString.length();
		sourceString = newSourceString.concat("\0").toCharArray();
		i = 0;
	}

	public String getSourceString() {
		return new String(sourceString);
	}

	public String getRestAsString() {
		skipWhitespace();
		final String rest = new String(sourceString, i, sourceLength - i + 1);
		i = sourceLength;
		return rest;
	}

	void skipWhitespace() {
		char c;
		do {
			c = sourceString[i++];
		} while (c == ' ' || c == '\t');
		i--;
	}

	public LogoToken getToken() {
		final LogoToken token = new LogoToken();
		final StringBuilder lexeme = new StringBuilder();

		if (i >= sourceLength) {
			token.kind = LogoToken.END_OF_INPUT;
			return token;
		}

		// Skip whitespace.
		skipWhitespace();
		char c = sourceString[i++];

		// Now figure out what kind of token we've got.
		if (c == '[' || c == ']') {
			token.kind = c;
			token.lexeme = String.valueOf(c);
		} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
			do {
				lexeme.append(c);
				c = sourceString[i++];
			} while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
			i--;
			token.lexeme = lexeme.toString();
			token.kind = LogoToken.IDENTIFIER;
			final Integer keyword = keywordTable.get(token.lexeme);
			if (keyword != null) {
				token.kind = keyword;
			}
		} else if (c >= '0' && c <= '9') {
			do {
				lexeme.append(c);
				c = sourceString[i++];
			} while (c >= '0' && c <= '9');
			boolean hasDecimalPart = false;
			if (c == '.') {
				do {
					lexeme.append(c);
					c = sourceString[i++];
				} while (c >= '0' && c <= '9');
				hasDecimalPart = true;
			}
			i--;
			token.lexeme = lexeme.toString();
			token.value = Float.parseFloat(token.lexeme);
			if (hasDecimalPart) {
				token.kind = LogoToken.FLOAT;
			} else {
				token.kind = LogoToken.INTEGER;
				token.intValue = Integer.parseInt(token.lexeme);
			}
		} else if (c == 0) {
			i--;
			token.kind = LogoToken.END_OF_INPUT;
		} else {
			i--;
			token.kind = LogoToken.INVALID_TOKEN;
		}

		return token;
	}
}
