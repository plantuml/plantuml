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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileSystem {

	private final static FileSystem singleton = new FileSystem();

	private final ThreadLocal<File> currentDir = new ThreadLocal<File>();

	private FileSystem() {
		reset();
	}

	public static FileSystem getInstance() {
		return singleton;
	}

	public void setCurrentDir(File dir) {
		// if (dir == null) {
		// throw new IllegalArgumentException();
		// }
		Log.info("Setting current dir: " + dir);
		this.currentDir.set(dir);
	}

	public File getCurrentDir() {
		return this.currentDir.get();
	}

	public File getFile(String nameOrPath) throws IOException {
		if (isAbsolute(nameOrPath)) {
			return new File(nameOrPath).getCanonicalFile();
		}
		final File dir = currentDir.get();
		File filecurrent = null;
		if (dir != null) {
			filecurrent = new File(dir.getAbsoluteFile(), nameOrPath);
			if (filecurrent.exists()) {
				return filecurrent.getCanonicalFile();

			}
		}
		for (File d : getPath("plantuml.include.path", true)) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return file.getCanonicalFile();
				}
			}
		}
		for (File d : getPath("java.class.path", true)) {
			if (d.isDirectory()) {
				final File file = new File(d, nameOrPath);
				if (file.exists()) {
					return file.getCanonicalFile();
				}
			}
		}
		if (dir == null) {
			assert filecurrent == null;
			return new File(nameOrPath).getCanonicalFile();
		}
		assert filecurrent != null;
		return filecurrent;
	}

	public static List<File> getPath(String prop, boolean onlyDir) {
		final List<File> result = new ArrayList<File>();
		String paths = System.getProperty(prop);
		if (paths == null) {
			return result;
		}
		paths = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(paths);
		final StringTokenizer st = new StringTokenizer(paths, System.getProperty("path.separator"));
		while (st.hasMoreTokens()) {
			final File f = new File(st.nextToken());
			if (f.exists() && (onlyDir == false || f.isDirectory())) {
				result.add(f);
			}
		}
		return result;
	}

	private boolean isAbsolute(String nameOrPath) {
		final File f = new File(nameOrPath);
		return f.isAbsolute();
	}

	public void reset() {
		setCurrentDir(new File("."));
	}

}
