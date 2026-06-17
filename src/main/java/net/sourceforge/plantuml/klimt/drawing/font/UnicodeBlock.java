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
package net.sourceforge.plantuml.klimt.drawing.font;

public class UnicodeBlock {

	private final byte data[];

	public UnicodeBlock(byte data[]) {
		if (data.length != 1 && data.length < 256) {
			this.data = decodeRLE(data);
		} else {
			this.data = data;
		}

	}

	private static byte[] decodeRLE(byte[] data) {
		final byte[] result = new byte[256];
		int idx = 0;
		for (int i = 0; i < data.length; i += 2) {
			final int count = data[i] & 0xFF;
			final byte value = data[i + 1];
			for (int j = 0; j < count; j++)
				result[idx++] = value;
		}
		return result;
	}

	public double getWidth(char ch) {
		if (data.length == 1)
			return (data[0] & 0xFF) / 10.0;

		final int width = data[ch & 0xFF] & 0xFF;
		return width / 10.0;
	}
}
