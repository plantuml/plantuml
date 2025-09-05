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

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.FileUtils;
import net.sourceforge.plantuml.emoji.SvgNanoParser;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.Log;

public class SpriteImage implements Sprite {

	private final UImage img;

	public SpriteImage(BufferedImage img) {
		this.img = new UImage(new PixelImage(Objects.requireNonNull(img), AffineTransformType.TYPE_BILINEAR));
	}

	public TextBlock asTextBlock(final HColor fontColor, final HColor forcedColor, final double scale) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final ColorMapper colorMapper = ug.getColorMapper();
				final HColor usedColor = forcedColor == null ? fontColor : forcedColor;

				if (colorMapper == ColorMapper.MONOCHROME)
					ug.draw(img.monochrome().scale(scale));
				else if (usedColor == null)
					ug.draw(img.scale(scale));
				else
					ug.draw(img.muteColor(usedColor.toColor(colorMapper)).scale(scale));
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(img.getWidth() * scale, img.getHeight() * scale);
			}
		};
	}

	public static Sprite fromInternal(String name) {
		if (name.endsWith(".png") || name.endsWith(".svg"))
			throw new IllegalArgumentException();

		try {
			InputStream is;
			is = getInternalSprite(name + ".svg");
			if (is != null)
				return new SvgNanoParser(FileUtils.readAllBytes(is));
			is = getInternalSprite(name + ".png");
			if (is != null)
				return new SpriteImage(SImageIO.read(is));
			return null;

		} catch (Throwable e) {
			Logme.error(e);
			return null;
		}

	}

	public static InputStream getInternalSprite(final String inner) {
		final String path = "/sprites/" + inner;
		Log.info(() -> "Triying " + path);
		final InputStream is = SpriteImage.class.getResourceAsStream(path);
		return is;
	}

}
