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

import net.sourceforge.plantuml.argon2.Argon2;
import net.sourceforge.plantuml.argon2.Argon2Factory;
import net.sourceforge.plantuml.argon2.model.Argon2Type;
import net.sourceforge.plantuml.utils.MTRandom;

public class Noise {

	private static Argon2 argon2(byte[] buffer, byte[] salt) {
		final Argon2 argon = Argon2Factory.create() //
				.setType(Argon2Type.Argon2id) //
				.setMemory(8) //
				.setSalt(salt.clone()) //
				.setIterations(50) //
				.setPassword(buffer.clone());
		argon.hashNow();
		return argon;
	}

	public static String computeArgon2String(byte[] buffer, byte[] salt) {
		return argon2(buffer, salt).getOutputString();
	}

	public static byte[] computeArgon2bytes(byte[] buffer, byte[] salt) {
		return argon2(buffer, salt).getOutput();
	}

	public static int shortHash(byte[] buffer, byte[] salt) {
		final byte hash[] = argon2(buffer, salt).getOutput();
		int result = 0;
		for (byte b : hash) {
			final int b1 = b & 0x0F;
			final int b2 = (b & 0xF0) >> 4;
			result ^= b1 ^ b2;
		}
		return result;
	}
	
	public static void shuffle(byte[] buffer, MTRandom rnd) {
		for (int i = 0; i < buffer.length; i++) {
			final int r1 = rnd.nextInt();
			final int r2 = rnd.nextInt();
			final int a = Math.abs(r1) % buffer.length;
			final int b = Math.abs(r2) % buffer.length;
			final byte tmp = buffer[a];
			buffer[a] = buffer[b];
			buffer[b] = tmp;
		}
	}
	
	public static void xor(byte[] buffer, byte[] xor) {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] ^= xor[i % xor.length];
		}
	}

	public static void xor(byte[] buffer, BlumBlumShub rnd) {
		for (int i = 0; i < buffer.length; i++) {
			final byte mask = (byte) (rnd.nextRnd(8) & 0xFF);
			buffer[i] = (byte) (buffer[i] ^ mask);
		}
	}
	
	public static byte[] reverse(byte[] buffer, int delta) {
		delta = Math.abs(delta) % buffer.length;
		final byte result[] = new byte[buffer.length];
		for (int i = 0; i < buffer.length; i++)
			result[i] = buffer[(buffer.length - 1 - i + delta) % buffer.length];
		return result;
	}







}
