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

public class SubBlock {
	public static enum PLANE {
		U, V, Y1, Y2
	};

	public static final int UV = 2;
	public static final int Y = 3;
	public static final int Y_AFTER_Y2 = 0;
	public static final int Y2 = 1;

	public static int planeToType(PLANE plane, Boolean withY2) {
		switch (plane) {
		case Y2:
			return 1;
		case Y1:
			if (withY2)
				return 0;
			else
				return 3;
		case U:
			return 2;
		case V:
			return 2;
		}
		return -1;

	}

	private SubBlock above;

	private int[][] dest;
	private int[][] diff;
	private boolean hasNoZeroToken;
	private SubBlock left;
	private MacroBlock macroBlock;
	private int mode;
	private PLANE plane;
	private int predict[][];
	private int tokens[];

	public SubBlock(MacroBlock macroBlock, SubBlock above, SubBlock left,
			SubBlock.PLANE plane) {
		this.macroBlock = macroBlock;
		this.plane = plane;
		this.above = above;
		this.left = left;
		mode = 0;
		tokens = new int[16];
		for (int z = 0; z < 16; z++)
			tokens[z] = 0;
	}

	private int DCTextra(BoolDecoder bc2, int p[]) throws IOException {
		int v = 0;
		int offset = 0;
		do {
			v += v + bc2.readBool(p[offset]);
			offset++;
		} while (p[offset] > 0);
		return v;
	}

	public void decodeSubBlock(BoolDecoder bc2, int[][][][] coef_probs,
			int ilc, int type, boolean withY2) throws IOException {
		SubBlock sb = this;
		int startAt = 0;
		if (withY2)
			startAt = 1;
		int lc = ilc;
		int c = 0;
		int v = 1;

		boolean skip = false;

		while (!(v == Globals.dct_eob) && c + startAt < 16) {

			if (!skip)
				v = bc2.readTree(Globals.vp8CoefTree,
						coef_probs[type][Globals.vp8CoefBands[c + startAt]][lc]);
			else
				v = bc2.readTreeSkip(
						Globals.vp8CoefTree,
						coef_probs[type][Globals.vp8CoefBands[c + startAt]][lc],
						1);

			int dv = decodeToken(bc2, v);
			lc = 0;
			skip = false;
			if (dv == 1 || dv == -1)
				lc = 1;
			else if (dv > 1 || dv < -1)
				lc = 2;
			else if (dv == Globals.DCT_0)
				skip = true;

			int tokens[] = sb.getTokens();

			if (v != Globals.dct_eob)
				tokens[Globals.vp8defaultZigZag1d[c + startAt]] = dv;
			c++;
		}
		hasNoZeroToken = false;
		for (int x = 0; x < 16; x++)
			if (tokens[x] != 0)
				hasNoZeroToken = true;
	}

	private int decodeToken(BoolDecoder bc2, int v) throws IOException {
		int r = v;

		if (v == Globals.dct_cat1) {
			r = 5 + DCTextra(bc2, Globals.Pcat1);
		}
		if (v == Globals.dct_cat2) {
			r = 7 + DCTextra(bc2, Globals.Pcat2);
		}
		if (v == Globals.dct_cat3) {
			r = 11 + DCTextra(bc2, Globals.Pcat3);
		}
		if (v == Globals.dct_cat4) {
			r = 19 + DCTextra(bc2, Globals.Pcat4);
		}
		if (v == Globals.dct_cat5) {
			r = 35 + DCTextra(bc2, Globals.Pcat5);
		}
		if (v == Globals.dct_cat6) {
			r = 67 + DCTextra(bc2, Globals.Pcat6);
		}
		if (v != Globals.DCT_0 && v != Globals.dct_eob) {
			if (bc2.readBit() > 0)
				r = -r;
		}

		return r;
	}

	public void dequantSubBlock(VP8Frame frame, Integer Dc) {
		SubBlock sb = this;

		int[] adjustedValues = new int[16];
		for (int i = 0; i < 16; i++) {
			int QValue;
			if (plane == PLANE.U || plane == PLANE.V) {
				QValue = frame.getSegmentQuants().getSegQuants()[this.getMacroBlock().getSegmentId()]
						.getUvac_delta_q();
				if (i == 0)
					QValue = frame.getSegmentQuants().getSegQuants()[this.getMacroBlock().getSegmentId()]
							.getUvdc_delta_q();
			} else {
				QValue = frame.getSegmentQuants().getSegQuants()[this.getMacroBlock().getSegmentId()].getY1ac();
				if (i == 0)
					QValue = frame.getSegmentQuants().getSegQuants()[this.getMacroBlock().getSegmentId()]
							.getY1dc();
			}

			int inputValue = sb.getTokens()[i];
			adjustedValues[i] = inputValue * QValue;

		}

		if (Dc != null)
			adjustedValues[0] = Dc;

		int[][] diff = IDCT.idct4x4llm(adjustedValues);
		sb.setDiff(diff);

	}

	public void drawDebug() {
		if (dest != null) {
			dest[0][0] = 128;
			dest[1][0] = 128;
			dest[2][0] = 128;
			dest[3][0] = 128;
			dest[0][0] = 128;
			dest[0][1] = 128;
			dest[0][2] = 128;
			dest[0][3] = 128;
		}

	}

	public void drawDebugH() {
		if (dest != null) {
			dest[0][0] = 0;
			dest[1][0] = 0;
			dest[2][0] = 0;
			dest[3][0] = 0;
		}

	}

	public void drawDebugV() {
		if (dest != null) {
			dest[0][0] = 0;
			dest[0][1] = 0;
			dest[0][2] = 0;
			dest[0][3] = 0;
		}
	}

	public SubBlock getAbove() {

		return above;
	}

	public String getDebugString() {
		String r = new String();
		r = r + "  " + plane;
		if (getMacroBlock().getYMode() == Globals.B_PRED
				&& plane == SubBlock.PLANE.Y1)
			r = r + "\n  " + Globals.getSubBlockModeAsString(mode);
		return r;
	}

	public int[][] getDest() {
		if (dest != null)
			return dest;
		else
			return new int[4][4];
	}

	public int[][] getDiff() {

		return diff;
	}

	public SubBlock getLeft() {

		return left;
	}

	public MacroBlock getMacroBlock() {
		return macroBlock;
	}

	public int[][] getMacroBlockPredict(int intra_mode) {
		if (dest != null)
			return dest;

		else {
			int rv = 127;
			if (intra_mode == Globals.H_PRED)
				rv = 129;
			int r[][] = new int[4][4];
			for (int j = 0; j < 4; j++)
				for (int i = 0; i < 4; i++)
					r[i][j] = rv;
			return r;
		}
	}

	public int getMode() {
		return mode;
	}

	public PLANE getPlane() {
		return plane;
	}

	public int[][] getPredict() {
		if (predict != null)
			return predict;
		return getPredict(Globals.B_DC_PRED, false);
	}

	public int[][] getPredict(int intra_bmode, boolean left) {
		if (dest != null)
			return dest;
		if (predict != null)
			return predict;
		else {
			int rv = 127;

			if ((intra_bmode == Globals.B_TM_PRED
					|| intra_bmode == Globals.B_DC_PRED
					|| intra_bmode == Globals.B_VE_PRED
					|| intra_bmode == Globals.B_HE_PRED
					|| intra_bmode == Globals.B_VR_PRED
					|| intra_bmode == Globals.B_RD_PRED || intra_bmode == Globals.B_HD_PRED)
					&& left)

				rv = 129;
			int r[][] = new int[4][4];
			for (int j = 0; j < 4; j++)
				for (int i = 0; i < 4; i++)
					r[i][j] = rv;
			return r;
		}
	}

	int[] getTokens() {
		return tokens;
	}

	public boolean hasNoZeroToken() {
		return hasNoZeroToken;
	}

	public boolean isDest() {
		if (dest == null)
			return false;
		return true;
	}

	public void predict(VP8Frame frame) {
		SubBlock sb = this;
		SubBlock aboveSb = frame.getAboveSubBlock(sb, sb.getPlane());
		SubBlock leftSb = frame.getLeftSubBlock(sb, sb.getPlane());

		int[] above = new int[4];
		int[] left = new int[4];

		above[0] = aboveSb.getPredict(sb.getMode(), false)[0][3];
		above[1] = aboveSb.getPredict(sb.getMode(), false)[1][3];
		above[2] = aboveSb.getPredict(sb.getMode(), false)[2][3];
		above[3] = aboveSb.getPredict(sb.getMode(), false)[3][3];
		left[0] = leftSb.getPredict(sb.getMode(), true)[3][0];
		left[1] = leftSb.getPredict(sb.getMode(), true)[3][1];
		left[2] = leftSb.getPredict(sb.getMode(), true)[3][2];
		left[3] = leftSb.getPredict(sb.getMode(), true)[3][3];
		SubBlock AL = frame.getLeftSubBlock(aboveSb, sb.getPlane());

		// for above left if left and above is null use left (129?) else use
		// above (127?)
		int al;
		if (!leftSb.isDest() && !aboveSb.isDest()) {

			al = AL.getPredict(sb.getMode(), false)[3][3];
		} else if (!aboveSb.isDest()) {

			al = AL.getPredict(sb.getMode(), false)[3][3];
		} else
			al = AL.getPredict(sb.getMode(), true)[3][3];
		SubBlock AR = frame.getAboveRightSubBlock(sb, sb.plane);
		int ar[] = new int[4];
		ar[0] = AR.getPredict(sb.getMode(), false)[0][3];
		ar[1] = AR.getPredict(sb.getMode(), false)[1][3];
		ar[2] = AR.getPredict(sb.getMode(), false)[2][3];
		ar[3] = AR.getPredict(sb.getMode(), false)[3][3];
		int[][] p = new int[4][4];
		int pp[];
		switch (sb.getMode()) {
		case Globals.B_DC_PRED:
			// System.out.println("B_DC_PRED");
			int expected_dc = 0;

			for (int i = 0; i < 4; i++) {
				expected_dc += above[i];
				expected_dc += left[i];
			}
			expected_dc = (expected_dc + 4) >> 3;

			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					p[x][y] = expected_dc;

			break;
		case Globals.B_TM_PRED:

			// System.out.println("B_TM_PRED");

			// prediction similar to true_motion prediction

			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {

					int pred = above[c] - al + left[r];
					if (pred < 0)
						pred = 0;

					if (pred > 255)
						pred = 255;

					p[c][r] = pred;
				}
			}
			break;
		case Globals.B_VE_PRED:
			// System.out.println("B_VE_PRED");

			int ap[] = new int[4];
			ap[0] = (al + 2 * above[0] + above[1] + 2) >> 2;
			ap[1] = (above[0] + 2 * above[1] + above[2] + 2) >> 2;
			ap[2] = (above[1] + 2 * above[2] + above[3] + 2) >> 2;
			ap[3] = (above[2] + 2 * above[3] + ar[0] + 2) >> 2;

			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {

					p[c][r] = ap[c];

				}
			}
			break;
		case Globals.B_HE_PRED:
			// System.out.println("B_HE_PRED");

			int lp[] = new int[4];
			lp[0] = (al + 2 * left[0] + left[1] + 2) >> 2;
			lp[1] = (left[0] + 2 * left[1] + left[2] + 2) >> 2;
			lp[2] = (left[1] + 2 * left[2] + left[3] + 2) >> 2;
			lp[3] = (left[2] + 2 * left[3] + left[3] + 2) >> 2;

			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {
					p[c][r] = lp[r];
				}
			}
			break;
		case Globals.B_LD_PRED:
			// System.out.println("B_LD_PRED");
			p[0][0] = (above[0] + above[1] * 2 + above[2] + 2) >> 2;
			p[1][0] = p[0][1] = (above[1] + above[2] * 2 + above[3] + 2) >> 2;
			p[2][0] = p[1][1] = p[0][2] = (above[2] + above[3] * 2 + ar[0] + 2) >> 2;
			p[3][0] = p[2][1] = p[1][2] = p[0][3] = (above[3] + ar[0] * 2
					+ ar[1] + 2) >> 2;
			p[3][1] = p[2][2] = p[1][3] = (ar[0] + ar[1] * 2 + ar[2] + 2) >> 2;
			p[3][2] = p[2][3] = (ar[1] + ar[2] * 2 + ar[3] + 2) >> 2;
			p[3][3] = (ar[2] + ar[3] * 2 + ar[3] + 2) >> 2;

			break;
		case Globals.B_RD_PRED:
			// System.out.println("B_RD_PRED");
			pp = new int[9];

			pp[0] = left[3];
			pp[1] = left[2];
			pp[2] = left[1];
			pp[3] = left[0];
			pp[4] = al;
			pp[5] = above[0];
			pp[6] = above[1];
			pp[7] = above[2];
			pp[8] = above[3];

			p[0][3] = (pp[0] + pp[1] * 2 + pp[2] + 2) >> 2;
			p[1][3] = p[0][2] = (pp[1] + pp[2] * 2 + pp[3] + 2) >> 2;
			p[2][3] = p[1][2] = p[0][1] = (pp[2] + pp[3] * 2 + pp[4] + 2) >> 2;
			p[3][3] = p[2][2] = p[1][1] = p[0][0] = (pp[3] + pp[4] * 2 + pp[5] + 2) >> 2;
			p[3][2] = p[2][1] = p[1][0] = (pp[4] + pp[5] * 2 + pp[6] + 2) >> 2;
			p[3][1] = p[2][0] = (pp[5] + pp[6] * 2 + pp[7] + 2) >> 2;
			p[3][0] = (pp[6] + pp[7] * 2 + pp[8] + 2) >> 2;
			break;

		case Globals.B_VR_PRED:
			// System.out.println("B_VR_PRED");
			pp = new int[9];

			pp[0] = left[3];
			pp[1] = left[2];
			pp[2] = left[1];
			pp[3] = left[0];
			pp[4] = al;
			pp[5] = above[0];
			pp[6] = above[1];
			pp[7] = above[2];
			pp[8] = above[3];

			p[0][3] = (pp[1] + pp[2] * 2 + pp[3] + 2) >> 2;
			p[0][2] = (pp[2] + pp[3] * 2 + pp[4] + 2) >> 2;
			p[1][3] = p[0][1] = (pp[3] + pp[4] * 2 + pp[5] + 2) >> 2;
			p[1][2] = p[0][0] = (pp[4] + pp[5] + 1) >> 1;
			p[2][3] = p[1][1] = (pp[4] + pp[5] * 2 + pp[6] + 2) >> 2;
			p[2][2] = p[1][0] = (pp[5] + pp[6] + 1) >> 1;
			p[3][3] = p[2][1] = (pp[5] + pp[6] * 2 + pp[7] + 2) >> 2;
			p[3][2] = p[2][0] = (pp[6] + pp[7] + 1) >> 1;
			p[3][1] = (pp[6] + pp[7] * 2 + pp[8] + 2) >> 2;
			p[3][0] = (pp[7] + pp[8] + 1) >> 1;

			break;
		case Globals.B_VL_PRED:
			// System.out.println("B_VL_PRED");

			p[0][0] = (above[0] + above[1] + 1) >> 1;
			p[0][1] = (above[0] + above[1] * 2 + above[2] + 2) >> 2;
			p[0][2] = p[1][0] = (above[1] + above[2] + 1) >> 1;
			p[1][1] = p[0][3] = (above[1] + above[2] * 2 + above[3] + 2) >> 2;
			p[1][2] = p[2][0] = (above[2] + above[3] + 1) >> 1;
			p[1][3] = p[2][1] = (above[2] + above[3] * 2 + ar[0] + 2) >> 2;
			p[3][0] = p[2][2] = (above[3] + ar[0] + 1) >> 1;
			p[3][1] = p[2][3] = (above[3] + ar[0] * 2 + ar[1] + 2) >> 2;
			p[3][2] = (ar[0] + ar[1] * 2 + ar[2] + 2) >> 2;
			p[3][3] = (ar[1] + ar[2] * 2 + ar[3] + 2) >> 2;

			break;
		case Globals.B_HD_PRED:
			// System.out.println("B_HD_PRED");
			pp = new int[9];
			pp[0] = left[3];
			pp[1] = left[2];
			pp[2] = left[1];
			pp[3] = left[0];
			pp[4] = al;
			pp[5] = above[0];
			pp[6] = above[1];
			pp[7] = above[2];
			pp[8] = above[3];

			p[0][3] = (pp[0] + pp[1] + 1) >> 1;
			p[1][3] = (pp[0] + pp[1] * 2 + pp[2] + 2) >> 2;
			p[0][2] = p[2][3] = (pp[1] + pp[2] + 1) >> 1;
			p[1][2] = p[3][3] = (pp[1] + pp[2] * 2 + pp[3] + 2) >> 2;
			p[2][2] = p[0][1] = (pp[2] + pp[3] + 1) >> 1;
			p[3][2] = p[1][1] = (pp[2] + pp[3] * 2 + pp[4] + 2) >> 2;
			p[2][1] = p[0][0] = (pp[3] + pp[4] + 1) >> 1;
			p[3][1] = p[1][0] = (pp[3] + pp[4] * 2 + pp[5] + 2) >> 2;
			p[2][0] = (pp[4] + pp[5] * 2 + pp[6] + 2) >> 2;
			p[3][0] = (pp[5] + pp[6] * 2 + pp[7] + 2) >> 2;
			break;
		case Globals.B_HU_PRED:
			// System.out.println("B_HU_PRED");

			p[0][0] = (left[0] + left[1] + 1) >> 1;
			p[1][0] = (left[0] + left[1] * 2 + left[2] + 2) >> 2;
			p[2][0] = p[0][1] = (left[1] + left[2] + 1) >> 1;
			p[3][0] = p[1][1] = (left[1] + left[2] * 2 + left[3] + 2) >> 2;
			p[2][1] = p[0][2] = (left[2] + left[3] + 1) >> 1;
			p[3][1] = p[1][2] = (left[2] + left[3] * 2 + left[3] + 2) >> 2;
			p[2][2] = p[3][2] = p[0][3] = p[1][3] = p[2][3] = p[3][3] = left[3];
			break;

		default:
			System.out.println("TODO: " + sb.getMode());
			System.exit(0);
			break;
		}

		sb.setPredict(p);
	}

	public void reconstruct() {
		SubBlock sb = this;

		int r, c;
		int p[][] = sb.getPredict(1, false);

		int dest[][] = new int[4][4];
		int diff[][] = sb.getDiff();

		for (r = 0; r < 4; r++) {
			for (c = 0; c < 4; c++) {
				int a = diff[r][c] + p[r][c];

				if (a < 0)
					a = 0;

				if (a > 255)
					a = 255;

				dest[r][c] = a;

			}

		}

		sb.setDest(dest);
		if (!this.getMacroBlock().isKeepDebugInfo()) {
			sb.diff = null;
			sb.predict = null;
			sb.tokens = null;
		}
	}

	public void setDest(int[][] dest) {
		this.dest = dest;
	}

	public void setDiff(int[][] diff) {
		this.diff = diff;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setPixel(int x, int y, int p) {
		if (dest == null) {
			dest = new int[4][4];
		}
		dest[x][y] = p;
	}

	public void setPredict(int[][] predict) {
		this.predict = predict;

	}

	public String toString() {
		String r = "[";
		for (int x = 0; x < 16; x++)
			r = r + tokens[x] + " ";
		r = r + "]";

		return r;
	}

}
