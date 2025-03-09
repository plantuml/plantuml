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
package net.sourceforge.plantuml.klimt.color;

import java.awt.Color;

public class HColorMiddle extends HColor {
	// ::remove file when __HAXE__

	private final HColor color1;
	private final HColor color2;

	HColorMiddle(HColor c1, HColor c2) {
		this.color1 = c1;
		this.color2 = c2;
	}

	public final HColor getColor1() {
		return color1;
	}

	public final HColor getColor2() {
		return color2;
	}

	@Override
	public Color toColor(ColorMapper mapper) {
		final Color cc1 = color1.toColor(mapper);
		final Color cc2 = color2.toColor(mapper);
		final int r1 = cc1.getRed();
		final int g1 = cc1.getGreen();
		final int b1 = cc1.getBlue();
		final int r2 = cc2.getRed();
		final int g2 = cc2.getGreen();
		final int b2 = cc2.getBlue();

		final int r = (r1 + r2) / 2;
		final int g = (g1 + g2) / 2;
		final int b = (b1 + b2) / 2;
		return new Color(r, g, b);
	}

}
