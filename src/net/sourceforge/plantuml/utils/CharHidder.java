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
package net.sourceforge.plantuml.utils;

public class CharHidder {

	public static String addTileAtBegin(String s) {
		return "~" + s;
	}

	public static String hide(String s) {
		// System.err.println("hide " + s);
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '\\' && i + 1 < s.length() && s.charAt(i + 1) == '~') {
				result.append(hideChar('~'));
				i++;
			} else if (c == '~' && i + 1 < s.length()) {
				i++;
				final char c2 = s.charAt(i);
				if (isToBeHidden(c2)) {
					result.append(hideChar(c2));
				} else {
					result.append(c);
					result.append(c2);
				}

			} else {
				result.append(c);
			}
		}
		// System.err.println("---> " + result);
		return result.toString();
	}

	private static boolean isToBeHidden(final char c) {
		if (c == '_' || c == '-' || c == '\"' || c == '#' || c == ']' || c == '[' || c == '*' || c == '.' || c == '/'
				|| c == '<') {
			return true;
		}
		return false;
	}

	private static char hideChar(char c) {
		if (c > 255) {
			throw new IllegalArgumentException();
		}
		return (char) ('\uE000' + c);
	}

	private static char unhideChar(char c) {
		if (c >= '\uE000' && c <= '\uE0FF') {
			return (char) (c - '\uE000');
		}
		return c;
	}

	public static String unhide(String s) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			result.append(unhideChar(c));
		}
		// System.err.println("unhide " + result);
		return result.toString();
	}

}
