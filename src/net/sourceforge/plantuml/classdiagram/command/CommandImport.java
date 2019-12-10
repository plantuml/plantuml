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
package net.sourceforge.plantuml.classdiagram.command;

import java.io.File;
import java.io.IOException;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandImport extends SingleLineCommand2<ClassDiagram> {

	public CommandImport() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandImport.class.getName(), //
				RegexLeaf.start(), //
				new RegexLeaf("import"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("[%g]?"), //
				new RegexLeaf("NAME", "([^%g]+)"), //
				new RegexLeaf("[%g]?"), RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg) {
		final String arg0 = arg.get("NAME", 0);
		try {
			final File f = FileSystem.getInstance().getFile(arg0);

			if (f.isFile()) {
				includeSimpleFile(diagram, f);
			} else if (f.isDirectory()) {
				includeDirectory(diagram, f);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return CommandExecutionResult.error("IO error " + e);
		}
		return CommandExecutionResult.ok();
	}

	private void includeDirectory(ClassDiagram classDiagram, File dir) throws IOException {
		for (File f : dir.listFiles()) {
			includeSimpleFile(classDiagram, f);
		}

	}

	private void includeSimpleFile(ClassDiagram classDiagram, File f) throws IOException {
		if (StringUtils.goLowerCase(f.getName()).endsWith(".java")) {
			includeFileJava(classDiagram, f);
		}
		// if (f.getName().goLowerCase().endsWith(".sql")) {
		// includeFileSql(f);
		// }
	}

	private void includeFileJava(ClassDiagram diagram, final File f) throws IOException {
		final JavaFile javaFile = new JavaFile(f);
		for (JavaClass cl : javaFile.getJavaClasses()) {
			final String idShort = cl.getName();
			final Code name = diagram.buildCode(idShort);
			final IEntity ent1 = diagram.getOrCreateLeaf(diagram.buildLeafIdent(idShort), name, cl.getType(), null);

			for (String p : cl.getParents()) {
				final IEntity ent2 = diagram.getOrCreateLeaf(diagram.buildLeafIdent(p), diagram.buildCode(p), cl.getParentType(), null);
				final Link link = new Link(ent2, ent1, new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS), Display.NULL,
						2, diagram.getSkinParam().getCurrentStyleBuilder());
				diagram.addLink(link);
			}
		}
	}

	// private void includeFileSql(final File f) throws IOException {
	// new SqlImporter(getSystem(), f).process();
	// }

}
