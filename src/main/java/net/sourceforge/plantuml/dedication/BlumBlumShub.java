package net.sourceforge.plantuml.dedication;

import java.math.BigInteger;

public class BlumBlumShub {
	// ::remove folder when __CORE__

	private static final BigInteger two = BigInteger.valueOf(2L);

	private BigInteger state;
	private final BigInteger pq;

	public BlumBlumShub(BigInteger pq, byte[] seed) {
		this.pq = pq;
		this.state = new BigInteger(1, seed).mod(pq);
	}

	public int nextRnd(int numBits) {
		int result = 0;
		for (int i = numBits; i != 0; --i) {
			state = state.modPow(two, pq);
			final int bit = state.testBit(0) ? 1 : 0;
			result = (result << 1) | bit;
		}
		return result;
	}

}
