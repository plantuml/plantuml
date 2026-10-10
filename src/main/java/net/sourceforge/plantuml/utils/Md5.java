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
package net.sourceforge.plantuml.utils;

/**
 * Minimal pure-Java MD5 (RFC 1321), for targets where
 * {@code java.security.MessageDigest} is not available (TeaVM C).
 */
public class Md5 {

	private static final int[] SHIFTS = { 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 5, 9, 14, 20, 5,
			9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 6, 10,
			15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };

	private static final int[] K = new int[64];

	static {
		for (int i = 0; i < 64; i++)
			K[i] = (int) (long) Math.floor(Math.abs(Math.sin(i + 1)) * 4294967296.0);
	}

	private Md5() {
	}

	public static byte[] digest(byte[] data) {
		final int padLen = ((data.length + 8) / 64 + 1) * 64;
		final byte[] msg = new byte[padLen];
		System.arraycopy(data, 0, msg, 0, data.length);
		msg[data.length] = (byte) 0x80;
		final long bitLen = (long) data.length * 8L;
		for (int i = 0; i < 8; i++)
			msg[padLen - 8 + i] = (byte) (bitLen >>> (8 * i));

		int a0 = 0x67452301;
		int b0 = 0xefcdab89;
		int c0 = 0x98badcfe;
		int d0 = 0x10325476;

		final int[] w = new int[16];
		for (int off = 0; off < padLen; off += 64) {
			for (int i = 0; i < 16; i++) {
				final int j = off + i * 4;
				w[i] = (msg[j] & 0xff) | (msg[j + 1] & 0xff) << 8 | (msg[j + 2] & 0xff) << 16
						| (msg[j + 3] & 0xff) << 24;
			}
			int a = a0;
			int b = b0;
			int c = c0;
			int d = d0;
			for (int i = 0; i < 64; i++) {
				int f;
				int g;
				if (i < 16) {
					f = (b & c) | (~b & d);
					g = i;
				} else if (i < 32) {
					f = (d & b) | (~d & c);
					g = (5 * i + 1) % 16;
				} else if (i < 48) {
					f = b ^ c ^ d;
					g = (3 * i + 5) % 16;
				} else {
					f = c ^ (b | ~d);
					g = (7 * i) % 16;
				}
				final int tmp = d;
				d = c;
				c = b;
				b = b + Integer.rotateLeft(a + f + K[i] + w[g], SHIFTS[i]);
				a = tmp;
			}
			a0 += a;
			b0 += b;
			c0 += c;
			d0 += d;
		}

		final byte[] out = new byte[16];
		final int[] state = { a0, b0, c0, d0 };
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				out[i * 4 + j] = (byte) (state[i] >>> (8 * j));
		return out;
	}
}
