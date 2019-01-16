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
package net.sourceforge.plantuml.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.version.Version;

public class HistoricalData {

	final private Preferences prefs;
	private ParsedGenerated current;
	final private List<ParsedGenerated> historical = new ArrayList<ParsedGenerated>();

	HistoricalData(Preferences prefs) {
		this.prefs = prefs;
		try {
			historical.addAll(reload());
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		Collections.sort(historical, getIdComparator());
	}

	public void reset() {
		char currentCode = (char) ('A' + historical.size());
		if (historical.size() > 7) {
			final ParsedGenerated last = historical.get(0);
			final String lastName = last.parsed().getName();
			currentCode = lastName.charAt("histo.".length());
		}
		this.current = ParsedGenerated.loadDated(prefs, "histo." + currentCode);
		this.current.reset();
		final long maxId = getMaxId();
		this.current.parsedDated().setComment(Long.toString(maxId + 1, 36) + "/" + Version.versionString());
	}

	private long getMaxId() {
		long v = 0;
		for (ParsedGenerated histo : historical) {
			v = Math.max(v, histo.getId());
		}
		return v;
	}

	private Comparator<? super ParsedGenerated> getIdComparator() {
		return new Comparator<ParsedGenerated>() {
			public int compare(ParsedGenerated v1, ParsedGenerated v2) {
				final long time1 = v1.getId();
				final long time2 = v2.getId();
				if (time1 > time2) {
					return 1;
				}
				if (time1 < time2) {
					return -1;
				}
				return 0;
			}
		};
	}

	private List<ParsedGenerated> reload() throws BackingStoreException {
		final List<ParsedGenerated> result = new ArrayList<ParsedGenerated>();
		final int length = "histo.".length();
		for (String key : prefs.keys()) {
			if (key.startsWith("histo.") && key.endsWith(".p.saved")) {
				final String name = key.substring(length, length + 1);
				final ParsedGenerated load = ParsedGenerated.loadDated(prefs, "histo." + name);
				result.add(load);
			}
		}
		return result;
	}

	public ParsedGenerated current() {
		return current;
	}

	public List<ParsedGenerated> getHistorical() {
		return Collections.unmodifiableList(historical);
	}

}
