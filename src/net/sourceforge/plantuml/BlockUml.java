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
 *
 * Revision $Revision: 4780 $
 *
 */
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.core.Diagram;

public class BlockUml {

	private final List<? extends CharSequence> data;
	private final int startLine;
	private Diagram system;

	private static final Pattern patternFilename = MyPattern.cmpile("^@start[^%s{}%g]+[%s{][%s%g]*([^%g]*?)[%s}%g]*$");

	BlockUml(String... strings) {
		this(Arrays.asList(strings), 0);
	}

	public BlockUml(List<? extends CharSequence> strings, int startLine) {
		this.startLine = startLine;
		final String s0 = StringUtils.trin(strings.get(0).toString());
		if (s0.startsWith("@start") == false) {
			throw new IllegalArgumentException();
		}
		this.data = new ArrayList<CharSequence>(strings);
	}

	public String getFilename() {
		if (OptionFlags.getInstance().isWord()) {
			return null;
		}
		final Matcher m = patternFilename.matcher(StringUtils.trin(data.get(0).toString()));
		final boolean ok = m.find();
		if (ok == false) {
			return null;
		}
		String result = m.group(1);
		final int x = result.indexOf(',');
		if (x != -1) {
			result = result.substring(0, x);
		}
		for (int i = 0; i < result.length(); i++) {
			final char c = result.charAt(i);
			if ("<>|".indexOf(c) != -1) {
				return null;
			}
		}
		return result;
	}

	public Diagram getDiagram() {
		if (system == null) {
			system = new PSystemBuilder().createPSystem(data);
		}
		return system;
	}

	public final int getStartLine() {
		return startLine;
	}

}
