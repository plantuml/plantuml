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
package net.sourceforge.plantuml.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.teavm.EmbeddedResources;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.utils.Log;

public final class StyleLoader2 {
//	private static final ConcurrentMap<String, StyleBuilder> cache = new ConcurrentHashMap<>();

	private StyleLoader2() {
	}

	public static Object loadSkin(String filename) throws IOException, StyleParsingException {

		final InputStream internalIs = getInputStreamForStyle(filename);
		if (internalIs == null) {
			Log.error("No .skin file seems to be available");
			throw new NoStyleAvailableException();
		}
		final BlocLines lines2 = BlocLines.load(internalIs, new LineLocationImpl(filename, null));
		System.err.println("size=" + lines2.size());
		// Working in progress

		return null;
	}

	public static InputStream getInputStreamForStyle(String filename) throws IOException {

		if (TeaVM.isTeaVM()) {
			return EmbeddedResources.openPlantumlSkin();
		} else {
			InputStream is = null;

			SFile localFile = new SFile(filename);
			Log.info(() -> "Trying to load style " + filename);
			try {
				if (localFile.exists() == false)
					localFile = FileSystem.getInstance().getFile(filename);
			} catch (IOException e) {
				Log.info(() -> "Cannot open file. " + e);
			}

			final SFile localFile2 = localFile;
			if (localFile.exists()) {
				Log.info(() -> "File found : " + localFile2.getPrintablePath());
				is = localFile.openFile();
			} else {
				Log.info(() -> "File not found : " + localFile2.getPrintablePath());
				final String res = "/skin/" + filename;
				is = StyleLoader2.class.getResourceAsStream(res);
				if (is != null)
					Log.info(() -> "... but " + filename + " found inside the .jar");

			}
			return is;
		}
	}


}