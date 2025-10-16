/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 * With assistance from ChatGPT (OpenAI)
 *
 */
package net.sourceforge.plantuml.png.quant;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import net.sourceforge.plantuml.utils.Log;

/**
 * A simple color quantizer based on RGB555 cubes.
 *
 * <p>
 * <b>Process:</b>
 * </p>
 * <ol>
 * <li>Each pixel is mapped to a 15-bit cube index (RGB555).</li>
 * <li>Inside the cube, a 9-bit sub-color index (0–511) is computed.</li>
 * <li>Frequencies of sub-colors are accumulated in each cube.</li>
 * <li>If more than 255 distinct cubes are populated, quantization aborts (since
 * the palette would exceed 256 colors).</li>
 * <li>For each cube, the most frequent sub-color is selected as the
 * representative palette entry.</li>
 * <li>An {@link IndexColorModel} is built and an 8-bit indexed image is
 * generated.</li>
 * </ol>
 * 
 * <p>
 * <b>Comparison to other algorithms:</b>
 * </p>
 * <ul>
 * <li><b>Median Cut:</b> Splits color space adaptively based on variance.
 * Produces better palettes on natural images (gradients, photos), but more
 * expensive computationally.</li>
 * <li><b>Wu's Algorithm:</b> Uses variance minimization and dynamic
 * programming. Produces very high-quality palettes (similar to pngquant), but
 * requires more memory and preprocessing.</li>
 * <li><b>RGB555 Cube Quantizer (this class):</b> Extremely fast, deterministic,
 * and simple. Well-suited for diagrams, icons, or graphics with limited colors.
 * However, palette quality is generally inferior to Wu or Median Cut,
 * especially for gradients and photos.</li>
 * </ul>
 *
 * <p>
 * <b>Advantages:</b>
 * </p>
 * <ul>
 * <li>Very fast: one pass to count, one pass to remap pixels.</li>
 * <li>Simple and predictable (no recursion, no complex math).</li>
 * <li>Guaranteed <= 256 colors (or aborts safely).</li>
 * </ul>
 * 
 * <p>
 * rules for transparency:
 * </p>
 * <ul>
 * <li>any pixel with alpha less than 127 is considered fully transparent and
 * mapped to ARGB 0x00000000.</li>
 * <li>a special cube id is reserved to represent this transparent color.</li>
 * </ul>
 *
 * <p>
 * <b>Limitations:</b>
 * </p>
 * <ul>
 * <li>Rigid partitioning (fixed RGB555 grid) does not adapt to the distribution
 * of colors in the image.</li>
 * <li>Poorer palette quality for continuous-tone images.</li>
 * <li>No dithering: banding artifacts are visible on gradients unless combined
 * with an error-diffusion algorithm (e.g. Floyd–Steinberg).</li>
 * </ul>
 */
public final class Quantify666 {

	// 32*32*32 RGB555 cubes + 1 extra slot for fully transparent color
	private static final int CUBE_COUNT_RGB666 = 64 * 64 * 64; // 0..262143
	private static final int TRANSPARENT_CUBE = CUBE_COUNT_RGB666; // 262144
	private static final int TOTAL_CUBE_SLOTS = CUBE_COUNT_RGB666 + 1;

	/**
	 * Attempts to quantize an image to <= 256 colors using the Cube555 structure.
	 * 
	 * @param src any {@link BufferedImage}
	 * @return a new {@link BufferedImage} with an indexed color model, or
	 *         {@code null} if quantization is not possible (too many colors)
	 */
	public static BufferedImage packMeIfPossible(BufferedImage src) {

		final int w = src.getWidth();
		final int h = src.getHeight();
		final int[] pixels = src.getRGB(0, 0, w, h, null, 0, w);

		Log.info(() -> "Using Quantify666.");

		int nbCubes = 0;

		// Step 1: Fill Cube555 structures with frequency counts
		final Cube666[] cubes = new Cube666[TOTAL_CUBE_SLOTS];
		for (int argb : pixels) {
			final int cubeIndex = getCubeIndex(argb);

			Cube666 cube = cubes[cubeIndex];
			if (cube == null) {
				// Abort if too many distinct cubes are found
				if (nbCubes++ > 255) {
					Log.info(() -> "...abort, too many colors");
					return null;
				}
				System.err.println("nbCubes=" + nbCubes);
				cube = new Cube666(cubeIndex);
				cubes[cubeIndex] = cube;
			}

			// for transparent pixels, we don't care about sub-color distribution;
			// just increment bucket 0 (or any fixed slot).
			final int sub = cubeIndex == TRANSPARENT_CUBE ? 0 : subColorIndex64(argb);
			cube.increment(sub);

		}

		// Step 2: Build the final indexed image
		return buildIndexedImageFromCubes(src, cubes);
	}

	public static boolean isTransparent(int argb) {
		return ((argb >>> 24) & 0xFF) < 127;
	}

	/**
	 * Builds an indexed image from the set of populated cubes.
	 *
	 * @param src   original ARGB image
	 * @param cubes array of Cube555 (null for empty cubes)
	 * @return an indexed (8-bit) {@link BufferedImage}
	 */
	private static BufferedImage buildIndexedImageFromCubes(BufferedImage src, Cube666[] cubes) {
		final int w = src.getWidth();
		final int h = src.getHeight();

		// Step 1: Build the palette from all non-empty cubes
		final int[] cubeToPal = new int[TOTAL_CUBE_SLOTS];
		Arrays.fill(cubeToPal, -1);

		final int[] palARGB = new int[256];
		int palSize = 0;

		for (Cube666 c : cubes) {
			if (c == null)
				continue;
			final int sub = c.best(); // most frequent sub-color (0..511)
			final int argb = representativeARGB(c.rgb666, sub);
			cubeToPal[c.rgb666] = palSize;
			palARGB[palSize++] = argb;
		}

		if (palSize == 0)
			throw new IllegalStateException();

		final IndexColorModel icm = buildICM(palARGB, palSize);

		// Step 2: Create output indexed image
		final BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, icm);
		final WritableRaster raster = dst.getRaster();

		// Step 3: Replace each pixel in the source with its palette index
		final int[] line = new int[w];
		for (int y = 0; y < h; y++) {
			src.getRGB(0, y, w, 1, line, 0, w);
			for (int x = 0; x < w; x++) {
				final int argb = line[x];
				final int cubeIndex = getCubeIndex(argb);
				final int p = cubeToPal[cubeIndex];
				if (p < 0)
					throw new IllegalStateException();

				raster.setSample(x, y, 0, p);
			}
		}
		return dst;
	}

	/**
	 * Builds an {@link IndexColorModel} from a palette of ARGB colors.
	 * 
	 * @param palARGB array of ARGB colors
	 * @param palSize number of colors actually used
	 * @return an {@link IndexColorModel} suitable for TYPE_BYTE_INDEXED
	 */
	public static IndexColorModel buildICM(final int[] palARGB, final int palSize) {
		final byte[] r = new byte[palSize];
		final byte[] g = new byte[palSize];
		final byte[] b = new byte[palSize];
		final byte[] a = new byte[palSize];

		for (int i = 0; i < palSize; i++) {
			final int argb = palARGB[i];
			a[i] = (byte) ((argb >>> 24) & 0xFF);
			r[i] = (byte) ((argb >>> 16) & 0xFF);
			g[i] = (byte) ((argb >>> 8) & 0xFF);
			b[i] = (byte) (argb & 0xFF);
		}

		return new IndexColorModel(8, palSize, r, g, b, a);
	}
	
	
	private static int representativeARGB(int cubeIndex, int sub64) {

		if (cubeIndex == TRANSPARENT_CUBE)
			return 0x00000000; // fully transparent black

		final int r6 = (cubeIndex >>> 12) & 0x3F;
		final int g6 = (cubeIndex >>> 6) & 0x3F;
		final int b6 = cubeIndex & 0x3F;

		final int rLow2 = (sub64 >>> 4) & 0x03;
		final int gLow2 = (sub64 >>> 2) & 0x03;
		final int bLow2 = sub64 & 0x03;

		final int r8 = (r6 << 2) | rLow2;
		final int g8 = (g6 << 2) | gLow2;
		final int b8 = (b6 << 2) | bLow2;

		return (0xFF << 24) | (r8 << 16) | (g8 << 8) | b8;
	}

	private static int getCubeIndex(int argb) {

		if (isTransparent(argb))
			return TRANSPARENT_CUBE;

		/*
		 * Compacts a 24-bit ARGB color into a 15-bit RGB555 index.
		 * 
		 * Formula: r5:g5:b5 -> (r5 << 10) | (g5 << 5) | b5 </p>
		 */

		final int r6 = ((argb >>> 16) & 0xFF) >>> 2; // keep 6 MSBs
		final int g6 = ((argb >>>  8) & 0xFF) >>> 2;
		final int b6 = ( argb         & 0xFF) >>> 2;
		return (r6 << 12) | (g6 << 6) | b6;
	}

	/**
	 * Computes the 9-bit sub-color index (0..511) of a pixel inside its RGB555
	 * cube.
	 * <p>
	 * Takes the 3 least significant bits of each 8-bit channel:
	 * 
	 * sub = (rLow3 << 6) | (gLow3 << 3) | bLow3
	 * </p>
	 *
	 * @param argb 32-bit ARGB color
	 * @return sub-color index (0..511)
	 */
	private static int subColorIndex64(int argb) {
		final int r8 = (argb >>> 16) & 0xFF;
		final int g8 = (argb >>> 8) & 0xFF;
		final int b8 = argb & 0xFF;

		final int rLow2 = r8 & 0x03; // 3 least significant bits
		final int gLow2 = g8 & 0x03;
		final int bLow2 = b8 & 0x03;

		return (rLow2 << 4) | (gLow2 << 2) | bLow2; // 0..64
	}

}