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

// A generic AsciiBlock decorator: wraps another AsciiBlock and pads it with a
// fixed number of blank columns on the left and/or right. The inner block
// never knows it's been padded — asciiDraw() just moves it right by
// marginLeft, and asciiDimension() grows to include both margins — so any
// AsciiBlock (a Display, an ANote, another decorator, ...) can gain a margin
// without changing its own drawing logic. Same composition principle as
// every other AsciiBlock in this package: the caller
// asks the wrapper for its size, never recomputes the padding by hand.
public final class AsciiBlockMarginLR implements AsciiBlock {

	private final AsciiBlock inner;
	private final int marginLeft;
	private final int marginRight;

	AsciiBlockMarginLR(AsciiBlock inner, int marginLeft, int marginRight) {
		if (marginLeft < 0 || marginRight < 0)
			throw new IllegalArgumentException();

		this.inner = inner;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
	}

	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D dim = inner.asciiDimension();
		if (dim.getWidth() == 0)
			// An empty inner block (e.g. an unlabeled message) stays empty:
			// there's no text to pad around, so no margin is reserved either.
			return dim;

		return dim.delta(marginLeft + marginRight, 0);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		inner.asciiDraw(plan.move(marginLeft, 0));
	}

}
