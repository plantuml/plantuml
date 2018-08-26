/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.preproc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.regex.Matcher2;
import net.sourceforge.plantuml.command.regex.MyPattern;
import net.sourceforge.plantuml.command.regex.Pattern2;
import net.sourceforge.plantuml.utils.StartUtils;

public class PreprocessorInclude extends ReadLineInstrumented implements ReadLine {

	private static final Pattern2 includeDefPattern = MyPattern.cmpile("^[%s]*!includedef[%s]+[%g]?([^%g]+)[%g]?$");
	private static final Pattern2 includePattern = MyPattern.cmpile("^[%s]*!include[%s]+[%g]?([^%g]+)[%g]?$");
	private static final Pattern2 includePatternStdlib = MyPattern.cmpile("^[%s]*!include[%s]+(\\<[^%g]+\\>)$");
	private static final Pattern2 includeManyPattern = MyPattern.cmpile("^[%s]*!include_many[%s]+[%g]?([^%g]+)[%g]?$");
	private static final Pattern2 includeURLPattern = MyPattern.cmpile("^[%s]*!includeurl[%s]+[%g]?([^%g]+)[%g]?$");

	private final ReadLine reader2;
	private final String charset;
	private final Defines defines;
	private final List<String> config;
	private final DefinitionsContainer definitionsContainer;

	private int numLine = 0;

	private PreprocessorInclude included = null;

	private final File oldCurrentDir;
	private final Set<FileWithSuffix> filesUsedCurrent;
	private final Set<FileWithSuffix> filesUsedGlobal;

	public PreprocessorInclude(List<String> config, ReadLine reader, Defines defines, String charset,
			File newCurrentDir, DefinitionsContainer definitionsContainer) {
		this(config, reader, defines, charset, newCurrentDir, new HashSet<FileWithSuffix>(),
				new HashSet<FileWithSuffix>(), definitionsContainer);
	}

	public Set<FileWithSuffix> getFilesUsedGlobal() {
		return Collections.unmodifiableSet(filesUsedGlobal);
	}

	private PreprocessorInclude(List<String> config, ReadLine reader, Defines defines, String charset,
			File newCurrentDir, Set<FileWithSuffix> filesUsedCurrent, Set<FileWithSuffix> filesUsedGlobal,
			DefinitionsContainer definitionsContainer) {
		this.config = config;
		this.defines = defines;
		this.charset = charset;
		this.reader2 = new ReadLineQuoteComment(reader);
		this.definitionsContainer = definitionsContainer;
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

	@Override
	CharSequence2 readLineInst() throws IOException {
		final CharSequence2 result = readLineInternal();
		if (result != null && StartUtils.isArobaseStartDiagram(result) && config.size() > 0) {
			final List<String> empty = new ArrayList<String>();
			included = new PreprocessorInclude(empty, new ReadLineList(config, result.getLocation()), defines, charset,
					null, filesUsedCurrent, filesUsedGlobal, definitionsContainer);
		}
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
		if (s.getPreprocessorError() == null && OptionFlags.ALLOW_INCLUDE) {
			assert included == null;
			final Matcher2 m1 = includePattern.matcher(s);
			if (m1.find()) {
				return manageFileInclude(s, m1, false);
			}
			final Matcher2 m2 = includeManyPattern.matcher(s);
			if (m2.find()) {
				return manageFileInclude(s, m2, true);
			}
			final Matcher2 m3 = includeDefPattern.matcher(s);
			if (m3.find()) {
				return manageDefinitionInclude(s, m3);
			}
		} else {
			final Matcher2 m1 = includePatternStdlib.matcher(s);
			if (m1.find()) {
				return manageFileInclude(s, m1, false);
			}
		}
		final Matcher2 mUrl = includeURLPattern.matcher(s);
		if (s.getPreprocessorError() == null && mUrl.find()) {
			return manageUrlInclude(s, mUrl);
		}
		return s;
	}

	private CharSequence2 manageUrlInclude(CharSequence2 s, Matcher2 m) throws IOException {
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
			included = new PreprocessorInclude(config, getReaderInclude(url, s, suf), defines, charset, null,
					filesUsedCurrent, filesUsedGlobal, definitionsContainer);
		} catch (MalformedURLException e) {
			return s.withErrorPreprocessor("Cannot include url " + urlString);
		}
		return this.readLine();
	}

	private CharSequence2 manageDefinitionInclude(CharSequence2 s, Matcher2 matcher) throws IOException {
		final String definitionName = matcher.group(1);
		final List<? extends CharSequence> definition = definitionsContainer.getDefinition(definitionName);
		included = new PreprocessorInclude(config, new ReadLineList(definition, s.getLocation()), defines, charset,
				null, filesUsedCurrent, filesUsedGlobal, definitionsContainer);
		return this.readLine();
	}

	private CharSequence2 manageFileInclude(CharSequence2 s, Matcher2 matcher, boolean allowMany) throws IOException {
		String fileName = matcher.group(1);
		fileName = defines.applyDefines(fileName).get(0);
		if (fileName.startsWith("<") && fileName.endsWith(">")) {
			final ReadLine strlibReader = getReaderStdlibInclude(s, fileName.substring(1, fileName.length() - 1));
			if (strlibReader == null) {
				return s.withErrorPreprocessor("Cannot include " + fileName);
			}
			included = new PreprocessorInclude(config, strlibReader, defines, charset, null, filesUsedCurrent,
					filesUsedGlobal, definitionsContainer);
			return this.readLine();
		}
		final int idx = fileName.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = fileName.substring(idx + 1);
			fileName = fileName.substring(0, idx);
		}
		// final File f = FileSystem.getInstance().getFile(withEnvironmentVariable(fileName));
		final FileWithSuffix f2 = new FileWithSuffix(withEnvironmentVariable(fileName), suf);
		if (f2.fileOk() == false) {
			return s.withErrorPreprocessor("Cannot include " + f2.getFile().getAbsolutePath());
		} else if (allowMany == false && filesUsedCurrent.contains(f2)) {
			// return CharSequence2Impl.errorPreprocessor("File already included " + f.getAbsolutePath(), lineLocation);
			return this.readLine();
		}
		filesUsedCurrent.add(f2);
		filesUsedGlobal.add(f2);
		included = new PreprocessorInclude(config, getReaderInclude(f2, s), defines, charset, f2.getParentFile(),
				filesUsedCurrent, filesUsedGlobal, definitionsContainer);
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

	private InputStream getStdlibInputStream(String filename) {
		final InputStream result = Stdlib.getResourceAsStream(filename);
		// Log.info("Loading sdlib " + filename + " ok");
		return result;
	}

	private ReadLine getReaderStdlibInclude(CharSequence2 s, String filename) {
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

	private ReadLine getReaderInclude(FileWithSuffix f2, CharSequence2 s) {
		try {
			if (StartDiagramExtractReader.containsStartDiagram(f2, s, charset)) {
				return StartDiagramExtractReader.build(f2, s, charset);
			}
			final Reader reader = f2.getReader(charset);
			if (reader == null) {
				return new ReadLineSimple(s, "Cannot open " + f2.getDescription());
			}
			return ReadLineReader.create(reader, f2.getDescription(), s.getLocation());
		} catch (IOException e) {
			e.printStackTrace();
			return new ReadLineSimple(s, e.toString());
		}

	}

	private ReadLine getReaderInclude(final URL url, CharSequence2 s, String suf) {
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
			return new ReadLineSimple(s, e.toString());
		}

	}

	public int getLineNumber() {
		return numLine;
	}

	@Override
	void closeInst() throws IOException {
		restoreCurrentDir();
		reader2.close();
	}

}
