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
package net.sourceforge.plantuml;


public abstract class Log {

	private static final long start = System.currentTimeMillis();

	public static synchronized void debug(String s) {
	}

	public static synchronized void info(String s) {
		if (OptionFlags.getInstance().isVerbose()) {
			ProgressBar.clear();
			System.err.println(format(s));
		}
	}

	public static synchronized void error(String s) {
		ProgressBar.clear();
		System.err.println(s);
	}

	private static String format(String s) {
		final long delta = System.currentTimeMillis() - start;
		final long freeMemory = Runtime.getRuntime().freeMemory();
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long total = totalMemory / 1024 / 1024;
		final long free = freeMemory / 1024 / 1024;
		return String.format("(%d.%s - %s Mo) %s Mo - %s", delta / 1000L, String.format("%03d", delta % 1000L), total, free, s);

	}

	public static void println(Object s) {
		if (header2.get() == null) {
			System.err.println("L = " + s);
		} else {
			System.err.println(header2.get() + " " + s);
		}
	}

	private static final ThreadLocal<String> header2 = new ThreadLocal<>();

	public static void header(String s) {
		header2.set(s);
	}
}
