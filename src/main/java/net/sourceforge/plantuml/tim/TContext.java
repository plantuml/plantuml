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
 */
package net.sourceforge.plantuml.tim;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.nio.InputFile;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.Environment;
import net.sourceforge.plantuml.preproc.DiagramDetector;
import net.sourceforge.plantuml.preproc.DiagramExtractor;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineList;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.ReadLineWithYamlHeader;
import net.sourceforge.plantuml.preproc.Sub;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.preproc2.PreprocessorIncludeStrategy;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.preproc2.ReadFilterMergeLines;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;
import net.sourceforge.plantuml.theme.Theme;
import net.sourceforge.plantuml.tim.expression.Knowledge;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.iterator.CodeIterator;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorAffectation;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorForeach;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorIf;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorImpl;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorInnerComment;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorLegacyDefine;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorLongComment;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorProcedure;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorReturnFunction;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorShortComment;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorSub;
import net.sourceforge.plantuml.tim.iterator.CodeIteratorWhile;
import net.sourceforge.plantuml.utils.LineLocation;
import net.sourceforge.plantuml.utils.MyCollections;

public class TContext {

	private static final Pattern NEWLINE = Pattern.compile("\n");

	private final List<StringLocated> resultList = new ArrayList<>();
	private final List<StringLocated> debug = new ArrayList<>();

	public final FunctionsSet functionsSet = new FunctionsSet(StandardFunctions.get());

	private final Environment environment;

	private final Charset charset;

	private final Map<String, Sub> subs = new HashMap<String, Sub>();
	private final DefinitionsContainer definitionsContainer;

	private final Set<File> filesUsedCurrent = new HashSet<>();
	/**
	 * What the include strategies count as included: a file with its selector,
	 * as FileWithSuffix did, so that two diagrams of one file
	 * (<code>file!0</code>, <code>file!1</code>) are two includes. A local file
	 * is its canonical path; a file the browser host delivered is the
	 * identifier the host gave it, a string compared as it is, which a File
	 * would normalise.
	 */
	private final Set<List<String>> includedCurrent = new HashSet<>();

	private final PreprocessingArtifact preprocessingArtifact = new PreprocessingArtifact();
	private PathSystem pathSystem;

	public Set<File> getFilesUsedCurrent() {
		return MyCollections.unmodifiableSet(filesUsedCurrent);
	}

	public TContext(PathSystem pathSystem, Environment environment, Charset charset,
			DefinitionsContainer definitionsContainer) {
		this.environment = requireNonNull(environment);
		this.pathSystem = pathSystem;
		this.definitionsContainer = definitionsContainer;
		this.charset = requireNonNull(charset);
	}

	/**
	 * Returns a value of the environment (see {@link Environment}), or
	 * <code>null</code> if there is none.
	 */
	public String getEnvironmentValue(String key) {
		return environment.get(key);
	}

	public Knowledge asKnowledge(final TMemory memory, final LineLocation location) {
		return new Knowledge() {

			public TValue getVariable(String name) throws EaterException {
				if (name.contains(".") || name.contains("[")) {
					final TValue result = fromJson(memory, name, location);
					return result;
				}
				return memory.getVariable(name);
			}

			public TFunction getFunction(TFunctionSignature name) {
				return functionsSet.getFunctionSmart(name);
			}
		};
	}

	private TValue fromJson(TMemory memory, String name, LineLocation location) throws EaterException {
		final String result = applyFunctionsAndVariables(memory, new StringLocated(name, location));
		try {
			final JsonValue json = Json.parse(result);
			return TValue.fromJson(json);
		} catch (Exception e) {
			return TValue.fromString(result);
		}
	}

	private CodeIterator buildCodeIterator(TMemory memory, List<StringLocated> body) {
		final CodeIterator it10 = new CodeIteratorImpl(body);
		final CodeIterator it20 = new CodeIteratorLongComment(it10, debug);
		final CodeIterator it30 = new CodeIteratorShortComment(it20, debug);
		final CodeIterator it40 = new CodeIteratorInnerComment(it30);
		final CodeIterator it50 = new CodeIteratorSub(it40, subs, this, memory);
		final CodeIterator it60 = new CodeIteratorReturnFunction(it50, this, memory, functionsSet, debug);
		final CodeIterator it61 = new CodeIteratorProcedure(it60, this, memory, functionsSet, debug);
		final CodeIterator it70 = new CodeIteratorIf(it61, this, memory, debug);
		final CodeIterator it80 = new CodeIteratorLegacyDefine(it70, this, memory, functionsSet, debug);
		final CodeIterator it90 = new CodeIteratorWhile(it80, this, memory, debug);
		final CodeIterator it100 = new CodeIteratorForeach(it90, this, memory, debug);
		final CodeIterator it110 = new CodeIteratorAffectation(it100, this, memory, debug);

		final CodeIterator it = it110;
		return it;
	}

	public TValue executeLines(TMemory memory, List<StringLocated> body, TFunctionType ftype, boolean modeSpecial)
			throws EaterException {
		BrowserLog.consoleLog(TContext.class, "executeLines start (" + body.size() + " lines)");
		final CodeIterator it = buildCodeIterator(memory, body);

		StringLocated s = null;
		while ((s = it.peek()) != null) {
			final TValue result = executeOneLineSafe(memory, s, ftype, modeSpecial);
			if (result != null) {
				BrowserLog.consoleLog(TContext.class, "executeLines ok -> " + result);
				return result;
			}
			it.next();
		}
		BrowserLog.consoleLog(TContext.class, "executeLines return null");
		return null;

	}

	private void executeLinesInternal(TMemory memory, List<StringLocated> body, TFunctionType ftype)
			throws EaterException {
		final CodeIterator it = buildCodeIterator(memory, body);

		StringLocated s = null;
		while ((s = it.peek()) != null) {
			executeOneLineSafe(memory, s, ftype, false);
			it.next();
		}

	}

	private TValue executeOneLineSafe(TMemory memory, StringLocated s, TFunctionType ftype, boolean modeSpecial)
			throws EaterException {
		try {
			this.debug.add(s);
			return executeOneLineNotSafe(memory, s, ftype, modeSpecial);
		} catch (Exception e) {
			if (e instanceof EaterException)
				throw (EaterException) e;
			Logme.error(e);
			throw new EaterException("Fatal parsing error", s);
		}
	}

	private TValue executeOneLineNotSafe(TMemory memory, StringLocated s, TFunctionType ftype, boolean modeSpecial)
			throws EaterException {
		final TLineType type = s.getType();

		if (type == TLineType.INCLUDESUB) {
			this.executeIncludesub(memory, s);
			return null;
		} else if (type == TLineType.THEME) {
			this.executeTheme(memory, s);
			return null;
		} else if (type == TLineType.INCLUDE) {
			this.executeInclude(memory, s);
			return null;
//		} else if (type == TLineType.INCLUDE_SPRITES) {
//			this.executeIncludeSprites(memory, s);
//			return null;
		} else if (type == TLineType.INCLUDE_DEF) {
			this.executeIncludeDef(memory, s);
			return null;
		} else if (type == TLineType.IMPORT) {
			this.executeImport(memory, s);
			return null;
		}
		if (type == TLineType.DUMP_MEMORY) {
			this.executeDumpMemory(memory, s.getTrimmed());
			return null;
		} else if (type == TLineType.ASSERT) {
			this.executeAssert(memory, s.getTrimmed());
			return null;
		} else if (type == TLineType.OPTION) {
			this.executeOption(memory, s.getTrimmed());
			return null;
		} else if (type == TLineType.UNDEF) {
			this.executeUndef(memory, s);
			return null;
		} else if (ftype != TFunctionType.RETURN_FUNCTION && type == TLineType.PLAIN) {
			this.addPlain(memory, s);
			return null;
		} else if (ftype == TFunctionType.RETURN_FUNCTION && type == TLineType.RETURN) {
			if (modeSpecial) {
				final EaterReturn eaterReturn = new EaterReturn(s);
				eaterReturn.analyze(this, memory);
				final TValue result = eaterReturn.getValue2();
				return result;
			}
			// Actually, ignore because we are in a if
			return null;
		} else if (ftype == TFunctionType.RETURN_FUNCTION && type == TLineType.PLAIN) {
			this.simulatePlain(memory, s);
			return null;
		} else if (type == TLineType.AFFECTATION_DEFINE) {
			this.executeAffectationDefine(memory, s);
			return null;
		} else if (ftype == null && type == TLineType.END_FUNCTION) {
			CommandExecutionResult.error("error endfunc");
			return null;
		} else if (type == TLineType.LOG) {
			this.executeLog(memory, s);
			return null;
		} else if (ONLY_WHITESPACE_NON_EMPTY.matcher(s.getString()).matches()) {
			return null;
		} else {
			throw new EaterException("Compile Error " + ftype + " " + type, s);
		}
	}

	private static final Pattern ONLY_WHITESPACE_NON_EMPTY = Pattern.compile("^\\s+$");

	private void addPlain(TMemory memory, StringLocated s) throws EaterException {
		final StringLocated tmp[] = applyFunctionsAndVariablesInternal(memory, s);
		if (tmp != null) {
			if (pendingAdd != null) {
				tmp[0] = new StringLocated(pendingAdd + tmp[0].getString(), tmp[0].getLocation());
				pendingAdd = null;
			}
			for (StringLocated line : tmp)
				addToResultList(line);

		}
	}

	private boolean addToResultList(StringLocated line) {
		if (Jaws.TRACE)
			System.err.println("adding " + line);
		return resultList.add(line);
	}

	private void simulatePlain(TMemory memory, StringLocated s) throws EaterException {
		final StringLocated ignored[] = applyFunctionsAndVariablesInternal(memory, s);
	}

	private void executeAffectationDefine(TMemory memory, StringLocated s) throws EaterException {
		new EaterAffectationDefine(s).analyze(this, memory);
	}

	private void executeDumpMemory(TMemory memory, StringLocated s) throws EaterException {
		final EaterDumpMemory condition = new EaterDumpMemory(s);
		condition.analyze(this, memory);
	}

	private void executeAssert(TMemory memory, StringLocated s) throws EaterException {
		final EaterAssert condition = new EaterAssert(s);
		condition.analyze(this, memory);
	}

	private void executeOption(TMemory memory, StringLocated s) throws EaterException {
		final EaterOption condition = new EaterOption(s);
		condition.analyze(this, memory);
	}

	private void executeUndef(TMemory memory, StringLocated s) throws EaterException {
		final EaterUndef undef = new EaterUndef(s);
		undef.analyze(this, memory);
	}

	@JawsStrange
	private StringLocated[] applyFunctionsAndVariablesInternal(TMemory memory, StringLocated located)
			throws EaterException {
		final String result = applyFunctionsAndVariables(memory, located);
		if (result == null)
			return null;

		if (Pragma.legacyReplaceBackslashNByNewline()) {
			final String[] splited = NEWLINE.split(result);
			final StringLocated[] tab = new StringLocated[splited.length];
			for (int i = 0; i < splited.length; i++)
				tab[i] = new StringLocated(splited[i], located.getLocation());

			return tab;
		}
		if (result.contains("\n"))
			throw new IllegalStateException(result);
		return new StringLocated[] { new StringLocated(result, located.getLocation()) };

	}

	private String pendingAdd = null;

	@JawsStrange
	public String applyFunctionsAndVariables(TMemory memory, final StringLocated str) throws EaterException {
		// https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm
		// https://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most
		// https://en.wikipedia.org/wiki/String-searching_algorithm
		// https://www.quora.com/What-is-the-most-efficient-algorithm-to-replace-all-occurrences-of-a-pattern-P-in-a-string-with-a-pattern-P
		// https://en.wikipedia.org/wiki/Trie
		final StringBuilder result = new StringBuilder();
		// One VariableManager for the whole line: it only holds final references, and building
		// two of them per character was a visible share of the preprocessor allocations.
		final VariableManager variableManager = new VariableManager(this, memory, str);
		for (int i = 0; i < str.length(); i++) {
			final char c = str.charAt(i);
			final String presentFunction = getFunctionNameAt(str.getString(), i);
			if (presentFunction != null) {
				final String sub = str.getString().substring(i);
				final EaterFunctionCall call = new EaterFunctionCall(new StringLocated(sub, str.getLocation()),
						isLegacyDefine(presentFunction), isUnquoted(presentFunction));
				call.analyze(this, memory);
				final TFunctionSignature signature = new TFunctionSignature(presentFunction, call.getValues().size(),
						call.getNamedArguments().keySet());
				final TFunction function = functionsSet.getFunctionSmart(signature);
				if (function == null)
					throw new EaterException("Function not found " + presentFunction, str);

				if (function.getFunctionType() == TFunctionType.PROCEDURE) {
					this.pendingAdd = result.toString();
					executeVoid3(str, memory, function, call);
					i += call.getCurrentPosition();
					final String remaining = str.getString().substring(i);
					if (remaining.length() > 0)
						appendToLastResult(remaining);

					return null;
				}
				if (function.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
					this.pendingAdd = str.getString().substring(0, i);
					executeVoid3(str, memory, function, call);
					return null;
				}
				if (TeaVM.a())
					assert function.getFunctionType() == TFunctionType.RETURN_FUNCTION
							|| function.getFunctionType() == TFunctionType.LEGACY_DEFINE;
				final TValue functionReturn = function.executeReturnFunction(this, memory, str, call.getValues(),
						call.getNamedArguments());
				String tmp = functionReturn.toString();
				// if (tmp.indexOf(Jaws.BLOCK_E1_NEWLINE) > 0)
				// System.err.println("tmp=" + tmp + " (" + function.getFunctionType() + ")");
				// if (function.getFunctionType() == TFunctionType.RETURN_FUNCTION &&
				// tmp.length() > 1) {
				// System.err.println("JE REPLACE");
				// tmp = StringLocated.expandsJaws32(tmp);
				// tmp = tmp.replace(Jaws.BLOCK_E1_NEWLINE, '\n');
				// System.err.println("DONC tmp=" + tmp);
				// }
				result.append(tmp);
				i += call.getCurrentPosition() - 1;
			} else if (variableManager.getVarnameAt(str.getString(), i) != null) {
				i = variableManager.replaceVariables(str.getString(), i, result);
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	private void appendToLastResult(String remaining) {
		final StringLocated last = this.resultList.get(this.resultList.size() - 1);
		this.resultList.set(this.resultList.size() - 1, last.append(remaining));
	}

	private void executeVoid3(StringLocated location, TMemory memory, TFunction function, EaterFunctionCall call)
			throws EaterException {
		function.executeProcedureInternal(this, memory, location, call.getValues(), call.getNamedArguments());
	}

	private void executeImport(TMemory memory, StringLocated s) throws EaterException {
		if (!TeaVM.isTeaVM()) {
			final EaterImport _import = new EaterImport(s.getTrimmed());
			_import.analyze(this, memory);

			try {
				final String what = applyFunctionsAndVariables(memory,
						new StringLocated(_import.getWhat(), s.getLocation()));
				// Same lookup as !include (relative to the directory of the current diagram)
				// and only then the historical one, based on the global current directory.
				final boolean special = what.startsWith("<") || what.startsWith("http://") || what.startsWith("https://");
				final InputFile found = special ? null : pathSystem.getInputFile(what);
				final SFile file = found instanceof SFile ? (SFile) found : FileSystem.getInstance().getFile(what);
				if (file.exists() && file.isDirectory() == false) {
					pathSystem.addImportFile(file);
					return;
				}
			} catch (IOException e) {
				Logme.error(e);
				throw new EaterException("Cannot import " + e.getMessage(), s);
			}
		}
		throw new EaterException("Cannot import", s);
	}

	private void executeLog(TMemory memory, StringLocated s) throws EaterException {
		final EaterLog log = new EaterLog(s.getTrimmed());
		log.analyze(this, memory);
	}

//	public FileWithSuffix getFileWithSuffix(String from, String realName) throws IOException {
//		throw new IOException("to be finished");
////		final String s = ThemeUtils.getFullPath(from, realName);
////		final FileWithSuffix file = importedFiles.getFile(s, null);
////		return file;
//
//	}

	private void executeIncludesub(TMemory memory, StringLocated s) throws EaterException {
		if (!TeaVM.isTeaVM()) {
			PathSystem saveImportedFiles = null;
			try {
				final EaterIncludesub include = new EaterIncludesub(s.getTrimmed());
				include.analyze(this, memory);
				final String what = include.getWhat();
				final int idx = what.indexOf('!');
				Sub sub = null;
				if (idx != -1) {
					final String filename = what.substring(0, idx);
					final String blocname = what.substring(idx + 1);
					try {
						final InputFile f2 = pathSystem.getFile(filename, null);
						if (f2 != null) {
							saveImportedFiles = this.pathSystem;
							this.pathSystem = this.pathSystem.withCurrentDir(f2.getParentFolder());
							final Reader reader = f2.getReader(charset);
							if (reader == null)
								throw new EaterException("cannot include " + what, s);

							try {
								ReadLine readerline = ReadLineReader.create(reader, what, s.getLocation());
								readerline = new UncommentReadLine(readerline);
								readerline = new ReadFilterMergeLines().applyFilter(readerline);
								sub = Sub.fromFile(readerline, blocname, this, memory);
							} finally {
								reader.close();
							}
						}
					} catch (IOException e) {
						Logme.error(e);
						throw new EaterException("cannot include " + what, s);
					}
				}
				if (sub == null)
					sub = subs.get(what);

				if (sub == null)
					throw new EaterException("cannot include " + what, s);

				executeLinesInternal(memory, sub.lines(), null);
			} finally {
				if (saveImportedFiles != null)
					this.pathSystem = saveImportedFiles;

			}
		}
	}

	private void executeIncludeDef(TMemory memory, StringLocated s) throws EaterException {
		if (!TeaVM.isTeaVM()) {

			final EaterIncludeDef include = new EaterIncludeDef(s.getTrimmed());
			include.analyze(this, memory);
			final String definitionName = include.getLocation();
			final List<String> definition = definitionsContainer.getDefinition(definitionName);
			final ReadLine reader2 = new ReadLineList(definition, s.getLocation());

			try {
				final List<StringLocated> body = new ArrayList<>();
				do {
					final StringLocated sl = reader2.readLine();
					if (sl == null) {
						executeLinesInternal(memory, body, null);
						return;
					}
					body.add(sl);
				} while (true);
			} catch (IOException e) {
				Logme.error(e);
				throw new EaterException("" + e, s);
			} finally {
				try {
					reader2.close();
				} catch (IOException e) {
					Logme.error(e);
				}
			}
		}
	}

	private JsonObject themeMetadata = new JsonObject();

	public JsonObject getThemeMetadata() {
		return themeMetadata;
	}

	private void executeTheme(TMemory memory, StringLocated s) throws EaterException {
		final EaterTheme eater = new EaterTheme(s.getTrimmed(), pathSystem);
		eater.analyze(this, memory);
		final Theme theme = eater.getTheme();
		if (theme == null)
			throw new EaterException("No such theme " + eater.getName(), s);

		final PathSystem saveImportedFiles = this.pathSystem;
		this.pathSystem = eater.getNewImportedFiles();
		if (TeaVM.isTeaVM())
			// A bundled theme is the engine's own: a relative include written in it
			// must not reach the browser host (restored in the finally below).
			this.pathSystem = this.pathSystem.withoutHostFiles();

		try {
			final List<StringLocated> body = new ArrayList<>();
			do {
				final StringLocated sl = theme.readLine();
				if (sl == null) {
					executeLines(memory, body, null, false);
					return;
				}
				body.add(sl);
			} while (true);
		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("Error reading theme " + e, s);
		} finally {
			this.themeMetadata = theme.getMetadata();
			this.pathSystem = saveImportedFiles;
			try {
				theme.close();
			} catch (IOException e) {
				Logme.error(e);
			}
		}
	}

//	private void executeIncludeSprites(TMemory memory, StringLocated s) throws EaterException {
//		final EaterIncludeSprites include = new EaterIncludeSprites(s.getTrimmed());
//		include.analyze(this, memory);
//		final String what = include.getWhat();
//		if (what.startsWith("<") && what.endsWith(">")) {
//			ReadLine reader = null;
//			try {
//				reader = PreprocessorUtils.getReaderStdlibIncludeSprites(s, what.substring(1, what.length() - 1));
//				final List<StringLocated> body = new ArrayList<>();
//				do {
//					final StringLocated sl = reader.readLine();
//					if (sl == null) {
//						executeLines(memory, body, null, false);
//						return;
//					}
//					body.add(sl);
//				} while (true);
//			} catch (IOException e) {
//				Logme.error(e);
//				throw new EaterException("cannot include " + e, s);
//			} finally {
//				if (reader != null)
//					try {
//						reader.close();
//					} catch (IOException e) {
//						Logme.error(e);
//					}
//			}
//
//		}
//		throw new EaterException("cannot include sprites from " + what, s);
//	}

	private void executeInclude(TMemory memory, StringLocated s) throws EaterException {
		final EaterInclude include = new EaterInclude(s.getTrimmed());
		include.analyze(this, memory);
		String what = include.getWhat();
		final PreprocessorIncludeStrategy strategy = include.getPreprocessorIncludeStrategy();
		final int idx = what.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = what.substring(idx + 1);
			what = what.substring(0, idx);
		}

		ReadLine reader = null;
		PathSystem saveImportedFiles = null;
		try {
			if (what.startsWith("<") && what.endsWith(">")) {
				final String stdlibPath = what.substring(1, what.length() - 1);
				saveImportedFiles = this.pathSystem;
				if (TeaVM.isTeaVM()) {
					final InputStream is = this.pathSystem.getTeaVMStdlibInputStream(what);
					if (is != null) {
						// The library's file is the engine's own: a relative include written
						// in it must not reach the browser host (restored with the rest).
						this.pathSystem = this.pathSystem.withoutHostFiles();
						reader = ReadLineReader.create(new InputStreamReader(is), what);
					}
				} else {
					InputFile tmp = this.pathSystem.getInputFile(what);
					this.pathSystem = this.pathSystem.changeCurrentDirectory(tmp.getParentFolder());
					reader = PreprocessorUtils.getReaderStdlibInclude(s, stdlibPath);
				}
			} else if (what.startsWith("http://") || what.startsWith("https://")) {
				if (!TeaVM.isTeaVM()) {
					final SURL url = SURL.create(what);
					if (url == null)
						throw new EaterException("Cannot open URL", s);

					reader = PreprocessorUtils.getReaderIncludeUrl(url, s, suf, charset);
				}
			} else if (what.startsWith("[") && what.endsWith("]")) {
				throw new IOException("To be finished");
				// reader = PreprocessorUtils.getReaderNonstandardInclude(s, what.substring(1,
				// what.length() - 1));
			} else {
				final InputFile f2 = this.pathSystem.getInputFile(what);
				if (f2 != null) {
					final File used = f2 instanceof SFile ? ((SFile) f2).getCanonicalFile().conv() : null;
					final String identity = used != null ? used.getPath() : this.pathSystem.getTeaVMFileId(f2);
					final List<String> included = identity == null ? null : Arrays.asList(identity, suf);
					final boolean seen = included != null && includedCurrent.contains(included);
					if (strategy == PreprocessorIncludeStrategy.DEFAULT && seen)
						return;

					if (strategy == PreprocessorIncludeStrategy.ONCE && seen)
						throw new EaterException("This file has already been included", s);

					if (used != null)
						filesUsedCurrent.add(used);

					try {
						reader = DiagramDetector.extractFromFile(f2, "desc2", suf);
					} catch (NumberFormatException | PatternSyntaxException e) {
						throw new EaterException("cannot include " + what + "!" + suf, s);
					}
					if (reader instanceof DiagramExtractor && ((DiagramExtractor) reader).isFound() == false)
						throw new EaterException("cannot include " + what + "!" + suf, s);

					if (reader == null) {
						final Reader tmp = f2.getReader(charset);
						if (tmp == null)
							throw new EaterException("Cannot include file", s);

						reader = ReadLineReader.create(tmp, what, s.getLocation());
					}
					saveImportedFiles = this.pathSystem;
					this.pathSystem = this.pathSystem.withCurrentDir(f2.getParentFolder());
					if (TeaVM.a())
						assert reader != null;
					if (included != null)
						includedCurrent.add(included);
				}
			}
			if (reader != null)
				try {
					final List<StringLocated> body = new ArrayList<>();
					reader = new ReadLineWithYamlHeader(reader);
					do {
						final StringLocated sl = reader.readLine();
						if (sl == null) {
							executeLines(memory, body, null, false);
							return;
						}
						body.add(sl);
					} while (true);
				} finally {
					if (saveImportedFiles != null)
						this.pathSystem = saveImportedFiles;
				}

		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("cannot include " + e, s);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					Logme.error(e);
				}
		}
		throw new EaterException("cannot include " + what, s);
	}

	public boolean isLegacyDefine(String functionName) {
		for (TFunction func : functionsSet.getFunctionsByName(functionName))
			if (func.getFunctionType().isLegacy())
				return true;

		return false;
	}

	public boolean isUnquoted(String functionName) {
		for (TFunction func : functionsSet.getFunctionsByName(functionName))
			if (func.isUnquoted())
				return true;

		return false;
	}

	public boolean doesFunctionExist(String functionName) {
		return functionsSet.doesFunctionExist(functionName);
	}

	@JawsStrange
	private String getFunctionNameAt(String s, int pos) {
		final boolean justAfterALetter = pos > 0 && TLineType.isLetterOrEmojiOrUnderscoreOrDigit(s.charAt(pos - 1))
				&& VariableManager.justAfterBackslashN(s, pos) == false;
		if (justAfterALetter && s.charAt(pos) != '%' && s.charAt(pos) != '$')
			return null;

		final String fname = functionsSet.getLonguestMatchStartingIn(s, pos);
		if (fname.length() == 0)
			return null;

		return fname.substring(0, fname.length() - 1);
	}

	public List<StringLocated> getResultList() {
		return resultList;
	}

	public List<StringLocated> getDebug() {
		return debug;
	}

	public String extractFromResultList(int n1) {
		final StringBuilder sb = new StringBuilder();
		while (resultList.size() > n1) {
			sb.append(resultList.get(n1).getString());
			resultList.remove(n1);
			if (resultList.size() > n1)
				sb.append(Jaws.BLOCK_E1_NEWLINE);

		}
		return sb.toString();
	}

	public void appendEndOfLine(String endOfLine) {
		if (endOfLine.length() > 0) {
			final int idx = resultList.size() - 1;
			StringLocated last = resultList.get(idx);
			last = last.append(endOfLine);
			resultList.set(idx, last);
		}
	}

	public TFunction getFunctionSmart(TFunctionSignature signature) {
		return functionsSet.getFunctionSmart(signature);
	}

	/**
	 * Retrieve data given after @startuml.
	 */
	public Optional<String> getXargs() {
		if (resultList.size() == 0)
			return Optional.empty();

		final String first = resultList.get(0).toString();
		final int idx = first.indexOf(' ');
		if (idx == -1)
			return Optional.empty();

		return Optional.of(first.substring(idx + 1).trim());
	}

	public PreprocessingArtifact getPreprocessingArtifact() {
		return preprocessingArtifact;
	}

}
