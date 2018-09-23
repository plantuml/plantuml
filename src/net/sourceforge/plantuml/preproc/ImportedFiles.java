/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * Modified by: Nicolas Jouanin
 * 
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AFile;
import net.sourceforge.plantuml.AFileRegular;
import net.sourceforge.plantuml.AFileZipEntry;
import net.sourceforge.plantuml.AParentFolder;
import net.sourceforge.plantuml.FileSystem;

public class ImportedFiles {

	private final List<File> imported = new ArrayList<File>();
	private AParentFolder currentDir;

	public AFile getAFile(String nameOrPath) throws IOException {
		final AParentFolder dir = currentDir;
		if (dir == null || isAbsolute(nameOrPath)) {
			return new AFileRegular(new File(nameOrPath).getCanonicalFile());
		}
		// final File filecurrent = new File(dir.getAbsoluteFile(), nameOrPath);
		final AFile filecurrent = dir.getAFile(nameOrPath);
		if (filecurrent != null && filecurrent.isOk()) {
			return filecurrent;
		}
		for (File d : getPath()) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return new AFileRegular(file.getCanonicalFile());
				}
			} else if (d.isFile()) {
				final AFileZipEntry zipEntry = new AFileZipEntry(d, nameOrPath);
				if (zipEntry.isOk()) {
					return zipEntry;
				}
			}
		}
		return filecurrent;
	}

	public List<File> getPath() {
		final List<File> result = new ArrayList<File>(imported);
		result.addAll(FileSystem.getPath("plantuml.include.path", true));
		result.addAll(FileSystem.getPath("java.class.path", true));
		return result;
	}

	private boolean isAbsolute(String nameOrPath) {
		final File f = new File(nameOrPath);
		return f.isAbsolute();
	}

	public void add(File file) {
		this.imported.add(file);
	}

	public void setCurrentDir(AParentFolder newCurrentDir) {
		this.currentDir = newCurrentDir;
	}

	public AParentFolder getCurrentDir() {
		return currentDir;
	}

}
