package net.sourceforge.plantuml.klimt.awt;

/**
 * Pure Java port of the part of Oracle's medialib (the native library behind
 * <code>java.awt.image.AffineTransformOp</code>) that scales an image by a
 * uniform factor, so that the C build gives the very same pixels as the JVM:
 * <code>mlib_AffineEdges</code> (which positions the destination pixels in the
 * source image, in 16.16 fixed point), the bilinear and nearest-neighbor
 * kernels and the processing of the edges (which uses double arithmetic).
 * <p>
 * Only what PlantUML needs is ported: a transformation which is a scale
 * (positive, identical on both axes, no translation) of an ARGB image.
 * Intermediate values keep the exact order of operations of the C code,
 * because a difference of one unit in the last place may change the pixel.
 */
final class MlibAffine {
	// ::remove file when JAVA8

	private static final int SHIFT = 16;
	private static final int PREC = 1 << SHIFT;
	private static final int MASK = PREC - 1;

	private MlibAffine() {
	}

	/** What <code>mlib_AffineEdges</code> computes: one entry per destination row. */
	private static final class Edges {
		int[] left;
		int[] right;
		int[] xStarts;
		int[] yStarts;
		int yStart = 0;
		int yFinish = -1;
		int dX;
		int dY;
	}

	/** Same as <code>(mlib_s32) value</code> saturated. */
	private static int sat32(double value) {
		if (value >= Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		if (value <= Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		return (int) value;
	}

	/**
	 * Port of <code>mlib_AffineEdges</code> for the matrix (s, 0, 0, 0, s, 0).
	 * 
	 * @param kw      the width of the filter kernel (1: nearest, 2: bilinear)
	 * @param wholeSrc true for the second call, which covers the whole source
	 *                image (the pixels to be extended)
	 */
	private static Edges edges(int srcW, int srcH, int dstW, int dstH, double s, int kw, boolean wholeSrc) {
		final Edges result = new Edges();
		final int kw1 = 0;
		double delta = 0.0;
		final double xClip;
		final double yClip;
		final double wClip;
		final double hClip;
		if (wholeSrc) {
			xClip = 0;
			yClip = 0;
			wClip = srcW;
			hClip = srcH;
		} else {
			if (kw > 1)
				delta = -0.5;
			xClip = kw1 - delta;
			yClip = kw1 - delta;
			wClip = srcW - ((kw - 1) - (kw1 - delta));
			hClip = srcH - ((kw - 1) - (kw1 - delta));
		}
		if (srcW >= (1 << 15) || srcH >= (1 << 15) || dstH <= 0)
			return result;

		final int[] leftEdges = new int[dstH];
		final int[] rightEdges = new int[dstH];
		final int[] xStarts = new int[dstH];
		final int[] yStarts = new int[dstH];
		result.left = leftEdges;
		result.right = rightEdges;
		result.xStarts = xStarts;
		result.yStarts = yStarts;

		if (xClip >= wClip || yClip >= hClip)
			return result;

		// The matrix is (a, b, tx, c, d, ty) = (s, 0, 0, 0, s, 0)
		final double a = s;
		final double b = 0.0;
		final double c = 0.0;
		final double d = s;
		double tx = 0.0;
		double ty = 0.0;
		final double div0 = a * d - b * c;
		if (div0 == 0.0)
			return result;

		final double a2 = d;
		final double b2 = -b;
		final double tx2 = (-d * tx + b * ty);
		final double c2 = -c;
		final double d2 = a;
		final double ty2 = (c * tx - a * ty);

		final double dx = a2;
		final double dy = c2;

		tx -= 0.5;
		ty -= 0.5;

		final double[][] coords = new double[4][2];
		coords[0][0] = xClip * a + yClip * b + tx;
		coords[0][1] = xClip * c + yClip * d + ty;
		coords[2][0] = wClip * a + hClip * b + tx;
		coords[2][1] = wClip * c + hClip * d + ty;
		if (div0 > 0) {
			coords[1][0] = wClip * a + yClip * b + tx;
			coords[1][1] = wClip * c + yClip * d + ty;
			coords[3][0] = xClip * a + hClip * b + tx;
			coords[3][1] = xClip * c + hClip * d + ty;
		} else {
			return result; // not needed: a scale is positive
		}

		int topIdx = 0;
		for (int i = 1; i < 4; i++)
			if (coords[i][1] < coords[topIdx][1])
				topIdx = i;

		final double dTop = coords[topIdx][1];
		int top = sat32(dTop);
		int bot = -1;
		if (top >= dstH)
			return result;

		if (dTop >= 0.0) {
			if (dTop == top) {
				double xLeft = coords[topIdx][0];
				double xRight = coords[topIdx][0];
				int nextIdx = (topIdx + 1) & 0x3;
				if (dTop == coords[nextIdx][1]) {
					final double x = coords[nextIdx][0];
					xLeft = (xLeft <= x) ? xLeft : x;
					xRight = (xRight >= x) ? xRight : x;
				}
				nextIdx = (topIdx - 1) & 0x3;
				if (dTop == coords[nextIdx][1]) {
					final double x = coords[nextIdx][0];
					xLeft = (xLeft <= x) ? xLeft : x;
					xRight = (xRight >= x) ? xRight : x;
				}
				int t = sat32(xLeft);
				leftEdges[top] = (t >= xLeft) ? t : ++t;
				if (xLeft >= Integer.MAX_VALUE)
					leftEdges[top] = Integer.MAX_VALUE;
				rightEdges[top] = sat32(xRight);
			} else {
				top++;
			}
		} else {
			top = 0;
		}

		for (int i = 0; i < 2; i++) {
			final double dY1 = coords[(topIdx - i) & 0x3][1];
			final double dX1 = coords[(topIdx - i) & 0x3][0];
			final double dY2 = coords[(topIdx - i - 1) & 0x3][1];
			final double dX2 = coords[(topIdx - i - 1) & 0x3][0];
			double x = dX1;
			if (dY1 == dY2)
				continue;
			final double slope = (dX2 - dX1) / (dY2 - dY1);
			if (Double.isNaN(slope) || Double.isInfinite(slope))
				continue;
			final int y1 = dY1 < 0.0 ? 0 : sat32(dY1 + 1);
			int y2 = sat32(dY2);
			if (y2 >= dstH)
				y2 = dstH - 1;
			x += slope * (y1 - dY1);
			for (int j = y1; j <= y2; j++) {
				int t = sat32(x);
				leftEdges[j] = (t >= x) ? t : ++t;
				if (x >= Integer.MAX_VALUE)
					leftEdges[j] = Integer.MAX_VALUE;
				x += slope;
			}
		}

		for (int i = 0; i < 2; i++) {
			final double dY1 = coords[(topIdx + i) & 0x3][1];
			final double dX1 = coords[(topIdx + i) & 0x3][0];
			final double dY2 = coords[(topIdx + i + 1) & 0x3][1];
			final double dX2 = coords[(topIdx + i + 1) & 0x3][0];
			double x = dX1;
			if (dY1 == dY2)
				continue;
			final double slope = (dX2 - dX1) / (dY2 - dY1);
			if (Double.isNaN(slope) || Double.isInfinite(slope))
				continue;
			final int y1 = dY1 < 0.0 ? 0 : sat32(dY1 + 1);
			int y2 = sat32(dY2);
			if (y2 >= dstH)
				y2 = dstH - 1;
			x += slope * (y1 - dY1);
			for (int j = y1; j <= y2; j++) {
				rightEdges[j] = sat32(x);
				x += slope;
			}
			bot = y2;
		}

		final double dxCl = xClip * div0;
		final double dyCl = yClip * div0;
		final double dwCl = wClip * div0;
		final double dhCl = hClip * div0;

		final int xCl = (int) (xClip + delta);
		final int yCl = (int) (yClip + delta);
		final int wCl = (int) (wClip + delta);
		final int hCl = (int) (hClip + delta);

		final double div = 1.0 / div0;

		int sdx = (int) (a2 * div * (1 << SHIFT));
		int sdy = (int) (c2 * div * (1 << SHIFT));

		for (int i = top; i <= bot; i++) {
			int xLeft = leftEdges[i];
			int xRight = rightEdges[i];

			xLeft = (xLeft < 0) ? 0 : xLeft;
			xRight = (xRight >= dstW) ? dstW - 1 : xRight;

			final double xl = xLeft + 0.5;
			final double ii = i + 0.5;
			final double xr = xRight + 0.5;
			double dxs = xl * a2 + ii * b2 + tx2;
			double dys = xl * c2 + ii * d2 + ty2;

			if ((dxs < dxCl) || (dxs >= dwCl) || (dys < dyCl) || (dys >= dhCl)) {
				dxs += dx;
				dys += dy;
				if (xLeft < Integer.MAX_VALUE)
					xLeft++;
				if ((dxs < dxCl) || (dxs >= dwCl) || (dys < dyCl) || (dys >= dhCl))
					xRight = -1;
			}

			double dxe = xr * a2 + ii * b2 + tx2;
			double dye = xr * c2 + ii * d2 + ty2;

			if ((dxe < dxCl) || (dxe >= dwCl) || (dye < dyCl) || (dye >= dhCl)) {
				dxe -= dx;
				dye -= dy;
				if (xRight > Integer.MIN_VALUE)
					xRight--;
				if ((dxe < dxCl) || (dxe >= dwCl) || (dye < dyCl) || (dye >= dhCl))
					xRight = -1;
			}

			int xs = (int) ((dxs * div + delta) * (1 << SHIFT));
			final int xS = xs >> SHIFT;

			int ys = (int) ((dys * div + delta) * (1 << SHIFT));
			final int yS = ys >> SHIFT;

			if (xS < xCl)
				xs = (xCl << SHIFT);
			else if (xS >= wCl)
				xs = ((wCl << SHIFT) - 1);

			if (yS < yCl)
				ys = (yCl << SHIFT);
			else if (yS >= hCl)
				ys = ((hCl << SHIFT) - 1);

			if (xRight >= xLeft) {
				final int xE = ((xRight - xLeft) * sdx + xs) >> SHIFT;
				final int yE = ((xRight - xLeft) * sdy + ys) >> SHIFT;

				if ((xE < xCl) || (xE >= wCl)) {
					if (sdx > 0)
						sdx -= 1;
					else
						sdx += 1;
				}

				if ((yE < yCl) || (yE >= hCl)) {
					if (sdy > 0)
						sdy -= 1;
					else
						sdy += 1;
				}
			}

			leftEdges[i] = xLeft;
			rightEdges[i] = xRight;
			xStarts[i] = xs;
			yStarts[i] = ys;
		}

		while (top <= bot && leftEdges[top] > rightEdges[top])
			top++;

		if (top < bot)
			while (leftEdges[bot] > rightEdges[bot])
				bot--;

		result.yStart = top;
		result.yFinish = bot;
		result.dX = sdx;
		result.dY = sdy;
		return result;
	}

	/**
	 * <code>AffineTransformOp(scale, TYPE_NEAREST_NEIGHBOR)</code> on an ARGB
	 * image. The destination must be filled with 0 (transparent).
	 */
	static void nearest(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH, double scale) {
		final Edges e = edges(srcW, srcH, dstW, dstH, scale, 1, false);
		for (int j = e.yStart; j <= e.yFinish; j++) {
			final int xLeft = e.left[j];
			final int xRight = e.right[j];
			int x = e.xStarts[j];
			int y = e.yStarts[j];
			if (xLeft > xRight)
				continue;
			for (int p = xLeft; p <= xRight; p++) {
				final int xSrc = clamp(x >> SHIFT, srcW);
				final int ySrc = clamp(y >> SHIFT, srcH);
				dst[j * dstW + p] = src[ySrc * srcW + xSrc];
				x += e.dX;
				y += e.dY;
			}
		}
	}

	/**
	 * <code>AffineTransformOp(scale, TYPE_BILINEAR)</code> on an ARGB image, with
	 * the pixels along the border computed by extending the image. The
	 * destination must be filled with 0 (transparent).
	 */
	static void bilinear(int[] src, int srcW, int srcH, int[] dst, int dstW, int dstH, double scale) {
		final Edges main = edges(srcW, srcH, dstW, dstH, scale, 2, false);
		for (int j = main.yStart; j <= main.yFinish; j++) {
			final int xLeft = main.left[j];
			final int xRight = main.right[j];
			int x = main.xStarts[j];
			int y = main.yStarts[j];
			if (xLeft > xRight)
				continue;
			for (int p = xLeft; p <= xRight; p++) {
				final int fdx = x & MASK;
				final int fdy = y & MASK;
				final int xSrc = clamp(x >> SHIFT, srcW);
				final int ySrc = clamp(y >> SHIFT, srcH);
				final int xNext = Math.min(xSrc + 1, srcW - 1);
				final int yNext = Math.min(ySrc + 1, srcH - 1);
				final int c00 = src[ySrc * srcW + xSrc];
				final int c01 = src[ySrc * srcW + xNext];
				final int c10 = src[yNext * srcW + xSrc];
				final int c11 = src[yNext * srcW + xNext];
				dst[j * dstW + p] = (interpolate(c00, c01, c10, c11, fdx, fdy, 24) << 24)
						| (interpolate(c00, c01, c10, c11, fdx, fdy, 16) << 16)
						| (interpolate(c00, c01, c10, c11, fdx, fdy, 8) << 8)
						| interpolate(c00, c01, c10, c11, fdx, fdy, 0);
				x += main.dX;
				y += main.dY;
			}
		}

		// The pixels that cannot be interpolated because they are too close to the
		// border: bilinear interpolation in double, with the image extended
		final Edges edge = edges(srcW, srcH, dstW, dstH, scale, 2, true);
		for (int i = edge.yStart; i <= edge.yFinish; i++) {
			// Row i of the edge pass; the interior pixels are those of the main pass
			final boolean inMain = i >= main.yStart && i <= main.yFinish;
			final int xLeftE = edge.left[i];
			final int xRightE = edge.right[i] + 1;
			if (inMain) {
				int xLeft = main.left[i];
				int xRight = main.right[i] + 1;
				if (xLeft < xRight) {
					extend(src, srcW, srcH, dst, dstW, i, xLeftE, xLeft, edge.xStarts[i], edge.yStarts[i], edge.dX,
							edge.dY);
				} else {
					xRight = xLeftE;
				}
				extend(src, srcW, srcH, dst, dstW, i, xRight, xRightE, edge.xStarts[i] + edge.dX * (xRight - xLeftE),
						edge.yStarts[i] + edge.dY * (xRight - xLeftE), edge.dX, edge.dY);
			} else {
				extend(src, srcW, srcH, dst, dstW, i, xLeftE, xRightE, edge.xStarts[i], edge.yStarts[i], edge.dX,
						edge.dY);
			}
		}
	}

	private static int clamp(int value, int size) {
		return value < 0 ? 0 : value >= size ? size - 1 : value;
	}

	/**
	 * One channel of the interpolation of the main kernel (integer arithmetic,
	 * rounded).
	 */
	private static int interpolate(int c00, int c01, int c10, int c11, int fdx, int fdy, int shift) {
		final int a00 = (c00 >>> shift) & 0xFF;
		final int a01 = (c01 >>> shift) & 0xFF;
		final int a10 = (c10 >>> shift) & 0xFF;
		final int a11 = (c11 >>> shift) & 0xFF;
		final int round = 1 << (SHIFT - 1);
		final int pix0 = a00 + ((fdy * (a10 - a00) + round) >> SHIFT);
		final int pix1 = a01 + ((fdy * (a11 - a01) + round) >> SHIFT);
		return pix0 + ((fdx * (pix1 - pix0) + round) >> SHIFT);
	}

	/** Port of MLIB_EDGE_BL: the pixels [left, right) of the row, in double. */
	private static void extend(int[] src, int srcW, int srcH, int[] dst, int dstW, int row, int left, int right,
			int xStart, int yStart, int dX, int dY) {
		final double scale = 1.0 / (double) PREC;
		int x = xStart;
		int y = yStart;
		for (int p = left; p < right; p++) {
			int ySrc = ((y - 32768) >> SHIFT);
			int xSrc = ((x - 32768) >> SHIFT);

			final double t = ((x - 32768) & MASK) * scale;
			final double u = ((y - 32768) & MASK) * scale;

			int xDelta = ((xSrc + 1 - srcW) >> 31) & 1;
			int yDelta = ((ySrc + 1 - srcH) >> 31) & 1;

			final int xFlag = (xSrc >> 15);
			xSrc = xSrc + (1 & xFlag);
			xDelta = xDelta & ~xFlag;

			final int yFlag = (ySrc >> 15);
			ySrc = ySrc + (1 & yFlag);
			yDelta = yDelta & ~yFlag;

			// Never out of the image for the pixels the edges select; just in case
			ySrc = clamp(ySrc, srcH);
			xSrc = clamp(xSrc, srcW);
			final int c00 = src[ySrc * srcW + xSrc];
			final int c01 = src[ySrc * srcW + clamp(xSrc + xDelta, srcW)];
			final int c10 = src[clamp(ySrc + yDelta, srcH) * srcW + xSrc];
			final int c11 = src[clamp(ySrc + yDelta, srcH) * srcW + clamp(xSrc + xDelta, srcW)];

			dst[row * dstW + p] = (extendChannel(c00, c01, c10, c11, t, u, 24) << 24)
					| (extendChannel(c00, c01, c10, c11, t, u, 16) << 16)
					| (extendChannel(c00, c01, c10, c11, t, u, 8) << 8) | extendChannel(c00, c01, c10, c11, t, u, 0);

			x += dX;
			y += dY;
		}
	}

	private static int extendChannel(int c00, int c01, int c10, int c11, double t, double u, int shift) {
		final double a00 = (c00 >>> shift) & 0xFF;
		final double a01 = (c01 >>> shift) & 0xFF;
		final double a10 = (c10 >>> shift) & 0xFF;
		final double a11 = (c11 >>> shift) & 0xFF;
		final double pix0 = (a00 * (1 - t) + a01 * t) * (1 - u) + (a10 * (1 - t) + a11 * t) * u;
		return ((int) pix0) & 0xFF;
	}

}
