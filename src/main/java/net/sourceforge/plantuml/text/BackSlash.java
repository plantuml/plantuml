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
 *
 */
package net.sourceforge.plantuml.text;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;

public class BackSlash {

	@JawsStrange(comment = "Ok because not really used")
	public static final String BS_BS_N = "\\n";
	
	public static final String NEWLINE = "\n";
	public static final char CHAR_NEWLINE = '\n';

	public static final String lineSeparator() {
		return System.lineSeparator();
	}

	@JawsStrange
	public static char hiddenNewLine() {
		return Jaws.BLOCK_E1_NEWLINE;
	}

	public static String translateBackSlashes(CharSequence s) {
		if (s == null)
			return null;

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
		if (s == null)
			return null;

		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > StringUtils.PRIVATE_BLOCK && c < '\uE07F')
				c = (char) (c - StringUtils.PRIVATE_BLOCK);

			result.append(c);
		}
		return result.toString();
	}

	private static char translateChar(char c) {
		if (c > 128)
			throw new IllegalArgumentException();

		return (char) (StringUtils.PRIVATE_BLOCK + c);
	}

}
