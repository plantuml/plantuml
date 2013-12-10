/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.preproc;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import net.sourceforge.plantuml.Pragma;

public class Defines extends Pragma {

	public String applyDefines(String line) {
		for (Map.Entry<String, String> ent : this.entrySet()) {
			final String key = ent.getKey();
			if (ent.getValue() == null) {
				continue;
			}
			final String value = Matcher.quoteReplacement(ent.getValue());
			if (key.contains("(")) {
				final StringTokenizer st = new StringTokenizer(key, "(),");
				final String fctName = st.nextToken();
				final String var1 = st.nextToken();
				final String regex = "\\b" + fctName + "\\(([^)]*)\\)";
				// final String newValue = value.replaceAll("\\b" + Matcher.quoteReplacement(var1) + "\\b", "\\$1");
				final String newValue = value.replaceAll("\\b" + var1 + "\\b", "\\$1");
				line = line.replaceAll(regex, newValue);
			} else {
				final String regex = "\\b" + key + "\\b";
				line = line.replaceAll(regex, value);
			}
		}
		return line;
	}

}
