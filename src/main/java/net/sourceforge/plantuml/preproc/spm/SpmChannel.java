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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.brotli.BrotliInputStream;
import net.sourceforge.plantuml.preproc.Stdlib;

public enum SpmChannel {
	INFO, //
	PUML, //
	JSON, //
	SPRITE, //
	SVG, //
	IMAGE;

	private String getFileName() {
		return name().toLowerCase().replace('_', '-') + ".spm";
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
		final String path = "stdlib/" + libname + "/" + getFileName();
		return new BrotliInputStream(inputStream(path));
	}

	public static InputStream inputStream(String path) throws IOException {
		InputStream result = Stdlib.class.getResourceAsStream("/" + path);
		if (result == null)
			result = new BufferedInputStream(new FileInputStream(path));
		return result;
	}

}
