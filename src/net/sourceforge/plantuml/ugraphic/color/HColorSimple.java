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

import net.sourceforge.plantuml.svek.DotStringFactory;

public class HColorSimple extends HColorAbstract implements HColor {

	private final Color color;
	private final boolean monochrome;

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	@Override
	public String toString() {
		if (isTransparent()) {
			return "transparent";
		}
		return color.toString() + " alpha=" + color.getAlpha() + " monochrome=" + monochrome;
	}

	@Override
	public String asString() {
		if (isTransparent()) {
			return "transparent";
		}
		if (color.getAlpha() == 255) {
			return DotStringFactory.sharp000000(color.getRGB());
		}
		return "#" + Integer.toHexString(color.getRGB());
	}

	@Override
	public HColor lighten(int ratio) {
		final float[] hsl = new HSLColor(color).getHSL();
		hsl[2] += hsl[2] * (ratio / 100.0);
		return new HColorSimple(new HSLColor(hsl).getRGB(), false);
	}

	@Override
	public HColor darken(int ratio) {
		final float[] hsl = new HSLColor(color).getHSL();
		hsl[2] -= hsl[2] * (ratio / 100.0);
		return new HColorSimple(new HSLColor(hsl).getRGB(), false);
	}

	@Override
	public HColor reverseHsluv() {
		return new HColorSimple(ColorUtils.reverseHsluv(color), false);
	}

	@Override
	public HColor reverse() {
		return new HColorSimple(ColorOrder.RGB.getReverse(color), false);
	}

	@Override
	public boolean isDark() {
		return ColorUtils.getGrayScale(color) < 128;
	}

	public boolean isTransparent() {
		return color.getAlpha() == 0;
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

	public final boolean isMonochrome() {
		return monochrome;
	}

	public static HColorSimple unlinear(HColorSimple color1, HColorSimple color2, int completionInt) {
		final HSLColor col1 = new HSLColor(color1.color);
		final HSLColor col2 = new HSLColor(color2.color);

		final float[] hsl1 = col1.getHSL();
		final float[] hsl2 = col2.getHSL();

		if (completionInt > 100)
			completionInt = 100;

		float completion = (float) (completionInt / 100.0);
		completion = completion * completion * completion;
		final float[] hsl = linear(completion, hsl1, hsl2);

		final HSLColor col = new HSLColor(hsl);

		return new HColorSimple(col.getRGB(), color1.monochrome);
	}

	private static float[] linear(float factor, float[] hsl1, float[] hsl2) {
		final float h = linear(factor, hsl1[0], hsl2[0]);
		final float s = linear(factor, hsl1[1], hsl2[1]);
		final float l = linear(factor, hsl1[2], hsl2[2]);
		return new float[] { h, s, l };
	}

	private static float linear(float factor, float x, float y) {
		return (x + (y - x) * factor);
	}

}
