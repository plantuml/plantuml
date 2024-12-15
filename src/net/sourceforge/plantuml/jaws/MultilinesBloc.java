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
import java.util.List;

public class MultilinesBloc {

	private final int initialHeaderLength;
	private final List<String> lines = new ArrayList<>();

	public MultilinesBloc(int headerLength, String first) {
		this.initialHeaderLength = headerLength;
		this.lines.add(first);
	}

	public void add(String part) {
		lines.add(part);
	}

	public String getMerged() {
		int minHeader = countSpaceAtStart(lines.get(1));
		for (int i = 2; i < lines.size(); i++)
			minHeader = Math.min(minHeader, countSpaceAtStart(lines.get(i)));

		final StringBuilder sb = new StringBuilder(lines.get(0));
		for (int i = 1; i < lines.size(); i++) {
			sb.append(Jaws.BLOCK_E1_NEWLINE);
			sb.append(lines.get(i).substring(minHeader));
		}

		return sb.toString();
	}

	private int countSpaceAtStart(String part) {
		int result = 0;
		while (result < part.length() && part.charAt(result) == ' ')
			result++;
		return result;
	}

}
