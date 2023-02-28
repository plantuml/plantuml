/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2.model;

import static net.sourceforge.plantuml.argon2.Constants.ARGON2_BLOCK_SIZE;
import static net.sourceforge.plantuml.argon2.Constants.ARGON2_QWORDS_IN_BLOCK;

import java.util.Arrays;

import net.sourceforge.plantuml.argon2.Util;

public class Block {
    // ::remove folder when __HAXE__

	/* 128 * 8 Byte QWords */
	public long[] v;

	public Block() {
		v = new long[ARGON2_QWORDS_IN_BLOCK];
	}

	public void fromBytes(byte[] input) {
		assert (input.length == ARGON2_BLOCK_SIZE);

		for (int i = 0; i < v.length; i++) {
			byte[] slice = Arrays.copyOfRange(input, i * 8, (i + 1) * 8);
			v[i] = Util.littleEndianBytesToLong(slice);
		}
	}

	public byte[] toBytes() {
		byte[] result = new byte[ARGON2_BLOCK_SIZE];

		for (int i = 0; i < v.length; i++) {
			byte[] bytes = Util.longToLittleEndianBytes(v[i]);
			System.arraycopy(bytes, 0, result, i * bytes.length, bytes.length);
		}

		return result;
	}

	public void copyBlock(Block other) {
		System.arraycopy(other.v, 0, v, 0, v.length);
	}

	public void xor(Block b1, Block b2) {
		for (int i = 0; i < v.length; i++) {
			v[i] = b1.v[i] ^ b2.v[i];
		}
	}

	public void xor(Block b1, Block b2, Block b3) {
		for (int i = 0; i < v.length; i++) {
			v[i] = b1.v[i] ^ b2.v[i] ^ b3.v[i];
		}
	}

	public void xorWith(Block other) {
		for (int i = 0; i < v.length; i++) {
			v[i] = v[i] ^ other.v[i];
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (long value : v) {
			result.append(Util.bytesToHexString(Util.longToLittleEndianBytes(value)));
		}

		return result.toString();
	}

	void clear() {
		Arrays.fill(v, 0);
	}
}
