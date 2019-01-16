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

import net.sourceforge.plantuml.Log;

public class PngSizer {

	static public BufferedImage process(BufferedImage im, int minsize) {

		if (minsize != Integer.MAX_VALUE) {
			return resize(im, minsize);
		}
		return im;

	}

	static private BufferedImage resize(BufferedImage im, int minsize) {
		Log.info("Resizing file to " + minsize);

		if (im.getWidth() >= minsize) {
			return im;
		}

		final BufferedImage newIm = new BufferedImage(minsize, im.getHeight(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, newIm.getWidth(), newIm.getHeight());
		final int delta = (minsize - im.getWidth()) / 2;
		g2d.drawImage(im, delta, 0, null);

		g2d.dispose();

		return newIm;

	}

}
