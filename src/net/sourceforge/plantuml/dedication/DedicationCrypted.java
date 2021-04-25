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

	public synchronized BufferedImage getImage(final String sentence) {
		if (sentence.length() < 40) {
			return null;
		}

		try {
			if (solution == null || sentence.equals(this.solution) == false) {
				if (System.currentTimeMillis() < next) {
					return null;
				}
				final int tinyHash = Noise.shortHash(sentence.getBytes("UTF-8"), N.toByteArray());
				if (this.tinyHash != tinyHash) {
					return null;
				}
				this.next = System.currentTimeMillis() + 5000L;
			}

			final byte[] hash1 = Noise.computeArgon2bytes(sentence.getBytes("UTF-8"),
					(pq.toString(35) + sentence).getBytes("UTF-8"));
			final byte[] hash2 = Noise.computeArgon2bytes(sentence.getBytes("UTF-8"),
					(pq.toString(36) + sentence).getBytes("UTF-8"));

			final BlumBlumShub rndBBS = new BlumBlumShub(pq, hash1);
			final MTRandom rndMT = new MTRandom(hash2);

			byte[] current = crypted.clone();
			Noise.shuffle(current, rndMT);
			Noise.xor(current, rndBBS);
			Noise.xor(current, sentence.getBytes("UTF-8"));

			Noise.shuffle(current, rndMT);

			final RBlocks init = RBlocks.readFrom(current, 513);
			final RBlocks decoded = init.change(E, N);

			current = decoded.toByteArray(512);

			Noise.shuffle(current, rndMT);
			Noise.xor(current, rndBBS);

			final String argon = Noise.computeArgon2String(current, (pq.toString(34) + sentence).getBytes("UTF-8"));

			if (this.argon2.equals(argon) == false) {
				return null;
			}
			Noise.shuffle(current, rndMT);
			current = Noise.reverse(current, rndMT.nextInt());

			final BufferedImage img = PSystemDedication.getBufferedImage(new ByteArrayInputStream(current));
			this.solution = sentence;
			return img;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

	}

}
