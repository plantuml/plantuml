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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BackSlash {

	private static final char PRIVATE_BLOCK = '\uE000';
	public static final String BS_BS_N = "\\n";
	public static final String NEWLINE = "\n";
	public static final char CHAR_NEWLINE = '\n';

	public static char hiddenNewLine() {
		return PRIVATE_BLOCK + BackSlash.CHAR_NEWLINE;
	}

	public static String convertHiddenNewLine(String s) {
		s = s.replaceAll("(?<!\\\\)\\\\n", "" + hiddenNewLine());
		s = s.replaceAll("\\\\\\\\n", "\\\\n");
		return s;
	}

	public static List<String> splitHiddenNewLine(String s) {
		return Arrays.asList(s.split("" + hiddenNewLine()));
	}

	public static String manageNewLine(String string) {
		return string.replace(hiddenNewLine(), BackSlash.CHAR_NEWLINE);
	}

	public static List<String> getWithNewlines(CharSequence s) {
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

	public static String translateBackSlashes(CharSequence s) {
		if (s == null) {
			return null;
		}
		// final String tmps = s.toString();
		// if (tmps.indexOf('\\') == -1) {
		// return tmps;
		// }
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i < s.length() - 1 && isEnglishLetterOfBackSlash(s.charAt(i + 1))) {
				result.append('\\');
				result.append(translateChar(s.charAt(i + 1)));
				i++;
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	private static boolean isEnglishLetterOfBackSlash(char c) {
		return c == 'n';
		// return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static String untranslateBackSlashes(CharSequence s) {
		if (s == null) {
			return null;
		}
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > PRIVATE_BLOCK && c < '\uE07F') {
				c = (char) (c - PRIVATE_BLOCK);
			}
			result.append(c);
		}
		return result.toString();
	}

	private static char translateChar(char c) {
		if (c > 128) {
			throw new IllegalArgumentException();
		}
		return (char) (PRIVATE_BLOCK + c);
	}

}
