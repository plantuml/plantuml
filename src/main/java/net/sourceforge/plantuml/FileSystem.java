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
package net.sourceforge.plantuml;

import java.io.IOException;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.utils.Log;

public class FileSystem {
	// ::remove file when __HAXE__

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
		// ::comment when __CORE__
		final String path = this.currentDir.get();
		if (path != null)
			return new SFile(path);
		// ::done

		return null;
	}

	public SFile getFile(String nameOrPath) throws IOException {
		// ::uncomment when __CORE__
		// return null;
		// ::done

		// ::comment when __CORE__
		if (isAbsolute(nameOrPath)) {
			final SFile result = new SFile(nameOrPath);
			Log.info("Trying " + result.getAbsolutePath());
			return result.getCanonicalFile();
		}

		final SFile dir = getCurrentDir();
		SFile filecurrent = null;
		if (dir != null) {
			filecurrent = dir.getAbsoluteFile().file(nameOrPath);
			Log.info("Current dir is " + dir.getAbsolutePath() + " so trying " + filecurrent.getAbsolutePath());
			if (filecurrent.exists())
				return filecurrent.getCanonicalFile();

		}
		for (SFile d : SecurityUtils.getPath(SecurityUtils.PATHS_INCLUDES)) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists())
				return file.getCanonicalFile();
		}
		for (SFile d : SecurityUtils.getPath(SecurityUtils.PATHS_CLASSES)) {
			assert d.isDirectory();
			final SFile file = d.file(nameOrPath);
			if (file.exists())
				return file.getCanonicalFile();

		}
		if (dir == null) {
			assert filecurrent == null;
			return new SFile(nameOrPath).getCanonicalFile();
		}
		assert filecurrent != null;
		return filecurrent;
		// ::done
	}

	// ::comment when __CORE__
	private boolean isAbsolute(String nameOrPath) {
		final SFile f = new SFile(nameOrPath);
		return f.isAbsolute();
	}
	// ::done

	public void reset() {
		setCurrentDir(new SFile("."));
	}

}
