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
package net.sourceforge.plantuml.teavm.c;

import org.teavm.interop.Import;
import org.teavm.interop.Unmanaged;
import org.teavm.interop.c.Include;

/**
 * Raw binding to the standard input and output (src/teavm-c/native/plantuml_stdin.h).
 * Use {@link StdinStream} and {@link StdoutStream}, not this class.
 *
 * Same rules as {@link NativeCanvas}: only native methods, primitive arrays are
 * pointers valid during the call.
 *
 * Only usable in the C build: on the JVM (or in JavaScript) any call fails.
 */
@Include(value = "plantuml_stdin.h", isSystem = false)
@Unmanaged
final class NativeStdin {
	// ::remove file when JAVA8

	private NativeStdin() {
	}

	/**
	 * Reads up to {@code length} bytes into the start of {@code dest}. Returns the
	 * number of bytes read (at least one: it blocks until some are available), -1
	 * at the end of the input, -2 on error.
	 */
	@Import(name = "plantuml_stdin_read")
	static native int read(byte[] dest, int length);

	/**
	 * Writes the first {@code length} bytes of {@code src} to the standard output,
	 * unchanged (binary mode). Returns 0, or -1 on error.
	 */
	@Import(name = "plantuml_stdout_write")
	static native int write(byte[] src, int length);

	/**
	 * Same as {@link #write(byte[], int)}, for the standard error.
	 */
	@Import(name = "plantuml_stderr_write")
	static native int writeError(byte[] src, int length);

}
