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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class Election {

	private final Map<String, CharSequence> all = new HashMap<String, CharSequence>();

	public void addCandidate(String display, CharSequence candidate) {
		all.put(display, candidate);

	}

	private CharSequence getCandidate(String shortName) {
		List<CharSequence> list = getAllCandidateContains(shortName);
		if (list.size() == 1) {
			return list.get(0);
		}
		list = getAllCandidateContainsStrict(shortName);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	private List<CharSequence> getAllCandidateContains(String shortName) {
		final List<CharSequence> result = new ArrayList<>();
		for (Map.Entry<String, CharSequence> ent : all.entrySet()) {
			if (ent.getKey().contains(shortName)) {
				result.add(ent.getValue());
			}
		}
		return result;
	}

	private List<CharSequence> getAllCandidateContainsStrict(String shortName) {
		final List<CharSequence> result = new ArrayList<>();
		for (Map.Entry<String, CharSequence> ent : all.entrySet()) {
			final String key = ent.getKey();
			if (key.matches(".*\\b" + shortName + "\\b.*")) {
				result.add(ent.getValue());
			}
		}
		return result;
	}

	public Map<CharSequence, String> getAllElected(Collection<String> shortNames) {
		final Map<CharSequence, String> memberWithPort = new HashMap<CharSequence, String>();
		for (String shortName : new HashSet<>(shortNames)) {
			final CharSequence m = getCandidate(shortName);
			if (m != null) {
				memberWithPort.put(m, shortName);
				shortNames.remove(shortName);
			}
		}
		return Collections.unmodifiableMap(memberWithPort);
	}

}
