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

public class IDCT {
	/* IDCT implementation */
	private static final int cospi8sqrt2minus1 = 20091;

	private static final int sinpi8sqrt2 = 35468;

	public static int[][] idct4x4llm(int input[]) {

		int i;
		int a1, b1, c1, d1;
		int offset = 0;

		int[] output = new int[16];
		int temp1, temp2;

		for (i = 0; i < 4; i++) {
			a1 = input[offset + 0] + input[offset + 8];
			b1 = input[offset + 0] - input[offset + 8];

			temp1 = (input[offset + 4] * sinpi8sqrt2) >> 16;
			temp2 = input[offset + 12]
					+ ((input[offset + 12] * cospi8sqrt2minus1) >> 16);

			c1 = temp1 - temp2;

			temp1 = input[offset + 4]
					+ ((input[offset + 4] * cospi8sqrt2minus1) >> 16);
			temp2 = (input[offset + 12] * sinpi8sqrt2) >> 16;
			d1 = temp1 + temp2;

			output[offset + (0 * 4)] = a1 + d1;
			output[offset + (3 * 4)] = a1 - d1;
			output[offset + (1 * 4)] = b1 + c1;
			output[offset + (2 * 4)] = b1 - c1;

			offset++;
		}

		int diffo = 0;
		int diff[][] = new int[4][4];
		offset = 0;
		for (i = 0; i < 4; i++) {
			a1 = output[(offset * 4) + 0] + output[(offset * 4) + 2];
			b1 = output[(offset * 4) + 0] - output[(offset * 4) + 2];

			temp1 = (output[(offset * 4) + 1] * sinpi8sqrt2) >> 16;
			temp2 = output[(offset * 4) + 3]
					+ ((output[(offset * 4) + 3] * cospi8sqrt2minus1) >> 16);
			c1 = temp1 - temp2;

			temp1 = output[(offset * 4) + 1]
					+ ((output[(offset * 4) + 1] * cospi8sqrt2minus1) >> 16);
			temp2 = (output[(offset * 4) + 3] * sinpi8sqrt2) >> 16;
			d1 = temp1 + temp2;

			output[(offset * 4) + 0] = (a1 + d1 + 4) >> 3;
			output[(offset * 4) + 3] = (a1 - d1 + 4) >> 3;
			output[(offset * 4) + 1] = (b1 + c1 + 4) >> 3;
			output[(offset * 4) + 2] = (b1 - c1 + 4) >> 3;

			diff[0][diffo] = (a1 + d1 + 4) >> 3;
			diff[3][diffo] = (a1 - d1 + 4) >> 3;
			diff[1][diffo] = (b1 + c1 + 4) >> 3;
			diff[2][diffo] = (b1 - c1 + 4) >> 3;

			offset++;
			diffo++;
		}

		return diff;

	}

	public static int[][] iwalsh4x4(int input[]) {
		int i;
		int a1, b1, c1, d1;
		int a2, b2, c2, d2;

		int[] output = new int[16];
		int diff[][] = new int[4][4];
		int offset = 0;
		for (i = 0; i < 4; i++) {
			a1 = input[offset + 0] + input[offset + 12];
			b1 = input[offset + 4] + input[offset + 8];
			c1 = input[offset + 4] - input[offset + 8];
			d1 = input[offset + 0] - input[offset + 12];

			output[offset + 0] = a1 + b1;
			output[offset + 4] = c1 + d1;
			output[offset + 8] = a1 - b1;
			output[offset + 12] = d1 - c1;
			offset++;
		}

		offset = 0;

		for (i = 0; i < 4; i++) {
			a1 = output[offset + 0] + output[offset + 3];
			b1 = output[offset + 1] + output[offset + 2];
			c1 = output[offset + 1] - output[offset + 2];
			d1 = output[offset + 0] - output[offset + 3];

			a2 = a1 + b1;
			b2 = c1 + d1;
			c2 = a1 - b1;
			d2 = d1 - c1;
			output[offset + 0] = (a2 + 3) >> 3;
			output[offset + 1] = (b2 + 3) >> 3;
			output[offset + 2] = (c2 + 3) >> 3;
			output[offset + 3] = (d2 + 3) >> 3;
			diff[0][i] = (a2 + 3) >> 3;
			diff[1][i] = (b2 + 3) >> 3;
			diff[2][i] = (c2 + 3) >> 3;
			diff[3][i] = (d2 + 3) >> 3;
			offset += 4;
		}

		return diff;

	}
}
