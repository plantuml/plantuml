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
package net.sourceforge.plantuml.style;

import java.util.HashMap;
import java.util.Map;

public class StringTrie<V> {

	private static class Node<V> {
		V value;
		Map<Character, Node<V>> children = new HashMap<>();
	}

	private final Node<V> root = new Node<>();

	public void put(String key, V value) {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null");
		Node<V> node = root;
		for (int i = 0; i < key.length(); i++) {
			final char c = Character.toLowerCase(key.charAt(i));
			node = node.children.computeIfAbsent(c, k -> new Node<>());
		}
		node.value = value;
	}

	public V get(String key) {
		if (key == null)
			return null;
		Node<V> node = root;
		for (int i = 0; i < key.length(); i++) {
			final char c = Character.toLowerCase(key.charAt(i));
			node = node.children.get(c);
			if (node == null)
				return null;
		}
		return node.value;
	}
}
