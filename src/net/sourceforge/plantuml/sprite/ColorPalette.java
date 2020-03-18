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

import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSimple;

public class ColorPalette {

	private static final String colorValue = "!#$%&*+-:;<=>?@^_~GHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public char getCharFor(Color dest) {
		return getCharFor(new HColorSimple(dest, false));
	}

	public char getCharFor(HColor dest) {
		char result = 0;
		double resultDist = Double.MAX_VALUE;
		for (int i = 0; i < colorValue.length(); i++) {
			final char c = colorValue.charAt(i);
			final double dist = ((HColorSimple) dest).distance(getHtmlColorSimpleFor(c));
			if (dist < resultDist) {
				result = c;
				resultDist = dist;
			}
		}
		assert result != 0;
		return result;
	}

	private HColorSimple getHtmlColorSimpleFor(char c) {
		final Color color = getColorFor(c);
		if (color == null) {
			throw new IllegalArgumentException();
		}
		return new HColorSimple(color, false);
	}

	public Color getColorFor(char c) {
		final int col = colorValue.indexOf(c);
		if (col == -1) {
			return null;
		}
		final int blue = (col % 4) * 85;
		final int green = ((col / 4) % 4) * 85;
		final int red = ((col / 16) % 4) * 85;
		return new Color(red, green, blue);
	}

}
