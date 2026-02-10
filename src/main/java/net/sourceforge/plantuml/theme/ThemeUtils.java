/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2021, Arnaud Roques
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
 * Original Author:  Matthew Leather
 *
 *
 */
package net.sourceforge.plantuml.theme;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.sprite.ResourcesUtils;
import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.utils.Log;

// ::uncomment when __CORE__
//import static com.plantuml.api.cheerpj.StaticMemory.cheerpjPath;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
// ::done

public class ThemeUtils {
	// ::remove folder when __HAXE__

	private static final String THEME_FILE_PREFIX = "puml-theme-";

	private static final String THEME_FILE_SUFFIX = ".puml";

	private static final String THEME_PATH = "themes";

	// ::comment when __CORE__ or __TEAVM__

	public static Theme loadTheme(PathSystem pathSystem, String name, String from, StringLocated location)
			throws IOException, EaterException {
		if (from == null)
			return loadBundledOrLocalTheme(pathSystem, name);

		if (from.startsWith("<") && from.endsWith(">"))
			return loadStdlibTheme(name, from);

		if (from.startsWith("http://") || from.startsWith("https://"))
			return loadHttpTheme(name, from, location);

		return loadFileTheme(pathSystem, name, from);
	}

	private static Theme loadBundledOrLocalTheme(PathSystem pathSystem, String name) throws IOException {
		// First, try bundled themes
		Log.info(() -> "Loading theme " + name);
		final String res = "/" + THEME_PATH + "/" + getFilename(name);
		final InputStream is = Stdlib.class.getResourceAsStream(res);
		if (is != null) {
			final String description = "<" + res + ">";
			return new Theme(ReadLineReader.create(new InputStreamReader(is), description));
		}

		// Then try local file
		final InputFile localFile = pathSystem.getInputFile(getFilename(name));
		if (localFile != null) {
			final Reader br = localFile.getReader(UTF_8);
			if (br != null)
				return new Theme(ReadLineReader.create(br, "theme " + name));

		}

		return null;
	}

	private static Theme loadStdlibTheme(String name, String from) {
		final String realFrom = from.substring(1, from.length() - 1);
		final String res = realFrom + "/" + getFilename(name);
		final byte[] content = Stdlib.getPumlResource(res);
		if (content == null)
			return null;

		final String description = name + " from " + from;
		return new Theme(ReadLineReader.create(content, description));
	}

	private static Theme loadHttpTheme(String name, String from, StringLocated location)
			throws UnsupportedEncodingException, EaterException {
		final SURL url = SURL.create(getFullPath(from, name));
		if (url == null)
			throw new EaterException("Cannot open URL", location);

		return new Theme(PreprocessorUtils.getReaderInclude(url, location, UTF_8));
	}

	private static String getFullPath(String from, String filename) {
		final StringBuilder sb = new StringBuilder(from);
		if (from.endsWith("/") == false)
			sb.append("/");

		return sb + getFilename(filename);
	}

	private static Theme loadFileTheme(PathSystem pathSystem, String name, String from) throws IOException {
		final InputFile file = pathSystem.getInputFile(from + getFilename(name));
		final InputStream is = file.newInputStream();
		if (is == null)
			return null;

		final String description = name + " from " + from;
		return new Theme(ReadLineReader.create(new InputStreamReader(is), description));
	}

	public static List<String> getAllThemeNames() throws IOException {
		final List<String> result = new ArrayList<>();
		final Collection<String> filenames = Objects.requireNonNull(ResourcesUtils.getJarFile(THEME_PATH, false));
		for (String f : filenames)
			if (f.startsWith(THEME_FILE_PREFIX) && f.endsWith(THEME_FILE_SUFFIX))
				result.add(f.substring(THEME_FILE_PREFIX.length(), f.length() - THEME_FILE_SUFFIX.length()));

		Collections.sort(result);
		return result;
	}
	// ::done

	public static String getFilename(String filename) {
		return THEME_FILE_PREFIX + filename + THEME_FILE_SUFFIX;
	}

}
