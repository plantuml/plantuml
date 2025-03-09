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

public class SpriteColor implements Sprite {

	private final int width;
	private final int height;
	private final int gray[][];
	private final int color[][];

	public SpriteColor(int width, int height) {
		this.width = width;
		this.height = height;
		this.gray = new int[height][width];
		this.color = new int[height][width];
	}

	public void setGray(int x, int y, int level) {
		if (x < 0 || x >= width)
			return;

		if (y < 0 || y >= height)
			return;

		if (level < 0 || level >= 16)
			throw new IllegalArgumentException();

		gray[y][x] = level;
		color[y][x] = -1;
	}

	public void setColor(int x, int y, int col) {
		if (x < 0 || x >= width)
			return;

		if (y < 0 || y >= height)
			return;

		gray[y][x] = -1;
		color[y][x] = col;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public UImage toUImage(ColorMapper colorMapper, HColor backcolor, HColor forecolor) {
		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		if (backcolor == null)
			backcolor = HColors.WHITE;

		if (forecolor == null)
			forecolor = HColors.BLACK;

		final HColorGradient gradient = HColors.gradient(backcolor, forecolor, '\0');
		for (int col = 0; col < width; col++) {
			for (int line = 0; line < height; line++) {
				final int localColor = color[line][col];
				if (localColor == -1) {
					final double coef = 1.0 * gray[line][col] / (16 - 1);
					final Color c = gradient.getColor(colorMapper, coef, 255);
					im.setRGB(col, line, c.getRGB());
				} else {
					im.setRGB(col, line, localColor);
				}
			}
		}
		return new UImage(new PixelImage(im, AffineTransformType.TYPE_BILINEAR));
	}

	public TextBlock asTextBlock(final HColor color, final double scale) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final UImage image = toUImage(ug.getColorMapper(), ug.getParam().getBackcolor(), color);
				ug.draw(image.scale(scale));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(getWidth() * scale, getHeight() * scale);
			}
		};
	}

}
