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
import java.io.IOException;
import java.io.InputStream;

public class Upf9Decoder {

	private Upf9Decoder() {
	}

	static int decodeChar(InputStream is) throws IOException {
		final int read0 = is.read();
		if (read0 == -1) {
			return -1;
		}
		if (read0 == 0x0B) {
			final int read1 = is.read();
			if (read1 >= 0x80)
				return (char) read1;
			return (char) ((0xE0 << 8) + read1);
		}
		if (read0 == 0x0C) {
			final int read1 = is.read();
			final int read2 = is.read();
			return (char) ((read1 << 8) + read2);
		}
		if (read0 >= 0x01 && read0 <= 0x08) {
			final int read1 = is.read();
			return (char) ((read0 << 8) + read1);
		}
		if (read0 >= 0x80 && read0 <= 0xFF) {
			final int read1 = is.read();
			return (char) (((read0 - 0x60) << 8) + read1);
		}
		return (char) read0;
	}

	public static String decodeString(byte[] data, int length) throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, length);
		final StringBuilder result = new StringBuilder();
		int read;
		while ((read = decodeChar(bais)) != -1) {
			result.append((char) read);
		}
		bais.close();
		return result.toString();
	}

}
