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
 * Revision $Revision: 4771 $
 *
 */
package net.sourceforge.plantuml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;
import net.sourceforge.plantuml.preproc.Defines;

public class SourceFileReader {

	private final File file;
	private final File outputDirectory;

	private final BlockUmlBuilder builder;
	private FileFormatOption fileFormatOption;

	public SourceFileReader(File file) throws IOException {
		this(file, file.getAbsoluteFile().getParentFile());
	}

	public SourceFileReader(final File file, File outputDirectory) throws IOException {
		this(new Defines(), file, outputDirectory, Collections.<String> emptyList(), null, new FileFormatOption(FileFormat.PNG));
	}

	public SourceFileReader(final File file, File outputDirectory, FileFormatOption fileFormatOption) throws IOException {
		this(new Defines(), file, outputDirectory, Collections.<String> emptyList(), null, fileFormatOption);
	}

	public SourceFileReader(Defines defines, final File file, File outputDirectory, List<String> config,
			String charset, FileFormatOption fileFormatOption) throws IOException {
		this.file = file;
		this.fileFormatOption = fileFormatOption;
		if (file.exists() == false) {
			throw new IllegalArgumentException();
		}
		FileSystem.getInstance().setCurrentDir(file.getAbsoluteFile().getParentFile());
		if (outputDirectory == null) {
			outputDirectory = file.getAbsoluteFile().getParentFile();
		} else if (outputDirectory.isAbsolute() == false) {
			outputDirectory = FileSystem.getInstance().getFile(outputDirectory.getName());
		}
		if (outputDirectory.exists() == false) {
			outputDirectory.mkdirs();
		}
		this.outputDirectory = outputDirectory;
		
		builder = new BlockUmlBuilder(config, defines, getReader(charset));
	}

	public List<GeneratedImage> getGeneratedImages() throws IOException, InterruptedException {
		Log.info("Reading file: " + file);

		int cpt = 0;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();

		for (BlockUml blockUml : builder.getBlockUmls()) {
			String newName = blockUml.getFilename();

			if (newName == null) {
				newName = changeName(file.getName(), cpt++, fileFormatOption.getFileFormat());
			}

			final File suggested = new File(outputDirectory, newName);
			suggested.getParentFile().mkdirs();

			for (File f : blockUml.getSystem().exportDiagrams(suggested, fileFormatOption)) {
				final String desc = "[" + file.getName() + "] " + blockUml.getSystem().getDescription();
				final GeneratedImage generatedImage = new GeneratedImage(f, desc);
				result.add(generatedImage);
			}

		}

		Log.info("Number of image(s): " + result.size());

		return Collections.unmodifiableList(result);
	}

	public List<String> getEncodedUrl() throws IOException, InterruptedException {
		final List<String> result = new ArrayList<String>();
		final Transcoder transcoder = TranscoderUtil.getDefaultTranscoder();
		for (BlockUml blockUml : builder.getBlockUmls()) {
			final String source = blockUml.getSystem().getSource().getPlainString();
			final String encoded = transcoder.encode(source);
			result.add(encoded);
		}
		return Collections.unmodifiableList(result);
	}

	static String changeName(String name, int cpt, FileFormat fileFormat) {
		if (cpt == 0) {
			return name.replaceAll("\\.\\w+$", fileFormat.getFileSuffix());
		}
		return name.replaceAll("\\.\\w+$", "_" + String.format("%03d", cpt) + fileFormat.getFileSuffix());
	}

	private Reader getReader(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(new FileInputStream(file));
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(new FileInputStream(file), charset);
	}

	public final void setFileFormatOption(FileFormatOption fileFormatOption) {
		this.fileFormatOption = fileFormatOption;
	}
	
	public final Set<File> getIncludedFiles() {
		return builder.getIncludedFiles();
	}


}
