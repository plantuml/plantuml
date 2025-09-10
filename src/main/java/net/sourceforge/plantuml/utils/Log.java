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
package net.sourceforge.plantuml.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Supplier;

import net.sourceforge.plantuml.ProgressBar;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;

public abstract class Log {
	// ::comment when __HAXE__
	private static final long start = System.currentTimeMillis();

	private static String format(String s) {
		final long delta = System.currentTimeMillis() - start;
		// final HealthCheck healthCheck = Performance.getHealthCheck();
		// final long cpu = healthCheck.jvmCpuTime() / 1000L / 1000L;
		// final long dot = healthCheck.dotTime().getSum();

		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long maxMemory = Runtime.getRuntime().maxMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long usedMemory = totalMemory - freeMemory;
		final int threadActiveCount = Thread.activeCount();

		final StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(delta / 1000L);
		sb.append(".");
		sb.append(String.format("%03d", delta % 1000L));
		// if (cpu != -1) {
		// sb.append(" - ");
		// sb.append(cpu / 1000L);
		// sb.append(".");
		// sb.append(String.format("%03d", cpu % 1000L));
		// }
		// sb.append(" - ");
		// sb.append(dot / 1000L);
		// sb.append(".");
		// sb.append(String.format("%03d", dot % 1000L));
		// sb.append("(");
		// sb.append(healthCheck.dotTime().getNb());
		// sb.append(")");
		sb.append(" - ");
		final long total = totalMemory / 1024 / 1024;
		final long free = freeMemory / 1024 / 1024;
		sb.append(total);
		sb.append(" Mo) ");
		sb.append(free);
		sb.append(" Mo - ");
		sb.append(s);
		return sb.toString();
	}

	public static void println(Object s) {
	}
	// ::done

	public synchronized static void debug(Supplier<String> msgSupplier) {
	}

	public synchronized static void info(Supplier<String> msgSupplier) {
		// ::comment when __CORE__ or __HAXE__
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.VERBOSE)) {
			ProgressBar.clear();
			System.err.println(format(msgSupplier.get()));
		}
		// ::done
	}

	public synchronized static void error(String s) {
		// ::comment when __CORE__ or __HAXE__
		ProgressBar.clear();
		// ::done
		System.err.println(s);
	}

	public static void header(String s) {
	}

	private static final String PERFLOG_FILENAME = "perflog.txt";
	private static boolean firstCall = true;

	public static void perflog(String line) {
		// perflogInternal(line);
	}

	public static void deletePerfLogFile() {
		final File file = new File(PERFLOG_FILENAME);
		if (file.exists()) {
			if (!file.delete()) {
				error("Cannot del " + PERFLOG_FILENAME);
			}
		}
	}

	private synchronized static void perflogInternal(String line) {
		try {
			if (firstCall) {
				deletePerfLogFile();
				firstCall = false;
			}
			try (FileWriter fw = new FileWriter(PERFLOG_FILENAME, true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				out.println(line);
			}
		} catch (IOException e) {
			error("Cannot write in " + PERFLOG_FILENAME + " : " + e.getMessage());
		}
	}

	public static synchronized void logStackTrace(String s) {
		if (firstCall) {
			deletePerfLogFile();
			firstCall = false;
		}
		try (FileWriter fw = new FileWriter(PERFLOG_FILENAME, true); PrintWriter pw = new PrintWriter(fw)) {
			final Exception e = new Exception("StackTrace " + s);
			e.fillInStackTrace();
			e.printStackTrace(pw);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
