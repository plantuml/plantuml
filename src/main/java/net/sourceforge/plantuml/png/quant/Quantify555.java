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
public final class Quantify555 {

	// 32*32*32 RGB555 cubes + 1 extra slot for fully transparent color
	private static final int CUBE_COUNT_RGB555 = 32 * 32 * 32; // 0..32767
	private static final int TRANSPARENT_CUBE = CUBE_COUNT_RGB555; // 32768
	private static final int TOTAL_CUBE_SLOTS = CUBE_COUNT_RGB555 + 1;

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

		Log.info(() -> "Using Quantify555.");

		int nbCubes = 0;

		// Step 1: Fill Cube555 structures with frequency counts
		final Cube555[] cubes = new Cube555[TOTAL_CUBE_SLOTS];
		for (int argb : pixels) {
			final int cubeIndex = getCubeIndex(argb);

			Cube555 cube = cubes[cubeIndex];
			if (cube == null) {
				// Abort if too many distinct cubes are found
				if (nbCubes++ > 255) {
					Log.info(() -> "...abort, too many colors");
					return null;
				}
				cube = new Cube555(cubeIndex);
				cubes[cubeIndex] = cube;
			}

			// for transparent pixels, we don't care about sub-color distribution;
			// just increment bucket 0 (or any fixed slot).
			final int sub = cubeIndex == TRANSPARENT_CUBE ? 0 : subColorIndex512(argb);
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
	private static BufferedImage buildIndexedImageFromCubes(BufferedImage src, Cube555[] cubes) {
		final int w = src.getWidth();
		final int h = src.getHeight();

		// Step 1: Build the palette from all non-empty cubes
		final int[] cubeToPal = new int[TOTAL_CUBE_SLOTS];
		Arrays.fill(cubeToPal, -1);

		final int[] palARGB = new int[256];
		int palSize = 0;

		for (Cube555 c : cubes) {
			if (c == null)
				continue;
			final int sub = c.best(); // most frequent sub-color (0..511)
			final int argb = representativeARGB(c.rgb555, sub);
			cubeToPal[c.rgb555] = palSize;
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

	/**
	 * Reconstructs an ARGB color from a cube index (RGB555) and a sub-color index
	 * (9 bits).
	 * <p>
	 * Conversion: r8 = (r5 << 3) | rLow3, etc. Alpha is set to 255 (opaque).
	 * </p>
	 *
	 * @param cubeIndex cube index (15-bit RGB555)
	 * @param sub512    sub-color index (0..511)
	 * @return reconstructed ARGB color
	 */
	private static int representativeARGB(int cubeIndex, int sub512) {

		if (cubeIndex == TRANSPARENT_CUBE)
			return 0x00000000; // fully transparent black

		final int r5 = (cubeIndex >>> 10) & 0x1F;
		final int g5 = (cubeIndex >>> 5) & 0x1F;
		final int b5 = cubeIndex & 0x1F;

		final int rLow3 = (sub512 >>> 6) & 0x07;
		final int gLow3 = (sub512 >>> 3) & 0x07;
		final int bLow3 = sub512 & 0x07;

		final int r8 = (r5 << 3) | rLow3;
		final int g8 = (g5 << 3) | gLow3;
		final int b8 = (b5 << 3) | bLow3;

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

		final int r5 = (argb >>> 19) & 0x1F; // bits 23..19
		final int g5 = (argb >>> 11) & 0x1F; // bits 15..11
		final int b5 = (argb >>> 3) & 0x1F; // bits 7..3
		return (r5 << 10) | (g5 << 5) | b5;
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
	private static int subColorIndex512(int argb) {
		final int r8 = (argb >>> 16) & 0xFF;
		final int g8 = (argb >>> 8) & 0xFF;
		final int b8 = argb & 0xFF;

		final int rLow3 = r8 & 0x07; // 3 least significant bits
		final int gLow3 = g8 & 0x07;
		final int bLow3 = b8 & 0x07;

		return (rLow3 << 6) | (gLow3 << 3) | bLow3; // 0..511
	}

}