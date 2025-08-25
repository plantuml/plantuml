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

import java.io.IOException;

public class BitInput {
	private final byte[] buf;
	private int byteIndex = 0;
	private int bitIndex = 0;

	BitInput(byte[] buf) {
		this.buf = buf;
	}

	int availableBits() {
		return (buf.length - byteIndex) * 8 - bitIndex;
	}

	boolean readBit() throws IOException {
		if (byteIndex >= buf.length)
			throw new IOException();

		final int current = buf[byteIndex] & 0xFF;
		final boolean bit = ((current >> bitIndex) & 1) == 1;

		bitIndex++;
		if (bitIndex == 8) {
			bitIndex = 0;
			byteIndex++;
		}
		return bit;
	}

}