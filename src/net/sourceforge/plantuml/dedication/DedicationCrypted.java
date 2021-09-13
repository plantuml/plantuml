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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;

import net.sourceforge.plantuml.utils.MTRandom;

public class DedicationCrypted implements Dedication {

	private final String argon2;
	private final BigInteger pq;
	private final byte crypted[];
	private final int tinyHash;
	private String solution;

	private long next = 0L;

	public DedicationCrypted(byte crypted[], int tinyHash, String argon2, BigInteger pq) {
		this.crypted = crypted;
		this.pq = pq;
		this.argon2 = argon2;
		this.tinyHash = tinyHash;
	}

	public synchronized BufferedImage getImage(final TinyHashableString sentence) {
		final String line = sentence.getSentence();

		if (line.length() < 40) {
			return null;
		}

		try {
			if (solution == null || line.equals(this.solution) == false) {
				if (System.currentTimeMillis() < next) {
					return null;
				}
				if (this.tinyHash != sentence.tinyHash()) {
					return null;
				}
				this.next = System.currentTimeMillis() + 5000L;
			}

			final byte[] hash1 = Noise.computeArgon2bytes(line.getBytes(UTF_8),
					(pq.toString(35) + line).getBytes(UTF_8));
			final byte[] hash2 = Noise.computeArgon2bytes(line.getBytes(UTF_8),
					(pq.toString(36) + line).getBytes(UTF_8));

			final BlumBlumShub rndBBS = new BlumBlumShub(pq, hash1);
			final MTRandom rndMT = new MTRandom(hash2);

			byte[] current = crypted.clone();
			Noise.shuffle(current, rndMT);
			Noise.xor(current, rndBBS);
			Noise.xor(current, line.getBytes(UTF_8));

			Noise.shuffle(current, rndMT);

			final RBlocks init = RBlocks.readFrom(current, 513);
			final RBlocks decoded = init.change(E, N);

			current = decoded.toByteArray(512);

			Noise.shuffle(current, rndMT);
			Noise.xor(current, rndBBS);

			final String argon = Noise.computeArgon2String(current, (pq.toString(34) + line).getBytes(UTF_8));

			if (this.argon2.equals(argon) == false) {
				return null;
			}
			Noise.shuffle(current, rndMT);
			current = Noise.reverse(current, rndMT.nextInt());

			final BufferedImage img = PSystemDedication.getBufferedImage(new ByteArrayInputStream(current));
			this.solution = line;
			return img;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

	}

}
