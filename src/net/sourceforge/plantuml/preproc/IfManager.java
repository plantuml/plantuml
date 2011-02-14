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
 * Revision $Revision: 6107 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IfManager implements ReadLine {

	protected static final Pattern ifdefPattern = Pattern.compile("^\\s*!if(n)?def\\s+([A-Za-z_][A-Za-z_0-9]*)$");
	protected static final Pattern elsePattern = Pattern.compile("^\\s*!else$");
	protected static final Pattern endifPattern = Pattern.compile("^\\s*!endif$");

	private final Defines defines;
	private final ReadLine source;

	private IfManager child;

	public IfManager(ReadLine source, Defines defines) {
		this.defines = defines;
		this.source = source;
	}

	final public String readLine() throws IOException {
		if (child != null) {
			final String s = child.readLine();
			if (s != null) {
				return s;
			}
			child = null;
		}

		return readLineInternal();
	}

	protected String readLineInternal() throws IOException {
		final String s = source.readLine();
		if (s == null) {
			return null;
		}

		final Matcher m = ifdefPattern.matcher(s);
		if (m.find()) {
			boolean ok = defines.isDefine(m.group(2));
			if (m.group(1) != null) {
				ok = !ok;
			}
			if (ok) {
				child = new IfManagerPositif(source, defines);
			} else {
				child = new IfManagerNegatif(source, defines);
			}
			// child = new IfManager(source, defines, ok ? IfPart.IF :
			// IfPart.SKIP);
			return this.readLine();
		}

		// m = endifPattern.matcher(s);
		// if (m.find()) {
		// return null;
		// }
		return s;
	}

	public void close() throws IOException {
		source.close();
	}

}
