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

public class TranscoderSmartAttic implements Transcoder {

	// Legacy encoder
	private final Transcoder oldOne = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionHuffman());
	private final Transcoder zlib = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionZlib());
	private final Transcoder brotli = TranscoderImpl.utf8(new AsciiEncoder(), new ArobaseStringCompressor(),
			new CompressionBrotli());

	private final Transcoder zlibBase64 = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionZlib());
	private final Transcoder brotliBase64 = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionBrotli());
	private final Transcoder base64only = TranscoderImpl.utf8(new AsciiEncoderBase64(), new ArobaseStringCompressor(),
			new CompressionNone());
	private final Transcoder hexOnly = TranscoderImpl.utf8(new AsciiEncoderHex(), new ArobaseStringCompressor(),
			new CompressionNone());

	public String decode(String code) throws NoPlantumlCompressionException {
		// Work in progress
		// See https://github.com/plantuml/plantuml/issues/117

		// Two char headers
		if (code.startsWith("0A")) {
			return zlibBase64.decode(code.substring(2));
		}
		if (code.startsWith("0B")) {
			return brotliBase64.decode(code.substring(2));
		}
		if (code.startsWith("0C")) {
			return base64only.decode(code.substring(2));
		}
		if (code.startsWith("0D")) {
			return hexOnly.decode(code.substring(2));
		}
		// Text prefix
		// Just a wild try: use them only for testing
		if (code.startsWith("-deflate-")) {
			return zlibBase64.decode(code.substring("-deflate-".length()));
		}
		if (code.startsWith("-brotli-")) {
			return brotliBase64.decode(code.substring("-brotli-".length()));
		}
		if (code.startsWith("-base64-")) {
			return base64only.decode(code.substring("-base64-".length()));
		}
		if (code.startsWith("-hex-")) {
			return hexOnly.decode(code.substring("-hex-".length()));
		}

		// Legacy decoding : you should not use it any more.
		if (code.startsWith("0")) {
			return brotli.decode(code.substring(1));
		}
		try {
			return zlib.decode(code);
		} catch (Exception ex) {
			return oldOne.decode(code);
		}
		// return zlib.decode(code);
	}

	public String encode(String text) throws IOException {
		// Right now, we still use the legacy encoding.
		// This will be changed in the incoming months
		return zlib.encode(text);
	}
}
