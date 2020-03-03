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
 */
package net.sourceforge.plantuml.tim.expression;

import net.sourceforge.plantuml.json.JsonValue;

public class Token {

	private final String surface;
	private final JsonValue json;
	private final TokenType tokenType;

	@Override
	public String toString() {
		return tokenType + "{" + surface + "}";
	}

	public Token(char surface, TokenType tokenType, JsonValue json) {
		this("" + surface, tokenType, json);
	}

	public Token(String surface, TokenType tokenType, JsonValue json) {
		this.surface = surface;
		this.tokenType = tokenType;
		this.json = json;
	}

	public TokenOperator getTokenOperator() {
		if (this.tokenType != TokenType.OPERATOR) {
			throw new IllegalStateException();
		}
		final char ch2 = surface.length() > 1 ? surface.charAt(1) : 0;
		return TokenOperator.getTokenOperator(surface.charAt(0), ch2);
	}

	public final String getSurface() {
		return surface;
	}

	public final TokenType getTokenType() {
		return tokenType;
	}

	public Token muteToFunction() {
		if (this.tokenType != TokenType.PLAIN_TEXT) {
			throw new IllegalStateException();
		}
		return new Token(surface, TokenType.FUNCTION_NAME, null);
	}

	public JsonValue getJson() {
		if (this.tokenType != TokenType.JSON_DATA) {
			throw new IllegalStateException();
		}
		return json;
	}

}
