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
package net.sourceforge.plantuml.command.regex;

import java.util.regex.Pattern;

// Splitter.java to be finished
public abstract class MyPattern {

	public static Pattern2 cmpile(String p) {
		p = transformAndCheck(p);
		return new Pattern2(Pattern.compile(p));
	}

	public static Pattern2 cmpileNockeck(String p) {
		p = transform(p);
		return new Pattern2(Pattern.compile(p));
	}

	public static Pattern2 cmpile(String p, int type) {
		p = transformAndCheck(p);
		return new Pattern2(Pattern.compile(p, type));
	}

	public static Pattern2 cmpileNockeck(String p, int type) {
		p = transform(p);
		return new Pattern2(Pattern.compile(p, type));
	}

	private static String transformAndCheck(String p) {
		p = transform(p);
		return p;
	}

	private static String transform(String p) {
		// Replace ReadLineReader.java
		p = p.replaceAll("%s", "\\\\s\u00A0"); // space
		p = p.replaceAll("%q", "'\u2018\u2019"); // quote
		p = p.replaceAll("%g", "\"\u201c\u201d\u00ab\u00bb"); // double quote
		return p;
	}

	public static boolean mtches(CharSequence input, String regex) {
		return cmpile(regex).matcher(input).matches();
	}

	public static CharSequence removeAll(CharSequence src, String regex) {
		return src.toString().replaceAll(transform(regex), "");
	}

}
