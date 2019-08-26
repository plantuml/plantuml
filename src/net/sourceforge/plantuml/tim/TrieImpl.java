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
 */
package net.sourceforge.plantuml.tim;

import java.util.HashMap;
import java.util.Map;

public class TrieImpl implements Trie {

	private final Map<Character, TrieImpl> brothers = new HashMap<Character, TrieImpl>();

	public void add(String s) {
		if (s.indexOf('\0') != -1) {
			throw new IllegalArgumentException();
		}
		addInternal(this, s + "\0");
	}

	private static void addInternal(TrieImpl current, String s) {
		if (s.length() == 0) {
			throw new UnsupportedOperationException();
		}
		while (s.length() > 0) {
			final Character added = s.charAt(0);
			final TrieImpl child = current.getOrCreate(added);
			s = s.substring(1);
			current = child;
		}
	}

	public boolean remove(String s) {
		return removeInternal(this, s + "\0");
	}

	private static boolean removeInternal(TrieImpl current, String s) {
		if (s.length() <= 1) {
			throw new UnsupportedOperationException();
		}
		while (s.length() > 0) {
			final Character first = s.charAt(0);
			final TrieImpl child = current.brothers.get(first);
			if (child == null) {
				return false;
			}
			s = s.substring(1);
			if (s.length() == 1) {
				assert s.charAt(0) == '\0';
				return child.brothers.remove('\0') != null;
			}
			current = child;
		}
		throw new IllegalStateException();
	}

	private TrieImpl getOrCreate(Character added) {
		TrieImpl result = brothers.get(added);
		if (result == null) {
			result = new TrieImpl();
			brothers.put(added, result);
		}
		return result;
	}

	public String getLonguestMatchStartingIn(String s) {
		return getLonguestMatchStartingIn(this, s);
	}

	private static String getLonguestMatchStartingIn(TrieImpl current, String s) {
		final StringBuilder result = new StringBuilder();
		while (current != null) {
			if (s.length() == 0) {
				if (current.brothers.containsKey('\0')) {
					return result.toString();
				} else {
					return "";
				}
			}
			final TrieImpl child = current.brothers.get(s.charAt(0));
			if (child == null || child.brothers.size() == 0) {
				if (current.brothers.containsKey('\0')) {
					return result.toString();
				} else {
					return "";
				}
			}
			result.append(s.charAt(0));
			current = child;
			s = s.substring(1);
		}
		return "";

	}

}
