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

import net.sourceforge.plantuml.png.PngIO;
import net.sourceforge.plantuml.utils.Log;

public final class QuantifyPacked28 {

	public static BufferedImage packMeIfPossible(BufferedImage src) {

		final int type = src.getType();

		assert type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB
				|| type == BufferedImage.TYPE_3BYTE_BGR || type == BufferedImage.TYPE_4BYTE_ABGR;

		final int w = src.getWidth();
		final int h = src.getHeight();
		final int pixelCount = w * h;

		Log.info(() -> "QuantifyPacked28: starting, image " + w + "x" + h + " (" + pixelCount + " pixels), type="
				+ type + ", memory: " + PngIO.getUsedMemoryMB() + " MB");

		try {
			// Step 1: Create destination image
			final long startCreate = System.currentTimeMillis();
			final long memBeforeCreate = PngIO.getUsedMemoryMB();
			final BufferedImage dst = new BufferedImage(w, h, type);
			final long createDuration = System.currentTimeMillis() - startCreate;
			final long memAfterCreate = PngIO.getUsedMemoryMB();
			Log.info(() -> "QuantifyPacked28: destination image created in " + createDuration + " ms, memory: "
					+ memBeforeCreate + " -> " + memAfterCreate + " MB (delta: " + (memAfterCreate - memBeforeCreate)
					+ " MB)");

			// Step 2: Extract pixels
			final long startExtract = System.currentTimeMillis();
			final long memBeforeExtract = PngIO.getUsedMemoryMB();
			final int[] pixels = src.getRGB(0, 0, w, h, null, 0, w);
			final long extractDuration = System.currentTimeMillis() - startExtract;
			final long memAfterExtract = PngIO.getUsedMemoryMB();
			Log.info(() -> "QuantifyPacked28: pixel extraction in " + extractDuration + " ms, memory: "
					+ memBeforeExtract + " -> " + memAfterExtract + " MB (delta: "
					+ (memAfterExtract - memBeforeExtract) + " MB)");

			// Step 3: Compress pixels
			final long startCompress = System.currentTimeMillis();
			final long memBeforeCompress = PngIO.getUsedMemoryMB();
			final int[] out = new int[pixels.length];
			for (int i = 0; i < pixels.length; i++) {
				final int argb = pixels[i];
				out[i] = QuantUtils.compressPackedARGB(argb);
			}
			final long compressDuration = System.currentTimeMillis() - startCompress;
			final long memAfterCompress = PngIO.getUsedMemoryMB();
			Log.info(() -> "QuantifyPacked28: pixel compression in " + compressDuration + " ms, memory: "
					+ memBeforeCompress + " -> " + memAfterCompress + " MB (delta: "
					+ (memAfterCompress - memBeforeCompress) + " MB)");

			// Step 4: Write to destination
			final long startWrite = System.currentTimeMillis();
			final long memBeforeWrite = PngIO.getUsedMemoryMB();
			dst.setRGB(0, 0, w, h, out, 0, w);
			final long writeDuration = System.currentTimeMillis() - startWrite;
			final long memAfterWrite = PngIO.getUsedMemoryMB();
			Log.info(() -> "QuantifyPacked28: pixel write in " + writeDuration + " ms, memory: " + memBeforeWrite
					+ " -> " + memAfterWrite + " MB (delta: " + (memAfterWrite - memBeforeWrite) + " MB)");

			return dst;
		} catch (Throwable t) {
			// Swallowing all throwables is intentional here: any unexpected failure
			// during pixel extraction or packing (including OutOfMemoryError or JVM-level
			// errors) prevents safe recovery. Returning null signals that the packed
			// representation could not be produced.
			Log.info(() -> "QuantifyPacked28: exception caught: " + t.getClass().getSimpleName() + " - "
					+ t.getMessage());
			return null;
		}
	}

}
