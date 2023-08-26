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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.Log;

public class SourceFileReader extends SourceFileReaderAbstract implements ISourceFileReader {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private File outputDirectory;

	public SourceFileReader(File file) throws IOException {
		this(file, file.getAbsoluteFile().getParentFile());
	}

	public SourceFileReader(File file, File outputDirectory, String charset) throws IOException {
		this(Defines.createWithFileName(file), file, outputDirectory, Collections.<String>emptyList(), charset,
				new FileFormatOption(FileFormat.PNG));
	}

	public SourceFileReader(final File file, File outputDirectory) throws IOException {
		this(Defines.createWithFileName(file), file, outputDirectory, Collections.<String>emptyList(), null,
				new FileFormatOption(FileFormat.PNG));
	}

	public SourceFileReader(final File file, File outputDirectory, FileFormatOption fileFormatOption)
			throws IOException {
		this(Defines.createWithFileName(file), file, outputDirectory, Collections.<String>emptyList(), null,
				fileFormatOption);
	}

	public SourceFileReader(Defines defines, final File file, File outputDirectory, List<String> config, String charset,
			FileFormatOption fileFormatOption) throws IOException {
		super(file, fileFormatOption, defines, config, charset);
		FileSystem.getInstance().setCurrentDir(SFile.fromFile(file.getAbsoluteFile().getParentFile()));
		if (outputDirectory == null)
			outputDirectory = file.getAbsoluteFile().getParentFile();
		else if (outputDirectory.isAbsolute() == false)
			outputDirectory = FileSystem.getInstance().getFile(outputDirectory.getPath()).conv();

		if (outputDirectory.exists() == false)
			outputDirectory.mkdirs();

		this.outputDirectory = outputDirectory;

	}

	private File getDirIfDirectory(String newName) throws FileNotFoundException {
		Log.info("Checking=" + newName);
		if (endsWithSlashOrAntislash(newName)) {
			Log.info("It ends with / so it looks like a directory");
			newName = newName.substring(0, newName.length() - 1);
			File f = new File(newName);
			Log.info("f=" + f);
			if (f.isAbsolute() == false) {
				Log.info("It's relative, so let's change it");
				f = new File(outputDirectory, newName);
				Log.info("f=" + f);
			}
			if (f.exists() == false) {
				Log.info("It does not exist: let's create it");
				try {
					f.mkdirs();
				} catch (Exception e) {
					Log.info("Error " + e);
				}
				if (f.exists() && f.isDirectory()) {
					Log.info("Creation ok");
					return f;
				}
				Log.info("We cannot create it");
			} else if (f.isDirectory() == false) {
				Log.info("It exists, but is not a directory: we ignore it");
				return null;
			}
			return f;

		}
		File f = new File(newName);
		Log.info("f=" + f);
		if (f.isAbsolute() == false) {
			Log.info("Relative, so let's change it");
			f = new File(outputDirectory, newName);
			Log.info("f=" + f);
		}
		if (f.exists() && f.isDirectory()) {
			Log.info("It's an existing directory");
			return f;
		}
		Log.info("It's not a directory");
		return null;

	}

	@Override
	protected SuggestedFile getSuggestedFile(BlockUml blockUml) throws FileNotFoundException {
		final String newName = blockUml.getFileOrDirname();
		SuggestedFile suggested = null;
		if (newName != null) {
			Log.info("name from block=" + newName);
			final File dir = getDirIfDirectory(newName);
			if (dir == null) {
				Log.info(newName + " is not taken as a directory");
				suggested = SuggestedFile.fromOutputFile(new File(outputDirectory, newName),
						getFileFormatOption().getFileFormat(), 0);
			} else {
				Log.info("We are going to create files in directory " + dir);
				suggested = SuggestedFile.fromOutputFile(new File(dir, getFileName()),
						getFileFormatOption().getFileFormat(), 0);
			}
			Log.info("We are going to put data in " + suggested);
		}
		if (suggested == null)
			suggested = getSuggestedFile(outputDirectory, getFileName());

		suggested.getParentFile().mkdirs();
		return suggested;
	}

}
