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
import java.awt.image.RenderedImage;

/**
 * Utility functions for image handling in the quantization process.
 */
public final class QuantUtils {

	/**
	 * Converts any {@link RenderedImage} into a {@link BufferedImage} of type
	 * {@link BufferedImage#TYPE_INT_ARGB}.
	 * <p>
	 * - If the input is already a {@link BufferedImage} with a supported type
	 * (ARGB, RGB, 3BYTE_BGR, or 4BYTE_ABGR), it is returned as-is. <br>
	 * - Otherwise, an {@link IllegalArgumentException} is thrown.
	 * </p>
	 *
	 * <p>
	 * This method is intended to normalize different image types into a single
	 * consistent format (ARGB) before performing color quantization.
	 * </p>
	 *
	 * @param src the input image (must be a {@link BufferedImage})
	 * @return the same image if it already has a supported format
	 * @throws IllegalArgumentException if the image is not a {@link BufferedImage}
	 *                                  or if its type is unsupported
	 */
	public static BufferedImage toBufferedARGBorRGB(RenderedImage src) {
		if (src instanceof BufferedImage) {
			final BufferedImage bi = (BufferedImage) src;
			final int type = bi.getType();
			// Only accept common formats that can be processed safely
			if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB
					|| type == BufferedImage.TYPE_3BYTE_BGR || type == BufferedImage.TYPE_4BYTE_ABGR)
				return bi;
			
			// This already indexed
			if (type == BufferedImage.TYPE_BYTE_INDEXED)
				return null;

			throw new IllegalArgumentException("BufferedImage type=" + type);
		}
		throw new IllegalArgumentException();
	}
	

	/**
	 * Bitwise, branch-free LSB -> MSB "compression" for packed {@code 0xAARRGGBB} pixels.
	 *
	 * <p>Replaces the least-significant bit (LSB) of each byte (A, R, G, B) with that byte’s
	 * most-significant bit (MSB). This slightly reduces per-channel entropy with negligible
	 * visual impact while keeping pure black and pure white unchanged.</p>
	 *
	 * <p>Formally, for each 8-bit component {@code x} in {A,R,G,B}:
	 * {@code x' = (x & 0xFE) | ((x >>> 7) & 0x01)}.</p>
	 *
	 * <p>This packed implementation is equivalent to applying the above transform independently
	 * to A, R, G, and B and then reassembling the pixel:</p>
	 *
	 * <pre>{@code
	 * // Reference (per-channel) form:
	 * int a = (argb >>> 24) & 0xFF;
	 * int r = (argb >>> 16) & 0xFF;
	 * int g = (argb >>>  8) & 0xFF;
	 * int b =  argb         & 0xFF;
	 *
	 * a = (a & 0xFE) | ((a >>> 7) & 1);
	 * r = (r & 0xFE) | ((r >>> 7) & 1);
	 * g = (g & 0xFE) | ((g >>> 7) & 1);
	 * b = (b & 0xFE) | ((b >>> 7) & 1);
	 *
	 * return (a << 24) | (r << 16) | (g << 8) | b;
	 * }</pre>
	 *
	 * <h4>Perceptual & compression rationale</h4>
	 * <ul>
	 *   <li><b>Visually negligible:</b> Flipping at most one LSB per channel changes the value by <=1/255
	 *       (~0.4%) per component. Under normal viewing conditions and typical display gamma, such changes
	 *       are below the just-noticeable difference for flat regions, lines, and text—effectively invisible
	 *       to the naked eye. Pure black ({@code 0x00}) and pure white ({@code 0xFF}) remain unchanged.</li>
	 *   <li><b>More compressible:</b> Enforcing a relation between each byte’s MSB and its LSB removes high-frequency
	 *       “LSB noise,” which increases redundancy. PNG’s scanline filters (Sub/Up/Average/Paeth) then produce
	 *       residuals with more zeros and repeated patterns, yielding better DEFLATE (LZ77+Huffman) matches.
	 *       In practice this often reduces output size without any visible degradation. The same intuition applies
	 *       to other lossless codecs that benefit from lower entropy and longer runs.</li>
	 * </ul>
	 *
	 * <h4>Properties</h4>
	 * <ul>
	 *   <li><b>Idempotent</b>: {@code f(f(x)) == f(x)}.</li>
	 *   <li><b>Only LSBs may change</b>: {@code x ^ f(x)} can have bits set only at positions 0, 8, 16, 24.</li>
	 *   <li><b>New LSB equals old MSB</b> for each byte.</li>
	 *   <li><b>Invariants</b>: {@code 0x00000000} (black) and {@code 0xFFFFFFFF} (white) remain unchanged.</li>
	 *   <li><b>Branchless and fast</b>: two ANDs, one logical right shift, one OR; very JIT- and SIMD-friendly.</li>
	 * </ul>
	 *
	 * <h4>Usage notes</h4>
	 * <ul>
	 *   <li>Input must be a packed 32-bit ARGB pixel ({@code 0xAARRGGBB}).</li>
	 *   <li>Prefer non-premultiplied images (e.g., {@code TYPE_INT_ARGB}). On premultiplied data
	 *       ({@code TYPE_INT_ARGB_PRE}), altering RGB independently by 1 LSB slightly breaks the A·RGB invariant.</li>
	 *   <li>This transform is intentionally <b>not reversible</b>; it loses at most one bit per channel.</li>
	 *   <li>On synthetic gradients specifically crafted to reveal banding, any LSB change could, in theory,
	 *       be measurable; for diagrams/UI/flat fills and typical photography, it is effectively invisible.</li>
	 * </ul>
	 *
	 * <h4>Examples</h4>
	 * <pre>{@code
	 * // Byte 0x7F (0111_1111) -> 0x7E (0111_1110)
	 * // Byte 0x80 (1000_0000) -> 0x81 (1000_0001)
	 * int p = 0x7F80_55AA;              // A=0x7F, R=0x80, G=0x55, B=0xAA
	 * int q = compressPackedARGB(p);    // q == 0x7E81_54AB
	 * }</pre>
	 *
	 * @param argb packed 32-bit pixel in ARGB order ({@code 0xAARRGGBB}).
	 * @return the pixel with each channel’s LSB replaced by its MSB.
	 * @implNote Packed equivalent of the per-channel form:
	 *           {@code (argb & 0xFEFEFEFE) | ((argb >>> 7) & 0x01010101)}.
	 * @see #smartCompress(int)
	 */
	public static int compressPackedARGB(int argb) {
	    return (argb & 0xFEFEFEFE) | ((argb >>> 7) & 0x01010101);
	}
}