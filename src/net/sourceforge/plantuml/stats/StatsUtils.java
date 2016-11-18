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

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.core.Diagram;

public class StatsUtils {

	public static void onceMoreParse(long duration, Class<? extends Diagram> type) {
	}

	public static void onceMoreGenerate(long duration, Class<? extends Diagram> type, FileFormat fileFormat) {
	}

	public static Stats getStats() {
		return null;
	}

	// final private static Preferences prefs = Preferences.userNodeForPackage(StatsUtils.class);
	//
	// private static long firstEverStartingTime;
	// private static long lastStartingTime;
	// private static final long currentStartingTime;
	// private static final long jvmCounting;
	//
	// private static ConcurrentMap<String, NumberAnalyzed> parsingEver = new ConcurrentHashMap<String,
	// NumberAnalyzed>();
	// private static ConcurrentMap<String, NumberAnalyzed> generatingEver = new ConcurrentHashMap<String,
	// NumberAnalyzed>();
	// private static ConcurrentMap<String, NumberAnalyzed> parsingCurrent = new ConcurrentHashMap<String,
	// NumberAnalyzed>();
	// private static ConcurrentMap<String, NumberAnalyzed> generatingCurrent = new ConcurrentHashMap<String,
	// NumberAnalyzed>();
	//
	// private static FormatCounter formatCounterCurrent = new FormatCounter();
	// private static FormatCounter formatCounterEver = new FormatCounter();
	//
	// public static void onceMoreParse(long duration, Class<? extends Diagram> type) {
	// getNumber("parse.", type, parsingCurrent).addValue(duration);
	// final NumberAnalyzed n1 = getNumber("parse.", type, parsingEver);
	// n1.addValue(duration);
	// n1.save(prefs);
	// }
	//
	// public static void onceMoreGenerate(long duration, Class<? extends Diagram> type, FileFormat fileFormat) {
	// getNumber("generate.", type, generatingCurrent).addValue(duration);
	// final NumberAnalyzed n1 = getNumber("generate.", type, generatingEver);
	// n1.addValue(duration);
	// formatCounterCurrent.plusOne(fileFormat, duration);
	// formatCounterEver.plusOne(fileFormat, duration);
	// formatCounterEver.save("format.", prefs, fileFormat);
	// n1.save(prefs);
	// }
	//
	// public static Stats getStats() {
	// return new Stats(firstEverStartingTime, lastStartingTime, currentStartingTime, jvmCounting, parsingEver,
	// generatingEver, parsingCurrent, generatingCurrent, formatCounterCurrent, formatCounterEver);
	// }
	//
	// private static NumberAnalyzed getNumber(String prefix, Class<? extends Diagram> type,
	// ConcurrentMap<String, NumberAnalyzed> map) {
	// final String name = name(type);
	// NumberAnalyzed n = map.get(name);
	// if (n == null) {
	// map.putIfAbsent(name, new NumberAnalyzed(prefix + name));
	// n = map.get(name);
	// }
	// return n;
	// }
	//
	// private static String name(Class<? extends Diagram> type) {
	// if (type == PSystemError.class) {
	// return "Error";
	// }
	// if (type == ActivityDiagram3.class) {
	// return "ActivityDiagramBeta";
	// }
	// if (type == PSystemSalt.class) {
	// return "Salt";
	// }
	// if (type == PSystemSudoku.class) {
	// return "Sudoku";
	// }
	// if (type == PSystemDot.class) {
	// return "Dot";
	// }
	// if (type == PSystemEmpty.class) {
	// return "Welcome";
	// }
	// if (type == PSystemDitaa.class) {
	// return "Ditaa";
	// }
	// if (type == PSystemJcckit.class) {
	// return "Jcckit";
	// }
	// final String name = type.getSimpleName();
	// if (name.endsWith("Diagram")) {
	// return name;
	// }
	// // return "Other " + name;
	// return "Other";
	// }
	//
	// static {
	// // try {
	// // prefs.clear();
	// // } catch (BackingStoreException e1) {
	// // e1.printStackTrace();
	// // }
	// currentStartingTime = System.currentTimeMillis();
	// firstEverStartingTime = prefs.getLong("firstEverStartingTime", 0);
	// if (firstEverStartingTime == 0) {
	// firstEverStartingTime = currentStartingTime;
	// prefs.putLong("firstEverStartingTime", firstEverStartingTime);
	// }
	// lastStartingTime = prefs.getLong("lastStartingTime", 0);
	// if (lastStartingTime == 0) {
	// lastStartingTime = currentStartingTime;
	// }
	// prefs.putLong("lastStartingTime", currentStartingTime);
	// jvmCounting = prefs.getLong("JVMcounting", 0) + 1;
	// prefs.putLong("JVMcounting", jvmCounting);
	// try {
	// reload("parse.", parsingEver);
	// reload("generate.", generatingEver);
	// formatCounterEver.reload("format.", prefs);
	// } catch (BackingStoreException e) {
	// Log.error("Error reloading stats " + e);
	// parsingEver.clear();
	// generatingEver.clear();
	// }
	// }
	//
	// private static void reload(final String tree, Map<String, NumberAnalyzed> data) throws BackingStoreException {
	// for (String key : prefs.keys()) {
	// if (key.startsWith(tree) && key.endsWith(".nb")) {
	// final String name = key.substring(0, key.length() - 3);
	// final NumberAnalyzed numberAnalyzed = NumberAnalyzed.load(name, prefs);
	// if (numberAnalyzed != null) {
	// data.put(name.substring(tree.length()), numberAnalyzed);
	// }
	// }
	// }
	// }

}
