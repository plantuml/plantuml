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
 * Modified by: Nicolas Jouanin
 * 
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.sourceforge.plantuml.file.AFile;
import net.sourceforge.plantuml.file.AFileRegular;
import net.sourceforge.plantuml.file.AParentFolder;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.Log;

public class FileWithSuffix {

	private final AFile file;
	private final String suffix;
	private final String entry;
	private final String description;

	@Override
	public String toString() {
		if (file == null) {
			return super.toString();
		}
		return file.toString();
	}

	public Reader getReader(Charset charset) throws IOException {
		if (file == null) {
			return null;
		}
		final InputStream tmp = file.openFile();
		if (tmp == null) {
			return null;
		}
		if (entry == null) {
			return new InputStreamReader(tmp, charset);
		}
		final InputStream is = getDataFromZip(tmp, entry);
		if (is == null) {
			return null;
		}
		return new InputStreamReader(is, charset);
	}

	private InputStream getDataFromZip(InputStream is, String name) throws IOException {
		final ZipInputStream zis = new ZipInputStream(is);
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			final String fileName = ze.getName();
			if (ze.isDirectory()) {
			} else if (fileName.equals(name)) {
				return zis;
			}
			ze = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
		return null;
	}

	public boolean fileOk() {
		return file != null && file.isOk();
	}

	FileWithSuffix(SFile file, String suffix) {
		this.file = new AFileRegular(file);
		this.suffix = suffix;
		this.entry = null;
		this.description = file.getName();
	}

	FileWithSuffix(String description, String suffix, AFile file, String entry) {
		this.description = description;
		this.suffix = suffix;
		this.file = file;
		this.entry = entry;
	}

	static FileWithSuffix none() {
		return new FileWithSuffix("NONE", null, null, null);
	}

	@Override
	public int hashCode() {
		int v = 0;
		if (file != null) {
			v += file.hashCode();
		}
		if (suffix != null) {
			v += suffix.hashCode() * 43;
		}
		if (entry != null) {
			v += entry.hashCode();
		}
		return v;
	}

	@Override
	public boolean equals(Object arg) {
		final FileWithSuffix other = (FileWithSuffix) arg;
		return this.file.equals(other.file) && equals(suffix, other.suffix) && same(entry, other.entry);
	}

	private static boolean same(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 != null) {
			return s1.equals(s2);
		}
		return false;
	}

	public AParentFolder getParentFile() {
		if (file == null) {
			return null;
		}
		Log.info(() -> "Getting parent of " + file);
		Log.info(() -> "-->The parent is " + file.getParentFile());
		return file.getParentFile();
	}

	public String getDescription() {
		return description;
	}

	public final String getSuffix() {
		return suffix;
	}

	private static boolean equals(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}
		if (s1 != null && s2 != null) {
			return s1.equals(s2);
		}
		assert (s1 == null && s2 != null) || (s1 != null && s2 == null);
		return false;
	}

	public static Set<File> convert(Set<FileWithSuffix> all) throws FileNotFoundException {
		final Set<File> result = new HashSet<>();
		for (FileWithSuffix f : all) {
			result.add(f.file.getUnderlyingFile().conv());
		}
		return result;
	}

	public static String getFileName(File file) {
		return file.getName();
	}

}
