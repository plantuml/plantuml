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


import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.plantuml.klimt.awt.XColor;

public class ColorTrieNode {

	private final ColorTrieNode[] children = new ColorTrieNode[26];
	private XColor value;

	public final static ColorTrieNode INSTANCE = new ColorTrieNode();
	public final static Collection<String> NAMES = new TreeSet<>();

	static {
		// Taken from http://perl.wikipedia.com/wiki/Named_colors ?
		// http://www.w3schools.com/HTML/html_colornames.asp
		register("AliceBlue", XColor.from(0xF0F8FF));
		register("AntiqueWhite", XColor.from(0xFAEBD7));
		register("Aqua", XColor.from(0x00FFFF));
		register("Aquamarine", XColor.from(0x7FFFD4));
		register("Azure", XColor.from(0xF0FFFF));
		register("Beige", XColor.from(0xF5F5DC));
		register("Bisque", XColor.from(0xFFE4C4));
		register("Black", XColor.from(0x000000));
		register("BlanchedAlmond", XColor.from(0xFFEBCD));
		register("Blue", XColor.from(0x0000FF));
		register("BlueViolet", XColor.from(0x8A2BE2));
		register("Brown", XColor.from(0xA52A2A));
		register("BurlyWood", XColor.from(0xDEB887));
		register("CadetBlue", XColor.from(0x5F9EA0));
		register("Chartreuse", XColor.from(0x7FFF00));
		register("Chocolate", XColor.from(0xD2691E));
		register("Coral", XColor.from(0xFF7F50));
		register("CornflowerBlue", XColor.from(0x6495ED));
		register("Cornsilk", XColor.from(0xFFF8DC));
		register("Crimson", XColor.from(0xDC143C));
		register("Cyan", XColor.from(0x00FFFF));
		register("DarkBlue", XColor.from(0x00008B));
		register("DarkCyan", XColor.from(0x008B8B));
		register("DarkGoldenRod", XColor.from(0xB8860B));
		register("DarkGray", XColor.from(0xA9A9A9));
		register("DarkGrey", XColor.from(0xA9A9A9));
		register("DarkGreen", XColor.from(0x006400));
		register("DarkKhaki", XColor.from(0xBDB76B));
		register("DarkMagenta", XColor.from(0x8B008B));
		register("DarkOliveGreen", XColor.from(0x556B2F));
		register("Darkorange", XColor.from(0xFF8C00));
		register("DarkOrchid", XColor.from(0x9932CC));
		register("DarkRed", XColor.from(0x8B0000));
		register("DarkSalmon", XColor.from(0xE9967A));
		register("DarkSeaGreen", XColor.from(0x8FBC8F));
		register("DarkSlateBlue", XColor.from(0x483D8B));
		register("DarkSlateGray", XColor.from(0x2F4F4F));
		register("DarkSlateGrey", XColor.from(0x2F4F4F));
		register("DarkTurquoise", XColor.from(0x00CED1));
		register("DarkViolet", XColor.from(0x9400D3));
		register("DeepPink", XColor.from(0xFF1493));
		register("DeepSkyBlue", XColor.from(0x00BFFF));
		register("DimGray", XColor.from(0x696969));
		register("DimGrey", XColor.from(0x696969));
		register("DodgerBlue", XColor.from(0x1E90FF));
		register("FireBrick", XColor.from(0xB22222));
		register("FloralWhite", XColor.from(0xFFFAF0));
		register("ForestGreen", XColor.from(0x228B22));
		register("Fuchsia", XColor.from(0xFF00FF));
		register("Gainsboro", XColor.from(0xDCDCDC));
		register("GhostWhite", XColor.from(0xF8F8FF));
		register("Gold", XColor.from(0xFFD700));
		register("GoldenRod", XColor.from(0xDAA520));
		register("Gray", XColor.from(0x808080));
		register("Grey", XColor.from(0x808080));
		register("Green", XColor.from(0x008000));
		register("GreenYellow", XColor.from(0xADFF2F));
		register("HoneyDew", XColor.from(0xF0FFF0));
		register("HotPink", XColor.from(0xFF69B4));
		register("IndianRed", XColor.from(0xCD5C5C));
		register("Indigo", XColor.from(0x4B0082));
		register("Ivory", XColor.from(0xFFFFF0));
		register("Khaki", XColor.from(0xF0E68C));
		register("Lavender", XColor.from(0xE6E6FA));
		register("LavenderBlush", XColor.from(0xFFF0F5));
		register("LawnGreen", XColor.from(0x7CFC00));
		register("LemonChiffon", XColor.from(0xFFFACD));
		register("LightBlue", XColor.from(0xADD8E6));
		register("LightCoral", XColor.from(0xF08080));
		register("LightCyan", XColor.from(0xE0FFFF));
		register("LightGoldenRodYellow", XColor.from(0xFAFAD2));
		register("LightGray", XColor.from(0xD3D3D3));
		register("LightGrey", XColor.from(0xD3D3D3));
		register("LightGreen", XColor.from(0x90EE90));
		register("LightPink", XColor.from(0xFFB6C1));
		register("LightSalmon", XColor.from(0xFFA07A));
		register("LightSeaGreen", XColor.from(0x20B2AA));
		register("LightSkyBlue", XColor.from(0x87CEFA));
		register("LightSlateGray", XColor.from(0x778899));
		register("LightSlateGrey", XColor.from(0x778899));
		register("LightSteelBlue", XColor.from(0xB0C4DE));
		register("LightYellow", XColor.from(0xFFFFE0));
		register("Lime", XColor.from(0x00FF00));
		register("LimeGreen", XColor.from(0x32CD32));
		register("Linen", XColor.from(0xFAF0E6));
		register("Magenta", XColor.from(0xFF00FF));
		register("Maroon", XColor.from(0x800000));
		register("MediumAquaMarine", XColor.from(0x66CDAA));
		register("MediumBlue", XColor.from(0x0000CD));
		register("MediumOrchid", XColor.from(0xBA55D3));
		register("MediumPurple", XColor.from(0x9370D8));
		register("MediumSeaGreen", XColor.from(0x3CB371));
		register("MediumSlateBlue", XColor.from(0x7B68EE));
		register("MediumSpringGreen", XColor.from(0x00FA9A));
		register("MediumTurquoise", XColor.from(0x48D1CC));
		register("MediumVioletRed", XColor.from(0xC71585));
		register("MidnightBlue", XColor.from(0x191970));
		register("MintCream", XColor.from(0xF5FFFA));
		register("MistyRose", XColor.from(0xFFE4E1));
		register("Moccasin", XColor.from(0xFFE4B5));
		register("NavajoWhite", XColor.from(0xFFDEAD));
		register("Navy", XColor.from(0x000080));
		register("OldLace", XColor.from(0xFDF5E6));
		register("Olive", XColor.from(0x808000));
		register("OliveDrab", XColor.from(0x6B8E23));
		register("Orange", XColor.from(0xFFA500));
		register("OrangeRed", XColor.from(0xFF4500));
		register("Orchid", XColor.from(0xDA70D6));
		register("PaleGoldenRod", XColor.from(0xEEE8AA));
		register("PaleGreen", XColor.from(0x98FB98));
		register("PaleTurquoise", XColor.from(0xAFEEEE));
		register("PaleVioletRed", XColor.from(0xD87093));
		register("PapayaWhip", XColor.from(0xFFEFD5));
		register("PeachPuff", XColor.from(0xFFDAB9));
		register("Peru", XColor.from(0xCD853F));
		register("Pink", XColor.from(0xFFC0CB));
		register("Plum", XColor.from(0xDDA0DD));
		register("PowderBlue", XColor.from(0xB0E0E6));
		register("Purple", XColor.from(0x800080));
		register("Red", XColor.from(0xFF0000));
		register("RosyBrown", XColor.from(0xBC8F8F));
		register("RoyalBlue", XColor.from(0x4169E1));
		register("SaddleBrown", XColor.from(0x8B4513));
		register("Salmon", XColor.from(0xFA8072));
		register("SandyBrown", XColor.from(0xF4A460));
		register("SeaGreen", XColor.from(0x2E8B57));
		register("SeaShell", XColor.from(0xFFF5EE));
		register("Sienna", XColor.from(0xA0522D));
		register("Silver", XColor.from(0xC0C0C0));
		register("SkyBlue", XColor.from(0x87CEEB));
		register("SlateBlue", XColor.from(0x6A5ACD));
		register("SlateGray", XColor.from(0x708090));
		register("SlateGrey", XColor.from(0x708090));
		register("Snow", XColor.from(0xFFFAFA));
		register("SpringGreen", XColor.from(0x00FF7F));
		register("SteelBlue", XColor.from(0x4682B4));
		register("Tan", XColor.from(0xD2B48C));
		register("Teal", XColor.from(0x008080));
		register("Thistle", XColor.from(0xD8BFD8));
		register("Tomato", XColor.from(0xFF6347));
		register("Turquoise", XColor.from(0x40E0D0));
		register("Violet", XColor.from(0xEE82EE));
		register("Wheat", XColor.from(0xF5DEB3));
		register("White", XColor.from(0xFFFFFF));
		register("WhiteSmoke", XColor.from(0xF5F5F5));
		register("Yellow", XColor.from(0xFFFF00));
		register("YellowGreen", XColor.from(0x9ACD32));
		// Archimate
		register("BUSINESS", XColor.from(0xFFFFCC));
		register("APPLICATION", XColor.from(0xC2F0FF));
		register("MOTIVATION", XColor.from(0xCCCCFF));
		register("STRATEGY", XColor.from(0xF8E7C0));
		register("TECHNOLOGY", XColor.from(0xC9FFC9));
		register("PHYSICAL", XColor.from(0x97FF97));
		register("IMPLEMENTATION", XColor.from(0xFFE0E0));
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

	private static void register(String name, XColor color) {
		NAMES.add(name);
		INSTANCE.putColor(name, color);

	}

	public void putColor(CharSequence name, XColor color) {
		ColorTrieNode n = this;
		for (int i = 0, len = name.length(); i < len; i++) {
			n = n.child(name.charAt(i), true);
			if (n == null)
				return;
		}
		n.value = color;
	}

	public XColor getColor(CharSequence name) {
		ColorTrieNode n = this;
		for (int i = 0, len = name.length(); i < len; i++) {
			n = n.child(name.charAt(i), false);
			if (n == null)
				return null;

		}
		return n.value;
	}

}