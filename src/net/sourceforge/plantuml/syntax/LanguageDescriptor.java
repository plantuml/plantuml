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
package net.sourceforge.plantuml.syntax;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.utils.Cypher;

public class LanguageDescriptor {

	private final Set<String> type = new TreeSet<>();
	private final Set<String> keyword = new TreeSet<>();
	private final Set<String> preproc = new TreeSet<>();

	public LanguageDescriptor() {

		type.add("actor");
		type.add("participant");
		type.add("usecase");
		type.add("class");
		type.add("interface");
		type.add("abstract");
		type.add("enum");
		type.add("component");
		type.add("state");
		type.add("object");
		type.add("artifact");
		type.add("folder");
		type.add("rectangle");
		type.add("node");
		type.add("frame");
		type.add("cloud");
		type.add("database");
		type.add("storage");
		type.add("agent");
		type.add("stack");
		type.add("boundary");
		type.add("control");
		type.add("entity");
		type.add("card");
		type.add("file");
		type.add("package");
		type.add("queue");
		type.add("archimate");
		type.add("diamond");
		type.add("collections");
		type.add("hexagon");
		type.add("label");
		type.add("person");
		type.add("annotation");

		keyword.add("@startwire");
		keyword.add("@startbpm");
		keyword.add("@startuml");
		keyword.add("@startdot");
		keyword.add("@startjcckit");
		keyword.add("@startditaa");
		keyword.add("@startproject");
		keyword.add("@startsalt");
		keyword.add("@startflow");
		keyword.add("@startcreole");
		keyword.add("@starttree");
		keyword.add("@startcute");
		keyword.add("@startmath");
		keyword.add("@startlatex");
		keyword.add("@startdef");
		keyword.add("@startgantt");
		keyword.add("@startnwdiag");
		keyword.add("@startmindmap");
		keyword.add("@startwbs");
		keyword.add("@startjson");
		keyword.add("@startgit");
		keyword.add("@startboard");
		keyword.add("@startyaml");
		keyword.add("@endwire");
		keyword.add("@endbpm");
		keyword.add("@enduml");
		keyword.add("@enddot");
		keyword.add("@endjcckit");
		keyword.add("@endditaa");
		keyword.add("@endproject");
		keyword.add("@endsalt");
		keyword.add("@endflow");
		keyword.add("@endcreole");
		keyword.add("@endtree");
		keyword.add("@endcute");
		keyword.add("@endmath");
		keyword.add("@endlatex");
		keyword.add("@enddef");
		keyword.add("@endgantt");
		keyword.add("@endnwdiag");
		keyword.add("@endmindmap");
		keyword.add("@endwbs");
		keyword.add("@endjson");
		keyword.add("@endgit");
		keyword.add("@endboard");
		keyword.add("@endyaml");
		keyword.add("as");
		keyword.add("also");
		keyword.add("autonumber");
		keyword.add("caption");
		keyword.add("title");
		keyword.add("newpage");
		keyword.add("box");
		keyword.add("alt");
		keyword.add("else");
		keyword.add("opt");
		keyword.add("loop");
		keyword.add("par");
		keyword.add("break");
		keyword.add("critical");
		keyword.add("note");
		keyword.add("rnote");
		keyword.add("hnote");
		keyword.add("legend");
		keyword.add("group");
		keyword.add("left");
		keyword.add("right");
		keyword.add("of");
		keyword.add("on");
		keyword.add("link");
		keyword.add("over");
		keyword.add("end");
		keyword.add("activate");
		keyword.add("deactivate");
		keyword.add("destroy");
		keyword.add("create");
		keyword.add("footbox");
		keyword.add("hide");
		keyword.add("show");
		keyword.add("skinparam");
		keyword.add("skin");
		keyword.add("top");
		keyword.add("bottom");
		keyword.add("top to bottom direction");
		keyword.add("left to right direction");
		keyword.add("package");
		keyword.add("namespace");
		keyword.add("page");
		keyword.add("up");
		keyword.add("down");
		keyword.add("if");
		keyword.add("then");
		keyword.add("else");
		keyword.add("elseif");
		keyword.add("endif");
		keyword.add("partition");
		keyword.add("footer");
		keyword.add("header");
		keyword.add("center");
		keyword.add("rotate");
		keyword.add("ref");
		keyword.add("return");
		keyword.add("is");
		keyword.add("repeat");
		keyword.add("start");
		keyword.add("stop");
		keyword.add("while");
		keyword.add("endwhile");
		keyword.add("fork");
		keyword.add("again");
		keyword.add("kill");
		keyword.add("detach");
		keyword.add("order");
		keyword.add("allow_mixing");
		keyword.add("allowmixing");
		keyword.add("mainframe");
		keyword.add("across");
		keyword.add("stereotype");
		keyword.add("split");
		keyword.add("style");
		keyword.add("sprite");		
		keyword.add("circle");
		keyword.add("empty");
		keyword.add("members");
		keyword.add("description");
		keyword.add("true");
		keyword.add("false");
		keyword.add("normal");
		keyword.add("italic");
		keyword.add("bold");
		keyword.add("plain");
		keyword.add("color");
		keyword.add("dotted");
		keyword.add("dashed");
		keyword.add("bold");
		keyword.add("map");


		preproc.add("!exit");
		preproc.add("!include");
		preproc.add("!theme");
		preproc.add("!pragma");
		preproc.add("!define");
		preproc.add("!undef");
		preproc.add("!if");
		preproc.add("!ifdef");
		preproc.add("!endif");
		preproc.add("!ifndef");
		preproc.add("!else");
		preproc.add("!definelong");
		preproc.add("!enddefinelong");
		preproc.add("!function");
		preproc.add("!procedure");
		preproc.add("!endfunction");
		preproc.add("!endprocedure");
		preproc.add("!unquoted");
		preproc.add("!return");
		preproc.add("!startsub");
		preproc.add("!endsub");
		preproc.add("!assert");
		preproc.add("!log");
		preproc.add("!local");
		preproc.add("!dump_memory");
		preproc.add("!import");
	}

	public Cypher getCypher() {
		final Cypher cypher = new Cypher();
		for (String s : type) {
			cypher.addException(s);
		}
		for (String s : keyword) {
			cypher.addException(s.replace("@", ""));
		}
		for (String s : preproc) {
			cypher.addException(s.substring(1));
		}
		for (String s : SkinParam.getPossibleValues()) {
			cypher.addException(s);
		}
		for (String s : HColorSet.instance().names()) {
			cypher.addException(s);
		}
		cypher.addException("o");
		return cypher;
	}

	public void print(PrintStream ps) {
		print(ps, "type", type);
		print(ps, "keyword", keyword);
		print(ps, "preprocessor", preproc);
		print(ps, "skinparameter", SkinParam.getPossibleValues());
		print(ps, "color", HColorSet.instance().names());
		ps.println(";EOF");
	}

	private static void print(PrintStream ps, String name, Collection<String> data) {
		ps.println(";" + name);
		ps.println(";" + data.size());
		for (String k : data) {
			ps.println(k);
		}
		ps.println();
	}

	public final Set<String> getType() {
		return Collections.unmodifiableSet(type);
	}

	public final Set<String> getKeyword() {
		return Collections.unmodifiableSet(keyword);
	}

	public final Set<String> getPreproc() {
		return Collections.unmodifiableSet(preproc);
	}

}
