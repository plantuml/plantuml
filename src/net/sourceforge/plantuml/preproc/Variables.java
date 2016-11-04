/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.preproc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Variables {

	private final List<DefineVariable> all = new ArrayList<DefineVariable>();

	public void add(DefineVariable var) {
		this.all.add(var);
	}

	public int countDefaultValue() {
		int result = 0;
		for (DefineVariable var : all) {
			if (var.getDefaultValue() != null) {
				result++;
			}
		}
		return result;
	}

	public Variables removeSomeDefaultValues(int nb) {
		if (nb == 0) {
			return this;
		}
		final Variables result = new Variables();
		for (DefineVariable v : all) {
			if (v.getDefaultValue() != null && nb > 0) {
				result.add(v.removeDefault());
				nb--;
			} else {
				result.add(v);
			}
		}
		if (nb != 0) {
			throw new IllegalArgumentException();
		}
		return result;
	}

	public String applyOn(String line, String fonctionName, String newValue) {
		// System.err.println("KEY="+key);
		// final StringTokenizer st = new StringTokenizer(key, "(),");
		// final String fctName = st.nextToken();
		final StringBuilder regex = new StringBuilder("\\b" + fonctionName + "\\(");

		final List<DefineVariable> variables = all;
		for (int j = 0; j < variables.size(); j++) {
			final DefineVariable variable = variables.get(j);
			final String varName = variable.getName();
			final String var2 = "(##" + varName + "\\b)|(\\b" + varName + "##)|(\\b" + varName + "\\b)";
			if (variable.getDefaultValue() == null) {
				regex.append("(?:(?:\\s*\"([^\"]*)\"\\s*)|(?:\\s*'([^']*)'\\s*)|\\s*" + "((?:\\([^()]*\\)|[^,'\"])*?)"
						+ ")");
				final int i = 1 + 3 * j;
				newValue = newValue.replaceAll(var2, "\\$" + i + "\\$" + (i + 1) + "\\$" + (i + 2));
				regex.append(",");
			} else {
				newValue = newValue.replaceAll(var2, Matcher.quoteReplacement(variable.getDefaultValue()));
			}
		}
		regex.setLength(regex.length() - 1);
		regex.append("\\)");
		// System.err.println("regex=" + regex);
		// System.err.println("newValue=" + newValue);
		line = line.replaceAll(regex.toString(), newValue);
		return line;
	}

}
