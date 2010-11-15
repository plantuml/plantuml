/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 3829 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnderlineTrickEps {

	private final StringBuilder result = new StringBuilder();

	public UnderlineTrickEps(String eps) {
		final List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(eps, "\n");
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			list.add(s);
			if (s.contains("(_!)")) {
				final String other = list.get(list.size() - 4);
				final String modified = changeToUnderscore(other);
				if (modified != null) {
					list.add(list.size() - 4, modified);
				}
			}
		}

		for (String s : list) {
			result.append(s);
			result.append('\n');
		}
	}

	static String changeToUnderscore(String s) {
		final Pattern p = Pattern.compile("\\((.*)\\)");
		final Matcher m = p.matcher(s);

		final StringBuffer result = new StringBuffer();

		if (m.find() == false) {
			return null;
		}

		final StringBuilder underscores = new StringBuilder("(");
		for (int i = 0; i < m.group(1).length(); i++) {
			underscores.append('_');
		}
		underscores.append(')');

		m.appendReplacement(result, underscores.toString());
		m.appendTail(result);
		return result.toString();
	}

	public String getString() {
		return result.toString();
	}

}
