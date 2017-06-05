/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.preproc;

import java.io.IOException;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.version.Version;

class IfManager implements ReadLine {

	protected static final Pattern2 ifdefPattern = MyPattern.cmpile("^[%s]*!if(n)?def[%s]+(.+)$");
	protected static final Pattern2 ifcomparePattern = MyPattern
			.cmpile("^[%s]*!if[%s]+\\%(\\w+)\\%[%s]*(\\<|\\<=|\\>|\\>=|=|==|!=|\\<\\>)[%s]*(\\d+)$");
	protected static final Pattern2 elsePattern = MyPattern.cmpile("^[%s]*!else[%s]*$");
	protected static final Pattern2 endifPattern = MyPattern.cmpile("^[%s]*!endif[%s]*$");

	private final Defines defines;
	private final ReadLine source;

	private IfManager child;

	public IfManager(ReadLine source, Defines defines) {
		this.defines = defines;
		this.source = source;
	}

	final public CharSequence2 readLine() throws IOException {
		if (child != null) {
			final CharSequence2 s = child.readLine();
			if (s != null) {
				return s;
			}
			child = null;
		}

		return readLineInternal();
	}

	protected CharSequence2 readLineInternal() throws IOException {
		final CharSequence2 s = source.readLine();
		if (s == null) {
			return null;
		}

		Matcher2 m = ifcomparePattern.matcher(s);
		if (m.find()) {
			final int value1 = getValue(m.group(1));
			final String operator = m.group(2);
			final int value2 = Integer.parseInt(m.group(3));
			final boolean ok = new NumericCompare(operator).isCompareOk(value1, value2);
			if (ok) {
				child = new IfManagerPositif(source, defines);
			} else {
				child = new IfManagerNegatif(source, defines);
			}
			return this.readLine();
		}

		m = ifdefPattern.matcher(s);
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
			return this.readLine();
		}

		return s;
	}

	private int getValue(final String arg) {
		if (arg.equalsIgnoreCase("PLANTUML_VERSION")) {
			return Version.versionPatched();
		}
		return 0;
	}

	public void close() throws IOException {
		source.close();
	}

}
