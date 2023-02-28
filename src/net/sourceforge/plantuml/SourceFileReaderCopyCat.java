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
import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.file.SuggestedFile;
import net.sourceforge.plantuml.preproc.Defines;

public class SourceFileReaderCopyCat extends SourceFileReaderAbstract implements ISourceFileReader {
	// ::remove file when __CORE__
	// ::remove file when __HAXE__

	private final File outputDirectory;

	public SourceFileReaderCopyCat(Defines defines, final File file, File outputDirectory, List<String> config,
			String charset, FileFormatOption fileFormatOption) throws IOException {
		super(file, fileFormatOption, defines, config, charset);
		final String path = file.getParentFile().getPath();
		this.outputDirectory = new File(outputDirectory, path).getAbsoluteFile();
		if (outputDirectory.exists() == false)
			outputDirectory.mkdirs();
	}

	@Override
	protected SuggestedFile getSuggestedFile(BlockUml blockUml) {
		String newName = blockUml.getFileOrDirname();
		if (newName == null)
			newName = getFileName();
		final SuggestedFile suggested = getSuggestedFile(outputDirectory, newName);
		suggested.getParentFile().mkdirs();
		return suggested;
	}

}
