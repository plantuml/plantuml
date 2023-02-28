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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.utils.Log;

// Used by the Eclipse Plugin, so do not change package location.
public class FileUtils {
	// ::remove file when __HAXE__

	private static AtomicInteger counter;

	public static void resetCounter() {
		counter = new AtomicInteger(0);
	}

	static public String readSvg(SFile svgFile) throws IOException {
		final BufferedReader br = svgFile.openBufferedReader();
		if (br == null)
			return null;

		return readSvg(br, false, true);
	}

	static public String readSvg(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return readSvg(br, false, false);
	}

	static public void copyInternal(final InputStream fis, final OutputStream fos, boolean close) throws IOException {
		final byte[] buf = new byte[10240];
		int len;
		while ((len = fis.read(buf)) > 0)
			fos.write(buf, 0, len);

		if (close) {
			fos.close();
			fis.close();
		}
	}

	private static String readSvg(BufferedReader br, boolean withNewline, boolean withClose) throws IOException {
		final StringBuilder sb = new StringBuilder();
		String s;
		while ((s = br.readLine()) != null) {
			sb.append(s);
			if (withNewline)
				sb.append("\n");

		}
		if (withClose)
			br.close();

		return sb.toString();
	}

	static public String readText(InputStream is) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return readSvg(br, true, true);
	}

	// ::comment when __CORE__
	static public File createTempFileLegacy(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false)
			throw new IllegalArgumentException();

		Objects.requireNonNull(prefix);
		final File f;
		if (counter == null) {
			f = File.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new File(name);
		}
		Log.info("Creating temporary file: " + f);
		f.deleteOnExit();
		return f;
	}

	static public SFile createTempFile(String prefix, String suffix) throws IOException {
		if (suffix.startsWith(".") == false)
			throw new IllegalArgumentException();

		Objects.requireNonNull(prefix);
		final SFile f;
		if (counter == null) {
			f = SFile.createTempFile(prefix, suffix);
		} else {
			final String name = prefix + counter.addAndGet(1) + suffix;
			f = new SFile(name);
		}
		Log.info("Creating temporary file: " + f);
		f.deleteOnExit();
		return f;
	}

	static public void copyToFile(SFile src, SFile dest) throws IOException {
		if (dest.isDirectory())
			dest = dest.file(src.getName());

		final InputStream fis = src.openFile();
		if (fis == null)
			throw new FileNotFoundException();

		final OutputStream fos = dest.createBufferedOutputStream();
		copyInternal(fis, fos, true);
	}

	static public void copyToStream(SFile src, OutputStream os) throws IOException {
		final InputStream fis = src.openFile();
		if (fis == null)
			throw new FileNotFoundException();

		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos, true);
	}

	static public void copyToStream(File src, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(new FileInputStream(src));
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos, true);
	}

	static public void copyToStream(InputStream is, OutputStream os) throws IOException {
		final InputStream fis = new BufferedInputStream(is);
		final OutputStream fos = new BufferedOutputStream(os);
		copyInternal(fis, fos, true);
	}

	static public void copyToFile(byte[] src, SFile dest) throws IOException {
		try (OutputStream fos = dest.createBufferedOutputStream()) {
			fos.write(src);
		}
	}

	static public String readFile(SFile svgFile) throws IOException {
		final BufferedReader br = svgFile.openBufferedReader();
		if (br == null)
			return null;

		return readSvg(br, true, true);
	}
	// ::done

}
