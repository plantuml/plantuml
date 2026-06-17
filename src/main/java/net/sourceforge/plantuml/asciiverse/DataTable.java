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
package net.sourceforge.plantuml.asciiverse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataTable<E> {

	private final Map<CellKey, E> cells;
	private final E defaultValue;

	private static class CellKey {
		final int x;
		final int y;

		CellKey(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			CellKey cellKey = (CellKey) o;
			return x == cellKey.x && y == cellKey.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	public DataTable(E defaultValue) {
		this.defaultValue = defaultValue;
		this.cells = new HashMap<>();
	}

	public void set(int x, int y, E value) {
		cells.put(new CellKey(x, y), value);
	}

	public E get(int x, int y) {
		return cells.getOrDefault(new CellKey(x, y), defaultValue);
	}

	public boolean contains(int x, int y) {
		return cells.containsKey(new CellKey(x, y));
	}

}
