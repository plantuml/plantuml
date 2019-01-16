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

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.api.NumberAnalyzed;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class FormatCounter {

	private ConcurrentMap<FileFormat, NumberAnalyzed> data = new ConcurrentHashMap<FileFormat, NumberAnalyzed>();

	public FormatCounter(String prefix) {
		for (FileFormat format : FileFormat.values()) {
			final String key = prefix + format.name();
			data.put(format, new NumberAnalyzed(key));
		}

	}

	public void plusOne(FileFormat fileFormat, long duration) {
		final NumberAnalyzed n = data.get(fileFormat);
		n.addValue(duration);
	}

	private StatsLine createLine(String name, NumberAnalyzed n) {
		final Map<StatsColumn, Object> result = new EnumMap<StatsColumn, Object>(StatsColumn.class);
		result.put(StatsColumn.FORMAT, name);
		result.put(StatsColumn.GENERATED_COUNT, n.getNb());
		result.put(StatsColumn.GENERATED_MEAN_TIME, n.getMean());
		result.put(StatsColumn.GENERATED_STANDARD_DEVIATION, n.getStandardDeviation());
		result.put(StatsColumn.GENERATED_MAX_TIME, n.getMax());
		return new StatsLineImpl(result);
	}

	public StatsTable getStatsTable(String name) {
		final StatsTableImpl result = new StatsTableImpl(name);
		final NumberAnalyzed total = new NumberAnalyzed();
		for (Map.Entry<FileFormat, NumberAnalyzed> ent : data.entrySet()) {
			final NumberAnalyzed n = ent.getValue();
			if (n.getNb() > 0) {
				result.addLine(createLine(ent.getKey().name(), n));
				total.add(n);
			}
		}
		result.addLine(createLine("Total", total));
		return result;
	}

	public void reload(String prefix, Preferences prefs) throws BackingStoreException {
		for (String key : prefs.keys()) {
			if (key.startsWith(prefix)) {
				try {
					final String name = removeDotSaved(key);
					final NumberAnalyzed value = NumberAnalyzed.load(name, prefs);
					if (value != null) {
						final FileFormat format = FileFormat.valueOf(name.substring(prefix.length()));
						data.put(format, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static String removeDotSaved(String key) {
		return key.substring(0, key.length() - ".saved".length());
	}

	public void save(Preferences prefs, FileFormat fileFormat) {
		data.get(fileFormat).save(prefs);
	}

}
