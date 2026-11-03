/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.teavm;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * TEMPORARY, to be removed: traces to find why the C executable fails in the
 * job "Build linux-x64" of the CI (NullPointerException in the CRC32 of the PNG
 * chunks with a heap of 64 MB, segmentation fault with 128 MB).
 * <p>
 * The traces go to the standard error and only exist in the C build, where
 * {@code Run} installs the stream with {@link #install(PrintStream)}. Elsewhere
 * (JVM, tests, JavaScript) nothing is written.
 */
public final class Trace {

	private static PrintStream sink;
	private static int counter;

	private Trace() {
	}

	public static void install(PrintStream out) {
		sink = out;
	}

	public static boolean enabled() {
		return sink != null;
	}

	public static void log(String message) {
		if (sink == null)
			return;

		counter++;
		sink.println("[trace " + counter + "] " + message);
		sink.flush();
	}

	/** The free and total memory of the heap of the C runtime. */
	public static void heap(String where) {
		if (sink == null)
			return;

		final Runtime runtime = Runtime.getRuntime();
		log(where + ": heap free=" + runtime.freeMemory() + " total=" + runtime.totalMemory());
	}

	/**
	 * Uses a CRC32 on a known value ("123456789" has the CRC 0xCBF43926): tells whether
	 * the class (and its table) still works here. The exception, if any, is printed.
	 */
	public static void crc(String where) {
		if (sink == null)
			return;

		try {
			final CRC32 crc = new CRC32();
			crc.update("123456789".getBytes(StandardCharsets.ISO_8859_1));
			final long value = crc.getValue();
			log(where + ": CRC32 " + Long.toHexString(value) + (value == 0xCBF43926L ? " OK" : " WRONG"));
		} catch (Throwable t) {
			log(where + ": CRC32 FAILED with " + t);
		}
	}

	/** A garbage collection, then the same checks: does the GC lose something? */
	public static void gc(String where) {
		if (sink == null)
			return;

		heap(where + " before gc");
		System.gc();
		heap(where + " after gc");
		crc(where + " after gc");
	}

	/** The first bytes of an array, in hexadecimal. */
	public static String head(byte[] data, int count) {
		if (data == null)
			return "null";

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length && i < count; i++)
			sb.append(hex(data[i]));
		return sb.toString();
	}

	/** The last bytes of an array, in hexadecimal. */
	public static String tail(byte[] data, int count) {
		if (data == null)
			return "null";

		final StringBuilder sb = new StringBuilder();
		for (int i = Math.max(0, data.length - count); i < data.length; i++)
			sb.append(hex(data[i]));
		return sb.toString();
	}

	private static String hex(byte b) {
		final String digits = "0123456789abcdef";
		return "" + digits.charAt((b >> 4) & 0xF) + digits.charAt(b & 0xF);
	}

}
