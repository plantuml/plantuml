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
 */
package net.sourceforge.plantuml.tim;

/**
 * Prefix tree used by the preprocessor to find variable and function names.
 * <p>
 * {@link #getLonguestMatchStartingIn(String, int)} is called for every
 * character of every preprocessed line, so this class avoids boxing, hashing
 * and any allocation when there is no match.
 * <p>
 * Small nodes keep their children in a short unsorted list. Once a node has
 * many children (typically the root, or the node after <code>$</code>), its
 * ASCII children move to a table indexed directly by the character.
 */
public class TrieImpl implements Trie {

	private static final int SMALL_LIMIT = 8;
	private static final int ASCII = 128;

	// Children not stored in ascii[] (all of them while the node is small)
	private char[] keys;
	private TrieImpl[] children;
	private int size;

	// Direct table for ASCII children, allocated once the node is large
	private TrieImpl[] ascii;

	// Total number of children
	private int count;

	// True if a word ends on this node
	private boolean terminal;

	public void add(String s) {
		if (s.indexOf('\0') != -1)
			throw new IllegalArgumentException();

		TrieImpl current = this;
		for (int i = 0; i < s.length(); i++)
			current = current.getOrCreate(s.charAt(i));

		current.terminal = true;
	}

	public boolean remove(String s) {
		if (s.isEmpty())
			throw new UnsupportedOperationException();

		TrieImpl current = this;
		for (int i = 0; i < s.length(); i++) {
			current = current.get(s.charAt(i));
			if (current == null)
				return false;
		}
		final boolean result = current.terminal;
		current.terminal = false;
		return result;
	}

	public String getLonguestMatchStartingIn(String s, int pos) {
		TrieImpl current = this;
		int i = pos;
		final int length = s.length();
		while (i < length) {
			final TrieImpl child = current.get(s.charAt(i));
			// A node left without any word after a remove() stops the walk
			if (child == null || (child.count == 0 && child.terminal == false))
				break;
			current = child;
			i++;
		}
		return current.terminal ? s.substring(pos, i) : "";
	}

	private TrieImpl get(char c) {
		if (ascii != null && c < ASCII)
			return ascii[c];

		for (int i = 0; i < size; i++)
			if (keys[i] == c)
				return children[i];

		return null;
	}

	private TrieImpl getOrCreate(char c) {
		final TrieImpl existing = get(c);
		if (existing != null)
			return existing;

		if (ascii == null && count >= SMALL_LIMIT)
			switchToAsciiTable();

		final TrieImpl result = new TrieImpl();
		if (ascii != null && c < ASCII)
			ascii[c] = result;
		else
			append(c, result);

		count++;
		return result;
	}

	private void append(char c, TrieImpl child) {
		if (keys == null) {
			keys = new char[2];
			children = new TrieImpl[2];
		} else if (size == keys.length) {
			final char[] newKeys = new char[size * 2];
			final TrieImpl[] newChildren = new TrieImpl[size * 2];
			System.arraycopy(keys, 0, newKeys, 0, size);
			System.arraycopy(children, 0, newChildren, 0, size);
			keys = newKeys;
			children = newChildren;
		}
		keys[size] = c;
		children[size] = child;
		size++;
	}

	private void switchToAsciiTable() {
		ascii = new TrieImpl[ASCII];
		int kept = 0;
		for (int i = 0; i < size; i++) {
			if (keys[i] < ASCII) {
				ascii[keys[i]] = children[i];
			} else {
				keys[kept] = keys[i];
				children[kept] = children[i];
				kept++;
			}
		}
		for (int i = kept; i < size; i++)
			children[i] = null;

		size = kept;
	}

}
