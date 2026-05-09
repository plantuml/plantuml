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

import java.util.List;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocation;

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

	public static void mutateExpands1(List<StringLocated> tmp) {
		mutateExpandsBreakline(tmp);
		if (JawsFlags.PARSE_NEW_MULTILINE_TRIPLE_MARKS)
			mergeTripleMarkBlocks(tmp);
	}

	private static void mutateExpandsBreakline(List<StringLocated> tmp) {
		for (int i = 0; i < tmp.size(); i++) {
			final StringLocated sl = tmp.get(i);
			final String s = sl.getString();

			// Fast path: no breakline -> nothing to do for this entry.
			if (s.indexOf(BLOCK_E1_BREAKLINE) == -1)
				continue;

			// Split the line at every BLOCK_E1_BREAKLINE that is not inside a {{...}}
			// embedded block. Write the first piece in place at index i, then insert the
			// remaining pieces right after.
			final LineLocation location = sl.getLocation();
			final String preprocessorError = sl.getPreprocessorError();
			final int len = s.length();
			final StringBuilder pending = new StringBuilder(len);
			int level = 0;
			boolean firstWritten = false;
			int inserted = 0;
			for (int j = 0; j < len; j++) {
				final char ch = s.charAt(j);

				if (ch == '{' && j + 1 < len && s.charAt(j + 1) == '{')
					level++;
				else if (ch == '}' && j + 1 < len && s.charAt(j + 1) == '}')
					level--;

				if (level > 0) {
					pending.append(ch);
				} else if (ch == BLOCK_E1_BREAKLINE) {
					final StringLocated piece = new StringLocated(pending.toString(), location, preprocessorError);
					if (firstWritten == false) {
						tmp.set(i, piece);
						firstWritten = true;
					} else {
						tmp.add(i + 1 + inserted, piece);
						inserted++;
					}
					pending.setLength(0);
				} else {
					pending.append(ch);
				}
			}

			// Tail piece (always emitted, even when empty, to match the legacy behaviour).
			final StringLocated tail = new StringLocated(pending.toString(), location, preprocessorError);
			if (firstWritten == false) {
				// No actual split happened (the BLOCK_E1_BREAKLINE chars were all inside
				// {{...}} blocks). Replace in place if the content is materially different.
				tmp.set(i, tail);
			} else {
				tmp.add(i + 1 + inserted, tail);
				inserted++;
			}

			i += inserted;
		}
	}

	private static void mergeTripleMarkBlocks(List<StringLocated> tmp) {
		// in-place expansion of triple-mark
		// separators ('''/!!!/""").
		// Common case (no triple-mark on a line): the entry is left untouched.
		// When a triple-mark opens on line i, we consume input lines until the closing
		// triple-mark and merge them all into a single output line at write index w.
		final int n = tmp.size();
		int w = 0;
		for (int i = 0; i < n; i++) {
			final StringLocated current = tmp.get(i);
			final int x = current.findMultilineTripleSeparator();
			if (x == -1) {
				if (w != i)
					tmp.set(w, current);
				w++;
				continue;
			}

			// Opening triple-mark found. Build the merged line in a single StringBuilder
			// to avoid the cascade of StringLocated.append() that allocate at each step.
			final StringLocated[] head = current.splitAtTripleSeparator(x);
			final StringLocated openLine = head[0];
			final String openTail = head[1].getString();

			final int headerLength = openLine.length() + 3;
			final MultilinesBloc block = new MultilinesBloc(headerLength, openTail);

			String closeTail = "";
			while (true) {
				i++;
				final StringLocated mid = tmp.get(i);
				final int xMid = mid.findMultilineTripleSeparator();
				if (xMid == -1) {
					block.add(mid.jawsHideBackslash().getString());
				} else {
					final StringLocated[] tail = mid.splitAtTripleSeparator(xMid);
					block.add(tail[0].getString());
					closeTail = tail[1].getString();
					break;
				}
			}

			final StringBuilder sb = new StringBuilder(
					openLine.length() + 2 + block.getMerged().length() + closeTail.length());
			sb.append(openLine.getString());
			sb.append(BLOCK_E1_INVISIBLE_QUOTE);
			sb.append(block.getMerged());
			sb.append(BLOCK_E1_INVISIBLE_QUOTE);
			sb.append(closeTail);

			tmp.set(w++, new StringLocated(sb.toString(), openLine.getLocation(), openLine.getPreprocessorError()));
		}

		// Drop the now-unused tail in one shot.
		if (w < n)
			tmp.subList(w, n).clear();
	}

}
