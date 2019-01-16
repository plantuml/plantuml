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

	private final Compression compression;
	private final URLEncoder urlEncoder;
	private final StringCompressor stringCompressor;

//	private TranscoderImpl() {
//		this(new AsciiEncoder(), new StringCompressorNone(), new CompressionHuffman());
//	}
//
//	private TranscoderImpl(Compression compression) {
//		this(new AsciiEncoder(), new StringCompressorNone(), compression);
//	}
//
//	private TranscoderImpl(URLEncoder urlEncoder, Compression compression) {
//		this(urlEncoder, new ArobaseStringCompressor(), compression);
//	}

	public TranscoderImpl(URLEncoder urlEncoder, StringCompressor stringCompressor, Compression compression) {
		this.compression = compression;
		this.urlEncoder = urlEncoder;
		this.stringCompressor = stringCompressor;
	}

	public String encode(String text) throws IOException {

		final String stringAnnoted = stringCompressor.compress(text);

		final byte[] data = stringAnnoted.getBytes("UTF-8");
		final byte[] compressedData = compression.compress(data);

		return urlEncoder.encode(compressedData);
	}

	public String decode(String code) throws IOException {
		final byte compressedData[] = urlEncoder.decode(code);
		final byte data[] = compression.decompress(compressedData);

		return stringCompressor.decompress(new String(data, "UTF-8"));
	}

}
