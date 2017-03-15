/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.util.List;
import java.util.Map;

class Election {

	private final Map<String, Member> all = new HashMap<String, Member>();

	public void addCandidat(String display, Member candidat) {
		all.put(display, candidat);

	}

	private Member getCandidat(String shortName) {
		List<Member> list = getAllCandidatContains(shortName);
		if (list.size() == 1) {
			return list.get(0);
		}
		list = getAllCandidatContainsStrict(shortName);
		if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	private List<Member> getAllCandidatContains(String shortName) {
		final List<Member> result = new ArrayList<Member>();
		for (Map.Entry<String, Member> ent : all.entrySet()) {
			if (ent.getKey().contains(shortName)) {
				result.add(ent.getValue());
			}
		}
		return result;
	}

	private List<Member> getAllCandidatContainsStrict(String shortName) {
		final List<Member> result = new ArrayList<Member>();
		for (Map.Entry<String, Member> ent : all.entrySet()) {
			final String key = ent.getKey();
			if (key.matches(".*\\b" + shortName + "\\b.*")) {
				result.add(ent.getValue());
			}
		}
		return result;
	}

	public Map<Member, String> getAllElected(Collection<String> shortNames) {
		final Map<Member, String> memberWithPort = new HashMap<Member, String>();
		for (String shortName : shortNames) {
			final Member m = getCandidat(shortName);
			if (m != null) {
				memberWithPort.put(m, shortName);
			}
		}
		return Collections.unmodifiableMap(memberWithPort);
	}

}
