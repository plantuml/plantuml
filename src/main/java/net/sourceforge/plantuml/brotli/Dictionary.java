/* Copyright 2015 Google Inc. All Rights Reserved.

   https://github.com/google/brotli/blob/master/LICENSE

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package net.sourceforge.plantuml.brotli;

import java.nio.ByteBuffer;

/**
 * Collection of static dictionary words.
 *
 * <p>
 * Dictionary content is loaded from binary resource when {@link #getData()} is
 * executed for the first time. Consequently, it saves memory and CPU in case
 * dictionary is not required.
 */
public final class Dictionary {
	private static volatile ByteBuffer data;
	private static boolean initialized = false;

	public static void setData(ByteBuffer data) {
		if (!data.isDirect() || !data.isReadOnly()) {
			throw new BrotliRuntimeException("data must be a direct read-only byte buffer");
		}
		Dictionary.data = data;
	}

	public static ByteBuffer getData() {
		// 1. If the buffer is already present, return it directly (fast path)
		if (data != null) {
			return data;
		}

		// 2. Double-checked locking to initialize in a thread-safe manner at run-time
		synchronized (Dictionary.class) {
			if (!initialized) {
				try {
					// Direct call to DictionaryData instead of using Class.forName.
					// This allows GraalVM to detect the dependency and ensure DictionaryData 
					// is included in the native binary.
					net.sourceforge.plantuml.brotli.DictionaryData.init(); 
				} catch (Throwable ex) {
					// Optional: logging or error handling
				}
				initialized = true;
			}
		}

		// 3. If it is still null after initialization, something went wrong
		if (data == null) {
			throw new BrotliRuntimeException("brotli dictionary is not set");
		}

		return data;
	}
}
