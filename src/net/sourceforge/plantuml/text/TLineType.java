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
package net.sourceforge.plantuml.text;

import java.util.regex.Pattern;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, UNDEF, IFNDEF, ELSE, ELSEIF, ENDIF, WHILE, ENDWHILE,
	FOREACH, ENDFOREACH, DECLARE_RETURN_FUNCTION, DECLARE_PROCEDURE, END_FUNCTION, RETURN, LEGACY_DEFINE,
	LEGACY_DEFINELONG, THEME, INCLUDE, INCLUDE_DEF, IMPORT, STARTSUB, ENDSUB, INCLUDESUB, LOG, DUMP_MEMORY,
	COMMENT_SIMPLE, COMMENT_LONG_START;

	private static final Pattern PATTERN_LEGACY_DEFINE = Pattern.compile("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\(.*");

	private static final Pattern PATTERN_LEGACY_DEFINELONG = Pattern
			.compile("^\\s*!definelong\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*");

	private static final Pattern PATTERN_AFFECTATION_DEFINE = Pattern
			.compile("^\\s*!define\\s+[\\p{L}_][\\p{L}_0-9]*\\b.*");

	private static final Pattern PATTERN_AFFECTATION = Pattern
			.compile("^\\s*!\\s*(local|global)?\\s*\\$?[\\p{L}_][\\p{L}_0-9]*\\s*\\??=.*");

	private static final Pattern PATTERN_COMMENT_SIMPLE1 = Pattern.compile("^\\s*'.*");

	private static final Pattern PATTERN_COMMENT_SIMPLE2 = Pattern.compile("^\\s*/'.*'/\\s*$");

	private static final Pattern PATTERN_COMMENT_LONG_START = Pattern.compile("^\\s*/'.*");

	private static final Pattern PATTERN_IFDEF = Pattern.compile("^\\s*!ifdef\\s+.*");

	private static final Pattern PATTERN_UNDEF = Pattern.compile("^\\s*!undef\\s+.*");

	private static final Pattern PATTERN_IFNDEF = Pattern.compile("^\\s*!ifndef\\s+.*");

	private static final Pattern PATTERN_ASSERT = Pattern.compile("^\\s*!assert\\s+.*");

	private static final Pattern PATTERN_IF = Pattern.compile("^\\s*!if\\s+.*");

	private static final Pattern PATTERN_DECLARE_RETURN_FUNCTION = Pattern
			.compile("^\\s*!(unquoted\\s|final\\s)*(function)\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*");

	private static final Pattern PATTERN_DECLARE_PROCEDURE = Pattern
			.compile("^\\s*!(unquoted\\s|final\\s)*(procedure)\\s+\\$?[\\p{L}_][\\p{L}_0-9]*.*");

	private static final Pattern PATTERN_ELSE = Pattern.compile("^\\s*!else\\b.*");

	private static final Pattern PATTERN_ELSEIF = Pattern.compile("^\\s*!elseif\\b.*");

	private static final Pattern PATTERN_ENDIF = Pattern.compile("^\\s*!endif\\b.*");

	private static final Pattern PATTERN_WHILE = Pattern.compile("^\\s*!while\\s+.*");

	private static final Pattern PATTERN_ENDWHILE = Pattern.compile("^\\s*!endwhile\\b.*");

	private static final Pattern PATTERN_FOREACH = Pattern.compile("^\\s*!foreach\\s+.*");

	private static final Pattern PATTERN_ENDFOREACH = Pattern.compile("^\\s*!endfor\\b.*");

	private static final Pattern PATTERN_END_FUNCTION = Pattern
			.compile("^\\s*!(end\\s*function|end\\s*definelong|end\\s*procedure)\\b.*");

	private static final Pattern PATTERN_RETURN = Pattern.compile("^\\s*!return\\b.*");

	private static final Pattern PATTERN_THEME = Pattern.compile("^\\s*!theme\\b.*");

	private static final Pattern PATTERN_INCLUDE = Pattern
			.compile("^\\s*!(include|includeurl|include_many|include_once)\\b.*");

	private static final Pattern PATTERN_INCLUDE_DEF = Pattern.compile("^\\s*!(includedef)\\b.*");

	private static final Pattern PATTERN_IMPORT = Pattern.compile("^\\s*!(import)\\b.*");

	private static final Pattern PATTERN_STARTSUB = Pattern.compile("^\\s*!startsub\\s+.*");

	private static final Pattern PATTERN_ENDSUB = Pattern.compile("^\\s*!endsub\\b.*");

	private static final Pattern PATTERN_INCLUDESUB = Pattern.compile("^\\s*!includesub\\b.*");

	private static final Pattern PATTERN_LOG = Pattern.compile("^\\s*!(log)\\b.*");

	private static final Pattern PATTERN_DUMP_MEMORY = Pattern.compile("^\\s*!(dump_memory)\\b.*");

	public static TLineType getFromLineInternal(String s) {
		if (PATTERN_LEGACY_DEFINE.matcher(s).matches())
			return LEGACY_DEFINE;

		if (PATTERN_LEGACY_DEFINELONG.matcher(s).matches())
			return LEGACY_DEFINELONG;

		if (PATTERN_AFFECTATION_DEFINE.matcher(s).matches())
			return AFFECTATION_DEFINE;

		if (PATTERN_AFFECTATION.matcher(s).matches())
			return AFFECTATION;

		if (PATTERN_COMMENT_SIMPLE1.matcher(s).matches())
			return COMMENT_SIMPLE;

		if (PATTERN_COMMENT_SIMPLE2.matcher(s).matches())
			return COMMENT_SIMPLE;

		if (PATTERN_COMMENT_LONG_START.matcher(s).matches() && s.contains("'/") == false)
			return COMMENT_LONG_START;

		if (PATTERN_IFDEF.matcher(s).matches())
			return IFDEF;

		if (PATTERN_UNDEF.matcher(s).matches())
			return UNDEF;

		if (PATTERN_IFNDEF.matcher(s).matches())
			return IFNDEF;

		if (PATTERN_ASSERT.matcher(s).matches())
			return ASSERT;

		if (PATTERN_IF.matcher(s).matches())
			return IF;

		if (PATTERN_DECLARE_RETURN_FUNCTION.matcher(s).matches())
			return DECLARE_RETURN_FUNCTION;

		if (PATTERN_DECLARE_PROCEDURE.matcher(s).matches())
			return DECLARE_PROCEDURE;

		if (PATTERN_ELSE.matcher(s).matches())
			return ELSE;

		if (PATTERN_ELSEIF.matcher(s).matches())
			return ELSEIF;

		if (PATTERN_ENDIF.matcher(s).matches())
			return ENDIF;

		if (PATTERN_WHILE.matcher(s).matches())
			return WHILE;

		if (PATTERN_ENDWHILE.matcher(s).matches())
			return ENDWHILE;

		if (PATTERN_FOREACH.matcher(s).matches())
			return FOREACH;

		if (PATTERN_ENDFOREACH.matcher(s).matches())
			return ENDFOREACH;

		if (PATTERN_END_FUNCTION.matcher(s).matches())
			return END_FUNCTION;

		if (PATTERN_RETURN.matcher(s).matches())
			return RETURN;

		if (PATTERN_THEME.matcher(s).matches())
			return THEME;

		if (PATTERN_INCLUDE.matcher(s).matches())
			return INCLUDE;

		if (PATTERN_INCLUDE_DEF.matcher(s).matches())
			return INCLUDE_DEF;

		if (PATTERN_IMPORT.matcher(s).matches())
			return IMPORT;

		if (PATTERN_STARTSUB.matcher(s).matches())
			return STARTSUB;

		if (PATTERN_ENDSUB.matcher(s).matches())
			return ENDSUB;

		if (PATTERN_INCLUDESUB.matcher(s).matches())
			return INCLUDESUB;

		if (PATTERN_LOG.matcher(s).matches())
			return LOG;

		if (PATTERN_DUMP_MEMORY.matcher(s).matches())
			return DUMP_MEMORY;

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
