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
 * Raw bindings to the files embedded in the executable
 * (src/teavm-c/native/plantuml_resources.h). Use {@link EmbeddedFiles}, not this
 * class.
 *
 * Same rules as {@link NativeCanvas}: only native methods, primitive arrays are
 * pointers valid during the call, text is UTF-16.
 *
 * Only usable in the C build: on the JVM (or in JavaScript) any call fails.
 */
@Include(value = "plantuml_resources.h", isSystem = false)
@Unmanaged
final class NativeResources {
	// ::remove file when JAVA8

	private NativeResources() {
	}

	@Import(name = "plantuml_resource_find")
	static native int find(char[] name, int length);

	@Import(name = "plantuml_resource_size")
	static native int size(int index);

	@Import(name = "plantuml_resource_copy")
	static native void copy(int index, byte[] dest);

	@Import(name = "plantuml_resource_decode_brotli")
	static native int decodeBrotli(int index);

	@Import(name = "plantuml_resource_copy_decoded")
	static native void copyDecoded(byte[] dest);

	@Import(name = "plantuml_resource_read_decoded")
	static native int readDecoded(int offset, int length, byte[] dest);

	@Import(name = "plantuml_resource_release_decoded")
	static native void releaseDecoded();

}
