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
 * Revision $Revision: 4780 $
 *
 */
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.core.Diagram;

public class BlockUml {

	private final List<? extends CharSequence> data;
	private Diagram system;

	// private static final Pattern patternFilename =
	// Pattern.compile("^@start\\S+\\s+\"?(.*?)\"?$");
	private static final Pattern patternFilename = Pattern
			.compile("^@start[^\\s{}\"]+[\\s{][\\s\"]*([^\"]*?)[\\s}\"]*$");

	BlockUml(String... strings) {
		this(Arrays.asList(strings));
	}

	public BlockUml(List<? extends CharSequence> strings) {
		final String s0 = strings.get(0).toString().trim();
		if (s0.startsWith("@start") == false) {
			throw new IllegalArgumentException();
		}
		this.data = new ArrayList<CharSequence>(strings);
	}

	public String getFilename() {
		if (OptionFlags.getInstance().isWord()) {
			return null;
		}
		final Matcher m = patternFilename.matcher(data.get(0).toString().trim());
		final boolean ok = m.find();
		if (ok == false) {
			return null;
		}
		final String result = m.group(1);
		for (int i = 0; i < result.length(); i++) {
			final char c = result.charAt(i);
			if ("<>|".indexOf(c) != -1) {
				return null;
			}
		}
		return result;
	}

	private Diagram getSystem() {
		if (system == null) {
			createSystem();
		}
		return system;
	}

	public Diagram getDiagram() {
		return getSystem();
	}

	private void createSystem() {
		system = new PSystemBuilder().createPSystem(data);
	}

}
