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

import java.util.ArrayList;
import java.util.List;

public class Capture {

	public static final Capture EMPTY = new Capture(SafeList.<CaptureEntry>createEmpty());
	private final SafeList<CaptureEntry> entries;

	private Capture(SafeList<CaptureEntry> list) {
		this.entries = list;
	}

	@Override
	public String toString() {
		return entries.toString();
	}

	public Capture withEntry(String key, String value) {
		return new Capture(entries.add(new CaptureEntry(key, value)));
	}

	public Capture merge(Capture other) {
		if (other == EMPTY)
			return this;

		if (this == EMPTY)
			return other;

		return new Capture(this.entries.addAll(other.entries));
	}

	public Capture withPrefixedKeys(String prefix) {
		return new Capture(this.entries.mapped(entry -> entry.withPrefixedKey(prefix)));
	}

	public List<String> findValuesByKey(String key) {
		final List<String> result = new ArrayList<>();
		for (CaptureEntry entry : this.entries)
			if (entry.getKey().equals(key))
				result.add(entry.getValue());
		return result;
	}

	public List<String> findFirstValuesByKeyPrefix(String keyPrefix) {
		for (CaptureEntry entry : this.entries)
			if (entry.getKey().startsWith(keyPrefix))
				return findValuesByKey(entry.getKey());
		return null;
	}
}
