/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.plantuml.FileFormat;

public class FormatCounter {

	private ConcurrentMap<FileFormat, AtomicLong> data = new ConcurrentHashMap<FileFormat, AtomicLong>();

	public FormatCounter() {
		for (FileFormat format : FileFormat.values()) {
			data.put(format, new AtomicLong());
		}

	}

	public void plusOne(FileFormat fileFormat, long duration) {
		final AtomicLong n = data.get(fileFormat);
		n.addAndGet(1);
	}

	public void printTable(List<String> strings) {
		Stats.addLine(strings, false, "Format", "#");
		long total = 0;
		for (Map.Entry<FileFormat, AtomicLong> ent : data.entrySet()) {
			final long value = ent.getValue().get();
			Stats.addLine(strings, false, ent.getKey().name(), value);
			total += value;
		}
		Stats.addLine(strings, true, "Total", total);

	}

	public void reload(String prefix, Preferences prefs) throws BackingStoreException {
		for (String key : prefs.keys()) {
			if (key.startsWith(prefix)) {
				try {
					final FileFormat format = FileFormat.valueOf(key.substring(prefix.length()));
					final long value = prefs.getLong(key, 0);
					data.put(format, new AtomicLong(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void save(String prefix, Preferences prefs, FileFormat fileFormat) {
		final String key = prefix + fileFormat.name();
		prefs.putLong(key, data.get(fileFormat).get());
	}

}
