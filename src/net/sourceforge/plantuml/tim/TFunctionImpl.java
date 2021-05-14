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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TFunctionImpl implements TFunction {

	private final TFunctionSignature signature;
	private final List<TFunctionArgument> args;
	private final List<StringLocated> body = new ArrayList<>();
	private final boolean unquoted;
	private /* final */ TFunctionType functionType;// = TFunctionType.VOID;
	private String legacyDefinition;
	private boolean containsReturn;

	public TFunctionImpl(String functionName, List<TFunctionArgument> args, boolean unquoted,
			TFunctionType functionType) {
		final Set<String> names = new HashSet<>();
		for (TFunctionArgument tmp : args) {
			names.add(tmp.getName());
		}
		this.signature = new TFunctionSignature(functionName, args.size(), names);
		this.args = args;
		this.unquoted = unquoted;
		this.functionType = functionType;
	}

	public boolean canCover(int nbArg, Set<String> namedArguments) {
		for (String n : namedArguments) {
			if (signature.getNamedArguments().contains(n) == false) {
				return false;
			}
		}
		if (nbArg > args.size()) {
			return false;
		}
		assert nbArg <= args.size();
		int neededArgument = 0;
		for (TFunctionArgument arg : args) {
			if (namedArguments.contains(arg.getName())) {
				continue;
			}
			if (arg.getOptionalDefaultValue() == null) {
				neededArgument++;
			}
		}
		if (nbArg < neededArgument) {
			return false;
		}
		assert nbArg >= neededArgument;
		return true;
	}

	private TMemory getNewMemory(TMemory memory, List<TValue> values, Map<String, TValue> namedArguments) {
		final Map<String, TValue> result = new HashMap<String, TValue>();
		int ivalue = 0;
		for (TFunctionArgument arg : args) {
			final TValue value;
			if (namedArguments.containsKey(arg.getName())) {
				value = namedArguments.get(arg.getName());
			} else if (ivalue < values.size()) {
				value = values.get(ivalue);
				ivalue++;
			} else {
				value = arg.getOptionalDefaultValue();
			}
			if (value == null) {
				throw new IllegalStateException();
			}
			result.put(arg.getName(), value);
		}
		return memory.forkFromGlobal(result);
	}

	@Override
	public String toString() {
		return "FUNCTION " + signature + " " + args;
	}

	public void addBody(StringLocated s) throws EaterExceptionLocated {
		body.add(s);
		if (s.getType() == TLineType.RETURN) {
			this.containsReturn = true;
			if (functionType == TFunctionType.PROCEDURE) {
				throw EaterExceptionLocated
						.located("A procedure cannot have !return directive. Declare it as a function instead ?", s);
				// this.functionType = TFunctionType.RETURN;
			}
		}
	}

	public void executeProcedure(TContext context, TMemory memory, LineLocation location, String s)
			throws EaterException, EaterExceptionLocated {
		final EaterFunctionCall call = new EaterFunctionCall(new StringLocated(s, location),
				context.isLegacyDefine(signature.getFunctionName()), unquoted);
		call.analyze(context, memory);
		final String endOfLine = call.getEndOfLine();
		final List<TValue> args = call.getValues();
		final Map<String, TValue> named = call.getNamedArguments();
		executeProcedureInternal(context, memory, args, named);
		context.appendEndOfLine(endOfLine);
	}

	public void executeProcedureInternal(TContext context, TMemory memory, List<TValue> args, Map<String, TValue> named)
			throws EaterException, EaterExceptionLocated {
		if (functionType != TFunctionType.PROCEDURE && functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args, named);
		context.executeLines(copy, body, TFunctionType.PROCEDURE, false);
	}

	public TValue executeReturnFunction(TContext context, TMemory memory, LineLocation location, List<TValue> args,
			Map<String, TValue> named) throws EaterException, EaterExceptionLocated {
		if (functionType == TFunctionType.LEGACY_DEFINE) {
			return executeReturnLegacyDefine(location, context, memory, args);
		}
		if (functionType != TFunctionType.RETURN_FUNCTION) {
			throw EaterException.unlocated("Illegal call here. Is there a return directive in your function?");
		}
		final TMemory copy = getNewMemory(memory, args, named);
		final TValue result = context.executeLines(copy, body, TFunctionType.RETURN_FUNCTION, true);
		if (result == null) {
			throw EaterException.unlocated("No return directive found in your function");
		}
		return result;
	}

	private TValue executeReturnLegacyDefine(LineLocation location, TContext context, TMemory memory, List<TValue> args)
			throws EaterException, EaterExceptionLocated {
		if (legacyDefinition == null) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args, Collections.<String, TValue>emptyMap());
		final String tmp = context.applyFunctionsAndVariables(copy, location, legacyDefinition);
		if (tmp == null) {
			return TValue.fromString("");
		}
		return TValue.fromString(tmp);
		// eaterReturn.execute(context, copy);
		// // System.err.println("s3=" + eaterReturn.getValue2());
		// return eaterReturn.getValue2();
	}

	public final TFunctionType getFunctionType() {
		return functionType;
	}

	public final TFunctionSignature getSignature() {
		return signature;
	}

//	public void setFunctionType(TFunctionType type) {
//		this.functionType = type;
//	}

	public void setLegacyDefinition(String legacyDefinition) {
		this.legacyDefinition = legacyDefinition;
	}

	public boolean isUnquoted() {
		return unquoted;
	}

	public boolean hasBody() {
		return body.size() > 0;
	}

	public void finalizeEnddefinelong() {
		if (functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new UnsupportedOperationException();
		}
		if (body.size() == 1) {
			this.functionType = TFunctionType.LEGACY_DEFINE;
			this.legacyDefinition = body.get(0).getString();
		}
	}

	public final boolean doesContainReturn() {
		return containsReturn;
	}

}
