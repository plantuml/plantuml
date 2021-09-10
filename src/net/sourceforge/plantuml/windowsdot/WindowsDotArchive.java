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
package net.sourceforge.plantuml.windowsdot;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sourceforge.plantuml.brotli.BrotliInputStream;

public final class WindowsDotArchive {

	private static WindowsDotArchive singleton = null;

	private Boolean isThereArchive;
	private File exe;

	private WindowsDotArchive() {

	}

	public final synchronized static WindowsDotArchive getInstance() {
		if (singleton == null) {
			singleton = new WindowsDotArchive();
		}
		return singleton;
	}

	final static public String readString(InputStream is) throws IOException {
		int len = readByte(is);
		final StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append((char) readByte(is));

		}
		return sb.toString();
	}

	final static public int readNumber(InputStream is) throws IOException {
		int result = readByte(is);
		result = result * 256 + readByte(is);
		result = result * 256 + readByte(is);
		return result;
	}

	private static int readByte(InputStream is) throws IOException {
		return is.read();
	}

	private static void extract(File dir) throws IOException {
		final InputStream raw = WindowsDotArchive.class.getResourceAsStream("graphviz.dat");
		try (final BrotliInputStream is = new BrotliInputStream(raw)) {
			while (true) {
				final String name = readString(is);
				if (name.length() == 0)
					break;
				final int size = readNumber(is);
				try (final OutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(dir, name)))) {
					for (int i = 0; i < size; i++) {
						fos.write(is.read());
					}
				}
			}
		}
	}

	public synchronized boolean isThereArchive() {
		if (isThereArchive == null)
			try (InputStream raw = WindowsDotArchive.class.getResourceAsStream("graphviz.dat")) {
				isThereArchive = raw != null;
			} catch (Exception e) {
				isThereArchive = false;
			}
		return isThereArchive;
	}

	public synchronized File getWindowsExeLite() {
		if (isThereArchive() == false) {
			return null;
		}
		if (exe == null)
			try {
				final File tmp = new File(System.getProperty("java.io.tmpdir"), "_graphviz");
				tmp.mkdirs();
				extract(tmp);
				exe = new File(tmp, "dot.exe");
			} catch (IOException e) {
				e.printStackTrace();
			}
		return exe;
	}

}
