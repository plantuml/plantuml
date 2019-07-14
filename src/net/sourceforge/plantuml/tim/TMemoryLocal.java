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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TMemoryLocal extends ConditionalContexts implements TMemory {

	private final TMemoryGlobal memoryGlobal;
	private final Map<String, TVariable> overridenVariables = new HashMap<String, TVariable>();
	private final Map<String, TVariable> localVariables = new HashMap<String, TVariable>();

	public TMemoryLocal(TMemoryGlobal global, Map<String, TVariable> input) {
		this.memoryGlobal = global;
		this.overridenVariables.putAll(input);
	}
	
	public void dumpDebug(String message) {
		Log.error("[MemLocal] Start of memory_dump " + message);
		memoryGlobal.dumpMemoryInternal();
		Log.error("[MemLocal] Number of overriden variable(s) : " + overridenVariables.size());
		for (Entry<String, TVariable> ent : new TreeMap<String, TVariable>(overridenVariables).entrySet()) {
			final String name = ent.getKey();
			final TValue value = ent.getValue().getValue();
			Log.error("[MemLocal] " + name + " = " + value);
		}
		Log.error("[MemLocal] Number of local variable(s) : " + localVariables.size());
		for (Entry<String, TVariable> ent : new TreeMap<String, TVariable>(localVariables).entrySet()) {
			final String name = ent.getKey();
			final TValue value = ent.getValue().getValue();
			Log.error("[MemLocal] " + name + " = " + value);
		}
		Log.error("[MemGlobal] End of memory_dump");
	}


	public void putVariable(String varname, TVariable value, TVariableScope scope) throws EaterException {
		if (scope == TVariableScope.GLOBAL) {
			memoryGlobal.putVariable(varname, value, scope);
			return;
		}
		if (scope == TVariableScope.LOCAL || overridenVariables.containsKey(varname)) {
			this.overridenVariables.put(varname, value);
			Log.info("[MemLocal/overrriden] Setting " + varname);
		} else if (memoryGlobal.getVariable(varname) != null) {
			memoryGlobal.putVariable(varname, value, scope);
		} else {
			this.localVariables.put(varname, value);
			Log.info("[MemLocal/local] Setting " + varname);
		}
	}

	public void removeVariable(String varname) {
		if (overridenVariables.containsKey(varname)) {
			this.overridenVariables.remove(varname);
		} else if (memoryGlobal.getVariable(varname) != null) {
			memoryGlobal.removeVariable(varname);
		} else {
			this.localVariables.remove(varname);
		}
	}

	public TVariable getVariable(String varname) {
		TVariable result = overridenVariables.get(varname);
		if (result != null) {
			return result;
		}
		result = memoryGlobal.getVariable(varname);
		if (result != null) {
			return result;
		}
		result = localVariables.get(varname);
		return result;
	}

	public Trie variablesNames3() {
		final Trie result = new Trie();
		for (String name : overridenVariables.keySet()) {
			result.add(name);
		}
		for (String name : memoryGlobal.variablesNames()) {
			result.add(name);
		}
		for (String name : localVariables.keySet()) {
			result.add(name);
		}
		return result;
	}

	public boolean isEmpty() {
		return memoryGlobal.isEmpty() && localVariables.isEmpty() && overridenVariables.isEmpty();
	}

	public Set<String> variablesNames() {
		throw new UnsupportedOperationException();
	}

	public TMemory forkFromGlobal(Map<String, TVariable> input) {
		return new TMemoryLocal(memoryGlobal, input);
	}

	// public final TMemoryGlobal getGlobalForInternalUseOnly() {
	// return memoryGlobal;
	// }

}
