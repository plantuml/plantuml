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
package net.sourceforge.plantuml.preproc;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.text.StringLocated;

public class CommentEmoji {

	// This Pattern filters out lines that start with:
	// - Any character in the Unicode block 2600-26FF (Miscellaneous Symbols),
	// - Any character in the Unicode block 2700-27BF (Dingbats),
	// - Any character in supplementary Unicode planes represented by high
	// surrogates
	// (e.g., Emoji and other extended symbols). These are grouped into:
	// - Block 1F300-1F5FF (Miscellaneous Symbols and Pictographs),
	// - Block 1F600-1F64F (Emoticons),
	// - Block 1F680-1F6FF (Transport and Map Symbols),
	// - Block 1F900-1F9FF (Supplemental Symbols and Pictographs).

	public static List<StringLocated> remove(List<StringLocated> input) {
		final List<StringLocated> result = new ArrayList<>();
		for (StringLocated sl : input) {
			final String line = sl.toString();
			if (line.length() > 0) {
				final int numBlock = line.codePointAt(0) / 256;
				if (numBlock == 0x26 || numBlock == 0x27 || numBlock == 0x1f3 || numBlock == 0x1f4 || numBlock == 0x1f5
						|| numBlock == 0x1f6 || numBlock == 0x1f9)
					continue;

			}
			result.add(sl);
		}

		return result;
	}

}
