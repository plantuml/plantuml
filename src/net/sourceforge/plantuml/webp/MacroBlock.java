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

public class MacroBlock {

	private int filterLevel;
	private boolean keepDebugInfo = false;
	private int segmentId;
	private int skipCoeff;
	private boolean skipInnerLoopFilter;
	SubBlock[][] uSubBlocks;
	private int uVFilterLevel;

	private int uvMode;
	SubBlock[][] vSubBlocks;
	private int x, y;
	SubBlock y2SubBlock;
	private int yMode;
	SubBlock[][] ySubBlocks;

	MacroBlock(int x, int y, boolean keepDebugInfo) {
		this.x = x - 1;
		this.y = y - 1;
		this.keepDebugInfo = keepDebugInfo;

		ySubBlocks = new SubBlock[4][4];
		uSubBlocks = new SubBlock[2][2];
		vSubBlocks = new SubBlock[2][2];
		SubBlock above = null;
		SubBlock left = null;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				left = null;
				above = null;
				if (j > 0)
					left = ySubBlocks[j - 1][i];
				if (i > 0)
					above = ySubBlocks[j][i - 1];
				ySubBlocks[j][i] = new SubBlock(this, above, left,
						SubBlock.PLANE.Y1);
			}
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				left = null;
				above = null;
				if (j > 0)
					left = uSubBlocks[j - 1][i];
				if (i > 0)
					above = uSubBlocks[j][i - 1];
				uSubBlocks[j][i] = new SubBlock(this, above, left,
						SubBlock.PLANE.U);
			}
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				left = null;
				above = null;
				if (j > 0)
					left = vSubBlocks[j - 1][i];
				if (i > 0)
					above = vSubBlocks[j][i - 1];
				vSubBlocks[j][i] = new SubBlock(this, above, left,
						SubBlock.PLANE.V);
			}
		}
		y2SubBlock = new SubBlock(this, null, null, SubBlock.PLANE.Y2);

	}

	public void decodeMacroBlock(VP8Frame frame) throws IOException {
		MacroBlock mb = this;
		if (mb.getSkipCoeff() > 0) {
			if (mb.getYMode() != Globals.B_PRED)
				mb.skipInnerLoopFilter = true;
		} else if (mb.getYMode() != Globals.B_PRED) {
			decodeMacroBlockTokens(frame, true);
		} else {
			decodeMacroBlockTokens(frame, false);
		}
	}

	private void decodeMacroBlockTokens(VP8Frame frame, boolean withY2)
			throws IOException {
		skipInnerLoopFilter = false;
		if (withY2) {
			skipInnerLoopFilter = skipInnerLoopFilter
					| decodePlaneTokens(frame, 1, SubBlock.PLANE.Y2, false);
		}
		skipInnerLoopFilter = skipInnerLoopFilter
				| decodePlaneTokens(frame, 4, SubBlock.PLANE.Y1, withY2);
		skipInnerLoopFilter = skipInnerLoopFilter
				| decodePlaneTokens(frame, 2, SubBlock.PLANE.U, false);
		skipInnerLoopFilter = skipInnerLoopFilter
				| decodePlaneTokens(frame, 2, SubBlock.PLANE.V, false);
		skipInnerLoopFilter = !skipInnerLoopFilter;
	}

	private boolean decodePlaneTokens(VP8Frame frame, int dimentions,
			SubBlock.PLANE plane, boolean withY2) throws IOException {
		MacroBlock mb = this;
		boolean r = false;
		for (int y = 0; y < dimentions; y++) {
			for (int x = 0; x < dimentions; x++) {
				int L = 0;
				int A = 0;
				int lc = 0;
				SubBlock sb = mb.getSubBlock(plane, x, y);
				SubBlock left = frame.getLeftSubBlock(sb, plane);
				SubBlock above = frame.getAboveSubBlock(sb, plane);
				if (left.hasNoZeroToken()) {

					L = 1;
				}

				lc += L;

				if (above.hasNoZeroToken()) {

					A = 1;
				}

				lc += A;
				sb.decodeSubBlock(frame.getTokenBoolDecoder(),
						frame.getCoefProbs(), lc,
						SubBlock.planeToType(plane, withY2), withY2);
				r = r | sb.hasNoZeroToken();
			}
		}
		return r;
	}

	public void dequantMacroBlock(VP8Frame frame) {
		MacroBlock mb = this;
		if (mb.getYMode() != Globals.B_PRED) {
			SubBlock sb = mb.getY2SubBlock();
			int acQValue = frame.getSegmentQuants().getSegQuants()[this.getSegmentId()]
					.getY2ac_delta_q();
			int dcQValue = frame.getSegmentQuants().getSegQuants()[this.getSegmentId()].getY2dc();

			int input[] = new int[16];
			input[0] = sb.getTokens()[0] * dcQValue;

			for (int x = 1; x < 16; x++)
				input[x] = sb.getTokens()[x] * acQValue;

			sb.setDiff(IDCT.iwalsh4x4(input));
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					SubBlock ysb = mb.getYSubBlock(i, j);
					ysb.dequantSubBlock(frame, sb.getDiff()[i][j]);
				}
			}

			mb.predictY(frame);
			mb.predictUV(frame);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					SubBlock uvsb = mb.getUSubBlock(j, i);
					uvsb.dequantSubBlock(frame, null);
					uvsb = mb.getVSubBlock(i, j);
					uvsb.dequantSubBlock(frame, null);
				}
			}
			mb.recon_mb();

		} else {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {
					SubBlock sb = mb.getYSubBlock(i, j);
					sb.dequantSubBlock(frame, null);
					sb.predict(frame);
					sb.reconstruct();
				}
			}
			mb.predictUV(frame);
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					SubBlock sb = mb.getUSubBlock(j, i);
					sb.dequantSubBlock(frame, null);
					sb.reconstruct();
				}
			}
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					SubBlock sb = mb.getVSubBlock(j, i);
					sb.dequantSubBlock(frame, null);
					sb.reconstruct();
				}
			}
		}
	}

	public void drawDebug() {
		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 4; i++) {
				SubBlock sb = ySubBlocks[i][0];
				sb.drawDebugH();
				sb = ySubBlocks[0][j];
				sb.drawDebugV();
			}
	}

	public SubBlock getBottomSubBlock(int x, SubBlock.PLANE plane) {
		if (plane == SubBlock.PLANE.Y1) {
			return ySubBlocks[x][3];
		} else if (plane == SubBlock.PLANE.U) {
			return uSubBlocks[x][1];
		} else if (plane == SubBlock.PLANE.V) {
			return vSubBlocks[x][1];
		} else if (plane == SubBlock.PLANE.Y2) {
			return y2SubBlock;
		}
		return null;
	}

	public String getDebugString() {
		String r = new String();
		r = r + " YMode: " + Globals.getModeAsString(yMode);
		r = r + "\n UVMode: " + Globals.getModeAsString(uvMode);
		r = r + "\n SegmentID: " + segmentId;
		r = r + "\n Filter Level: " + filterLevel;
		r = r + "\n UV Filter Level: " + uVFilterLevel;
		r = r + "\n Skip Coeff: " + skipCoeff;

		return r;
	}

	public int getFilterLevel() {
		return this.filterLevel;
	}

	public SubBlock getLeftSubBlock(int y, SubBlock.PLANE plane) {
		if (plane == SubBlock.PLANE.Y1) {
			return ySubBlocks[0][y];
		}

		else if (plane == SubBlock.PLANE.V) {
			return vSubBlocks[0][y];
		}
		if (plane == SubBlock.PLANE.Y2) {
			return y2SubBlock;
		} else if (plane == SubBlock.PLANE.U) {
			return uSubBlocks[0][y];
		}
		return null;
	}

	public SubBlock getRightSubBlock(int y, SubBlock.PLANE plane) {
		if (plane == SubBlock.PLANE.Y1) {
			return ySubBlocks[3][y];
		} else if (plane == SubBlock.PLANE.U) {
			return uSubBlocks[1][y];
		} else if (plane == SubBlock.PLANE.V) {
			return vSubBlocks[1][y];
		} else if (plane == SubBlock.PLANE.Y2) {
			return y2SubBlock;
		}
		return null;
	}

	public int getSkipCoeff() {
		return skipCoeff;
	}

	public SubBlock getSubBlock(SubBlock.PLANE plane, int i, int j) {
		switch (plane) {
		case Y1:
			return getYSubBlock(i, j);
		case U:
			return getUSubBlock(i, j);

		case V:
			return getVSubBlock(i, j);
		case Y2:
			return getY2SubBlock();
		}
		return null;
	}

	public int getSubblockX(SubBlock sb) {
		if (sb.getPlane() == SubBlock.PLANE.Y1) {
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					if (ySubBlocks[x][y] == sb)
						return x;
		} else if (sb.getPlane() == SubBlock.PLANE.U) {
			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++)
					if (uSubBlocks[x][y] == sb)
						return x;
		} else if (sb.getPlane() == SubBlock.PLANE.V) {
			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++)
					if (vSubBlocks[x][y] == sb)
						return x;
		} else if (sb.getPlane() == SubBlock.PLANE.Y2) {
			return 0;
		}

		return -100;

	}

	public int getSubblockY(SubBlock sb) {
		if (sb.getPlane() == SubBlock.PLANE.Y1) {
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					if (ySubBlocks[x][y] == sb)
						return y;
		} else if (sb.getPlane() == SubBlock.PLANE.U) {
			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++)
					if (uSubBlocks[x][y] == sb)
						return y;
		} else if (sb.getPlane() == SubBlock.PLANE.V) {
			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++)
					if (vSubBlocks[x][y] == sb)
						return y;
		} else if (sb.getPlane() == SubBlock.PLANE.Y2) {
			return 0;
		}

		return -100;
	}

	public SubBlock getUSubBlock(int i, int j) {
		return uSubBlocks[i][j];
	}

	public int getUVFilterLevel() {
		return this.uVFilterLevel;
	}

	public int getUvMode() {
		return uvMode;
	}

	public SubBlock getVSubBlock(int i, int j) {
		return vSubBlocks[i][j];
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public SubBlock getY2SubBlock() {
		return y2SubBlock;
	}

	public int getYMode() {
		return yMode;
	}

	public SubBlock getYSubBlock(int i, int j) {
		return ySubBlocks[i][j];
	}

	public boolean isKeepDebugInfo() {
		return keepDebugInfo;
	}

	public boolean isSkip_inner_lf() {
		return skipInnerLoopFilter;
	}

	public void predictUV(VP8Frame frame) {
		MacroBlock aboveMb = frame.getMacroBlock(x, y - 1);
		MacroBlock leftMb = frame.getMacroBlock(x - 1, y);

		switch (this.uvMode) {
		case Globals.DC_PRED:
			// System.out.println("UV DC_PRED");

			boolean up_available = false;
			boolean left_available = false;
			int Uaverage = 0;
			int Vaverage = 0;
			int expected_udc = 0;
			int expected_vdc = 0;
			if (x > 0)
				left_available = true;
			if (y > 0)
				up_available = true;
			if (up_available || left_available) {
				if (up_available) {
					for (int j = 0; j < 2; j++) {
						SubBlock usb = aboveMb.getUSubBlock(j, 1);
						SubBlock vsb = aboveMb.getVSubBlock(j, 1);
						for (int i = 0; i < 4; i++) {
							Uaverage += usb.getDest()[i][3];
							Vaverage += vsb.getDest()[i][3];
						}
					}
				}

				if (left_available) {
					for (int j = 0; j < 2; j++) {
						SubBlock usb = leftMb.getUSubBlock(1, j);
						SubBlock vsb = leftMb.getVSubBlock(1, j);
						for (int i = 0; i < 4; i++) {
							Uaverage += usb.getDest()[3][i];
							Vaverage += vsb.getDest()[3][i];
						}
					}
				}

				int shift = 2;
				if (up_available)
					shift++;
				if (left_available)
					shift++;

				expected_udc = (Uaverage + (1 << (shift - 1))) >> shift;
				expected_vdc = (Vaverage + (1 << (shift - 1))) >> shift;
			} else {
				expected_udc = 128;
				expected_vdc = 128;
			}

			int ufill[][] = new int[4][4];
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					ufill[x][y] = expected_udc;

			int vfill[][] = new int[4][4];
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					vfill[x][y] = expected_vdc;

			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++) {
					SubBlock usb = uSubBlocks[x][y];
					SubBlock vsb = vSubBlocks[x][y];
					usb.setPredict(ufill);
					vsb.setPredict(vfill);
				}

			break;
		case Globals.V_PRED:
			// System.out.println("UV V_PRED");

			SubBlock[] aboveUSb = new SubBlock[2];
			SubBlock[] aboveVSb = new SubBlock[2];
			for (int x = 0; x < 2; x++) {
				aboveUSb[x] = aboveMb.getUSubBlock(x, 1);
				aboveVSb[x] = aboveMb.getVSubBlock(x, 1);
			}

			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++) {
					SubBlock usb = uSubBlocks[y][x];
					SubBlock vsb = vSubBlocks[y][x];
					int ublock[][] = new int[4][4];
					int vblock[][] = new int[4][4];
					for (int j = 0; j < 4; j++)
						for (int i = 0; i < 4; i++) {
							ublock[j][i] = aboveUSb[y]
									.getMacroBlockPredict(Globals.V_PRED)[j][3];
							vblock[j][i] = aboveVSb[y]
									.getMacroBlockPredict(Globals.V_PRED)[j][3];
						}
					usb.setPredict(ublock);
					vsb.setPredict(vblock);
				}

			break;

		case Globals.H_PRED:
			// System.out.println("UV H_PRED");

			SubBlock[] leftUSb = new SubBlock[2];
			SubBlock[] leftVSb = new SubBlock[2];
			for (int x = 0; x < 2; x++) {
				leftUSb[x] = leftMb.getUSubBlock(1, x);
				leftVSb[x] = leftMb.getVSubBlock(1, x);
			}

			for (int y = 0; y < 2; y++)
				for (int x = 0; x < 2; x++) {
					SubBlock usb = uSubBlocks[x][y];
					SubBlock vsb = vSubBlocks[x][y];
					int ublock[][] = new int[4][4];
					int vblock[][] = new int[4][4];
					for (int j = 0; j < 4; j++)
						for (int i = 0; i < 4; i++) {
							ublock[i][j] = leftUSb[y]
									.getMacroBlockPredict(Globals.H_PRED)[3][j];
							vblock[i][j] = leftVSb[y]
									.getMacroBlockPredict(Globals.H_PRED)[3][j];
						}
					usb.setPredict(ublock);
					vsb.setPredict(vblock);
				}

			break;
		case Globals.TM_PRED:
			// TODO:
			// System.out.println("UV TM_PRED MB");
			MacroBlock ALMb = frame.getMacroBlock(x - 1, y - 1);
			SubBlock ALUSb = ALMb.getUSubBlock(1, 1);
			int alu = ALUSb.getDest()[3][3];
			SubBlock ALVSb = ALMb.getVSubBlock(1, 1);
			int alv = ALVSb.getDest()[3][3];

			aboveUSb = new SubBlock[2];
			leftUSb = new SubBlock[2];
			aboveVSb = new SubBlock[2];
			leftVSb = new SubBlock[2];
			for (int x = 0; x < 2; x++) {
				aboveUSb[x] = aboveMb.getUSubBlock(x, 1);
				leftUSb[x] = leftMb.getUSubBlock(1, x);
				aboveVSb[x] = aboveMb.getVSubBlock(x, 1);
				leftVSb[x] = leftMb.getVSubBlock(1, x);
			}

			for (int b = 0; b < 2; b++) {
				for (int a = 0; a < 4; a++) {
					for (int d = 0; d < 2; d++) {
						for (int c = 0; c < 4; c++) {

							int upred = leftUSb[b].getDest()[3][a]
									+ aboveUSb[d].getDest()[c][3] - alu;
							upred = Globals.clamp(upred, 255);
							uSubBlocks[d][b].setPixel(c, a, upred);

							int vpred = leftVSb[b].getDest()[3][a]
									+ aboveVSb[d].getDest()[c][3] - alv;
							vpred = Globals.clamp(vpred, 255);
							vSubBlocks[d][b].setPixel(c, a, vpred);

						}
					}

				}
			}

			break;
		default:
			System.out.println("TODO predict_mb_uv: " + this.yMode);
			System.exit(0);
		}
	}

	public void predictY(VP8Frame frame) {
		MacroBlock aboveMb = frame.getMacroBlock(x, y - 1);
		MacroBlock leftMb = frame.getMacroBlock(x - 1, y);

		switch (this.yMode) {
		case Globals.DC_PRED:
			// System.out.println("DC_PRED");
			boolean up_available = false;
			boolean left_available = false;

			int average = 0;
			int expected_dc = 0;
			if (x > 0)
				left_available = true;
			if (y > 0)
				up_available = true;

			if (up_available || left_available) {
				if (up_available) {
					for (int j = 0; j < 4; j++) {
						SubBlock sb = aboveMb.getYSubBlock(j, 3);
						for (int i = 0; i < 4; i++) {
							average += sb.getDest()[i][3];
						}
					}
				}

				if (left_available) {
					for (int j = 0; j < 4; j++) {
						SubBlock sb = leftMb.getYSubBlock(3, j);
						for (int i = 0; i < 4; i++) {
							average += sb.getDest()[3][i];
						}
					}
				}

				int shift = 3;
				if (up_available)
					shift++;
				if (left_available)
					shift++;

				expected_dc = (average + (1 << (shift - 1))) >> shift;
			} else {
				expected_dc = 128;
			}

			int fill[][] = new int[4][4];
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++)
					fill[x][y] = expected_dc;
			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++) {
					SubBlock sb = ySubBlocks[x][y];
					sb.setPredict(fill);
				}

			break;
		case Globals.V_PRED:
			// System.out.println("V_PRED");

			SubBlock[] aboveYSb = new SubBlock[4];
			for (int x = 0; x < 4; x++)
				aboveYSb[x] = aboveMb.getYSubBlock(x, 3);

			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 4; x++) {
					SubBlock sb = ySubBlocks[x][y];
					int block[][] = new int[4][4];
					for (int j = 0; j < 4; j++)
						for (int i = 0; i < 4; i++) {
							block[i][j] = aboveYSb[x].getPredict(
									Globals.B_VE_PRED, false)[i][3];
						}
					sb.setPredict(block);

				}
			}

			break;

		case Globals.H_PRED:
			// System.out.println("H_PRED");

			SubBlock[] leftYSb = new SubBlock[4];
			for (int x = 0; x < 4; x++)
				leftYSb[x] = leftMb.getYSubBlock(3, x);

			for (int y = 0; y < 4; y++)
				for (int x = 0; x < 4; x++) {
					SubBlock sb = ySubBlocks[x][y];
					int block[][] = new int[4][4];
					for (int j = 0; j < 4; j++)
						for (int i = 0; i < 4; i++) {
							block[i][j] = leftYSb[y].getPredict(
									Globals.B_DC_PRED, true)[3][j];
						}
					sb.setPredict(block);
				}

			SubBlock[] leftUSb = new SubBlock[2];
			for (int x = 0; x < 2; x++)
				leftUSb[x] = leftMb.getYSubBlock(1, x);

			break;
		case Globals.TM_PRED:
			// System.out.println("TM_PRED MB");
			MacroBlock ALMb = frame.getMacroBlock(x - 1, y - 1);
			SubBlock ALSb = ALMb.getYSubBlock(3, 3);
			int al = ALSb.getDest()[3][3];

			aboveYSb = new SubBlock[4];
			leftYSb = new SubBlock[4];
			for (int x = 0; x < 4; x++)
				aboveYSb[x] = aboveMb.getYSubBlock(x, 3);
			for (int x = 0; x < 4; x++)
				leftYSb[x] = leftMb.getYSubBlock(3, x);
			fill = new int[4][4];

			for (int b = 0; b < 4; b++) {
				for (int a = 0; a < 4; a++) {

					for (int d = 0; d < 4; d++) {
						for (int c = 0; c < 4; c++) {

							int pred = leftYSb[b].getDest()[3][a]
									+ aboveYSb[d].getDest()[c][3] - al;

							ySubBlocks[d][b].setPixel(c, a,
									Globals.clamp(pred, 255));

						}
					}

				}
			}

			break;
		default:
			System.out.println("TODO predict_mb_y: " + this.yMode);
			System.exit(0);
		}
	}

	public void recon_mb() {
		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 4; i++) {
				SubBlock sb = ySubBlocks[i][j];
				sb.reconstruct();
			}

		for (int j = 0; j < 2; j++)
			for (int i = 0; i < 2; i++) {
				SubBlock sb = uSubBlocks[i][j];
				sb.reconstruct();
			}
		for (int j = 0; j < 2; j++)
			for (int i = 0; i < 2; i++) {
				SubBlock sb = vSubBlocks[i][j];
				sb.reconstruct();
			}

	}

	public void setFilterLevel(int value) {
		this.filterLevel = value;
	}

	public void setSegmentId(int value) {
		this.segmentId = value;
	}

	public void setSkipCoeff(int mbSkipCoeff) {
		skipCoeff = mbSkipCoeff;
	}

	public void setUVFilterLevel(int value) {
		this.uVFilterLevel = value;
	}

	public void setUvMode(int mode) {
		this.uvMode = mode;
	}

	public void setYMode(int yMode) {
		this.yMode = yMode;
	}

	public String toString() {
		return "x: " + x + "y: " + y;
	}

	public int getSegmentId() {
		return segmentId;
	}
}
