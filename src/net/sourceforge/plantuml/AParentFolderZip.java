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

public class AParentFolderZip implements AParentFolder {

	private final File zipFile;
	private final String parent;

	@Override
	public String toString() {
		return "AParentFolderZip::" + zipFile + " " + parent;
	}

	public AParentFolderZip(File zipFile, String entry) {
		this.zipFile = zipFile;
		final int idx = entry.lastIndexOf('/');
		if (idx == -1) {
			parent = "";
		} else {
			parent = entry.substring(0, idx + 1);
		}
	}

	public AFile getAFile(String nameOrPath) throws IOException {
		return new AFileZipEntry(zipFile, merge(parent + nameOrPath));
	}

	String merge(String full) {
		// full = full.replaceFirst("\\.", "Z");
		while (true) {
			int len = full.length();
			full = full.replaceFirst("[^/]+/\\.\\./", "");
			if (full.length() == len) {
				return full;
			}
		}
	}

}
