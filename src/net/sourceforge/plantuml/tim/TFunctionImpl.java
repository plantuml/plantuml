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
import java.util.List;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TFunctionImpl implements TFunction {

	private final TFunctionSignature signature;
	private final List<TFunctionArgument> args;
	private final List<StringLocated> body = new ArrayList<StringLocated>();
	private final boolean unquoted;
	private TFunctionType functionType = TFunctionType.VOID;
	private String legacyDefinition;

	public TFunctionImpl(String functionName, List<TFunctionArgument> args, boolean unquoted) {
		this.signature = new TFunctionSignature(functionName, args.size());
		this.args = args;
		this.unquoted = unquoted;
	}

	public boolean canCover(int nbArg) {
		if (nbArg > args.size()) {
			return false;
		}
		for (int i = nbArg; i < args.size(); i++) {
			if (args.get(i).getOptionalDefaultValue() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "FUNCTION " + signature + " " + args;
	}

	public void addBody(StringLocated s) {
		body.add(s);
		if (TLineType.getFromLine(s.getString()) == TLineType.RETURN) {
			this.functionType = TFunctionType.RETURN;
		}
	}

	public void executeVoid(TContext context, String s, TMemory memory) throws EaterException {
		if (functionType != TFunctionType.VOID && functionType != TFunctionType.LEGACY_DEFINELONG) {
			throw new IllegalStateException();
		}
		final EaterFunctionCall call = new EaterFunctionCall(s, context.isLegacyDefine(signature.getFunctionName()),
				unquoted);
		call.execute(context, memory);
		final TMemory copy = getNewMemory(memory, call.getValues2());
		for (StringLocated sl : body) {
			final CommandExecutionResult exe = context.executeOneLine(copy, TLineType.getFromLine(sl.getString()), sl,
					TFunctionType.VOID);
			if (exe.isOk() == false) {
				throw new EaterException(exe.getError());
			}
		}

	}

	private TMemory getNewMemory(TMemory memory, List<TValue> values) {
		final TMemory copy = memory.forkFromGlobal();
		for (int i = 0; i < args.size(); i++) {
			final TValue tmp = i < values.size() ? values.get(i) : args.get(i).getOptionalDefaultValue();
			copy.put(args.get(i).getName(), new TVariable(tmp));
		}
		return copy;
	}

	public TValue executeReturn(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		if (functionType == TFunctionType.LEGACY_DEFINE) {
			return executeReturnLegacyDefine(context, memory, args);
		}
		if (functionType != TFunctionType.RETURN) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args);

		for (StringLocated sl : body) {
			final TLineType lineType = TLineType.getFromLine(sl.getString());
			final ConditionalContext conditionalContext = copy.peekConditionalContext();
			if ((conditionalContext == null || conditionalContext.conditionIsOkHere()) && lineType == TLineType.RETURN) {
				// System.err.println("s2=" + sl.getString());
				final EaterReturn eaterReturn = new EaterReturn(sl.getString());
				eaterReturn.execute(context, copy);
				// System.err.println("s3=" + eaterReturn.getValue2());
				return eaterReturn.getValue2();
			}
			final CommandExecutionResult exe = context.executeOneLine(copy, lineType, sl, TFunctionType.RETURN);
			if (exe.isOk() == false) {
				throw new EaterException(exe.getError());
			}
		}
		throw new EaterException("no return");
		// return TValue.fromString("(NONE)");
	}

	private TValue executeReturnLegacyDefine(TContext context, TMemory memory, List<TValue> args) throws EaterException {
		if (legacyDefinition == null) {
			throw new IllegalStateException();
		}
		final TMemory copy = getNewMemory(memory, args);
		final String tmp = context.applyFunctionsAndVariables(copy, legacyDefinition);
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

	public void setFunctionType(TFunctionType type) {
		this.functionType = type;
	}

	public void setLegacyDefinition(String legacyDefinition) {
		this.legacyDefinition = legacyDefinition;
	}

	public boolean isUnquoted() {
		return unquoted;
	}

	public boolean hasBody() {
		return body.size() > 0;
	}

}
