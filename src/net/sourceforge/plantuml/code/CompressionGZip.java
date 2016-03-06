/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.code;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressionGZip implements Compression {

	class MyGZIPOutputStream extends GZIPOutputStream {

		public MyGZIPOutputStream(OutputStream baos) throws IOException {
			super(baos);
			def.setLevel(9);
		}

	}

	public byte[] compress(byte[] in) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			final GZIPOutputStream gz = new MyGZIPOutputStream(baos);
			gz.write(in);
			gz.close();
			baos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new IllegalStateException(e.toString());
		}
	}

	public byte[] decompress(byte[] in) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final ByteArrayInputStream bais = new ByteArrayInputStream(in);
		final GZIPInputStream gz = new GZIPInputStream(bais);
		int read;
		while ((read = gz.read()) != -1) {
			baos.write(read);
		}
		gz.close();
		bais.close();
		baos.close();
		return baos.toByteArray();
	}

}
