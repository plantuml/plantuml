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

import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;

public enum TLineType {

	PLAIN, AFFECTATION_DEFINE, AFFECTATION, ASSERT, IF, IFDEF, UNDEF, IFNDEF, ELSE, ELSEIF, ENDIF, WHILE, ENDWHILE,
	FOREACH, ENDFOREACH, DECLARE_RETURN_FUNCTION, DECLARE_PROCEDURE, END_FUNCTION, RETURN, LEGACY_DEFINE,
	LEGACY_DEFINELONG, THEME, INCLUDE, /*INCLUDE_SPRITES,*/ INCLUDE_DEF, IMPORT, STARTSUB, ENDSUB, INCLUDESUB, LOG,
	DUMP_MEMORY, COMMENT_SIMPLE, COMMENT_LONG_START, OPTION;

	private static final RegexLeaf IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT = new RegexLeaf(
			"[\\p{L}\\uD800-\\uDBFF\\uDC00-\\uDFFF_][\\p{L}\\uD800-\\uDBFF\\uDC00-\\uDFFF_0-9]*");

	private static RegexConcat simpleKeyword(String word) {
		return new RegexConcat(RegexLeaf.start(), RegexLeaf.spaceZeroOrMore(), new RegexLeaf(word),
				new RegexLeaf("\\b"));
	}

	private static final IRegex PATTERN_LEGACY_DEFINE = new RegexConcat(RegexLeaf.start(), RegexLeaf.spaceZeroOrMore(),
			new RegexLeaf("!define"), RegexLeaf.spaceOneOrMore(), IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT,
			new RegexLeaf("\\("));

	private static final IRegex PATTERN_LEGACY_DEFINELONG = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("!definelong"), RegexLeaf.spaceOneOrMore(),
			IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT, new RegexLeaf("\\b"));

	private static final IRegex PATTERN_AFFECTATION_DEFINE = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("!define"), RegexLeaf.spaceOneOrMore(),
			IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT, new RegexLeaf("\\b"));

	private static final IRegex PATTERN_AFFECTATION = new RegexConcat(RegexLeaf.start(), RegexLeaf.spaceZeroOrMore(),
			new RegexLeaf("!"), RegexLeaf.spaceZeroOrMore(), new RegexLeaf(1, "(local|global)?"),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\$?"), IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT,
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("\\??="));

	private static final IRegex PATTERN_COMMENT_SIMPLE1 = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("'"));

	private static final IRegex PATTERN_COMMENT_SIMPLE2 = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("/'"), new RegexLeaf(".*"), new RegexLeaf("'/"),
			RegexLeaf.spaceZeroOrMore(), RegexLeaf.end());

	private static final IRegex PATTERN_COMMENT_LONG_START = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("/'"));

	private static final IRegex PATTERN_IFDEF = simpleKeyword("!ifdef");

	private static final IRegex PATTERN_UNDEF = simpleKeyword("!undef");

	private static final IRegex PATTERN_IFNDEF = simpleKeyword("!ifndef");

	private static final IRegex PATTERN_ASSERT = simpleKeyword("!assert");

	private static final IRegex PATTERN_IF = simpleKeyword("!if");

	private static final IRegex PATTERN_DECLARE_RETURN_FUNCTION = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("!"), new RegexLeaf(1, "(unquoted\\s|final\\s)*"),
			new RegexLeaf("function"), RegexLeaf.spaceOneOrMore(), new RegexLeaf("\\$?"),
			IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT);

	private static final IRegex PATTERN_DECLARE_PROCEDURE = new RegexConcat(RegexLeaf.start(),
			RegexLeaf.spaceZeroOrMore(), new RegexLeaf("!"), new RegexLeaf(1, "(unquoted\\s|final\\s)*"),
			new RegexLeaf("procedure"), RegexLeaf.spaceOneOrMore(), new RegexLeaf("\\$?"),
			IDENTIFIER_WITH_UNICODE_SURROGATES_SUPPORT);

	private static final IRegex PATTERN_ELSE = simpleKeyword("!else");

	private static final IRegex PATTERN_ELSEIF = simpleKeyword("!elseif");

	private static final IRegex PATTERN_ENDIF = simpleKeyword("!endif");

	private static final IRegex PATTERN_WHILE = simpleKeyword("!while");

	private static final IRegex PATTERN_ENDWHILE = simpleKeyword("!endwhile");

	private static final IRegex PATTERN_FOREACH = simpleKeyword("!foreach");

	private static final IRegex PATTERN_ENDFOREACH = simpleKeyword("!endfor");

	private static final IRegex PATTERN_END_FUNCTION = new RegexConcat(RegexLeaf.start(), RegexLeaf.spaceZeroOrMore(),
			new RegexLeaf("!end"), RegexLeaf.spaceZeroOrMore(),
			new RegexOr(new RegexLeaf("function"), new RegexLeaf("definelong"), new RegexLeaf("procedure")),
			new RegexLeaf("\\b"));

	private static final IRegex PATTERN_RETURN = simpleKeyword("!return");

	private static final IRegex PATTERN_THEME = simpleKeyword("!theme");

	private static final IRegex PATTERN_INCLUDE = new RegexConcat(RegexLeaf.start(), RegexLeaf.spaceZeroOrMore(),
			new RegexLeaf("!include"), RegexLeaf.spaceZeroOrMore(),
			new RegexOptional(new RegexOr(new RegexLeaf("url"), new RegexLeaf("_many"), new RegexLeaf("_once"))),
			new RegexLeaf("\\b"));

	// private static final IRegex PATTERN_INCLUDE_SPRITES = simpleKeyword("!include_sprites");

	private static final IRegex PATTERN_INCLUDE_DEF = simpleKeyword("!includedef");

	private static final IRegex PATTERN_IMPORT = simpleKeyword("!import");

	private static final IRegex PATTERN_STARTSUB = simpleKeyword("!startsub");

	private static final IRegex PATTERN_ENDSUB = simpleKeyword("!endsub");

	private static final IRegex PATTERN_INCLUDESUB = simpleKeyword("!includesub");

	private static final IRegex PATTERN_LOG = simpleKeyword("!log");

	private static final IRegex PATTERN_DUMP_MEMORY = simpleKeyword("!dump_memory");

	private static final IRegex PATTERN_OPTION = simpleKeyword("!option");

	public static TLineType getFromLineInternal(StringLocated sl) {
		final String s = sl.getString();

		if (PATTERN_COMMENT_SIMPLE1.match(sl))
			return COMMENT_SIMPLE;

		if (PATTERN_COMMENT_SIMPLE2.match(sl))
			return COMMENT_SIMPLE;

		if (PATTERN_COMMENT_LONG_START.match(sl) && s.contains("'/") == false)
			return COMMENT_LONG_START;

		if (sl.containsExclamationMark() == false)
			return PLAIN;

		if (PATTERN_LEGACY_DEFINE.match(sl))
			return LEGACY_DEFINE;

		if (PATTERN_LEGACY_DEFINELONG.match(sl))
			return LEGACY_DEFINELONG;

		if (PATTERN_AFFECTATION_DEFINE.match(sl))
			return AFFECTATION_DEFINE;

		if (PATTERN_AFFECTATION.match(sl))
			return AFFECTATION;

		if (PATTERN_IFDEF.match(sl))
			return IFDEF;

		if (PATTERN_UNDEF.match(sl))
			return UNDEF;

		if (PATTERN_IFNDEF.match(sl))
			return IFNDEF;

		if (PATTERN_ASSERT.match(sl))
			return ASSERT;

		if (PATTERN_IF.match(sl))
			return IF;

		if (PATTERN_DECLARE_RETURN_FUNCTION.match(sl))
			return DECLARE_RETURN_FUNCTION;

		if (PATTERN_DECLARE_PROCEDURE.match(sl))
			return DECLARE_PROCEDURE;

		if (PATTERN_ELSE.match(sl))
			return ELSE;

		if (PATTERN_ELSEIF.match(sl))
			return ELSEIF;

		if (PATTERN_ENDIF.match(sl))
			return ENDIF;

		if (PATTERN_WHILE.match(sl))
			return WHILE;

		if (PATTERN_ENDWHILE.match(sl))
			return ENDWHILE;

		if (PATTERN_FOREACH.match(sl))
			return FOREACH;

		if (PATTERN_ENDFOREACH.match(sl))
			return ENDFOREACH;

		if (PATTERN_END_FUNCTION.match(sl))
			return END_FUNCTION;

		if (PATTERN_RETURN.match(sl))
			return RETURN;

		if (PATTERN_THEME.match(sl))
			return THEME;

		if (PATTERN_INCLUDE.match(sl))
			return INCLUDE;

//		if (PATTERN_INCLUDE_SPRITES.match(sl))
//			return INCLUDE_SPRITES;

		if (PATTERN_INCLUDE_DEF.match(sl))
			return INCLUDE_DEF;

		if (PATTERN_IMPORT.match(sl))
			return IMPORT;

		if (PATTERN_STARTSUB.match(sl))
			return STARTSUB;

		if (PATTERN_ENDSUB.match(sl))
			return ENDSUB;

		if (PATTERN_INCLUDESUB.match(sl))
			return INCLUDESUB;

		if (PATTERN_LOG.match(sl))
			return LOG;

		if (PATTERN_DUMP_MEMORY.match(sl))
			return DUMP_MEMORY;

		if (PATTERN_OPTION.match(sl))
			return OPTION;

		return PLAIN;
	}

	public static boolean isQuote(char ch) {
		return ch == '\"' || ch == '\'';
	}

	public static boolean isSpaceChar(char ch) {
		return Character.isSpaceChar(ch);
	}

	public static boolean isLatinDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	public static boolean isLetterOrEmojiOrUnderscoreOrDigit(char ch) {
		return isLetterOrUnderscore(ch) || isLatinDigit(ch) || isEmoji(ch);
	}

	public static boolean isLetterOrEmojiOrUnderscoreOrDollar(char ch) {
		return isLetterOrUnderscore(ch) || ch == '$' || isEmoji(ch);
	}

	private static boolean isLetterOrUnderscore(char ch) {
		return isLetter(ch) || ch == '_';
	}

	private static boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

	private static boolean isEmoji(char ch) {
		return Character.isLowSurrogate(ch) || Character.isHighSurrogate(ch);
	}

}
