/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Modified by: Nicolas Jouanin
 * 
 * Revision $Revision: 16359 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileWithSuffix {

	private final File file;
	private final String suffix;

	public FileWithSuffix(File file, String suffix) {
		this.file = file;
		this.suffix = suffix;
	}

	@Override
	public int hashCode() {
		return file.hashCode() + (suffix == null ? 0 : suffix.hashCode() * 43);
	}

	@Override
	public boolean equals(Object arg) {
		final FileWithSuffix other = (FileWithSuffix) arg;
		return this.file.equals(other.file) && equals(suffix, other.suffix);
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

	public static Set<File> convert(Set<FileWithSuffix> all) {
		final Set<File> result = new HashSet<File>();
		for (FileWithSuffix f : all) {
			result.add(f.file);
		}
		return result;
	}

}
