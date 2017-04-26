/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import net.sourceforge.plantuml.BackSlash;

public class Define {

	private final DefineSignature signature;
	private final String definition;
	private final String definitionQuoted;

	public Define(String key, List<String> lines) {
		this.signature = new DefineSignature(key);
		if (lines == null) {
			this.definition = null;
			this.definitionQuoted = null;
		} else {
			final StringBuilder sb = new StringBuilder();
			for (final Iterator<String> it = lines.iterator(); it.hasNext();) {
				sb.append(it.next());
				if (it.hasNext()) {
					sb.append('\n');
				}
			}
			this.definition = sb.toString();
			this.definitionQuoted = Matcher.quoteReplacement(definition);
		}
	}

	@Override
	public String toString() {
		return signature.toString();
	}

	public String apply(String line) {
		if (definition == null) {
			return line;
		}
		if (signature.isMethod()) {
			for (Variables vars : signature.getVariationVariables()) {
				line = vars.applyOn(line, signature.getFonctionName(), definitionQuoted);
			}
		} else {
			final String regex = "\\b" + signature.getKey() + "\\b";
			line = BackSlash.translateBackSlashes(line);
			line = line.replaceAll(regex, definitionQuoted);
			line = BackSlash.untranslateBackSlashes(line);
		}
		return line;
	}

}
