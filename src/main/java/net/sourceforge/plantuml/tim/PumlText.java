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
package net.sourceforge.plantuml.tim;

/** The pure text helpers of the <code>puml</code> module. No regex, no java.text: same result on every edition. */
final class PumlText {

	private PumlText() {
	}

	/**
	 * <code>puml.quote(s)</code>: a double-quoted PlantUML string. A backslash
	 * and a double quote are escaped with a backslash, a newline becomes the
	 * two characters <code>\n</code> that PlantUML reads as a line break.
	 */
	static String quote(String s) {
		final StringBuilder sb = new StringBuilder("\"");
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			if (ch == '\\' || ch == '"')
				sb.append('\\').append(ch);
			else if (ch == '\n')
				sb.append("\\n");
			else if (ch != '\r')
				sb.append(ch);
		}
		return sb.append('"').toString();
	}

	/**
	 * <code>puml.id(s)</code>: a valid PlantUML identifier. Letters (accented
	 * ones too) and digits are kept; every run of other characters (spaces,
	 * dashes, ...) becomes one underscore. The result never is empty and never
	 * starts with a digit.
	 */
	static String id(String s) {
		final StringBuilder sb = new StringBuilder();
		boolean pendingSeparator = false;
		for (int i = 0; i < s.length(); i++) {
			final char ch = s.charAt(i);
			if (Character.isLetterOrDigit(ch) || Character.isSurrogate(ch)) {
				if (pendingSeparator && sb.length() > 0)
					sb.append('_');
				pendingSeparator = false;
				sb.append(ch);
			} else {
				pendingSeparator = true;
			}
		}
		if (sb.length() == 0)
			return "_";
		if (Character.isDigit(sb.charAt(0)))
			sb.insert(0, '_');
		return sb.toString();
	}
}
