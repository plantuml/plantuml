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
 * Revision $Revision: 3830 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.math.BigInteger;

public class EggUtils {

	public static String fromByteArrays(byte data[]) {
		final StringBuilder sb = new StringBuilder();
		for (byte b : data) {
			final String hex = Integer.toHexString(b & 0xFF);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static byte[] toByteArrays(String s) {
		final byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		}
		return result;
	}

	public static BigInteger fromSecretSentence(String s) {
		BigInteger result = BigInteger.ZERO;
		final BigInteger twentySix = BigInteger.valueOf(26);
		s = s.replace('\u00E9', 'e');
		s = s.replace('\u00EA', 'e');
		for (char c : s.toCharArray()) {
			final int num = convertChar(c);
			if (num != -1) {
				result = result.multiply(twentySix);
				result = result.add(BigInteger.valueOf(num));

			}
		}
		return result;

	}

	private static int convertChar(char c) {
		c = Character.toLowerCase(c);
		if (c >= 'a' && c <= 'z') {
			return c - 'a';
		}
		return -1;
	}

	public static byte[] xor(byte data[], byte key[]) {
		final byte[] result = new byte[data.length];
		int pos = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (data[i] ^ key[pos++]);
			if (pos == key.length) {
				pos = 0;
			}

		}
		return result;
	}

}
