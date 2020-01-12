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
package net.sourceforge.plantuml.preproc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.LineLocationImpl;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringLocated;

public class ReadLineReader implements ReadLine {

	private final BufferedReader br;
	private LineLocationImpl location;
	private final String description;

	private ReadLineReader(Reader reader, String description, LineLocation parent) {
		if (description == null) {
			description = "?";
		}
		this.br = new BufferedReader(reader);
		this.location = new LineLocationImpl(description, parent);
		this.description = description;
		Log.info("Reading from " + description);
	}

	@Override
	public String toString() {
		return super.toString() + " " + description;
	}

	private ReadLineReader(Reader reader, String desc) {
		this(reader, desc, null);
	}

	public static ReadLine create(Reader reader, String description) {
		return new ReadLineReader(reader, description, null);
	}

	public static ReadLine create(Reader reader, String description, LineLocation parent) {
		return new ReadLineReader(reader, description, parent);
	}

	public StringLocated readLine() throws IOException {
		String s = br.readLine();
		location = location.oneLineRead();
		if (s == null) {
			return null;
		}
		// if (s.length() > LIMIT) {
		// Log.debug("Line truncated from " + s.length() + " to " + LIMIT);
		// s = s.substring(0, LIMIT);
		// }
		if (s.startsWith("\uFEFF")) {
			s = s.substring(1);
		}
		s = s.replace('\u2013', '-');
		// s = BackSlash.convertHiddenNewLine(s);
		// s = s.replace('\u00A0', ' ');
		// s = s.replace('\u201c', '\"');
		// s = s.replace('\u201d', '\"');
		// s = s.replace('\u00ab', '\"');
		// s = s.replace('\u00bb', '\"');
		// s = s.replace('\u2018', '\'');
		// s = s.replace('\u2019', '\'');
		// for (int i = 0; i < s.length(); i++) {
		// char c = s.charAt(i);
		// System.err.println("X " + Integer.toHexString((int) c) + " " + c);
		// }
		return new StringLocated(s, location);
	}

	public void close() throws IOException {
		br.close();
	}

}
