/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SkinParam;

public class StyleBuilder implements AutomaticCounter {

	private final Map<StyleSignature, Style> styles = new LinkedHashMap<StyleSignature, Style>();
	private final Set<StyleSignature> printedForLog;
	private final SkinParam skinParam;
	private int counter;

	private StyleBuilder(SkinParam skinParam, Set<StyleSignature> printedForLog) {
		this.skinParam = skinParam;
		this.printedForLog = new LinkedHashSet<StyleSignature>();
	}

	public StyleBuilder(SkinParam skinParam) {
		this(skinParam, new LinkedHashSet<StyleSignature>());
	}

	public final SkinParam getSkinParam() {
		return skinParam;
	}

	// public Collection<StyleSignature> getAllStyleSignatures() {
	// return Collections.unmodifiableCollection(styles.keySet());
	// }

	public Style createStyle(String name) {
		name = name.toLowerCase();
		final StyleSignature signature = new StyleSignature(name);
		final Style result = styles.get(signature);
		if (result == null) {
			return new Style(signature, new EnumMap<PName, Value>(PName.class));
		}
		return result;
	}

	public StyleBuilder muteStyle(Style modifiedStyle) {
		final Map<StyleSignature, Style> copy = new LinkedHashMap<StyleSignature, Style>(styles);
		final StyleSignature signature = modifiedStyle.getSignature();
		final Style orig = copy.get(signature);
		if (orig == null) {
			copy.put(signature, modifiedStyle);
		} else {
			final Style newStyle = orig.mergeWith(modifiedStyle);
			copy.put(signature, newStyle);
		}
		final StyleBuilder result = new StyleBuilder(skinParam, this.printedForLog);
		result.styles.putAll(copy);
		result.counter = this.counter;
		return result;
	}

	public void put(StyleSignature styleName, Style newStyle) {
		this.styles.put(styleName, newStyle);
	}

	public int getNextInt() {
		return ++counter;
	}

	public Style getMergedStyle(StyleSignature signature) {
		boolean added = this.printedForLog.add(signature);
		if (added) {
			Log.info("Using style " + signature);
		}
		Style result = null;
		for (Entry<StyleSignature, Style> ent : styles.entrySet()) {
			final StyleSignature key = ent.getKey();
			if (key.matchAll(signature) == false) {
				continue;
			}
			if (result == null) {
				result = ent.getValue();
			} else {
				result = result.mergeWith(ent.getValue());
			}

		}
		return result;
	}

}