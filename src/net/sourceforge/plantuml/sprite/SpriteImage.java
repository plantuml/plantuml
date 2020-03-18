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

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class SpriteImage implements Sprite {

	private final UImage img;

	public SpriteImage(BufferedImage img) {
		this.img = new UImage(img);
	}

	public TextBlock asTextBlock(final HColor color, final double scale) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				if (color == null) {
					ug.draw(img.scale(scale));
				} else {
					ug.draw(img.muteColor(((HColorSimple) color).getColor999()).scale(scale));
				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(img.getWidth() * scale, img.getHeight() * scale);
			}
		};
	}

	public static Sprite fromInternal(String name) {
		if (name.endsWith(".png")) {
			throw new IllegalArgumentException();
		}
		final InputStream is = getInternalSprite(name + ".png");
		if (is == null) {
			return null;
		}
		try {
			return new SpriteImage(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static InputStream getInternalSprite(final String inner) {
		final String path = "/sprites/" + inner;
		final InputStream is = SpriteImage.class.getResourceAsStream(path);
		return is;
	}

}
