/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public class SpriteMonochrome implements Sprite {

	private final int width;
	private final int height;
	private final int grayLevel;
	private final int pixels[][];

	SpriteMonochrome(int width, int height, int grayLevel) {
		if (grayLevel != 2 && grayLevel != 4 && grayLevel != 8 && grayLevel != 16) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.height = height;
		this.grayLevel = grayLevel;
		this.pixels = new int[height][width];
	}

	void setPixel(int x, int y, int level) {
		if (x < 0 || x >= width) {
			return;
		}
		if (y < 0 || y >= height) {
			return;
		}
		if (level < 0 || level >= grayLevel) {
			throw new IllegalArgumentException("level=" + level + " grayLevel=" + grayLevel);
		}
		pixels[y][x] = level;
	}

	public int getHeight() {
		return height;
	}

	int getWidth() {
		return width;
	}

	public UImage toUImage(ColorMapper colorMapper, HtmlColor backcolor, HtmlColor color) {
		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		if (backcolor == null) {
			backcolor = HtmlColorUtils.WHITE;
		}
		if (color == null) {
			color = HtmlColorUtils.BLACK;
		}
		final HtmlColorGradient gradient = new HtmlColorGradient(backcolor, color, '\0');
		for (int col = 0; col < width; col++) {
			for (int line = 0; line < height; line++) {
				final double coef = 1.0 * pixels[line][col] / (grayLevel - 1);
				final Color c = gradient.getColor(colorMapper, coef);
				im.setRGB(col, line, c.getRGB());
			}
		}
		return new UImage(im);
	}

	public TextBlock asTextBlock(final HtmlColor color) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				ug.draw(toUImage(ug.getColorMapper(), ug.getParam().getBackcolor(), color));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(getWidth(), getHeight());
			}
		};
	}

}
