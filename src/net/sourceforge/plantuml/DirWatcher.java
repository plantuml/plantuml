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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;

@Deprecated
public class DirWatcher {

	final private File dir;
	final private Option option;
	final private String pattern;

	final private Map<File, FileWatcher> modifieds = new HashMap<File, FileWatcher>();

	public DirWatcher(File dir, Option option, String pattern) {
		this.dir = dir;
		this.option = option;
		this.pattern = pattern;
	}

	public List<GeneratedImage> buildCreatedFiles() throws IOException, InterruptedException {
		boolean error = false;
		final List<GeneratedImage> result = new ArrayList<GeneratedImage>();
		for (File f : dir.listFiles()) {
			if (error) {
				continue;
			}
			if (f.isFile() == false) {
				continue;
			}
			if (fileToProcess(f.getName()) == false) {
				continue;
			}
			final FileWatcher watcher = modifieds.get(f);

			if (watcher == null || watcher.hasChanged()) {
				final SourceFileReader sourceFileReader = new SourceFileReader(Defines.createWithFileName(f), f,
						option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
				final Set<File> files = FileWithSuffix.convert(sourceFileReader.getIncludedFiles());
				files.add(f);
				for (GeneratedImage g : sourceFileReader.getGeneratedImages()) {
					result.add(g);
					if (option.isFailfastOrFailfast2() && g.lineErrorRaw() != -1) {
						error = true;
					}
				}
				modifieds.put(f, new FileWatcher(files));
			}
		}
		Collections.sort(result);
		return Collections.unmodifiableList(result);
	}

	public File getErrorFile() throws IOException, InterruptedException {
		for (File f : dir.listFiles()) {
			if (f.isFile() == false) {
				continue;
			}
			if (fileToProcess(f.getName()) == false) {
				continue;
			}
			final FileWatcher watcher = modifieds.get(f);

			if (watcher == null || watcher.hasChanged()) {
				final SourceFileReader sourceFileReader = new SourceFileReader(Defines.createWithFileName(f), f,
						option.getOutputDir(), option.getConfig(), option.getCharset(), option.getFileFormatOption());
				if (sourceFileReader.hasError()) {
					return f;
				}
			}
		}
		return null;
	}

	private boolean fileToProcess(String name) {
		return name.matches(pattern);
	}

	public final File getDir() {
		return dir;
	}

	// public void setPattern(String pattern) {
	// this.pattern = pattern;
	// }
}
