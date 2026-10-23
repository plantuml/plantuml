/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 *
 */
package net.sourceforge.plantuml.teavm.c;

/**
 * Encodes a raster image (ARGB pixels) as a PNG in C, with the same encoder as
 * the PNG output (src/teavm-c/native/plantuml_png.h). Only call it when
 * {@code TeaVM.isC()}.
 */
public final class PngImageC {
	// ::remove file when JAVA8

	private PngImageC() {
	}

	/**
	 * @param argb   the pixels, row by row, as 0xAARRGGBB (not premultiplied)
	 * @param opaque whether the alpha channel is ignored; the colors of an opaque
	 *               image are also reduced to a palette of 255 (lossy)
	 */
	public static byte[] encode(int[] argb, int width, int height, boolean opaque) {
		final int size = NativeCanvas.encodeImage(argb, width, height, opaque ? 1 : 0);
		if (size < 0)
			throw new IllegalStateException("PNG encoding failed");
		final byte[] result = new byte[size];
		NativeCanvas.copyImage(result);
		return result;
	}

}
