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
import java.util.TreeSet;

public class ColorTrieNode {

	private final ColorTrieNode[] children = new ColorTrieNode[26];
	private Color value;

	public final static ColorTrieNode INSTANCE = new ColorTrieNode();
	public final static Collection<String> NAMES = new TreeSet<>();

	static {
		// Taken from http://perl.wikipedia.com/wiki/Named_colors ?
		// http://www.w3schools.com/HTML/html_colornames.asp
		register("AliceBlue", new Color(0xF0F8FF));
		register("AntiqueWhite", new Color(0xFAEBD7));
		register("Aqua", new Color(0x00FFFF));
		register("Aquamarine", new Color(0x7FFFD4));
		register("Azure", new Color(0xF0FFFF));
		register("Beige", new Color(0xF5F5DC));
		register("Bisque", new Color(0xFFE4C4));
		register("Black", new Color(0x000000));
		register("BlanchedAlmond", new Color(0xFFEBCD));
		register("Blue", new Color(0x0000FF));
		register("BlueViolet", new Color(0x8A2BE2));
		register("Brown", new Color(0xA52A2A));
		register("BurlyWood", new Color(0xDEB887));
		register("CadetBlue", new Color(0x5F9EA0));
		register("Chartreuse", new Color(0x7FFF00));
		register("Chocolate", new Color(0xD2691E));
		register("Coral", new Color(0xFF7F50));
		register("CornflowerBlue", new Color(0x6495ED));
		register("Cornsilk", new Color(0xFFF8DC));
		register("Crimson", new Color(0xDC143C));
		register("Cyan", new Color(0x00FFFF));
		register("DarkBlue", new Color(0x00008B));
		register("DarkCyan", new Color(0x008B8B));
		register("DarkGoldenRod", new Color(0xB8860B));
		register("DarkGray", new Color(0xA9A9A9));
		register("DarkGrey", new Color(0xA9A9A9));
		register("DarkGreen", new Color(0x006400));
		register("DarkKhaki", new Color(0xBDB76B));
		register("DarkMagenta", new Color(0x8B008B));
		register("DarkOliveGreen", new Color(0x556B2F));
		register("Darkorange", new Color(0xFF8C00));
		register("DarkOrchid", new Color(0x9932CC));
		register("DarkRed", new Color(0x8B0000));
		register("DarkSalmon", new Color(0xE9967A));
		register("DarkSeaGreen", new Color(0x8FBC8F));
		register("DarkSlateBlue", new Color(0x483D8B));
		register("DarkSlateGray", new Color(0x2F4F4F));
		register("DarkSlateGrey", new Color(0x2F4F4F));
		register("DarkTurquoise", new Color(0x00CED1));
		register("DarkViolet", new Color(0x9400D3));
		register("DeepPink", new Color(0xFF1493));
		register("DeepSkyBlue", new Color(0x00BFFF));
		register("DimGray", new Color(0x696969));
		register("DimGrey", new Color(0x696969));
		register("DodgerBlue", new Color(0x1E90FF));
		register("FireBrick", new Color(0xB22222));
		register("FloralWhite", new Color(0xFFFAF0));
		register("ForestGreen", new Color(0x228B22));
		register("Fuchsia", new Color(0xFF00FF));
		register("Gainsboro", new Color(0xDCDCDC));
		register("GhostWhite", new Color(0xF8F8FF));
		register("Gold", new Color(0xFFD700));
		register("GoldenRod", new Color(0xDAA520));
		register("Gray", new Color(0x808080));
		register("Grey", new Color(0x808080));
		register("Green", new Color(0x008000));
		register("GreenYellow", new Color(0xADFF2F));
		register("HoneyDew", new Color(0xF0FFF0));
		register("HotPink", new Color(0xFF69B4));
		register("IndianRed", new Color(0xCD5C5C));
		register("Indigo", new Color(0x4B0082));
		register("Ivory", new Color(0xFFFFF0));
		register("Khaki", new Color(0xF0E68C));
		register("Lavender", new Color(0xE6E6FA));
		register("LavenderBlush", new Color(0xFFF0F5));
		register("LawnGreen", new Color(0x7CFC00));
		register("LemonChiffon", new Color(0xFFFACD));
		register("LightBlue", new Color(0xADD8E6));
		register("LightCoral", new Color(0xF08080));
		register("LightCyan", new Color(0xE0FFFF));
		register("LightGoldenRodYellow", new Color(0xFAFAD2));
		register("LightGray", new Color(0xD3D3D3));
		register("LightGrey", new Color(0xD3D3D3));
		register("LightGreen", new Color(0x90EE90));
		register("LightPink", new Color(0xFFB6C1));
		register("LightSalmon", new Color(0xFFA07A));
		register("LightSeaGreen", new Color(0x20B2AA));
		register("LightSkyBlue", new Color(0x87CEFA));
		register("LightSlateGray", new Color(0x778899));
		register("LightSlateGrey", new Color(0x778899));
		register("LightSteelBlue", new Color(0xB0C4DE));
		register("LightYellow", new Color(0xFFFFE0));
		register("Lime", new Color(0x00FF00));
		register("LimeGreen", new Color(0x32CD32));
		register("Linen", new Color(0xFAF0E6));
		register("Magenta", new Color(0xFF00FF));
		register("Maroon", new Color(0x800000));
		register("MediumAquaMarine", new Color(0x66CDAA));
		register("MediumBlue", new Color(0x0000CD));
		register("MediumOrchid", new Color(0xBA55D3));
		register("MediumPurple", new Color(0x9370D8));
		register("MediumSeaGreen", new Color(0x3CB371));
		register("MediumSlateBlue", new Color(0x7B68EE));
		register("MediumSpringGreen", new Color(0x00FA9A));
		register("MediumTurquoise", new Color(0x48D1CC));
		register("MediumVioletRed", new Color(0xC71585));
		register("MidnightBlue", new Color(0x191970));
		register("MintCream", new Color(0xF5FFFA));
		register("MistyRose", new Color(0xFFE4E1));
		register("Moccasin", new Color(0xFFE4B5));
		register("NavajoWhite", new Color(0xFFDEAD));
		register("Navy", new Color(0x000080));
		register("OldLace", new Color(0xFDF5E6));
		register("Olive", new Color(0x808000));
		register("OliveDrab", new Color(0x6B8E23));
		register("Orange", new Color(0xFFA500));
		register("OrangeRed", new Color(0xFF4500));
		register("Orchid", new Color(0xDA70D6));
		register("PaleGoldenRod", new Color(0xEEE8AA));
		register("PaleGreen", new Color(0x98FB98));
		register("PaleTurquoise", new Color(0xAFEEEE));
		register("PaleVioletRed", new Color(0xD87093));
		register("PapayaWhip", new Color(0xFFEFD5));
		register("PeachPuff", new Color(0xFFDAB9));
		register("Peru", new Color(0xCD853F));
		register("Pink", new Color(0xFFC0CB));
		register("Plum", new Color(0xDDA0DD));
		register("PowderBlue", new Color(0xB0E0E6));
		register("Purple", new Color(0x800080));
		register("Red", new Color(0xFF0000));
		register("RosyBrown", new Color(0xBC8F8F));
		register("RoyalBlue", new Color(0x4169E1));
		register("SaddleBrown", new Color(0x8B4513));
		register("Salmon", new Color(0xFA8072));
		register("SandyBrown", new Color(0xF4A460));
		register("SeaGreen", new Color(0x2E8B57));
		register("SeaShell", new Color(0xFFF5EE));
		register("Sienna", new Color(0xA0522D));
		register("Silver", new Color(0xC0C0C0));
		register("SkyBlue", new Color(0x87CEEB));
		register("SlateBlue", new Color(0x6A5ACD));
		register("SlateGray", new Color(0x708090));
		register("SlateGrey", new Color(0x708090));
		register("Snow", new Color(0xFFFAFA));
		register("SpringGreen", new Color(0x00FF7F));
		register("SteelBlue", new Color(0x4682B4));
		register("Tan", new Color(0xD2B48C));
		register("Teal", new Color(0x008080));
		register("Thistle", new Color(0xD8BFD8));
		register("Tomato", new Color(0xFF6347));
		register("Turquoise", new Color(0x40E0D0));
		register("Violet", new Color(0xEE82EE));
		register("Wheat", new Color(0xF5DEB3));
		register("White", new Color(0xFFFFFF));
		register("WhiteSmoke", new Color(0xF5F5F5));
		register("Yellow", new Color(0xFFFF00));
		register("YellowGreen", new Color(0x9ACD32));
		// Archimate
		register("BUSINESS", new Color(0xFFFFCC));
		register("APPLICATION", new Color(0xC2F0FF));
		register("MOTIVATION", new Color(0xCCCCFF));
		register("STRATEGY", new Color(0xF8E7C0));
		register("TECHNOLOGY", new Color(0xC9FFC9));
		register("PHYSICAL", new Color(0x97FF97));
		register("IMPLEMENTATION", new Color(0xFFE0E0));
	}

	private ColorTrieNode child(char k, boolean create) {
		final int idx = ((k | 0x20) - 'a');
		if (idx < 0 || idx >= 26)
			return null;

		ColorTrieNode c = children[idx];
		if (c == null && create) {
			c = new ColorTrieNode();
			children[idx] = c;
		}
		return c;
	}

	private static void register(String name, Color color) {
		NAMES.add(name);
		INSTANCE.putColor(name, color);

	}

	public void putColor(CharSequence name, Color color) {
		ColorTrieNode n = this;
		for (int i = 0, len = name.length(); i < len; i++) {
			n = n.child(name.charAt(i), true);
			if (n == null)
				return;
		}
		n.value = color;
	}

	public Color getColor(CharSequence name) {
		ColorTrieNode n = this;
		for (int i = 0, len = name.length(); i < len; i++) {
			n = n.child(name.charAt(i), false);
			if (n == null)
				return null;

		}
		return n.value;
	}

}