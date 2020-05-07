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
import java.util.zip.Deflater;

import net.sourceforge.plantuml.code.deflate.ByteBitInputStream;
import net.sourceforge.plantuml.code.deflate.Decompressor;

public class CompressionZlibPure implements Compression {

	private static boolean USE_ZOPFLI = false;
	private static final int COMPRESSION_LEVEL = 9;

	public byte[] compress(byte[] in) {
		if (USE_ZOPFLI) {
			return new CompressionZopfliZlib().compress(in);
		}
		if (in.length == 0) {
			return null;
		}
		int len = in.length * 2;
		if (len < 1000) {
			len = 1000;
		}
		// Compress the bytes
		final Deflater compresser = new Deflater(COMPRESSION_LEVEL, true);
		compresser.setInput(in);
		compresser.finish();

		final byte[] output = new byte[len];
		final int compressedDataLength = compresser.deflate(output);
		if (compresser.finished() == false) {
			return null;
		}
		return copyArray(output, compressedDataLength);
	}

	public ByteArray decompress(byte[] in) throws NoPlantumlCompressionException {
		final ByteBitInputStream in2 = new ByteBitInputStream(new ByteArrayInputStream(in));
		try {
			return ByteArray.from(Decompressor.decompress(in2));
		} catch (Exception e) {
			throw new NoPlantumlCompressionException(e);
		}
	}

	private byte[] copyArray(final byte[] data, final int len) {
		final byte[] result = new byte[len];
		System.arraycopy(data, 0, result, 0, len);
		return result;
	}

}
