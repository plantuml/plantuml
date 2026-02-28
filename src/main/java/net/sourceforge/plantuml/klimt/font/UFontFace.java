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
package net.sourceforge.plantuml.klimt.font;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable value object that combines a font style (normal/italic) and a font
 * weight (CSS 100-900). This mirrors the CSS model where {@code font-style} and
 * {@code font-weight} are independent axes.
 * <p>
 * A {@code UFontFace} can be applied to any {@link java.awt.Font} via
 * {@link #deriveFont(Font)} to produce a font with the requested style and
 * weight.
 */
public class UFontFace {

	private static final UFontFace NORMAL = new UFontFace(UFontStyle.NORMAL, UFontWeight.fromCssValue(400));
	private static final UFontFace BOLD = new UFontFace(UFontStyle.NORMAL, UFontWeight.fromCssValue(700));
	private static final UFontFace ITALIC = new UFontFace(UFontStyle.ITALIC, UFontWeight.fromCssValue(400));
	private static final UFontFace BOLD_ITALIC = new UFontFace(UFontStyle.ITALIC, UFontWeight.fromCssValue(700));

	private final UFontStyle style;
	private final UFontWeight weight;

	private UFontFace(UFontStyle style, UFontWeight weight) {
		this.style = Objects.requireNonNull(style);
		this.weight = Objects.requireNonNull(weight);
	}

	/**
	 * Returns the standard normal face (weight 400, non-italic).
	 */
	public static UFontFace normal() {
		return NORMAL;
	}

	/**
	 * Returns the standard bold face (weight 700, non-italic).
	 */
	public static UFontFace bold() {
		return BOLD;
	}

	/**
	 * Returns the standard italic face (weight 400, italic).
	 */
	public static UFontFace italic() {
		return ITALIC;
	}

	/**
	 * Returns the standard bold-italic face (weight 700, italic).
	 */
	public static UFontFace boldItalic() {
		return BOLD_ITALIC;
	}

	/**
	 * Creates a face from an explicit style and weight.
	 */
	public static UFontFace of(UFontStyle style, UFontWeight weight) {
		return new UFontFace(style, weight);
	}

	/**
	 * Creates a face from a CSS numeric weight (100-900) and a style.
	 */
	public static UFontFace of(UFontStyle style, int cssWeight) {
		return new UFontFace(style, UFontWeight.fromCssValue(cssWeight));
	}

	/**
	 * Creates a face from a legacy {@link java.awt.Font} style constant
	 * ({@code Font.PLAIN}, {@code Font.BOLD}, {@code Font.ITALIC},
	 * {@code Font.BOLD | Font.ITALIC}).
	 */
	public static UFontFace fromLegacyStyle(int fontStyle) {
		final UFontStyle s = (fontStyle & Font.ITALIC) != 0 ? UFontStyle.ITALIC : UFontStyle.NORMAL;
		final int w = (fontStyle & Font.BOLD) != 0 ? 700 : 400;
		return new UFontFace(s, UFontWeight.fromCssValue(w));
	}

	/**
	 * Parses a CSS {@code font-weight} string. Accepts numeric values (100-900)
	 * and keywords: {@code normal}, {@code bold}, {@code lighter},
	 * {@code bolder}.
	 *
	 * @return a face with the parsed weight and NORMAL style, or {@code null} if
	 *         the input cannot be parsed
	 */
	public static UFontFace fromCssWeight(String cssValue) {
		if (cssValue == null)
			return null;

		final String trimmed = cssValue.trim().toLowerCase();
		switch (trimmed) {
		case "normal":
			return NORMAL;
		case "bold":
			return BOLD;
		case "lighter":
			return of(UFontStyle.NORMAL, 300);
		case "bolder":
			return of(UFontStyle.NORMAL, 800);
		default:
			try {
				final int w = Integer.parseInt(trimmed);
				return of(UFontStyle.NORMAL, w);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	public UFontStyle getStyle() {
		return style;
	}

	public UFontWeight getWeight() {
		return weight;
	}

	public int getCssWeight() {
		return weight.getWeight();
	}

	public boolean isItalic() {
		return style == UFontStyle.ITALIC;
	}

	public boolean isBold() {
		return weight.getWeight() >= 700;
	}

	/**
	 * Returns the legacy {@link java.awt.Font} style flags corresponding to this
	 * face. This is useful for renderers that do not support fine-grained weights
	 * (e.g. EPS).
	 */
	public int toLegacyStyle() {
		int result = Font.PLAIN;
		if (isItalic())
			result |= Font.ITALIC;
		if (isBold())
			result |= Font.BOLD;
		return result;
	}

	/**
	 * Returns a new face with the given style, keeping the current weight.
	 */
	public UFontFace withStyle(UFontStyle newStyle) {
		if (this.style == newStyle)
			return this;
		return new UFontFace(newStyle, this.weight);
	}

	/**
	 * Returns a new face with the given CSS weight, keeping the current style.
	 */
	public UFontFace withWeight(int cssWeight) {
		final UFontWeight newWeight = UFontWeight.fromCssValue(cssWeight);
		if (this.weight.equals(newWeight))
			return this;
		return new UFontFace(this.style, newWeight);
	}

	/**
	 * Returns a new face with the given weight, keeping the current style.
	 */
	public UFontFace withWeight(UFontWeight newWeight) {
		return new UFontFace(this.style, newWeight);
	}

	// ::remove folder when __HAXE__

	/**
	 * Maps a CSS numeric weight (100-900) to the corresponding
	 * {@link TextAttribute#WEIGHT} float constant.
	 */
	public float toTextAttributeWeight() {
		final int w = weight.getWeight();
		if (w <= 200)
			return TextAttribute.WEIGHT_EXTRA_LIGHT;
		if (w <= 300)
			return TextAttribute.WEIGHT_LIGHT;
		if (w <= 400)
			return TextAttribute.WEIGHT_REGULAR;
		if (w <= 500)
			return TextAttribute.WEIGHT_MEDIUM;
		if (w <= 600)
			return TextAttribute.WEIGHT_SEMIBOLD;
		if (w <= 700)
			return TextAttribute.WEIGHT_BOLD;
		if (w <= 800)
			return TextAttribute.WEIGHT_HEAVY;
		return TextAttribute.WEIGHT_ULTRABOLD;
	}

	/**
	 * Derives a new {@link java.awt.Font} from the given base font, applying both
	 * style (italic) and weight via {@link TextAttribute}s.
	 * <p>
	 * This is the primary integration point for Java2D rendering (PNG). For
	 * renderers that do not support fine-grained weights (EPS, SVG text), use
	 * {@link #toLegacyStyle()} instead.
	 */
	public Font deriveFont(Font baseFont) {
		final Map<TextAttribute, Object> attrs = new HashMap<>();
		attrs.put(TextAttribute.WEIGHT, toTextAttributeWeight());
		if (isItalic())
			attrs.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
		return baseFont.deriveFont(attrs);
	}

	// ::done

	/**
	 * Returns a CSS-compatible {@code font-weight} string for SVG output.
	 */
	public String toCssWeightString() {
		return String.valueOf(weight.getWeight());
	}

	/**
	 * Returns a CSS-compatible {@code font-style} string for SVG output.
	 */
	public String toCssStyleString() {
		return isItalic() ? "italic" : "normal";
	}

	@Override
	public int hashCode() {
		return Objects.hash(style, weight.getWeight());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UFontFace))
			return false;
		final UFontFace other = (UFontFace) obj;
		return this.style == other.style && this.weight.getWeight() == other.weight.getWeight();
	}

	@Override
	public String toString() {
		return "UFontFace[" + style + ", weight=" + weight.getWeight() + "]";
	}

}
