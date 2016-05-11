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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class QBlock {

	private final BigInteger big;

	public static QBlock read(InputStream source, int size) throws IOException {
		final byte[] block = new byte[size + 1];
		for (int i = 0; i < size; i++) {
			final int read = source.read();
			if (read == -1) {
				if (i == 0) {
					return null;
				}
				break;
			}
			block[i + 1] = (byte) read;
		}
		return new QBlock(new BigInteger(block));
	}

	public QBlock(BigInteger number) {
		this.big = number;
	}

	public QBlock change(BigInteger E, BigInteger N) {
		final BigInteger changed = big.modPow(E, N);
		return new QBlock(changed);
	}

	@Override
	public String toString() {
		return big.toByteArray().length + " " + big.toString();
	}

	public void write(OutputStream os, int size) throws IOException {
		final byte[] data = big.toByteArray();
		final int start = data.length - size;
		if (start < 0) {
			for (int i = 0; i < -start; i++) {
				os.write(0);
			}
		}
		for (int i = Math.max(start, 0); i < data.length; i++) {
			int b = data[i];
			os.write(b);
		}

	}

}
