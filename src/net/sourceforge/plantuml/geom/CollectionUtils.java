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
package net.sourceforge.plantuml.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {

	public static <E> Collection<List<E>> selectUpTo(List<E> original, int nb) {
		final List<List<E>> result = new ArrayList<List<E>>();
		for (int i = 1; i <= nb; i++) {
			result.addAll(selectExactly(original, i));
		}
		return Collections.unmodifiableList(result);
	}

	public static <E> Collection<List<E>> selectExactly(List<E> original, int nb) {
		if (nb < 0) {
			throw new IllegalArgumentException();
		}
		if (nb == 0) {
			return Collections.emptyList();
		}
		if (nb == 1) {
			final List<List<E>> result = new ArrayList<List<E>>();
			for (E element : original) {
				result.add(Collections.singletonList(element));
			}
			return result;

		}
		if (nb > original.size()) {
			return Collections.emptyList();
		}
		if (nb == original.size()) {
			return Collections.singletonList(original);
		}
		final List<List<E>> result = new ArrayList<List<E>>();

		for (List<E> subList : selectExactly(original.subList(1, original.size()), nb - 1)) {
			final List<E> newList = new ArrayList<E>();
			newList.add(original.get(0));
			newList.addAll(subList);
			result.add(Collections.unmodifiableList(newList));
		}
		result.addAll(selectExactly(original.subList(1, original.size()), nb));

		return Collections.unmodifiableList(result);
	}
}
