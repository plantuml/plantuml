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
package net.sourceforge.plantuml.yaml.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Monomorph {

	private MonomorphType type;

	// Only one of this 3 cannot be null.
	private String value;
	private List<Monomorph> list;
	private Map<String, Monomorph> map;

	public Monomorph() {
		// At creation, the Monomorph is undetermined
		this.type = MonomorphType.UNDETERMINATE;
	}

	@Override
	public String toString() {
		switch (type) {
		case UNDETERMINATE:
			return "???";
		case SCALAR:
			return getValue();
		case LIST:
			return list.toString();
		case MAP:
			return map.toString();
		}
		return super.toString();
	}

	public void setValue(String value) {
		if (this.type == MonomorphType.LIST || this.type == MonomorphType.MAP)
			throw new IllegalStateException();
		this.value = value;
		this.list = null;
		this.map = null;
		this.type = MonomorphType.SCALAR;
	}

	public void addInList(Monomorph elementToBeAdded) {
		if (this.type == MonomorphType.UNDETERMINATE) {
			this.list = new ArrayList<>();
			this.type = MonomorphType.LIST;
		} else if (this.type != MonomorphType.LIST) {
			throw new IllegalStateException("Monomorph is not of type LIST.");
		}
		this.list.add(elementToBeAdded);
	}

	public void putInMap(String key, Monomorph value) {
		if (this.type == MonomorphType.UNDETERMINATE) {
			this.map = new LinkedHashMap<>();
			this.type = MonomorphType.MAP;
		} else if (this.type != MonomorphType.MAP) {
			throw new IllegalStateException("Monomorph is not of type MAP.");
		}
		this.map.put(key, value);
	}

	public MonomorphType getType() {
		return type;
	}

	public String getValue() {
		if (this.type != MonomorphType.SCALAR)
			throw new IllegalStateException("Not a scalar value.");

		return this.value;
	}

	public Monomorph getElementAt(int index) {
		if (this.type != MonomorphType.LIST)
			throw new IllegalStateException("Not a list.");

		return this.list.get(index);
	}

	public Monomorph getMapValue(String key) {
		if (this.type != MonomorphType.MAP)
			throw new IllegalStateException("Not a map.");

		return this.map.get(key);
	}

	public Set<String> keys() {
		if (this.type != MonomorphType.MAP)
			throw new IllegalStateException("Not a map.");

		return this.map.keySet();
	}

	public int size() {
		if (this.type == MonomorphType.LIST)
			return this.list.size();
		else if (this.type == MonomorphType.MAP)
			return this.map.size();

		throw new IllegalStateException("Not a container type.");
	}

	public static Monomorph scalar(String value) {
		final Monomorph result = new Monomorph();
		result.setValue(value);
		return result;
	}

	public static Monomorph list(List<String> input) {
		final Monomorph result = new Monomorph();
		// If input is an empty list!
		result.list = new ArrayList<>();
		result.type = MonomorphType.LIST;

		for (String s : input)
			result.addInList(Monomorph.scalar(s));

		return result;
	}

}
