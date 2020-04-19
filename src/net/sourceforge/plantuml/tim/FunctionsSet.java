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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.StringLocated;

public class FunctionsSet {

	private final Map<TFunctionSignature, TFunction> functions = new HashMap<TFunctionSignature, TFunction>();
	private final Set<TFunctionSignature> functionsFinal = new HashSet<TFunctionSignature>();
	private final Trie functions3 = new TrieImpl();
	private TFunctionImpl pendingFunction;

	public TFunction getFunctionSmart(TFunctionSignature searched) {
		final TFunction func = this.functions.get(searched);
		if (func != null) {
			return func;
		}
		for (TFunction candidate : this.functions.values()) {
			if (candidate.getSignature().sameNameAs(searched) == false) {
				continue;
			}
			if (candidate.canCover(searched.getNbArg())) {
				return candidate;
			}
		}
		return null;
	}

	public int size() {
		return functions().size();
	}

	public Map<TFunctionSignature, TFunction> functions() {
		return Collections.unmodifiableMap(functions);
	}

	public String getLonguestMatchStartingIn(String s) {
		return functions3.getLonguestMatchStartingIn(s);
	}

	public TFunctionImpl pendingFunction() {
		return pendingFunction;
	}

	public void addFunction(TFunction func) {
		if (func.getFunctionType() == TFunctionType.LEGACY_DEFINELONG) {
			((TFunctionImpl) func).finalizeEnddefinelong();
		}
		this.functions.put(func.getSignature(), func);
		this.functions3.add(func.getSignature().getFunctionName() + "(");
	}

	public void executeEndfunction() {
		this.addFunction(this.pendingFunction);
		this.pendingFunction = null;
	}

	public void executeLegacyDefine(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0048", s);
		}
		final EaterLegacyDefine legacyDefine = new EaterLegacyDefine(s);
		legacyDefine.analyze(context, memory);
		final TFunction function = legacyDefine.getFunction();
		this.functions.put(function.getSignature(), function);
		this.functions3.add(function.getSignature().getFunctionName() + "(");
	}

	public void executeLegacyDefineLong(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068", s);
		}
		final EaterLegacyDefineLong legacyDefineLong = new EaterLegacyDefineLong(s);
		legacyDefineLong.analyze(context, memory);
		this.pendingFunction = legacyDefineLong.getFunction();
	}

	public void executeDeclareReturnFunction(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068", s);
		}
		final EaterDeclareReturnFunction declareFunction = new EaterDeclareReturnFunction(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.functions.get(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature))) {
			throw EaterException.located("This function is already defined", s);
		}
		if (finalFlag) {
			this.functionsFinal.add(declaredSignature);
		}
		if (declareFunction.getFunction().hasBody()) {
			this.addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
	}

	public void executeDeclareProcedure(TContext context, TMemory memory, StringLocated s)
			throws EaterException, EaterExceptionLocated {
		if (this.pendingFunction != null) {
			throw EaterException.located("already0068", s);
		}
		final EaterDeclareProcedure declareFunction = new EaterDeclareProcedure(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.functions.get(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature))) {
			throw EaterException.located("This function is already defined", s);
		}
		if (finalFlag) {
			this.functionsFinal.add(declaredSignature);
		}
		if (declareFunction.getFunction().hasBody()) {
			this.addFunction(declareFunction.getFunction());
		} else {
			this.pendingFunction = declareFunction.getFunction();
		}
	}

}
