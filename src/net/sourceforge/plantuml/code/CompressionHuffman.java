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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class CompressionHuffman implements Compression {

	public byte[] compress(byte[] in) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final Deflater deflater = new Deflater(Deflater.HUFFMAN_ONLY);
		deflater.setLevel(9);
		final DeflaterOutputStream gz = new DeflaterOutputStream(baos, deflater);
		try {
			gz.write(in);
			gz.close();
			baos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e.toString());
		}
	}

	public ByteArray decompress(byte[] in) throws NoPlantumlCompressionException {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();

			final ByteArrayInputStream bais = new ByteArrayInputStream(in);
			final InflaterInputStream gz = new InflaterInputStream(bais);
			int read;
			while ((read = gz.read()) != -1) {
				baos.write(read);
			}
			gz.close();
			bais.close();
			baos.close();
			return ByteArray.from(baos.toByteArray());
		} catch (IOException e) {
			System.err.println("Not Huffman");
			throw new NoPlantumlCompressionException(e);
		}
	}

}
