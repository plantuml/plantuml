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
package net.sourceforge.plantuml;

import java.util.concurrent.atomic.AtomicInteger;

public class ProgressBar {

	private static boolean enable;
	private static String last = null;
	private static final AtomicInteger total = new AtomicInteger();
	private static final AtomicInteger done = new AtomicInteger();

	private synchronized static void print(String message) {
		clear();
		System.err.print(message);
		last = message;
	}

	public synchronized static void clear() {
		if (last != null) {
			for (int i = 0; i < last.length(); i++) {
				System.err.print('\b');
			}
			for (int i = 0; i < last.length(); i++) {
				System.err.print(' ');
			}
			for (int i = 0; i < last.length(); i++) {
				System.err.print('\b');
			}
		}
		last = null;
	}

	public static void incTotal(int nb) {
		total.addAndGet(nb);
		printBar(done.intValue(), total.intValue());
	}

	private synchronized static void printBar(int done, int total) {
		if (enable == false) {
			return;
		}
		if (total == 0) {
			return;
		}
		final String message = "[" + getBar(done, total) + "] " + done + "/" + total;
		print(message);

	}

	private static String getBar(int done, int total) {
		final StringBuilder sb = new StringBuilder();
		final int width = 30;
		final int value = width * done / total;
		for (int i = 0; i < width; i++) {
			sb.append(i < value ? '#' : ' ');
		}
		return sb.toString();
	}

	public static void incDone(boolean error) {
		done.incrementAndGet();
		printBar(done.intValue(), total.intValue());
	}

	public static void setEnable(boolean value) {
		enable = value;
	}

}
