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

import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.plantuml.style.MergeStrategy;
import net.sourceforge.plantuml.style.PName;

/**
 * The compiled, queryable counterpart of a {@link MergedStyleSheet}: a single
 * {@link StyleAtomTrie} -- there is no separate tree for {@code @media} content, since
 * {@link MergedStyleNode} already folds a dark declaration into the very same node as its
 * light counterpart. {@link #resolve(StyleQuery)} therefore never picks a theme: it returns,
 * for each property, whichever of light/dark (or both) the matching declarations set, exactly
 * like the legacy {@code Style}/{@code ValueImpl} pipeline leaves the choice to
 * {@code HColor#withDark} at render time rather than to style resolution.
 *
 * It intentionally keeps no cache: a memoized resolver for repeated queries is future work,
 * not part of this pass.
 */
public final class CompiledStyleSheet {

	private final StyleAtomTrie<CompiledStyleRule> base;

	private CompiledStyleSheet(StyleAtomTrie<CompiledStyleRule> base) {
		this.base = base;
	}

	public static CompiledStyleSheet compile(MergedStyleSheet sheet) {
		return new CompiledStyleSheet(StyleAtomTrie.compile(sheet.getBase()));
	}

	public StyleAtomTrie<CompiledStyleRule> getBase() {
		return base;
	}

	/**
	 * Folds every declaration matching {@code query} together by priority (see
	 * {@link StyleMerge#mergeAll}), using {@link MergeStrategy#OVERWRITE_EXISTING_VALUE} -- the
	 * same strategy the legacy code always uses for this kind of plain (non-stereotype-list)
	 * resolution. Every call re-walks the trie; nothing here is memoized.
	 */
	public Map<PName, PrioritizedValue> resolve(StyleQuery query) {
		return StyleMerge.mergeAll(base.findMatching(query), MergeStrategy.OVERWRITE_EXISTING_VALUE);
	}

	/**
	 * Convenience for callers that do not (yet) need the light/dark distinction: projects each
	 * resolved value to {@link PrioritizedValue#getValue()} (light if present, else dark). A
	 * property with genuinely different light and dark values loses its dark half here --
	 * prefer {@link #resolve(StyleQuery)} for anything color-related.
	 */
	public Map<PName, String> resolveToStrings(StyleQuery query) {
		final Map<PName, PrioritizedValue> resolved = resolve(query);
		final Map<PName, String> result = new LinkedHashMap<PName, String>();
		for (Map.Entry<PName, PrioritizedValue> ent : resolved.entrySet())
			result.put(ent.getKey(), ent.getValue().getValue());
		return result;
	}

}
