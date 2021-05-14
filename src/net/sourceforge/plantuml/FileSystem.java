/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 *
 *
 */
package net.sourceforge.plantuml;

import java.io.IOException;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;

public class FileSystem {

	private final static FileSystem singleton = new FileSystem();

	private ThreadLocal<String> currentDir = new ThreadLocal<>();

	private FileSystem() {
		reset();
	}

	public static FileSystem getInstance() {
		return singleton;
	}

	public void setCurrentDir(SFile dir) {
		if (dir == null) {
			this.currentDir.set(null);
		} else {
			Log.info("Setting current dir: " + dir.getAbsolutePath());
			this.currentDir.set(dir.getAbsolutePath());
		}
	}

	public SFile getCurrentDir() {
		final String path = this.currentDir.get();
		if (path != null) {
			return new SFile(path);
		}
		return null;
	}

	public SFile getFile(String nameOrPath) throws IOException {
		if (isAbsolute(nameOrPath)) {
			return new SFile(nameOrPath).getCanonicalFile();
		}
		final SFile dir = getCurrentDir();
		SFile filecurrent = null;
		if (dir != null) {
			filecurrent = dir.getAbsoluteFile().file(nameOrPath);
			if (filecurrent.exists()) {
				return filecurrent.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("plantuml.include.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();

			}
		}
		for (SFile d : SecurityUtils.getPath("java.class.path")) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists()) {
				return file.getCanonicalFile();
			}
		}
		if (dir == null) {
			assert filecurrent == null;
			return new SFile(nameOrPath).getCanonicalFile();
		}
		assert filecurrent != null;
		return filecurrent;
	}

	private boolean isAbsolute(String nameOrPath) {
		final SFile f = new SFile(nameOrPath);
		return f.isAbsolute();
	}

	public void reset() {
		setCurrentDir(new SFile("."));
	}

}
