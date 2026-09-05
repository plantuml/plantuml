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

import java.util.Collection;
import java.util.EnumMap;

import net.sourceforge.plantuml.style.parser2.StyleQuery;

public class StyleBuilder implements AutomaticCounter {

	// The trie-backed replacement for the old, plain-linear-scan StyleStorage -- see
	// StyleIndex's own documentation for why: resolving a style is by far the hottest path
	// through this class (once per diagram element, not once per file), so this is the part
	// that actually had to change; how styles are parsed into Style objects in the first
	// place (loadInternal/muteStyle's callers) is untouched.
	private StyleIndex index = StyleIndex.empty();
	// private final Set<StyleSignature> printedForLog;
	private int counter;

	public void printMe() {
		for (Style style : index.getAllStyles())
			style.printMe();
	}

//	private StyleBuilder(Set<StyleSignature> printedForLog) {
//		this.printedForLog = new LinkedHashSet<>();
//	}

	public StyleBuilder() {
		// this(new LinkedHashSet<StyleSignature>());
	}

	public StyleBuilder cloneMe() {
		final StyleBuilder result = new StyleBuilder();
		result.index = this.index;
		result.counter = this.counter;
		return result;

	}

	public Style createStyleStereotype(String name) {
		if (name.contains(Style.STAR))
			throw new IllegalArgumentException();

		name = name.toLowerCase();
		final StyleQuery signature = StyleQuery.empty().withStereotype(name);

		// An exact-signature lookup, not a cascade: only a bare ".name { ... }" declared with
		// no SName scoping at all can ever match. Several such declarations (repeated in the
		// file) are folded together here rather than pre-merged at load time, since this index
		// no longer deduplicates by exact signature as it is populated -- see StyleIndex.
		Style result = null;
		for (Style style : index.getAllStyles())
			if (style.getSignature().equals(signature))
				result = result == null ? style : result.mergeWith(style, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		if (result == null)
			return new Style(signature, new EnumMap<PName, Value>(PName.class));

		return result;
	}

	public StyleBuilder muteStyle(Collection<Style> modifiedStyles) {
		final StyleBuilder result = new StyleBuilder(/*this.printedForLog*/);
		result.counter = this.counter;
		result.index = this.index.withMuted(modifiedStyles);
		return result;
	}

	public void loadInternal(StyleQuery signature, Style newStyle) {
		if (signature.getLevelConstraint().isStar())
			throw new IllegalArgumentException();

		this.index = this.index.withLoaded(newStyle);
	}

	@Override
	public int getNextInt() {
		return ++counter;
	}

	public Style getMergedStyle(StyleQuery query) {
		return index.getMergedStyle(query);
	}

	public Style getMergedStyleSpecial(StyleQuery query, int ancestorRank) {
//		boolean added = this.printedForLog.add(query);
//		if (added)
//			Log.info(() -> "Using style " + query);

		Style mergedStyle = null;
		for (Style style : index.findMatching(query)) {
			final StyleQuery key = style.getSignature();

			Style tmp = style;
			if (key.getLevelConstraint().isStar())
				tmp = tmp.withAncestorRank(ancestorRank);

			if (mergedStyle == null)
				mergedStyle = tmp;
			else
				mergedStyle = mergedStyle.mergeWith(tmp, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return mergedStyle;
	}

}
