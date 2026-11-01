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

import java.nio.charset.StandardCharsets;

import net.sourceforge.plantuml.security.SecurityProfile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.security.UrlPolicy;
import net.sourceforge.plantuml.utils.Log;

/**
 * Reads an http(s) URL in the C build, for {@code !include}, {@code !includesub}
 * and the other directives that read a file.
 * <p>
 * The decision (security profile, {@code plantuml.allowlist.url}) is taken here
 * by {@link UrlPolicy}, with the rules of {@code SURL} on the JVM; the transfer is
 * done by native code (WinHTTP on Windows, libcurl elsewhere).
 * <p>
 * Only call it when {@code TeaVM.isC()}: elsewhere the native methods fail.
 */
public final class HttpC {
	// ::remove file when JAVA8

	/** Largest response read, whatever the profile. */
	private static final int MAX_BYTES = 32 * 1024 * 1024;

	private static final int BLOCK = 16384;

	private HttpC() {
	}

	/**
	 * The content of the URL, or {@code null} if the security profile refuses it or
	 * if it cannot be read.
	 */
	public static byte[] get(String url) {
		if (UrlPolicy.isAllowed(url) == false) {
			Log.info(() -> "URL refused by the security profile: " + url);
			return null;
		}

		final SecurityProfile profile = SecurityUtils.getSecurityProfile();
		int flags = 0;
		if (UrlPolicy.isInnerAddressAllowed(url))
			flags |= NativeHttp.ALLOW_INNER;
		// As on the JVM, only INSECURE follows the redirections
		if (profile == SecurityProfile.INSECURE)
			flags |= NativeHttp.FOLLOW_REDIRECTS;

		final int timeout = (int) Math.min(profile.getTimeout(), Integer.MAX_VALUE);
		final byte[] bytes = url.getBytes(StandardCharsets.UTF_8);
		final int size = NativeHttp.fetch(bytes, bytes.length, timeout, MAX_BYTES, flags);
		if (size < 0) {
			if (size == NativeHttp.NOT_AVAILABLE)
				Log.error("This build cannot read a URL: libcurl was not available when it was built");
			else
				Log.info(() -> "Cannot read " + url + " (code " + size + ")");
			return null;
		}

		try {
			final byte[] result = new byte[size];
			final byte[] block = new byte[BLOCK];
			int done = 0;
			while (done < size) {
				final int count = NativeHttp.read(block, Math.min(BLOCK, size - done));
				if (count <= 0)
					return null;
				System.arraycopy(block, 0, result, done, count);
				done += count;
			}
			return result;
		} finally {
			NativeHttp.release();
		}
	}

}
