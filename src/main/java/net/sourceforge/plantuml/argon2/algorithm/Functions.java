/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2.algorithm;

import static net.sourceforge.plantuml.argon2.Constants.ARGON2_BLOCK_SIZE;
import static net.sourceforge.plantuml.argon2.Constants.ARGON2_PREHASH_DIGEST_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.ARGON2_PREHASH_SEED_LENGTH;

import net.sourceforge.plantuml.argon2.Util;
import net.sourceforge.plantuml.argon2.blake2.Blake2b;
import net.sourceforge.plantuml.argon2.model.Block;

class Functions {

	/**
	 * H0 = H64(p, \u03c4, m, t, v, y, |P|, P, |S|, S, |L|, K, |X|, X) -> 64 byte
	 * (ARGON2_PREHASH_DIGEST_LENGTH)
	 */
	static byte[] initialHash(byte[] lanes, byte[] outputLength, byte[] memory, byte[] iterations, byte[] version,
			byte[] type, byte[] passwordLength, byte[] password, byte[] saltLength, byte[] salt, byte[] secretLength,
			byte[] secret, byte[] additionalLength, byte[] additional) {

		Blake2b.Param params = new Blake2b.Param().setDigestLength(ARGON2_PREHASH_DIGEST_LENGTH);

		final Blake2b blake2b = Blake2b.Digest.newInstance(params);

		blake2b.update(lanes);
		blake2b.update(outputLength);
		blake2b.update(memory);
		blake2b.update(iterations);
		blake2b.update(version);
		blake2b.update(type);

		blake2b.update(passwordLength);
		if (password != null) {
			blake2b.update(password);
		}

		blake2b.update(saltLength);
		if (salt != null) {
			blake2b.update(salt);
		}

		blake2b.update(secretLength);
		if (secret != null) {
			blake2b.update(secret);
		}

		blake2b.update(additionalLength);
		if (additional != null) {
			blake2b.update(additional);
		}

		byte[] blake2hash = blake2b.digest();
		assert (blake2hash.length == 64);

		return blake2hash;
	}

	/**
	 * H' - blake2bLong - variable length hash function
	 */
	static byte[] blake2bLong(byte[] input, int outputLength) {

		assert (input.length == ARGON2_PREHASH_SEED_LENGTH || input.length == ARGON2_BLOCK_SIZE);

		byte[] result = new byte[outputLength];
		byte[] outlenBytes = Util.intToLittleEndianBytes(outputLength);

		int blake2bLength = 64;

		if (outputLength <= blake2bLength) {
			result = blake2b(input, outlenBytes, outputLength);
		} else {
			byte[] outBuffer;

			/* V1 */
			outBuffer = blake2b(input, outlenBytes, blake2bLength);
			System.arraycopy(outBuffer, 0, result, 0, blake2bLength / 2);

			int r = (outputLength / 32) + (outputLength % 32 == 0 ? 0 : 1) - 2;

			int position = blake2bLength / 2;
			for (int i = 2; i <= r; i++, position += blake2bLength / 2) {
				/* V2 to Vr */
				outBuffer = blake2b(outBuffer, null, blake2bLength);
				System.arraycopy(outBuffer, 0, result, position, blake2bLength / 2);
			}

			int lastLength = outputLength - 32 * r;

			/* Vr+1 */
			outBuffer = blake2b(outBuffer, null, lastLength);
			System.arraycopy(outBuffer, 0, result, position, lastLength);
		}

		assert (result.length == outputLength);
		return result;
	}

	private static byte[] blake2b(byte[] input, byte[] outlenBytes, int outputLength) {
		Blake2b.Param params = new Blake2b.Param().setDigestLength(outputLength);

		final Blake2b blake2b = Blake2b.Digest.newInstance(params);

		if (outlenBytes != null)
			blake2b.update(outlenBytes);

		blake2b.update(input);

		return blake2b.digest();
	}

	static void roundFunction(Block block, int v0, int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8,
			int v9, int v10, int v11, int v12, int v13, int v14, int v15) {

		F(block, v0, v4, v8, v12);
		F(block, v1, v5, v9, v13);
		F(block, v2, v6, v10, v14);
		F(block, v3, v7, v11, v15);

		F(block, v0, v5, v10, v15);
		F(block, v1, v6, v11, v12);
		F(block, v2, v7, v8, v13);
		F(block, v3, v4, v9, v14);
	}

	private static void F(Block block, int a, int b, int c, int d) {
		fBlaMka(block, a, b);
		rotr64(block, d, a, 32);

		fBlaMka(block, c, d);
		rotr64(block, b, c, 24);

		fBlaMka(block, a, b);
		rotr64(block, d, a, 16);

		fBlaMka(block, c, d);
		rotr64(block, b, c, 63);
	}

	/* designed by the Lyra PHC team */
	/*
	 * a <- a + b + 2*aL*bL + == addition modulo 2^64 aL = least 32 bit
	 */
	private static void fBlaMka(Block block, int x, int y) {
		final long m = 0xFFFFFFFFL;
		final long xy = (block.v[x] & m) * (block.v[y] & m);

		block.v[x] = block.v[x] + block.v[y] + 2 * xy;
	}

	private static void rotr64(Block block, int v, int w, long c) {
		final long temp = block.v[v] ^ block.v[w];
		block.v[v] = (temp >>> c) | (temp << (64 - c));
	}
}
