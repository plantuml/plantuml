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
package net.sourceforge.plantuml.anim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.script.ScriptException;

public class AnimationDecoder {

	private final List<String> result = new ArrayList<String>();

	public AnimationDecoder(Iterable<CharSequence> data) throws ScriptException {
		
		for (final Iterator<CharSequence> it = data.iterator(); it.hasNext();) {
			String line = it.next().toString();
			if (line.matches("^\\s*\\[script\\]\\s*$")) {
				final StringBuilder scriptText = new StringBuilder();
				while (true) {
					line = it.next().toString();
					if (line.matches("^\\s*\\[/script\\]\\s*$")) {
						final AnimationScript script = new AnimationScript();
						final String out = script.eval(scriptText.toString());
						for (final StringTokenizer st = new StringTokenizer(out, "\n"); st.hasMoreTokens();) {
							result.add(st.nextToken());
						}
						break;
					} else {
						scriptText.append(line);
						scriptText.append("\n");
					}
				}
			} else {
				result.add(line);
			}
		}
	}

	public List<String> decode() {
		return Collections.unmodifiableList(result);
	}

}
