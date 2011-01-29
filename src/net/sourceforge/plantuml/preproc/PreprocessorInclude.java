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
 * Revision $Revision: 6070 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.FileSystem;

class PreprocessorInclude implements ReadLine {

	private static final Pattern includePattern = Pattern.compile("^!include\\s+\"?([^\"]+)\"?$");

	private final ReadLine reader2;
	private int numLine = 0;

	private PreprocessorInclude included = null;
	
	private final Set<File> filesUsed;

	public PreprocessorInclude(ReadLine reader, Set<File> filesUsed) {
		this.reader2 = reader;
		this.filesUsed = filesUsed;
	}

	public String readLine() throws IOException {
		if (included != null) {
			final String s = included.readLine();
			if (s != null) {
				return s;
			}
			included.close();
			included = null;
		}

		final String s = reader2.readLine();
		numLine++;
		if (s == null) {
			return null;
		}
		final Matcher m = includePattern.matcher(s);
		assert included == null;
		if (m.find()) {
			return manageInclude(m);
		}

		return s;

	}

	private String manageInclude(Matcher m) throws IOException, FileNotFoundException {
		final String fileName = m.group(1);
		final File f = FileSystem.getInstance().getFile(fileName);
		if (f.exists()) {
			filesUsed.add(f);
			included = new PreprocessorInclude(new ReadLineReader(new FileReader(f)), filesUsed);
		}
		return this.readLine();
	}

	public int getLineNumber() {
		return numLine;
	}

	public void close() throws IOException {
		reader2.close();
	}

}
