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
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.ReadLineNumbered;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.preproc2.Preprocessor;
import net.sourceforge.plantuml.utils.StartUtils;

public final class BlockUmlBuilder implements DefinitionsContainer {

	private final List<BlockUml> blocks = new ArrayList<BlockUml>();
	private Set<FileWithSuffix> usedFiles = new HashSet<FileWithSuffix>();
	private final UncommentReadLine reader;
	private final Defines defines;
	private final ImportedFiles importedFiles;
	private final String charset;

	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader readerInit, File newCurrentDir,
			String desc) throws IOException {
		ReadLineNumbered includer = null;
		this.defines = defines;
		this.charset = charset;
		try {
			this.reader = new UncommentReadLine(ReadLineReader.create(readerInit, desc));
			this.importedFiles = ImportedFiles.createImportedFiles(new AParentFolderRegular(newCurrentDir));
			includer = new Preprocessor(config, reader);
			init(includer);
		} finally {
			if (includer != null) {
				includer.close();
				// usedFiles = includer.getFilesUsedTOBEREMOVED();
			}
			readerInit.close();
		}
	}

	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader reader) throws IOException {
		this(config, charset, defines, reader, null, null);
	}

	private void init(ReadLineNumbered includer) throws IOException {
		StringLocated s = null;
		List<StringLocated> current2 = null;
		boolean paused = false;

		while ((s = includer.readLine()) != null) {
			if (StartUtils.isArobaseStartDiagram(s.getString())) {
				current2 = new ArrayList<StringLocated>();
				paused = false;
			}
			if (StartUtils.isArobasePauseDiagram(s.getString())) {
				paused = true;
				reader.setPaused(true);
			}
			if (StartUtils.isExit(s.getString())) {
				paused = true;
				reader.setPaused(true);
			}
			if (current2 != null && paused == false) {
				current2.add(s);
			} else if (paused) {
				final StringLocated append = StartUtils.getPossibleAppend(s);
				if (append != null) {
					current2.add(append);
				}
			}

			if (StartUtils.isArobaseUnpauseDiagram(s.getString())) {
				paused = false;
				reader.setPaused(false);
			}
			if (StartUtils.isArobaseEndDiagram(s.getString()) && current2 != null) {
				if (paused) {
					current2.add(s);
				}
				final BlockUml uml = new BlockUml(current2, defines.cloneMe(), null, this);
				usedFiles.addAll(uml.getIncluded());
				blocks.add(uml);
				current2 = null;
				reader.setPaused(false);
			}
		}
	}

	public List<BlockUml> getBlockUmls() {
		return Collections.unmodifiableList(blocks);
	}

	public final Set<FileWithSuffix> getIncludedFiles() {
		return Collections.unmodifiableSet(usedFiles);
	}

	public List<String> getDefinition(String name) {
		for (BlockUml block : blocks) {
			if (block.isStartDef(name)) {
				return block.getDefinition(false);
			}
		}
		return Collections.emptyList();
	}

	public final ImportedFiles getImportedFiles() {
		return importedFiles;
	}

	public final String getCharset() {
		return charset;
	}

}
