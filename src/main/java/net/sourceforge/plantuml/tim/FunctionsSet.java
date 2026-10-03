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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.text.StringLocated;

/**
 * The functions known by a diagram. They are kept in two layers: the shared and
 * read-only registry of the standard functions, and the functions that the
 * diagram defines itself, which take precedence.
 */
public class FunctionsSet {

	private final FunctionsSet base;
	private final Map<TFunctionSignature, TFunction> functions = new HashMap<TFunctionSignature, TFunction>();
	private final Map<String, Map<TFunctionSignature, TFunction>> functionsByName = new HashMap<>();
	private final Set<TFunctionSignature> functionsFinal = new HashSet<>();
	private final Trie functions3 = new TrieImpl();
	private TFunctionImpl pendingFunction;

	public FunctionsSet() {
		this(null);
	}

	/**
	 * @param base a registry that is only read, never modified, or null
	 */
	public FunctionsSet(FunctionsSet base) {
		this.base = base;
	}

	private TFunction getExact(TFunctionSignature searched) {
		final TFunction func = this.functions.get(searched);
		if (func == null && base != null)
			return base.getExact(searched);
		return func;
	}

	public TFunction getFunctionSmart(TFunctionSignature searched) {
		final TFunction func = getExact(searched);
		if (func != null)
			return func;

		return searchCovering(searched);
	}

	private TFunction searchCovering(TFunctionSignature searched) {
		for (TFunction candidate : this.functions.values())
			if (isCovering(candidate, searched))
				return candidate;

		if (base == null)
			return null;

		for (TFunction candidate : base.functions.values())
			if (this.functions.containsKey(candidate.getSignature()) == false && isCovering(candidate, searched))
				return candidate;

		return null;
	}

	private static boolean isCovering(TFunction candidate, TFunctionSignature searched) {
		return candidate.getSignature().sameFunctionNameAs(searched)
				&& candidate.canCover(searched.getNbArg(), searched.getNamedArguments());
	}

	public String getLonguestMatchStartingIn(String s, int pos) {
		final String result = functions3.getLonguestMatchStartingIn(s, pos);
		if (base == null || result.length() > 0)
			return result;
		return base.getLonguestMatchStartingIn(s, pos);
	}

	public TFunctionImpl pendingFunction() {
		return pendingFunction;
	}

	public void addFunction(TFunction func) {
		if (func.getFunctionType() == TFunctionType.LEGACY_DEFINELONG)
			((TFunctionImpl) func).finalizeEnddefinelong();

		this.functions.put(func.getSignature(), func);
		this.functions3.add(func.getSignature().getFunctionName() + "(");
		this.updateFunctionsByName(func);
	}

	private void updateFunctionsByName(TFunction func) {
		final String name = func.getSignature().getFunctionName();
		this.functionsByName.computeIfAbsent(name, k -> new HashMap<>()).put(func.getSignature(), func);
	}

	/**
	 * Returns true if at least one function with the given name exists.
	 */
	public boolean doesFunctionExist(String functionName) {
		return this.functionsByName.containsKey(functionName) || (base != null && base.doesFunctionExist(functionName));
	}

	/**
	 * Returns the functions matching the given name, or an empty collection.
	 */
	public Iterable<TFunction> getFunctionsByName(String functionName) {
		final Map<TFunctionSignature, TFunction> own = this.functionsByName.get(functionName);
		final Iterable<TFunction> inherited = base == null ? Collections.<TFunction>emptyList()
				: base.getFunctionsByName(functionName);
		if (own == null)
			return inherited;

		final List<TFunction> result = new ArrayList<>(own.values());
		for (TFunction func : inherited)
			if (own.containsKey(func.getSignature()) == false)
				result.add(func);
		return result;
	}

	public void executeEndfunction() {
		this.addFunction(this.pendingFunction);
		this.pendingFunction = null;
	}

	public void executeLegacyDefine(TContext context, TMemory memory, StringLocated s)
			throws EaterException {
		if (this.pendingFunction != null)
			throw new EaterException("already0048", s);

		final EaterLegacyDefine legacyDefine = new EaterLegacyDefine(s);
		legacyDefine.analyze(context, memory);
		final TFunction function = legacyDefine.getFunction();
		this.functions.put(function.getSignature(), function);
		this.functions3.add(function.getSignature().getFunctionName() + "(");
		this.updateFunctionsByName(function);
	}

	public void executeLegacyDefineLong(TContext context, TMemory memory, StringLocated s)
			throws EaterException {
		if (this.pendingFunction != null)
			throw new EaterException("already0068", s);

		final EaterLegacyDefineLong legacyDefineLong = new EaterLegacyDefineLong(s);
		legacyDefineLong.analyze(context, memory);
		this.pendingFunction = legacyDefineLong.getFunction();
	}

	public void executeDeclareReturnFunction(TContext context, TMemory memory, StringLocated s)
			throws EaterException {
		if (this.pendingFunction != null)
			throw new EaterException("already0068", s);

		final EaterDeclareReturnFunction declareFunction = new EaterDeclareReturnFunction(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.getExact(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature)))
			throw new EaterException("This function is already defined", s);

		if (finalFlag)
			this.functionsFinal.add(declaredSignature);

		if (declareFunction.getFunction().hasBody())
			this.addFunction(declareFunction.getFunction());
		else
			this.pendingFunction = declareFunction.getFunction();

	}

	public void executeDeclareProcedure(TContext context, TMemory memory, StringLocated s)
			throws EaterException {
		if (this.pendingFunction != null)
			throw new EaterException("already0068", s);

		final EaterDeclareProcedure declareFunction = new EaterDeclareProcedure(s);
		declareFunction.analyze(context, memory);
		final boolean finalFlag = declareFunction.getFinalFlag();
		final TFunctionSignature declaredSignature = declareFunction.getFunction().getSignature();
		final TFunction previous = this.getExact(declaredSignature);
		if (previous != null && (finalFlag || this.functionsFinal.contains(declaredSignature)))
			throw new EaterException("This function is already defined", s);

		if (finalFlag)
			this.functionsFinal.add(declaredSignature);

		if (declareFunction.getFunction().hasBody())
			this.addFunction(declareFunction.getFunction());
		else
			this.pendingFunction = declareFunction.getFunction();

	}

}
