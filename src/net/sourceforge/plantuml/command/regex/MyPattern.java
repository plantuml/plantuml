/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 4762 $
 *
 */
package net.sourceforge.plantuml.command.regex;

import java.util.regex.Pattern;

// Splitter.java to be finished
public abstract class MyPattern {

	public static Pattern cmpile(String p) {
		p = transformAndCheck(p);
		return Pattern.compile(p);
	}

	public static Pattern cmpileNockeck(String p) {
		p = transform(p);
		return Pattern.compile(p);
	}

	public static Pattern cmpile(String p, int type) {
		p = transformAndCheck(p);
		return Pattern.compile(p, type);
	}

	public static Pattern cmpileNockeck(String p, int type) {
		p = transform(p);
		return Pattern.compile(p, type);
	}

	private static String transformAndCheck(String p) {
		// if (p.contains("\\s")) {
		// Thread.dumpStack();
		// System.err.println(p);
		// System.exit(0);
		// }
		// if (p.contains("'")) {
		// Thread.dumpStack();
		// System.err.println(p);
		// System.exit(0);
		// }
		// if (p.contains("\"")) {
		// Thread.dumpStack();
		// System.err.println(p);
		// System.exit(0);
		// }
		p = transform(p);
		// if (p.contains(" ") || p.contains("%")) {
		// Thread.dumpStack();
		// System.err.println(p);
		// System.exit(0);
		// }
		return p;
	}

	private static String transform(String p) {
		// Replace ReadLineReader.java
		p = p.replaceAll("%s", "\\\\s\u00A0"); // space
		p = p.replaceAll("%q", "'\u2018\u2019"); // quote
		p = p.replaceAll("%g", "\"\u201c\u201d\u00ab\u00bb"); // double quote
		return p;
	}

	// public static boolean mtches(String input, String regex) {
	// return cmpile(regex).matcher(input).matches();
	// }
	//
	public static boolean mtches(CharSequence input, String regex) {
		return cmpile(regex).matcher(input).matches();
	}

	public static CharSequence removeAll(CharSequence src, String regex) {
		return src.toString().replaceAll(transform(regex), "");
	}

}
