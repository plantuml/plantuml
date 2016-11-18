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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import net.sourceforge.plantuml.api.NumberAnalyzed;

public class Stats {

	private final long firstEverStartingTime;
	private final long lastStartingTime;
	private final long currentStartingTime;
	private final long jvmcounting;
	private final Map<String, NumberAnalyzed> parsingEver;
	private final Map<String, NumberAnalyzed> generatingEver;
	private final Map<String, NumberAnalyzed> parsingCurrent;
	private final Map<String, NumberAnalyzed> generatingCurrent;
	private final FormatCounter formatCounterCurrent;
	private final FormatCounter formatCounterEver;

	Stats(long firstEverStartingTime, long lastStartingTime, long thisStartingTime, long jvmcounting,
			Map<String, NumberAnalyzed> parsingEver, Map<String, NumberAnalyzed> generatingEver,
			ConcurrentMap<String, NumberAnalyzed> parsingCurrent,
			ConcurrentMap<String, NumberAnalyzed> generatingCurrent, FormatCounter formatCounterCurrent,
			FormatCounter formatCounterEver) {
		this.firstEverStartingTime = firstEverStartingTime;
		this.lastStartingTime = lastStartingTime;
		this.currentStartingTime = thisStartingTime;
		this.jvmcounting = jvmcounting;
		this.parsingEver = parsingEver;
		this.generatingEver = generatingEver;
		this.parsingCurrent = parsingCurrent;
		this.generatingCurrent = generatingCurrent;
		this.formatCounterCurrent = formatCounterCurrent;
		this.formatCounterEver = formatCounterEver;
	}

	public void printMe(List<String> strings) {
		strings.add("<b><size:16>Statistics</b>");
		strings.add("| Parameter | Value |");
		strings.add("| <i>First Starting Time Ever</i> | " + new Date(firstEverStartingTime) + "|");
		strings.add("| <i>Last Starting Time</i> | " + new Date(lastStartingTime) + "|");
		strings.add("| <i>Current Starting Time</i> | " + new Date(currentStartingTime) + "|");
		strings.add("| <i>Total launch</i> | " + jvmcounting + "|");
		strings.add(" ");
		strings.add("<b><size:16>Current session statistics</b>");
		printTable(strings, parsingCurrent, generatingCurrent);
		strings.add(" ");
		formatCounterCurrent.printTable(strings);
		strings.add(" ");
		strings.add("<b><size:16>General statistics since ever</b>");
		printTable(strings, parsingEver, generatingEver);
		strings.add(" ");
		formatCounterEver.printTable(strings);

	}

	private static void printTable(List<String> strings, Map<String, NumberAnalyzed> parsingEver,
			Map<String, NumberAnalyzed> generatingEver) {
		final TreeSet<String> keys = new TreeSet<String>(parsingEver.keySet());
		keys.addAll(generatingEver.keySet());
		addLine(strings, false, "Diagram type", "# Parsed", "Mean parsing\\ntime (ms)", "Max parsing\\ntime (ms)",
				"# Generated", "Mean generation\\ntime (ms)", "Max generation\\ntime (ms)");
		NumberAnalyzed totalParsing = new NumberAnalyzed();
		NumberAnalyzed totalGenerating = new NumberAnalyzed();
		for (String key : keys) {
			NumberAnalyzed parse = parsingEver.get(key);
			if (parse == null) {
				parse = new NumberAnalyzed();
			}
			NumberAnalyzed generate = generatingEver.get(key);
			if (generate == null) {
				generate = new NumberAnalyzed();
			}
			totalParsing.add(parse);
			totalGenerating.add(generate);
			addLine(strings, false, key, parse.getNb(), parse.getMean(), parse.getMax(), generate.getNb(),
					generate.getMean(), generate.getMax());
		}
		addLine(strings, true, "Total", totalParsing.getNb(), totalParsing.getMean(), totalParsing.getMax(),
				totalGenerating.getNb(), totalGenerating.getMean(), totalGenerating.getMax());
	}

	static void addLine(List<String> strings, boolean bold, Object... data) {
		final StringBuilder result = new StringBuilder();
		for (Object v : data) {
			result.append("| ");
			if (bold) {
				result.append("<b>");
			}
			result.append(v.toString());
			if (bold) {
				result.append("</b>");
			}
			result.append(" ");
		}
		result.append("|");
		strings.add(result.toString());
	}

}
