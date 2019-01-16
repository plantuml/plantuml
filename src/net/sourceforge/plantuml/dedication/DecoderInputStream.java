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
package net.sourceforge.plantuml.dedication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DecoderInputStream extends InputStream {

	private final TurningBytes message;
	private final TurningBytes sha;
	private final Random rnd;
	private final InputStream source;

	public DecoderInputStream(InputStream source, String s) {
		this.source = source;
		try {
			final byte[] text = s.getBytes("UTF-8");
			final byte[] key = getSignatureSha512(text);
			this.rnd = new Random(getSeed(key));
			this.message = new TurningBytes(text);
			this.sha = new TurningBytes(key);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	private static byte[] getSignatureSha512(byte[] bytes) {
		try {
			final MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.update(bytes);
			return msgDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnsupportedOperationException(e);
		}
	}

	private long getSeed(byte[] bytes) {
		long result = 17;
		for (byte b : bytes) {
			result = result * 37 + b;
		}
		return result;
	}

	private byte getNextByte() {
		return (byte) (rnd.nextInt() ^ message.nextByte() ^ sha.nextByte());
	}

	@Override
	public void close() throws IOException {
		source.close();
	}

	@Override
	public int read() throws IOException {
		int b = source.read();
		if (b == -1) {
			return -1;
		}
		b = (b ^ getNextByte()) & 0xFF;
		return b;
	}

}
