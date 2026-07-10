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

// The folded-corner ("dog-ear") note box, as an AsciiBlock: created by
// InfinitePlan.createNoteBox() and drawn later via asciiDraw(plan), against
// whatever InfinitePlan the caller has positioned at the box's top-left
// corner. Idea copied, not code, from the legacy
// asciiart.UmlCharAreaImpl.drawNoteSimple()/drawNoteSimpleUnicode() (see
// ASCIIVERSE.md, that package is slated for removal): ASCII cuts a "!."/"|_\"
// dog-ear into the top-right corner instead of a plain corner character;
// Unicode draws a double-line box (deliberately different from the
// single-line participant box) plus a shaded block simulating the fold.
public final class ANote implements AsciiBlock {

	private final int width;
	private final int height;
	private final AsciiBlock text;

	// Size is derived from the content alone, once, at construction time —
	// never passed in and never recomputed elsewhere (the exact bug we hit
	// once already: a caller-side copy of this formula that forgot the `+2`,
	// see ASCIIVERSE.md §18). `width = max(5, textWidth + 2)`, `height =
	// max(3, textHeight + 2)`: the `+2` on each axis is the border, the floors
	// are what the ASCII dog-ear (5-column minimum) and the 3-row border (top
	// + content + bottom) need. Notably this doesn't need to know
	// ASCII-vs-Unicode at all: both variants share the same width/height,
	// only the border *characters* differ (asciiDrawAscii()/asciiDrawUnicode()
	// below) — which is also why isUnicode isn't a constructor argument
	// either (see asciiDraw() below).
	public ANote(AsciiBlock text) {
		this.text = text;
		final ADimension2D textDim = text.asciiDimension();
		this.width = Math.max(5, textDim.getWidth() + 2);
		this.height = Math.max(3, textDim.getHeight() + 2);
	}

	@Override
	public ADimension2D asciiDimension() {
		return new ADimension2D(width, height);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		// isUnicode isn't stored on the object: like every other draw decision
		// (column, row — §18), it's read off whatever `plan` this block is
		// asked to draw against, rather than fixed at construction. A caller
		// that only needs the box's size (e.g. CommunicationTileNoteRight/Left's
		// asciiDimension(), §19–§20) can build an ANote with nothing but the
		// text and never touch a plan at all.
		if (plan.isUnicode())
			asciiDrawUnicode(plan);
		else
			asciiDrawAscii(plan);

		// The text sits one column/row inside the border on every side (top
		// border row + left border column), exactly where the old
		// caller-side `plan.move(left + 1, 1).drawString(noteText)` used to
		// place it — now delegated to `text` itself, which may draw several
		// rows (see Display.asciiDraw()) instead of a single flattened line.
		text.asciiDraw(plan.move(1, 1));
	}

	private void asciiDrawAscii(InfinitePlan plan) {
		if (width > 2)
			plan.drawHLine(1, width - 2, 0);
		if (height > 2 && width > 2)
			plan.drawHLine(1, width - 2, height - 1);
		if (height > 2)
			plan.fillVLine('|', 1, height - 2, 0);
		if (height > 2 && width > 1)
			plan.fillVLine('|', 1, height - 2, width - 1);

		plan.drawChar(',');
		if (height > 1)
			plan.move(0, height - 1).drawChar('`');
		if (height > 1 && width > 1)
			plan.move(width - 1, height - 1).drawChar('\'');

		// The dog-ear cut, overwriting the top-right corner: needs at least 4
		// columns (left corner + 2-char fold + at least one border column).
		if (width >= 4) {
			plan.move(width - 3, 0).drawString("!.");
			plan.move(width - 1, 0).drawChar(' ');
			if (height > 1)
				plan.move(width - 3, 1).drawString("|_\\");
		}
	}

	private void asciiDrawUnicode(InfinitePlan plan) {
		if (width > 2)
			plan.fillHLine('═', 1, width - 2, 0);
		if (height > 2 && width > 2)
			plan.fillHLine('═', 1, width - 2, height - 1);
		if (height > 2)
			plan.fillVLine('║', 1, height - 2, 0);
		if (height > 2 && width > 1)
			plan.fillVLine('║', 1, height - 2, width - 1);

		plan.drawChar('╔');
		if (width > 1)
			plan.move(width - 1, 0).drawChar('╗');
		if (height > 1)
			plan.move(0, height - 1).drawChar('╚');
		if (height > 1 && width > 1)
			plan.move(width - 1, height - 1).drawChar('╝');

		// Shaded block simulating the folded corner, one row below the top
		// border, one column left of the right border.
		if (width >= 2 && height >= 2)
			plan.move(width - 2, 1).drawChar('░');
	}

}
