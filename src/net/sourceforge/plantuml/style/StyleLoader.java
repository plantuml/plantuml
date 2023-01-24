/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
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
package net.sourceforge.plantuml.style;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.style.parser.StyleParser;
import net.sourceforge.plantuml.style.parser.StyleParsingException;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.LineLocationImpl;
import net.sourceforge.plantuml.utils.Log;

public class StyleLoader {

	private final SkinParam skinParam;

	public StyleLoader(SkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private StyleBuilder styleBuilder;

	public StyleBuilder loadSkin(String filename) throws IOException, StyleParsingException {
		this.styleBuilder = new StyleBuilder(skinParam);

		final InputStream internalIs = getInputStreamForStyle(filename);
		if (internalIs == null) {
			Log.error("No .skin file seems to be available");
			throw new NoStyleAvailableException();
		}
		final BlocLines lines2 = BlocLines.load(internalIs, new LineLocationImpl(filename, null));
		loadSkinInternal(lines2);
		if (this.styleBuilder == null) {
			Log.error("No .skin file seems to be available");
			throw new NoStyleAvailableException();
		}
		return this.styleBuilder;
	}

	public static InputStream getInputStreamForStyle(String filename) throws IOException {
		InputStream internalIs = null;
		SFile localFile = new SFile(filename);
		Log.info("Trying to load style " + filename);
		try {
			if (localFile.exists() == false)
				localFile = FileSystem.getInstance().getFile(filename);
		} catch (IOException e) {
			Log.info("Cannot open file. " + e);
		}

		if (localFile.exists()) {
			Log.info("File found : " + localFile.getPrintablePath());
			internalIs = localFile.openFile();
		} else {
			Log.info("File not found : " + localFile.getPrintablePath());
			final String res = "/skin/" + filename;
			internalIs = StyleLoader.class.getResourceAsStream(res);
			if (internalIs != null)
				Log.info("... but " + filename + " found inside the .jar");

		}
		return internalIs;
	}

	private void loadSkinInternal(final BlocLines lines) throws StyleParsingException {
		for (Style newStyle : StyleParser.parse(lines, styleBuilder))
			this.styleBuilder.loadInternal(newStyle.getSignature(), newStyle);
	}

	public static final int DELTA_PRIORITY_FOR_STEREOTYPE = 1000;

	public static Map<PName, Value> addPriorityForStereotype(Map<PName, Value> tmp) {
		final Map<PName, Value> result = new EnumMap<>(PName.class);
		for (Entry<PName, Value> ent : tmp.entrySet())
			result.put(ent.getKey(), ((ValueImpl) ent.getValue()).addPriority(DELTA_PRIORITY_FOR_STEREOTYPE));

		return result;
	}

}