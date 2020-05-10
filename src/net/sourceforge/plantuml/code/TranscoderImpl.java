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

import java.io.IOException;

public class TranscoderImpl implements Transcoder {

	static enum Format {
		UTF8, UPF9;
	}

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;
	private final Format format;

	private TranscoderImpl(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression,
			Format format) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
		this.format = format;
	}

	public static Transcoder utf8(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		return new TranscoderImpl(urlEncoder, stringCompressor, compression, Format.UTF8);
	}

	public static Transcoder upf9(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		return new TranscoderImpl(urlEncoder, stringCompressor, compression, Format.UPF9);
	}

	public String encode(String text) throws IOException {
		final String stringAnnoted = stringCompressor.compress(text);
		final byte[] data;
		if (format == Format.UTF8)
			data = stringAnnoted.getBytes("UTF-8");
		else
			data = Upf9Encoder.getBytes(stringAnnoted);

		final byte[] compressedData = compression.compress(data);

		return urlEncoder.encode(compressedData);
	}

	public String decode(String code) throws NoPlantumlCompressionException {
		try {
			final byte compressedData[] = urlEncoder.decode(code);
			final ByteArray data = compression.decompress(compressedData);
			final String string = format == Format.UTF8 ? data.toUFT8String() : data.toUPF9String();
			return stringCompressor.decompress(string);
		} catch (Exception e) {
			System.err.println("Cannot decode string");
			throw new NoPlantumlCompressionException(e);
		}
	}

}
