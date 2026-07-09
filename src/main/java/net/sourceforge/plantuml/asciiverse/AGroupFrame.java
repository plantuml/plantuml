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

// The frame box that encloses a group (frame, alt, opt, loop, partition, ...)
// as an AsciiBlock: the ASCII counterpart of the pixel GROUPING_HEADER_TEOZ
// Component that GroupingTile.drawU() obtains from the skin and delegates to
// (see ASCIIVERSE.md §26). This is the second cut of this class (§28):
// the first cut reused InfinitePlan.drawBox() for a plain, uniform
// rectangle; this one deliberately draws three visually distinct pieces —
// a dashed top border, '!' sides, a tilde bottom border — plus a small
// pentagon-style title tab in the top-left, mirroring the actual pixel
// GROUPING_HEADER_TEOZ shape (a small cut-corner tab) more closely, per a
// hand-drawn ATXT mockup Arnaud provided.
//
// Like §26 already established, this is a *container* whose size is
// dictated from the outside (the span of the group's children and its
// stacked body height) — so it takes an explicit ADimension2D rather than
// computing one, the same distinction as ANote's self-sized text vs. a
// pixel Component drawn into an Area handed to it.
public final class AGroupFrame implements AsciiBlock {

	private final ADimension2D dimension;
	private final String title;

	public AGroupFrame(ADimension2D dimension, String title) {
		this.dimension = dimension;
		this.title = title == null ? "" : title;
	}

	@Override
	public ADimension2D asciiDimension() {
		return dimension;
	}

	// How many rows, counted from this frame's own top, are consumed by the
	// header — the top border alone (1) when there's no title or it wouldn't
	// fit, or the top border plus the title tab's two rows (3: text row, then
	// the diagonal-fold row) otherwise. GroupingTile needs this to know where
	// its own child-stacking loop should start; rather than duplicating the
	// "does the tab fit" guard in GroupingTile, it builds a throwaway
	// AGroupFrame and reads this back — the same "one place owns the size
	// rule, callers read it back" discipline as ANote (§18/§20).
	public int getBodyRowOffset() {
		return hasTab() ? 3 : 1;
	}

	private boolean hasTab() {
		return title.isEmpty() == false && dimension.getWidth() > tabWidth() + 2;
	}

	// " " + title + " ": one padding column each side, the same tab-text
	// convention the first cut of this class used for its title stamp (§26).
	private int tabWidth() {
		return title.length() + 2;
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int width = dimension.getWidth();
		final int height = dimension.getHeight();

		// Top border: a plain dashed line with corners — an ordinary box edge,
		// so its corners stay Unicode-aware (getTopLeftChar()/getTopRightChar(),
		// opened up for this in §28) and its dash run reuses drawHLine(), which
		// already picks '-' vs '─' internally (§12). Not drawn via drawBox():
		// that would also draw a matching bottom border and '|' sides, both
		// wrong here (see below).
		if (width >= 1) {
			plan.move(0, 0).drawChar(plan.getTopLeftChar());
			if (width > 2)
				plan.drawHLine(1, width - 2, 0);
			if (width > 1)
				plan.move(width - 1, 0).drawChar(plan.getTopRightChar());
		}

		// Sides: '!', deliberately not '|'. A frame border must stay visually
		// distinct from a participant's lifeline running through it, which a
		// shared '|' cannot do — this is exactly the ambiguity the first cut
		// of this class had (its drawBox()-based '|' sides collided with
		// crossing lifelines, e.g. the pre-§28 hello4 reference output reads
		// "-|p1", the frame's own corner and a lifeline character
		// indistinguishable from one another). Still ASCII-only for now (no
		// Unicode variant) — deferred along with the rest of Unicode support
		// for this shape, see the "Still ATXT-only" note below.
		for (int y = 1; y <= height - 2; y++) {
			plan.move(0, y).drawChar('!');
			if (width > 1)
				plan.move(width - 1, y).drawChar('!');
		}

		// Bottom border: tildes across the full width, no distinct corner
		// characters — a deliberately different shape from the top border, so
		// the frame's footer reads as visually distinct from its header at a
		// glance. A confirmed stylistic choice (per the mockup), not a
		// plain box edge with a character swapped in.
		if (height >= 2)
			for (int x = 0; x < width; x++)
				plan.move(x, height - 1).drawChar('~');

		// The title's small pentagon-style tab, attached to the top-left
		// inside the frame — one column/row padding from the border, the same
		// convention ANote's text uses (§18) — text row, then a diagonal fold
		// row (underscores + a trailing '/'). This is the ASCII transposition
		// of the small cut-corner tab the pixel GROUPING_HEADER_TEOZ component
		// actually draws, rather than the plain "stamp the title on the top
		// border" treatment the first cut of this class used (§26). Skipped
		// (falls back to the top border alone, getBodyRowOffset() == 1) when
		// there's no title or it wouldn't fit — the same "skip if it doesn't
		// fit" policy the title stamp it replaces already had.
		if (hasTab()) {
			final int tabWidth = tabWidth();
			plan.move(1, 1).drawString(" " + title + " ");
			for (int x = 0; x < tabWidth - 1; x++)
				plan.move(1 + x, 2).drawChar('_');
			plan.move(1 + tabWidth - 1, 2).drawChar('/');
		}
	}

	// Still ATXT-only: the sides ('!'), the bottom border ('~'), and the tab's
	// fold ('_', '/') are hardcoded ASCII characters, not chosen through
	// InfinitePlan's isUnicode()-aware getters the way the top border's dashes
	// and corners are. This mirrors the top-level decision (Arnaud, this
	// session) to shape the ATXT rendering first and revisit UTXT once it's
	// settled — unlike every other shape in this file (ANote, the box,
	// AElseSeparator's dotted line), which already picked a Unicode variant
	// alongside the ASCII one. A UTXT pass for this shape is the natural next
	// step once the ATXT version is confirmed against real output.

}
