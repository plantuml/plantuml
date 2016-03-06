/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * Modified by: Nicolas Jouanin
 * 
 * Revision $Revision: 19109 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.utils.StartUtils;

class PreprocessorInclude implements ReadLine {

	private static final Pattern includePattern = MyPattern.cmpile("^[%s]*!include[%s]+[%g]?([^%g]+)[%g]?$");
	private static final Pattern includeURLPattern = MyPattern.cmpile("^[%s]*!includeurl[%s]+[%g]?([^%g]+)[%g]?$");

	private final ReadLine reader2;
	private final String charset;
	private final Defines defines;

	private int numLine = 0;

	private PreprocessorInclude included = null;

	private final File oldCurrentDir;
	private final Set<FileWithSuffix> filesUsedCurrent;
	private final Set<FileWithSuffix> filesUsedGlobal;

	public PreprocessorInclude(ReadLine reader, Defines defines, String charset, File newCurrentDir) {
		this(reader, defines, charset, newCurrentDir, new HashSet<FileWithSuffix>(), new HashSet<FileWithSuffix>());
	}

	public Set<FileWithSuffix> getFilesUsedGlobal() {
		return Collections.unmodifiableSet(filesUsedGlobal);
	}

	private PreprocessorInclude(ReadLine reader, Defines defines, String charset, File newCurrentDir,
			Set<FileWithSuffix> filesUsedCurrent, Set<FileWithSuffix> filesUsedGlobal) {
		this.defines = defines;
		this.charset = charset;
		this.reader2 = reader;
		this.filesUsedCurrent = filesUsedCurrent;
		this.filesUsedGlobal = filesUsedGlobal;
		if (newCurrentDir == null) {
			oldCurrentDir = null;
		} else {
			oldCurrentDir = FileSystem.getInstance().getCurrentDir();
			FileSystem.getInstance().setCurrentDir(newCurrentDir);
		}
	}

	private void restoreCurrentDir() {
		if (oldCurrentDir != null) {
			FileSystem.getInstance().setCurrentDir(oldCurrentDir);
		}
	}

	public CharSequence2 readLine() throws IOException {
		final CharSequence2 result = readLineInternal();
		if (result != null && (StartUtils.isArobaseEndDiagram(result) || StartUtils.isArobaseStartDiagram(result))) {
			// http://plantuml.sourceforge.net/qa/?qa=3389/error-generating-when-same-file-included-different-diagram
			filesUsedCurrent.clear();
		}
		return result;
	}

	private CharSequence2 readLineInternal() throws IOException {
		if (included != null) {
			final CharSequence2 s = included.readLine();
			if (s != null) {
				return s;
			}
			included.close();
			included = null;
		}

		final CharSequence2 s = reader2.readLine();
		numLine++;
		if (s == null) {
			return null;
		}
		if (OptionFlags.ALLOW_INCLUDE) {
			final Matcher m = includePattern.matcher(s);
			assert included == null;
			if (m.find()) {
				return manageFileInclude(m, s.getLocation());
			}
		}
		final Matcher mUrl = includeURLPattern.matcher(s);
		if (mUrl.find()) {
			return manageUrlInclude(mUrl, s.getLocation());
		}
		return s;
	}

	private CharSequence2 manageUrlInclude(Matcher m, LineLocation lineLocation) throws IOException {
		String urlString = m.group(1);
		urlString = defines.applyDefines(urlString).get(0);
		//
		final int idx = urlString.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = urlString.substring(idx + 1);
			urlString = urlString.substring(0, idx);
		}
		try {
			final URL url = new URL(urlString);
			included = new PreprocessorInclude(getReaderInclude(url, suf, lineLocation), defines, charset, null, filesUsedCurrent,
					filesUsedGlobal);
		} catch (MalformedURLException e) {
			return CharSequence2Impl.errorPreprocessor("Cannot include url " + urlString, lineLocation);
		}
		return this.readLine();
	}

	private CharSequence2 manageFileInclude(Matcher m, LineLocation lineLocation) throws IOException {
		String fileName = m.group(1);
		fileName = defines.applyDefines(fileName).get(0);
		final int idx = fileName.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = fileName.substring(idx + 1);
			fileName = fileName.substring(0, idx);
		}
		final File f = FileSystem.getInstance().getFile(withEnvironmentVariable(fileName));
		final FileWithSuffix f2 = new FileWithSuffix(f, suf);
		if (f.exists() == false) {
			return CharSequence2Impl.errorPreprocessor("Cannot include " + f.getAbsolutePath(), lineLocation);
		} else if (filesUsedCurrent.contains(f2)) {
			// return CharSequence2Impl.errorPreprocessor("File already included " + f.getAbsolutePath(), lineLocation);
			return this.readLine();
		} else {
			filesUsedCurrent.add(f2);
			filesUsedGlobal.add(f2);
			included = new PreprocessorInclude(getReaderInclude(f, suf, lineLocation), defines, charset, f.getParentFile(),
					filesUsedCurrent, filesUsedGlobal);
		}
		return this.readLine();
	}

	static String withEnvironmentVariable(String s) {
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

	private static String getenv(String var) {
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

	private ReadLine getReaderInclude(final File f, String suf, LineLocation parent) throws IOException {
		if (StartDiagramExtractReader.containsStartDiagram(f, charset)) {
			int bloc = 0;
			if (suf != null && suf.matches("\\d+")) {
				bloc = Integer.parseInt(suf);
			}
			return new StartDiagramExtractReader(f, bloc, charset);
		}
		if (charset == null) {
			Log.info("Using default charset");
			return new ReadLineReader(new FileReader(f), f.getAbsolutePath(), parent);
		}
		Log.info("Using charset " + charset);
		return new ReadLineReader(new InputStreamReader(new FileInputStream(f), charset), f.getAbsolutePath(), parent);
	}

	private ReadLine getReaderInclude(final URL url, String suf, LineLocation parent) throws IOException {
		if (StartDiagramExtractReader.containsStartDiagram(url, charset)) {
			int bloc = 0;
			if (suf != null && suf.matches("\\d+")) {
				bloc = Integer.parseInt(suf);
			}
			return new StartDiagramExtractReader(url, bloc, charset);
		}
		final InputStream is = url.openStream();
		if (charset == null) {
			Log.info("Using default charset");
			return new ReadLineReader(new InputStreamReader(is), url.toString(), parent);
		}
		Log.info("Using charset " + charset);
		return new ReadLineReader(new InputStreamReader(is, charset), url.toString(), parent);
	}

	public int getLineNumber() {
		return numLine;
	}

	public void close() throws IOException {
		restoreCurrentDir();
		reader2.close();
	}

}
