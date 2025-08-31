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
package net.sourceforge.plantuml.png.quantx;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Map;

import net.sourceforge.plantuml.png.quant.QuantUtils;

public final class QuantifyOKLab {

	public static BufferedImage quantifyMeIfPossible(RenderedImage input) {

		final BufferedImage src = QuantUtils.toBufferedARGBorRGB(input);
		if (src == null)
			return null;

		final int w = src.getWidth();
		final int h = src.getHeight();
		int[] pixels = src.getRGB(0, 0, w, h, null, 0, w);

		Map<Integer, Integer> palette = new QuantizerWuOKLab().quantize(pixels, 256);

		return OKLabPaletteMapper.quantize(src, palette, true);

	}

}