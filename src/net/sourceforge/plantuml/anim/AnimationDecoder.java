/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 6170 $
 *
 */
package net.sourceforge.plantuml.anim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.script.ScriptException;

public class AnimationDecoder {

	private final List<String> result = new ArrayList<String>();

	public AnimationDecoder(List<String> data) throws ScriptException {
		for (int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			if (line.matches("^\\s*\\[script\\]\\s*$")) {
				final StringBuilder scriptText = new StringBuilder();
				while (true) {
					i++;
					line = data.get(i);
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
