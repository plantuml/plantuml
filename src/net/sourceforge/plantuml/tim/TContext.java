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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.preproc.ImportedFiles;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineReader;
import net.sourceforge.plantuml.preproc2.PreprocessorInclude;
import net.sourceforge.plantuml.tim.expression.Knowledge;
import net.sourceforge.plantuml.tim.expression.TValue;
import net.sourceforge.plantuml.tim.stdlib.DateFunction;
import net.sourceforge.plantuml.tim.stdlib.Dirpath;
import net.sourceforge.plantuml.tim.stdlib.FileExists;
import net.sourceforge.plantuml.tim.stdlib.Filename;
import net.sourceforge.plantuml.tim.stdlib.Getenv;
import net.sourceforge.plantuml.tim.stdlib.Strlen;
import net.sourceforge.plantuml.tim.stdlib.Strpos;
import net.sourceforge.plantuml.tim.stdlib.Substr;

public class TContext {

	private final ArrayList<StringLocated> result = new ArrayList<StringLocated>();
	private final Map<TFunctionSignature, TFunction> functions2 = new HashMap<TFunctionSignature, TFunction>();
	private final Trie functions3 = new Trie();
	private final ImportedFiles importedFiles;

	private TFunctionImpl pendingFunction;

	private void addStandardFunctions(Defines defines) {
		addFunction(new Strlen());
		addFunction(new Substr());
		addFunction(new FileExists());
		addFunction(new Getenv());
		addFunction(new Dirpath(defines));
		addFunction(new Filename(defines));
		addFunction(new DateFunction());
		addFunction(new Strpos());
	}


	public TContext(ImportedFiles importedFiles, Defines defines) {
		this.importedFiles = importedFiles;
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

	private TFunction getFunctionSmart(TFunctionSignature searched) {
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

	public CommandExecutionResult executeOneLine(TMemory memory, TLineType type, StringLocated s, TFunctionType fromType) {
		if (this.getPendingFunction() != null) {
			if (type == TLineType.END_FUNCTION) {
				this.executeEndfunction();
			} else {
				this.getPendingFunction().addBody(s);
			}
			return CommandExecutionResult.ok();
		}
		assert type == TLineType.getFromLine(s.getString());
		try {

			if (type == TLineType.ASSERT) {
				return this.executeAssert(memory, s.getStringTrimmed());
			} else if (type == TLineType.IF) {
				return this.executeIf(memory, s.getStringTrimmed());
			} else if (type == TLineType.IFDEF) {
				return this.executeIfdef(memory, s.getStringTrimmed());
			} else if (type == TLineType.IFNDEF) {
				return this.executeIfndef(memory, s.getStringTrimmed());
			} else if (type == TLineType.ELSE) {
				return this.executeElse(memory, s.getStringTrimmed());
			} else if (type == TLineType.ENDIF) {
				return this.executeEndif(memory, s.getStringTrimmed());
			}

			final ConditionalContext conditionalContext = memory.peekConditionalContext();
			if (conditionalContext != null && conditionalContext.conditionIsOkHere() == false) {
				return CommandExecutionResult.ok();
			}

			if (fromType != TFunctionType.RETURN && type == TLineType.PLAIN) {
				return this.addPlain(memory, s);
			} else if (fromType == TFunctionType.RETURN && type == TLineType.RETURN) {
				// Actually, ignore because we are in a if
				return CommandExecutionResult.ok();
			} else if (type == TLineType.LEGACY_DEFINE) {
				return this.executeLegacyDefine(memory, s);
			} else if (type == TLineType.LEGACY_DEFINELONG) {
				return this.executeLegacyDefineLong(memory, s);
			} else if (type == TLineType.AFFECTATION_DEFINE) {
				return this.executeAffectationDefine(memory, s.getStringTrimmed());
			} else if (type == TLineType.AFFECTATION) {
				return this.executeAffectation(memory, s.getStringTrimmed());
			} else if (fromType == null && type == TLineType.DECLARE_FUNCTION) {
				return this.executeDeclareFunction(memory, s);
			} else if (fromType == null && type == TLineType.END_FUNCTION) {
				return CommandExecutionResult.error("error endfunc");
			} else if (type == TLineType.INCLUDE) {
				return this.executeInclude(memory, s);
			} else if (type == TLineType.IMPORT) {
				return this.executeImport(memory, s);
			} else {
				throw new UnsupportedOperationException("type=" + type + " fromType=" + fromType);
			}
		} catch (EaterException e) {
			e.printStackTrace();
			return CommandExecutionResult.error(e.getMessage());
		}

	}

	private CommandExecutionResult addPlain(TMemory memory, StringLocated s) throws EaterException {
		StringLocated tmp = applyFunctionsAndVariables(memory, s);
		if (tmp != null) {
			if (pendingAdd != null) {
				tmp = new StringLocated(pendingAdd + tmp.getString(), tmp.getLocation());
				pendingAdd = null;
			}
			result.add(tmp);
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeAffectationDefine(TMemory memory, String s) throws EaterException {
		new EaterAffectationDefine(s).execute(this, memory);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeAffectation(TMemory memory, String s) throws EaterException {
		new EaterAffectation(s).execute(this, memory);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeIf(TMemory memory, String s) throws EaterException {
		final EaterIf condition = new EaterIf(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue();
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeAssert(TMemory memory, String s) throws EaterException {
		final EaterAssert condition = new EaterAssert(s);
		condition.execute(this, memory);
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeIfdef(TMemory memory, String s) throws EaterException {
		final EaterIfdef condition = new EaterIfdef(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue(this, memory);
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeIfndef(TMemory memory, String s) throws EaterException {
		final EaterIfndef condition = new EaterIfndef(s);
		condition.execute(this, memory);
		final boolean isTrue = condition.isTrue(this, memory);
		memory.addConditionalContext(ConditionalContext.fromValue(isTrue));
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeElse(TMemory memory, String s) throws EaterException {
		final ConditionalContext poll = memory.peekConditionalContext();
		if (poll == null) {
			return CommandExecutionResult.error("No if related to this else");
		}
		poll.nowInElse();
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeEndif(TMemory memory, String s) throws EaterException {
		final ConditionalContext poll = memory.pollConditionalContext();
		if (poll == null) {
			return CommandExecutionResult.error("No if related to this endif");
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeDeclareFunction(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0068");
		}
		final EaterDeclareFunction declareFunction = new EaterDeclareFunction(s);
		declareFunction.execute(this, memory);
		if (functions2.containsKey(declareFunction.getFunction().getSignature())) {
			throw new EaterException("already0046");
		}
		if (declareFunction.getFunction().hasBody()) {
			addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeLegacyDefine(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0048");
		}
		final EaterLegacyDefine legacyDefine = new EaterLegacyDefine(s);
		legacyDefine.execute(this, memory);
		final TFunction function = legacyDefine.getFunction();
		if (functions2.containsKey(function.getSignature())) {
			throw new EaterException("already0047");
		}
		this.functions2.put(function.getSignature(), function);
		this.functions3.add(function.getSignature().getFunctionName() + "(");
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeLegacyDefineLong(TMemory memory, StringLocated s) throws EaterException {
		if (this.pendingFunction != null) {
			throw new EaterException("already0068");
		}
		final EaterLegacyDefineLong legacyDefineLong = new EaterLegacyDefineLong(s);
		legacyDefineLong.execute(this, memory);
		if (functions2.containsKey(legacyDefineLong.getFunction().getSignature())) {
			throw new EaterException("already0066");
		}
		this.pendingFunction = legacyDefineLong.getFunction();
		return CommandExecutionResult.ok();
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
				final TFunction function = getFunctionSmart(new TFunctionSignature(presentFunction, call.getValues2()
						.size()));
				if (function == null) {
					throw new EaterException("Function not found " + presentFunction);
				}
				if (function.getFunctionType() == TFunctionType.VOID) {
					function.executeVoid(this, sub, memory);
					return null;
				}
				if (function.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
					this.pendingAdd = s.substring(0, i);
					function.executeVoid(this, sub, memory);
					return null;
				}
				assert function.getFunctionType() == TFunctionType.RETURN
						|| function.getFunctionType() == TFunctionType.LEGACY_DEFINE;
				final TValue functionReturn = function.executeReturn(this, memory, call.getValues2());
				result.append(functionReturn.toString());
				i += call.getCurrentPosition() - 1;
				continue;
			}
			final String presentVariable = getVarnameAt(memory, s, i);
			if (presentVariable != null) {
				result.append(memory.getVariable(presentVariable).getValue2().toString());
				i += presentVariable.length() - 1;
				if (i + 2 < s.length() && s.charAt(i + 1) == '#' && s.charAt(i + 2) == '#') {
					i += 2;
				}
				continue;
			}
			result.append(c);
		}
		return result.toString();
	}

	private CommandExecutionResult executeImport(TMemory memory, StringLocated s) throws EaterException {
		final EaterImport _import = new EaterImport(s.getStringTrimmed());
		_import.execute(this, memory);

		try {
			final File file = FileSystem.getInstance().getFile(
					applyFunctionsAndVariables(memory, _import.getLocation()));
			if (file.exists() && file.isDirectory() == false) {
				importedFiles.add(file);
				return CommandExecutionResult.ok();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return CommandExecutionResult.error("Cannot import " + e.getMessage());
		}

		return CommandExecutionResult.error("Cannot import");
	}

	private CommandExecutionResult executeInclude(TMemory memory, StringLocated s) throws EaterException {
		final EaterInclude include = new EaterInclude(s.getStringTrimmed());
		include.execute(this, memory);
		String location = include.getLocation();
		final int idx = location.lastIndexOf('!');
		String suf = null;
		if (idx != -1) {
			suf = location.substring(idx + 1);
			location = location.substring(0, idx);
		}

		final String charset = null;

		ReadLine reader2 = null;
		try {
			if (location.startsWith("http://") || location.startsWith("https://")) {
				final URL url = new URL(location);
				reader2 = PreprocessorInclude.getReaderIncludeUrl(url, s, suf, charset);

			}
			if (location.startsWith("<") && location.endsWith(">")) {
				reader2 = PreprocessorInclude.getReaderStdlibInclude(s, location.substring(1, location.length() - 1));
			} else if (OptionFlags.ALLOW_INCLUDE) {
				final FileWithSuffix f2 = new FileWithSuffix(importedFiles, location, suf);
				if (f2.fileOk()) {
					final Reader reader = f2.getReader(charset);
					reader2 = ReadLineReader.create(reader, location, s.getLocation());
				}
			}
			if (reader2 != null) {
				do {
					final StringLocated sl = reader2.readLine();
					if (sl == null) {
						return CommandExecutionResult.ok();
					}
					final CommandExecutionResult exe = executeOneLine(memory, TLineType.getFromLine(sl.getString()),
							sl, null);
					if (exe.isOk() == false) {
						return exe;
					}
				} while (true);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return CommandExecutionResult.error("cannot include " + e);
		}

		System.err.println("location=" + location);
		return CommandExecutionResult.error("cannot include");
	}

	private Reader getReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
		final String charset = null;
		if (charset == null) {
			Log.info("Using default charset");
			return new InputStreamReader(new FileInputStream(file));
		}
		Log.info("Using charset " + charset);
		return new InputStreamReader(new FileInputStream(file), charset);
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
		final String varname = memory.variablesNames3().getLonguestMatchStartingIn(s.substring(pos));
		if (varname.length() == 0) {
			return null;
		}
		if (pos + varname.length() == s.length()
				|| Character.isLetterOrDigit(s.charAt(pos + varname.length())) == false) {
			return varname;
		}
		return null;
	}

	private static String getVarnameAtOld(TMemory memory, String s, int pos) {
		for (String varname : memory.variablesNames()) {
			if (s.substring(pos).startsWith(varname))
				if (pos + varname.length() == s.length()
						|| Character.isLetterOrDigit(s.charAt(pos + varname.length())) == false) {
					return varname;
				}
		}
		return null;
	}

	private String getFunctionNameAt(String s, int pos) {
		final String fname = functions3.getLonguestMatchStartingIn(s.substring(pos));
		if (fname.length() == 0) {
			return null;
		}
		return fname.substring(0, fname.length() - 1);
	}

	private String getFunctionNameAtOld(String s, int pos) {
		for (TFunctionSignature fname : functions2.keySet()) {
			if (s.substring(pos).startsWith(fname.getFunctionName() + "(")) {
				return fname.getFunctionName();
			}
		}
		return null;
	}

	public List<StringLocated> getResult() {
		return Collections.unmodifiableList(result);
	}

	public List<StringLocated> getResultWithError(StringLocated error) {
		result.add(error);
		return Collections.unmodifiableList(result);
	}

	public final TFunctionImpl getPendingFunction() {
		return pendingFunction;
	}

	private void addFunction(TFunction func) {
		this.functions2.put(func.getSignature(), func);
		this.functions3.add(func.getSignature().getFunctionName() + "(");
	}

	public void executeEndfunction() {
		addFunction(pendingFunction);
		pendingFunction = null;
	}

}
