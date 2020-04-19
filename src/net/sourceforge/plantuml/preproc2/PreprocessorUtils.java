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
 * Modified by: Nicolas Jouanin
 * 
 *
 */
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.ReadLineSimple;
import net.sourceforge.plantuml.preproc.StartDiagramExtractReader;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.tim.EaterException;

public class PreprocessorUtils {

	public static String withEnvironmentVariable(String s) {
		final Pattern p = Pattern.compile("%(\\w+)%");

		final Matcher m = p.matcher(s);
		final StringBuffer sb = new StringBuffer();
		while (m.find()) {
			final String var = m.group(1);
			final String value = getenv(var);
			if (value != null) {
				m.appendReplacement(sb, Matcher.quoteReplacement(value));
			}
		}
		m.appendTail(sb);
		s = sb.toString();
		return s;
	}

	public static String getenv(String var) {
		final String env = System.getProperty(var);
		if (StringUtils.isNotEmpty(env)) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env);
		}
		final String getenv = System.getenv(var);
		if (StringUtils.isNotEmpty(getenv)) {
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(getenv);
		}
		return null;
	}

	private static InputStream getStdlibInputStream(String filename) {
		final InputStream result = Stdlib.getResourceAsStream(filename);
		// Log.info("Loading sdlib " + filename + " ok");
		return result;
	}

	public static ReadLine getReaderStdlibInclude(StringLocated s, String filename) {
		Log.info("Loading sdlib " + filename);
		InputStream is = getStdlibInputStream(filename);
		if (is == null) {
			return null;
		}
		final String description = "<" + filename + ">";
		try {
			if (StartDiagramExtractReader.containsStartDiagram(is, s, description)) {
				is = getStdlibInputStream(filename);
				return StartDiagramExtractReader.build(is, s, description);
			}
			is = getStdlibInputStream(filename);
			if (is == null) {
				return null;
			}
			return ReadLineReader.create(new InputStreamReader(is), description);
		} catch (IOException e) {
			e.printStackTrace();
			return new ReadLineSimple(s, e.toString());
		}
	}

	public static ReadLine getReaderIncludeUrl2(final URL url, StringLocated s, String suf, String charset)
			throws EaterException {
		try {
			if (StartDiagramExtractReader.containsStartDiagram(url, s, charset)) {
				return StartDiagramExtractReader.build(url, s, suf, charset);
			}
			final InputStream is = url.openStream();
			if (charset == null) {
				Log.info("Using default charset");
				return ReadLineReader.create(new InputStreamReader(is), url.toString(), s.getLocation());
			}
			Log.info("Using charset " + charset);
			return ReadLineReader.create(new InputStreamReader(is, charset), url.toString(), s.getLocation());
		} catch (IOException e) {
			e.printStackTrace();
			throw EaterException.located("Cannot open URL " + e.getMessage(), s);
		}

	}

}
