/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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

import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.utils.Cypher;

public class LanguageDescriptor {

	private final Set<String> type = new TreeSet<>();
	private final Set<String> keyword = new TreeSet<>();
	private final Set<String> preproc = new TreeSet<>();

	public LanguageDescriptor() {

		addArobase("board");
		addArobase("bpm");
		addArobase("chen");
		addArobase("chronology");
		addArobase("creole");
		addArobase("cute");
		addArobase("def");
		addArobase("ditaa");
		addArobase("dot");
		addArobase("ebnf");
		addArobase("files");
		addArobase("flow");
		addArobase("gantt");
		addArobase("git");
		addArobase("hcl");
		addArobase("jcckit");
		addArobase("json");
		addArobase("latex");
		addArobase("math");
		addArobase("mindmap");
		addArobase("nwdiag");
		addArobase("project");
		addArobase("regex");
		addArobase("salt");
		addArobase("tree");
		addArobase("uml");
		addArobase("wbs");
		addArobase("wire");
		addArobase("yaml");

		type.add("abstract");
		type.add("action");
		type.add("actor");
		type.add("agent");
		type.add("analog");
		type.add("annotation");
		type.add("archimate");
		type.add("artifact");
		type.add("binary");
		type.add("boundary");
		type.add("card");
		type.add("class");
		type.add("clock");
		type.add("cloud");
		type.add("collections");
		type.add("component");
		type.add("concise");
		type.add("control");
		type.add("database");
		type.add("dataclass");
		type.add("diamond");
		type.add("entity");
		type.add("enum");
		type.add("exception");
		type.add("file");
		type.add("folder");
		type.add("frame");
		type.add("hexagon");
		type.add("interface");
		type.add("json");
		type.add("label");
		type.add("map");
		type.add("metaclass");
		type.add("network");
		type.add("node");
		type.add("nwdiag");
		type.add("object");
		type.add("package");
		type.add("participant");
		type.add("person");
		type.add("process");
		type.add("protocol");
		type.add("queue");
		type.add("record");
		type.add("rectangle");
		type.add("relationship");
		type.add("robust");
		type.add("stack");
		type.add("state");
		type.add("storage");
		type.add("struct");
		type.add("usecase");

		keyword.add("across");
		keyword.add("activate");
		keyword.add("address");
		keyword.add("again");
		keyword.add("allow_mixing");
		keyword.add("allowmixing");
		keyword.add("also");
		keyword.add("alt");
		keyword.add("as");
		keyword.add("attribute");
		keyword.add("attributes");
		keyword.add("autonumber");
		keyword.add("backward");
		keyword.add("bold");
		keyword.add("bottom");
		keyword.add("box");
		keyword.add("break");
		keyword.add("case");
		keyword.add("caption");
		keyword.add("center");
		keyword.add("circle");
		keyword.add("circled");
		keyword.add("circles");
		keyword.add("color");
		keyword.add("create");
		keyword.add("critical");
		keyword.add("dashed");
		keyword.add("deactivate");
		keyword.add("description");
		keyword.add("destroy");
		keyword.add("detach");
		keyword.add("dotted");
		keyword.add("down");
		keyword.add("else");
		keyword.add("else");
		keyword.add("elseif");
		keyword.add("empty");
		keyword.add("end");
		keyword.add("endcaption");
		keyword.add("endfooter");
		keyword.add("endfork");
		keyword.add("endheader");
		keyword.add("endif");
		keyword.add("endlegend");
		keyword.add("endmerge");
		keyword.add("endswitch");
		keyword.add("endtitle");
		keyword.add("endwhile");
		keyword.add("false");
		keyword.add("field");
		keyword.add("fields");
		keyword.add("floating note");
		keyword.add("footbox");
		keyword.add("footer");
		keyword.add("fork");
		keyword.add("goto");
		keyword.add("group");
		keyword.add("header");
		keyword.add("hide");
		keyword.add("highlight");
		keyword.add("hnote");
		keyword.add("if");
		keyword.add("is");
		keyword.add("italic");
		keyword.add("kill");
		keyword.add("label");
		keyword.add("left");
		keyword.add("left to right direction");
		keyword.add("legend");
		keyword.add("link");
		keyword.add("loop");
		keyword.add("mainframe");
		keyword.add("member");
		keyword.add("members");
		keyword.add("merge");
		keyword.add("method");
		keyword.add("methods");
		keyword.add("namespace");
		keyword.add("newpage");
		keyword.add("normal");
		keyword.add("not");
		keyword.add("note");
		keyword.add("null");
		keyword.add("of");
		keyword.add("on");
		keyword.add("opt");
		keyword.add("order");
		keyword.add("over");
		keyword.add("package");
		keyword.add("page");
		keyword.add("par");
		keyword.add("partition");
		keyword.add("plain");
		keyword.add("private");
		keyword.add("protected");
		keyword.add("public");
		keyword.add("ref");
		keyword.add("repeat");
		keyword.add("repeatwhile");
		keyword.add("return");
		keyword.add("right");
		keyword.add("rnote");
		keyword.add("rotate");
		keyword.add("shape");
		keyword.add("show");
		keyword.add("skin");
		keyword.add("skinparam");
		keyword.add("split");
		keyword.add("sprite");
		keyword.add("start");
		keyword.add("stereotype");
		keyword.add("stereotypes");
		keyword.add("stop");
		keyword.add("style");
		keyword.add("switch");
		keyword.add("then");
		keyword.add("title");
		keyword.add("together");
		keyword.add("top");
		keyword.add("top to bottom direction");
		keyword.add("true");
		keyword.add("up");
		keyword.add("while");

		preproc.add("!assert");
		preproc.add("!define");
		preproc.add("!definelong");
		preproc.add("!dump_memory");
		preproc.add("!else");
		preproc.add("!end");
		preproc.add("!enddefinelong");
		preproc.add("!endfor");
		preproc.add("!endfunction");
		preproc.add("!endif");
		preproc.add("!endprocedure");
		preproc.add("!endsub");
		preproc.add("!endwhile");
		preproc.add("!exit");
		preproc.add("!final");
		preproc.add("!foreach");
		preproc.add("!function");
		preproc.add("!if");
		preproc.add("!ifdef");
		preproc.add("!ifndef");
		preproc.add("!import");
		preproc.add("!include");
		preproc.add("!includedef");
		preproc.add("!includesub");
		preproc.add("!local");
		preproc.add("!log");
		preproc.add("!option");
		preproc.add("!pragma");
		preproc.add("!procedure");
		preproc.add("!return");
		preproc.add("!startsub");
		preproc.add("!theme");
		preproc.add("!undef");
		preproc.add("!unquoted");
		preproc.add("!while");
	}

	private void addArobase(String type) {
		keyword.add("@start" + type);
		keyword.add("@end" + type);
	}

	public Cypher getCypher() {
		final Cypher cypher = new Cypher();
		for (String s : type)
			cypher.addException(s);

		for (String s : keyword)
			cypher.addException(s.replace("@", ""));

		for (String s : preproc)
			cypher.addException(s.substring(1));

		for (String s : SkinParam.getPossibleValues())
			cypher.addException(s);

		for (String s : HColorSet.instance().names())
			cypher.addException(s);

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
		for (String k : data)
			ps.println(k);

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

	public static void main(String[] args) {
		LanguageDescriptor languageDescriptor = new LanguageDescriptor();
		for (String s : languageDescriptor.type) {
			System.out.print(s);
			System.out.print("|");
		}
		System.out.println();
		for (String s : languageDescriptor.keyword) {
			if (s.startsWith("@"))
				continue;
			System.out.print(s);
			System.out.print("|");
		}
	}

}
