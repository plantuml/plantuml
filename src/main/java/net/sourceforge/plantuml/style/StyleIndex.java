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

import net.sourceforge.plantuml.style.parser2.CompiledStyleSheet;
import net.sourceforge.plantuml.style.parser2.MergedStyleSheet;
import net.sourceforge.plantuml.style.parser2.StyleAtomTrie;
import net.sourceforge.plantuml.style.parser2.StyleQuery;

/**
 * The fast, queryable counterpart of the old {@code StyleStorage}. Keeps two things apart,
 * traded off very differently:
 * <ul>
 * <li>the <b>base</b> style sheet -- a whole .skin file, loaded once (see
 * {@code StyleBuilder#forBaseStyleText}) and then shared, byte-for-byte, across every diagram
 * that reuses this cached {@link StyleBuilder} (see {@code StyleLoader#loadSkin}'s cache and
 * {@link StyleBuilder#cloneMe}) -- kept as a single {@link MergedStyleSheet} tree and compiled,
 * lazily and only once, straight into a {@link CompiledStyleSheet}: one walk of the tree, not
 * the tree flattened into N legacy {@link Style} objects first (as {@code LegacyStyleFlattener}
 * still does for {@link #getAllStyles()}) and then that flat list walked again to build a
 * second, {@code StyleAtom}-keyed index, which is what this class used to do;</li>
 * <li>everything <b>muted</b> in afterwards -- {@code <style>} overlays, imported style sheets,
 * CSS commands, stereotype overrides -- kept as the flat {@link Style} objects they always were,
 * in a small {@link StyleAtomTrie}. An overlay is small (a handful of declarations, never a
 * whole skin file) and never shared across diagrams the way the base is, so there is no
 * comparable win in replacing this half.</li>
 * </ul>
 * Resolving a query folds both halves together: the base is answered by the compiled engine
 * alone (see {@link CompiledStyleSheet#resolveAsStyle}), then whatever the (usually empty)
 * overlay trie also matches is folded on top with the very same {@link Style#mergeWith} cascade
 * as before -- both halves draw declaration order from the very same {@link StyleBuilder}
 * counter (see {@code MergedStyleSheet#build(RawStyleSheet, AutomaticCounter)}), so an overlay
 * declaration still always outranks a base one of equal specificity, whichever half resolves it.
 *
 * This is the actual fix for the slow style resolution ({@link StyleBuilder#getMergedStyle},
 * {@link StyleBuilder#getMergedStyleSpecial}) the {@code parser2} engine was built for -- see
 * {@code net.sourceforge.plantuml.style.parser2.LevelConstraint} and
 * {@code net.sourceforge.plantuml.style.parser2.StyleAtom} for how the subset-query trie itself
 * replicates the legacy {@code StyleSignatureBasic#matchAll} semantics exactly (level, star,
 * {@code SName} set and stereotype set all included).
 *
 * Immutable: {@link #withLoaded(Style)} and {@link #withMuted(Collection)} both return a new
 * index, leaving this one exactly as queryable as before, so that a {@link StyleBuilder} cached
 * and reused across diagrams never has a later diagram's own {@code <style>} override leak back
 * into the shared, cached instance it was cloned from.
 */
public final class StyleIndex {

	private static final StyleIndex EMPTY = new StyleIndex(MergedStyleSheet.empty(),
			Collections.<Style> emptyList());

	private final MergedStyleSheet baseSheet;
	private final List<Style> overlayStyles;

	// Lazily built from baseSheet on first query, then reused for this index's whole lifetime --
	// a base sheet is loaded once but queried once per diagram element, so this amortizes the
	// (cheap, a few hundred entries at most) compile cost across every query this index, and
	// every StyleBuilder#cloneMe clone sharing it, will ever answer. Racing this on two threads
	// only risks the harmless case of compiling it twice, since every compile from the same
	// (immutable) baseSheet produces an equally valid result.
	private volatile CompiledStyleSheet compiledBaseCache;

	// Same idea as compiledBaseCache, but for the (usually empty, always much smaller) overlay
	// half -- see this class's own documentation for why the two are not worth unifying.
	private volatile StyleAtomTrie<Style> overlayTrieCache;

	// Lazily flattened from baseSheet, only the first time getAllStyles() is actually asked for
	// (StyleBuilder#printMe and #createStyleStereotype -- a cold, debug-ish or rare path, unlike
	// getMergedStyle), instead of eagerly at load time as the old flatten-everything-up-front
	// design forced on every caller whether it ever asked for this or not.
	private volatile List<Style> allStylesCache;

	// Memoizes getMergedStyle by query, exactly like the old StyleBuilder#mergedStyleCache
	// used to before this index existed: getMergedStyle is called from some 300 sites across
	// the diagram packages, essentially once per rendered element, but any given element kind
	// (e.g. "root document sequenceDiagram message arrow") shares the same StyleQuery across
	// every one of its occurrences in a diagram -- so without this, resolving it (a trie walk
	// down findMatching plus the Style#mergeWith cascade over whatever it finds) is redone
	// from scratch for every single occurrence instead of once. Safe to key by StyleQuery
	// as-is (it is immutable with proper equals/hashCode) and safe to keep for this index's
	// whole lifetime (this index itself is immutable -- withLoaded/withMuted always return a
	// new one -- so there is no later mutation this cache could ever go stale against).
	private final Map<StyleQuery, Style> mergedStyleCache = new ConcurrentHashMap<StyleQuery, Style>();

	private StyleIndex(MergedStyleSheet baseSheet, List<Style> overlayStyles) {
		this.baseSheet = baseSheet;
		this.overlayStyles = overlayStyles;
	}

	public static StyleIndex empty() {
		return EMPTY;
	}

	/**
	 * A fresh index over a whole, already-compiled base style sheet, with nothing muted into it
	 * yet -- what {@code StyleBuilder#forBaseStyleText} builds a .skin file's own index with.
	 */
	public static StyleIndex forBase(MergedStyleSheet baseSheet) {
		return new StyleIndex(baseSheet, Collections.<Style> emptyList());
	}

	/**
	 * The counterpart of the old {@code StyleBuilder#loadInternal}'s
	 * {@code storage.get(signature)}/{@code storage.put(...)} pair: a light-only
	 * "root { FontColor black }" and a later, dark-only "@media (dark) { root { FontColor white }
	 * }" sharing the exact same {@link StyleQuery} MUST be folded into one {@link Style} right
	 * here, not left for later resolution to hand back as two separate entries -- see
	 * {@link #withMuted(Collection)} for why. Kept for whatever still loads one {@link Style} at
	 * a time (see {@code StyleBuilder#loadInternal}); a whole base .skin file no longer goes
	 * through this one call at a time -- see {@link #forBase(MergedStyleSheet)}.
	 */
	public StyleIndex withLoaded(Style newStyle) {
		final List<Style> updated = new ArrayList<Style>(overlayStyles.size() + 1);
		updated.addAll(overlayStyles);
		mergeOrAppend(updated, newStyle);
		return new StyleIndex(baseSheet, Collections.unmodifiableList(updated));
	}

	/**
	 * The counterpart of the old {@code StyleBuilder#muteStyle}: folds every style in
	 * {@code newStyles} into a brand new index's overlay half, this one left untouched --
	 * merging each one, in turn, with whatever already shares its exact signature (either
	 * already in this index's overlay, or earlier in {@code newStyles} itself). Deferring that
	 * fold to query time would be unsound: a cross-selector cascade merge (say, "root" combined
	 * with a more specific "root document ganttDiagram") uses specificity alone to decide the
	 * winner once both sides already carry a value ({@code DarkString#mergeWith}'s final,
	 * non-combining branch), so a still-unmerged dark-only "root" entry -- whose specificity is
	 * always higher, since every dark declaration is numbered after every light one -- can beat
	 * an already-combined light+dark value from a less specific selector and silently drop that
	 * combined value's light half. Pre-merging same-signature entries here, exactly as the old
	 * code did, keeps every entry this index ever hands out fully self-consistent (both halves
	 * set whenever either declaration set either), so that later cascade folding only ever has
	 * to pick a winner between two complete values.
	 */
	public StyleIndex withMuted(Collection<Style> newStyles) {
		if (newStyles.isEmpty())
			return this;

		final List<Style> updated = new ArrayList<Style>(overlayStyles.size() + newStyles.size());
		updated.addAll(overlayStyles);
		for (Style modifiedStyle : newStyles)
			mergeOrAppend(updated, modifiedStyle);
		return new StyleIndex(baseSheet, Collections.unmodifiableList(updated));
	}

	/**
	 * Folds {@code newStyle} into whichever element of {@code list} already carries the exact same
	 * signature, in place (so the merged entry keeps its original position, exactly like
	 * {@code Map#put} on an existing key leaves a {@code LinkedHashMap}'s iteration order alone) --
	 * or appends it as a new entry when no such element exists yet. {@code list} is always the
	 * (small) overlay half -- the base half is never touched here, see this class's own
	 * documentation.
	 */
	private static void mergeOrAppend(List<Style> list, Style newStyle) {
		final StyleQuery signature = newStyle.getQuery();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getQuery().equals(signature)) {
				list.set(i, list.get(i).mergeWith(newStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE));
				return;
			}
		}
		list.add(newStyle);
	}

	/**
	 * Whether this index has no style at all -- checked cheaply, straight off the base tree (see
	 * {@code MergedStyleNode#isEmpty()}), without forcing the full flatten {@link #getAllStyles()}
	 * would ({@code StyleLoader#loadSkinSlow}'s own "no style found in this file" guard needs
	 * exactly this, and only this, right after loading -- not the flattened list itself).
	 */
	public boolean isEmpty() {
		return overlayStyles.isEmpty() && baseSheet.getBase().isEmpty();
	}

	private CompiledStyleSheet compiledBase() {
		CompiledStyleSheet result = compiledBaseCache;
		if (result == null) {
			result = CompiledStyleSheet.compile(baseSheet.getBase());
			compiledBaseCache = result;
		}
		return result;
	}

	private StyleAtomTrie<Style> overlayTrie() {
		StyleAtomTrie<Style> result = overlayTrieCache;
		if (result == null) {
			result = new StyleAtomTrie<Style>();
			for (Style style : overlayStyles) {
				final StyleQuery asDeclaration = style.getQuery();
				result.insert(asDeclaration.getAtoms(), asDeclaration.getLevelConstraint(), style);
			}
			overlayTrieCache = result;
		}
		return result;
	}

	Style getMergedStyle(StyleQuery query) {
		final Style cached = mergedStyleCache.get(query);
		if (cached != null)
			return cached;

		// Not computeIfAbsent: computeMergedStyle legitimately returns null when nothing
		// matches (see below), and computeIfAbsent never records a null result, so a
		// no-match query would otherwise be recomputed on every single call forever. A
		// plain get/put pair costs one extra (harmless, idempotent) recomputation on a
		// race between two callers instead -- still infinitely better than never caching
		// a hit at all.
		final Style computed = computeMergedStyle(query);
		if (computed != null)
			mergedStyleCache.put(query, computed);

		return computed;
	}

	private Style computeMergedStyle(StyleQuery query) {
		Style mergedStyle = compiledBase().resolveAsStyle(query);
		for (Style overlay : overlayTrie().findMatching(query))
			mergedStyle = mergedStyle == null ? overlay : mergedStyle.mergeWith(overlay, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		return mergedStyle;
	}

	/**
	 * The counterpart of the old {@code StyleBuilder#getMergedStyleSpecial}: same idea as
	 * {@link #getMergedStyle(StyleQuery)}, but every starred (catch-all) declaration matching
	 * {@code query} -- base or overlay -- is first re-ranked at ancestor rank {@code ancestorRank}
	 * (see {@code Specificity#withAncestorRank}) before folding, exactly what the mindmap/wbs
	 * ancestor-inheritance cascade needs (one call per ancestor level, each at its own rank, the
	 * partial results then merged by the caller -- see {@code Idea#getStyle()}). Not memoized,
	 * unlike {@link #getMergedStyle}: called far less often, and at a different rank each time.
	 */
	Style getMergedStyleSpecial(StyleQuery query, int ancestorRank) {
		Style mergedStyle = compiledBase().resolveWithAncestorRank(query, ancestorRank);
		for (Style overlay : overlayTrie().findMatching(query)) {
			Style tmp = overlay;
			if (overlay.getQuery().getLevelConstraint().isStar())
				tmp = tmp.withAncestorRank(ancestorRank);
			mergedStyle = mergedStyle == null ? tmp : mergedStyle.mergeWith(tmp, MergeStrategy.OVERWRITE_EXISTING_VALUE);
		}
		return mergedStyle;
	}

	/**
	 * Every style ever loaded or muted into this index, base and overlay alike, flattened and
	 * cached the first time this is actually called -- {@code StyleBuilder#printMe} and
	 * {@code #createStyleStereotype} are the only two callers, and both are cold enough (a debug
	 * dump, and an exact-signature scan for a bare stereotype selector) that most
	 * {@link StyleIndex} instances never need this at all.
	 */
	public List<Style> getAllStyles() {
		List<Style> result = allStylesCache;
		if (result == null) {
			final List<Style> flattenedBase = LegacyStyleFlattener.flatten(baseSheet.getBase());
			final List<Style> combined = new ArrayList<Style>(flattenedBase.size() + overlayStyles.size());
			combined.addAll(flattenedBase);
			combined.addAll(overlayStyles);
			result = Collections.unmodifiableList(combined);
			allStylesCache = result;
		}
		return result;
	}

}
