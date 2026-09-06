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

import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.style.parser2.MergedStyleSheet;
import net.sourceforge.plantuml.style.parser2.RawStyleParser;
import net.sourceforge.plantuml.style.parser2.RawStyleSheet;
import net.sourceforge.plantuml.style.parser2.StyleQuery;
import net.sourceforge.plantuml.utils.BlocLines;

public class StyleBuilder implements AutomaticCounter {

	// The trie-backed replacement for the old, plain-linear-scan StyleStorage -- see
	// StyleIndex's own documentation for why: resolving a style is by far the hottest path
	// through this class (once per diagram element, not once per file), so this is the part
	// that actually had to change. A whole base .skin file is compiled straight into it (see
	// forBaseStyleText); loadInternal/muteStyle still hand it flat Style objects one at a time
	// or in small batches, exactly as before.
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

	/**
	 * Builds a {@link StyleBuilder} straight from a whole .skin file's text -- the counterpart of
	 * the old {@code StyleLoader#loadSkinSlow} loop that parsed the file into flat {@link Style}
	 * objects first (through {@code StyleLoader#parseStyleText}) and then loaded them one at a
	 * time via {@link #loadInternal(StyleQuery, Style)}, each call re-scanning what had already
	 * been loaded (see the old {@code StyleIndex#mergeOrAppend}) and, on top of that, forcing
	 * every query afterwards to re-derive {@code StyleAtom} paths from those flattened
	 * {@link Style} objects instead of the tree they came from. This compiles that tree exactly
	 * once, directly, via {@link StyleIndex#forBase(MergedStyleSheet)} -- see that class's own
	 * documentation.
	 *
	 * <p>
	 * {@code this} is passed as the {@link net.sourceforge.plantuml.style.AutomaticCounter} to
	 * {@link MergedStyleSheet#build(RawStyleSheet, net.sourceforge.plantuml.style.AutomaticCounter)}
	 * so the returned builder's own counter ends up exactly where the old per-{@link Style} loop
	 * would have left it: a later {@link #muteStyle(Collection)} overlay, which continues this
	 * same counter, is still guaranteed to always outrank every declaration in this base,
	 * whatever their relative specificity would otherwise have been.
	 */
	public static StyleBuilder forBaseStyleText(BlocLines lines) throws StyleParsingException {
		final StyleBuilder result = new StyleBuilder();
		final RawStyleSheet raw = RawStyleParser.parse(lines);
		result.index = StyleIndex.forBase(MergedStyleSheet.build(raw, result));
		return result;
	}

	/** Whether this builder has no style at all loaded or muted into it. */
	public boolean isEmpty() {
		return index.isEmpty();
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
			if (style.getQuery().equals(signature))
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

		return index.getMergedStyleSpecial(query, ancestorRank);
	}

}
