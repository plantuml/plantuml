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

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, UNDEF, IFNDEF, ELSE, ELSEIF, ENDIF, WHILE, ENDWHILE,
	FOREACH, ENDFOREACH, DECLARE_RETURN_FUNCTION, DECLARE_PROCEDURE, END_FUNCTION, RETURN, LEGACY_DEFINE, LEGACY_DEFINELONG, 
	THEME, INCLUDE, INCLUDE_DEF,
	IMPORT, STARTSUB, ENDSUB, INCLUDESUB, LOG, DUMP_MEMORY, COMMENT_SIMPLE, COMMENT_LONG_START;

//	private boolean elseLike() {
//		return this == ELSE || this == ELSEIF;
//	}
//
//	public boolean incIndentAfter() {
//		return this == IF || this == IFDEF || this == IFNDEF || elseLike();
//	}
//
//	public boolean decIndentBefore() {
//		return this == ENDIF || elseLike();
//	}

	private static final List<ComplexDirectiveData> COMPLEX_DIRECTIVES;

	private static final Map<String, TLineType> SIMPLE_DIRECTIVE_MAP;

	private static final Pattern SIMPLE_DIRECTIVE_PATTERN;

	// Messy but provides backwards compatibility (visible for testing)
	static final Set<String> REQUIRES_TRAILING_SPACE;

	private static class ComplexDirectiveData {
		final Pattern pattern;
		final TLineType lineType;

		public ComplexDirectiveData(Pattern pattern, TLineType lineType) {
			this.pattern = pattern;
			this.lineType = lineType;
		}
	}

	// Init COMPLEX_DIRECTIVES
	static {
		final Object[] data = new Object[]{
				"\\s*!\\s*(local|global)?\\s*\\$?[\\p{L}_][\\p{L}_0-9]*\\s*\\??=.*", AFFECTATION,

				"\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\(.*", LEGACY_DEFINE,

				"\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*", AFFECTATION_DEFINE,

				"\\s*('.*|/'.*'/\\s*)", COMMENT_SIMPLE,

				"\\s*/'([^']|'(?!/))*", COMMENT_LONG_START,

				"\\s*!(unquoted\\s|final\\s)*(procedure)\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*", DECLARE_PROCEDURE,

				"\\s*!(unquoted\\s|final\\s)*(function)\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*", DECLARE_RETURN_FUNCTION,

				"\\s*!(end\\s*function|end\\s*definelong|end\\s*procedure)\\b.*", END_FUNCTION,

				"\\s*!definelong\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*", LEGACY_DEFINELONG
		};

		final List<ComplexDirectiveData> list = new ArrayList<>();

		for (int i = 0; i < data.length; i += 2) {
			final String pattern = (String) data[i];
			final TLineType lineType = (TLineType) data[i + 1];
			list.add(new ComplexDirectiveData(Pattern.compile(pattern), lineType));
		}

		COMPLEX_DIRECTIVES = unmodifiableList(list);
	}

	// Init SIMPLE_DIRECTIVE_MAP,  SIMPLE_DIRECTIVE_PATTERN, REQUIRES_TRAILING_SPACE
	static {
		final Object[] data = new Object[]{
				"!assert ", ASSERT,
				"!dump_memory", DUMP_MEMORY,
				"!else", ELSE,
				"!elseif", ELSEIF,
				"!endfor", ENDFOREACH,
				"!endif", ENDIF,
				"!endsub", ENDSUB,
				"!endwhile", ENDWHILE,
				"!foreach ", FOREACH,
				"!if ", IF,
				"!ifdef ", IFDEF,
				"!ifndef ", IFNDEF,
				"!import", IMPORT,
				"!include", INCLUDE,
				"!include_many", INCLUDE,
				"!include_once", INCLUDE,
				"!includedef", INCLUDE_DEF,
				"!includesub", INCLUDESUB,
				"!includeurl", INCLUDE,
				"!log", LOG,
				"!return", RETURN,
				"!startsub ", STARTSUB,
				"!theme", THEME,
				"!undef ", UNDEF,
				"!while ", WHILE,
		};

		final Map<String, TLineType> map = new HashMap<>();
		final StringBuilder pattern = new StringBuilder("\\s*(");
		final Set<String> requiresTrailingSpace = new HashSet<>();

		for (int i = 0; i < data.length; i += 2) {
			final String directive = (String) data[i];
			final TLineType lineType = (TLineType) data[i + 1];

			if (i != 0) {
				pattern.append('|');
			}

			pattern.append(directive);
			map.put(directive, lineType);

			if (directive.endsWith(" ")) {
				requiresTrailingSpace.add(directive.trim());
			} else {
				pattern.append("\\b");
			}
		}

		pattern.append(").*");
		REQUIRES_TRAILING_SPACE = unmodifiableSet(requiresTrailingSpace);
		SIMPLE_DIRECTIVE_MAP = unmodifiableMap(map);
		SIMPLE_DIRECTIVE_PATTERN = Pattern.compile(pattern.toString());
	}

	public static TLineType getFromLineInternal(String s) {
		final Matcher matcher = SIMPLE_DIRECTIVE_PATTERN.matcher(s);
		if (matcher.matches()) {
			return requireNonNull(SIMPLE_DIRECTIVE_MAP.get(matcher.group(1)));
		}

		for (ComplexDirectiveData data : COMPLEX_DIRECTIVES) {
			if (data.pattern.matcher(s).matches()) {
				return data.lineType;
			}
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
