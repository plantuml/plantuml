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
package net.sourceforge.plantuml.licensing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ASCII7Format {

	public static byte[] encodeString(String text) throws IOException {
		if (text == null || text.isEmpty())
			throw new IllegalArgumentException();

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (BooleanOutputStream bos = new BooleanOutputStream(baos)) {
			for (int i = 0; i < text.length(); i++) {
				final int c = text.charAt(i);
				if (c <= 0 || c > 127)
					throw new IllegalArgumentException();

				for (int b = 0; b < 7; b++) {
					final boolean bit = ((c >> b) & 1) == 1;
					bos.writeBoolean(bit);
				}
			}
		}
		return baos.toByteArray();
	}

	public static String decodeString(byte[] data) throws IOException {
		if (data == null || data.length == 0)
			throw new IllegalArgumentException();

		final BitInput in = new BitInput(data);
		final StringBuilder sb = new StringBuilder();

		while (in.availableBits() >= 7) {
			int value = 0;
			for (int b = 0; b < 7; b++) {
				if (in.readBit())
					value |= (1 << b);

			}
			if (value == 0)
				break;

			if (value < 0 || value > 127)
				throw new IOException("Bad value: " + value);

			sb.append((char) value);
		}

		return sb.toString();
	}
}
