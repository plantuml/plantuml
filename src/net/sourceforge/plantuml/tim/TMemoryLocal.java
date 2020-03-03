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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.tim.expression.TValue;

public class TMemoryLocal extends ExecutionContexts implements TMemory {

	private final TMemoryGlobal memoryGlobal;
	private TrieImpl overridenVariables00;
	private final Map<String, TValue> overridenVariables01 = new HashMap<String, TValue>();
	private final TrieImpl localVariables00 = new TrieImpl();
	private final Map<String, TValue> localVariables01 = new HashMap<String, TValue>();

	public TMemoryLocal(TMemoryGlobal global, Map<String, TValue> input) {
		this.memoryGlobal = global;
		this.overridenVariables01.putAll(input);
	}

	public void dumpDebug(String message) {
		Log.error("[MemLocal] Start of memory_dump " + message);
		memoryGlobal.dumpMemoryInternal();
		final TreeMap<String, TValue> over = new TreeMap<String, TValue>(overridenVariables01);
		Log.error("[MemLocal] Number of overriden variable(s) : " + over.size());
		for (Entry<String, TValue> ent : over.entrySet()) {
			final String name = ent.getKey();
			final TValue value = ent.getValue();
			Log.error("[MemLocal] " + name + " = " + value);
		}
		final TreeMap<String, TValue> local = new TreeMap<String, TValue>(localVariables01);
		Log.error("[MemLocal] Number of local variable(s) : " + local.size());
		for (Entry<String, TValue> ent : local.entrySet()) {
			final String name = ent.getKey();
			final TValue value = ent.getValue();
			Log.error("[MemLocal] " + name + " = " + value);
		}
		Log.error("[MemGlobal] End of memory_dump");
	}

	public void putVariable(String varname, TValue value, TVariableScope scope) throws EaterException {
		if (scope == TVariableScope.GLOBAL) {
			memoryGlobal.putVariable(varname, value, scope);
			return;
		}
		if (scope == TVariableScope.LOCAL || overridenVariables01.containsKey(varname)) {
			this.overridenVariables01.put(varname, value);
			if (this.overridenVariables00 != null) {
				this.overridenVariables00.add(varname);
			}
			Log.info("[MemLocal/overrriden] Setting " + varname);
		} else if (memoryGlobal.getVariable(varname) != null) {
			memoryGlobal.putVariable(varname, value, scope);
		} else {
			this.localVariables01.put(varname, value);
			this.localVariables00.add(varname);
			Log.info("[MemLocal/local] Setting " + varname);
		}
	}

	public void removeVariable(String varname) {
		if (overridenVariables01.containsKey(varname)) {
			this.overridenVariables01.remove(varname);
			if (this.overridenVariables00 != null) {
				this.overridenVariables00.remove(varname);
			}
		} else if (memoryGlobal.getVariable(varname) != null) {
			memoryGlobal.removeVariable(varname);
		} else {
			this.localVariables01.remove(varname);
			this.localVariables00.remove(varname);
		}
	}

	public TValue getVariable(String varname) {
		TValue result = overridenVariables01.get(varname);
		if (result != null) {
			return result;
		}
		result = memoryGlobal.getVariable(varname);
		if (result != null) {
			return result;
		}
		result = localVariables01.get(varname);
		return result;
	}

	public Trie variablesNames3() {
		if (overridenVariables00 == null) {
			overridenVariables00 = new TrieImpl();
			for (String name : overridenVariables01.keySet()) {
				overridenVariables00.add(name);
			}
		}
		return new Trie() {
			public void add(String s) {
				throw new UnsupportedOperationException();
			}

			public String getLonguestMatchStartingIn(String s) {
				final String s1 = memoryGlobal.variablesNames3().getLonguestMatchStartingIn(s);
				final String s2 = overridenVariables00.getLonguestMatchStartingIn(s);
				final String s3 = localVariables00.getLonguestMatchStartingIn(s);

				if (s1.length() >= s2.length() && s1.length() >= s3.length()) {
					return s1;
				}
				if (s2.length() >= s3.length() && s2.length() >= s1.length()) {
					return s2;
				}
				return s3;
			}
		};
		// final Trie result = new TrieImpl();
		// for (String name : overridenVariables.keySet()) {
		// result.add(name);
		// }
		// for (String name : memoryGlobal.variablesNames()) {
		// result.add(name);
		// }
		// for (String name : localVariables.keySet()) {
		// result.add(name);
		// }
		// return result;
	}

	public boolean isEmpty() {
		return memoryGlobal.isEmpty() && localVariables01.isEmpty() && overridenVariables01.isEmpty();
	}

	public Set<String> variablesNames() {
		throw new UnsupportedOperationException();
	}

	public TMemory forkFromGlobal(Map<String, TValue> input) {
		return new TMemoryLocal(memoryGlobal, input);
	}

	// public final TMemoryGlobal getGlobalForInternalUseOnly() {
	// return memoryGlobal;
	// }

}
