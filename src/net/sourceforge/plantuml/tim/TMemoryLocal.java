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

public class TMemoryLocal extends ConditionalContexts implements TMemory {

	private final TMemoryGlobal global;
	private final Map<String, TVariable> localVariables = new HashMap<String, TVariable>();
	private Trie variables;

	public TMemoryLocal(TMemoryGlobal global) {
		this.global = global;
	}

	private void initTrie() {
		for (String name : global.variablesNames()) {
			variables.add(name);
		}
	}

	public TVariable getVariable(String varname) {
		final TVariable result = localVariables.get(varname);
		if (result != null) {
			return result;
		}
		return global.getVariable(varname);
	}

	public Trie variablesNames3() {
		if (variables == null) {
			return global.variablesNames3();
		}
		return variables;
	}

	public void put(String varname, TVariable value) {
		this.localVariables.put(varname, value);
		if (this.variables == null) {
			this.variables = new Trie();
			initTrie();
		}
		this.variables.add(varname);
	}

	public boolean isEmpty() {
		return global.isEmpty() && localVariables.isEmpty();
	}

	public Set<String> variablesNames() {
		// final Set<String> result = new HashSet<String>(localVariables.keySet());
		// result.addAll(global.variablesNames());
		// return Collections.unmodifiableSet(result);
		throw new UnsupportedOperationException();
	}

	public TMemory forkFromGlobal() {
		return new TMemoryLocal(global);
	}

}
