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
package net.sourceforge.plantuml.ngdot.attr;

/**
 * Reading numbers and booleans in DOT attribute values, as Graphviz does.
 */
public final class DotNumbers {

	private DotNumbers() {
	}

	/**
	 * Reads the number at the start of a value ({@code "1.2 equally"} gives
	 * 1.2), or returns {@code null} if there is none.
	 */
	public static Double parseFirst(String value) {
		if (value == null)
			return null;
		final String s = value.trim();
		int end = 0;
		boolean digit = false;
		if (end < s.length() && (s.charAt(end) == '-' || s.charAt(end) == '+'))
			end++;
		while (end < s.length() && Character.isDigit(s.charAt(end))) {
			end++;
			digit = true;
		}
		if (end < s.length() && s.charAt(end) == '.') {
			end++;
			while (end < s.length() && Character.isDigit(s.charAt(end))) {
				end++;
				digit = true;
			}
		}
		if (digit == false)
			return null;
		try {
			return Double.parseDouble(s.substring(0, end));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static double parse(String value, double defaultValue) {
		final Double result = parseFirst(value);
		if (result == null)
			return defaultValue;
		return result;
	}

	/**
	 * Graphviz {@code mapbool}: {@code false}, {@code no} and numbers equal to 0
	 * are false; {@code true}, {@code yes} and other numbers are true.
	 *
	 * @return the value, or {@code defaultValue} for {@code null} or an
	 *         unrecognized value
	 */
	public static boolean parseBoolean(String value, boolean defaultValue) {
		if (value == null)
			return defaultValue;
		final String s = value.trim();
		if (s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no"))
			return false;
		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes"))
			return true;
		final Double number = parseFirst(s);
		if (number != null)
			return number.intValue() != 0;
		return defaultValue;
	}

	public static boolean isFalse(String value) {
		return parseBoolean(value, true) == false;
	}

}
