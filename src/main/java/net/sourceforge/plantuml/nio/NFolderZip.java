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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.sourceforge.plantuml.teavm.TeaVM;

/**
 * A virtual folder representing a location inside a ZIP archive.
 *
 * <p>
 * Instances are immutable. Each instance points to the same ZIP file but to a
 * specific sub-path (which can be the root) inside that archive.
 * <p>
 * The paths are handled as strings, since the C build has no file system
 * provider behind java.nio.file.
 */
public final class NFolderZip implements NFolder {

	private final File zipFile;
	/** Current path inside the ZIP (relative, forward slashes, no leading slash). */
	private final String pathInsideZip;

	/** Creates a folder view of the ZIP root. */
	public NFolderZip(File zipFile) {
		this(zipFile, "");
	}

	private NFolderZip(File zipFile, String currentPathInsideZip) {
		this.zipFile = Objects.requireNonNull(zipFile);
		this.pathInsideZip = normalize(Objects.requireNonNull(currentPathInsideZip));
	}

	@Override
	public InputFile getInputFile(Path nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		return getInputFile(nameOrPath.toString());
	}

	/**
	 * Same as {@link #getInputFile(Path)}, without java.nio.file.
	 */
	public InputFile getInputFile(String nameOrPath) {
		Objects.requireNonNull(nameOrPath);
		final String entry = toZipEntryName(nameOrPath);
		return new InputFileZip(zipFile, entry);
	}

	/**
	 * Tells whether the given path designates an existing file (not a directory)
	 * inside the archive. Unlike {@link #getInputFile(Path)}, which always returns
	 * an {@link InputFile} that fails when read, this is a plain existence check,
	 * usable when the archive is only one of several places to search.
	 */
	public boolean contains(Path nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		return contains(nameOrPath.toString());
	}

	/**
	 * Same as {@link #contains(Path)}, without java.nio.file.
	 */
	public boolean contains(String nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		final String entry = toZipEntryName(nameOrPath);
		if (entry.isEmpty() || zipFile.canRead() == false)
			return false;

		// ::comment when JAVA8
		if (TeaVM.isC()) {
			final ZipReaderIo reader = ZipReaderIo.open(zipFile);
			return reader != null && reader.contains(entry);
		}
		// ::done

		try (ZipFile zf = new ZipFile(zipFile)) {
			final ZipEntry ze = zf.getEntry(entry);
			return ze != null && ze.isDirectory() == false;
		} catch (ZipException e) {
			// Not a valid archive
			return false;
		}
	}

	@Override
	public NFolder getSubfolder(Path nameOrPath) throws IOException {
		Objects.requireNonNull(nameOrPath);
		return getSubfolder(nameOrPath.toString());
	}

	/**
	 * Same as {@link #getSubfolder(Path)}, without java.nio.file.
	 */
	public NFolder getSubfolder(String nameOrPath) {
		Objects.requireNonNull(nameOrPath);
		return new NFolderZip(zipFile, toZipEntryName(nameOrPath));
	}

	@Override
	public String toString() {
		return zipFile.getName() + "!" + pathInsideZip;
	}

	/**
	 * Resolves a path inside the ZIP and returns a forward-slash entry name without
	 * any leading slash (as typically stored in ZIPs).
	 */
	private String toZipEntryName(String child) {
		final String slashed = child.replace('\\', '/');
		return slashed.startsWith("/") ? normalize(slashed) : normalize(pathInsideZip + "/" + slashed);
	}

	/**
	 * Forward slashes, no empty or "." segment, ".." removing the segment before
	 * it, no leading slash.
	 */
	private static String normalize(String path) {
		final List<String> result = new ArrayList<String>();
		for (String segment : path.replace('\\', '/').split("/")) {
			if (segment.isEmpty() || segment.equals("."))
				continue;

			if (segment.equals("..") && result.size() > 0 && result.get(result.size() - 1).equals("..") == false)
				result.remove(result.size() - 1);
			else
				result.add(segment);
		}
		final StringBuilder sb = new StringBuilder();
		for (String segment : result) {
			if (sb.length() > 0)
				sb.append('/');
			sb.append(segment);
		}
		return sb.toString();
	}

}
