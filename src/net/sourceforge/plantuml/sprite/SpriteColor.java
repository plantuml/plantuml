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
package net.sourceforge.plantuml.sprite;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorGradient;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class SpriteColor implements Sprite {

	private final int width;
	private final int height;
	private final int grey[][];
	private final int color[][];

	public SpriteColor(int width, int height) {
		this.width = width;
		this.height = height;
		this.grey = new int[height][width];
		this.color = new int[height][width];
	}

	public void setGrey(int x, int y, int level) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		if (level < 0 || level >= 16) {
			throw new IllegalArgumentException();
		}
		grey[y][x] = level;
		color[y][x] = -1;
	}

	public void setColor(int x, int y, int col) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		grey[y][x] = -1;
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

		if (backcolor == null) {
			backcolor = HColorUtils.WHITE;
		}
		if (forecolor == null) {
			forecolor = HColorUtils.BLACK;
		}
		final HColorGradient gradient = new HColorGradient(backcolor, forecolor, '\0');
		for (int col = 0; col < width; col++) {
			for (int line = 0; line < height; line++) {
				final int localColor = color[line][col];
				if (localColor == -1) {
					final double coef = 1.0 * grey[line][col] / (16 - 1);
					final Color c = gradient.getColor(colorMapper, coef);
					im.setRGB(col, line, c.getRGB());
				} else {
					im.setRGB(col, line, localColor);
				}
			}
		}
		return new UImage(im);
	}

	public TextBlock asTextBlock(final HColor color, final double scale) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final UImage image = toUImage(ug.getColorMapper(), ug.getParam().getBackcolor(), color);
				ug.draw(image.scale(scale));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(getWidth() * scale, getHeight() * scale);
			}
		};
	}

}
