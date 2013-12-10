/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 8515 $
 *
 */
package net.sourceforge.plantuml.flashcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.plantuml.code.Compression;
import net.sourceforge.plantuml.code.CompressionZlib;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class FlashCodeUtilsZxing implements FlashCodeUtils {

	public List<BufferedImage> exportFlashcodeSimple(String s) throws IOException {
		try {
			final QRCodeWriter writer = new QRCodeWriter();
			final Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			final int multiple = 1;
			final BitMatrix bit = writer.encode(s, BarcodeFormat.QR_CODE, multiple);
			final BufferedImage im = MatrixToImageWriter.toBufferedImage(bit);
			return Arrays.asList(im);
		} catch (WriterException e) {
			throw new IOException("WriterException");
		}
	}

	public List<BufferedImage> exportFlashcodeCompress(String s) throws IOException {
		try {
			final QRCodeWriter writer = new QRCodeWriter();
			final Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

			final Compression comp = new CompressionZlib();
			final byte data[] = comp.compress(s.getBytes("UTF-8"));

			// Encoder.DEFAULT_BYTE_MODE_ENCODING
			final int multiple = 1;
			final BitMatrix bit = writer.encode(new String(data, "ISO-8859-1"), BarcodeFormat.QR_CODE, multiple);
			final BufferedImage im = MatrixToImageWriter.toBufferedImage(bit);
			return Arrays.asList(im);
		} catch (WriterException e) {
			throw new IOException("WriterException");
		}
	}

	public List<BufferedImage> exportSplitCompress(String s) throws IOException {
		final QRCodeWriter writer = new QRCodeWriter();
		final Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		final Compression comp = new CompressionZlib();
		final byte data[] = comp.compress(s.getBytes("UTF-8"));

		final List<BufferedImage> result = new ArrayList<BufferedImage>();

		final List<byte[]> blocs = new ArrayList<byte[]>();
		for (int i = 0; i < 4; i++) {
			blocs.add(getSplited(data, i, 4));
		}

		blocs.add(xor(blocs));

		try {
			final int multiple = 1;
			for (byte d[] : blocs) {
				// Encoder.DEFAULT_BYTE_MODE_ENCODING
				final BitMatrix bit = writer.encode(new String(d, "ISO-8859-1"), BarcodeFormat.QR_CODE, multiple);
				result.add(MatrixToImageWriter.toBufferedImage(bit));
			}
		} catch (WriterException e) {
			throw new IOException("WriterException");
		}

		return Collections.unmodifiableList(result);
	}

	static byte[] xor(List<byte[]> blocs) {
		final byte result[] = new byte[blocs.get(0).length];
		for (int i = 0; i < result.length; i++) {
			result[i] = xor(blocs, i);
		}
		return result;
	}

	static byte xor(List<byte[]> blocs, int nb) {
		byte result = 0;
		for (byte[] bloc : blocs) {
			result = (byte) (result ^ bloc[nb]);
		}
		return result;
	}

	static byte[] getSplited(byte[] data, int n, int total) {
		final int size = (data.length + total - 1) / total;
		assert size * total >= data.length;
		final byte result[] = new byte[size + 1];
		result[0] = (byte) (1 << n);
		for (int i = 0; (i < size) && (n * total + i < data.length); i++) {
			result[i + 1] = data[n * total + i];
		}
		return result;
	}

}
