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

public class ColorChangerMonochrome {

	public Color getChangedColor(Color color) {
		if (color == null) {
			return null;
		}
		final int grayScale = getGrayScale(color);
		return new Color(grayScale, grayScale, grayScale);
	}

	private static int getGrayScale(Color color) {
		final int red = color.getRed();
		final int green = color.getGreen();
		final int blue = color.getBlue();
		return (int) (red * .3 + green * .59 + blue * .11);
	}

	public static int getGrayScale(int rgb) {
		final int red = rgb & 0x00FF0000;
		final int green = (rgb & 0x0000FF00) >> 8;
		final int blue = (rgb & 0x000000FF) >> 16;
		return (int) (red * .3 + green * .59 + blue * .11);
	}
}
