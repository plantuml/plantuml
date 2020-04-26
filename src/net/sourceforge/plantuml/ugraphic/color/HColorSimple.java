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

public class HColorSimple extends HColorAbstract implements HColor {

	private final Color color;
	private final boolean monochrome;

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	@Override
	public String toString() {
		if (color.getAlpha() == 0) {
			return "transparent";
		}
		return color.toString() + " alpha=" + color.getAlpha() + " monochrome=" + monochrome;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof HColorSimple == false) {
			return false;
		}
		return this.color.equals(((HColorSimple) other).color);
	}

	public HColorSimple(Color c, boolean monochrome) {
		this.color = c;
		this.monochrome = monochrome;
	}

	public Color getColor999() {
		return color;
	}

	public HColorSimple asMonochrome() {
		if (monochrome) {
			throw new IllegalStateException();
		}
		return new HColorSimple(new ColorChangerMonochrome().getChangedColor(color), true);
	}

	public HColorSimple opposite() {
		final Color mono = new ColorChangerMonochrome().getChangedColor(color);
		final int grayScale = 255 - mono.getGreen() > 127 ? 255 : 0;
		return new HColorSimple(new Color(grayScale, grayScale, grayScale), true);
	}

	public double distance(HColorSimple other) {
		final int diffRed = Math.abs(this.color.getRed() - other.color.getRed());
		final int diffGreen = Math.abs(this.color.getGreen() - other.color.getGreen());
		final int diffBlue = Math.abs(this.color.getBlue() - other.color.getBlue());
		return diffRed * .3 + diffGreen * .59 + diffBlue * .11;
	}

}
