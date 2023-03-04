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

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.utils.Log;

public class StyleBuilder implements AutomaticCounter {
    // ::remove file when __HAXE__

	private final Map<StyleSignatureBasic, Style> stylesMap = new LinkedHashMap<StyleSignatureBasic, Style>();
	private final Set<StyleSignatureBasic> printedForLog;
	private final SkinParam skinParam;
	private int counter;

	public void printMe() {
		for (Entry<StyleSignatureBasic, Style> ent : stylesMap.entrySet())
			ent.getValue().printMe();

	}

	private StyleBuilder(SkinParam skinParam, Set<StyleSignatureBasic> printedForLog) {
		this.skinParam = skinParam;
		this.printedForLog = new LinkedHashSet<>();
	}

	public StyleBuilder(SkinParam skinParam) {
		this(skinParam, new LinkedHashSet<StyleSignatureBasic>());
	}

	public final SkinParam getSkinParam() {
		return skinParam;
	}

	public Style createStyle(String name) {
		if (name.contains("*"))
			throw new IllegalArgumentException();

		name = name.toLowerCase();
		final StyleSignatureBasic signature = new StyleSignatureBasic(name);
		final Style result = stylesMap.get(signature);
		if (result == null)
			return new Style(signature, new EnumMap<PName, Value>(PName.class));

		return result;
	}

	public StyleBuilder muteStyle(Style modifiedStyle) {
		final Map<StyleSignatureBasic, Style> copy = new LinkedHashMap<StyleSignatureBasic, Style>(stylesMap);
		final StyleSignatureBasic signature = modifiedStyle.getSignature();
		final Style orig = copy.get(signature);
		if (orig == null) {
			copy.put(signature, modifiedStyle);
		} else {
			final Style tmp = orig.mergeWith(modifiedStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);
			copy.put(signature, tmp);
		}
		final StyleBuilder result = new StyleBuilder(skinParam, this.printedForLog);
		result.stylesMap.putAll(copy);
		result.counter = this.counter;
		return result;
	}

	public void loadInternal(StyleSignatureBasic signature, Style newStyle) {
		if (signature.isStarred())
			throw new IllegalArgumentException();

		final Style orig = this.stylesMap.get(signature);
		if (orig == null) {
			this.stylesMap.put(signature, newStyle);
		} else {
			final Style tmp = orig.mergeWith(newStyle, MergeStrategy.OVERWRITE_EXISTING_VALUE);
			this.stylesMap.put(signature, tmp);
		}
	}

	public int getNextInt() {
		return ++counter;
	}

	public Style getMergedStyle(StyleSignatureBasic signature) {
		boolean added = this.printedForLog.add(signature);
		if (added)
			Log.info("Using style " + signature);

		Style result = null;
		for (Entry<StyleSignatureBasic, Style> ent : stylesMap.entrySet()) {
			final StyleSignatureBasic key = ent.getKey();
			if (key.matchAll(signature) == false)
				continue;

			if (result == null)
				result = ent.getValue();
			else
				result = result.mergeWith(ent.getValue(), MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return result;
	}

	public Style getMergedStyleSpecial(StyleSignatureBasic signature, int deltaPriority) {
		boolean added = this.printedForLog.add(signature);
		if (added)
			Log.info("Using style " + signature);

		Style result = null;
		for (Entry<StyleSignatureBasic, Style> ent : stylesMap.entrySet()) {
			final StyleSignatureBasic key = ent.getKey();
			if (key.matchAll(signature) == false)
				continue;

			Style tmp = ent.getValue();
			if (key.isStarred())
				tmp = tmp.deltaPriority(deltaPriority);

			if (result == null)
				result = tmp;
			else
				result = result.mergeWith(tmp, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return result;
	}

}
