/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 6908 $
 *
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;

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
		if (dir == null) {
			throw new IllegalArgumentException();
		}
		Log.info("Setting current dir: " + dir);
		this.currentDir.set(dir);
	}

	public File getCurrentDir() {
		return this.currentDir.get();
	}

	public File getFile(String nameOrPath) throws IOException {
		final File dir = currentDir.get();
		if (dir == null) {
			return new File(nameOrPath).getCanonicalFile();
		}
		return new File(dir.getAbsoluteFile(), nameOrPath).getCanonicalFile();
	}

	public void reset() {
		setCurrentDir(new File("."));
	}

}
