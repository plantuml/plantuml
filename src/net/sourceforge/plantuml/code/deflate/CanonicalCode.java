package net.sourceforge.plantuml.code.deflate;

/* 
 * Simple DEFLATE decompressor
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/simple-deflate-decompressor
 * https://github.com/nayuki/Simple-DEFLATE-decompressor
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


/**
 * A canonical Huffman code, where the code values for each symbol is derived
 * from a given sequence of code lengths. This data structure is immutable.
 * This could be transformed into an explicit Huffman code tree.
 * <p>Example:</p>
 * <pre>  Code lengths (canonical code):
 *    Symbol A: 1
 *    Symbol B: 0 (no code)
 *    Symbol C: 3
 *    Symbol D: 2
 *    Symbol E: 3
 *  
 *  Generated Huffman codes:
 *    Symbol A: 0
 *    Symbol B: (Absent)
 *    Symbol C: 110
 *    Symbol D: 10
 *    Symbol E: 111
 *  
 *  Huffman code tree:
 *      .
 *     / \
 *    A   .
 *       / \
 *      D   .
 *         / \
 *        C   E</pre>
 */
final class CanonicalCode {
	
	/* 
	 * These arrays store the Huffman codes and values necessary for decoding.
	 * symbolCodeBits contains Huffman codes, each padded with a 1 bit at the
	 * beginning to disambiguate codes of different lengths (e.g. otherwise we
	 * can't distinguish 0b01 from 0b0001). Each symbolCodeBits[i] decodes to its
	 * corresponding symbolValues[i]. Values in symbolCodeBits are strictly increasing.
	 * 
	 * For the example of codeLengths=[1,0,3,2,3], we would have:
	 *   i | symbolCodeBits[i] | symbolValues[i]
	 *   --+-------------------+----------------
	 *   0 |             0b1_0 |               0
	 *   1 |            0b1_10 |               3
	 *   2 |           0b1_110 |               2
	 *   3 |           0b1_111 |               4
	 */
	private int[] symbolCodeBits;
	private int[] symbolValues;
	
	
	
	/**
	 * Constructs a canonical Huffman code from the specified array of symbol code lengths.
	 * Each code length must be non-negative. Code length 0 means no code for the symbol.
	 * The collection of code lengths must represent a proper full Huffman code tree.
	 * <p>Examples of code lengths that result in correct full Huffman code trees:</p>
	 * <ul>
	 *   <li>[1, 1] (result: A=0, B=1)</li>
	 *   <li>[2, 2, 1, 0, 0, 0] (result: A=10, B=11, C=0)</li>
	 *   <li>[3, 3, 3, 3, 3, 3, 3, 3] (result: A=000, B=001, C=010, ..., H=111)</li>
	 * </ul>
	 * <p>Examples of code lengths that result in under-full Huffman code trees:</p>
	 * <ul>
	 *   <li>[0, 2, 0] (result: B=00, unused=01, unused=1)</li>
	 *   <li>[0, 1, 0, 2] (result: B=0, D=10, unused=11)</li>
	 * </ul>
	 * <p>Examples of code lengths that result in over-full Huffman code trees:</p>
	 * <ul>
	 *   <li>[1, 1, 1] (result: A=0, B=1, C=overflow)</li>
	 *   <li>[1, 1, 2, 2, 3, 3, 3, 3] (result: A=0, B=1, C=overflow, ...)</li>
	 * </ul>
	 * @param canonicalCodeLengths array of symbol code lengths (not {@code null})
	 * @throws NullPointerException if the array is {@code null}
	 * @throws IllegalArgumentException if any element is negative, any value exceeds MAX_CODE_LENGTH,
	 * or the collection of code lengths would yield an under-full or over-full Huffman code tree
	 */
	public CanonicalCode(int[] codeLengths) {
		// Check argument values
		Objects.requireNonNull(codeLengths);
		for (int x : codeLengths) {
			if (x < 0)
				throw new IllegalArgumentException("Negative code length");
			if (x > MAX_CODE_LENGTH)
				throw new IllegalArgumentException("Maximum code length exceeded");
		}
		
		// Allocate code values to symbols. Symbols are processed in the order
		// of shortest code length first, breaking ties by lowest symbol value.
		symbolCodeBits = new int[codeLengths.length];
		symbolValues   = new int[codeLengths.length];
		int numSymbolsAllocated = 0;
		int nextCode = 0;
		for (int codeLength = 1; codeLength <= MAX_CODE_LENGTH; codeLength++) {
			nextCode <<= 1;
			int startBit = 1 << codeLength;
			for (int symbol = 0; symbol < codeLengths.length; symbol++) {
				if (codeLengths[symbol] != codeLength)
					continue;
				if (nextCode >= startBit)
					throw new IllegalArgumentException("This canonical code produces an over-full Huffman code tree");
				
				symbolCodeBits[numSymbolsAllocated] = startBit | nextCode;
				symbolValues  [numSymbolsAllocated] = symbol;
				numSymbolsAllocated++;
				nextCode++;
			}
		}
		if (nextCode != 1 << MAX_CODE_LENGTH)
			throw new IllegalArgumentException("This canonical code produces an under-full Huffman code tree");
		
		// Trim unused trailing elements
		symbolCodeBits = Arrays.copyOf(symbolCodeBits, numSymbolsAllocated);
		symbolValues   = Arrays.copyOf(symbolValues  , numSymbolsAllocated);
	}
	
	
	
	/**
	 * Decodes the next symbol from the specified bit input stream based on this
	 * canonical code. The returned symbol value is in the range [0, codeLengths.length).
	 * @param in the bit input stream to read from
	 * @return the next decoded symbol
	 * @throws IOException if an I/O exception occurred
	 */
	public int decodeNextSymbol(BitInputStream in) throws IOException {
		Objects.requireNonNull(in);
		int codeBits = 1;  // The start bit
		while (true) {
			// Accumulate one bit at a time on the right side until a match is
			// found in the symbolCodeBits array. Because the Huffman code tree is
			// full, this loop must terminate after at most MAX_CODE_LENGTH iterations.
			codeBits = codeBits << 1 | in.readNoEof();
			int index = Arrays.binarySearch(symbolCodeBits, codeBits);
			if (index >= 0)
				return symbolValues[index];
		}
	}
	
	
	/**
	 * Returns a string representation of this canonical code,
	 * useful for debugging only, and the format is subject to change.
	 * @return a string representation of this canonical code
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < symbolCodeBits.length; i++) {
			sb.append(String.format("Code %s: Symbol %d%n",
				Integer.toBinaryString(symbolCodeBits[i]).substring(1),
				symbolValues[i]));
		}
		return sb.toString();
	}
	
	
	
	// The maximum Huffman code length allowed in the DEFLATE standard.
	private static final int MAX_CODE_LENGTH = 15;
	
}
