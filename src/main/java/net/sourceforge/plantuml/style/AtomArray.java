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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An immutable, sorted, duplicate-free collection of {@link StyleAtom}, backed by a plain array
 * instead of a {@link java.util.TreeSet} -- a candidate replacement for the
 * {@code SortedSet<StyleAtom>} that {@link StyleQuery} currently keeps as {@code atoms} (not yet
 * wired in there: this class stands on its own, validated by its own unit tests, before any
 * caller is switched over).
 *
 * <p>{@code StyleQuery.atoms} is always tiny (a handful of elements: {@code root}, {@code
 * element}, a diagram-type {@link SName}, maybe a sub-element and a stereotype or two) and, once
 * built, is never mutated again -- every {@code StyleQuery} "with"/"add" method builds a brand
 * new set rather than touching the existing one. A {@link java.util.TreeSet} is the wrong tool
 * for that shape: each element costs a red-black-tree node (parent/left/right/color fields), and
 * a copy-then-add rebuilds that whole node chain, when a handful of {@link StyleAtom} references
 * would fit in a single small array instead. This class trades the tree for exactly that:
 * {@link #plus} does a binary-search insert with {@code System.arraycopy}, {@link #plusAll} does
 * a single linear merge of two already-sorted arrays instead of inserting one element at a time,
 * and {@link #equals}/{@link #hashCode} reduce to {@link Arrays#equals(Object[], Object[])}/
 * {@link Arrays#hashCode(Object[])} instead of a set-to-set comparison.
 *
 * <p>The sort order is {@link StyleAtom}'s natural order (see {@link StyleAtom#compareTo}), the
 * same order {@code StyleQuery.atoms} relies on today, so two {@code AtomArray}s built from the
 * same conceptual set of atoms -- regardless of the order they were added in -- always end up
 * holding an identical backing array (which is exactly what makes {@link #equals}/
 * {@link #hashCode} safe to compute from the array alone). Adding an atom already present is a
 * silent no-op, exactly like {@code TreeSet.add} returning {@code false} -- callers such as
 * {@code StyleQuery.withStereotype} rely on that to fold in a stereotype whether or not it is
 * already there.
 */
public final class AtomArray implements Iterable<StyleAtom> {

	private static final StyleAtom[] NO_ATOMS = new StyleAtom[0];
	private static final AtomArray EMPTY = new AtomArray(NO_ATOMS);

	private final StyleAtom[] data;

	private AtomArray(StyleAtom[] data) {
		this.data = data;
	}

	/** The empty {@code AtomArray} -- mirrors {@code StyleQuery.empty()}'s starting point. */
	public static AtomArray empty() {
		return EMPTY;
	}

	/**
	 * Builds an {@code AtomArray} holding every one of {@code atoms}, sorted and deduplicated.
	 * {@code atoms} itself need not be sorted or duplicate-free, and is never modified.
	 */
	public static AtomArray of(StyleAtom... atoms) {
		if (atoms.length == 0)
			return EMPTY;

		final StyleAtom[] sorted = Arrays.copyOf(atoms, atoms.length);
		for (StyleAtom atom : sorted)
			if (atom == null)
				throw new IllegalArgumentException("atom");
		Arrays.sort(sorted);

		int size = 0;
		for (int i = 0; i < sorted.length; i++)
			if (size == 0 || sorted[size - 1].compareTo(sorted[i]) != 0)
				sorted[size++] = sorted[i];

		return new AtomArray(size == sorted.length ? sorted : Arrays.copyOf(sorted, size));
	}

	public AtomArray plus(StyleAtom atom) {
		final int found = Arrays.binarySearch(data, atom);
		if (found >= 0)
			return this;

		final int insertAt = -found - 1;
		final StyleAtom[] result = new StyleAtom[data.length + 1];
		System.arraycopy(data, 0, result, 0, insertAt);
		result[insertAt] = atom;
		System.arraycopy(data, insertAt, result, insertAt + 1, data.length - insertAt);
		return new AtomArray(result);
	}

	public AtomArray plus(StyleAtom atom1, StyleAtom atom2) {
		final StyleAtom[] newAtoms = atom1.compareTo(atom2) < 0 ? new StyleAtom[] { atom1, atom2 }
				: new StyleAtom[] { atom2, atom1 };
		return plusSorted(newAtoms);
	}

	public AtomArray plus(StyleAtom atom1, StyleAtom atom2, StyleAtom atom3) {
		final StyleAtom[] sorted = { atom1, atom2, atom3 };
		Arrays.sort(sorted);

		int size = 1;
		for (int i = 1; i < sorted.length; i++)
			if (sorted[size - 1].compareTo(sorted[i]) != 0)
				sorted[size++] = sorted[i];

		return plusSorted(size == sorted.length ? sorted : Arrays.copyOf(sorted, size));
	}

	public AtomArray plusAll(AtomArray other) {
		if (other == null || other.isEmpty())
			return this;
		if (this.isEmpty())
			return other;

		return plusSorted(other.data);
	}

	private AtomArray plusSorted(StyleAtom[] newAtoms) {
		final StyleAtom[] merged = new StyleAtom[data.length + newAtoms.length];
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < data.length && j < newAtoms.length) {
			final int cmp = data[i].compareTo(newAtoms[j]);
			if (cmp < 0)
				merged[k++] = data[i++];
			else if (cmp > 0)
				merged[k++] = newAtoms[j++];
			else {
				merged[k++] = data[i++];
				j++;
			}
		}
		while (i < data.length)
			merged[k++] = data[i++];
		while (j < newAtoms.length)
			merged[k++] = newAtoms[j++];

		return new AtomArray(k == merged.length ? merged : Arrays.copyOf(merged, k));
	}

	public boolean contains(StyleAtom atom) {
		return atom != null && Arrays.binarySearch(data, atom) >= 0;
	}

	public int size() {
		return data.length;
	}

	public boolean isEmpty() {
		return data.length == 0;
	}

	/** The atom at {@code index}, in ascending order -- zero-allocation random access. */
	public StyleAtom get(int index) {
		return data[index];
	}

	@Override
	public Iterator<StyleAtom> iterator() {
		return new Iterator<StyleAtom>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < data.length;
			}

			@Override
			public StyleAtom next() {
				if (hasNext() == false)
					throw new NoSuchElementException();
				return data[index++];
			}
		};
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AtomArray == false)
			return false;
		return Arrays.equals(this.data, ((AtomArray) obj).data);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
	public String toString() {
		return Arrays.toString(data);
	}

}
