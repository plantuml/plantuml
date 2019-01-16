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
package net.sourceforge.plantuml.code;

public class AsciiEncoder implements URLEncoder {

	// Temporary because of AsciiEncoderFinalZeros
	final static/* private */char encode6bit[] = new char[64];
	final static/* private */byte decode6bit[] = new byte[128];

	static {
		for (byte b = 0; b < 64; b++) {
			encode6bit[b] = encode6bit(b);
			decode6bit[encode6bit[b]] = b;
		}
	}

	public String encode(byte data[]) {
		if (data == null) {
			return "";
		}
		final StringBuilder result = new StringBuilder((data.length * 4 + 2) / 3);
		for (int i = 0; i < data.length; i += 3) {
			append3bytes(result, data[i] & 0xFF, i + 1 < data.length ? data[i + 1] & 0xFF : 0,
					i + 2 < data.length ? data[i + 2] & 0xFF : 0);
		}
		return result.toString();
	}

	public byte[] decode(String s) {
		// if (s.length() % 4 != 0) {
		// throw new IllegalArgumentException("Cannot decode " + s);
		// }
		final byte data[] = new byte[computeSize(s.length())];
		int pos = 0;
		for (int i = 0; i < s.length(); i += 4) {
			decode3bytes(data, pos, scharAt(s, i), scharAt(s, i + 1), scharAt(s, i + 2), scharAt(s, i + 3));
			pos += 3;
		}
		return data;
	}

	private int computeSize(int length) {
		// while (length % 4 != 0) {
		// length++;
		// }
		final int r = length % 4;
		if (r != 0) {
			length += 4 - r;
		}
		// System.err.println("length=" + length);
		// System.err.println("length1=" + (length % 4));
		// length += length % 4;
		// System.err.println("length2=" + length);
		assert length % 4 == 0 : "length=" + length;
		return (length * 3 + 3) / 4;
	}

	private char scharAt(String s, int i) {
		if (i >= s.length()) {
			return '0';
		}
		return s.charAt(i);
	}

	public static int decode6bit(char c) {
		return decode6bit[c];
	}

	public static char encode6bit(byte b) {
		assert b >= 0 && b < 64;
		if (b < 10) {
			return (char) ('0' + b);
		}
		b -= 10;
		if (b < 26) {
			return (char) ('A' + b);
		}
		b -= 26;
		if (b < 26) {
			return (char) ('a' + b);
		}
		b -= 26;
		if (b == 0) {
			return '-';
		}
		if (b == 1) {
			return '_';
		}
		assert false;
		return '?';
	}

	private void append3bytes(StringBuilder sb, int b1, int b2, int b3) {
		final int c1 = b1 >> 2;
		final int c2 = ((b1 & 0x3) << 4) | (b2 >> 4);
		final int c3 = ((b2 & 0xF) << 2) | (b3 >> 6);
		final int c4 = b3 & 0x3F;
		sb.append(encode6bit[c1 & 0x3F]);
		sb.append(encode6bit[c2 & 0x3F]);
		sb.append(encode6bit[c3 & 0x3F]);
		sb.append(encode6bit[c4 & 0x3F]);
	}

	private void decode3bytes(byte r[], int pos, char cc1, char cc2, char cc3, char cc4) {
		final int c1 = decode6bit[cc1];
		final int c2 = decode6bit[cc2];
		final int c3 = decode6bit[cc3];
		final int c4 = decode6bit[cc4];
		r[pos] = (byte) ((c1 << 2) | (c2 >> 4));
		r[pos + 1] = (byte) (((c2 & 0x0F) << 4) | (c3 >> 2));
		r[pos + 2] = (byte) (((c3 & 0x3) << 6) | c4);
	}

}
