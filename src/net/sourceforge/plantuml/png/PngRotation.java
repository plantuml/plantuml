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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Log;

public class PngRotation {

	public static BufferedImage process(BufferedImage im) {

		Log.info("Rotation");

		final BufferedImage newIm = new BufferedImage(im.getHeight(), im.getWidth(), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = newIm.createGraphics();

		final AffineTransform at = new AffineTransform(0, 1, 1, 0, 0, 0);
		at.concatenate(new AffineTransform(-1, 0, 0, 1, im.getWidth(), 0));
		g2d.setTransform(at);

		g2d.drawImage(im, 0, 0, null);
		g2d.dispose();

		return newIm;
	}

}
