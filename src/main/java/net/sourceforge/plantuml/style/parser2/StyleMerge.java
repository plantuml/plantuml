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

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.StyleLoader;

/**
 * Folds several {@link CompiledStyleRule} matches for the same query into one property map,
 * combining each property by priority (and, when one match is light-only and another
 * dark-only, folding both into one value) instead of "whichever {@link StyleAtomTrie} happened
 * to visit last" -- the direct counterpart of the legacy
 * {@code net.sourceforge.plantuml.style.Style#mergeWith} / {@code StyleBuilder#getMergedStyleSpecial}.
 *
 * {@link PrioritizedValue#mergeWith(PrioritizedValue)} is associative and commutative, so
 * folding matches one at a time here gives the exact same result as combining several
 * already-merged maps afterwards -- which is what lets an ancestor cascade (mindmap/wbs style:
 * one query per ancestor level, each with its own {@link #mergeAllWithDeltaForStarred delta}) be
 * assembled by calling this repeatedly and merging the partial results, exactly as
 * {@code Idea#getStyle()} does today with a loop of {@code getMergedStyleSpecial} calls.
 */
public final class StyleMerge {

	private StyleMerge() {
	}

	/** Same threshold the legacy code boosts a stereotype-matched value's priority by. */
	public static final int DELTA_PRIORITY_FOR_STEREOTYPE = StyleLoader.DELTA_PRIORITY_FOR_STEREOTYPE;

	/**
	 * Merges {@code incoming} on top of {@code accumulated}, mirroring {@code Style#mergeWith}
	 * exactly: for each property {@code incoming} sets, if {@code strategy} is
	 * {@link MergeStrategy#KEEP_EXISTING_VALUE_OF_STEREOTYPE} and the accumulated value is
	 * already a stereotype-boosted one (priority strictly above
	 * {@link #DELTA_PRIORITY_FOR_STEREOTYPE}), it is left untouched, whatever {@code incoming}'s
	 * own priority is; otherwise the two are combined with
	 * {@link PrioritizedValue#mergeWith(PrioritizedValue)}.
	 */
	public static Map<PName, PrioritizedValue> mergeInto(Map<PName, PrioritizedValue> accumulated,
			Map<PName, PrioritizedValue> incoming, MergeStrategy strategy) {
		final Map<PName, PrioritizedValue> result = new EnumMap<PName, PrioritizedValue>(accumulated);

		for (Map.Entry<PName, PrioritizedValue> ent : incoming.entrySet()) {
			final PrioritizedValue existing = result.get(ent.getKey());
			if (strategy == MergeStrategy.KEEP_EXISTING_VALUE_OF_STEREOTYPE && existing != null
					&& existing.getPriority() > DELTA_PRIORITY_FOR_STEREOTYPE)
				continue;

			result.put(ent.getKey(), ent.getValue().mergeWith(existing));
		}

		return result;
	}

	/** Shifts every value in {@code values} by {@code delta}, keeping the same properties. */
	public static Map<PName, PrioritizedValue> shiftPriority(Map<PName, PrioritizedValue> values, int delta) {
		final Map<PName, PrioritizedValue> result = new EnumMap<PName, PrioritizedValue>(PName.class);
		for (Map.Entry<PName, PrioritizedValue> ent : values.entrySet())
			result.put(ent.getKey(), ent.getValue().shiftPriority(delta));
		return result;
	}

	/**
	 * Folds every match in {@code matches}, in order, into one property map by priority -- the
	 * replacement for a plain {@code Map#putAll} loop over {@link StyleAtomTrie#findMatching}'s
	 * results.
	 */
	public static Map<PName, PrioritizedValue> mergeAll(List<CompiledStyleRule> matches, MergeStrategy strategy) {
		Map<PName, PrioritizedValue> result = new EnumMap<PName, PrioritizedValue>(PName.class);
		for (CompiledStyleRule rule : matches)
			result = mergeInto(result, rule.getPrioritizedProperties(), strategy);
		return result;
	}

	/**
	 * Same as {@link #mergeAll}, but first shifts by {@code deltaPriority} the properties of
	 * every match whose declaration is starred -- exactly {@code StyleBuilder#getMergedStyleSpecial}'s
	 * {@code if (key.isStarred()) tmp = tmp.deltaPriority(deltaPriority);}. This is what an
	 * ancestor-inheritance cascade (mindmap/wbs) uses: querying an ancestor with a star query
	 * (see {@link LevelConstraint}) only ever reaches starred (catch-all) declarations in the
	 * first place, and this delta then lets a closer ancestor's catch-all still lose to the
	 * element's own explicit declaration, or to a nearer ancestor's, once the results of several
	 * such calls (one per ancestor level, each with its own delta) are merged together with
	 * {@link #mergeInto}.
	 */
	public static Map<PName, PrioritizedValue> mergeAllWithDeltaForStarred(List<CompiledStyleRule> matches,
			int deltaPriority, MergeStrategy strategy) {
		Map<PName, PrioritizedValue> result = new EnumMap<PName, PrioritizedValue>(PName.class);
		for (CompiledStyleRule rule : matches) {
			Map<PName, PrioritizedValue> values = rule.getPrioritizedProperties();
			if (rule.getLevelConstraint().isStar())
				values = shiftPriority(values, deltaPriority);
			result = mergeInto(result, values, strategy);
		}
		return result;
	}

}
