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
package net.sourceforge.plantuml.ngdot.attr;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.klimt.awt.XColor;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;

/**
 * Reading DOT colors.
 * <p>
 * Supported: {@code #rrggbb}, {@code #rrggbbaa}, the color names (looked up in
 * {@link HColorSet}, with the X11 values of Graphviz where they differ from
 * the CSS ones), {@code grayN}/{@code greyN} and the numbered X11 variants
 * ({@code red3}, approximated from the base color), {@code transparent},
 * {@code none} and {@code invis}. A color list ({@code "red:blue"},
 * {@code "red;0.3:blue"}) is reduced to its first color for now.
 * <p>
 * Not supported, read as "no color" so that the theme applies: HSV values
 * ({@code "0.6 0.4 0.9"}) and color schemes ({@code /blues9/3}).
 */
public final class DotColors {

	private static final Map<String, Integer> X11 = new HashMap<String, Integer>();

	static {
		// X11 names whose value differs from the CSS one, or that CSS does not have
		X11.put("gray", 0xBEBEBE);
		X11.put("grey", 0xBEBEBE);
		X11.put("green", 0x00FF00);
		X11.put("maroon", 0xB03060);
		X11.put("purple", 0xA020F0);
		X11.put("lightgoldenrod", 0xEEDD82);
		X11.put("navyblue", 0x000080);
		X11.put("violetred", 0xD02090);
		X11.put("lightslateblue", 0x8470FF);
		X11.put("x11gray", 0xBEBEBE);
		X11.put("x11grey", 0xBEBEBE);
		X11.put("x11green", 0x00FF00);
		X11.put("x11maroon", 0xB03060);
		X11.put("x11purple", 0xA020F0);
	}

	/** X11 numbered variants: 1 is the base color, 2, 3 and 4 are darker. */
	private static final double[] VARIANT_FACTORS = { 1.0, 1.0, 238.0 / 255, 205.0 / 255, 139.0 / 255 };

	private DotColors() {
	}

	/**
	 * Reads a DOT color.
	 *
	 * @return the color, {@code HColors.none()} for a transparent color, or
	 *         {@code null} when the value is missing or not understood
	 */
	public static HColor parse(String value, HColorSet set) {
		if (value == null)
			return null;
		String s = value.trim();
		// A color list: keep the first color, without its weight
		final int colon = s.indexOf(':');
		if (colon != -1)
			s = s.substring(0, colon);
		final int semicolon = s.indexOf(';');
		if (semicolon != -1)
			s = s.substring(0, semicolon);
		s = s.trim();
		if (s.length() == 0)
			return null;

		if (s.startsWith("#"))
			return parseHex(s.substring(1));

		// A color scheme prefix ("/x11/red"): only the X11 scheme is known
		if (s.startsWith("/")) {
			final int slash = s.lastIndexOf('/');
			final String scheme = s.substring(1, Math.max(1, slash));
			if (slash == 0 || (scheme.length() > 0 && scheme.equalsIgnoreCase("x11") == false))
				return null;
			s = s.substring(slash + 1);
		}

		final String name = s.toLowerCase();
		if (isName(name) == false)
			return null;

		if (name.equals("transparent") || name.equals("none") || name.equals("invis"))
			return HColors.none();

		final HColor known = named(name, set);
		if (known != null)
			return known;

		// grayN / greyN, N from 0 to 100
		if (name.startsWith("gray") || name.startsWith("grey")) {
			final Integer level = parseInt(name.substring(4));
			if (level != null && level >= 0 && level <= 100) {
				// As in the X11 table: grey50 is #7F7F7F
				final int v = (int) Math.round(level * 2.55);
				return HColors.simple(new XColor(v, v, v));
			}
		}

		// X11 numbered variants: red1 .. red4
		final char last = name.charAt(name.length() - 1);
		if (last >= '1' && last <= '4') {
			final HColor base = named(name.substring(0, name.length() - 1), set);
			if (base != null)
				return darker(base, VARIANT_FACTORS[last - '0']);
		}
		return null;
	}

	private static HColor named(String name, HColorSet set) {
		final Integer x11 = X11.get(name);
		if (x11 != null)
			return HColors.simple(XColor.from(x11));
		return set.getColorOrNull(name);
	}

	private static HColor darker(HColor color, double factor) {
		if (factor == 1.0)
			return color;
		final XColor c = color.toColor(ColorMapper.IDENTITY);
		return HColors.simple(new XColor((int) Math.round(c.getRed() * factor),
				(int) Math.round(c.getGreen() * factor), (int) Math.round(c.getBlue() * factor)));
	}

	private static HColor parseHex(String hex) {
		if (hex.length() != 6 && hex.length() != 8)
			return null;
		for (int i = 0; i < hex.length(); i++)
			if (Character.digit(hex.charAt(i), 16) == -1)
				return null;
		final int r = Integer.parseInt(hex.substring(0, 2), 16);
		final int g = Integer.parseInt(hex.substring(2, 4), 16);
		final int b = Integer.parseInt(hex.substring(4, 6), 16);
		if (hex.length() == 6)
			return HColors.simple(new XColor(r, g, b));
		final int a = Integer.parseInt(hex.substring(6, 8), 16);
		if (a == 0)
			return HColors.none();
		return HColors.simple(new XColor(r, g, b, a));
	}

	private static boolean isName(String s) {
		if (Character.isLetter(s.charAt(0)) == false)
			return false;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') == false && (c >= '0' && c <= '9') == false)
				return false;
		}
		return true;
	}

	private static Integer parseInt(String s) {
		if (s.length() == 0 || s.length() > 3)
			return null;
		for (int i = 0; i < s.length(); i++)
			if (Character.isDigit(s.charAt(i)) == false)
				return null;
		return Integer.parseInt(s);
	}

}
