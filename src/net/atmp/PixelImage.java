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
 * 
 *
 */
package net.atmp;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.color.ColorUtils;

public class PixelImage implements MutableImage {

	private final BufferedImage bufferedImageScale1;
	private final double scale;
	private final AffineTransformType type;
	private BufferedImage cache = null;

	public PixelImage(BufferedImage bufferedImage, AffineTransformType type) {
		this(bufferedImage, type, 1);
	}

	private PixelImage(BufferedImage bufferedImage, AffineTransformType type, double scale) {
		this.bufferedImageScale1 = bufferedImage;
		this.scale = scale;
		this.type = Objects.requireNonNull(type);
	}

	@Override
	public MutableImage withScale(double scale) {
		return new PixelImage(bufferedImageScale1, type, this.scale * scale);
	}

	@Override
	public final BufferedImage getImage() {
		if (scale == 1)
			return bufferedImageScale1;

		if (cache == null) {
			final int w = (int) Math.round(bufferedImageScale1.getWidth() * scale);
			final int h = (int) Math.round(bufferedImageScale1.getHeight() * scale);
			final BufferedImage after = new BufferedImage(w, h, bufferedImageScale1.getType());
			final AffineTransform at = new AffineTransform();
			at.scale(scale, scale);
			final AffineTransformOp scaleOp = new AffineTransformOp(at, type.toLegacyInt());
			this.cache = scaleOp.filter(bufferedImageScale1, after);
		}
		return cache;
	}

	@Override
	public MutableImage monochrome() {
		final BufferedImage copy = deepCopy();
		for (int i = 0; i < bufferedImageScale1.getWidth(); i++)
			for (int j = 0; j < bufferedImageScale1.getHeight(); j++) {
				final int color = bufferedImageScale1.getRGB(i, j);
				final int rgb = getRgb(color);
				final int grayScale = ColorUtils.getGrayScaleFromRGB(rgb);
				final int gray = grayScale + grayScale << 8 + grayScale << 16;
				final int a = getA(color);
				copy.setRGB(i, j, gray + a);
			}

		return new PixelImage(copy, type, scale);
	}

	@Override
	public MutableImage muteColor(Color newColor) {
		if (newColor == null)
			return this;

		int darkerRgb = getDarkerRgb();
		final BufferedImage copy = deepCopy();
		for (int i = 0; i < bufferedImageScale1.getWidth(); i++)
			for (int j = 0; j < bufferedImageScale1.getHeight(); j++) {
				final int color = bufferedImageScale1.getRGB(i, j);
				final int rgb = getRgb(color);
				final int a = getA(color);
				if (a != 0 && rgb == darkerRgb)
					copy.setRGB(i, j, newColor.getRGB() + a);
			}

		return new PixelImage(copy, type, scale);
	}

	@Override
	public MutableImage muteTransparentColor(Color newColor) {
		if (newColor == null)
			newColor = Color.WHITE;

		final BufferedImage copy = deepCopy();
		for (int i = 0; i < bufferedImageScale1.getWidth(); i++)
			for (int j = 0; j < bufferedImageScale1.getHeight(); j++) {
				final int color = bufferedImageScale1.getRGB(i, j);
				final int a = getA(color);
				if (a == 0)
					copy.setRGB(i, j, newColor.getRGB());

			}

		return new PixelImage(copy, type, scale);
	}

	private int getDarkerRgb() {
		int darkerRgb = -1;
		for (int i = 0; i < bufferedImageScale1.getWidth(); i++)
			for (int j = 0; j < bufferedImageScale1.getHeight(); j++) {
				final int color = bufferedImageScale1.getRGB(i, j);
				final int rgb = getRgb(color);
				final int a = getA(color);
				if (a != mask_a__)
					continue;

				// if (isTransparent(color)) {
				// continue;
				// }
				final int gray = ColorUtils.getGrayScaleFromRGB(rgb);
				if (darkerRgb == -1 || gray < ColorUtils.getGrayScaleFromRGB(darkerRgb))
					darkerRgb = rgb;

			}
		return darkerRgb;
	}

	private static final int mask_a__ = 0xFF000000;
	private static final int mask_rgb = 0x00FFFFFF;

	private int getRgb(int color) {
		return color & mask_rgb;
	}

	private int getA(int color) {
		return color & mask_a__;
	}

	// From
	// https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
//	private static BufferedImage deepCopyOld(BufferedImage bi) {
//		final ColorModel cm = bi.getColorModel();
//		final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//		final WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
//		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
//	}

	private BufferedImage deepCopy() {
		final BufferedImage result = new BufferedImage(bufferedImageScale1.getWidth(), bufferedImageScale1.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < this.bufferedImageScale1.getWidth(); i++)
			for (int j = 0; j < this.bufferedImageScale1.getHeight(); j++)
				result.setRGB(i, j, bufferedImageScale1.getRGB(i, j));

		return result;
	}

	@Override
	public final double getScale() {
		return scale;
	}

}
