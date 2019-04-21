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
package net.sourceforge.plantuml.tim;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, IFNDEF, ELSE, ENDIF, DECLARE_FUNCTION, END_FUNCTION, RETURN, LEGACY_DEFINE, LEGACY_DEFINELONG, INCLUDE, IMPORT;

	public static TLineType getFromLine(String s) {
		if (s.matches("^!define\\s+[\\p{L}_][\\p{L}_0-9]*\\(.*")) {
			return LEGACY_DEFINE;
		}
		if (s.matches("^!definelong\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return LEGACY_DEFINELONG;
		}
		if (s.matches("^!define\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*")) {
			return AFFECTATION_DEFINE;
		}
		if (s.matches("^!\\s*\\$?[\\p{L}_][\\p{L}_0-9]*\\s*=.*")) {
			return AFFECTATION;
		}
		if (s.matches("^!ifdef\\s+.*")) {
			return IFDEF;
		}
		if (s.matches("^!ifndef\\s+.*")) {
			return IFNDEF;
		}
		if (s.matches("^!assert\\s+.*")) {
			return ASSERT;
		}
		if (s.matches("^!if\\s+.*")) {
			return IF;
		}
		if (s.matches("^!(unquoted\\s)?function\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*")) {
			return DECLARE_FUNCTION;
		}
		if (s.matches("^!else\\b.*")) {
			return ELSE;
		}
		if (s.matches("^!endif\\b.*")) {
			return ENDIF;
		}
		if (s.matches("^!(endfunction|enddefinelong)\\b.*")) {
			return END_FUNCTION;
		}
		if (s.matches("^!return\\b.*")) {
			return RETURN;
		}
		if (s.matches("^!(include|includeurl)\\b.*")) {
			return INCLUDE;
		}
		if (s.matches("^!(import)\\b.*")) {
			return IMPORT;
		}
		return PLAIN;
	}

	public static boolean isQuote(char ch) {
		return ch == '\"' || ch == '\'';
	}

	public static boolean isLetterOrUnderscoreOrDigit(char ch) {
		return isLetterOrUnderscore(ch) || isLatinDigit(ch);
	}

	public static boolean isLetterOrUnderscore(char ch) {
		return isLetter(ch) || ch == '_';
	}

	public static boolean isLetterOrUnderscoreOrDollar(char ch) {
		return isLetterOrUnderscore(ch) || ch == '$';
	}

	public static boolean isLetterOrDigit(char ch) {
		return isLetter(ch) || isLatinDigit(ch);
	}

	public static boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

	public static boolean isSpaceChar(char ch) {
		return Character.isSpaceChar(ch);
	}

	public static boolean isLatinDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

}
