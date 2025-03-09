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
	
	public static final boolean TRACE = false;

	public static final char BLOCK_E1_NEWLINE = '\uE100';
	public static final char BLOCK_E1_NEWLINE_LEFT_ALIGN = '\uE101';
	public static final char BLOCK_E1_NEWLINE_RIGHT_ALIGN = '\uE102';
	public static final char BLOCK_E1_BREAKLINE = '\uE103';

	public static final char BLOCK_E1_REAL_BACKSLASH = '\uE110';
	public static final char BLOCK_E1_REAL_TABULATION = '\uE111';

	public static final char BLOCK_E1_INVISIBLE_QUOTE = '\uE121';
	public static final char BLOCK_E1_START_TEXTBLOCK = '\uE122';
	public static final char BLOCK_E1_END_TEXTBLOCK = '\uE123';

	public static List<StringLocated> expandsJawsForPreprocessor(List<StringLocated> input) {
		final List<StringLocated> output = new ArrayList<StringLocated>();
		for (int i = 0; i < input.size(); i++) {
			List<StringLocated> splitted = input.get(i).expandsJawsForPreprocessor();
			StringLocated line = splitted.get(0);
			if (splitted.size() == 2) {
				final int headerLength = line.length() + 3;
				line = line.append(BLOCK_E1_INVISIBLE_QUOTE);
				final MultilinesBloc bloc = new MultilinesBloc(headerLength, splitted.get(1).getString());
				while (true) {
					i++;
					splitted = input.get(i).expandsJawsForPreprocessor();
					bloc.add(splitted.get(0).getString());
					if (splitted.size() == 2)
						break;
				}
				line = line.append(bloc.getMerged());
				line = line.append(BLOCK_E1_INVISIBLE_QUOTE);
				line = line.append(splitted.get(1).getString());
			}
			output.add(line);
		}
		return Collections.unmodifiableList(output);
	}

	public static List<StringLocated> expands0(List<StringLocated> input) {
		final List<StringLocated> output = new ArrayList<StringLocated>();
		for (StringLocated sl : input) 
			output.addAll(sl.expandsBreaklineButEmbedded());
		
		return output;
	}

}
