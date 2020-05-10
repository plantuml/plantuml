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
package net.sourceforge.plantuml.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Tokens {

	private final List<String> keywords = new ArrayList<String>();

	public static void main(String[] args) {
		System.err.println("keywords=" + new Tokens().keywords.size());
		final Set<String> sorted = new TreeSet<String>(new Tokens().keywords);
		for (String s : sorted) {
			System.err.println(s);
		}
	}

	public String compressUnicodeE000(String s) {
		for (int i = 0; i < keywords.size(); i++) {
			final char c = (char) ('\uE000' + i);
			s = s.replace(keywords.get(i), "" + c);
		}
		return s;
	}

	public String compressAscii128(String s) {
		for (int i = 0; i < keywords.size(); i++) {
			final char c = (char) (128 + i);
			s = s.replace(keywords.get(i), "" + c);
		}
		return s;
	}

	public Tokens() {
		add("actor");
		add("participant");
		add("usecase");
		add("class");
		add("interface");
		add("abstract");
		add("enum");
		add("component");
		add("state");
		add("object");
		add("artifact");
		add("folder");
		add("rectangle");
		add("node");
		add("frame");
		add("cloud");
		add("database");
		add("storage");
		add("agent");
		add("stack");
		add("boundary");
		add("control");
		add("entity");
		add("card");
		add("file");
		add("package");
		add("queue");
		add("archimate");
		add("diamond");
		add("detach");

		add("@start");
		add("@end");
		add("also");
		add("autonumber");
		add("caption");
		add("title");
		add("newpage");
		add("loop");
		add("break");
		add("critical");
		add("note");
		add("legend");
		add("group");
		add("left");
		add("right");
		add("link");
		add("over");
		add("activate");
		add("deactivate");
		add("destroy");
		add("create");
		add("footbox");
		add("hide");
		add("show");
		add("skinparam");
		add("skin");
		add("bottom");
		add("namespace");
		add("page");
		add("down");
		add("else");
		add("endif");
		add("partition");
		add("footer");
		add("header");
		add("center");
		add("rotate");
		add("return");
		add("repeat");
		add("start");
		add("stop");
		add("while");
		add("endwhile");
		add("fork");
		add("again");
		add("kill");
		add("order");
		add("mainframe");
		add("across");
		add("stereotype");
		add("split");
		add("style");
		add("sprite");

		add("exit");
		add("include");
		add("pragma");
		add("undef");
		add("ifdef");
		// add("endif");
		add("ifndef");
		// add("else");
		add("function");
		add("procedure");
		add("endfunction");
		add("endprocedure");
		add("unquoted");
		// add("return");
		add("startsub");
		add("endsub");
		add("assert");
		add("local");

		add("!definelong");
		add("!enddefinelong");
		add("!define");

		add("define");
		add("alias");
		add("shape");
		add("label");
		add("BackgroundColor");
		add("Color");
		add("color");
		add("Entity");
		add("ENTITY");
		add("COLOR");
		add("LARGE");
		add("stereo");
		add("AZURE");
		add("Azure");

	}

	private void add(String string) {
		if (keywords.contains(string)) {
			System.err.println(string);
			throw new IllegalArgumentException(string);
		}
		if (string.length() <= 3) {
			System.err.println(string);
			throw new IllegalArgumentException(string);
		}
		if (string.matches("[!@]?[A-Za-z]+") == false) {
			System.err.println(string);
			throw new IllegalArgumentException(string);
		}
		keywords.add(string);
		if (keywords.size() > 127) {
			System.err.println(string);
			throw new IllegalArgumentException();
		}
	}

}
