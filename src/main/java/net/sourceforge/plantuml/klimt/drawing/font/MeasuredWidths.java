/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
 */
package net.sourceforge.plantuml.klimt.drawing.font;

/**
 * Widths of characters measured with Java on Windows, for the cases where the
 * embedded font alone cannot tell them (see
 * tools/font-metrics/MeasureLogicalFonts.java), decoded from the compact tables
 * generated in {@link LiberationSansMetrics}.
 *
 * <p>
 * Widths are in 2048ths of the font size, for the 4 styles (regular, bold,
 * italic, bold italic).
 */
final class MeasuredWidths {

	static final int REGULAR = 0;
	static final int BOLD = 1;
	static final int ITALIC = 2;
	static final int BOLD_ITALIC = 3;

	// Marks a style whose width is the one the embedded font gives
	private static final char SAME = '￿';

	// Three chars per range of code points: first, last, and the index in widths
	// of the 4 widths of the range; ranges in increasing order
	private final String ranges;
	// Four chars per entry: the widths of the 4 styles, or SAME
	private final String widths;

	MeasuredWidths(String ranges, String widths) {
		if (ranges.length() % 3 != 0 || widths.length() % 4 != 0)
			throw new IllegalStateException("Corrupted measured widths");
		this.ranges = ranges;
		this.widths = widths;
	}

	/**
	 * @return the measured width of the code point in the style, in 2048ths of
	 *         the font size, or -1 if the embedded font gives the right width
	 */
	int getWidth(int codePoint, int style) {
		if (codePoint < 0 || codePoint > 0xFFFF)
			return -1;
		// Last range starting at or before the code point
		int low = 0;
		int high = ranges.length() / 3 - 1;
		while (low <= high) {
			final int middle = (low + high) >>> 1;
			if (ranges.charAt(3 * middle) <= codePoint)
				low = middle + 1;
			else
				high = middle - 1;
		}
		if (high < 0 || codePoint > ranges.charAt(3 * high + 1))
			return -1;
		final char width = widths.charAt(4 * ranges.charAt(3 * high + 2) + style);
		return width == SAME ? -1 : width;
	}

}
