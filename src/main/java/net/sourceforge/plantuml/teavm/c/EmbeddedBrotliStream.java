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
import java.io.InputStream;

/**
 * A Brotli-compressed embedded file (a .spm file of the standard library),
 * decompressed in C and read by parts: the decompressed file stays in C memory,
 * and only the bytes being read go through the Java heap. Some files are
 * decompressed to tens of megabytes, more than the heap of the C build holds.
 * <p>
 * Only one such stream is usable at a time (decompressing another file frees
 * the previous one): read it, then close it.
 */
final class EmbeddedBrotliStream extends InputStream {
	// ::remove file when JAVA8

	private int position;
	private boolean closed;

	EmbeddedBrotliStream(int index) throws IOException {
		if (NativeResources.decodeBrotli(index) < 0)
			throw new IOException("Not valid Brotli");
	}

	@Override
	public int read() throws IOException {
		final byte[] one = new byte[1];
		final int count = read(one, 0, 1);
		return count <= 0 ? -1 : one[0] & 0xFF;
	}

	@Override
	public int read(byte[] buffer, int offset, int length) throws IOException {
		if (closed)
			throw new IOException("Closed");
		if (length == 0)
			return 0;

		// The native method fills the start of its array: go through a block
		final byte[] block = offset == 0 ? buffer : new byte[Math.min(length, 65536)];
		final int count = NativeResources.readDecoded(position, Math.min(length, block.length), block);
		if (count <= 0)
			return -1;

		if (block != buffer)
			System.arraycopy(block, 0, buffer, offset, count);
		position += count;
		return count;
	}

	@Override
	public long skip(long n) throws IOException {
		if (closed || n <= 0)
			return 0;

		final long skipped = Math.min(n, Integer.MAX_VALUE - position);
		position += (int) skipped;
		return skipped;
	}

	@Override
	public void close() {
		if (closed == false) {
			closed = true;
			NativeResources.releaseDecoded();
		}
	}

}
