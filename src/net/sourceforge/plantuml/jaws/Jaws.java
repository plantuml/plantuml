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
package net.sourceforge.plantuml.jaws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.text.StringLocated;

public class Jaws {

	public static final char BLOCK_E1_NEWLINE = '\uE100';
	public static final char BLOCK_E1_REAL_BACKSLASH = '\uE101';
	public static final char BLOCK_E1_START_TEXTBLOCK = '\uE102';
	public static final char BLOCK_E1_END_TEXTBLOCK = '\uE103';

	private final List<StringLocated> output = new ArrayList<StringLocated>();

	public Jaws(List<StringLocated> input) {
		for (int i = 0; i < input.size(); i++) {
			List<StringLocated> splitted = input.get(i).expandsJaws();
			StringLocated line = splitted.get(0);
			if (splitted.size() == 2) {
				line = line.append(splitted.get(1).getString());
				while (true) {
					line = line.append(BLOCK_E1_NEWLINE);
					i++;
					splitted = input.get(i).expandsJaws();
					if (splitted.size() == 2)
						break;
					line = line.append(splitted.get(0).getString());
				}
				line = line.append(splitted.get(0).getString());
				line = line.append(splitted.get(1).getString());
			}
			output.add(line);
		}
	}

	public List<StringLocated> getResultList() {
		return Collections.unmodifiableList(output);
	}

}
