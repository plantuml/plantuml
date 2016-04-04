/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 19271 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class UImage implements UShape {

	private final BufferedImage image;

	public UImage(BufferedImage image) {
		this.image = image;
	}

	public UImage(BufferedImage before, double scale) {
		if (scale == 1) {
			this.image = before;
			return;
		}

		final int w = (int) Math.round(before.getWidth() * scale);
		final int h = (int) Math.round(before.getHeight() * scale);
		final BufferedImage after = new BufferedImage(w, h, before.getType());
		final AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		this.image = scaleOp.filter(before, after);
	}

	public UImage scale(double scale) {
		return new UImage(image, scale);
	}

	public final BufferedImage getImage() {
		return image;
	}

	public double getWidth() {
		return image.getWidth();
	}

	public double getHeight() {
		return image.getHeight();
	}

}
