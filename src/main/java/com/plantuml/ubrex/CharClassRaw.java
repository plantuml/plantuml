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
package com.plantuml.ubrex;

import java.util.Set;

public enum CharClassRaw {

	ANY(".") {
		@Override
		boolean internalMatches(char ch) {
			return true;
		}
	},
	SPACE("s") {
		@Override
		boolean internalMatches(char ch) {
			return ch == ' ';
		}
	},
	GUILLEMET("g") {
		@Override
		boolean internalMatches(char ch) {
			return ch == '\"' || ch == '“' || ch == '”';
		}
	},
	WORD("w") {
		@Override
		boolean internalMatches(char ch) {
			return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || (ch == '_'));
		}
	},
	DIGIT("d") {
		@Override
		boolean internalMatches(char ch) {
			return (ch >= '0' && ch <= '9') || (ch == '_');
		}
	},
	ALPHA_NUMERIC("an") {
		@Override
		boolean internalMatches(char ch) {
			return Character.isLetterOrDigit(ch);
		}
	},
	LETTER("le") {
		@Override
		boolean internalMatches(char ch) {
			return Character.isLetter(ch);
		}
	};

	abstract boolean internalMatches(char ch);

	private final String syntax;

	private CharClassRaw(String syntax) {
		this.syntax = syntax;
	}

	public int getDefinitionLength() {
		return syntax.length();
	}

	public static CharClassRaw fromDefinition(TextNavigator nav) {
		char ch = nav.charAt(0);
		switch (ch) {
		case 'S':
		case 's':
			return CharClassRaw.SPACE;
		case 'G':
		case 'g':
			return CharClassRaw.GUILLEMET;
		case 'D':
		case 'd':
			return CharClassRaw.DIGIT;
		case 'W':
		case 'w':
			return CharClassRaw.WORD;
		case 'A':
		case 'a':
			// Not finished because we don't test the "n"
			return CharClassRaw.ALPHA_NUMERIC;
		case 'L':
		case 'l':
			// Not finished because we don't test the "e"
			return CharClassRaw.LETTER;
		case '.':
			return CharClassRaw.ANY;
		default:
			throw new UnsupportedOperationException("wip01class " + ch);
		}
	}

	public static boolean internalMatchesAny(Set<CharClassRaw> set, char ch) {
		for (CharClassRaw charClass : set)
			if (charClass.internalMatches(ch))
				return true;

		return false;
	}

}
