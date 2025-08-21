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

public class ColorTrieNode {

	private final ColorTrieNode[] children = new ColorTrieNode[26];
	private Color value;

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

	public void putColor(CharSequence name, Color color) {
		ColorTrieNode n = this;
		for (int i = 0, len = name.length(); i < len; i++) {
			ColorTrieNode next = n.child(name.charAt(i), true);
			if (next == null)
				return;
			n = next;
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

	public static void main(String[] args) {
		ColorTrieNode data = new ColorTrieNode();
		data.putColor("Black", Color.BLACK);
		data.putColor("blue", Color.BLUE);
		data.putColor("BlackRed", Color.RED);
		System.out.println(data.getColor("black")); // java.awt.Color[r=0,g=0,b=0]
		System.out.println(data.getColor("BLACK")); // java.awt.Color[r=0,g=0,b=0]
		System.out.println(data.getColor("bLUE")); // java.awt.Color[r=0,g=0,b=255]
		System.out.println(data.getColor("Foo")); // null
		System.out.println(data.getColor("BLACKred")); // java.awt.Color[r=255,g=0,b=0]
	}
}