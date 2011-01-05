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
 * Revision $Revision: 5749 $
 *
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.plantuml.cucadiagram.dot.DrawFile;

public class FileUtils {

	private static final Collection<DrawFile> toDelete = new ArrayList<DrawFile>();

	public static void deleteOnExit(DrawFile file) {
		if (toDelete.isEmpty()) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
						for (DrawFile f : toDelete) {
							f.delete();
						}
					}
				}
			});
		}
		toDelete.add(file);
	}

	public static File getTmpDir() {
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		if (tmpDir.exists() == false || tmpDir.isDirectory() == false) {
			throw new IllegalStateException();
		}
		return tmpDir;
	}

	public static void delete(File f) {
		if (f == null) {
			return;
		}
		Thread.yield();
		Log.info("Deleting temporary file " + f);
		final boolean ok = f.delete();
		if (ok == false) {
			Log.error("Cannot delete: " + f);
		}
	}
	
	static public File createTempFile(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false) {
			throw new IllegalArgumentException();
		}
		final File f = File.createTempFile(prefix, suffix);
		Log.info("Creating temporary file: " + f);
		if (OptionFlags.getInstance().isKeepTmpFiles() == false) {
			f.deleteOnExit();
		}
		return f;
	}



}
