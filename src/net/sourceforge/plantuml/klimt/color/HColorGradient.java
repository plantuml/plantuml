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
 */
package net.sourceforge.plantuml.klimt.color;

import java.awt.Color;
import java.util.Objects;

public class HColorGradient extends HColor {

	private final HColor color1;
	private final HColor color2;
	private final char policy;

	HColorGradient(HColor color1arg, HColor color2arg, char policy) {
		if (color1arg instanceof HColorGradient)
			color1arg = ((HColorGradient) color1arg).color1;

		if (color2arg instanceof HColorGradient)
			color2arg = ((HColorGradient) color2arg).color2;

		this.color1 = Objects.requireNonNull(color1arg);
		this.color2 = Objects.requireNonNull(color2arg);
		this.policy = policy;
	}

	public final HColor getColor1() {
		return color1;
	}

	public final HColor getColor2() {
		return color2;
	}

	public final Color getColor(ColorMapper mapper, double coeff, int alpha) {
		if (coeff > 1 || coeff < 0)
			throw new IllegalArgumentException("c=" + coeff);

		final Color c1 = color1.toColor(mapper);
		final Color c2 = color2.toColor(mapper);

		final int diffRed = c2.getRed() - c1.getRed();
		final int diffGreen = c2.getGreen() - c1.getGreen();
		final int diffBlue = c2.getBlue() - c1.getBlue();

		final int vRed = (int) (coeff * diffRed);
		final int vGreen = (int) (coeff * diffGreen);
		final int vBlue = (int) (coeff * diffBlue);

		final int red = c1.getRed() + vRed;
		final int green = c1.getGreen() + vGreen;
		final int blue = c1.getBlue() + vBlue;

		return new Color(red, green, blue, alpha);

	}

	public final char getPolicy() {
		return policy;
	}

	@Override
	public Color toColor(ColorMapper mapper) {
		return color1.toColor(mapper);
	}

	@Override
	public HColor opposite() {
		return color1.opposite();
	}

}
