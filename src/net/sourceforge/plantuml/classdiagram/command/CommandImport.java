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
 * Revision $Revision: 18280 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LinkDecor;
import net.sourceforge.plantuml.cucadiagram.LinkType;

public class CommandImport extends SingleLineCommand<ClassDiagram> {

	public CommandImport() {
		super("(?i)^import[%s]+[%g]?([^%g]+)[%g]?$");
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram classDiagram, List<String> arg) {
		final String arg0 = arg.get(0);
		try {
			final File f = FileSystem.getInstance().getFile(arg0);

			if (f.isFile()) {
				includeSimpleFile(classDiagram, f);
			} else if (f.isDirectory()) {
				includeDirectory(classDiagram, f);
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

	private void includeFileJava(ClassDiagram classDiagram, final File f) throws IOException {
		final JavaFile javaFile = new JavaFile(f);
		for (JavaClass cl : javaFile.getJavaClasses()) {
			final Code name = Code.of(cl.getName());
			final IEntity ent1 = classDiagram.getOrCreateLeaf(name, cl.getType(), null);

			for (String p : cl.getParents()) {
				final IEntity ent2 = classDiagram.getOrCreateLeaf(Code.of(p), cl.getParentType(), null);
				final Link link = new Link(ent2, ent1, new LinkType(LinkDecor.NONE, LinkDecor.EXTENDS), Display.NULL, 2);
				classDiagram.addLink(link);
			}
		}
	}

	// private void includeFileSql(final File f) throws IOException {
	// new SqlImporter(getSystem(), f).process();
	// }

}
