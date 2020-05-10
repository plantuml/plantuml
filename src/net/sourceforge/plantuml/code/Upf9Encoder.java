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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Upf9Encoder {

	private Upf9Encoder() {

	}

	public static byte[] encodeChar(char c) {
		final byte[] result = encodeCharInternal(c);
		assert checkBack(c, result);
		return result;
	}

	private static boolean checkBack(char c, byte[] result) {
		try {
			if (c == Upf9Decoder.decodeChar(new ByteArrayInputStream(result)))
				return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static byte[] encodeCharInternal(char c) {
		if (c == '\n' || c == '\r' || c == '\t') {
			// Using regular ASCII code for <u+0009> <u+000A> and <u+000D>
			return new byte[] { (byte) c };
		}
		if (c >= '\u000E' && c <= '\u0012') {
			return new byte[] { (byte) c };
		}
		if (c >= '\u0020' && c <= '\u007E') {
			// Using regular ASCII code for ASCII printable char
			return new byte[] { (byte) c };
		}
		if (c >= '\u0080' && c <= '\u00FF') {
			// Char from <u+0080> to <u+00FF> are encoded as [0x0B 0x80] to [0x0B 0xFF]
			return new byte[] { 0x0B, (byte) c };
		}
		if (c >= '\u0100' && c <= '\u08FF') {
			// Char from <u+0100> to <u+08FF> are encoded as [0x01 0x00] to [0x08 0xFF]
			return new byte[] { highByte(c), lowByte(c) };
		}
		if (c >= '\u2000' && c <= '\u9FFF') {
			// Char from <u+2000> to <u+9FFF> are encoded as [0x80 0x00] to [0xFF 0xFF]
			return new byte[] { (byte) (0x60 + highByte(c)), lowByte(c) };
		}
		if (c >= '\uE000' && c <= '\uE07F') {
			// Char from <u+E000> to <u+E07F> are encoded as [0x0B 0x00] to [0x0B 0x7F]
			return new byte[] { 0x0B, lowByte(c) };
		}
		// All other char are encoded on 3 bytes, starting with 0x0C
		return new byte[] { 0x0C, highByte(c), lowByte(c) };
	}

	private static byte lowByte(char c) {
		return (byte) (c & 0x00FF);
	}

	private static byte highByte(char c) {
		return (byte) ((c & 0xFF00) >> 8);
	}

	public static byte[] getBytes(String s) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < s.length(); i++) {
			baos.write(encodeChar(s.charAt(i)));
		}
		baos.close();
		final byte[] result = baos.toByteArray();
		assert s.endsWith(Upf9Decoder.decodeString(result, result.length));
		return result;
	}

}
