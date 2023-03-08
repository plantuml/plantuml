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
package net.sourceforge.plantuml;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ProgressBar {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private static final java.util.logging.Logger logger;

	static {
		logger = java.util.logging.Logger.getLogger("com.plantuml.ProgressBar");
		logger.setUseParentHandlers(false);
		logger.addHandler(new StdErrHandler());
	}

	private static boolean enable;
	private static String last = null;
	private static final AtomicInteger total = new AtomicInteger();
	private static final AtomicInteger done = new AtomicInteger();

	private synchronized static void print(String message) {
		logger.log(Level.INFO, buildClearMessage() + message);
		last = message;
	}

	public synchronized static void clear() {
		logger.log(Level.INFO, buildClearMessage());
		last = null;
	}

	private static String buildClearMessage() {
		if (last != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < last.length(); i++) {
				sb.append("\b");
			}
			for (int i = 0; i < last.length(); i++) {
				sb.append(" ");
			}
			for (int i = 0; i < last.length(); i++) {
				sb.append("\b");
			}
			return sb.toString();
		}
		return "";
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
		print("[" + getBar(done, total) + "] " + done + "/" + total);
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

	private static class StdErrHandler extends Handler {
		public StdErrHandler() {
		}

		@Override
		public void publish(LogRecord record) {
			String message = record.getMessage();
			System.err.print(message);
			this.flush();
		}

		@Override
		public void flush() {
			System.err.flush();
		}

		/**
		 * Override {@code StreamHandler.close} to do a flush but not to close the
		 * output stream. That is, we do <b>not</b> close {@code System.err}.
		 */
		@Override
		public void close() {
			flush();
		}
	}
}
