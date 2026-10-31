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
package net.sourceforge.plantuml.preproc.spm;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.brotli.BrotliInputStream;
// ::comment when JAVA8
import net.sourceforge.plantuml.teavm.c.EmbeddedFiles;
// ::done
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.preproc.Stdlib;

public enum SpmChannel {
	INFO, //
	PUML, //
	JSON, //
	SPRITE, //
	SVG, //
	IMAGE;

	private String getFileName() {
		return StringUtils.goLowerCase(name()).replace('_', '-') + ".spm";
	}

//	public Path getPath(Path rootFolder, String name) throws IOException {
//		final Path dir = rootFolder.resolve(name);
//		return getPath(dir);
//	}
//
//	public Path getPath(Path dir) throws IOException {
//		Files.createDirectories(dir);
//		return dir.resolve(getFileName());
//	}

	public InputStream getInternalInputStream(String libname) throws IOException {
		// No TeaVM build ships the .spm files: the browser engines get the standard
		// library from <lib>.min.js bundles or PLANTUML_STDLIB_LOADER instead. Failing
		// here for every TeaVM target, and not only C, keeps the Brotli decoder and its
		// big dictionary out of plantuml.js and plantuml.wasm, whatever path reaches
		// Stdlib (a local !theme, %get_stdlib, a sprite...).
		if (TeaVM.isTeaVM() && TeaVM.isC() == false)
			throw new IOException("The standard library is not available in the TeaVM build");
		
		final String path = "stdlib/" + libname + "/" + getFileName();
		// ::comment when JAVA8
		// The C build embeds the .spm files, and decompresses them with the Brotli
		// decoder in C: the Java one and its big dictionary stay out of it
		if (TeaVM.isC()) {
			// Read by parts from C memory: some files are tens of megabytes once
			// decompressed, more than the heap. The stream must be closed.
			final InputStream result = EmbeddedFiles.openBrotli(path);
			if (result == null)
				throw new IOException("Not available in the standard library of the C build: " + path);

			return result;
		}
		// ::done
		return new BrotliInputStream(inputStream(path));
	}

	public static InputStream inputStream(String path) throws IOException {
		// ::comment when JAVA8
		if (TeaVM.isC()) {
			final byte[] data = EmbeddedFiles.read(path);
			if (data == null)
				throw new IOException("Not embedded in the C build: " + path);

			return new ByteArrayInputStream(data);
		}
		// ::done
		InputStream result = Stdlib.class.getResourceAsStream("/" + path);
		if (result == null)
			result = new BufferedInputStream(new FileInputStream(path));
		return result;
	}

}
