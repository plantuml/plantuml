/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.teavm.c;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The standard output of the C build, for {@code -pipe}: the bytes are written
 * as they are, in binary mode.
 * <p>
 * {@code System.out} cannot be used for an image there: on Windows it turned
 * each {@code \n} of a PNG into {@code \r\n} and lost the bytes it could not
 * encode, so the file was not a PNG.
 * <p>
 * Only create it when {@code TeaVM.isC()}: elsewhere the native method fails.
 */
public final class StdoutStream extends OutputStream {
	// ::remove file when JAVA8

	// The native method reads the start of its array: go through a block
	private final byte[] block = new byte[8192];

	public StdoutStream() {
	}

	@Override
	public void write(int b) throws IOException {
		block[0] = (byte) b;
		send(1);
	}

	@Override
	public void write(byte[] buffer, int offset, int length) throws IOException {
		while (length > 0) {
			final int count = Math.min(length, block.length);
			System.arraycopy(buffer, offset, block, 0, count);
			send(count);
			offset += count;
			length -= count;
		}
	}

	private void send(int count) throws IOException {
		if (NativeStdin.write(block, count) != 0)
			throw new IOException("Cannot write to the standard output");
	}

}
