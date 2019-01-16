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
package net.sourceforge.plantuml.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SortedCollectionArrayList<S extends Comparable<S>> implements SortedCollection<S> {

	private final List<S> allAsList = new ArrayList<S>();
	private final Set<S> allAsSet = new HashSet<S>();

	public Iterator<S> iterator() {
		return allAsList.iterator();
	}

	public void add(S newEntry) {
		final int r = Collections.binarySearch(allAsList, newEntry);
		if (r >= 0) {
			allAsList.add(r, newEntry);
		} else {
			allAsList.add(-1 - r, newEntry);
		}
		allAsSet.add(newEntry);
		assert isSorted();
	}

	public int size() {
		assert allAsSet.size() == allAsList.size();
		return allAsList.size();
	}

	List<S> toList() {
		return new ArrayList<S>(allAsList);
	}

	boolean isSorted() {
		S before = null;
		for (S ent : allAsList) {
			if (before != null && ent.compareTo(before) < 0) {
				return false;
			}
			before = ent;
		}
		return true;
	}

	public boolean contains(S entry) {
		return allAsSet.contains(entry);
	}

}
