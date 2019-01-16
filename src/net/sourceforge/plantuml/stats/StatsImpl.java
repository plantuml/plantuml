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

import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeSet;

import net.sourceforge.plantuml.api.NumberAnalyzed;
import net.sourceforge.plantuml.stats.api.Stats;
import net.sourceforge.plantuml.stats.api.StatsColumn;
import net.sourceforge.plantuml.stats.api.StatsLine;
import net.sourceforge.plantuml.stats.api.StatsTable;

public class StatsImpl implements Stats {

	// private final long jvmcounting;

	private final FormatCounter formatCounterCurrent;
	private final FormatCounter formatCounterEver;

	private final Map<String, ParsedGenerated> byTypeEver;
	private final Map<String, ParsedGenerated> byTypeCurrent;

	private final ParsedGenerated fullEver;
	private final HistoricalData historicalData;

	StatsImpl(Map<String, ParsedGenerated> byTypeEver, Map<String, ParsedGenerated> byTypeCurrent,
			FormatCounter formatCounterCurrent, FormatCounter formatCounterEver, HistoricalData historicalData,
			ParsedGenerated fullEver) {
		// this.jvmcounting = jvmcounting;
		this.byTypeEver = byTypeEver;
		this.byTypeCurrent = byTypeCurrent;
		this.formatCounterCurrent = formatCounterCurrent;
		this.formatCounterEver = formatCounterEver;
		this.fullEver = fullEver;
		this.historicalData = historicalData;
	}

	private StatsLine createDataLineSession(final ParsedGenerated data) {
		final Map<StatsColumn, Object> result = new EnumMap<StatsColumn, Object>(StatsColumn.class);
		final long id = data.getId();
		if (id != -1) {
			result.put(StatsColumn.SESSION_ID, id);
		}
		result.put(StatsColumn.VERSION, data.getVersion());
		final long creationTime = data.parsedDated().getCreationTime();
		final long modificationTime = data.parsedDated().getModificationTime();
		result.put(StatsColumn.STARTING, new Date(creationTime));
		result.put(StatsColumn.LAST, new Date(modificationTime));
		result.put(StatsColumn.DURATION_STRING, new HumanDuration(modificationTime - creationTime));
		result.put(StatsColumn.PARSED_COUNT, data.parsedDated().getNb());
		result.put(StatsColumn.PARSED_MEAN_TIME, data.parsedDated().getMean());
		result.put(StatsColumn.PARSED_STANDARD_DEVIATION, data.parsedDated().getStandardDeviation());
		result.put(StatsColumn.PARSED_MAX_TIME, data.parsedDated().getMax());
		result.put(StatsColumn.GENERATED_COUNT, data.generatedDated().getNb());
		result.put(StatsColumn.GENERATED_MEAN_TIME, data.generatedDated().getMean());
		result.put(StatsColumn.GENERATED_STANDARD_DEVIATION, data.generatedDated().getStandardDeviation());
		result.put(StatsColumn.GENERATED_MAX_TIME, data.generatedDated().getMax());
		return new StatsLineImpl(result);
	}

	private StatsLine createLineByDiagramType(String key, NumberAnalyzed parse, NumberAnalyzed generate) {
		final Map<StatsColumn, Object> result = new EnumMap<StatsColumn, Object>(StatsColumn.class);
		result.put(StatsColumn.DIAGRAM_TYPE, key);
		result.put(StatsColumn.PARSED_COUNT, parse.getNb());
		result.put(StatsColumn.PARSED_MEAN_TIME, parse.getMean());
		result.put(StatsColumn.PARSED_STANDARD_DEVIATION, parse.getStandardDeviation());
		result.put(StatsColumn.PARSED_MAX_TIME, parse.getMax());
		result.put(StatsColumn.GENERATED_COUNT, generate.getNb());
		result.put(StatsColumn.GENERATED_MEAN_TIME, generate.getMean());
		result.put(StatsColumn.GENERATED_STANDARD_DEVIATION, generate.getStandardDeviation());
		result.put(StatsColumn.GENERATED_MAX_TIME, generate.getMax());
		return new StatsLineImpl(result);
	}

	public StatsTable getLastSessions() {
		final StatsTableImpl result = new StatsTableImpl("Statistics");
		for (ParsedGenerated histo : historicalData.getHistorical()) {
			result.addLine(createDataLineSession(histo));
		}
		if (historicalData.current() != null) {
			result.addLine(createDataLineSession(historicalData.current()));
		}
		result.addLine(createDataLineSession(fullEver));
		return result;
	}

	public StatsTable getCurrentSessionByFormat() {
		return formatCounterCurrent.getStatsTable("current format");
	}

	public StatsTable getAllByFormat() {
		return formatCounterEver.getStatsTable("all format");
	}

	public StatsTable getCurrentSessionByDiagramType() {
		return getByDiagramType("Current session", byTypeCurrent);
	}

	public StatsTable getAllByDiagramType() {
		return getByDiagramType("All session", byTypeEver);
	}

	private StatsTable getByDiagramType(String name, Map<String, ParsedGenerated> data) {
		final StatsTableImpl result = new StatsTableImpl(name);
		final TreeSet<String> keys = new TreeSet<String>(data.keySet());
		final NumberAnalyzed totalParsing = new NumberAnalyzed();
		final NumberAnalyzed totalGenerating = new NumberAnalyzed();
		for (String key : keys) {
			final NumberAnalyzed parse = data.get(key).parsed();
			final NumberAnalyzed generate = data.get(key).generated();
			totalParsing.add(parse);
			totalGenerating.add(generate);
			result.addLine(createLineByDiagramType(key, parse, generate));
		}
		result.addLine(createLineByDiagramType("Total", totalParsing, totalGenerating));
		return result;
	}

	// public long totalLaunch() {
	// return jvmcounting;
	// }

}
