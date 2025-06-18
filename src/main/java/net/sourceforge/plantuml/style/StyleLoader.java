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
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.utils.Log;

public final class StyleLoader {
	// ::remove file when __HAXE__

	private static final ConcurrentMap<String, StyleBuilder> cache = new ConcurrentHashMap<>();

	private StyleLoader() {
	}

	public static StyleBuilder loadSkin(String filename) throws IOException, StyleParsingException {
		try {
			if (cache.size() >= 30)
				cache.clear();

			final StyleBuilder builder = cache.computeIfAbsent(filename, key -> {
				try {
					return loadSkinSlow(key);
				} catch (IOException | StyleParsingException e) {
					throw new RuntimeException(e);
				}
			});
			return builder.cloneMe();
		} catch (RuntimeException ex) {
			Throwable cause = ex.getCause();
			if (cause instanceof IOException)
				throw (IOException) cause;
			if (cause instanceof StyleParsingException)
				throw (StyleParsingException) cause;
			throw ex;
		}
	}

	private static StyleBuilder loadSkinSlow(String filename) throws IOException, StyleParsingException {
		final StyleBuilder styleBuilder = new StyleBuilder();

		final InputStream internalIs = getInputStreamForStyle(filename);
		if (internalIs == null) {
			Log.error("No .skin file seems to be available");
			throw new NoStyleAvailableException();
		}
		final BlocLines lines2 = BlocLines.load(internalIs, new LineLocationImpl(filename, null));
		for (Style newStyle : StyleParser.parse(lines2, styleBuilder))
			styleBuilder.loadInternal(newStyle.getSignature(), newStyle);

		return styleBuilder;
	}

	public static InputStream getInputStreamForStyle(String filename) throws IOException {
		// ::uncomment when __CORE__
//		final String res = "/skin/" + filename;
//		final InputStream is = StyleLoader.class.getResourceAsStream(res);
//		return is;
		// ::done

		// ::comment when __CORE__
		InputStream internalIs = null;
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
			internalIs = localFile.openFile();
		} else {
			Log.info(() -> "File not found : " + localFile2.getPrintablePath());
			final String res = "/skin/" + filename;
			internalIs = StyleLoader.class.getResourceAsStream(res);
			if (internalIs != null)
				Log.info(() -> "... but " + filename + " found inside the .jar");

		}
		return internalIs;
		// ::done
	}

	public static final int DELTA_PRIORITY_FOR_STEREOTYPE = 1000;

	public static Map<PName, Value> addPriorityForStereotype(Map<PName, Value> tmp) {
		final Map<PName, Value> result = new EnumMap<>(PName.class);
		for (Entry<PName, Value> ent : tmp.entrySet())
			result.put(ent.getKey(), ((ValueImpl) ent.getValue()).addPriority(DELTA_PRIORITY_FOR_STEREOTYPE));

		return result;
	}

}