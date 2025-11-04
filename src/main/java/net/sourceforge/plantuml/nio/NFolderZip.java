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
package net.sourceforge.plantuml.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A virtual folder representing a location inside a ZIP archive.
 *
 * <p>
 * Instances are immutable. Each instance points to the same ZIP file but to a
 * specific sub-path (which can be the root) inside that archive.
 */
public final class NFolderZip implements NFolder {

	private final File zipFile;
	/** Current path inside the ZIP (relative, no leading slash). */
	private final Path pathInsideZip;

	/** Creates a folder view of the ZIP root. */
	public NFolderZip(File zipFile) {
		this(zipFile, Paths.get(""));
	}

	private NFolderZip(File zipFile, Path currentPathInsideZip) {
		this.zipFile = Objects.requireNonNull(zipFile);
		this.pathInsideZip = Objects.requireNonNull(currentPathInsideZip).normalize();
	}

	@Override
	public InputFile getInputFile(Path nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		final String entry = toZipEntryName(nameOrPath);
		return new InputFileZip(zipFile, entry);
	}

	@Override
	public NFolder getSubfolder(Path nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		final String entry = toZipEntryName(nameOrPath);
		final Path next = entry.isEmpty() ? Paths.get("") : Paths.get(entry);
		return new NFolderZip(zipFile, next);
	}

	@Override
	public String toString() {
		final String prefix = pathInsideZip.toString().replace('\\', '/');
		return zipFile.getName() + "!" + prefix;
	}

	/**
	 * Resolves a path inside the ZIP and returns a forward-slash entry name without
	 * any leading slash (as typically stored in ZIPs).
	 */
	private String toZipEntryName(Path child) {
		final Path resolved = child.isAbsolute() ? child.normalize() : pathInsideZip.resolve(child).normalize();
		String s = resolved.toString().replace('\\', '/');
		while (s.startsWith("/"))
			s = s.substring(1);

		return s;
	}

}
