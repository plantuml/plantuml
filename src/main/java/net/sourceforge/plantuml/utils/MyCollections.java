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
 * Original Author:  Shunli Han
 *
 *
 */
package net.sourceforge.plantuml.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import net.sourceforge.plantuml.teavm.TeaVM;

public class MyCollections {

	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> map) {
		if (TeaVM.isTeaVM())
			return (Map<K, V>) map;
		return Collections.unmodifiableMap(map);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> unmodifiableList(List<? extends T> list) {
		if (TeaVM.isTeaVM())
			return (List<T>) list;
		return Collections.unmodifiableList(list);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> collection) {
		if (TeaVM.isTeaVM())
			return (Collection<T>) collection;
		return Collections.unmodifiableCollection(collection);
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> unmodifiableSet(Set<? extends T> set) {
		if (TeaVM.isTeaVM())
			return (Set<T>) set;
		return Collections.unmodifiableSet(set);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> map) {
		if (TeaVM.isTeaVM())
			return (SortedMap<K, V>) map;
		return Collections.unmodifiableSortedMap(map);
	}

	public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<T> set) {
		if (TeaVM.isTeaVM())
			return set;
		return Collections.unmodifiableSortedSet(set);
	}

}
