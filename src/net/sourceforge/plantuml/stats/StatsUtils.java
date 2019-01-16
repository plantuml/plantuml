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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.stats.api.Stats;

public class StatsUtils {

	final static Preferences prefs = Preferences.userNodeForPackage(StatsUtils.class);

	static ParsedGenerated fullEver;

	static ConcurrentMap<String, ParsedGenerated> byTypeEver = new ConcurrentHashMap<String, ParsedGenerated>();
	static ConcurrentMap<String, ParsedGenerated> byTypeCurrent = new ConcurrentHashMap<String, ParsedGenerated>();

	static FormatCounter formatCounterCurrent = new FormatCounter("currentformat.");
	static FormatCounter formatCounterEver = new FormatCounter("format.");

	static HistoricalData historicalData;

	static boolean xmlStats = false;
	static boolean htmlStats = false;
	static boolean realTimeStats = false;

	public static Stats getStats() {
		return new StatsImpl(byTypeEver, byTypeCurrent, formatCounterCurrent, formatCounterEver, historicalData,
				fullEver);
	}

	private final static int VERSION = 14;

	static {
		try {
			// Logger.getLogger("java.util.prefs").setLevel(Level.OFF);
			Logger.getLogger("java.util.prefs").setFilter(new Filter() {
				public boolean isLoggable(LogRecord record) {
					final String message = record.getMessage();
					System.err.println("SPECIAL TRACE FOR PLANTUML: " + message);
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (prefs.getInt("VERSION", 0) != VERSION) {
			try {
				prefs.clear();
			} catch (BackingStoreException e1) {
				e1.printStackTrace();
			}
			prefs.putInt("VERSION", VERSION);
		}
		restoreNow();
		if (historicalData != null) {
			historicalData.reset();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				endingHook();
			}
		});
	}

	private static void restoreNow() {
		try {
			prefs.sync();
			fullEver = ParsedGenerated.loadDated(prefs, "full");
			historicalData = new HistoricalData(prefs);
			reload();
			formatCounterEver.reload("format.", prefs);
		} catch (BackingStoreException e) {
			Log.error("Error reloading stats " + e);
			byTypeEver.clear();
		}
	}

	private static void reload() throws BackingStoreException {
		for (String key : prefs.keys()) {
			if (key.startsWith("type.") && key.endsWith(".p.saved")) {
				final String name = removeDotPSaved(key);
				final ParsedGenerated p = ParsedGenerated.loadDated(prefs, name);
				if (p != null) {
					byTypeEver.put(name.substring("type.".length()), p);
				}
			}
		}
	}

	static String removeDotPSaved(String key) {
		return key.substring(0, key.length() - ".p.saved".length());
	}

	private static void endingHook() {
		try {
			final Stats stats = getStatsLazzy();
			if (xmlStats) {
				xmlOutput(stats);
			}
			if (htmlStats) {
				htmlOutput(stats);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Stats getStatsLazzy() {
		if (xmlStats || htmlStats) {
			return getStats();
		}
		return null;
	}

	static void htmlOutput(Stats stats) throws FileNotFoundException {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("plantuml-stats.html");
			pw.print(new HtmlConverter(stats).toHtml());
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	static void xmlOutput(Stats stats) throws FileNotFoundException, TransformerException,
			ParserConfigurationException, IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream("plantuml-stats.xml");
			new XmlConverter(stats).createXml(os);
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public static void setXmlStats(boolean value) {
		xmlStats = value;
	}

	public static void setHtmlStats(boolean value) {
		htmlStats = value;
	}

	public static void setRealTimeStats(boolean value) {
		realTimeStats = value;
	}

	public static void outHtml() throws FileNotFoundException {
		htmlOutput(getStats());
	}

	public static void dumpStats() {
		new TextConverter(getStats()).printMe(System.out);
	}

	public static void loopStats() throws InterruptedException {
		int linesUsed = 0;
		while (true) {
			restoreNow();
			clearScreen(System.out, linesUsed);
			final TextConverter textConverter = new TextConverter(getStats());
			textConverter.printMe(System.out);
			linesUsed = textConverter.getLinesUsed();
			Thread.sleep(3000L);
		}

	}

	private static void clearScreen(PrintStream ps, int linesUsed) {
		if (linesUsed == 0) {
			return;
		}
		if (File.separatorChar == '/') {
			System.out.println(String.format("\033[%dA", linesUsed + 1)); // Move up
		} else {
			for (int i = 0; i < 20; i++) {
				ps.println();
			}
		}
	}
}
