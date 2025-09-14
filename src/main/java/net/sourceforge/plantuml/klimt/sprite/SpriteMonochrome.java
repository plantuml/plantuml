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
package net.sourceforge.plantuml.klimt.sprite;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorGradient;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImage;

public class SpriteMonochrome implements Sprite {

	private final int width;
	private final int height;
	private final int grayLevel;
	private final int gray[][];

	public boolean isSameKind(SpriteMonochrome other) {
		if (this.width != other.width)
			return false;

		if (this.height != other.height)
			return false;

		if (this.grayLevel != other.grayLevel)
			return false;

		return true;
	}

	public boolean isSame(SpriteMonochrome other) {
		if (isSameKind(other) == false)
			return false;

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (this.gray[j][i] != other.gray[j][i])
					return false;

		return true;
	}

	public SpriteMonochrome xor(SpriteMonochrome other) {
		if (this.width != other.width)
			throw new IllegalStateException();

		if (this.height != other.height)
			throw new IllegalStateException();

		if (this.grayLevel != other.grayLevel)
			throw new IllegalStateException();

		final SpriteMonochrome result = new SpriteMonochrome(width, height, grayLevel);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				result.gray[j][i] = this.gray[j][i] ^ other.gray[j][i];

		return result;
	}

	public SpriteMonochrome(int width, int height, int grayLevel) {
		if (grayLevel != 2 && grayLevel != 4 && grayLevel != 8 && grayLevel != 16)
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
		this.grayLevel = grayLevel;
		this.gray = new int[height][width];
	}

	public SpriteMonochrome xSymetric() {
		final SpriteMonochrome result = new SpriteMonochrome(width, height, grayLevel);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				result.setGray(i, j, this.getGray(i, j));

		for (int j = 0; j < height; j++)
			for (int i = 0; i < width / 2; i++) {
				final int i2 = width - 1 - i;
				final int level = result.getGray(i, j) ^ result.getGray(i2, j);
				result.setGray(i2, j, level);
			}

		return result;
	}

	public SpriteMonochrome ySymetric() {
		final SpriteMonochrome result = new SpriteMonochrome(width, height, grayLevel);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				result.setGray(i, j, this.getGray(i, j));

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height / 2; j++) {
				final int j2 = height - 1 - j;
				final int level = result.getGray(i, j) ^ result.getGray(i, j2);
				result.setGray(i, j2, level);
			}

		return result;
	}

	public void setGray(int x, int y, int level) {
		if (x < 0 || x >= width)
			return;

		if (y < 0 || y >= height)
			return;

		if (level < 0 || level >= grayLevel)
			throw new IllegalArgumentException("level=" + level + " grayLevel=" + grayLevel);

		gray[y][x] = level;
	}

	public int getGray(int x, int y) {
		if (x >= width)
			throw new IllegalArgumentException("x=" + x + " width=" + width);

		if (y >= height)
			throw new IllegalArgumentException("y=" + y + " height=" + height);

		return gray[y][x];
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public UImage toUImage(ColorMapper colorMapper, HColor backcolor, HColor color) {

		if (backcolor == null || backcolor.isTransparent())
			backcolor = HColors.WHITE.withDark(HColors.BLACK);

		if (color == null || color.isTransparent())
			color = HColors.BLACK.withDark(HColors.WHITE);

		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final HColorGradient gradient = HColors.gradient(backcolor, color, '\0');
		double maxCoef = 0;
		for (int col = 0; col < width; col++)
			for (int line = 0; line < height; line++)
				maxCoef = Math.max(maxCoef, 1.0 * gray[line][col] / (grayLevel - 1));

		for (int col = 0; col < width; col++)
			for (int line = 0; line < height; line++) {
				final int grayValue = gray[line][col];
				final double coef = 1.0 * grayValue / (grayLevel - 1);
				final int alpha;
				if (coef > maxCoef / 4)
					alpha = 255;
				else
					alpha = (int) (255 * (coef * 4 / maxCoef));
				final Color c = gradient.getColor(colorMapper, coef, alpha);
				im.setRGB(col, line, c.getRGB());
			}

		return new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
	}

	@Override
	public TextBlock asTextBlock(final HColor fontColor, final HColor forcedColor, final double scale, final HColor backColor) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final UImage image = toUImage(ug.getColorMapper(), ug.getParam().getBackcolor(),
						forcedColor == null ? fontColor : forcedColor);
				ug.draw(image.scale(scale));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(getWidth() * scale, getHeight() * scale);
			}
		};
	}

	public void exportSprite1(OutputStream fos) throws IOException {
		for (int y = 0; y < this.getHeight(); y += 2)
			for (int x = 0; x < this.getWidth(); x += 1) {
				int b1 = this.getGray(x, y);
				int b2 = y + 1 < this.getHeight() ? this.getGray(x, y + 1) : b1;
				fos.write(b1 * 16 + b2);
			}

	}

}
