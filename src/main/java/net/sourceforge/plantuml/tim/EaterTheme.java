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
 */
package net.sourceforge.plantuml.tim;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import net.sourceforge.plantuml.file.AFile;
import net.sourceforge.plantuml.file.AParentFolder;
import net.sourceforge.plantuml.file.AParentFolderRegular;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.theme.Theme;
import net.sourceforge.plantuml.theme.ThemeUtils;

public class EaterTheme extends Eater {
	// ::remove folder when __HAXE__

	private String realName;
	private String name;
	private String from;
	private TContext context;
	private final ImportedFiles importedFiles;
	private ImportedFiles newImportedFiles;

	public EaterTheme(StringLocated s, ImportedFiles importedFiles) {
		super(s);
		this.importedFiles = importedFiles;
		this.newImportedFiles = importedFiles;
	}

	@Override
	public void analyze(TContext context, TMemory memory) throws EaterException {
		skipSpaces();
		checkAndEatChar("!theme");
		skipSpaces();
		this.name = this.eatAllToEnd();

		final int x = this.name.toLowerCase().indexOf(" from ");
		if (x != -1) {
			final String fromTmp = this.name.substring(x + " from ".length()).trim();
			this.from = context.applyFunctionsAndVariables(memory, new StringLocated(fromTmp, getLineLocation()));
			this.name = this.name.substring(0, x).trim();
			this.context = context;
		}

		this.realName = context.applyFunctionsAndVariables(memory, new StringLocated(this.name, getLineLocation()));

	}

	public final Theme getTheme() throws EaterException {
		if (from == null) {
			try {
				final Theme theme = ThemeUtils.getReaderTheme(realName);
				if (theme != null)
					return theme;

				final AFile localFile = importedFiles.getAFile(ThemeUtils.getFilename(realName));
				if (localFile != null && localFile.isOk()) {
					final BufferedReader br = localFile.getUnderlyingFile().openBufferedReader();
					if (br != null)
						return new Theme(ReadLineReader.create(br, "theme " + realName));
				}
			} catch (IOException e) {
				Logme.error(e);
			}
			throw new EaterException("Cannot load " + realName, getStringLocated());
		}

		if (from.startsWith("<") && from.endsWith(">")) {
			final Theme theme = ThemeUtils.getReaderTheme(realName, from);
			if (theme == null)
				throw new EaterException("No such theme " + realName + " in " + from, getStringLocated());
			return theme;
		} else if (from.startsWith("http://") || from.startsWith("https://")) {
			final SURL url = SURL.create(ThemeUtils.getFullPath(from, realName));
			if (url == null)
				throw new EaterException("Cannot open URL", getStringLocated());

			try {
				return new Theme(PreprocessorUtils.getReaderInclude(url, getStringLocated(), UTF_8));
			} catch (UnsupportedEncodingException e) {
				Logme.error(e);
				throw new EaterException("Cannot decode charset", getStringLocated());
			}
		}

		try {
			final FileWithSuffix file = context.getFileWithSuffix(from, realName);
			final Reader tmp = file.getReader(UTF_8);
			if (tmp == null)
				throw new EaterException("No such theme " + realName, getStringLocated());

			final AFile fromDir = importedFiles.getAFile(from);
			final AParentFolder parent = new AParentFolderRegular(fromDir.getUnderlyingFile());
			newImportedFiles = importedFiles.withCurrentDir(parent);

			return new Theme(ReadLineReader.create(tmp, "theme " + realName));
		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("Cannot load " + realName, getStringLocated());
		}

	}

	public String getName() {
		return name;
	}

	public ImportedFiles getNewImportedFiles() {
		return newImportedFiles;
	}

}
