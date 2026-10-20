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

// ::remove file when JAVA8

/**
 * Files embedded in the executable of the C build: the themes today, the
 * standard library later. They come from src/main/resources, copied by Gradle
 * next to the generated C and embedded by CMake (embed_resources.cmake).
 * <p>
 * A file is designated by its path under src/main/resources, for instance
 * "themes/puml-theme-amiga.puml".
 * <p>
 * Only call it when {@code TeaVM.isC()}: elsewhere the native methods fail.
 */
public final class EmbeddedFiles {

	private EmbeddedFiles() {
	}

	/**
	 * The content of the embedded file of this name, or null if there is none.
	 */
	public static byte[] read(String name) {
		final int index = NativeResources.find(name.toCharArray(), name.length());
		if (index < 0)
			return null;

		final byte[] result = new byte[NativeResources.size(index)];
		NativeResources.copy(index, result);
		return result;
	}

	/**
	 * The content of the embedded file of this name, which is compressed with
	 * Brotli (a .spm file of the standard library), decompressed; or null if there
	 * is no such file, or it is not valid Brotli.
	 * <p>
	 * C decompresses it straight from the embedded bytes, which never go through
	 * the Java heap.
	 */
	public static byte[] readBrotli(String name) {
		final int index = NativeResources.find(name.toCharArray(), name.length());
		if (index < 0)
			return null;

		final int size = NativeResources.decodeBrotli(index);
		if (size < 0)
			return null;

		final byte[] result = new byte[size];
		NativeResources.copyDecoded(result);
		return result;
	}

}
