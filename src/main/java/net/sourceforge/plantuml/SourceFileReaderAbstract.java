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
package net.sourceforge.plantuml;

import static net.sourceforge.plantuml.utils.CharsetUtils.charsetOrDefault;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.crash.CrashReportHandler;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.utils.Log;

public abstract class SourceFileReaderAbstract implements ISourceFileReader {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	final private File file;

	private FileFormatOption fileFormatOption;
	private boolean checkMetadata;
	private boolean noerror;

	final private Charset charset;

	private final BlockUmlBuilder builder;
	private int cpt;

	protected final SuggestedFile getSuggestedFile(File outputDirectory, String newName) {
		final File outFile = new File(outputDirectory, newName);
		return SuggestedFile.fromOutputFile(outFile, getFileFormatOption().getFileFormat(), cpt++);
	}

	public SourceFileReaderAbstract(File file, FileFormatOption fileFormatOption, Defines defines, List<String> config,
			String charsetName) throws IOException {

		if (file.exists() == false)
			throw new IllegalArgumentException();

		this.file = file;
		this.charset = charsetOrDefault(charsetName);
		this.fileFormatOption = fileFormatOption;
		this.builder = new BlockUmlBuilder(config, charset, defines, getReader(charset),
				SFile.fromFile(file.getAbsoluteFile().getParentFile()), FileWithSuffix.getFileName(file));
	}

	protected final FileFormatOption getFileFormatOption() {
		return fileFormatOption;
	}

	public void setCheckMetadata(boolean checkMetadata) {
		this.checkMetadata = checkMetadata;
	}

	public boolean hasError() {
		for (final BlockUml b : builder.getBlockUmls())
			if (b.getDiagram() instanceof PSystemError)
				return true;
		return false;
	}

	public List<BlockUml> getBlocks() {
		return builder.getBlockUmls();
	}

	protected Reader getReader(Charset charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset);
	}

	public final Set<FileWithSuffix> getIncludedFiles() throws IOException {
		return builder.getIncludedFiles();
	}

	@Override
	public final ISourceFileReader setFileFormatOption(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
		return this;
	}

	protected boolean endsWithSlashOrAntislash(String newName) {
		return newName.endsWith("/") || newName.endsWith("\\");
	}

	private List<GeneratedImage> getCrashedImage(BlockUml blockUml, Throwable t, SFile outputFile) throws IOException {
		final GeneratedImage image = new GeneratedImageImpl(outputFile, "Crash Error", blockUml, FileImageData.CRASH);
		try (OutputStream os = outputFile.createBufferedOutputStream()) {
			final String flash = blockUml.getFlashData();
			final CrashReportHandler report = new CrashReportHandler(t, null, flash);
			report.anErrorHasOccured(t, flash);
			report.youShouldSendThisDiagram();
			report.addEmptyLine();
			report.exportDiagramError(fileFormatOption, (long) 42, os);
		}
		return Collections.singletonList(image);
	}

	protected void exportWarnOrErrIfWord(SFile f, Diagram system) throws FileNotFoundException {
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.WORD) && f != null) {
			final String warnOrError = system.getWarningOrError();
			if (warnOrError != null) {
				final String name = f.getName().substring(0, f.getName().length() - 4) + ".err";
				final SFile errorFile = f.getParentFile().file(name);
				try (PrintStream ps = SecurityUtils.createPrintStream(errorFile.createFileOutputStream())) {
					ps.print(warnOrError);
				}
			}
		}
	}

	final public List<GeneratedImage> getGeneratedImages() throws IOException {
		Log.info(() -> "Reading file: " + file);

		cpt = 0;
		final List<GeneratedImage> result = new ArrayList<>();

		for (BlockUml blockUml : builder.getBlockUmls()) {
			final SuggestedFile suggested = getSuggestedFile(blockUml);

			final Diagram system;
			try {
				system = blockUml.getDiagram();
			} catch (Throwable t) {
				Logme.error(t);
				if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.SILENTLY_COMPLETELY_IGNORE_ERRORS) || noerror)
					continue;

				return getCrashedImage(blockUml, t, suggested.getFile(0));
			}

			if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.SILENTLY_COMPLETELY_IGNORE_ERRORS) && system instanceof PSystemError)
				continue;

			// GlobalConfig.getInstance().logData(SFile.fromFile(file), system);
			final List<FileImageData> exportDiagrams;
			if (noerror && system instanceof PSystemError) {
				exportDiagrams = new ArrayList<FileImageData>();
				exportDiagrams
						.add(new FileImageData(null, new ImageDataSimple(new XDimension2D(0, 0), FileImageData.ERROR)));
			} else
				exportDiagrams = PSystemUtils.exportDiagrams(system, suggested, fileFormatOption, checkMetadata);

			if (exportDiagrams.size() > 1)
				cpt += exportDiagrams.size() - 1;

			for (FileImageData fdata : exportDiagrams) {
				final String desc = "[" + file.getName() + "] " + system.getDescription();
				final SFile f = fdata.getFile();
				exportWarnOrErrIfWord(f, system);
				final GeneratedImage generatedImage = new GeneratedImageImpl(f, desc, blockUml, fdata.getStatus());
				result.add(generatedImage);
			}

		}

		Log.info(() -> "Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	abstract protected SuggestedFile getSuggestedFile(BlockUml blockUml) throws FileNotFoundException;

	protected final void setNoerror(boolean noerror) {
		this.noerror = noerror;

	}

	final protected String getFileName() {
		return file.getName();
	}

}
