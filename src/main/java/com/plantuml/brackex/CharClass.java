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
package com.plantuml.brackex;

import java.util.Set;

public enum CharClass {

	ANY(".") {
		@Override
		boolean matches(char ch) {
			return true;
		}
	},
	SPACE("s") {
		@Override
		boolean matches(char ch) {
			return ch == ' ';
		}
	},
	GUILLEMET("g") {
		@Override
		boolean matches(char ch) {
			return ch == '\"' || ch == '“' || ch == '”';
		}
	},
	WORD("w") {
		@Override
		boolean matches(char ch) {
			return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || (ch == '_'));
		}
	},
	DIGIT("d") {
		@Override
		boolean matches(char ch) {
			return (ch >= '0' && ch <= '9') || (ch == '_');
		}
	},
	ALPHA_NUMERIC("an") {
		@Override
		boolean matches(char ch) {
			throw new UnsupportedOperationException();
		}
	},
	LETTER("le") {
		@Override
		boolean matches(char ch) {
			throw new UnsupportedOperationException();
		}
	};

	abstract boolean matches(char ch);

	private final String syntax;

	private CharClass(String syntax) {
		this.syntax = syntax;
	}

	public int getDefinitionLength() {
		return syntax.length();
	}

	public static CharClass fromDefinition(TextNavigator nav) {
		char ch = nav.charAt(0);
		switch (ch) {
		case 'S':
		case 's':
			return CharClass.SPACE;
		case 'G':
		case 'g':
			return CharClass.GUILLEMET;
		case 'D':
		case 'd':
			return CharClass.DIGIT;
		case 'W':
		case 'w':
			return CharClass.WORD;
		case '.':
			return CharClass.ANY;
		default:
			throw new UnsupportedOperationException("wip01class " + ch);
		}
	}

	public static boolean matchesAny(Set<CharClass> set, char ch) {
		for (CharClass charClass : set)
			if (charClass.matches(ch))
				return true;

		return false;
	}

}
