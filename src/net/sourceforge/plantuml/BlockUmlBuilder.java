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

import static java.util.Objects.requireNonNull;
import static net.sourceforge.plantuml.utils.CharsetUtils.charsetOrDefault;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
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
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.StartUtils;

public final class BlockUmlBuilder implements DefinitionsContainer {

	private final List<BlockUml> blocks = new ArrayList<>();
	private Set<FileWithSuffix> usedFiles = new HashSet<>();
	private final UncommentReadLine reader;
	private final Defines defines;
	private final ImportedFiles importedFiles;
	private final Charset charset;

	/**
	 * @deprecated being kept for backwards compatibility, perhaps other projects are using this? 
	 */
	@Deprecated
	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader readerInit, SFile newCurrentDir,
			String desc) throws IOException {
		
		this(config, charsetOrDefault(charset), defines, readerInit, newCurrentDir, desc);
	}
	
	public BlockUmlBuilder(List<String> config, Charset charset, Defines defines, Reader readerInit, SFile newCurrentDir,
			String desc) throws IOException {
		
		this.defines = defines;
		this.charset = requireNonNull(charset);
		this.reader = new UncommentReadLine(ReadLineReader.create(readerInit, desc));
		this.importedFiles = ImportedFiles.createImportedFiles(new AParentFolderRegular(newCurrentDir));
		
		try (ReadLineNumbered includer = new Preprocessor(config, reader)) {
			init(includer);
		} finally {
			readerInit.close();
		}
	}

	/**
	 * @deprecated being kept for backwards compatibility, perhaps other projects are using this? 
	 */
	@Deprecated
	public BlockUmlBuilder(List<String> config, String charset, Defines defines, Reader reader) throws IOException {
		this(config, charset, defines, reader, null, null);
	}

	private void init(ReadLineNumbered includer) throws IOException {
		StringLocated s = null;
		List<StringLocated> current = null;
		boolean paused = false;

		while ((s = includer.readLine()) != null) {
			if (StartUtils.isArobaseStartDiagram(s.getString())) {
				current = new ArrayList<>();
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
			if (current != null && paused == false) {
				current.add(s);
			} else if (paused) {
				final StringLocated append = StartUtils.getPossibleAppend(s);
				if (append != null) {
					current.add(append);
				}
			}

			if (StartUtils.isArobaseUnpauseDiagram(s.getString())) {
				paused = false;
				reader.setPaused(false);
			}
			if (StartUtils.isArobaseEndDiagram(s.getString()) && current != null) {
				if (paused) {
					current.add(s);
				}
				final BlockUml uml = new BlockUml(current, defines.cloneMe(), null, this, charset);
				usedFiles.addAll(uml.getIncluded());
				blocks.add(uml);
				current = null;
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

	/**
	 * @deprecated being kept for backwards compatibility, perhaps other projects are using this? 
	 */
	@Deprecated
	public final String getCharset() {
		return charset.name();
	}

}
