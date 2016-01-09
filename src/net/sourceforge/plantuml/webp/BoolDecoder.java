/*	This file is part of javavp8decoder.

    javavp8decoder is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    javavp8decoder is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with javavp8decoder.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.plantuml.webp;

import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

public class BoolDecoder {
	int bit_count; /* # of bits shifted out of value, at most 7 */
	ImageInputStream data;
	private long offset; /* pointer to next compressed data byte */
	private int range; /* always identical to encoder's range */
	private int value; /* contains at least 24 significant bits */

	BoolDecoder(ImageInputStream frame, long offset) throws IOException {
		this.data = frame;
		this.offset = offset;
		initBoolDecoder();
	}

	private void initBoolDecoder() throws IOException {

		value = 0; /* value = first 16 input bits */

		data.seek(offset);
		value = data.readUnsignedByte() << 8;
		// value = (data[offset]) << 8;
		offset++;

		range = 255; /* initial range is full */
		bit_count = 0; /* have not yet shifted out any bits */
	}

	public int readBit() throws IOException {
		return readBool(128);
	}

	public int readBool(int probability) throws IOException {

		int bit = 0;
		int split;
		int bigsplit;
		int range = this.range;
		int value = this.value;
		split = 1 + (((range - 1) * probability) >> 8);
		bigsplit = (split << 8);
		range = split;

		if (value >= bigsplit) {
			range = this.range - split;
			value = value - bigsplit;
			bit = 1;
		}

		{
			int count = this.bit_count;
			int shift = Globals.vp8dxBitreaderNorm[range];
			range <<= shift;
			value <<= shift;
			count -= shift;

			if (count <= 0) {
				// data.seek(offset);
				value |= data.readUnsignedByte() << (-count);
				// value |= data[offset] << (-count);
				offset++;
				count += 8;
			}

			this.bit_count = count;
		}
		this.value = value;
		this.range = range;
		return bit;
	}

	/*
	 * Convenience function reads a "literal", that is, a "num_bits" wide
	 * unsigned value whose bits come high- to low-order, with each bit encoded
	 * at probability 128 (i.e., 1/2).
	 */
	public int readLiteral(int num_bits) throws IOException {
		int v = 0;
		while (num_bits-- > 0)
			v = (v << 1) + readBool(128);
		return v;
	}

	int readTree(int t[], /* tree specification */
	int p[] /* corresponding interior node probabilities */
	) throws IOException {
		int i = 0; /* begin at root */

		/* Descend tree until leaf is reached */
		while ((i = t[i + readBool(p[i >> 1])]) > 0) {
		}
		return -i; /* return value is negation of nonpositive index */

	}

	int readTreeSkip(int t[], /* tree specification */
	int p[], /* corresponding interior node probabilities */
	int skip_branches) throws IOException {
		int i = skip_branches * 2; /* begin at root */

		/* Descend tree until leaf is reached */
		while ((i = t[i + readBool(p[i >> 1])]) > 0) {
		}
		return -i; /* return value is negation of nonpositive index */

	}

	public void seek() throws IOException {
		data.seek(offset);
	}

	public String toString() {
		return "bc: " + value;
	}

}
