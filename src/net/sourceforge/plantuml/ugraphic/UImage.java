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
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class UImage implements UShape {

	private final BufferedImage image;
	private final String formula;

	public UImage(BufferedImage image) {
		this(image, null);
	}

	public UImage(BufferedImage image, String formula) {
		this.image = image;
		this.formula = formula;
	}

	public UImage scale(double scale) {
		return scale(scale, AffineTransformOp.TYPE_BILINEAR);
	}

	public UImage scaleNearestNeighbor(double scale) {
		return scale(scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	}

	private UImage scale(double scale, final int type) {
		if (scale == 1) {
			return this;
		}
		final int w = (int) Math.round(image.getWidth() * scale);
		final int h = (int) Math.round(image.getHeight() * scale);
		final BufferedImage after = new BufferedImage(w, h, image.getType());
		final AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		final AffineTransformOp scaleOp = new AffineTransformOp(at, type);
		return new UImage(scaleOp.filter(image, after), formula);
	}

	public final BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return image.getWidth() - 1;
	}

	public int getHeight() {
		return image.getHeight() - 1;
	}

	public final String getFormula() {
		return formula;
	}

	public UImage muteColor(Color newColor) {
		if (newColor == null) {
			return this;
		}
		int darker = -1;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				final int color = image.getRGB(i, j);
				if (isTransparent(color)) {
					continue;
				}
				final int grey = ColorChangerMonochrome.getGrayScale(color);
				if (darker == -1 || grey < ColorChangerMonochrome.getGrayScale(darker)) {
					darker = color;
				}
			}
		}
		final BufferedImage copy = deepCopy(image);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (copy.getRGB(i, j) == darker) {
					copy.setRGB(i, j, newColor.getRGB());
				}
			}
		}
		return new UImage(copy, formula);
	}

	private boolean isTransparent(int color) {
		if (color == 0) {
			return true;
		}
		return false;
	}

	// From https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
	private static BufferedImage deepCopy(BufferedImage bi) {
		final ColorModel cm = bi.getColorModel();
		final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		final WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
