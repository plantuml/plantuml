/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class UImage implements UShape {

	private final BufferedImage image;

	// public final double getScale() {
	// return scale;
	// }

	public UImage(BufferedImage image) {
		this.image = image;
	}

	// public UImage(BufferedImage before, double scale) {
	// this.image = before;
	// this.scale = scale;
	// // if (scale == 1) {
	// // this.image = before;
	// // return;
	// // }
	//
	// // final int w = (int) Math.round(before.getWidth() * scale);
	// // final int h = (int) Math.round(before.getHeight() * scale);
	// // final BufferedImage after = new BufferedImage(w, h, before.getType());
	// // final AffineTransform at = new AffineTransform();
	// // at.scale(scale, scale);
	// // final AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	// // this.image = scaleOp.filter(before, after);
	// }

	public UImage scale(double scale) {
		if (scale == 1) {
			return this;
		}
		final int w = (int) Math.round(image.getWidth() * scale);
		final int h = (int) Math.round(image.getHeight() * scale);
		final BufferedImage after = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return new UImage(scaleOp.filter(image, after));
	}

	public final BufferedImage getImage() {
		return image;
	}

	public double getWidth() {
		return image.getWidth() - 1;
	}

	public double getHeight() {
		return image.getHeight() - 1;
	}

	// public UShape getScaled(double scale) {
	// return scale(scale);
	// }

}
