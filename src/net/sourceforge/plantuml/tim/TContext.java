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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.DefinitionsContainer;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.CommandExecutionResult;
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
import net.sourceforge.plantuml.tim.stdlib.InvokeVoidFunction;
import net.sourceforge.plantuml.tim.stdlib.LogicalNot;
import net.sourceforge.plantuml.tim.stdlib.RetrieveVoidFunction;
import net.sourceforge.plantuml.tim.stdlib.SetVariableValue;
import net.sourceforge.plantuml.tim.stdlib.Strlen;
import net.sourceforge.plantuml.tim.stdlib.Strpos;
import net.sourceforge.plantuml.tim.stdlib.Substr;
import net.sourceforge.plantuml.tim.stdlib.VariableExists;

public class TContext {

	private final List<StringLocated> resultList = new ArrayList<StringLocated>();
	private final List<StringLocated> debug = new ArrayList<StringLocated>();
	private final Map<TFunctionSignature, TFunction> functions2 = new HashMap<TFunctionSignature, TFunction>();
	private final Set<TFunctionSignature> functionsFinal = new HashSet<TFunctionSignature>();
	private final Trie functions3 = new TrieImpl();
	private ImportedFiles importedFiles;
	private final String charset;

	private TFunctionImpl pendingFunction;
	private Sub pendingSub;
	private boolean inLongComment;
	private final Map<String, Sub> subs = new HashMap<String, Sub>();
	private final DefinitionsContainer definitionsContainer;

	// private final Set<FileWithSuffix> usedFiles = new HashSet<FileWithSuffix>();
	private final Set<FileWithSuffix> filesUsedCurrent = new HashSet<FileWithSuffix>();

	private void addStandardFunctions(Defines defines) {
		addFunction(new Strlen());
		addFunction(new Substr());
		addFunction(new FileExists());
		addFunction(new Getenv());
		addFunction(new Dirpath(defines));
		addFunction(new Filename(defines));
		addFunction(new DateFunction());
		addFunction(new Strpos());
		addFunction(new InvokeVoidFunction());
		addFunction(new AlwaysFalse());
		addFunction(new AlwaysTrue());
		addFunction(new LogicalNot());
		addFunction(new FunctionExists());
		addFunction(new VariableExists());
		addFunction(new CallUserFunction());
		addFunction(new RetrieveVoidFunction());
		addFunction(new SetVariableValue());
		addFunction(new GetVariableValue());
		addFunction(new IntVal());
		addFunction(new GetVersion());
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

	public Knowledge asKnowledge(final TMemory memory) {
		return new Knowledge() {

			public TVariable getVariable(String name) {
				return memory.getVariable(name);
			}

			public TFunction getFunction(TFunctionSignature name) {
				return getFunctionSmart(name);
			}
		};
	}

	public TFunction getFunctionSmart(TFunctionSignature searched) {
		final TFunction func = functions2.get(searched);
		if (func != null) {
			return func;
		}
		for (TFunction candidate : functions2.values()) {
			if (candidate.getSignature().sameNameAs(searched) == false) {
				continue;
			}
			if (candidate.canCover(searched.getNbArg())) {
				return candidate;
			}
		}
		return null;
	}

	public void executeOneLine(TMemory memory, TLineType type, StringLocated s, TFunctionType fromType)
			throws EaterException {

		this.debug.add(s);
		assert type == TLineType.getFromLine(s.getString());

		if (this.inLongComment == false && type == TLineType.STARTSUB) {
			if (pendingSub != null) {
				throw new EaterException("Cannot nest sub");
			}
			final EaterStartsub eater = new EaterStartsub(s.getTrimmed().getString());
			eater.execute(this, memory);
			this.pendingSub = new Sub(eater.getSubname());
			this.subs.put(eater.getSubname(), this.pendingSub);
			return;
		}
		if (this.inLongComment == false && type == TLineType.ENDSUB) {
			if (pendingSub == null) {
				throw new EaterException("No corresponding !startsub");
			}
			final Sub newly = this.pendingSub;
			this.pendingSub = null;
			this.runSub(memory, newly);
			return;
		}
		// if (this.inLongComment == false && type == TLineType.INCLUDESUB) {
		// this.executeIncludesub(memory, s);
		// return;
		// }

		if (pendingSub != null) {
			pendingSub.add(s);
			return;
		}

		if (this.getPendingFunction() != null) {
			if (this.inLongComment == false && type == TLineType.END_FUNCTION) {
				this.executeEndfunction();
			} else {
				this.getPendingFunction().addBody(s);
			}
			return;
		}

		if (this.inLongComment && s.getTrimmed().getString().endsWith("'/")) {
			this.inLongComment = false;
			return;
		}

		if (type == TLineType.COMMENT_LONG_START) {
			this.inLongComment = true;
			return;
		}
		if (this.inLongComment) {
			return;
		}
		if (type == TLineType.COMMENT_SIMPLE) {
			return;
		}
		s = s.removeInnerComment();

		if (type == TLineType.IF) {
			this.executeIf(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.IFDEF) {
			this.executeIfdef(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.IFNDEF) {
			this.executeIfndef(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.ELSE) {
			this.executeElse(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.ELSEIF) {
			this.executeElseIf(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.ENDIF) {
			this.executeEndif(memory, s.getTrimmed().getString());
			return;
		}

		final ConditionalContext conditionalContext = memory.peekConditionalContext();
		if (conditionalContext != null && memory.areAllIfOk() == false) {
			return;
		}
		if (this.inLongComment == false && type == TLineType.INCLUDESUB) {
			this.executeIncludesub(memory, s);
			return;
		}

		if (type == TLineType.DUMP_MEMORY) {
			this.executeDumpMemory(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.ASSERT) {
			this.executeAssert(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.UNDEF) {
			this.executeUndef(memory, s);
			return;
		} else if (fromType != TFunctionType.RETURN && type == TLineType.PLAIN) {
			this.addPlain(memory, s);
			return;
		} else if (fromType == TFunctionType.RETURN && type == TLineType.RETURN) {
			// Actually, ignore because we are in a if
			return;
		} else if (type == TLineType.LEGACY_DEFINE) {
			this.executeLegacyDefine(memory, s);
			return;
		} else if (type == TLineType.LEGACY_DEFINELONG) {
			this.executeLegacyDefineLong(memory, s);
			return;
		} else if (type == TLineType.AFFECTATION_DEFINE) {
			this.executeAffectationDefine(memory, s.getTrimmed().getString());
			return;
		} else if (type == TLineType.AFFECTATION) {
			this.executeAffectation(memory, s.getTrimmed().getString());
			return;
		} else if (fromType == null && type == TLineType.DECLARE_FUNCTION) {
			this.executeDeclareFunction(memory, s);
			return;
		} else if (fromType == null && type == TLineType.END_FUNCTION) {
			CommandExecutionResult.error("error endfunc");
			return;
		} else if (type == TLineType.INCLUDE) {
			this.executeInclude(memory, s);
			return;
		} else if (type == TLineType.INCLUDE_DEF) {
			this.executeIncludeDef(memory, s);
			return;
		} else if (type == TLineType.IMPORT) {
			this.executeImport(memory, s);
			return;
		} else if (type == TLineType.LOG) {
			this.executeLog(memory, s);
			return;
		} else {
			// Thread.dumpStack();
			throw new EaterException("Parsing Error");
			// throw new UnsupportedOperationException("type=" + type + " fromType=" + fromType);
		}
	}

	private void addPlain(TMemory memory, StringLocated s) throws EaterException {
		StringLocated tmp = applyFunctionsAndVariables(memory, s);
		if (tmp != null) {
			if (pendingAdd != null) {
				tmp = new StringLocated(pendingAdd + tmp.getString(), tmp.getLocation());
				pendingAdd = null;
			}
			resultList.add(tmp);
		}
	}

	private void executeAffectationDefine(TMemory memory, String s) throws EaterException {
		new EaterAffectationDefine(s).execute(this, memory);
	}

	private void executeAffectation(TMemory memory, String s) throws EaterException {
		new EaterAffectation(s).execute(this, memory);
	}

	private void executeIf(TMemory memory, String s) throws EaterException {
		final EaterIf condition = new EaterIf(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue();
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
	}

	private void executeElseIf(TMemory memory, String s) throws EaterException {
		final ConditionalContext poll = memory.peekConditionalContext();
		if (poll == null) {
			throw new EaterException("No if related to this else");
		}

		poll.enteringElseIf();
		if (poll.hasBeenBurn() == false) {
			final EaterElseIf condition = new EaterElseIf(s);
			condition.execute(this, memory);
			final boolean isTrue = condition.isTrue();
			if (isTrue) {
				poll.nowInSomeElseIf();
			}
		}
	}

	private void executeDumpMemory(TMemory memory, String s) throws EaterException {
		final EaterDumpMemory condition = new EaterDumpMemory(s);
		condition.execute(this, memory);
	}

	private void executeAssert(TMemory memory, String s) throws EaterException {
		final EaterAssert condition = new EaterAssert(s);
		condition.execute(this, memory);
	}

	private void executeIfdef(TMemory memory, String s) throws EaterException {
		final EaterIfdef condition = new EaterIfdef(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue(this, memory);
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
	}

	private void executeIfndef(TMemory memory, String s) throws EaterException {
		final EaterIfndef condition = new EaterIfndef(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue(this, memory);
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
	}

	private void executeElse(TMemory memory, String s) throws EaterException {
		final ConditionalContext poll = memory.peekConditionalContext();
		if (poll == null) {
			throw new EaterException("No if related to this else");
		}
		poll.nowInElse();
	}

	private void executeEndif(TMemory memory, String s) throws EaterException {
		final ConditionalContext poll = memory.pollConditionalContext();
		if (poll == null) {
			throw new EaterException("No if related to this endif");
		}
	}

	private void executeDeclareFunction(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0068");
		}
		final EaterDeclareFunction declareFunction = new EaterDeclareFunction(s);
		declareFunction.execute(this, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = functions2.get(declaredSignature);
		if (previous != null && (finalFlag || functionsFinal.contains(declaredSignature))) {
			throw new EaterException("This function is already defined");
		}
		if (finalFlag) {
			functionsFinal.add(declaredSignature);
		}
		if (declareFunction.getFunction().hasBody()) {
			addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
	}

	private void executeUndef(TMemory memory, StringLocated s) throws EaterException {
		final EaterUndef undef = new EaterUndef(s);
		undef.execute(this, memory);
	}

	private void executeLegacyDefine(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0048");
		}
		final EaterLegacyDefine legacyDefine = new EaterLegacyDefine(s);
		legacyDefine.execute(this, memory);
		final TFunction function = legacyDefine.getFunction();
		// if (functions2.containsKey(function.getSignature())) {
		// throw new EaterException("already0047");
		// }
		this.functions2.put(function.getSignature(), function);
		this.functions3.add(function.getSignature().getFunctionName() + "(");
	}

	private void executeLegacyDefineLong(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0068");
		}
		final EaterLegacyDefineLong legacyDefineLong = new EaterLegacyDefineLong(s);
		legacyDefineLong.execute(this, memory);
		// if (functions2.containsKey(legacyDefineLong.getFunction().getSignature())) {
		// throw new EaterException("already0066");
		// }
		this.pendingFunction = legacyDefineLong.getFunction();
	}

	private StringLocated applyFunctionsAndVariables(TMemory memory, StringLocated located) throws EaterException {
		if (memory.isEmpty() && functions2.size() == 0) {
			return located;
		}
		final String s = located.getString();
		final String result = applyFunctionsAndVariables(memory, s);
		if (result == null) {
			return null;
		}
		return new StringLocated(result, located.getLocation());
	}

	private String pendingAdd = null;

	public String applyFunctionsAndVariables(TMemory memory, String s) throws EaterException {
		// https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm
		// https://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most
		// https://en.wikipedia.org/wiki/String-searching_algorithm
		// https://www.quora.com/What-is-the-most-efficient-algorithm-to-replace-all-occurrences-of-a-pattern-P-in-a-string-with-a-pattern-P
		// https://en.wikipedia.org/wiki/Trie
		if (memory.isEmpty() && functions2.size() == 0) {
			return s;
		}
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			final String presentFunction = getFunctionNameAt(s, i);
			if (presentFunction != null) {
				final String sub = s.substring(i);
				final EaterFunctionCall call = new EaterFunctionCall(sub, isLegacyDefine(presentFunction),
						isUnquoted(presentFunction));
				call.execute(this, memory);
				final TFunction function = getFunctionSmart(new TFunctionSignature(presentFunction, call.getValues()
						.size()));
				if (function == null) {
					throw new EaterException("Function not found " + presentFunction);
				}
				if (function.getFunctionType() == TFunctionType.VOID) {
					this.pendingAdd = result.toString();
					executeVoid3(memory, sub, function);
					return null;
				}
				if (function.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
					this.pendingAdd = s.substring(0, i);
					executeVoid3(memory, sub, function);
					return null;
				}
				assert function.getFunctionType() == TFunctionType.RETURN
						|| function.getFunctionType() == TFunctionType.LEGACY_DEFINE;
				final TValue functionReturn = function.executeReturn(this, memory, call.getValues());
				result.append(functionReturn.toString());
				i += call.getCurrentPosition() - 1;
				continue;
			}
			final String presentVariable = getVarnameAt(memory, s, i);
			if (presentVariable != null) {
				if (result.toString().endsWith("##")) {
					result.setLength(result.length() - 2);
				}
				final TValue value = memory.getVariable(presentVariable).getValue();
				i += presentVariable.length() - 1;
				if (value.isJson()) {
					JsonValue jsonValue = (JsonObject) value.toJson();
					// System.err.println("jsonValue1=" + jsonValue);
					i++;
					while (true) {
						final char n = s.charAt(i);
						// System.err.println("n=" + n);
						if (n != '.') {
							if (jsonValue.isString()) {
								result.append(jsonValue.asString());
							} else {
								result.append(jsonValue.toString());
							}
							break;
						}
						i++;
						final StringBuilder fieldName = new StringBuilder();
						while (true) {
							if (Character.isJavaIdentifierPart(s.charAt(i)) == false) {
								break;
							}
							fieldName.append(s.charAt(i));
							i++;
						}
						// System.err.println("fieldName=" + fieldName);
						jsonValue = ((JsonObject) jsonValue).get(fieldName.toString());
						// System.err.println("jsonValue2=" + jsonValue);
					}
				} else {
					result.append(value.toString());
				}
				if (i + 2 < s.length() && s.charAt(i + 1) == '#' && s.charAt(i + 2) == '#') {
					i += 2;
				}
				continue;
			}
			result.append(c);
		}
		return result.toString();
	}

	private void executeVoid3(TMemory memory, String s, TFunction function) throws EaterException {
		function.executeVoid(this, memory, s);
	}

	private void executeImport(TMemory memory, StringLocated s) throws EaterException {
		final EaterImport _import = new EaterImport(s.getTrimmed().getString());
		_import.execute(this, memory);

		try {
			final File file = FileSystem.getInstance().getFile(
					applyFunctionsAndVariables(memory, _import.getLocation()));
			if (file.exists() && file.isDirectory() == false) {
				importedFiles.add(file);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new EaterException("Cannot import " + e.getMessage());
		}

		throw new EaterException("Cannot import");
	}

	private void executeLog(TMemory memory, StringLocated s) throws EaterException {
		final EaterLog log = new EaterLog(s.getTrimmed().getString());
		log.execute(this, memory);
	}

	private void executeIncludesub(TMemory memory, StringLocated s) throws EaterException {
		ImportedFiles saveImportedFiles = null;
		try {
			final EaterIncludesub include = new EaterIncludesub(s.getTrimmed().getString());
			include.execute(this, memory);
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
						// readerline = new ReadLineQuoteComment(true).applyFilter(readerline);
						sub = Sub.fromFile(readerline, blocname, this, memory);
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new EaterException("cannot include " + e);
				}
			}
			if (sub == null) {
				sub = subs.get(location);
			}
			if (sub == null) {
				throw new EaterException("cannot include " + location);
			}
			runSub(memory, sub);
		} finally {
			if (saveImportedFiles != null) {
				this.importedFiles = saveImportedFiles;
			}
		}
	}

	private void runSub(TMemory memory, final Sub sub) throws EaterException {
		for (StringLocated sl : sub.lines()) {
			executeOneLine(memory, TLineType.getFromLine(sl.getString()), sl, null);
		}
	}

	private void executeIncludeDef(TMemory memory, StringLocated s) throws EaterException {
		final EaterIncludeDef include = new EaterIncludeDef(s.getTrimmed().getString());
		include.execute(this, memory);
		final String definitionName = include.getLocation();
		final List<String> definition = definitionsContainer.getDefinition(definitionName);
		ReadLine reader2 = new ReadLineList(definition, s.getLocation());

		try {
			// reader2 = new ReadLineQuoteComment(true).applyFilter(reader2);
			do {
				final StringLocated sl = reader2.readLine();
				if (sl == null) {
					return;
				}
				executeOneLine(memory, TLineType.getFromLine(sl.getString()), sl, null);
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new EaterException("" + e);
		}
	}

	private void executeInclude(TMemory memory, StringLocated s) throws EaterException {
		final EaterInclude include = new EaterInclude(s.getTrimmed().getString());
		include.execute(this, memory);
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
						throw new EaterException("This file has already been included");
					}

					if (StartDiagramExtractReader.containsStartDiagram(f2, s, charset)) {
						reader2 = StartDiagramExtractReader.build(f2, s, charset);
					} else {
						final Reader reader = f2.getReader(charset);
						if (reader == null) {
							throw new EaterException("Cannot include file");
						}
						reader2 = ReadLineReader.create(reader, location, s.getLocation());
					}
					saveImportedFiles = this.importedFiles;
					this.importedFiles = this.importedFiles.withCurrentDir(f2.getParentFile());
					assert reader2 != null;
					filesUsedCurrent.add(f2);
					// filesUsedGlobal.add(f2);
				}
			}
			if (reader2 != null) {
				// reader2 = new ReadLineQuoteComment(true).applyFilter(reader2);
				try {
					do {
						final StringLocated sl = reader2.readLine();
						if (sl == null) {
							return;
						}
						executeOneLine(memory, TLineType.getFromLine(sl.getString()), sl, null);
					} while (true);
				} finally {
					if (saveImportedFiles != null) {
						this.importedFiles = saveImportedFiles;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new EaterException("cannot include " + e);
		}

		throw new EaterException("cannot include " + location);
	}

	public boolean isLegacyDefine(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functions2.entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().getFunctionType().isLegacy()) {
				return true;
			}
		}
		return false;
	}

	public boolean isUnquoted(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functions2.entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName) && ent.getValue().isUnquoted()) {
				return true;
			}
		}
		return false;
	}

	public boolean doesFunctionExist(String functionName) {
		for (Map.Entry<TFunctionSignature, TFunction> ent : functions2.entrySet()) {
			if (ent.getKey().getFunctionName().equals(functionName)) {
				return true;
			}
		}
		return false;
	}

	private static String getVarnameAt(TMemory memory, String s, int pos) {
		if (pos > 0 && TLineType.isLetterOrUnderscoreOrDigit(s.charAt(pos - 1)) && justAfterBackslashN(s, pos) == false) {
			return null;
		}
		final String varname = memory.variablesNames3().getLonguestMatchStartingIn(s.substring(pos));
		if (varname.length() == 0) {
			return null;
		}
		if (pos + varname.length() == s.length()
				|| TLineType.isLetterOrUnderscoreOrDigit(s.charAt(pos + varname.length())) == false) {
			return varname;
		}
		return null;
	}

	private static boolean justAfterBackslashN(String s, int pos) {
		return pos > 1 && s.charAt(pos - 2) == '\\' && s.charAt(pos - 1) == 'n';
	}

	private String getFunctionNameAt(String s, int pos) {
		if (pos > 0 && TLineType.isLetterOrUnderscoreOrDigit(s.charAt(pos - 1)) && justAfterBackslashN(s, pos) == false) {
			return null;
		}
		final String fname = functions3.getLonguestMatchStartingIn(s.substring(pos));
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

	public final TFunctionImpl getPendingFunction() {
		return pendingFunction;
	}

	private void addFunction(TFunction func) {
		if (func.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
			((TFunctionImpl) func).finalizeEnddefinelong();
		}
		this.functions2.put(func.getSignature(), func);
		this.functions3.add(func.getSignature().getFunctionName() + "(");
	}

	public void executeEndfunction() {
		addFunction(pendingFunction);
		pendingFunction = null;
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

}
