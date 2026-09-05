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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.style.parser2.StyleAtomTrie;
import net.sourceforge.plantuml.style.parser2.StyleQuery;

/**
 * The fast, queryable counterpart of the old {@code StyleStorage}: every loaded {@link Style}
 * is indexed by its signature's tags (see {@link StyleSignature}) in a
 * {@link StyleAtomTrie}, so {@link #findMatching(StyleSignature)} only ever visits
 * declarations that could possibly match a query, instead of the old linear scan over every
 * style ever loaded checking {@code StyleSignatureBasic#matchAll} one by one -- the actual fix
 * for the slow style resolution ({@link StyleBuilder#getMergedStyle},
 * {@link StyleBuilder#getMergedStyleSpecial}) this whole {@code parser2} engine was built for.
 * The subset-query trie itself already replicates {@code matchAll}'s semantics exactly (level,
 * star, {@code SName} set and stereotype set all included) -- see
 * {@code net.sourceforge.plantuml.style.parser2.LevelConstraint} and
 * {@code net.sourceforge.plantuml.style.parser2.StyleAtom}.
 *
 * Immutable: {@link #withLoaded(Style)} and {@link #withMuted(Collection)} both return a new
 * index, leaving this one exactly as queryable as before, so that a {@link StyleBuilder} cached
 * and reused across diagrams (see {@code StyleLoader#loadSkin}) never has a later diagram's own
 * {@code <style>} override leak back into the shared, cached instance it was cloned from.
 *
 * {@link #getMergedStyle(StyleSignature)}'s result IS memoized here, per signature -- and
 * deliberately at this level rather than in {@link StyleBuilder}: {@code StyleLoader#loadSkin}
 * hands out a fresh {@link StyleBuilder} (via {@code cloneMe()}) for every diagram that shares a
 * given {@code .skin} file, but every one of those clones keeps pointing at the very same
 * {@code StyleIndex} instance as long as neither has muted or loaded anything into it -- the
 * overwhelmingly common case, since most diagrams carry no embedded {@code <style>} override at
 * all. Caching here means every diagram sharing an unmuted skin also shares already-resolved
 * signatures, instead of every diagram separately paying the first-resolution cost for the same
 * answer. Correctness falls out for free from immutability: {@link #withLoaded(Style)} and
 * {@link #withMuted(Collection)} both return a brand new instance -- with its own, empty cache --
 * so a diagram that mutates its copy can never see a stale entry, and can never poison the
 * shared one either.
 */
public final class StyleIndex {

	private static final StyleIndex EMPTY = new StyleIndex(Collections.<Style> emptyList());

	private final List<Style> allStyles;

	private final Map<StyleSignature, Style> mergedStyleCache = new ConcurrentHashMap<StyleSignature, Style>();

	// Lazily (re)built from allStyles on first query after a with...() call produced this
	// index, then reused for every later query -- a style sheet is loaded once but queried
	// once per diagram element, so this amortizes the (cheap, a few hundred entries at most)
	// build cost across every query this index will ever answer. Racing this on two threads
	// only risks the harmless case of building it twice, since every build from the same
	// (immutable) allStyles produces an equally valid trie.
	private volatile StyleAtomTrie<Style> trieCache;

	private StyleIndex(List<Style> allStyles) {
		this.allStyles = allStyles;
	}

	public static StyleIndex empty() {
		return EMPTY;
	}

	/**
	 * The counterpart of the old {@code StyleBuilder#loadInternal}'s
	 * {@code storage.get(signature)}/{@code storage.put(...)} pair, called while loading a .skin
	 * file: a light-only "root { FontColor black }" and a later, dark-only
	 * "@media (dark) { root { FontColor white } }" share the exact same {@link StyleSignature}
	 * and MUST be folded into one {@link Style} right here, at load time -- not left for
	 * {@link #findMatching} to hand back as two separate entries. Deferring that fold to query
	 * time is unsound: a cross-selector cascade merge (say, "root" combined with a more specific
	 * "root document ganttDiagram") uses priority alone to decide the winner once both sides
	 * already carry a value ({@link DarkString#mergeWith}'s final, non-combining branch), so a
	 * still-unmerged dark-only "root" entry -- whose priority is always high, since every dark
	 * declaration in a .skin file is numbered after every light one -- can beat an already-combined
	 * light+dark value from a less specific selector and silently drop that combined value's light
	 * half. Pre-merging same-signature entries here, exactly as the old code did, keeps every
	 * entry this index ever hands out fully self-consistent (both halves set whenever either
	 * declaration set either), so that later cascade folding only ever has to pick a winner
	 * between two complete values.
	 */
	public StyleIndex withLoaded(Style newStyle) {
		final List<Style> updated = new ArrayList<Style>(allStyles.size() + 1);
		updated.addAll(allStyles);
		mergeOrAppend(updated, newStyle);
		return new StyleIndex(Collections.unmodifiableList(updated));
	}

	/**
	 * The counterpart of the old {@code StyleBuilder#muteStyle}: folds every style in
	 * {@code newStyles} into a brand new index, this one left untouched -- merging each one, in
	 * turn, with whatever already shares its exact signature (either already in this index, or
	 * earlier in {@code newStyles} itself), for the same reason {@link #withLoaded} does.
	 */
	public StyleIndex withMuted(Collection<Style> newStyles) {
		if (newStyles.isEmpty())
			return this;

		final List<Style> updated = new ArrayList<Style>(allStyles.size() + newStyles.size());
		updated.addAll(allStyles);
		for (Style modifiedStyle : newStyles)
			mergeOrAppend(updated, modifiedStyle);
		return new StyleIndex(Collections.unmodifiableList(updated));
	}

	/**
	 * Folds {@code newStyle} into whichever element of {@code list} already carries the exact same
	 * signature, in place (so the merged entry keeps its original position, exactly like
	 * {@code Map#put} on an existing key leaves a {@code LinkedHashMap}'s iteration order alone) --
	 * or appends it as a new entry when no such element exists yet.
	 */
	private static void mergeOrAppend(List<Style> list, Style newStyle) {
		final StyleSignature signature = newStyle.getSignature();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSignature().equals(signature)) {
				list.set(i, list.get(i).mergeWith(newStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE));
				return;
			}
		}
		list.add(newStyle);
	}

	private StyleAtomTrie<Style> trie() {
		StyleAtomTrie<Style> result = trieCache;
		if (result == null) {
			result = new StyleAtomTrie<Style>();
			for (Style style : allStyles) {
				final StyleQuery asDeclaration = style.getSignature().toQuery();
				result.insert(asDeclaration.getAtoms(), asDeclaration.getLevelConstraint(), style);
			}
			trieCache = result;
		}
		return result;
	}
	/**
	 * Every loaded style whose signature is a subset of {@code query}'s (per
	 * {@link StyleSignature#matchAll}), in the order they were loaded or muted in.
	 */
	public List<Style> findMatching(StyleQuery query) {
		return trie().findMatching(query);
	}

	Style getMergedStyle(StyleQuery query) {
		return computeMergedStyle(query);
	}

	private Style computeMergedStyle(StyleQuery query) {
		Style mergedStyle = null;
		for (Style style : findMatching(query)) {
			if (mergedStyle == null)
				mergedStyle = style;
			else
				mergedStyle = mergedStyle.mergeWith(style, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		}
		return mergedStyle;
	}

	/** Every style ever loaded or muted into this index, in that same order. */
	public List<Style> getAllStyles() {
		return allStyles;
	}

}
