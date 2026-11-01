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
 * Raw binding to the HTTP client (src/teavm-c/native/plantuml_http.h). Use
 * {@link HttpC}, not this class.
 *
 * Same rules as {@link NativeStdin}: only native methods, primitive arrays are
 * pointers valid during the call, and a native method fills the start of its
 * array.
 *
 * Only usable in the C build: on the JVM (or in JavaScript) any call fails.
 */
@Include(value = "plantuml_http.h", isSystem = false)
@Unmanaged
final class NativeHttp {
	// ::remove file when JAVA8

	/** The host may resolve to an inner address. */
	static final int ALLOW_INNER = 1;

	/** Follow the redirections. */
	static final int FOLLOW_REDIRECTS = 2;

	/** Built without an HTTP client (no libcurl). */
	static final int NOT_AVAILABLE = -10;

	private NativeHttp() {
	}

	/**
	 * Downloads {@code url} (its first {@code urlLength} bytes, UTF-8). Returns the
	 * size of the response, kept natively until {@link #release()}, or a negative
	 * code (see plantuml_http.h).
	 */
	@Import(name = "plantuml_http_fetch")
	static native int fetch(byte[] url, int urlLength, int timeoutMs, int maxBytes, int flags);

	/**
	 * Copies the next bytes of the response into the start of {@code dest}. Returns
	 * the number of bytes copied, 0 when nothing is left.
	 */
	@Import(name = "plantuml_http_read")
	static native int read(byte[] dest, int length);

	/** Forgets the response. */
	@Import(name = "plantuml_http_release")
	static native void release();

}
