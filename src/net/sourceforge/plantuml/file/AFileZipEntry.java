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
package net.sourceforge.plantuml.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.security.SFile;

public class AFileZipEntry implements AFile {
    // ::remove folder when __HAXE__

	private final SFile zipFile;
	private final String entry;

	public AFileZipEntry(SFile file, String entry) {
		this.zipFile = file;
		this.entry = entry;
	}

	@Override
	public String toString() {
		return "AFileZipEntry::" + zipFile.getAbsolutePath() + " " + entry;
	}

	public InputStream openFile() {
		final InputStream tmp = zipFile.openFile();
		if (tmp != null)
			try {
				final ZipInputStream zis = new ZipInputStream(tmp);
				ZipEntry ze = zis.getNextEntry();

				while (ze != null) {
					final String fileName = ze.getName();
					if (ze.isDirectory()) {
					} else if (fileName.trim().equalsIgnoreCase(entry.trim())) {
						return zis;
					}
					ze = zis.getNextEntry();
				}
				zis.closeEntry();
				zis.close();
			} catch (IOException e) {
				Logme.error(e);
			}
		return null;
	}

	public boolean isOk() {
		if (zipFile.exists() && zipFile.isDirectory() == false) {
			final InputStream is = openFile();
			if (is != null) {
				try {
					is.close();
					return true;
				} catch (IOException e) {
					Logme.error(e);
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return zipFile.hashCode() + entry.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AFileZipEntry == false) {
			return false;
		}
		final AFileZipEntry other = (AFileZipEntry) obj;
		return this.zipFile.equals(other.zipFile) && this.entry.equals(other.entry);
	}

	public AParentFolder getParentFile() {
		return new AParentFolderZip(zipFile, entry);
	}

	public SFile getUnderlyingFile() {
		return zipFile;
	}

	public SFile getSystemFolder() throws IOException {
		return zipFile.getParentFile().getCanonicalFile();
	}

}
