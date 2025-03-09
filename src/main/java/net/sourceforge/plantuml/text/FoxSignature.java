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

public class FoxSignature {

	private static final long masks[] = new long[127];
	private static final long MASK_SPACES;
	private static final long MASK_SPECIAL1;

	static {
		final String full = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0!\"#$%&\'()*+,-./:;<=>?@[\\]^_{|}~";
		long m = 1L;
		MASK_SPACES = m;
		m = m << 1;
		MASK_SPECIAL1 = m;
		m = m << 1;
		for (int i = 0; i < full.length(); i++) {
			char ch = full.charAt(i);
			masks[ch] = m;
			if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch + ('a' - 'A'));
				masks[ch] = m;
			} else if (ch == '.' || ch == '=' || ch == '-' || ch == '~')
				masks[ch] |= MASK_SPECIAL1;
			m = m << 1;
		}
		masks[' '] = MASK_SPACES;
		masks['\t'] = MASK_SPACES;
		masks['\r'] = MASK_SPACES;
		masks['\n'] = MASK_SPACES;
		masks['\f'] = MASK_SPACES;

	}

	public static long getSpecialSpaces() {
		return MASK_SPACES;
	}

	public static long getSpecial1() {
		return MASK_SPECIAL1;
	}

	public static void printMe() {
		for (int i = 0; i < masks.length; i++)
			if (masks[i] > 0) {
				final char ch = (char) i;
				System.err.println("ch=" + ch + " " + masks[i]);
			}

	}

	private static long getMask(char ch) {
		if (ch < masks.length)
			return masks[ch];
		else if (ch == '\u00A0')
			return MASK_SPACES;

		return 0L;
	}

	public static long getFoxSignatureFromRealString(String s) {
		long result = 0;
		for (int i = 0; i < s.length(); i++)
			result = result | getMask(s.charAt(i));
		return result;
	}

	public static long getFoxSignatureFromRegex(String s) {
		long result = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.') {
				if (s.charAt(i + 1) == '+' || s.charAt(i + 1) == '*')
					i++;
				else
					throw new IllegalArgumentException(s);
			} else if (s.charAt(i) == '\\') {
				if (s.charAt(i + 1) == 'b')
					i++;
				else if (Character.isLetterOrDigit(s.charAt(i + 1)))
					throw new IllegalArgumentException(s);
			} else {
				if (i + 1 < s.length() && (s.charAt(i + 1) == '?' || s.charAt(i + 1) == '*')) {
					i++;
					continue;
				}
				result = result | getMask(s.charAt(i));
				if (i + 1 < s.length() && s.charAt(i + 1) == '+') {
					i++;
				}
			}
		}
		return result;
	}

	public static String backToString(long check) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < masks.length; i++)
			if (masks[i] != 0L && (check & masks[i]) != 0L) {
				final char ch = (char) i;
				sb.append(ch);
			}
		return sb.toString();
	}

}
