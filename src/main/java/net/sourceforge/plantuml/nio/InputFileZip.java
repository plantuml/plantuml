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
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Represents an entry inside a ZIP archive as an {@link InputFile}.
 *
 * <p>
 * Example:
 * 
 * <pre>
 * InputFile in = new InputFileZip(new File("docs.zip"), "manual/readme.txt");
 * try (InputStream is = in.newInputStream()) {
 * 	// read bytes...
 * }
 * </pre>
 */
public class InputFileZip implements InputFile {

	private final File zipFile;
	private final String entryName;

	/**
	 * Creates a new ZIP input for the given archive and entry name.
	 *
	 * @param zipFile   the ZIP archive file
	 * @param entryName the name of the entry inside the archive
	 */
	public InputFileZip(File zipFile, String entryName) {
		this.zipFile = Objects.requireNonNull(zipFile);
		this.entryName = Objects.requireNonNull(entryName);
	}

	@Override
	public InputStream newInputStream() throws IOException {
		final ZipFile zf = new ZipFile(zipFile);
		final ZipEntry entry = zf.getEntry(entryName);

		if (entry == null) {
			// Ensure the ZipFile is closed immediately in case of missing entry
			zf.close();
			throw new IOException("Entry not found in ZIP: " + entryName + " (" + zipFile + ")");
		}

		// Wrap the entry InputStream so closing it also closes the ZipFile.
		return new ZipEntryInputStream(zf, zf.getInputStream(entry));
	}

	/**
	 * Small helper InputStream wrapper that closes the underlying ZipFile when the
	 * stream is closed.
	 */
	private static final class ZipEntryInputStream extends InputStream {
		private final ZipFile zipFile;
		private final InputStream delegate;
		private boolean closed;

		private ZipEntryInputStream(ZipFile zipFile, InputStream delegate) {
			this.zipFile = zipFile;
			this.delegate = delegate;
		}

		@Override
		public int read() throws IOException {
			return delegate.read();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return delegate.read(b, off, len);
		}

		@Override
		public void close() throws IOException {
			if (!closed)
				try {
					delegate.close();
				} finally {
					zipFile.close();
					closed = true;
				}

		}
	}

	@Override
	public String toString() {
		return Paths.get(zipFile.getPath()).toAbsolutePath() + "!" + entryName;
	}

	@Override
	public NFolder getParentFolder() throws IOException {
		final Path parent = getParentFrom(entryName);
		return new NFolderZip(zipFile).getSubfolder(parent);
	}

	/**
	 * Computes the parent path of an entry inside the ZIP. For example:
	 * <ul>
	 * <li>{@code "a/b/c.txt"} → {@code Paths.get("a/b")}</li>
	 * <li>{@code "a.txt"} → {@code Paths.get("")}</li>
	 * <li>{@code ""} → {@code Paths.get("")}</li>
	 * </ul>
	 */
	private static Path getParentFrom(String entryName) {
		if (entryName == null || entryName.isEmpty())
			return Paths.get("");
		final int idx = entryName.replace('\\', '/').lastIndexOf('/');
		if (idx <= 0)
			return Paths.get("");
		final String parent = entryName.substring(0, idx);
		return Paths.get(parent);
	}
}
