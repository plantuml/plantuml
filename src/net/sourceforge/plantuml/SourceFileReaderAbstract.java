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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.preproc.FileWithSuffix;

public abstract class SourceFileReaderAbstract {

	protected File file;
	protected File outputDirectory;
	protected File outputFile;

	protected BlockUmlBuilder builder;
	protected FileFormatOption fileFormatOption;
	private boolean checkMetadata;

	public void setCheckMetadata(boolean checkMetadata) {
		this.checkMetadata = checkMetadata;
	}

	public boolean hasError() {
		for (final BlockUml b : builder.getBlockUmls()) {
			if (b.getDiagram() instanceof PSystemError) {
				return true;
			}
		}
		return false;
	}

	public List<BlockUml> getBlocks() {
		return builder.getBlockUmls();
	}

	protected Reader getReader(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(new FileInputStream(file));
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(new FileInputStream(file), charset);
	}

	public final Set<FileWithSuffix> getIncludedFiles() {
		return builder.getIncludedFiles();
	}

	public final void setFileFormatOption(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
	}

	protected boolean endsWithSlashOrAntislash(String newName) {
		return newName.endsWith("/") || newName.endsWith("\\");
	}

	private List<GeneratedImage> getCrashedImage(BlockUml blockUml, Throwable t, File outputFile) throws IOException {
		final GeneratedImage image = new GeneratedImageImpl(outputFile, "Crash Error", blockUml, FileImageData.CRASH);
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(outputFile));
			UmlDiagram.exportDiagramError(os, t, fileFormatOption, 42, null, blockUml.getFlashData(),
					UmlDiagram.getFailureText2(t, blockUml.getFlashData()));
		} finally {
			if (os != null) {
				os.close();
			}
		}

		return Collections.singletonList(image);
	}

	protected void exportWarnOrErrIfWord(final File f, final Diagram system) throws FileNotFoundException {
		if (OptionFlags.getInstance().isWord()) {
			final String warnOrError = system.getWarningOrError();
			if (warnOrError != null) {
				final String name = f.getName().substring(0, f.getName().length() - 4) + ".err";
				final File errorFile = new File(f.getParentFile(), name);
				final PrintStream ps = new PrintStream(new FileOutputStream(errorFile));
				ps.print(warnOrError);
				ps.close();
			}
		}
	}

	protected int cpt;

	final public List<GeneratedImage> getGeneratedImages() throws IOException {
		Log.info("Reading file: " + file);

		cpt = 0;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		for (BlockUml blockUml : builder.getBlockUmls()) {
			final SuggestedFile suggested = getSuggestedFile(blockUml);

			final Diagram system;
			try {
				system = blockUml.getDiagram();
			} catch (Throwable t) {
				t.printStackTrace();
				return getCrashedImage(blockUml, t, suggested.getFile(0));
			}

			if (OptionFlags.getInstance().isSilentlyCompletelyIgnoreErrors() && system instanceof PSystemError) {
				continue;
			}

			OptionFlags.getInstance().logData(file, system);
			final List<FileImageData> exportDiagrams = PSystemUtils.exportDiagrams(system, suggested, fileFormatOption,
					checkMetadata);
			if (exportDiagrams.size() > 1) {
				cpt += exportDiagrams.size() - 1;
			}

			for (FileImageData fdata : exportDiagrams) {
				final String desc = "[" + file.getName() + "] " + system.getDescription();
				final File f = fdata.getFile();
				exportWarnOrErrIfWord(f, system);
				final GeneratedImage generatedImage = new GeneratedImageImpl(f, desc, blockUml, fdata.getStatus());
				result.add(generatedImage);
			}

		}

		Log.info("Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	abstract protected SuggestedFile getSuggestedFile(BlockUml blockUml);

}
