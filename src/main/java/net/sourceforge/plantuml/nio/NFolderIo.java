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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

/**
 * A folder read with java.io only, for the C build: TeaVM's C classlib has
 * java.io.File but no file system provider behind java.nio.file. It is what
 * the includes of a diagram resolve their relative paths against.
 * <p>
 * Every file goes through {@link SFile}, so that the security profile, the
 * include path (plantuml.include.path) and the allowlist
 * (plantuml.allowlist.path) apply as on the JVM. It carries no archive, unlike
 * {@link NFolderRegular}.
 */
public class NFolderIo implements NFolder {

	private final File dir;

	public NFolderIo(File dir) {
		this.dir = dir;
	}

	/**
	 * The readable file of this name or path (relative to this folder unless it is
	 * absolute), or null.
	 */
	public InputFile getInputFile(String nameOrPath) {
		File file = new File(nameOrPath);
		if (file.isAbsolute() == false)
			file = new File(dir, nameOrPath);

		if (file.isFile() && SFile.fromFile(file).canRead())
			return new InputFileIo(file);

		// Not found from this folder: the include path, as NFolderRegular does
		if (new File(nameOrPath).isAbsolute() == false)
			for (SFile d : SecurityUtils.getPath(PATHS_INCLUDES)) {
				final File inInclude = new File(d.conv(), nameOrPath);
				if (inInclude.isFile() && SFile.fromFile(inInclude).canRead())
					return new InputFileIo(inInclude);
			}

		return null;
	}

	@Override
	public InputFile getInputFile(Path nameOrPath) throws IOException {
		return getInputFile(nameOrPath.toString());
	}

	@Override
	public NFolder getSubfolder(Path nameOrPath) throws IOException {
		return new NFolderIo(new File(dir, nameOrPath.toString()));
	}

	@Override
	public String toString() {
		return "NFolderIo[" + dir + "]";
	}

}
