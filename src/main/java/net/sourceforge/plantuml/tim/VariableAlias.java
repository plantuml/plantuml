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

import java.util.Map;

/**
 * A variable can be written with or without its leading dollar: "$toto" and
 * "toto" are the same variable. The variable is stored under the spelling used
 * when it was created, and this class finds it again whatever the spelling used
 * to read, write or remove it.
 * <p>
 * This only concerns the expressions and the functions (<code>!if $toto == 1</code>,
 * <code>%get_variable_value("toto")</code>, ...). The replacement in the text is
 * unchanged: it only knows the spelling used to create the variable, so that a
 * sprite like <code>&lt;$toto&gt;</code> is not replaced just because a variable
 * "toto" exists (for instance a <code>!define toto</code>).
 */
final class VariableAlias {

	private VariableAlias() {
	}

	/**
	 * The other spelling of a variable name: "toto" gives "$toto" and "$toto" gives
	 * "toto". Returns <code>null</code> when there is none.
	 */
	static String alias(String varname) {
		if (varname.startsWith("$"))
			return varname.length() > 1 ? varname.substring(1) : null;

		return varname.length() > 0 ? "$" + varname : null;
	}

	/**
	 * The key under which the variable is stored in this map: the given name if it
	 * is there, else its other spelling if this one is there. If the variable does
	 * not exist, this is the given name.
	 */
	static String resolveKey(Map<String, ?> map, String varname) {
		if (map.containsKey(varname))
			return varname;

		final String alias = alias(varname);
		if (alias != null && map.containsKey(alias))
			return alias;

		return varname;
	}

	static <T> T get(Map<String, T> map, String varname) {
		final T result = map.get(varname);
		if (result != null)
			return result;

		final String alias = alias(varname);
		return alias == null ? null : map.get(alias);
	}

}
