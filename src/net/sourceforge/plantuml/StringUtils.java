/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5957 $
 *
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String getPlateformDependentAbsolutePath(File file) {
		return file.getAbsolutePath();
	}

	public static List<String> getWithNewlines(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		final List<String> result = new ArrayList<String>();
		final StringBuilder current = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() + 1) {
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

	public static String getMergedLines(List<String> strings) {
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
		return input != null && input.trim().length() > 0;
	}

	public static boolean isEmpty(String input) {
		return input == null || input.trim().length() == 0;
	}

	public static String manageHtml(String s) {
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		return s;
	}

	public static String manageArrowForSequence(String s) {
		s = s.replace('=', '-');
		return s;
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

	public static String eventuallyRemoveStartingAndEndingDoubleQuote(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1);
		}
		if (s.startsWith("(") && s.endsWith(")")) {
			return s.substring(1, s.length() - 1);
		}
		if (s.startsWith("[") && s.endsWith("]")) {
			return s.substring(1, s.length() - 1);
		}
		if (s.startsWith(":") && s.endsWith(":")) {
			return s.substring(1, s.length() - 1);
		}
		return s;
	}

	// private static String cleanLineFromSource(String s) {
	// if (s.startsWith("\uFEFF")) {
	// s = s.substring(1);
	// }
	// if (s.startsWith("~~")) {
	// s = s.substring("~~".length());
	// }
	// // if (s.startsWith(" * ")) {
	// // s = s.substring(" * ".length());
	// // }
	// s = s.replaceFirst("^\\s+\\* ", "");
	// if (s.equals(" *")) {
	// s = "";
	// }
	// s = s.trim();
	// while (s.startsWith(" ") || s.startsWith("/") || s.startsWith("\t") ||
	// s.startsWith("%") || s.startsWith("/*")) {
	// if (s.startsWith("/*")) {
	// s = s.substring(2).trim();
	// } else {
	// s = s.substring(1).trim();
	// }
	// }
	// return s;
	// }

	public static boolean isCJK(char c) {
		final Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
		System.err.println(block);
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

	public static int getWidth(List<? extends CharSequence> stringsToDisplay) {
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

	private static void removeFirstColumn(List<String> data) {
		for (int i = 0; i < data.size(); i++) {
			final String s = data.get(i);
			if (s.length() > 0) {
				data.set(i, s.substring(1));
			}
		}
	}

	public static List<String> removeEmptyColumns(List<String> data) {
		if (firstColumnRemovable(data) == false) {
			return data;
		}
		final List<String> result = new ArrayList<String>(data);
		do {
			removeFirstColumn(result);
		} while (firstColumnRemovable(result));
		return Collections.unmodifiableList(result);
	}

	public static void trim(List<String> data, boolean removeEmptyLines) {
		for (int i = 0; i < data.size(); i++) {
			final String s = data.get(i);
			data.set(i, s.trim());
		}
		if (removeEmptyLines) {
			for (final Iterator<String> it = data.iterator(); it.hasNext();) {
				if (it.next().length() == 0) {
					it.remove();
				}
			}
		}
	}

}
