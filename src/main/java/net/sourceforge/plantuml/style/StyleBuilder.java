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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.utils.Log;

public class StyleBuilder implements AutomaticCounter {
	// ::remove file when __HAXE__

	private final Map<StyleSignatureBasic, Style> stylesMap = new LinkedHashMap<StyleSignatureBasic, Style>();
	private final Set<StyleSignatureBasic> printedForLog;
	private int counter;

	public void printMe() {
		for (Entry<StyleSignatureBasic, Style> ent : stylesMap.entrySet())
			ent.getValue().printMe();

	}

	private StyleBuilder(Set<StyleSignatureBasic> printedForLog) {
		this.printedForLog = new LinkedHashSet<>();
	}

	public StyleBuilder() {
		this(new LinkedHashSet<StyleSignatureBasic>());
	}

	public StyleBuilder cloneMe() {
		final StyleBuilder result = new StyleBuilder();
		result.stylesMap.putAll(this.stylesMap);
		result.counter = this.counter;
		return result;

	}

	public Style createStyleStereotype(String name) {
		if (name.contains(StyleSignatureBasic.STAR))
			throw new IllegalArgumentException();

		name = name.toLowerCase();
		final StyleSignatureBasic signature = StyleSignatureBasic.createStereotype(name);

		return stylesMap.computeIfAbsent(signature, key -> new Style(key, new EnumMap<PName, Value>(PName.class)));

	}

	public StyleBuilder muteStyle(Collection<Style> modifiedStyles) {
		final StyleBuilder result = new StyleBuilder(this.printedForLog);
		result.counter = this.counter;
		result.stylesMap.putAll(stylesMap);

		for (Style modifiedStyle : modifiedStyles) {
			final StyleSignatureBasic signature = modifiedStyle.getSignature();
			result.stylesMap.compute(signature, (key, orig) -> {
				if (orig == null)
					return modifiedStyle;
				else
					return orig.mergeWith(modifiedStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

			});

		}
		return result;
	}

	public void loadInternal(StyleSignatureBasic signature, Style newStyle) {
		if (signature.isStarred())
			throw new IllegalArgumentException();

		this.stylesMap.compute(signature, (key, orig) -> {
			if (orig == null)
				return newStyle;
			else
				return orig.mergeWith(newStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		});
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
		if (this.printedForLog.add(signature)) {
			Log.info(() -> "Using style " + signature);
		}

		Style mergedStyle = null;

		for (Map.Entry<StyleSignatureBasic, Style> entry : stylesMap.entrySet()) {
			final StyleSignatureBasic key = entry.getKey();
			if (key.matchAll(signature) == false)
				continue;

			final Style style = entry.getValue();
			if (mergedStyle == null)
				mergedStyle = style;
			else
				mergedStyle = mergedStyle.mergeWith(style, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return mergedStyle;
	}

	public Style getMergedStyleSpecial(StyleSignatureBasic signature, int deltaPriority) {
		if (this.printedForLog.add(signature)) {
			Log.info(() -> "Using style " + signature);
		}

		Style mergedStyle = null;

		for (Map.Entry<StyleSignatureBasic, Style> entry : stylesMap.entrySet()) {
			final StyleSignatureBasic key = entry.getKey();
			if (key.matchAll(signature) == false)
				continue;

			Style style = entry.getValue();
			if (key.isStarred())
				style = style.deltaPriority(deltaPriority);

			if (mergedStyle == null)
				mergedStyle = style;
			else
				mergedStyle = mergedStyle.mergeWith(style, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return mergedStyle;
	}

}
