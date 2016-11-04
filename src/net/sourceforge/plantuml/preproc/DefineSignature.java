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
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class DefineSignature {

	private final String key;
	private final String fctName;
	private final Variables vars = new Variables();

	public DefineSignature(String key) {
		this.key = key;

		final StringTokenizer st = new StringTokenizer(key, "(),");
		this.fctName = st.nextToken().trim();

		while (st.hasMoreTokens()) {
			final String var1 = st.nextToken().trim();
			this.vars.add(new DefineVariable(var1));
		}
	}

	public boolean isMethod() {
		return key.contains("(");
	}

	public String getKey() {
		return key;
	}

	public List<Variables> getVariationVariables() {
		final List<Variables> result = new ArrayList<Variables>();
		final int count = vars.countDefaultValue();
		for (int i = 0; i <= count; i++) {
			result.add(vars.removeSomeDefaultValues(i));
		}
		return Collections.unmodifiableList(result);
	}

	public String getFonctionName() {
		return fctName;
	}

}
