/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 4952 $
 * 
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.Preprocessor;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;

final public class BlockUmlBuilder {

	private final List<BlockUml> blocks = new ArrayList<BlockUml>();
	private final Set<File> usedFiles = new HashSet<File>();

	public BlockUmlBuilder(List<String> config, Defines defines, Reader reader) throws IOException {
		Preprocessor includer = null;
		try {
			includer = new Preprocessor(new UncommentReadLine(new ReadLineReader(reader)), defines, usedFiles);
			init(includer, config);
		} finally {
			if (includer != null) {
				includer.close();
			}
		}
	}

	public static boolean isArobaseEnduml(String s) {
		s = s.trim();
		return s.equals("@enduml") || s.startsWith("@enduml ");
	}

	private boolean isIgnoredLine(final String s) {
		// return s.length() == 0 || s.startsWith("#") || s.startsWith("'");
		// return s.length() == 0 || s.startsWith("'");
		return s.startsWith("'");
	}

	public static boolean isArobaseStartuml(String s) {
		s = s.trim();
		return s.equals("@startuml") || s.startsWith("@startuml ");
	}

	private void init(Preprocessor includer, List<String> config) throws IOException {
		String s = null;
		List<String> current = null;
		while ((s = includer.readLine()) != null) {
			if (isArobaseStartuml(s)) {
				current = new ArrayList<String>();
			}
			if (current != null && isIgnoredLine(s.trim()) == false) {
				current.add(s);
			}
			if (isArobaseEnduml(s) && current != null) {
				current.addAll(1, config);
				blocks.add(new BlockUml(current));
				current = null;
			}
		}
	}

	public List<BlockUml> getBlockUmls() {
		return Collections.unmodifiableList(blocks);
	}

	public final Set<File> getIncludedFiles() {
		return Collections.unmodifiableSet(usedFiles);
	}

	/*
	 * private List<String> getStrings(Reader reader) throws IOException {
	 * final List<String> result = new ArrayList<String>(); Preprocessor
	 * includer = null; try { includer = new Preprocessor(reader, defines);
	 * String s = null; while ((s = includer.readLine()) != null) {
	 * result.add(s); } } finally { if (includer != null) { includer.close(); } }
	 * return Collections.unmodifiableList(result); }
	 */
}
