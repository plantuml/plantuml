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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class StyleStorage {
	// ::remove file when __HAXE__

	private final Map<StyleSignatureBasic, Style> legacy = new LinkedHashMap<StyleSignatureBasic, Style>();

	private final Map<StyleKey, Style> plain = new LinkedHashMap<StyleKey, Style>();

	public void printMe() {
		for (Entry<StyleSignatureBasic, Style> ent : legacy.entrySet())
			ent.getValue().printMe();

	}

	public void putAll(StyleStorage other) {
		legacy.putAll(other.legacy);
		plain.putAll(other.plain);

	}

	public Style get(StyleSignatureBasic signature) {
//		if (signature.getStereotypes().size() > 1)
//			System.err.println("get " + signature);

		if (signature.getStereotypes().size() == 0)
			return plain.get(signature.getKey());

		return legacy.get(signature);
	}

	public void put(Style modifiedStyle) {
//		if (signature.getStereotypes().size() > 1)
//			System.err.println("put " + signature);
		
		final StyleSignatureBasic signature = modifiedStyle.getSignature();

		if (signature.getStereotypes().size() == 0)
			plain.put(signature.getKey(), modifiedStyle);
		else
			legacy.put(signature, modifiedStyle);

	}

	public Collection<Style> getStyles() {
		return new AbstractCollection<Style>() {
			@Override
			public Iterator<Style> iterator() {
				return new ConcatIterator<>(legacy.values().iterator(), plain.values().iterator());
			}

			@Override
			public int size() {
				return legacy.size() + plain.size();
			}
		};
	}
	
	public Style computeMergedStyle(StyleSignatureBasic signature) {
		Style mergedStyle = null;
		for (Style style : getStyles()) {
			final StyleSignatureBasic key = style.getSignature();
			if (key.matchAll(signature) == false)
				continue;

			if (mergedStyle == null)
				mergedStyle = style;
			else
				mergedStyle = mergedStyle.mergeWith(style, MergeStrategy.OVERWRITE_EXISTING_VALUE);

		}
		return mergedStyle;
	}



}

class ConcatIterator<T> implements Iterator<T> {
	private final Iterator<T> first;
	private final Iterator<T> second;

	public ConcatIterator(Iterator<T> first, Iterator<T> second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean hasNext() {
		return first.hasNext() || second.hasNext();
	}

	@Override
	public T next() {
		if (first.hasNext())
			return first.next();
		else if (second.hasNext())
			return second.next();
		else
			throw new NoSuchElementException();

	}
}
