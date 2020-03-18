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
package net.sourceforge.plantuml.ugraphic.color;

import java.awt.Color;

public class ColorUtils {

	static int getGrayScale(Color color) {
		final int grayScale = (int) (color.getRed() * .3 + color.getGreen() * .59 + color.getBlue() * .11);
		return grayScale;
	}

	public static Color getGrayScaleColor(Color color) {
		final int grayScale = getGrayScale(color);
		return new Color(grayScale, grayScale, grayScale);
	}

	public static Color getGrayScaleColorReverse(Color color) {
		final int grayScale = 255 - getGrayScale(color);
		return new Color(grayScale, grayScale, grayScale);
	}

	/*
	 * This code is still experimental. If you can improve it, please go ahead :-)
	 * 
	 * Many thanks to Alexei Boronine for the idea.
	 * 
	 * Some pointer to help you: https://www.hsluv.org/
	 * https://www.kuon.ch/post/2020-03-08-hsluv/
	 * https://www.boronine.com/2012/03/26/Color-Spaces-for-Human-Beings/
	 * 
	 */
	public static synchronized Color getReversed(Color color) {
		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();

		final double hsluv[] = HUSLColorConverter.rgbToHsluv(new double[] { red / 256.0, green / 256.0, blue / 256.0 });

		final double h = hsluv[0];
		final double s = hsluv[1];
		double l = hsluv[2];

		if (s > 40 && s < 60) {
			if (l > 50) {
				l -= 50;
			} else if (l < 50) {
				l += 50;
			}
		} else {
			l = 100 - l;
		}

		final double rgb[] = HUSLColorConverter.hsluvToRgb(new double[] { h, s, l });

		final int red2 = to255(rgb[0]);
		final int green2 = to255(rgb[1]);
		final int blue2 = to255(rgb[2]);

		return new Color(red2, green2, blue2);
	}

	private static int to255(final double value) {
		final int result = (int) (255 * value);
		if (result < 0) {
			return 0;
		}
		if (result > 255) {
			return 255;
		}
		return result;
	}

}
