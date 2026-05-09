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
 */
package com.plantuml.ubrex;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SafeList<E> implements Iterable<E> {

	private static final SafeList<?> EMPTY = new SafeList<Object>(0, null, null, null);

	// Each node holds either a single element (data1) or a block of elements
	// (data2).
	// Exactly one of them is non-null, except for the empty node where both are
	// null.
	// 'size' is the cumulative size of the whole chain, not the local block size.
	private final int size;
	private final SafeList<E> previous;
	private final E data1;
	private final E[] data2;
	private E[] cachedArray;

	private SafeList(int size, SafeList<E> previous, E data1, E[] data2) {
		this.size = size;
		this.previous = previous;
		this.data1 = data1;
		this.data2 = data2;
	}

	public int size() {
		return size;
	}

	@SuppressWarnings("unchecked")
	public static <E> SafeList<E> createEmpty() {
		return (SafeList<E>) EMPTY;
	}

	public SafeList<E> add(E data) {
		return new SafeList<E>(this.size + 1, this, data, null);
	}

	@SuppressWarnings("unchecked")
	public SafeList<E> mapped(Function<E, E> fn) {
		if (size == 0)
			return this;

		final E[] source = getCachedArray();
		final E[] transformed = (E[]) new Object[size];
		for (int i = 0; i < size; i++)
			transformed[i] = fn.apply(source[i]);

		final SafeList<E> empty = createEmpty();
		final SafeList<E> result = new SafeList<E>(size, empty, null, transformed);
		// The block IS the full materialized list, so we can use it as the cache
		// directly.
		result.cachedArray = transformed;
		return result;
	}

	private int blockSize() {
		if (data1 != null)
			return 1;
		if (data2 != null)
			return data2.length;
		return 0;
	}

	private void copyBlockInto(E[] dest, int destEnd) {
		// Copies this node's block into dest, ending at index destEnd (exclusive).
		if (data1 != null) {
			dest[destEnd - 1] = data1;
		} else if (data2 != null) {
			System.arraycopy(data2, 0, dest, destEnd - data2.length, data2.length);
		}
	}

	private List<E> toList() {
		if (size == 0)
			return Collections.emptyList();

		return Arrays.asList(getCachedArray());
	}

	@SuppressWarnings("unchecked")
	private E[] getCachedArray() {
		if (cachedArray != null)
			return cachedArray;

		this.cachedArray = (E[]) new Object[size];
		SafeList<E> current = this;
		int end = size;
		while (current != null && current.size != 0) {
			current.copyBlockInto(cachedArray, end);
			end -= current.blockSize();
			current = current.previous;
		}

		return cachedArray;
	}

	@Override
	public Iterator<E> iterator() {
		return toList().iterator();
	}

	@Override
	public String toString() {
		return toList().toString();
	}

	private E singletonElement() {
		if (data1 != null)
			return data1;
		return data2[0];
	}

	public SafeList<E> addAll(SafeList<E> other) {
		if (other.size == 0)
			return this;
		if (this.size == 0)
			return other;
		if (other.size == 1)
			return this.add(other.singletonElement());

		final E[] otherArray = other.getCachedArray();
		return new SafeList<E>(this.size + other.size, this, null, otherArray);
	}
}
