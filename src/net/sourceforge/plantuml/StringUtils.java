/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 13958 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorTransparent;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.ugraphic.ColorMapper;

// Do not move
public class StringUtils {

	public static String getPlateformDependentAbsolutePath(File file) {
		return file.getAbsolutePath();
	}

	public static List<String> getWithNewlines2(Code s) {
		return getWithNewlines2(s.getFullName());
	}

	public static List<String> getWithNewlines2(String s) {
		if (s == null) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1) {
				final char c2 = s.charAt(i + 1);
				i++;
				if (c2 == 'n') {
					result.add(current.toString());
					current.setLength(0);
				} else if (c2 == 't') {
					current.append('\t');
				} else if (c2 == '\\') {
					current.append(c2);
				}
			} else {
				current.append(c);
			}
		}
		result.add(current.toString());
		return Collections.unmodifiableList(result);
	}

	public static String getMergedLines(List<? extends CharSequence> strings) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.size(); i++) {
			sb.append(strings.get(i));
			if (i < strings.size() - 1) {
				sb.append("\\n");
			}
		}
		return sb.toString();
	}

	final static public List<String> getSplit(Pattern pattern, String line) {
		final Matcher m = pattern.matcher(line);
		if (m.find() == false) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		for (int i = 1; i <= m.groupCount(); i++) {
			result.add(m.group(i));
		}
		return result;

	}

	public static boolean isNotEmpty(String input) {
		return input != null && trin(input).length() > 0;
	}

	public static boolean isNotEmpty(List<? extends CharSequence> input) {
		return input != null && input.size() > 0;
	}

	public static boolean isEmpty(String input) {
		return input == null || trin(input).length() == 0;
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
	
	public static char hiddenNewLine() {
		return '\u0009';
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

	public static int getWidth(List<? extends CharSequence> stringsToDisplay) {
		int result = 1;
		for (CharSequence s : stringsToDisplay) {
			if (result < s.length()) {
				result = s.length();
			}
		}
		return result;
	}

	public static int getWidth(Display stringsToDisplay) {
		int result = 1;
		for (CharSequence s : stringsToDisplay) {
			if (result < s.length()) {
				result = s.length();
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

	private static void removeFirstColumn(List<String> data) {
		for (int i = 0; i < data.size(); i++) {
			final String s = data.get(i);
			if (s.length() > 0) {
				data.set(i, s.substring(1));
			}
		}
	}

	private static boolean firstColumnRemovable(List<String> data) {
		boolean allEmpty = true;
		for (String s : data) {
			if (s.length() == 0) {
				continue;
			}
			allEmpty = false;
			final char c = s.charAt(0);
			if (c != ' ' && c != '\t') {
				return false;
			}
		}
		return allEmpty == false;
	}

	public static List<String> removeEmptyColumns(List<String> data) {
		if (firstColumnRemovable(data) == false) {
			return data;
		}
		final List<String> result = new ArrayList<String>(data);
		do {
			removeFirstColumn(result);
		} while (firstColumnRemovable(result));
		return result;
	}

	public static void trimSmart(List<String> data, int referenceLine) {
		if (data.size() <= referenceLine) {
			return;
		}
		final int nbStartingSpace = nbStartingSpace(data.get(referenceLine));
		for (int i = referenceLine; i < data.size(); i++) {
			final String s = data.get(i);
			data.set(i, removeStartingSpaces(s, nbStartingSpace));
		}
	}

	public static String removeStartingSpaces(String s, int nbStartingSpace) {
		for (int i = 0; i < nbStartingSpace; i++) {
			if (s.length() > 0 && isSpaceOrTab(s.charAt(0))) {
				s = s.substring(1);
			} else {
				return s;
			}
		}
		return s;
	}

	private static boolean isSpaceOrTab(char c) {
		return c == ' ' || c == '\t';
	}

	private static int nbStartingSpace(String s) {
		int nb = 0;
		while (nb < s.length() && isSpaceOrTab(s.charAt(nb))) {
			nb++;
		}
		return nb;
	}

	public static void trim(List<String> data, boolean removeEmptyLines) {
		for (int i = 0; i < data.size(); i++) {
			final String s = data.get(i);
			data.set(i, trin(s));
		}
		if (removeEmptyLines) {
			for (final Iterator<String> it = data.iterator(); it.hasNext();) {
				if (it.next().length() == 0) {
					it.remove();
				}
			}
		}
	}

	public static String uncommentSource(String source) {
		final StringReader sr = new StringReader(source);
		final UncommentReadLine un = new UncommentReadLine(new ReadLineReader(sr));
		final StringBuilder sb = new StringBuilder();
		String s = null;
		try {
			while ((s = un.readLine()) != null) {
				sb.append(s);
				sb.append('\n');
			}
		} catch (IOException e) {
			Log.error("Error " + e);
			throw new IllegalStateException(e.toString());
		}

		sr.close();
		return sb.toString();
	}

	public static boolean isDiagramCacheable(String uml) {
		uml = uml.toLowerCase();
		if (uml.startsWith("@startuml\nversion\n")) {
			return false;
		}
		if (uml.startsWith("@startuml\ncheckversion")) {
			return false;
		}
		if (uml.startsWith("@startuml\ntestdot\n")) {
			return false;
		}
		if (uml.startsWith("@startuml\nsudoku\n")) {
			return false;
		}
		return true;
	}

	public static List<String> splitComma(String s) {
		s = trin(s);
		// if (s.matches("([\\p{L}0-9_.]+|[%g][^%g]+[%g])(\\s*,\\s*([\\p{L}0-9_.]+|[%g][^%g]+[%g]))*") == false) {
		// throw new IllegalArgumentException();
		// }
		final List<String> result = new ArrayList<String>();
		final Pattern p = MyPattern.cmpile("([\\p{L}0-9_.]+|[%g][^%g]+[%g])");
		final Matcher m = p.matcher(s);
		while (m.find()) {
			result.add(eventuallyRemoveStartingAndEndingDoubleQuote(m.group(0)));
		}
		return Collections.unmodifiableList(result);
	}

	public static String getAsHtml(Color color) {
		if (color == null) {
			return null;
		}
		return getAsHtml(color.getRGB());
	}

	public static String getAsSvg(ColorMapper mapper, HtmlColor color) {
		if (color == null) {
			return "none";
		}
		if (color instanceof HtmlColorTransparent) {
			return "#FFFFFF";
		}
		return getAsHtml(mapper.getMappedColor(color));
	}

	public static String getAsHtml(int color) {
		final int v = 0xFFFFFF & color;
		String s = "000000" + Integer.toHexString(v).toUpperCase();
		s = s.substring(s.length() - 6);
		return "#" + s;
	}

	public static String getUid(String uid1, int uid2) {
		return uid1 + String.format("%04d", uid2);
	}

	public static boolean isMethod(String s) {
		return s.contains("(") || s.contains(")");
	}

	public static <O> List<O> merge(List<O> l1, List<O> l2) {
		final List<O> result = new ArrayList<O>(l1);
		result.addAll(l2);
		return Collections.unmodifiableList(result);
	}

	public static boolean endsWithBackslash(final String s) {
		return s.endsWith("\\") && s.endsWith("\\\\") == false;
	}

	public static String manageGuillemetStrict(String st) {
		if (st.startsWith("<< ")) {
			st = "\u00AB" + st.substring(3);
		} else if (st.startsWith("<<")) {
			st = "\u00AB" + st.substring(2);
		}
		if (st.endsWith(" >>")) {
			st = st.substring(0, st.length() - 3) + "\u00BB";
		} else if (st.endsWith(">>")) {
			st = st.substring(0, st.length() - 2) + "\u00BB";
		}
		return st;
	}

	public static String manageGuillemet(String st) {
		return st.replaceAll("\\<\\<([^<>]+)\\>\\>", "\u00AB$1\u00BB");
	}

	public static String trinNoTrace(String s) {
		return s.trim();
	}

	public static String trin(String s) {
		final String result = s.trim();
		// if (result.equals(s) == false && s.contains("prop")) {
		// System.err.println("TRIMING " + s);
		// }
		return result;
	}

	// http://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
}
