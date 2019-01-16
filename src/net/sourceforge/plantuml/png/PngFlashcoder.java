/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 *
 */
package net.sourceforge.plantuml.png;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import net.sourceforge.plantuml.ugraphic.UAntiAliasing;

public class PngFlashcoder {

	private final List<BufferedImage> flashcodes;

	public PngFlashcoder(List<BufferedImage> flashcodes) {
		this.flashcodes = flashcodes;
	}

	public BufferedImage processImage(BufferedImage im, Color background) {
		if (flashcodes != null) {
			im = addImage(im, background);
		}
		return im;

	}

	private BufferedImage addImage(BufferedImage im, Color background) {

		final double width = Math.max(im.getWidth(), getWidth(flashcodes));
		final double height = im.getHeight() + getHeight(flashcodes);

		final BufferedImage newIm = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		UAntiAliasing.ANTI_ALIASING_OFF.apply(g2d);
		g2d.setColor(background);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		g2d.drawImage(im, null, 0, 0);
		int x = 0;
		for (BufferedImage f : flashcodes) {
			g2d.drawImage(f, null, x, (int) im.getHeight());
			x += f.getWidth();
		}
		g2d.dispose();
		return newIm;

	}

	public static int getHeight(List<BufferedImage> flashcodes) {
		int result = 0;
		for (BufferedImage im : flashcodes) {
			result = Math.max(result, im.getWidth());
		}
		return result;
	}

	public static int getWidth(List<BufferedImage> flashcodes) {
		int result = 0;
		for (BufferedImage im : flashcodes) {
			result += im.getWidth();
		}
		return result;
	}
}
