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
import java.util.Collection;
import java.util.Collections;

public class HColorSet {

	private final static HColorSet singleton = new HColorSet();

	public static HColorSet instance() {
		return singleton;
	}

	public Collection<String> names() {
		return Collections.unmodifiableCollection(ColorTrieNode.NAMES);
	}

	private HColorSet() {
	}

	public HColor getColorOrWhite(String s) {
		final HColor result = parseColor(s);
		if (result == null)
			return HColors.WHITE;
		return result;
	}

	public HColor getColor(String s) throws NoSuchColorException {
		final HColor result = parseColor(s);

		if (result == null)
			throw new NoSuchColorException();

		return result;
	}

	private HColor parseColor(String s) {
		if (s.startsWith("#"))
			s = s.substring(1);

		if (s.equalsIgnoreCase("transparent") || s.equalsIgnoreCase("background"))
			return HColors.none();

		if (s.equalsIgnoreCase("automatic"))
			return new HColorAutomagic();

		if (s.equalsIgnoreCase("transparent"))
			return HColors.simple(new Color(0, 0, 0, 0));

		final Color result = parseSimpleColor(s);
		if (result != null)
			return HColors.simple(result);

		if (s.startsWith("?")) {
			final String[] colors = s.substring(1).split(":");
			if (colors.length == 2 || colors.length == 3) {
				final Color col0 = parseSimpleColor(colors[0]);
				final Color col1 = parseSimpleColor(colors[1]);
				if (colors.length == 2 && col0 != null && col1 != null)
					return new HColorScheme(HColors.simple(col0), HColors.simple(col1), null);
				final Color col2 = parseSimpleColor(colors[2]);
				if (col2 != null)
					return new HColorScheme(HColors.simple(col0), HColors.simple(col1), HColors.simple(col2));

			}
		}

		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '-' || c == '\\' || c == '|' || c == '/') {
				final Color col0 = parseSimpleColor(s.substring(0, i));
				final Color col1 = parseSimpleColor(s.substring(i + 1));
				if (col0 != null && col1 != null)
					return HColors.gradient(HColors.simple(col0), HColors.simple(col1), c);
			}
		}

		return null;
	}

	private Color parseSimpleColor(String s) {
		if (s.startsWith("#"))
			s = s.substring(1);
		final int len = s.length();

		if (len == 1) {
			final int d = hexNibble(s.charAt(0));
			if (d >= 0) {
				final int v = (d << 4) | d;
				final int rgb = (v << 16) | (v << 8) | v;
				return new Color(rgb);
			}
		} else if (len == 3) {
			final int r = hexNibble(s.charAt(0));
			final int g = hexNibble(s.charAt(1));
			final int b = hexNibble(s.charAt(2));
			if (r >= 0 && g >= 0 && b >= 0) {
				final int rr = (r << 4) | r;
				final int gg = (g << 4) | g;
				final int bb = (b << 4) | b;
				final int rgb = (rr << 16) | (gg << 8) | bb;
				return new Color(rgb);
			}
		} else if (len == 6) {
			final int rgb = parseHex24(s, 0);
			if (rgb >= 0)
				return new Color(rgb);
		} else if (len == 8) {
			// https://forum.plantuml.net/11606/full-opacity-alpha-compositing-support-for-svg-and-png
			final int r = parseHexByte(s, 0);
			final int g = parseHexByte(s, 2);
			final int b = parseHexByte(s, 4);
			final int a = parseHexByte(s, 6);
			if (r >= 0 && g >= 0 && b >= 0 && a >= 0)
				return new Color(r, g, b, a);
		}
		return ColorTrieNode.INSTANCE.getColor(s);
	}

	private static int hexNibble(char c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		else if (c >= 'a' && c <= 'f')
			return c - 'a' + 10;
		else if (c >= 'A' && c <= 'F')
			return c - 'A' + 10;
		else
			return -1;

	}

	private static int parseHexByte(CharSequence s, int off) {
		final int hi = hexNibble(s.charAt(off));
		final int lo = hexNibble(s.charAt(off + 1));
		return (hi | lo) < 0 ? -1 : ((hi << 4) | lo);
	}

	private static int parseHex24(CharSequence s, int off) {
		final int r = parseHexByte(s, off);
		final int g = parseHexByte(s, off + 2);
		final int b = parseHexByte(s, off + 4);
		return (r | g | b) < 0 ? -1 : ((r << 16) | (g << 8) | b);
	}

}
