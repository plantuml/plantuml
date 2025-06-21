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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.utils.Log;

public class StyleBuilder implements AutomaticCounter {
	// ::remove file when __HAXE__

	private final StyleStorage storage = new StyleStorage();
	private final Set<StyleSignatureBasic> printedForLog;
	private int counter;

	public void printMe() {
		storage.printMe();
	}

	private StyleBuilder(Set<StyleSignatureBasic> printedForLog) {
		this.printedForLog = new LinkedHashSet<>();
	}

	public StyleBuilder() {
		this(new LinkedHashSet<StyleSignatureBasic>());
	}

	public StyleBuilder cloneMe() {
		final StyleBuilder result = new StyleBuilder();
		result.storage.putAll(storage);
		result.counter = this.counter;
		return result;

	}

	public Style createStyleStereotype(String name) {
		if (name.contains(StyleSignatureBasic.STAR))
			throw new IllegalArgumentException();

		name = name.toLowerCase();
		final StyleSignatureBasic signature = StyleSignatureBasic.createStereotype(name);
		final Style result = storage.get(signature);
		if (result == null)
			return new Style(signature, new EnumMap<PName, Value>(PName.class));

		return result;
	}

	public StyleBuilder muteStyle(Collection<Style> modifiedStyles) {
		final StyleBuilder result = new StyleBuilder(this.printedForLog);
		result.counter = this.counter;
		result.storage.putAll(storage);

		for (Style modifiedStyle : modifiedStyles) {
			final StyleSignatureBasic signature = modifiedStyle.getSignature();

			final Style orig = result.storage.get(signature);
			if (orig == null) {
				result.storage.put(modifiedStyle);
			} else {
				final Style tmp = orig.mergeWith(modifiedStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);
				result.storage.put(tmp);
			}

		}
		return result;
	}

	public void loadInternal(StyleSignatureBasic signature, Style newStyle) {
		if (signature.isStarred())
			throw new IllegalArgumentException();

		final Style orig = this.storage.get(signature);
		if (orig == null) {
			this.storage.put(newStyle);
		} else {
			final Style tmp = orig.mergeWith(newStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);
			this.storage.put(tmp);
		}
	}

	public int getNextInt() {
		return ++counter;
	}

	private final Map<StyleSignatureBasic, Style> mergedStyleCache = new ConcurrentHashMap<>();

	public Style getMergedStyle(StyleSignatureBasic signature) {
		// return computeMergedStyle(signature);
		return mergedStyleCache.computeIfAbsent(signature, sig -> computeMergedStyle(sig));
	}

	private Style computeMergedStyle(StyleSignatureBasic signature) {
		boolean added = this.printedForLog.add(signature);
		if (added)
			Log.info(() -> "Using style " + signature);

		return storage.computeMergedStyle(signature);
	}

	public Style getMergedStyleSpecial(StyleSignatureBasic signature, int deltaPriority) {
		boolean added = this.printedForLog.add(signature);
		if (added)
			Log.info(() -> "Using style " + signature);

		Style mergedStyle = null;
		for (Style style : storage.getStyles()) {
			final StyleSignatureBasic key = style.getSignature();
			if (key.matchAll(signature) == false)
				continue;

			Style tmp = style;
			if (key.isStarred())
				tmp = tmp.deltaPriority(deltaPriority);

			if (mergedStyle == null)
				mergedStyle = tmp;
			else
				mergedStyle = mergedStyle.mergeWith(tmp, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return mergedStyle;
	}

}
