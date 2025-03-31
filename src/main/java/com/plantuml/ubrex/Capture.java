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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Capture {

	public static final Capture EMPTY = new Capture();

	private Capture() {

	}

	private List<Map.Entry<String, String>> getList() {
		if (list == null)
			list = new ArrayList<>();

		return list;
	}

	public List<Map.Entry<String, String>> getCaptures() {
		if (list == null)
			return Collections.emptyList();
		return Collections.unmodifiableList(list);
	}

	@Override
	public String toString() {
		if (list == null)
			return "[]";
		return getCaptures().toString();
	}

	public Capture withEntry(String key, String value) {
		final Capture copy = new Capture();
		if (this.list != null)
			copy.getList().addAll(this.list);

		copy.getList().add(new AbstractMap.SimpleEntry<>(key, value));
		return copy;
	}

	public Capture merge(Capture other) {
		if (other == EMPTY)
			return this;

		if (this == EMPTY)
			return other;

		final Capture copy = new Capture();
		if (this.list != null)
			copy.getList().addAll(this.list);

		if (other.list != null)
			copy.getList().addAll(other.list);

		return copy;
	}

	public Capture withPrefixedKeys(String prefix) {
		if (this.list == null)
			return this;

		final Capture copy = new Capture();
		for (Map.Entry<String, String> entry : this.list)
			copy.getList().add(new AbstractMap.SimpleEntry<>(prefix + "/" + entry.getKey(), entry.getValue()));

		return copy;
	}

	private List<Map.Entry<String, String>> list;


	public List<String> findValuesByKey(String key) {
		if (this.list == null)
			return Collections.emptyList();

		return this.list.stream() //
				.filter(e -> e.getKey().equals(key)) //
				.map(Map.Entry::getValue) //
				.collect(Collectors.toList());

	}

	
	public List<String> getKeysToBeRefactored() {
		return this.list.stream() //
				.map(Map.Entry::getKey) //
				.collect(Collectors.toList());
	}
}
