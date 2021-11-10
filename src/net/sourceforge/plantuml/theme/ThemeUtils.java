/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
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
 * Original Author:  Matthew Leather
 *
 *
 */
package net.sourceforge.plantuml.theme;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.sprite.RessourcesUtils;

public class ThemeUtils {

	private static final String THEME_FILE_PREFIX = "puml-theme-";

	private static final String THEME_FILE_SUFFIX = ".puml";

	private static final String THEME_PATH = "themes";

	public static List<String> getAllThemeNames() throws IOException {
		final Collection<String> filenames = Objects.requireNonNull(RessourcesUtils.getJarFile(THEME_PATH, false));
		final List<String> result = new ArrayList<>();
		for (String f : filenames) {
			if (f.startsWith(THEME_FILE_PREFIX) && f.endsWith(THEME_FILE_SUFFIX)) {
				result.add(f.substring(THEME_FILE_PREFIX.length(), f.length() - THEME_FILE_SUFFIX.length()));
			}
		}
		Collections.sort(result);
		return result;
	}

	public static ReadLine getReaderTheme(String filename) {
		Log.info("Loading theme " + filename);
		final String res = "/" + THEME_PATH + "/" + THEME_FILE_PREFIX + filename + THEME_FILE_SUFFIX;
		final String description = "<" + res + ">";
		final InputStream is = Stdlib.class.getResourceAsStream(res);
		if (is == null) {
			return null;
		}
		return ReadLineReader.create(new InputStreamReader(is), description);
	}

	public static String getFullPath(String from, String filename) {
		final StringBuilder sb = new StringBuilder(from);
		if (from.endsWith("/") == false) {
			sb.append("/");
		}
		return sb + getFilename(filename);
	}

	public static String getFilename(String filename) {
		return THEME_FILE_PREFIX + filename + THEME_FILE_SUFFIX;
	}

}
