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
 *
 */
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.asciiart.Wcwidth;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.cucadiagram.Display;

// Do not move
public class StringUtils {

	public static final char USER_NEWLINE = '\uEE00';
	public static final char USER_TAB = '\uEE01';

	public static final char HR_SIMPLE = '\uEEFF';
	public static final char HR_DOUBLE = '\uEEFE';
	public static final char HR_DOTTED = '\uEEFD';
	public static final char HR_BOLD = '\uEEFC';

	public static final char PRIVATE_FIELD = '\uEEFB';
	public static final char PROTECTED_FIELD = '\uEEFA';
	public static final char PACKAGE_PRIVATE_FIELD = '\uEEF9';
	public static final char PUBLIC_FIELD = '\uEEF8';
	public static final char PRIVATE_METHOD = '\uEEF7';
	public static final char PROTECTED_METHOD = '\uEEF6';
	public static final char PACKAGE_PRIVATE_METHOD = '\uEEF5';
	public static final char PUBLIC_METHOD = '\uEEF4';
	public static final char IE_MANDATORY = '\uEEF3';

	public static final char BOLD_START = '\uEEF2';
	public static final char BOLD_END = '\uEEF1';

	// Used in BackSlash
	public static final char PRIVATE_BLOCK = '\uE000';

	public static final char INTERNAL_BOLD = '\uE100';

	public static String toInternalBoldNumber(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c >= '0' && c <= '9') {
				sb.append(Character.toChars('\uE100' + c - '0'));
			} else {
				sb.append(c);
			}

		}
		return sb.toString();
	}

	public static void appendInternalToRealBoldNumber(StringBuilder sb, char c) {
		if (c >= '\uE100' && c <= ('\uE100' + 9)) {
			sb.append(Character.toChars(0x1d7ce + c - '\uE100'));
		} else {
			sb.append(c);
		}
	}

	public static void appendInternalToPlainNumber(StringBuilder sb, char c) {
		if (c >= '\uE100' && c <= ('\uE100' + 9)) {
			sb.append(Character.toChars('0' + c - '\uE100'));
		} else {
			sb.append(c);
		}
	}

	final static public List<String> getSplit(Pattern2 pattern, String line) {
		final Matcher2 m = pattern.matcher(line);
		if (m.find() == false) {
			return null;
		}
		final List<String> result = new ArrayList<>();
		for (int i = 1; i <= m.groupCount(); i++) {
			result.add(m.group(i));
		}
		return result;
	}

	public static boolean isNotEmpty(CharSequence s) {
		return !isEmpty(s);
	}

	public static boolean isNotEmpty(List<? extends CharSequence> input) {
		return input != null && input.size() > 0;
	}

	public static boolean isEmpty(CharSequence s) {
		if (s == null) return true;
		final int length = s.length();
		if (length == 0) return true;
		for (int i = 0; i < length; i++) {
			if (!isSpaceOrTabOrNull(s.charAt(i))) return false;
		}
		return true;
	}

	public static String manageHtml(String s) {
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

	public static String unicode(String s) {
		final StringBuilder result = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c > 127 || c == '&' || c == '|') {
				final int i = c;
				result.append("&#" + i + ";");
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public static String unicodeForHtml(String s) {
		final StringBuilder result = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (c > 127 || c == '&' || c == '|' || c == '<' || c == '>') {
				final int i = c;
				result.append("&#" + i + ";");
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public static String unicodeForHtml(Display display) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < display.size(); i++) {
			result.append(unicodeForHtml(display.get(i).toString()));
			if (i < display.size() - 1) {
				result.append("<br>");
			}
		}
		return result.toString();
	}

	public static String manageArrowForSequence(String s) {
		s = s.replace('=', '-').toLowerCase();
		return s;
	}

	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public static String goUpperCase(String s) {
		return s.toUpperCase(Locale.ENGLISH);
	}

	public static char goUpperCase(char c) {
		return goUpperCase("" + c).charAt(0);
	}

	public static String goLowerCase(String s) {
		return s.toLowerCase(Locale.ENGLISH);
	}

	public static char goLowerCase(char c) {
		return goLowerCase("" + c).charAt(0);
	}

	public static String manageArrowForCuca(String s) {
		final Direction dir = getArrowDirection(s);
		s = s.replace('=', '-');
		s = s.replaceAll("\\w*", "");
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			s = s.replaceAll("-+", "-");
		}
		if (s.length() == 2 && (dir == Direction.UP || dir == Direction.DOWN)) {
			s = s.replaceFirst("-", "--");
		}
		return s;
	}

	public static String manageQueueForCuca(String s) {
		final Direction dir = getQueueDirection(s);
		s = s.replace('=', '-');
		s = s.replaceAll("\\w*", "");
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			s = s.replaceAll("-+", "-");
		}
		if (s.length() == 1 && (dir == Direction.UP || dir == Direction.DOWN)) {
			s = s.replaceFirst("-", "--");
		}
		return s;
	}

	public static Direction getArrowDirection(String s) {
		if (s.endsWith(">")) {
			return getQueueDirection(s.substring(0, s.length() - 1));
		}
		if (s.startsWith("<")) {
			if (s.length() == 2) {
				return Direction.LEFT;
			}
			return Direction.UP;
		}
		throw new IllegalArgumentException(s);
	}

	public static Direction getQueueDirection(String s) {
		if (s.indexOf('<') != -1 || s.indexOf('>') != -1) {
			throw new IllegalArgumentException(s);
		}
		s = s.toLowerCase();
		if (s.contains("left")) {
			return Direction.LEFT;
		}
		if (s.contains("right")) {
			return Direction.RIGHT;
		}
		if (s.contains("up")) {
			return Direction.UP;
		}
		if (s.contains("down")) {
			return Direction.DOWN;
		}
		if (s.contains("l")) {
			return Direction.LEFT;
		}
		if (s.contains("r")) {
			return Direction.RIGHT;
		}
		if (s.contains("u")) {
			return Direction.UP;
		}
		if (s.contains("d")) {
			return Direction.DOWN;
		}
		if (s.length() == 1) {
			return Direction.RIGHT;
		}
		return Direction.DOWN;
	}

	// public static Code eventuallyRemoveStartingAndEndingDoubleQuote(Code s) {
	// return Code.of(eventuallyRemoveStartingAndEndingDoubleQuote(s.getCode()));
	// }

	public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s, String format) {
		if (s == null) {
			return null;
		}
		if (format.contains("\"") && s.length() > 1 && isDoubleQuote(s.charAt(0))
				&& isDoubleQuote(s.charAt(s.length() - 1))) {
			return s.substring(1, s.length() - 1);
		}
		if (format.contains("(") && s.startsWith("(") && s.endsWith(")")) {
			return s.substring(1, s.length() - 1);
		}
		if (format.contains("[") && s.startsWith("[") && s.endsWith("]")) {
			return s.substring(1, s.length() - 1);
		}
		if (format.contains(":") && s.startsWith(":") && s.endsWith(":")) {
			return s.substring(1, s.length() - 1);
		}
		return s;
	}

	public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s) {
		if (s == null) {
			return s;
		}
		return eventuallyRemoveStartingAndEndingDoubleQuote(s, "\"([:");
	}

	private static boolean isDoubleQuote(char c) {
		return c == '\"' || c == '\u201c' || c == '\u201d' || c == '\u00ab' || c == '\u00bb';
	}

	public static boolean isCJK(char c) {
		final Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		Log.println("block=" + block);
		return false;
	}

	public static char hiddenLesserThan() {
		return '\u0005';
	}

	public static char hiddenBiggerThan() {
		return '\u0006';
	}

	public static String hideComparatorCharacters(String s) {
		s = s.replace('<', hiddenLesserThan());
		s = s.replace('>', hiddenBiggerThan());
		return s;
	}

	public static String showComparatorCharacters(String s) {
		s = s.replace(hiddenLesserThan(), '<');
		s = s.replace(hiddenBiggerThan(), '>');
		return s;
	}

	private static int getWidth(Display stringsToDisplay) {
		int result = 1;
		for (CharSequence s : stringsToDisplay) {
			if (s != null && result < s.length()) {
				result = s.length();
			}
		}
		return result;
	}

	public static int getWcWidth(Display stringsToDisplay) {
		int result = 1;
		for (CharSequence s : stringsToDisplay) {
			if (s == null) {
				continue;
			}
			final int length = Wcwidth.length(s);
			if (result < length) {
				result = length;
			}
		}
		return result;
	}

	public static int getHeight(List<? extends CharSequence> stringsToDisplay) {
		return stringsToDisplay.size();
	}

	public static int getHeight(Display stringsToDisplay) {
		return stringsToDisplay.size();
	}

	public static boolean isDiagramCacheable(String uml) {
		if (uml.length() < 35) {
			return false;
		}
//		uml = uml.toLowerCase();
//		if (uml.startsWith("@startuml\nversion\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\nlicense\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\nlicence\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\nauthor\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\ndonors\n")) {
//			return false;
//		}
////		if (uml.startsWith("@startuml\ncheckversion")) {
////			return false;
////		}
//		if (uml.startsWith("@startuml\ntestdot\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\nsudoku\n")) {
//			return false;
//		}
//		if (uml.startsWith("@startuml\nstdlib\n")) {
//			return false;
//		}
		return true;
	}

	public static int getPragmaRevision(String uml) {
		uml = uml.toLowerCase();
		final String header = "@startuml\n!pragma revision ";
		if (uml.startsWith(header) == false) {
			return -1;
		}
		int x1 = header.length();
		int x2 = x1;
		while (x2 < uml.length() && Character.isDigit(uml.charAt(x2))) {
			x2++;
		}
		if (x1 == x2) {
			return -1;
		}
		return Integer.parseInt(uml.substring(x1, x2));
	}

	public static List<String> splitComma(String s) {
		s = trin(s);
		final List<String> result = new ArrayList<>();
		final Pattern2 p = MyPattern.cmpile("([%pLN_.]+|[%g][^%g]+[%g])");
		final Matcher2 m = p.matcher(s);
		while (m.find()) {
			result.add(eventuallyRemoveStartingAndEndingDoubleQuote(m.group(0)));
		}
		return Collections.unmodifiableList(result);
	}

	public static String getUid(String uid1, int uid2) {
		return uid1 + String.format("%04d", uid2);
	}

	public static <O> List<O> merge(List<O> l1, List<O> l2) {
		final List<O> result = new ArrayList<>(l1);
		result.addAll(l2);
		return Collections.unmodifiableList(result);
	}

	public static boolean endsWithBackslash(final String s) {
		return s.endsWith("\\") && s.endsWith("\\\\") == false;
	}

	public static String rot(String s) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'm') || (c >= 'A' && c <= 'M')) {
				c += 13;
			} else if ((c >= 'n' && c <= 'z') || (c >= 'N' && c <= 'Z')) {
				c -= 13;
			} else if (c > 126) {
				throw new IllegalArgumentException(s);
			}
			sb.append(c);
		}
		return sb.toString();
	}

	public static String manageUnicodeNotationUplus(String s) {
		final Pattern pattern = Pattern.compile("\\<U\\+([0-9a-fA-F]{4,5})\\>");
		final Matcher matcher = pattern.matcher(s);
		final StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			final String num = matcher.group(1);
			final int value = Integer.parseInt(num, 16);
			final String replace = new String(Character.toChars(value));
			matcher.appendReplacement(result, Matcher.quoteReplacement(replace));
		}
		matcher.appendTail(result);
		return result.toString();
	}

	public static String manageAmpDiese(String s) {
		final Pattern pattern = Pattern.compile("\\&#([0-9]+);");
		final Matcher matcher = pattern.matcher(s);
		final StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			final String num = matcher.group(1);
			final char c = (char) Integer.parseInt(num);
			matcher.appendReplacement(result, "" + c);
		}
		matcher.appendTail(result);
		return result.toString();
	}

	public static String manageTildeArobaseStart(String s) {
		s = s.replaceAll("~@start", "@start");
		return s;
	}

	public static String trinNoTrace(CharSequence s) {
		return s.toString().trim();
	}

	public static String trin(String arg) {
		if (arg.length() == 0) {
			return arg;
		}
		return trinEndingInternal(arg, getPositionStartNonSpace(arg));
	}

	private static int getPositionStartNonSpace(String arg) {
		int i = 0;
		while (i < arg.length() && isSpaceOrTabOrNull(arg.charAt(i))) {
			i++;
		}
		return i;
	}

	private static String trinEnding(String arg) {
		if (arg.length() == 0) {
			return arg;
		}
		return trinEndingInternal(arg, 0);
	}

	private static String trinEndingInternal(String arg, int from) {
		int j = arg.length() - 1;
		while (j >= from && isSpaceOrTabOrNull(arg.charAt(j))) {
			j--;
		}
		if (from == 0 && j == arg.length() - 1) {
			return arg;
		}
		return arg.substring(from, j + 1);
	}

	private static boolean isSpaceOrTabOrNull(char c) {
		return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\0';
	}

	public static String manageEscapedTabs(String s) {
		return s.replace("\\t", "\t");
	}

	public static long seed(String string) {
		long h = 1125899906842597L; // prime
		final int len = string.length();

		for (int i = 0; i < len; i++) {
			h = 31 * h + string.charAt(i);
		}
		return h;
	}

	// http://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
}
