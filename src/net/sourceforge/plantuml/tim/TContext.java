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

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineList;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.StartDiagramExtractReader;
import net.sourceforge.plantuml.preproc.Sub;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.preproc2.PreprocessorIncludeStrategy;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;
import net.sourceforge.plantuml.theme.ThemeUtils;
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
import net.sourceforge.plantuml.tim.stdlib.AlwaysFalse;
import net.sourceforge.plantuml.tim.stdlib.AlwaysTrue;
import net.sourceforge.plantuml.tim.stdlib.CallUserFunction;
import net.sourceforge.plantuml.tim.stdlib.Chr;
import net.sourceforge.plantuml.tim.stdlib.Darken;
import net.sourceforge.plantuml.tim.stdlib.DateFunction;
import net.sourceforge.plantuml.tim.stdlib.Dec2hex;
import net.sourceforge.plantuml.tim.stdlib.Dirpath;
import net.sourceforge.plantuml.tim.stdlib.Eval;
import net.sourceforge.plantuml.tim.stdlib.Feature;
import net.sourceforge.plantuml.tim.stdlib.FileExists;
import net.sourceforge.plantuml.tim.stdlib.Filename;
import net.sourceforge.plantuml.tim.stdlib.FunctionExists;
import net.sourceforge.plantuml.tim.stdlib.GetAllTheme;
import net.sourceforge.plantuml.tim.stdlib.GetJsonKey;
import net.sourceforge.plantuml.tim.stdlib.GetJsonType;
import net.sourceforge.plantuml.tim.stdlib.GetVariableValue;
import net.sourceforge.plantuml.tim.stdlib.GetVersion;
import net.sourceforge.plantuml.tim.stdlib.Getenv;
import net.sourceforge.plantuml.tim.stdlib.Hex2dec;
import net.sourceforge.plantuml.tim.stdlib.HslColor;
import net.sourceforge.plantuml.tim.stdlib.IntVal;
import net.sourceforge.plantuml.tim.stdlib.InvokeProcedure;
import net.sourceforge.plantuml.tim.stdlib.IsDark;
import net.sourceforge.plantuml.tim.stdlib.IsLight;
import net.sourceforge.plantuml.tim.stdlib.JsonKeyExists;
import net.sourceforge.plantuml.tim.stdlib.Lighten;
import net.sourceforge.plantuml.tim.stdlib.LoadJson;
import net.sourceforge.plantuml.tim.stdlib.LogicalAnd;
import net.sourceforge.plantuml.tim.stdlib.LogicalNand;
import net.sourceforge.plantuml.tim.stdlib.LogicalNor;
import net.sourceforge.plantuml.tim.stdlib.LogicalNot;
import net.sourceforge.plantuml.tim.stdlib.LogicalNxor;
import net.sourceforge.plantuml.tim.stdlib.LogicalOr;
import net.sourceforge.plantuml.tim.stdlib.LogicalXor;
import net.sourceforge.plantuml.tim.stdlib.Lower;
import net.sourceforge.plantuml.tim.stdlib.Newline;
import net.sourceforge.plantuml.tim.stdlib.Now;
import net.sourceforge.plantuml.tim.stdlib.Ord;
import net.sourceforge.plantuml.tim.stdlib.RandomFunction;
import net.sourceforge.plantuml.tim.stdlib.RetrieveProcedure;
import net.sourceforge.plantuml.tim.stdlib.ReverseColor;
import net.sourceforge.plantuml.tim.stdlib.ReverseHsluvColor;
import net.sourceforge.plantuml.tim.stdlib.SetVariableValue;
import net.sourceforge.plantuml.tim.stdlib.Size;
import net.sourceforge.plantuml.tim.stdlib.SplitStr;
import net.sourceforge.plantuml.tim.stdlib.StringFunction;
import net.sourceforge.plantuml.tim.stdlib.Strlen;
import net.sourceforge.plantuml.tim.stdlib.Strpos;
import net.sourceforge.plantuml.tim.stdlib.Substr;
import net.sourceforge.plantuml.tim.stdlib.Upper;
import net.sourceforge.plantuml.tim.stdlib.VariableExists;
import net.sourceforge.plantuml.utils.LineLocation;

public class TContext {

	private final List<StringLocated> resultList = new ArrayList<>();
	private final List<StringLocated> debug = new ArrayList<>();

	public final FunctionsSet functionsSet = new FunctionsSet();

	private ImportedFiles importedFiles;
	private final Charset charset;

	private final Map<String, Sub> subs = new HashMap<String, Sub>();
	private final DefinitionsContainer definitionsContainer;

	// private final Set<FileWithSuffix> usedFiles = new HashSet<>();
	private final Set<FileWithSuffix> filesUsedCurrent = new HashSet<>();

	public Set<FileWithSuffix> getFilesUsedCurrent() {
		return Collections.unmodifiableSet(filesUsedCurrent);
	}

	private void addStandardFunctions(Defines defines) {
		functionsSet.addFunction(new Strlen());
		functionsSet.addFunction(new Substr());
		functionsSet.addFunction(new FileExists());
		functionsSet.addFunction(new Getenv());
		functionsSet.addFunction(new Dirpath(defines));
		functionsSet.addFunction(new Filename(defines));
		functionsSet.addFunction(new DateFunction());
		functionsSet.addFunction(new Strpos());
		functionsSet.addFunction(new InvokeProcedure());
		functionsSet.addFunction(new AlwaysFalse());
		functionsSet.addFunction(new AlwaysTrue());
		functionsSet.addFunction(new LogicalNot());
		functionsSet.addFunction(new FunctionExists());
		functionsSet.addFunction(new VariableExists());
		functionsSet.addFunction(new CallUserFunction());
		functionsSet.addFunction(new RetrieveProcedure());
		functionsSet.addFunction(new SetVariableValue());
		functionsSet.addFunction(new GetVariableValue());
		functionsSet.addFunction(new IntVal());
		functionsSet.addFunction(new GetVersion());
		functionsSet.addFunction(new Upper());
		functionsSet.addFunction(new Lower());
		functionsSet.addFunction(new StringFunction());
		functionsSet.addFunction(new Newline());
		functionsSet.addFunction(new Feature());
		functionsSet.addFunction(new Lighten());
		functionsSet.addFunction(new Darken());
		functionsSet.addFunction(new IsDark());
		functionsSet.addFunction(new IsLight());
		functionsSet.addFunction(new ReverseHsluvColor());
		functionsSet.addFunction(new ReverseColor());
		functionsSet.addFunction(new Eval());
		functionsSet.addFunction(new Hex2dec());
		functionsSet.addFunction(new Dec2hex());
		functionsSet.addFunction(new HslColor());
		functionsSet.addFunction(new LoadJson());
		// functionsSet.addFunction(new LoadJsonLegacy());
		functionsSet.addFunction(new Chr());
		functionsSet.addFunction(new Size());
		functionsSet.addFunction(new GetJsonKey());
		functionsSet.addFunction(new GetJsonType());
		functionsSet.addFunction(new SplitStr());
		functionsSet.addFunction(new JsonKeyExists());
		functionsSet.addFunction(new Now());
		functionsSet.addFunction(new LogicalAnd());
		functionsSet.addFunction(new LogicalOr());
		functionsSet.addFunction(new LogicalXor());
		functionsSet.addFunction(new LogicalNand());
		functionsSet.addFunction(new LogicalNor());
		functionsSet.addFunction(new LogicalNxor());
		functionsSet.addFunction(new Ord());
		functionsSet.addFunction(new RandomFunction());
		functionsSet.addFunction(new GetAllTheme());
		// %standard_exists_function
		// %str_replace
		// !exit
		// !log
		// %min
		// %max
		// Regexp
		// %time
		// %trim
	}

	public TContext(ImportedFiles importedFiles, Defines defines, Charset charset,
			DefinitionsContainer definitionsContainer) {
		this.definitionsContainer = definitionsContainer;
		this.importedFiles = importedFiles;
		this.charset = requireNonNull(charset);
		this.addStandardFunctions(defines);
	}

	public Knowledge asKnowledge(final TMemory memory, final LineLocation location) {
		return new Knowledge() {

			public TValue getVariable(String name) throws EaterException, EaterExceptionLocated {
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

	private TValue fromJson(TMemory memory, String name, LineLocation location)
			throws EaterException, EaterExceptionLocated {
		final String result = applyFunctionsAndVariables(memory, location, name);
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
			throws EaterExceptionLocated {
		final CodeIterator it = buildCodeIterator(memory, body);

		StringLocated s = null;
		try {
			while ((s = it.peek()) != null) {
				final TValue result = executeOneLineSafe(memory, s, ftype, modeSpecial);
				if (result != null)
					return result;

				it.next();
			}
			return null;
		} catch (EaterException e) {
			throw e.withLocation(s);
		}

	}

	private void executeLinesInternal(TMemory memory, List<StringLocated> body, TFunctionType ftype)
			throws EaterExceptionLocated, EaterException {
		final CodeIterator it = buildCodeIterator(memory, body);

		StringLocated s = null;
		while ((s = it.peek()) != null) {
			executeOneLineSafe(memory, s, ftype, false);
			it.next();
		}

	}

	private TValue executeOneLineSafe(TMemory memory, StringLocated s, TFunctionType ftype, boolean modeSpecial)
			throws EaterException, EaterExceptionLocated {
		try {
			this.debug.add(s);
			return executeOneLineNotSafe(memory, s, ftype, modeSpecial);
		} catch (Exception e) {
			if (e instanceof EaterException)
				throw (EaterException) e;
			if (e instanceof EaterExceptionLocated)
				throw (EaterExceptionLocated) e;
			Logme.error(e);
			throw EaterException.located("Fatal parsing error");
		}
	}

	private TValue executeOneLineNotSafe(TMemory memory, StringLocated s, TFunctionType ftype, boolean modeSpecial)
			throws EaterException, EaterExceptionLocated {
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
		} else if (s.getString().matches("^\\s+$")) {
			return null;
		} else {
			throw EaterException.located("Compile Error " + ftype + " " + type);
		}
	}

	private void addPlain(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final StringLocated tmp[] = applyFunctionsAndVariablesInternal(memory, s);
		if (tmp != null) {
			if (pendingAdd != null) {
				tmp[0] = new StringLocated(pendingAdd + tmp[0].getString(), tmp[0].getLocation());
				pendingAdd = null;
			}
			for (StringLocated line : tmp)
				resultList.add(line);

		}
	}

	private void simulatePlain(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final StringLocated ignored[] = applyFunctionsAndVariablesInternal(memory, s);
	}

	private void executeAffectationDefine(TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		new EaterAffectationDefine(s).analyze(this, memory);
	}

	private void executeDumpMemory(TMemory memory, StringLocated s) throws EaterException {
		final EaterDumpMemory condition = new EaterDumpMemory(s);
		condition.analyze(this, memory);
	}

	private void executeAssert(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterAssert condition = new EaterAssert(s);
		condition.analyze(this, memory);
	}

	private void executeUndef(TMemory memory, StringLocated s) throws EaterException {
		final EaterUndef undef = new EaterUndef(s);
		undef.analyze(this, memory);
	}

	private StringLocated[] applyFunctionsAndVariablesInternal(TMemory memory, StringLocated located)
			throws EaterException, EaterExceptionLocated {
		if (memory.isEmpty() && functionsSet.size() == 0)
			return new StringLocated[] { located };

		final String result = applyFunctionsAndVariables(memory, located.getLocation(), located.getString());
		if (result == null)
			return null;

		final String[] splited = result.split("\n");
		final StringLocated[] tab = new StringLocated[splited.length];
		for (int i = 0; i < splited.length; i++)
			tab[i] = new StringLocated(splited[i], located.getLocation());

		return tab;
	}

	private String pendingAdd = null;

	public String applyFunctionsAndVariables(TMemory memory, LineLocation location, final String str)
			throws EaterException, EaterExceptionLocated {
		// https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm
		// https://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most
		// https://en.wikipedia.org/wiki/String-searching_algorithm
		// https://www.quora.com/What-is-the-most-efficient-algorithm-to-replace-all-occurrences-of-a-pattern-P-in-a-string-with-a-pattern-P
		// https://en.wikipedia.org/wiki/Trie
		if (memory.isEmpty() && functionsSet.size() == 0)
			return str;

		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			final char c = str.charAt(i);
			final String presentFunction = getFunctionNameAt(str, i);
			if (presentFunction != null) {
				final String sub = str.substring(i);
				final EaterFunctionCall call = new EaterFunctionCall(new StringLocated(sub, location),
						isLegacyDefine(presentFunction), isUnquoted(presentFunction));
				call.analyze(this, memory);
				final TFunctionSignature signature = new TFunctionSignature(presentFunction, call.getValues().size(),
						call.getNamedArguments().keySet());
				final TFunction function = functionsSet.getFunctionSmart(signature);
				if (function == null)
					throw EaterException.located("Function not found " + presentFunction);

				if (function.getFunctionType() == TFunctionType.PROCEDURE) {
					this.pendingAdd = result.toString();
					executeVoid3(location, memory, sub, function, call);
					i += call.getCurrentPosition();
					final String remaining = str.substring(i);
					if (remaining.length() > 0)
						appendToLastResult(remaining);

					return null;
				}
				if (function.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
					this.pendingAdd = str.substring(0, i);
					executeVoid3(location, memory, sub, function, call);
					return null;
				}
				assert function.getFunctionType() == TFunctionType.RETURN_FUNCTION
						|| function.getFunctionType() == TFunctionType.LEGACY_DEFINE;
				final TValue functionReturn = function.executeReturnFunction(this, memory, location, call.getValues(),
						call.getNamedArguments());
				result.append(functionReturn.toString());
				i += call.getCurrentPosition() - 1;
			} else if (new VariableManager(this, memory, location).getVarnameAt(str, i) != null) {
				i = new VariableManager(this, memory, location).replaceVariables(str, i, result);
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

	private void executeVoid3(LineLocation location, TMemory memory, String s, TFunction function,
			EaterFunctionCall call) throws EaterException, EaterExceptionLocated {
		function.executeProcedureInternal(this, memory, call.getValues(), call.getNamedArguments());
		// function.executeProcedure(this, memory, location, s, call.getValues(),
		// call.getNamedArguments());
	}

	private void executeImport(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterImport _import = new EaterImport(s.getTrimmed());
		_import.analyze(this, memory);

		try {
			final SFile file = FileSystem.getInstance()
					.getFile(applyFunctionsAndVariables(memory, s.getLocation(), _import.getLocation()));
			if (file.exists() && file.isDirectory() == false) {
				importedFiles.add(file);
				return;
			}
		} catch (IOException e) {
			Logme.error(e);
			throw EaterException.located("Cannot import " + e.getMessage());
		}

		throw EaterException.located("Cannot import");
	}

	private void executeLog(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterLog log = new EaterLog(s.getTrimmed());
		log.analyze(this, memory);
	}

	public FileWithSuffix getFileWithSuffix(String from, String realName) throws IOException {
		final String s = ThemeUtils.getFullPath(from, realName);
		final FileWithSuffix file = importedFiles.getFile(s, null);
		return file;

	}

	private void executeIncludesub(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		ImportedFiles saveImportedFiles = null;
		try {
			final EaterIncludesub include = new EaterIncludesub(s.getTrimmed());
			include.analyze(this, memory);
			final String location = include.getLocation();
			final int idx = location.indexOf('!');
			Sub sub = null;
			if (idx != -1) {
				final String filename = location.substring(0, idx);
				final String blocname = location.substring(idx + 1);
				try {
					final FileWithSuffix f2 = importedFiles.getFile(filename, null);
					if (f2.fileOk()) {
						saveImportedFiles = this.importedFiles;
						this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
						final Reader reader = f2.getReader(charset);
						if (reader == null)
							throw EaterException.located("cannot include " + location);

						try {
							ReadLine readerline = ReadLineReader.create(reader, location, s.getLocation());
							readerline = new UncommentReadLine(readerline);
							sub = Sub.fromFile(readerline, blocname, this, memory);
						} finally {
							reader.close();
						}
					}
				} catch (IOException e) {
					Logme.error(e);
					throw EaterException.located("cannot include " + location);
				}
			}
			if (sub == null)
				sub = subs.get(location);

			if (sub == null)
				throw EaterException.located("cannot include " + location);

			executeLinesInternal(memory, sub.lines(), null);
		} finally {
			if (saveImportedFiles != null)
				this.importedFiles = saveImportedFiles;

		}
	}

	private void executeIncludeDef(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
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
			throw EaterException.located("" + e);
		} finally {
			try {
				reader2.close();
			} catch (IOException e) {
				Logme.error(e);
			}
		}
	}

	private void executeTheme(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterTheme eater = new EaterTheme(s.getTrimmed(), importedFiles);
		eater.analyze(this, memory);
		final ReadLine reader = eater.getTheme();
		if (reader == null)
			throw EaterException.located("No such theme " + eater.getName());

		try {
			final List<StringLocated> body = new ArrayList<>();
			do {
				final StringLocated sl = reader.readLine();
				if (sl == null) {
					executeLines(memory, body, null, false);
					return;
				}
				body.add(sl);
			} while (true);
		} catch (IOException e) {
			Logme.error(e);
			throw EaterException.located("Error reading theme " + e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				Logme.error(e);
			}
		}
	}

	private void executeInclude(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterInclude include = new EaterInclude(s.getTrimmed());
		include.analyze(this, memory);
		String location = include.getLocation();
		final PreprocessorIncludeStrategy strategy = include.getPreprocessorIncludeStrategy();
		final int idx = location.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = location.substring(idx + 1);
			location = location.substring(0, idx);
		}

		ReadLine reader = null;
		ImportedFiles saveImportedFiles = null;
		try {
			if (location.startsWith("http://") || location.startsWith("https://")) {
				final SURL url = SURL.create(location);
				if (url == null)
					throw EaterException.located("Cannot open URL");

				reader = PreprocessorUtils.getReaderIncludeUrl(url, s, suf, charset);
			} else if (location.startsWith("<") && location.endsWith(">")) {
				reader = PreprocessorUtils.getReaderStdlibInclude(s, location.substring(1, location.length() - 1));
				// ::comment when __CORE__
			} else if (location.startsWith("[") && location.endsWith("]")) {
				reader = PreprocessorUtils.getReaderNonstandardInclude(s, location.substring(1, location.length() - 1));
				// ::done
			} else {
				final FileWithSuffix f2 = importedFiles.getFile(location, suf);
				if (f2.fileOk()) {
					if (strategy == PreprocessorIncludeStrategy.DEFAULT && filesUsedCurrent.contains(f2))
						return;

					if (strategy == PreprocessorIncludeStrategy.ONCE && filesUsedCurrent.contains(f2))
						throw EaterException.located("This file has already been included");

					if (StartDiagramExtractReader.containsStartDiagram(f2, s, charset)) {
						reader = StartDiagramExtractReader.build(f2, s, charset);
					} else {
						final Reader tmp = f2.getReader(charset);
						if (tmp == null)
							throw EaterException.located("Cannot include file");

						reader = ReadLineReader.create(tmp, location, s.getLocation());
					}
					saveImportedFiles = this.importedFiles;
					this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
					assert reader != null;
					filesUsedCurrent.add(f2);
				}
			}
			if (reader != null) {
				try {
					final List<StringLocated> body = new ArrayList<>();
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
						this.importedFiles = saveImportedFiles;

				}
			}
		} catch (IOException e) {
			Logme.error(e);
			throw EaterException.located("cannot include " + e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Logme.error(e);
				}
			}
		}

		throw EaterException.located("cannot include " + location);
	}

	public boolean isLegacyDefine(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet())
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().getFunctionType().isLegacy())
				return true;

		return false;
	}

	public boolean isUnquoted(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet())
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().isUnquoted())
				return true;

		return false;
	}

	public boolean doesFunctionExist(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet())
			if (ent.getKey().getFunctionName().equals(functionName))
				return true;

		return false;
	}

	private String getFunctionNameAt(String s, int pos) {
		if (pos > 0 && TLineType.isLetterOrUnderscoreOrDigit(s.charAt(pos - 1))
				&& VariableManager.justAfterBackslashN(s, pos) == false)
			return null;

		final String fname = functionsSet.getLonguestMatchStartingIn(s.substring(pos));
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
				sb.append("\\n");

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

}
