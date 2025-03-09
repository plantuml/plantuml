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
package net.sourceforge.plantuml.code;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ByteArray {

	private final byte data[];
	private final int length;

	private ByteArray(byte data[], int length) {
		this.data = data;
		this.length = length;
	}

	public static ByteArray from(byte[] input) {
		return new ByteArray(input, input.length);
	}

	public String toUFT8String() throws UnsupportedEncodingException {
		return new String(data, 0, length, UTF_8);
	}

	// ::comment when __CORE__
	public String toUPF9String() throws IOException {
		return Upf9Decoder.decodeString(data, length);
	}
	// ::done

	public int getByteAt(int i) {
		return data[i];
	}

	public int length() {
		return length;
	}

}
