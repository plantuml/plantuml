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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.plantuml.ubrex.UMatcher;

public class RegexResult {

	private final Map<String, RegexPartialMatch> data;
	private final UMatcher matcher;

	public RegexResult(Map<String, RegexPartialMatch> data) {
		this.data = Collections.unmodifiableMap(data);
		this.matcher = null;
	}

	public RegexResult(UMatcher matcher) {
		this.data = Collections.emptyMap();
		this.matcher = matcher;
	}

	@Override
	public String toString() {
		if (matcher != null)
			return matcher.toString();
		return data.toString();
	}

	public RegexPartialMatch get(String key) {
		if (matcher != null)
			throw new UnsupportedOperationException();
		return data.get(key);
	}

	public String get(String key, int num) {
		if (matcher != null) {
			final List<String> list = matcher.getCapture(key);
			if (list == null || list.size() == 0)
				return null;
			return list.get(num);

		}
		final RegexPartialMatch reg = data.get(key);
		if (reg == null)
			return null;

		return reg.get(num);
	}

	public String getLazzy(String key, int num) {
		if (matcher != null) {
			for (String candidate : matcher.getKeysToBeRefactored()) {
				if (candidate.startsWith(key) == false)
					continue;
				final List<String> list = matcher.getCapture(candidate);
				if (list == null || list.size() == 0)
					return null;
				return list.get(num);

			}
			return null;
		}

		for (Map.Entry<String, RegexPartialMatch> ent : data.entrySet()) {
			if (ent.getKey().startsWith(key) == false)
				continue;

			final RegexPartialMatch match = ent.getValue();
			if (num >= match.size())
				continue;

			if (match.get(num) != null)
				return ent.getValue().get(num);

		}
		return null;
	}

	public int size() {
		if (matcher != null)
			throw new UnsupportedOperationException();
		return data.size();
	}

}
