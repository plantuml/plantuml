/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * 
 * Revision $Revision: 9911 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor implements ReadLine {

	private static final Pattern definePattern = Pattern.compile("^\\s*!define\\s+([A-Za-z_][A-Za-z_0-9]*(?:\\([A-Za-z_][A-Za-z_0-9]*\\))?)(?:\\s+(.*))?$");
	private static final Pattern undefPattern = Pattern.compile("^\\s*!undef\\s+([A-Za-z_][A-Za-z_0-9]*)$");

	private final Defines defines;
	private final PreprocessorInclude rawSource;
	private final IfManager source;

	public Preprocessor(ReadLine reader, String charset, Defines defines, Set<File> filesUsed, File newCurrentDir) {
		this.defines = defines;
		this.rawSource = new PreprocessorInclude(reader, charset, filesUsed, newCurrentDir);
		this.source = new IfManager(rawSource, defines);
	}

	public String readLine() throws IOException {
		String s = source.readLine();
		if (s == null) {
			return null;
		}

		Matcher m = definePattern.matcher(s);
		if (m.find()) {
			return manageDefine(m);
		}

		m = undefPattern.matcher(s);
		if (m.find()) {
			return manageUndef(m);
		}

		s = defines.applyDefines(s);
		return s;
	}

	private String manageUndef(Matcher m) throws IOException {
		defines.undefine(m.group(1));
		return this.readLine();
	}

	private String manageDefine(Matcher m) throws IOException {
		defines.define(m.group(1), m.group(2));
		return this.readLine();
	}

	public int getLineNumber() {
		return rawSource.getLineNumber();
	}

	public void close() throws IOException {
		rawSource.close();
	}

}
