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
package net.sourceforge.plantuml.style.parser2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sourceforge.plantuml.style.SName;

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
 * The payload type {@code T} is deliberately opaque to the trie -- it is what lets the very
 * same indexing structure serve both {@link CompiledStyleSheet} (compiled from a
 * {@link MergedStyleNode} tree, {@code T} = {@link CompiledStyleRule}) and the legacy
 * {@code net.sourceforge.plantuml.style.StyleBuilder} (indexing legacy
 * {@code net.sourceforge.plantuml.style.Style} objects directly, {@code T} = {@code Style})
 * without either one knowing about the other.
 */
public final class StyleAtomTrie<T> {

	private final TrieNode<T> root = new TrieNode<T>();

	private static final class TrieNode<T> {
		private final Map<StyleAtom, TrieNode<T>> children = new TreeMap<StyleAtom, TrieNode<T>>();
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
	 * {@link LevelConstraint} it accepts (see {@link LevelConstraint#matches}).
	 */
	public void insert(SortedSet<StyleAtom> atoms, LevelConstraint levelConstraint, T payload) {
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
		final List<StyleAtom> queryAtoms = new ArrayList<StyleAtom>(query.getAtoms());
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

	/**
	 * Compiles one {@link MergedStyleNode} tree (in practice, {@link MergedStyleSheet#getBase()}
	 * -- there is only ever the one tree now, light and dark declarations already folded
	 * together by {@link MergedStyleNode#mergeRule}) into a fresh trie: every node with
	 * properties becomes one {@link CompiledStyleRule}, keyed by the {@link StyleAtom} path
	 * accumulated from the root, with {@code depth(n)} nodes folded into a
	 * {@link LevelConstraint} instead of an atom, and any {@code *} met along the way -- on a
	 * {@code depth(n)} node or a plain one -- carried forward onto every declaration below it.
	 */
	public static StyleAtomTrie<CompiledStyleRule> compile(MergedStyleNode root) {
		final StyleAtomTrie<CompiledStyleRule> trie = new StyleAtomTrie<CompiledStyleRule>();
		compileNode(root, new TreeSet<StyleAtom>(), LevelConstraint.none(), trie);
		return trie;
	}

	private static void compileNode(MergedStyleNode node, TreeSet<StyleAtom> pathAtoms, LevelConstraint inherited,
			StyleAtomTrie<CompiledStyleRule> target) {
		if (node.getProperties().isEmpty() == false)
			target.insert(pathAtoms, inherited, new CompiledStyleRule(inherited, node.getProperties()));

		// A selector declared once plain and once starred lives in two separate MergedStyleNode
		// child slots, never one -- see MergedStyleNode's own javadoc -- so each child kind is
		// walked across both its plain and its starred map.
		compileNamedChildren(node.getNamedChildren(), pathAtoms, inherited, target);
		compileNamedChildren(node.getStarredNamedChildren(), pathAtoms, inherited, target);
		compileOtherChildren(node, node.getOtherChildren(), pathAtoms, inherited, target);
		compileOtherChildren(node, node.getStarredOtherChildren(), pathAtoms, inherited, target);
	}

	private static void compileNamedChildren(Map<SName, MergedStyleNode> children, TreeSet<StyleAtom> pathAtoms,
			LevelConstraint inherited, StyleAtomTrie<CompiledStyleRule> target) {
		for (Map.Entry<SName, MergedStyleNode> ent : children.entrySet()) {
			final MergedStyleNode child = ent.getValue();
			final StyleAtom atom = StyleAtom.of(ent.getKey());
			final LevelConstraint next = withStar(inherited, child.isStar());
			pathAtoms.add(atom);
			compileNode(child, pathAtoms, next, target);
			pathAtoms.remove(atom);
		}
	}

	private static void compileOtherChildren(MergedStyleNode node, Map<String, MergedStyleNode> children,
			TreeSet<StyleAtom> pathAtoms, LevelConstraint inherited, StyleAtomTrie<CompiledStyleRule> target) {
		for (Map.Entry<String, MergedStyleNode> ent : children.entrySet()) {
			final String key = ent.getKey();
			final MergedStyleNode child = ent.getValue();
			final RawSelector.Kind kind = node.getOtherChildKind(key);

			if (kind == RawSelector.Kind.DEPTH) {
				final int level = parseDepthKey(key);
				final LevelConstraint next = LevelConstraint.of(level, inherited.isStar() || child.isStar());
				compileNode(child, pathAtoms, next, target);
				continue;
			}

			if (kind == RawSelector.Kind.UNKNOWN) {
				// A selector SName does not know about: it adds no atom and no level
				// constraint of its own, it simply is not a distinguishing tag.
				final LevelConstraint next = withStar(inherited, child.isStar());
				compileNode(child, pathAtoms, next, target);
				continue;
			}

			final StyleAtom atom = StyleAtom.ofStereotype(key);
			final LevelConstraint next = withStar(inherited, child.isStar());
			pathAtoms.add(atom);
			compileNode(child, pathAtoms, next, target);
			pathAtoms.remove(atom);
		}
	}

	private static LevelConstraint withStar(LevelConstraint inherited, boolean extraStar) {
		if (extraStar == false || inherited.isStar())
			return inherited;
		return LevelConstraint.of(inherited.getLevel(), true);
	}

	private static int parseDepthKey(String key) {
		return Integer.parseInt(key.substring("depth(".length(), key.length() - 1));
	}

}
