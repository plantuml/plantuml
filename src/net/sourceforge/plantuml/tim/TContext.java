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
 *
 */
package net.sourceforge.plantuml.tim;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
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
import net.sourceforge.plantuml.tim.stdlib.DateFunction;
import net.sourceforge.plantuml.tim.stdlib.Dirpath;
import net.sourceforge.plantuml.tim.stdlib.FileExists;
import net.sourceforge.plantuml.tim.stdlib.Filename;
import net.sourceforge.plantuml.tim.stdlib.FunctionExists;
import net.sourceforge.plantuml.tim.stdlib.GetVariableValue;
import net.sourceforge.plantuml.tim.stdlib.GetVersion;
import net.sourceforge.plantuml.tim.stdlib.Getenv;
import net.sourceforge.plantuml.tim.stdlib.IntVal;
import net.sourceforge.plantuml.tim.stdlib.InvokeProcedure;
import net.sourceforge.plantuml.tim.stdlib.LogicalNot;
import net.sourceforge.plantuml.tim.stdlib.Lower;
import net.sourceforge.plantuml.tim.stdlib.RetrieveProcedure;
import net.sourceforge.plantuml.tim.stdlib.SetVariableValue;
import net.sourceforge.plantuml.tim.stdlib.StringFunction;
import net.sourceforge.plantuml.tim.stdlib.Strlen;
import net.sourceforge.plantuml.tim.stdlib.Strpos;
import net.sourceforge.plantuml.tim.stdlib.Substr;
import net.sourceforge.plantuml.tim.stdlib.Upper;
import net.sourceforge.plantuml.tim.stdlib.VariableExists;

public class TContext {

	private final List<StringLocated> resultList = new ArrayList<StringLocated>();
	private final List<StringLocated> debug = new ArrayList<StringLocated>();

	public final FunctionsSet functionsSet = new FunctionsSet();

	private ImportedFiles importedFiles;
	private final String charset;

	private final Map<String, Sub> subs = new HashMap<String, Sub>();
	private final DefinitionsContainer definitionsContainer;

	// private final Set<FileWithSuffix> usedFiles = new HashSet<FileWithSuffix>();
	private final Set<FileWithSuffix> filesUsedCurrent = new HashSet<FileWithSuffix>();

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
		// !exit
		// !log
		// %min
		// %max
		// Regexp
		// %plantuml_version
		// %time
		// %trim
		// %str_replace
	}

	public TContext(ImportedFiles importedFiles, Defines defines, String charset,
			DefinitionsContainer definitionsContainer) {
		this.definitionsContainer = definitionsContainer;
		this.importedFiles = importedFiles;
		this.charset = charset;
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

	private TValue fromJsonOld(TMemory memory, String name) {
		final int x = name.indexOf('.');
		final TValue data = memory.getVariable(name.substring(0, x));
		if (data == null) {
			return null;
		}
		final JsonObject json = (JsonObject) data.toJson();
		final JsonValue result = json.get(name.substring(x + 1));
		return TValue.fromJson(result);
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
				if (result != null) {
					return result;
				}
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
			e.printStackTrace();
			throw EaterException.located("Fatal parsing error", s);
		}
	}

	private TValue executeOneLineNotSafe(TMemory memory, StringLocated s, TFunctionType ftype, boolean modeSpecial)
			throws EaterException, EaterExceptionLocated {
		final TLineType type = s.getType();

		if (type == TLineType.INCLUDESUB) {
			this.executeIncludesub(memory, s);
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
			throw EaterException.located("Compile Error " + ftype + " " + type, s);
		}
	}

	private void addPlain(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		StringLocated tmp = applyFunctionsAndVariablesInternal(memory, s);
		if (tmp != null) {
			if (pendingAdd != null) {
				tmp = new StringLocated(pendingAdd + tmp.getString(), tmp.getLocation());
				pendingAdd = null;
			}
			resultList.add(tmp);
		}
	}

	private void simulatePlain(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final StringLocated ignored = applyFunctionsAndVariablesInternal(memory, s);
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

	private StringLocated applyFunctionsAndVariablesInternal(TMemory memory, StringLocated located)
			throws EaterException, EaterExceptionLocated {
		if (memory.isEmpty() && functionsSet.size() == 0) {
			return located;
		}
		final String result = applyFunctionsAndVariables(memory, located.getLocation(), located.getString());
		if (result == null) {
			return null;
		}
		return new StringLocated(result, located.getLocation());
	}

	private String pendingAdd = null;

	public String applyFunctionsAndVariables(TMemory memory, LineLocation location, String str)
			throws EaterException, EaterExceptionLocated {
		// https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm
		// https://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most
		// https://en.wikipedia.org/wiki/String-searching_algorithm
		// https://www.quora.com/What-is-the-most-efficient-algorithm-to-replace-all-occurrences-of-a-pattern-P-in-a-string-with-a-pattern-P
		// https://en.wikipedia.org/wiki/Trie
		if (memory.isEmpty() && functionsSet.size() == 0) {
			return str;
		}
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			final char c = str.charAt(i);
			final String presentFunction = getFunctionNameAt(str, i);
			if (presentFunction != null) {
				final String sub = str.substring(i);
				final EaterFunctionCall call = new EaterFunctionCall(new StringLocated(sub, location),
						isLegacyDefine(presentFunction), isUnquoted(presentFunction));
				call.analyze(this, memory);
				final TFunction function = functionsSet
						.getFunctionSmart(new TFunctionSignature(presentFunction, call.getValues().size()));
				if (function == null) {
					throw EaterException.located("Function not found " + presentFunction,
							new StringLocated(str, location));
				}
				if (function.getFunctionType() == TFunctionType.PROCEDURE) {
					this.pendingAdd = result.toString();
					executeVoid3(location, memory, sub, function);
					return null;
				}
				if (function.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
					this.pendingAdd = str.substring(0, i);
					executeVoid3(location, memory, sub, function);
					return null;
				}
				assert function.getFunctionType() == TFunctionType.RETURN_FUNCTION
						|| function.getFunctionType() == TFunctionType.LEGACY_DEFINE;
				final TValue functionReturn = function.executeReturnFunction(this, memory, location, call.getValues());
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

	private void executeVoid3(LineLocation location, TMemory memory, String s, TFunction function)
			throws EaterException, EaterExceptionLocated {
		function.executeProcedure(this, memory, location, s);
	}

	private void executeImport(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterImport _import = new EaterImport(s.getTrimmed());
		_import.analyze(this, memory);

		try {
			final File file = FileSystem.getInstance()
					.getFile(applyFunctionsAndVariables(memory, s.getLocation(), _import.getLocation()));
			if (file.exists() && file.isDirectory() == false) {
				importedFiles.add(file);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw EaterException.located("Cannot import " + e.getMessage(), s);
		}

		throw EaterException.located("Cannot import", s);
	}

	private void executeLog(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterLog log = new EaterLog(s.getTrimmed());
		log.analyze(this, memory);
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
						ReadLine readerline = ReadLineReader.create(reader, location, s.getLocation());
						readerline = new UncommentReadLine(readerline);
						sub = Sub.fromFile(readerline, blocname, this, memory);
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw EaterException.located("cannot include " + e, s);
				}
			}
			if (sub == null) {
				sub = subs.get(location);
			}
			if (sub == null) {
				throw EaterException.located("cannot include " + location, s);
			}
			executeLinesInternal(memory, sub.lines(), null);
		} finally {
			if (saveImportedFiles != null) {
				this.importedFiles = saveImportedFiles;
			}
		}
	}

	private void executeIncludeDef(TMemory memory, StringLocated s) throws EaterException, EaterExceptionLocated {
		final EaterIncludeDef include = new EaterIncludeDef(s.getTrimmed());
		include.analyze(this, memory);
		final String definitionName = include.getLocation();
		final List<String> definition = definitionsContainer.getDefinition(definitionName);
		ReadLine reader2 = new ReadLineList(definition, s.getLocation());

		try {
			final List<StringLocated> body = new ArrayList<StringLocated>();
			do {
				final StringLocated sl = reader2.readLine();
				if (sl == null) {
					executeLinesInternal(memory, body, null);
					return;
				}
				body.add(sl);
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
			throw EaterException.located("" + e, s);
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

		ReadLine reader2 = null;
		ImportedFiles saveImportedFiles = null;
		try {
			if (location.startsWith("http://") || location.startsWith("https://")) {
				final URL url = new URL(location);
				reader2 = PreprocessorUtils.getReaderIncludeUrl2(url, s, suf, charset);

			}
			if (location.startsWith("<") && location.endsWith(">")) {
				reader2 = PreprocessorUtils.getReaderStdlibInclude(s, location.substring(1, location.length() - 1));
			} else {
				final FileWithSuffix f2 = importedFiles.getFile(location, suf);
				if (f2.fileOk()) {
					if (strategy == PreprocessorIncludeStrategy.DEFAULT && filesUsedCurrent.contains(f2)) {
						return;
					}
					if (strategy == PreprocessorIncludeStrategy.ONCE && filesUsedCurrent.contains(f2)) {
						throw EaterException.located("This file has already been included", s);
					}

					if (StartDiagramExtractReader.containsStartDiagram(f2, s, charset)) {
						reader2 = StartDiagramExtractReader.build(f2, s, charset);
					} else {
						final Reader reader = f2.getReader(charset);
						if (reader == null) {
							throw EaterException.located("Cannot include file", s);
						}
						reader2 = ReadLineReader.create(reader, location, s.getLocation());
					}
					saveImportedFiles = this.importedFiles;
					this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
					assert reader2 != null;
					filesUsedCurrent.add(f2);
				}
			}
			if (reader2 != null) {
				try {
					final List<StringLocated> body = new ArrayList<StringLocated>();
					do {
						final StringLocated sl = reader2.readLine();
						if (sl == null) {
							executeLines(memory, body, null, false);
							return;
						}
						body.add(sl);
					} while (true);
				} finally {
					if (saveImportedFiles != null) {
						this.importedFiles = saveImportedFiles;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw EaterException.located("cannot include " + e, s);
		}

		throw EaterException.located("cannot include " + location, s);
	}

	public boolean isLegacyDefine(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().getFunctionType().isLegacy()) {
				return true;
			}
		}
		return false;
	}

	public boolean isUnquoted(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().isUnquoted()) {
				return true;
			}
		}
		return false;
	}

	public boolean doesFunctionExist(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functionsSet.functions().entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName)) {
				return true;
			}
		}
		return false;
	}

	private String getFunctionNameAt(String s, int pos) {
		if (pos > 0 && TLineType.isLetterOrUnderscoreOrDigit(s.charAt(pos - 1))
				&& VariableManager.justAfterBackslashN(s, pos) == false) {
			return null;
		}
		final String fname = functionsSet.getLonguestMatchStartingIn(s.substring(pos));
		if (fname.length() == 0) {
			return null;
		}
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
			if (resultList.size() > n1) {
				sb.append("\\n");
			}
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
