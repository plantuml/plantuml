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
 * Modified by: Nicolas Jouanin
 * 
 *
 */
package net.sourceforge.plantuml.preproc2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.ReadLineSimple;
import net.sourceforge.plantuml.preproc.StartDiagramExtractReader;
import net.sourceforge.plantuml.preproc.Stdlib;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.utils.Log;

public class PreprocessorUtils {

	private static final Pattern p = Pattern.compile("%(\\w+)%");

	public static String withEnvironmentVariable(String s) {

		final Matcher m = p.matcher(s);
		final StringBuffer sb = new StringBuffer(); // Can't be switched to StringBuilder in order to support Java 8
		while (m.find()) {
			final String var = m.group(1);
			final String value = getenv(var);
			if (value != null)
				m.appendReplacement(sb, Matcher.quoteReplacement(value));

		}
		m.appendTail(sb);
		s = sb.toString();
		return s;
	}

	public static String getenv(String var) {
		final String env = System.getProperty(var);
		if (StringUtils.isNotEmpty(env))
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(env);

		final String getenv = System.getenv(var);
		if (StringUtils.isNotEmpty(getenv))
			return StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(getenv);

		return null;
	}

	// ::comment when __CORE__
	public static ReadLine getReaderNonstandardInclude(StringLocated s, String filename) {
		if (filename.endsWith(".puml") == false)
			filename = filename + ".puml";

		final String filename2 = filename;
		Log.info(() -> "Loading non standard " + filename2);
		final String res = "/stdlib/" + filename;
		InputStream is = Stdlib.class.getResourceAsStream(res);

		if (is == null)
			return null;

		final String description = "[" + filename + "]";
		return ReadLineReader.create(new InputStreamReader(is), description);
	}
	// ::done

	public static ReadLine getReaderStdlibInclude(StringLocated s, String filename) throws IOException {
		Log.info(() -> "Loading sdlib " + filename);
		final byte[] puml = Stdlib.getPumlResource(filename);
		if (puml == null)
			return null;

		final String description = "<" + filename + ">";
		try {
			if (StartDiagramExtractReader.containsStartDiagram(new ByteArrayInputStream(puml), description))
				return StartDiagramExtractReader.build(new ByteArrayInputStream(puml), description);

			return ReadLineReader.create(puml, description);
		} catch (IOException e) {
			Logme.error(e);
			return new ReadLineSimple(s, e.toString());
		}
	}

//	public static ReadLine getReaderStdlibIncludeSprites(StringLocated s, String root) throws IOException {
//		final Stdlib lib = Stdlib.retrieve(root);
//		final Collection<String> filenames = lib.getAllFilenamesWithSprites();
//		final List<ReadLine> readers = new ArrayList<>();
//
//		for (String name : filenames) {
//			final String data = lib.loadResource(name);
//			final InputStream is = new ByteArrayInputStream(data.getBytes(UTF_8));
//			readers.add(StartDiagramExtractReader.build(is, "<" + root + "/" + name + ">"));
//		}
//		return new ReadLineConcat(readers);
//	}

	public static ReadLine getReaderIncludeUrl(final SURL url, StringLocated s, String suf, Charset charset)
			throws EaterException {
		try {
			if (StartDiagramExtractReader.containsStartDiagram(url, s, charset))
				return StartDiagramExtractReader.build(url, s, suf, charset);

			return getReaderInclude(url, s, charset);
		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("Cannot open URL " + e.getMessage(), s);
		}

	}

	public static ReadLine getReaderInclude(SURL url, StringLocated s, Charset charset)
			throws EaterException, UnsupportedEncodingException {
		final InputStream is = url.openStream();
		if (is == null)
			throw new EaterException("Cannot open URL", s);

		return ReadLineReader.create(new InputStreamReader(is, charset), url.toString(), s.getLocation());
	}

}
