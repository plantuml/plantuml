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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class DecoderInputStream extends FilterInputStream {

	private final byte key[];
	private int idx;
	private final Random rnd;

	public DecoderInputStream(InputStream source, byte key[]) {
		super(source);
		this.key = key;
		this.rnd = new Random(getSeed());
	}

	private long getSeed() {
		long result = 17;
		for (byte b : key) {
			result = result * 37 + b;
		}
		return result;
	}

	private byte getNextByte() {
		for (int i = 0; i < nextKey(); i++) {
			rnd.nextInt();
		}
		return (byte) rnd.nextInt();
	}

	private int nextKey() {
		final int result = key[idx];
		idx++;
		if (idx >= key.length) {
			idx = 0;
		}
		if (result < 0) {
			return result + 256;
		}
		return result;
	}

	@Override
	public int read() throws IOException {
		int b = super.read();
		if (b == -1) {
			return -1;
		}
		b = b ^ getNextByte();
		return b;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		final int nb = super.read(b, off, len);
		if (nb == -1) {
			return nb;
		}
		for (int i = 0; i < nb; i++) {
			b[i + off] = (byte) (b[i + off] ^ getNextByte());
		}
		return nb;
	}

}
