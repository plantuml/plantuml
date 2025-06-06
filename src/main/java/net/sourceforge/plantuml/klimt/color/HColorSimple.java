/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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

import static net.sourceforge.plantuml.klimt.color.HColor.TransparentFillBehavior.WITH_FILL_NONE;

import java.awt.Color;

import net.sourceforge.plantuml.StringUtils;

public class HColorSimple extends HColor {

	private final Color color;
	private final HColor dark;
	private final TransparentFillBehavior transparentFillBehavior;

	@Override
	public String toString() {

		final boolean withDark = dark != null;

		final StringBuilder sb = new StringBuilder();
		if (withDark)
			sb.append("WITHDARK ");
		sb.append(color.toString());
		sb.append(" \u03B1=");
		sb.append(color.getAlpha());
		if (isTransparent())
			sb.append(" transparent");
		if (transparentFillBehavior != WITH_FILL_NONE)
			sb.append(" ").append(transparentFillBehavior);
		return sb.toString();
	}

	// ::comment when __HAXE__
	@Override
	public String asString() {
		if (isTransparent())
			return "transparent";

		if (color.getAlpha() == 255)
			return StringUtils.sharp000000(color.getRGB());

		return "#" + Integer.toHexString(color.getRGB());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof HColorSimple == false)
			return false;

		return this.color.equals(((HColorSimple) other).color);
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}

	@Override
	public HColor lighten(int ratio) {
		final float[] hsl = new HSLColor(color).getHSL();
		hsl[2] += hsl[2] * (ratio / 100.0);
		return HColorSimple.create(new HSLColor(hsl).getRGB());
	}

	@Override
	public HColor darken(int ratio) {
		final float[] hsl = new HSLColor(color).getHSL();
		hsl[2] -= hsl[2] * (ratio / 100.0);
		return HColorSimple.create(new HSLColor(hsl).getRGB());
	}

	@Override
	public HColor reverseHsluv() {
		return HColorSimple.create(ColorUtils.reverseHsluv(color));
	}

	@Override
	public HColor reverse() {
		return HColorSimple.create(ColorOrder.RGB.getReverse(color));
	}
	// ::done

	@Override
	public boolean isDark() {
		return ColorUtils.getGrayScale(color) < 128;
	}

	@Override
	public boolean isTransparent() {
		return color.getAlpha() == 0;
	}

	@Override
	public TransparentFillBehavior transparentFillBehavior() {
		return transparentFillBehavior;
	}

	public static HColorSimple create(Color c) {
		return new HColorSimple(c, null, WITH_FILL_NONE);
	}

	private HColorSimple(Color c, HColor dark, TransparentFillBehavior transparentFillBehavior) {
		this.color = c;
		this.dark = dark;
		this.transparentFillBehavior = transparentFillBehavior;
	}

	public Color getAwtColor() {
		return color;
	}

	public HColor asMonochrome() {
		return HColorSimple.create(ColorUtils.getGrayScaleColor(color));
	}

	// ::comment when __HAXE__
	public HColor asMonochrome(HColorSimple colorForMonochrome, double minGray, double maxGray) {
		final Color tmp = ColorUtils.getGrayScaleColor(color);
		final int gray = tmp.getGreen();
		assert gray == tmp.getBlue();
		assert gray == tmp.getRed();

		final double coef = (gray - minGray) / 256.0;
		final Color result = ColorUtils.grayToColor(coef, colorForMonochrome.color);
		return HColorSimple.create(result);
	}

	@Override
	public Color toColor(ColorMapper mapper) {
		if (this.isTransparent())
			return getAwtColor();
		return mapper.fromColorSimple(this);
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

		return HColorSimple.create(col.getRGB());
	}

	private static float[] linear(float factor, float[] hsl1, float[] hsl2) {
		final float h = linear(factor, hsl1[0], hsl2[0]);
		final float s = linear(factor, hsl1[1], hsl2[1]);
		final float l = linear(factor, hsl1[2], hsl2[2]);
		return new float[] { h, s, l };
	}

	private static float linear(float factor, float x, float y) {
		return x + (y - x) * factor;
	}

	// ::done

	@Override
	public HColor opposite() {
		final Color mono = ColorUtils.getGrayScaleColor(color);
		final int grayScale = 255 - mono.getGreen() > 127 ? 255 : 0;
		return HColorSimple.create(new Color(grayScale, grayScale, grayScale));
	}

	public int distanceTo(HColorSimple other) {
		return ColorUtils.distance(this.color, other.color);
	}

	public boolean isGray() {
		return color.getRed() == color.getGreen() && color.getGreen() == color.getBlue();
	}

	@Override
	public HColor withDark(HColor dark) {
		return new HColorSimple(color, dark, transparentFillBehavior);
	}

	@Override
	public HColor withTransparentFillBehavior(TransparentFillBehavior transparentFillBehavior) {
		return new HColorSimple(color, dark, transparentFillBehavior);
	}

	@Override
	public HColor darkSchemeTheme() {
		if (dark == null)
			return this;
		return dark;
	}

}
