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
package net.sourceforge.plantuml.nio;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.code.deflate.ByteBitInputStream;
import net.sourceforge.plantuml.code.deflate.Decompressor;

/**
 * Reads a ZIP archive with java.io only, for the C build, where
 * java.util.zip.ZipFile is not available. The archive is read whole in memory,
 * its central directory gives the entries, and the deflated ones go through the
 * pure Java inflater of PlantUML.
 * <p>
 * Neither encryption nor multi-volume archives are supported.
 */
final class ZipReaderIo {

	private static final int METHOD_STORED = 0;
	private static final int METHOD_DEFLATED = 8;

	private static final class Entry {
		private final int method;
		private final long compressedSize;
		private final long size;
		private final long localHeaderOffset;

		private Entry(int method, long compressedSize, long size, long localHeaderOffset) {
			this.method = method;
			this.compressedSize = compressedSize;
			this.size = size;
			this.localHeaderOffset = localHeaderOffset;
		}
	}

	// The last archive read: the same one is asked again for each include
	private static ZipReaderIo last;

	private final String key;
	private final byte[] data;
	private final Map<String, Entry> entries = new HashMap<String, Entry>();

	private ZipReaderIo(String key, byte[] data) {
		this.key = key;
		this.data = data;
	}

	/**
	 * The reader of this file, or null if it is not a readable ZIP archive.
	 */
	static ZipReaderIo open(File file) throws IOException {
		if (file.isFile() == false || file.canRead() == false)
			return null;

		final String key = file.getAbsolutePath() + "|" + file.length() + "|" + file.lastModified();
		if (last != null && last.key.equals(key))
			return last;

		final ZipReaderIo result = new ZipReaderIo(key, readAll(file));
		if (result.readCentralDirectory() == false)
			return null;

		last = result;
		return result;
	}

	private static byte[] readAll(File file) throws IOException {
		final InputStream is = new FileInputStream(file);
		try {
			final byte[] result = new byte[(int) file.length()];
			int done = 0;
			while (done < result.length) {
				final int n = is.read(result, done, result.length - done);
				if (n < 0)
					break;
				done += n;
			}
			if (done < result.length) {
				final byte[] shorter = new byte[done];
				System.arraycopy(result, 0, shorter, 0, done);
				return shorter;
			}
			return result;
		} finally {
			is.close();
		}
	}

	private int u16(int pos) {
		return (data[pos] & 0xFF) | ((data[pos + 1] & 0xFF) << 8);
	}

	private long u32(int pos) {
		return (u16(pos) & 0xFFFFL) | ((u16(pos + 2) & 0xFFFFL) << 16);
	}

	private boolean readCentralDirectory() {
		// End of central directory record: 22 bytes, then an optional comment
		int end = -1;
		for (int pos = data.length - 22; pos >= 0 && pos >= data.length - 22 - 0xFFFF; pos--)
			if (u32(pos) == 0x06054b50L) {
				end = pos;
				break;
			}
		if (end == -1)
			return false;

		final int count = u16(end + 10);
		long pos = u32(end + 16);
		for (int i = 0; i < count; i++) {
			if (pos < 0 || pos + 46 > data.length || u32((int) pos) != 0x02014b50L)
				return false;

			final int p = (int) pos;
			final int method = u16(p + 10);
			final long compressedSize = u32(p + 20);
			final long size = u32(p + 24);
			final int nameLength = u16(p + 28);
			final int extraLength = u16(p + 30);
			final int commentLength = u16(p + 32);
			final long localHeaderOffset = u32(p + 42);
			if (p + 46 + nameLength > data.length)
				return false;

			final String name = new String(data, p + 46, nameLength, StandardCharsets.UTF_8);
			entries.put(name, new Entry(method, compressedSize, size, localHeaderOffset));
			pos = p + 46L + nameLength + extraLength + commentLength;
		}
		return true;
	}

	/**
	 * Whether this is a file (not a directory) of the archive.
	 */
	boolean contains(String name) {
		return entries.containsKey(name) && name.endsWith("/") == false;
	}

	/**
	 * The uncompressed content of this entry, or null if there is none.
	 */
	byte[] read(String name) throws IOException {
		final Entry entry = entries.get(name);
		if (entry == null || name.endsWith("/"))
			return null;

		final long local = entry.localHeaderOffset;
		if (local < 0 || local + 30 > data.length || u32((int) local) != 0x04034b50L)
			throw new IOException("Bad ZIP entry: " + name);

		final int start = (int) local + 30 + u16((int) local + 26) + u16((int) local + 28);
		if (entry.compressedSize < 0 || start + entry.compressedSize > data.length)
			throw new IOException("Bad ZIP entry: " + name);

		final int length = (int) entry.compressedSize;
		if (entry.method == METHOD_STORED) {
			final byte[] result = new byte[length];
			System.arraycopy(data, start, result, 0, length);
			return result;
		}
		if (entry.method != METHOD_DEFLATED)
			throw new IOException("Unsupported ZIP compression method " + entry.method + ": " + name);

		// The bit reader looks a little beyond the last block: pad as CompressionZlib does
		final byte[] padded = new byte[length + 256];
		System.arraycopy(data, start, padded, 0, length);
		try {
			final byte[] result = Decompressor
					.decompress(new ByteBitInputStream(new ByteArrayInputStream(padded)));
			if (result.length != entry.size)
				throw new IOException("Bad ZIP entry size: " + name);
			return result;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException("Bad ZIP entry: " + name + " (" + e + ")");
		}
	}

}
