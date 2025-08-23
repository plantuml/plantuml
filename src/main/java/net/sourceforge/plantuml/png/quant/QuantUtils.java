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
	public static BufferedImage toBufferedARGB(RenderedImage src) {
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

}