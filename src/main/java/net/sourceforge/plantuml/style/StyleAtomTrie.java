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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set-trie: every declaration is stored under the sorted path of {@link StyleAtom} it
 * requires, and {@link #findMatching(StyleQuery)} walks it once to return every declaration
 * whose required atom set is a subset of the query's -- the same test as the legacy
 * {@code element.snames.containsAll(declaration.snames)} (plus stereotypes), but sharing
 * common prefixes across declarations instead of re-testing each one in turn, and with no
 * separate plain/legacy storage to keep in sync.
 *
 * {@code depth(n)} / {@code *} play no part in the trie's atom alphabet at all: every stored
 * entry carries its own {@link LevelConstraint} alongside its payload, checked as a cheap
 * side-condition once a candidate is found by the atom walk, exactly as designed.
 *
 * The payload type {@code T} is deliberately opaque to the trie: {@code net.sourceforge.plantuml.style.StyleIndex}
 * is the only indexing structure using it today, over legacy {@code net.sourceforge.plantuml.style.Style}
 * objects ({@code T} = {@code Style}), but nothing here is specific to that type.
 */
public final class StyleAtomTrie<T> {

	private final TrieNode<T> root = new TrieNode<T>();

	private static final class TrieNode<T> {
		// A plain hash map, not a TreeMap: insert()/collect() only ever do point get()/put()
		// lookups on this map, keyed by one StyleAtom at a time -- neither walks it in key
		// order, so StyleAtom's Comparable ordering (which the query-side sort in
		// findMatching still relies on) buys nothing here, only an O(log n) comparison
		// (a string compare, for stereotype atoms) on every edge traversal instead of an
		// O(1) hash lookup.
		private final Map<StyleAtom, TrieNode<T>> children = new HashMap<StyleAtom, TrieNode<T>>();
		private final List<Stored<T>> rulesHere = new ArrayList<Stored<T>>();
	}

	private static final class Stored<T> {
		private final LevelConstraint levelConstraint;
		private final T payload;

		private Stored(LevelConstraint levelConstraint, T payload) {
			this.levelConstraint = levelConstraint;
			this.payload = payload;
		}
	}

	public StyleAtomTrie() {
	}

	/**
	 * Stores {@code payload} under {@code atoms}, matched later only against a query whose
	 * {@link LevelConstraint} it accepts (see {@link LevelConstraint#matches}). {@code atoms} must
	 * already be in ascending order (its natural order, per {@link StyleAtom#compareTo}) and
	 * duplicate-free -- exactly what a {@code SortedSet<StyleAtom>} or an {@code AtomArray}
	 * already guarantees -- since {@link #findMatching} walks a stored path and the query's own
	 * atoms in lockstep, both assumed sorted the same way.
	 */
	public void insert(Iterable<StyleAtom> atoms, LevelConstraint levelConstraint, T payload) {
		TrieNode<T> current = root;
		for (StyleAtom atom : atoms) {
			TrieNode<T> child = current.children.get(atom);
			if (child == null) {
				child = new TrieNode<T>();
				current.children.put(atom, child);
			}
			current = child;
		}
		current.rulesHere.add(new Stored<T>(levelConstraint, payload));
	}

	/**
	 * Every stored payload whose required atoms are all present in {@code query}'s, and whose
	 * {@link LevelConstraint} accepts the query's, in the order the trie holds them (roughly
	 * insertion order for entries sharing the same atom path). This does not merge or rank the
	 * results -- that is resolver work, left for later.
	 */
	public List<T> findMatching(StyleQuery query) {
		final List<StyleAtom> queryAtoms = new ArrayList<StyleAtom>();
		for (StyleAtom atom : query.getAtoms())
			queryAtoms.add(atom);
		final List<T> result = new ArrayList<T>();
		collect(root, queryAtoms, 0, query.getLevelConstraint(), result);
		return result;
	}

	private static <T> void collect(TrieNode<T> node, List<StyleAtom> queryAtoms, int fromIndex,
			LevelConstraint queryLevel, List<T> result) {
		for (Stored<T> stored : node.rulesHere)
			if (LevelConstraint.matches(stored.levelConstraint, queryLevel))
				result.add(stored.payload);

		for (int i = fromIndex; i < queryAtoms.size(); i++) {
			final TrieNode<T> child = node.children.get(queryAtoms.get(i));
			if (child != null)
				collect(child, queryAtoms, i + 1, queryLevel, result);
		}
	}

}
