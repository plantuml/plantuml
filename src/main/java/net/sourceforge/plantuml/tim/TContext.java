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
import java.util.Optional;
import java.util.Set;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.file.AFile;
import net.sourceforge.plantuml.file.AParentFolder;
import net.sourceforge.plantuml.file.AParentFolderStdlib;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonObject;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineList;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc.ReadLineWithYamlHeader;
import net.sourceforge.plantuml.preproc.StartDiagramExtractReader;
import net.sourceforge.plantuml.preproc.Sub;
import net.sourceforge.plantuml.preproc.UncommentReadLine;
import net.sourceforge.plantuml.preproc2.PreprocessorIncludeStrategy;
import net.sourceforge.plantuml.preproc2.PreprocessorUtils;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SURL;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.text.TLineType;
import net.sourceforge.plantuml.theme.Theme;
import net.sourceforge.plantuml.theme.ThemeUtils;
import net.sourceforge.plantuml.tim.builtin.AlwaysFalse;
import net.sourceforge.plantuml.tim.builtin.AlwaysTrue;
import net.sourceforge.plantuml.tim.builtin.Backslash;
import net.sourceforge.plantuml.tim.builtin.BoolVal;
import net.sourceforge.plantuml.tim.builtin.Breakline;
import net.sourceforge.plantuml.tim.builtin.CallUserFunction;
import net.sourceforge.plantuml.tim.builtin.Chr;
import net.sourceforge.plantuml.tim.builtin.Darken;
import net.sourceforge.plantuml.tim.builtin.DateFunction;
import net.sourceforge.plantuml.tim.builtin.Dec2hex;
import net.sourceforge.plantuml.tim.builtin.Dirpath;
import net.sourceforge.plantuml.tim.builtin.Dollar;
import net.sourceforge.plantuml.tim.builtin.Eval;
import net.sourceforge.plantuml.tim.builtin.Feature;
import net.sourceforge.plantuml.tim.builtin.FileExists;
import net.sourceforge.plantuml.tim.builtin.Filedate;
import net.sourceforge.plantuml.tim.builtin.Filename;
import net.sourceforge.plantuml.tim.builtin.FilenameNoExtension;
import net.sourceforge.plantuml.tim.builtin.FunctionExists;
import net.sourceforge.plantuml.tim.builtin.GetAllStdlib;
import net.sourceforge.plantuml.tim.builtin.GetAllTheme;
import net.sourceforge.plantuml.tim.builtin.GetCurrentTheme;
import net.sourceforge.plantuml.tim.builtin.GetJsonKey;
import net.sourceforge.plantuml.tim.builtin.GetJsonType;
import net.sourceforge.plantuml.tim.builtin.GetStdlib;
import net.sourceforge.plantuml.tim.builtin.GetVariableValue;
import net.sourceforge.plantuml.tim.builtin.GetVersion;
import net.sourceforge.plantuml.tim.builtin.Getenv;
import net.sourceforge.plantuml.tim.builtin.Hex2dec;
import net.sourceforge.plantuml.tim.builtin.HslColor;
import net.sourceforge.plantuml.tim.builtin.IntVal;
import net.sourceforge.plantuml.tim.builtin.InvokeProcedure;
import net.sourceforge.plantuml.tim.builtin.IsDark;
import net.sourceforge.plantuml.tim.builtin.IsLight;
import net.sourceforge.plantuml.tim.builtin.JsonAdd;
import net.sourceforge.plantuml.tim.builtin.JsonKeyExists;
import net.sourceforge.plantuml.tim.builtin.JsonMerge;
import net.sourceforge.plantuml.tim.builtin.JsonRemove;
import net.sourceforge.plantuml.tim.builtin.JsonSet;
import net.sourceforge.plantuml.tim.builtin.LeftAlign;
import net.sourceforge.plantuml.tim.builtin.Lighten;
import net.sourceforge.plantuml.tim.builtin.LoadJson;
import net.sourceforge.plantuml.tim.builtin.LogicalAnd;
import net.sourceforge.plantuml.tim.builtin.LogicalNand;
import net.sourceforge.plantuml.tim.builtin.LogicalNor;
import net.sourceforge.plantuml.tim.builtin.LogicalNot;
import net.sourceforge.plantuml.tim.builtin.LogicalNxor;
import net.sourceforge.plantuml.tim.builtin.LogicalOr;
import net.sourceforge.plantuml.tim.builtin.LogicalXor;
import net.sourceforge.plantuml.tim.builtin.Lower;
import net.sourceforge.plantuml.tim.builtin.Modulo;
import net.sourceforge.plantuml.tim.builtin.Newline;
import net.sourceforge.plantuml.tim.builtin.NewlineShort;
import net.sourceforge.plantuml.tim.builtin.Now;
import net.sourceforge.plantuml.tim.builtin.Ord;
import net.sourceforge.plantuml.tim.builtin.Percent;
import net.sourceforge.plantuml.tim.builtin.RandomFunction;
import net.sourceforge.plantuml.tim.builtin.RetrieveProcedure;
import net.sourceforge.plantuml.tim.builtin.ReverseColor;
import net.sourceforge.plantuml.tim.builtin.ReverseHsluvColor;
import net.sourceforge.plantuml.tim.builtin.RightAlign;
import net.sourceforge.plantuml.tim.builtin.SetVariableValue;
import net.sourceforge.plantuml.tim.builtin.Size;
import net.sourceforge.plantuml.tim.builtin.SplitStr;
import net.sourceforge.plantuml.tim.builtin.SplitStrRegex;
import net.sourceforge.plantuml.tim.builtin.Str2Json;
import net.sourceforge.plantuml.tim.builtin.StringFunction;
import net.sourceforge.plantuml.tim.builtin.Strlen;
import net.sourceforge.plantuml.tim.builtin.Strpos;
import net.sourceforge.plantuml.tim.builtin.Substr;
import net.sourceforge.plantuml.tim.builtin.Tabulation;
import net.sourceforge.plantuml.tim.builtin.Upper;
import net.sourceforge.plantuml.tim.builtin.VariableExists;
import net.sourceforge.plantuml.tim.builtin.Xargs;
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

	private final PreprocessingArtifact preprocessingArtifact = new PreprocessingArtifact();

	public Set<FileWithSuffix> getFilesUsedCurrent() {
		return Collections.unmodifiableSet(filesUsedCurrent);
	}

	private void addStandardFunctions(Defines defines) {
		functionsSet.addFunction(new AlwaysFalse());
		functionsSet.addFunction(new AlwaysTrue());
		functionsSet.addFunction(new Backslash());
		functionsSet.addFunction(new BoolVal());
		functionsSet.addFunction(new Breakline());
		functionsSet.addFunction(new CallUserFunction());
		functionsSet.addFunction(new Chr());
		functionsSet.addFunction(new Darken());
		functionsSet.addFunction(new DateFunction());
		functionsSet.addFunction(new Dec2hex());
		functionsSet.addFunction(new Dirpath(defines));
		functionsSet.addFunction(new Dollar());
		functionsSet.addFunction(new Eval());
		functionsSet.addFunction(new Feature());
		functionsSet.addFunction(new Filedate(defines));
		functionsSet.addFunction(new FileExists());
		functionsSet.addFunction(new Filename(defines));
		functionsSet.addFunction(new FilenameNoExtension(defines));
		functionsSet.addFunction(new FunctionExists());
		functionsSet.addFunction(new GetAllStdlib());
		functionsSet.addFunction(new GetAllTheme());
		functionsSet.addFunction(new GetCurrentTheme());
		functionsSet.addFunction(new GetJsonKey());
		functionsSet.addFunction(new GetJsonType());
		functionsSet.addFunction(new GetStdlib());
		functionsSet.addFunction(new GetVariableValue());
		functionsSet.addFunction(new GetVersion());
		functionsSet.addFunction(new Getenv());
		functionsSet.addFunction(new Hex2dec());
		functionsSet.addFunction(new HslColor());
		functionsSet.addFunction(new IntVal());
		functionsSet.addFunction(new InvokeProcedure());
		functionsSet.addFunction(new IsDark());
		functionsSet.addFunction(new IsLight());
		functionsSet.addFunction(new JsonAdd());
		functionsSet.addFunction(new JsonKeyExists());
		functionsSet.addFunction(new JsonMerge());
		functionsSet.addFunction(new JsonRemove());
		functionsSet.addFunction(new JsonSet());
		functionsSet.addFunction(new LeftAlign());
		functionsSet.addFunction(new Lighten());
		functionsSet.addFunction(new LoadJson());
		// functionsSet.addFunction(new LoadJsonLegacy());
		functionsSet.addFunction(new LogicalAnd());
		functionsSet.addFunction(new LogicalNand());
		functionsSet.addFunction(new LogicalNor());
		functionsSet.addFunction(new LogicalNot());
		functionsSet.addFunction(new LogicalNxor());
		functionsSet.addFunction(new LogicalOr());
		functionsSet.addFunction(new LogicalXor());
		functionsSet.addFunction(new Lower());
		functionsSet.addFunction(new Modulo());
		functionsSet.addFunction(new Newline());
		functionsSet.addFunction(new NewlineShort());
		functionsSet.addFunction(new Now());
		functionsSet.addFunction(new Ord());
		functionsSet.addFunction(new Percent());
		functionsSet.addFunction(new RandomFunction());
		functionsSet.addFunction(new RetrieveProcedure());
		functionsSet.addFunction(new ReverseColor());
		functionsSet.addFunction(new ReverseHsluvColor());
		functionsSet.addFunction(new RightAlign());
		functionsSet.addFunction(new SetVariableValue());
		functionsSet.addFunction(new Size());
		functionsSet.addFunction(new SplitStr());
		functionsSet.addFunction(new SplitStrRegex());
		functionsSet.addFunction(new Str2Json());
		functionsSet.addFunction(new StringFunction());
		functionsSet.addFunction(new Strlen());
		functionsSet.addFunction(new Strpos());
		functionsSet.addFunction(new Substr());
		functionsSet.addFunction(new Tabulation());
		functionsSet.addFunction(new Upper());
		functionsSet.addFunction(new VariableExists());
		functionsSet.addFunction(new Xargs());
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
		final CodeIterator it = buildCodeIterator(memory, body);

		StringLocated s = null;
		while ((s = it.peek()) != null) {
			final TValue result = executeOneLineSafe(memory, s, ftype, modeSpecial);
			if (result != null)
				return result;

			it.next();
		}
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
		} else if (s.getString().matches("^\\s+$")) {
			return null;
		} else {
			throw new EaterException("Compile Error " + ftype + " " + type, s);
		}
	}

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
		if (memory.isEmpty() && functionsSet.size() == 0)
			return new StringLocated[] { located };

		final String result = applyFunctionsAndVariables(memory, located);
		if (result == null)
			return null;

		if (Pragma.legacyReplaceBackslashNByNewline()) {
			final String[] splited = result.split("\n");
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
		if (memory.isEmpty() && functionsSet.size() == 0)
			return str.getString();

		final StringBuilder result = new StringBuilder();
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
			} else if (new VariableManager(this, memory, str).getVarnameAt(str.getString(), i) != null) {
				i = new VariableManager(this, memory, str).replaceVariables(str.getString(), i, result);
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
		final EaterImport _import = new EaterImport(s.getTrimmed());
		_import.analyze(this, memory);

		try {
			final SFile file = FileSystem.getInstance()
					.getFile(applyFunctionsAndVariables(memory, new StringLocated(_import.getWhat(), s.getLocation())));
			if (file.exists() && file.isDirectory() == false) {
				importedFiles.addImportFile(file);
				return;
			}
		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("Cannot import " + e.getMessage(), s);
		}

		throw new EaterException("Cannot import", s);
	}

	private void executeLog(TMemory memory, StringLocated s) throws EaterException {
		final EaterLog log = new EaterLog(s.getTrimmed());
		log.analyze(this, memory);
	}

	public FileWithSuffix getFileWithSuffix(String from, String realName) throws IOException {
		final String s = ThemeUtils.getFullPath(from, realName);
		final FileWithSuffix file = importedFiles.getFile(s, null);
		return file;

	}

	private void executeIncludesub(TMemory memory, StringLocated s) throws EaterException {
		ImportedFiles saveImportedFiles = null;
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
					final FileWithSuffix f2 = importedFiles.getFile(filename, null);
					if (f2.fileOk()) {
						saveImportedFiles = this.importedFiles;
						this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
						final Reader reader = f2.getReader(charset);
						if (reader == null)
							throw new EaterException("cannot include " + what, s);

						try {
							ReadLine readerline = ReadLineReader.create(reader, what, s.getLocation());
							readerline = new UncommentReadLine(readerline);
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
				this.importedFiles = saveImportedFiles;

		}
	}

	private void executeIncludeDef(TMemory memory, StringLocated s) throws EaterException {
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

	private JsonObject themeMetadata = new JsonObject();

	public JsonObject getThemeMetadata() {
		return themeMetadata;
	}

	private void executeTheme(TMemory memory, StringLocated s) throws EaterException {
		final EaterTheme eater = new EaterTheme(s.getTrimmed(), importedFiles);
		eater.analyze(this, memory);
		final Theme theme = eater.getTheme();
		if (theme == null)
			throw new EaterException("No such theme " + eater.getName(), s);

		final ImportedFiles saveImportedFiles = this.importedFiles;
		this.importedFiles = eater.getNewImportedFiles();

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
			this.importedFiles = saveImportedFiles;
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
		ImportedFiles saveImportedFiles = null;
		try {
			if (what.startsWith("http://") || what.startsWith("https://")) {
				final SURL url = SURL.create(what);
				if (url == null)
					throw new EaterException("Cannot open URL", s);

				reader = PreprocessorUtils.getReaderIncludeUrl(url, s, suf, charset);
			} else if (what.startsWith("<") && what.endsWith(">")) {
				final String stdlibPath = what.substring(1, what.length() - 1);
				final String libname = stdlibPath.substring(0, stdlibPath.indexOf('/'));
				saveImportedFiles = this.importedFiles;
				this.importedFiles = this.importedFiles.withCurrentDir(new AParentFolderStdlib(s, libname));
				reader = PreprocessorUtils.getReaderStdlibInclude(s, stdlibPath);
				// ::comment when __CORE__
			} else if (what.startsWith("[") && what.endsWith("]")) {
				reader = PreprocessorUtils.getReaderNonstandardInclude(s, what.substring(1, what.length() - 1));
				// ::done
			} else if (importedFiles.getCurrentDir() instanceof AParentFolderStdlib) {
				final AParentFolderStdlib folderStdlib = (AParentFolderStdlib) importedFiles.getCurrentDir();
				reader = folderStdlib.getReader(what);
			} else {
				final FileWithSuffix f2 = importedFiles.getFile(what, suf);
				if (f2.fileOk()) {
					if (strategy == PreprocessorIncludeStrategy.DEFAULT && filesUsedCurrent.contains(f2))
						return;

					if (strategy == PreprocessorIncludeStrategy.ONCE && filesUsedCurrent.contains(f2))
						throw new EaterException("This file has already been included", s);

					if (StartDiagramExtractReader.containsStartDiagram(f2, s, charset)) {
						reader = StartDiagramExtractReader.build(f2, s, charset);
					} else {
						final Reader tmp = f2.getReader(charset);
						if (tmp == null)
							throw new EaterException("Cannot include file", s);

						reader = ReadLineReader.create(tmp, what, s.getLocation());
					}
					saveImportedFiles = this.importedFiles;
					this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
					assert reader != null;
					filesUsedCurrent.add(f2);
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
						this.importedFiles = saveImportedFiles;

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
