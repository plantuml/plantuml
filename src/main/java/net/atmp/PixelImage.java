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

import java.util.Objects;

import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.MutableImage;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.awt.PortableImageFactory;
import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorUtils;

public class PixelImage implements MutableImage {

	private final PortableImage imageScale1;
	private final double scale;
	private final AffineTransformType type;
	private PortableImage cache = null;

	public PixelImage(PortableImage image, AffineTransformType type) {
		this(image, type, 1);
	}

	private PixelImage(PortableImage image, AffineTransformType type, double scale) {
		this.imageScale1 = image;
		this.scale = scale;
		this.type = Objects.requireNonNull(type);
	}

	@Override
	public MutableImage withScale(double scale) {
		return new PixelImage(imageScale1, type, this.scale * scale);
	}

	@Override
	public final PortableImage getImage() {
		if (scale == 1)
			return imageScale1;

		if (cache == null)
			this.cache = imageScale1.scale(scale, type.toLegacyInt());

		return cache;
	}

	@Override
	public MutableImage monochrome() {
		final PortableImage copy = deepCopy();
		for (int i = 0; i < imageScale1.getWidth(); i++)
			for (int j = 0; j < imageScale1.getHeight(); j++) {
				final int color = imageScale1.getRGB(i, j);
				final int rgb = getRgb(color);
				final int grayScale = ColorUtils.getGrayScaleFromRGB(rgb);
				final int gray = grayScale + grayScale << 8 + grayScale << 16;
				final int a = getA(color);
				copy.setRGB(i, j, gray + a);
			}

		return new PixelImage(copy, type, scale);
	}

	@Override
	public MutableImage muteColor(XColor newColor) {
		if (newColor == null)
			return this;

		int darkerRgb = getDarkerRgb();
		final PortableImage copy = deepCopy();
		for (int i = 0; i < imageScale1.getWidth(); i++)
			for (int j = 0; j < imageScale1.getHeight(); j++) {
				final int color = imageScale1.getRGB(i, j);
				final int rgb = getRgb(color);
				final int a = getA(color);
				if (a != 0 && rgb == darkerRgb)
					copy.setRGB(i, j, newColor.getRGB() + a);
			}

		return new PixelImage(copy, type, scale);
	}

	@Override
	public MutableImage muteTransparentColor(XColor newColor) {
		if (newColor == null)
			newColor = XColor.WHITE;

		final PortableImage copy = deepCopy();
		for (int i = 0; i < imageScale1.getWidth(); i++)
			for (int j = 0; j < imageScale1.getHeight(); j++) {
				final int color = imageScale1.getRGB(i, j);
				final int a = getA(color);
				if (a == 0)
					copy.setRGB(i, j, newColor.getRGB());

			}

		return new PixelImage(copy, type, scale);
	}

	private int getDarkerRgb() {
		int darkerRgb = -1;
		for (int i = 0; i < imageScale1.getWidth(); i++)
			for (int j = 0; j < imageScale1.getHeight(); j++) {
				final int color = imageScale1.getRGB(i, j);
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

	private PortableImage deepCopy() {
		final PortableImage result = PortableImageFactory.build(imageScale1.getWidth(), imageScale1.getHeight(),
				PortableImage.TYPE_INT_ARGB);
		for (int i = 0; i < this.imageScale1.getWidth(); i++)
			for (int j = 0; j < this.imageScale1.getHeight(); j++)
				result.setRGB(i, j, imageScale1.getRGB(i, j));

		return result;
	}

	@Override
	public final double getScale() {
		return scale;
	}

}
