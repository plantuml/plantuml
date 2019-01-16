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
 *
 */
package net.sourceforge.plantuml.preproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class DefineSignature {

	private final String key;
	private final String fonctionName;
	private final List<Variables> variables = new ArrayList<Variables>();
	private final boolean isMethod;

	public DefineSignature(String key, String definitionQuoted) {
		this.key = key;
		this.isMethod = key.contains("(");

		final StringTokenizer st = new StringTokenizer(key, "(),");
		this.fonctionName = st.nextToken().trim();
		final Variables master = new Variables(fonctionName, definitionQuoted);

		while (st.hasMoreTokens()) {
			final String var1 = st.nextToken().trim();
			master.add(new DefineVariable(var1));
		}

		final int count = master.countDefaultValue();
		for (int i = 0; i <= count; i++) {
			variables.add(master.removeSomeDefaultValues(i));
		}
	}

	@Override
	public String toString() {
		return key + "/" + fonctionName;
	}

	public boolean isMethod() {
		return isMethod;
	}

	public String getKey() {
		return key;
	}

	public List<Variables> getVariationVariables() {
		return Collections.unmodifiableList(variables);
	}

	public final String getFonctionName() {
		return fonctionName;
	}

}
