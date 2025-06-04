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
 *
 */
package net.sourceforge.plantuml.style.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CssVariables {

	private final Map<String, String> variables = new HashMap<>();

	private static final Pattern learnPattern = Pattern.compile("^--([_\\w][-_\\w]+)[ :]+(.*?);?");
	private static final Pattern retrieve = Pattern.compile("var\\(-*([_\\w][-_\\w]+)\\)");

	public void learn(String s) {
		final Matcher m = learnPattern.matcher(s);
		if (m.matches())
			variables.put(m.group(1), m.group(2));
	}

	public void learn(String var, String value) {
		if (var.startsWith("--"))
			var = var.substring(2);
		variables.put(var, value);
	}

	public String value(String v) {
		if (v.startsWith("var(")) {
			final Matcher m = retrieve.matcher(v);
			if (m.matches()) {
				final String varname = m.group(1);
				final String result = variables.get(varname);
				if (result != null)
					return result;
			}
		}
		return v;
	}

}