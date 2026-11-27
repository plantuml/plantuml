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
package net.sourceforge.plantuml.ngdot.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * An ordered set of DOT attributes ({@code name=value}).
 * <p>
 * Attribute names are case-sensitive in DOT ({@code URL} and {@code url} are
 * two different attributes), so they are stored as written. Setting an
 * attribute twice keeps its first position and its last value.
 */
public final class DotAttributes {

	private final Map<String, DotValue> map = new LinkedHashMap<String, DotValue>();

	public DotAttributes() {
	}

	public DotAttributes(DotAttributes other) {
		map.putAll(other.map);
	}

	public void put(String name, DotValue value) {
		map.put(name, value);
	}

	public void putAll(DotAttributes other) {
		map.putAll(other.map);
	}

	public DotValue get(String name) {
		return map.get(name);
	}

	/**
	 * Returns the text of an attribute, or {@code null} if it is not set.
	 */
	public String getString(String name) {
		final DotValue value = map.get(name);
		if (value == null)
			return null;
		return value.getText();
	}

	public String getString(String name, String defaultValue) {
		final String result = getString(name);
		if (result == null)
			return defaultValue;
		return result;
	}

	public boolean contains(String name) {
		return map.containsKey(name);
	}

	public Set<String> names() {
		return Collections.unmodifiableSet(map.keySet());
	}

	public Map<String, DotValue> asMap() {
		return Collections.unmodifiableMap(map);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public int size() {
		return map.size();
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
