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
package net.sourceforge.plantuml.regex;

import java.util.Arrays;
import java.util.List;

import com.plantuml.ubrex.UMatcher;
import net.sourceforge.plantuml.teavm.TeaVM;

public class RegexResult {

	// The named groups of one match, in the order fillPartialMatch added them. A match has at
	// most a few dozen of them (28 for the largest regex of the test suite), read a handful of
	// times by one command: two arrays scanned linearly cost less than a HashMap, its table and
	// one node per entry, built for every line recognized.
	private String[] names;
	private RegexPartialMatch[] values;
	private int size;
	private final UMatcher matcher;

	public UMatcher getUMatcher() {
		return matcher;
	}

	// Filled by RegexComposed.matcher, through fillPartialMatch.
	RegexResult() {
		this.names = new String[16];
		this.values = new RegexPartialMatch[16];
		this.matcher = null;
	}

	public RegexResult(UMatcher matcher) {
		this.names = new String[0];
		this.values = new RegexPartialMatch[0];
		this.matcher = matcher;
	}

	void add(String name, RegexPartialMatch value) {
		// A name appears once per regex: nothing in the test suite ever adds it twice. The
		// HashMap used before would silently have kept the last one.
		if (TeaVM.a())
			assert indexOf(name) == -1 : "Duplicate group name " + name;
		if (size == names.length) {
			names = Arrays.copyOf(names, size * 2);
			values = Arrays.copyOf(values, size * 2);
		}
		names[size] = name;
		values[size] = value;
		size++;
	}

	private int indexOf(String key) {
		for (int i = 0; i < size; i++)
			if (names[i].equals(key))
				return i;
		return -1;
	}

	@Override
	public String toString() {
		if (matcher != null)
			return matcher.toString();
		final StringBuilder sb = new StringBuilder("{");
		for (int i = 0; i < size; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(names[i]).append('=').append(values[i]);
		}
		return sb.append('}').toString();
	}

	public RegexPartialMatch get(String key) {
		if (matcher != null)
			throw new UnsupportedOperationException();
		final int idx = indexOf(key);
		return idx == -1 ? null : values[idx];
	}

	public String get(String key, int num) {
		if (matcher != null) {
			final List<String> list = matcher.findValuesByKey(key);
			if (list == null || list.size() == 0)
				return null;
			return list.get(num);

		}
		final RegexPartialMatch reg = get(key);
		if (reg == null)
			return null;

		return reg.get(num);
	}

	public String getLazzy(String key, int num) {
		if (matcher != null) {
			final List<String> list = matcher.findFirstValuesByKeyPrefix(key);
			if (list == null || list.size() == 0)
				return null;
			return list.get(num);
		}

		for (int i = 0; i < size; i++) {
			final String value = lazzyValue(i, key, num);
			if (value != null) {
				// The first one found is the answer only because there is no other: the
				// alternatives sharing a prefix (DISPLAY1, DISPLAY2...) belong to different
				// branches of a RegexOr, so at most one of them has matched. Checked over the
				// whole test suite. The HashMap used before had no defined order to fall back on.
				if (TeaVM.a())
					assert lazzyUnique(i, key, num) : "Several groups match " + key + " in " + this;
				return value;
			}
		}
		return null;
	}

	private String lazzyValue(int i, String key, int num) {
		if (names[i].startsWith(key) == false)
			return null;

		final RegexPartialMatch match = values[i];
		if (num >= match.size())
			return null;

		return match.get(num);
	}

	private boolean lazzyUnique(int found, String key, int num) {
		for (int i = found + 1; i < size; i++)
			if (lazzyValue(i, key, num) != null)
				return false;
		return true;
	}

	public int size() {
		if (matcher != null)
			throw new UnsupportedOperationException();
		return size;
	}

}
