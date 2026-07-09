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
package net.sourceforge.plantuml.asciiverse;

// A single "infinite" line of characters, extending both to the right
// (position >= 0, backed by positiveChars) and to the left (position < 0,
// backed by negativeChars). This mirrors InfinitePlan itself, whose move()
// can shift dx/dy negative (e.g. a note box drawn to the left of the
// leftmost participant, ASCIIVERSE.md §16/§18): a single line must be able
// to grow in either direction from an arbitrary starting position, not just
// rightward from 0.
//
// negativeChars stores position -1 at index 0, position -2 at index 1, and
// so on (index = -position - 1): appending to this StringBuilder always
// extends further left, away from zero, exactly the way appending to
// positiveChars always extends further right. The two halves never need to
// "meet in the middle" or resize each other; position 0 itself always lives
// in positiveChars.
public class InfiniteString {

	private final StringBuilder positiveChars = new StringBuilder();
	private final StringBuilder negativeChars = new StringBuilder();

	private void ensureLength(int length) {
		while (positiveChars.length() < length)
			positiveChars.append(' ');

	}

	private void ensureNegativeLength(int length) {
		while (negativeChars.length() < length)
			negativeChars.append(' ');

	}

	public void setCharAt(int position, char ch) {
		if (position >= 0) {
			ensureLength(position + 1);
			positiveChars.setCharAt(position, ch);
		} else {
			final int negIndex = -position - 1;
			ensureNegativeLength(negIndex + 1);
			negativeChars.setCharAt(negIndex, ch);
		}
	}

	public void setStringAt(int position, String s) {
		// Delegates to setCharAt() one character at a time rather than
		// pre-sizing positiveChars/negativeChars in bulk: a string can start
		// negative and cross into positive territory (or vice versa) in a
		// single call, and reusing setCharAt() keeps that split in exactly
		// one place instead of duplicating the position >= 0 test here.
		for (int i = 0; i < s.length(); i++)
			setCharAt(position + i, s.charAt(i));
	}

	public char getCharAt(int position) {
		if (position >= 0) {
			if (position >= positiveChars.length())
				return ' ';

			return positiveChars.charAt(position);
		}

		final int negIndex = -position - 1;
		if (negIndex >= negativeChars.length())
			return ' ';

		return negativeChars.charAt(negIndex);
	}

	// The leftmost position ever written to on this line, as a value <= 0:
	// -negativeChars.length() (0 if setCharAt()/setStringAt() never touched a
	// negative position). Lets a caller that renders many lines together
	// (InfinitePlan.exportTxt()) find the leftmost column across *all* of them
	// before picking a single common startingPosition to pass to getString()
	// — needed because each InfiniteString only knows its own extent, not its
	// neighbors', and every line must be rendered from the same column for the
	// output to stay aligned.
	public int getLeftmostPosition() {
		return -negativeChars.length();
	}

	// Renders the line from startingPosition (inclusive) to the end of
	// positiveChars. startingPosition may be negative: the caller (typically
	// InfinitePlan.exportTxt(), once it tracks the leftmost column used
	// across every line) decides how far left the export should reach, and
	// any position not actually written to (either because this particular
	// line never drew that far left, or because startingPosition reaches
	// further left than any line did) reads back as a space, same as
	// getCharAt() does.
	public String getString(int startingPosition) {
		if (startingPosition >= 0)
			return positiveChars.substring(startingPosition);

		final int negLength = -startingPosition;
		final StringBuilder result = new StringBuilder(negLength + positiveChars.length());
		// Walk from the leftmost requested position (highest negIndex) up to
		// -1 (negIndex 0), i.e. left to right, matching reading order.
		for (int negIndex = negLength - 1; negIndex >= 0; negIndex--)
			result.append(negIndex < negativeChars.length() ? negativeChars.charAt(negIndex) : ' ');

		result.append(positiveChars);
		return result.toString();
	}
}
