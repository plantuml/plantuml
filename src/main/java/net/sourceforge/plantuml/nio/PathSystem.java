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
package net.sourceforge.plantuml.nio;

import java.io.IOException;
import java.nio.file.Path;

import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;

// Replacement for FileSystem
//See ImportedFiles
//See TContext::executeInclude
//See PreprocessorUtils

public class PathSystem {

	public static PathSystem fetch() {
		return new PathSystem(new NFolderRegular(Path.of(".")));
	}

	private final NFolder currentFolder;

	private PathSystem(NFolder currentFolder) {
		this.currentFolder = currentFolder;
	}

//	public Path asPath() {
//		return currentFolder;
//	}

	public PathSystem changeCurrentDirectory(Path newCurrentDir) throws IOException {
		return new PathSystem(currentFolder.getSubfolder(newCurrentDir));
	}

	@Override
	public String toString() {
		return currentFolder.toString();
	}

	public InputFile getInputFile(String path) throws IOException {
		if (path.startsWith("http://") || path.startsWith("https://")) {
			final SURL url = SURL.create(path);
			if (url == null)
				throw new IOException("Cannot open URL " + path);
			return new InputFileUrl(url);
		}

		if (path.startsWith("<") && path.endsWith(">")) {
			final String full = path.substring(1, path.length() - 1);
			final String libname = full.substring(0, full.indexOf('/'));
			final String filepath = full.substring(libname.length() + 1);
			return new InputFileStdlib(Stdlib.retrieve(libname), Path.of(filepath));
		}

		if (path.startsWith("~/")) {
			// Expand to the user's home directory
			final String home = System.getProperty("user.home");
			final Path homePath = Path.of(home).resolve(path.substring(2)).normalize();
			return SFile.fromFile(homePath.toFile());
		}

		return currentFolder.getInputFile(Path.of(path));
	}

	public static void main(String[] args) {
		System.out.println(PathSystem.fetch());
	}
}
