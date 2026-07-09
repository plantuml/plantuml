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
// (see the design notes below). Third cut of this class: ATXT and UTXT are
// now genuinely different shapes (not a character-substitution of the same
// layout, the way ANote's two variants are) — ATXT keeps the pentagon-style
// cut-corner tab; UTXT draws a small single-line rectangular
// tab sharing the frame's own top/left double-line border, with its right
// edge continuing as a single-line divider for the frame's full height. Split
// into asciiDrawAscii()/asciiDrawUnicode(), the same dispatch shape ANote
// already uses, rather than one method with isUnicode() branches
// threaded through shared geometry — the two shapes don't actually share
// enough geometry to make that worthwhile.
//
// This is a *container* whose size is
// dictated from the outside (the span of the group's children and its
// stacked body height) — so it takes an explicit ADimension2D rather than
// computing one, the same distinction as ANote's self-sized text vs. a
// pixel Component drawn into an Area handed to it.
public final class AGroupFrame implements AsciiBlock {

	private final ADimension2D dimension;
	private final String title;
	// True for a plain "frame"/"alt"/"loop"/... group: the pentagon-style
	// cut-corner tab. False for a partition: a partition's pixel rendering (PartitionTile.getComponent())
	// never draws a tab at all — it stamps the title centered directly on the
	// frame's own top border, the plain box shape this class's very first cut used before the tab was introduced. Rather than duplicating
	// AGroupFrame (border, sides, corners are identical either way) this one
	// class now supports both shapes, switched on this flag, exactly like
	// asciiDraw() already dispatches on isUnicode() for ATXT vs UTXT.
	private final boolean useTab;

	public AGroupFrame(ADimension2D dimension, String title) {
		this(dimension, title, true);
	}

	public AGroupFrame(ADimension2D dimension, String title, boolean useTab) {
		this.dimension = dimension;
		this.title = title == null ? "" : title;
		this.useTab = useTab;
	}

	@Override
	public ADimension2D asciiDimension() {
		return dimension;
	}

	// How many rows, counted from this frame's own top, are consumed by the
	// header — the top border alone (1) when there's no title or it wouldn't
	// fit, or the top border plus the title tab's two rows (3: text/tab row,
	// then the tab's own closing row) otherwise. Same row count in both ATXT
	// and UTXT — only the characters differ, not the geometry — so this stays
	// a single, format-agnostic method. GroupingTile needs it to know where
	// its own child-stacking loop should start; rather than duplicating the
	// "does the tab fit" guard in GroupingTile, it builds a throwaway
	// AGroupFrame and reads this back — the same "one place owns the size
	// rule, callers read it back" discipline as ANote.
	public int getBodyRowOffset() {
		return hasTab() ? 3 : 1;
	}

	private boolean hasTab() {
		return useTab && title.isEmpty() == false && dimension.getWidth() > tabWidth() + 2;
	}

	// The plain-frame counterpart of hasTab(): whether the title is stamped
	// centered on the top border itself (useTab == false, a partition) rather
	// than in a tab. Same "skip if it doesn't fit" policy as hasTab() — a
	// title longer than the frame simply doesn't get drawn, rather than
	// truncated or overflowing past the corners.
	private boolean hasTitleOnBorder() {
		return useTab == false && title.isEmpty() == false
				&& dimension.getWidth() >= title.length() + 2 + 2;
	}

	// Width of the tab's own text block: " " + title + " ", one padding
	// column each side, the same tab-text convention the first cut of this
	// class used for its title stamp. Both asciiDrawAscii() and
	// asciiDrawUnicode() start their own closing character(s) right after
	// this block, at column tabWidth() + 1 (1-indexed from the tab's own
	// left, i.e. column 1 relative to this frame).
	private int tabWidth() {
		return title.length() + 2;
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		if (plan.isUnicode())
			asciiDrawUnicode(plan);
		else
			asciiDrawAscii(plan);
	}

	// ATXT: dashed top border, '!' sides, tildes across the bottom, and a
	// pentagon-style cut-corner tab — see those sections for the
	// full rationale of each choice; unchanged by this section's UTXT work.
	private void asciiDrawAscii(InfinitePlan plan) {
		final int width = dimension.getWidth();
		final int height = dimension.getHeight();

		// Top border: a plain dashed line with corners — an ordinary box edge,
		// so its corners stay Unicode-aware (getTopLeftChar()/getTopRightChar(),
		// opened up for this) and its dash run reuses drawHLine(), which
		// already picks '-' vs '─' internally — moot here since this
		// method is only ever reached in ASCII mode, but kept for symmetry with
		// asciiDrawUnicode() reading the same plan. Not drawn via drawBox():
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
		// crossing lifelines, e.g. the hello4 reference output reads
		// "-|p1", the frame's own corner and a lifeline character
		// indistinguishable from one another).
		for (int y = 1; y <= height - 2; y++) {
			plan.move(0, y).drawChar('!');
			if (width > 1)
				plan.move(width - 1, y).drawChar('!');
		}

		// Bottom border: tildes across the full width for a tab-style frame, no
		// distinct corner characters — a deliberately different shape from the
		// top border, so the frame's footer reads as visually distinct from its
		// header at a glance. A confirmed stylistic choice (per the mockup), not
		// a plain box edge with a character swapped in. UTXT does not repeat this choice: its bottom border is a plain double line, matching
		// its top. A plain (useTab == false) frame — a partition — has no such
		// header/footer asymmetry to signal in the first place (its title sits
		// on the top border itself, not in a tab), so its bottom mirrors its top
		// exactly: same corners, same dash run, via drawBox()'s own convention.
		if (height >= 2) {
			if (useTab) {
				for (int x = 0; x < width; x++)
					plan.move(x, height - 1).drawChar('~');
			} else {
				plan.move(0, height - 1).drawChar(plan.getBottomLeftChar());
				if (width > 2)
					plan.drawHLine(1, width - 2, height - 1);
				if (width > 1)
					plan.move(width - 1, height - 1).drawChar(plan.getBottomRightChar());
			}
		}

		// The title's small pentagon-style tab, attached to the top-left
		// inside the frame — one column/row padding from the border, the same
		// convention ANote's text uses — text row, then a diagonal fold
		// row (underscores + a trailing '/'). This is the ASCII transposition
		// of the small cut-corner tab the pixel GROUPING_HEADER_TEOZ component
		// actually draws, rather than the plain "stamp the title on the top
		// border" treatment the first cut of this class used. Skipped
		// (falls back to the top border alone, getBodyRowOffset() == 1) when
		// there's no title or it wouldn't fit — the same "skip if it doesn't
		// fit" policy the title stamp it replaces already had.
		//
		// The text row ends in " /" rather than a plain padding space: the
		// tab needs its own right edge closed off, the same way its left edge is
		// implicitly closed by the frame's own '!' at column 0. That trailing '/'
		// lands one column to the right of, and one row above, the fold row's own
		// '/' directly beneath it — so the two slashes form a genuine 45-degree
		// diagonal cut running across both rows, rather than a straight edge on
		// one row with a separate cut below it: the standard UML frame
		// cut-corner pentagon look, just traced over two character rows instead
		// of one continuous line.
		if (hasTab()) {
			final int tabWidth = tabWidth();
			plan.move(1, 1).drawString(" " + title + " /");
			for (int x = 0; x < tabWidth - 1; x++)
				plan.move(1 + x, 2).drawChar('_');
			plan.move(1 + tabWidth - 1, 2).drawChar('/');
		}

		drawTitleOnBorderIfNeeded(plan, width);
	}

	// A partition's title (useTab == false): stamped centered directly on the
	// top border row, overwriting the dashes there — the plain "stamp the
	// title on the top border" treatment this class's first cut used
	// before the tab was introduced for other group kinds, and exactly
	// what PartitionTile.getComponent() does in pixel: draw the title, then
	// the rectangle border, centered over the same top edge. Shared verbatim
	// between ATXT and UTXT (only the border characters around it differ,
	// already drawn by the caller before this runs) so it lives once, outside
	// either asciiDrawAscii()/asciiDrawUnicode().
	private void drawTitleOnBorderIfNeeded(InfinitePlan plan, int width) {
		if (hasTitleOnBorder() == false)
			return;

		final String labeled = " " + title + " ";
		final int start = (width - labeled.length()) / 2;
		plan.move(start, 0).drawString(labeled);
	}

	// UTXT: a double-line frame (╔═╗/║/╚═╝ — the same double-line convention
	// ANote already uses for its own Unicode box, keeping frames and
	// notes visually matched and both distinct from participants' single-line
	// boxes and lifelines), with a small single-line tab in the top-left that
	// shares the frame's own top and left border rather than drawing its own
	// (mirroring the legacy asciiart.ComponentTextGroupingHeader's UTXT
	// branch — copied for the idea and the junction characters, not the code,
	// per the standing rule). One deliberate departure from
	// that legacy rendering: here the tab's right edge continues as a plain
	// single-line divider for the frame's *entire* height, down to a junction
	// with the bottom border — confirmed with Arnaud, who wanted the full
	// height rather than the legacy rendering's divider stopping at the tab's
	// own three rows.
	private void asciiDrawUnicode(InfinitePlan plan) {
		final int width = dimension.getWidth();
		final int height = dimension.getHeight();
		final boolean tab = hasTab();
		// One column past the tab's own text block (tabWidth()) — the
		// column every one of the tab's own characters below is drawn at:
		// the top-border junction, the text row's closing '│', the tab's own
		// bottom-right corner, and the full-height divider underneath it all.
		final int dividerColumn = 1 + tabWidth();

		// Top border: double line, with a junction ('╤', DOWN SINGLE AND
		// HORIZONTAL DOUBLE) where the tab's divider begins, instead of a
		// plain '═' at that one column.
		if (width >= 1) {
			plan.move(0, 0).drawChar('╔');
			if (width > 2)
				for (int x = 1; x <= width - 2; x++)
					plan.move(x, 0).drawChar(x == dividerColumn && tab ? '╤' : '═');
			if (width > 1)
				plan.move(width - 1, 0).drawChar('╗');
		}

		// Sides: double line, for every row between the top and bottom
		// borders — the frame's own outer edge, distinct from the tab's
		// single-line divider drawn separately below.
		for (int y = 1; y <= height - 2; y++) {
			plan.move(0, y).drawChar('║');
			if (width > 1)
				plan.move(width - 1, y).drawChar('║');
		}

		// Bottom border: double line, plain — unlike ATXT (asciiDrawAscii()),
		// UTXT does not switch to a different character for the footer; a
		// junction ('╧', UP SINGLE AND HORIZONTAL DOUBLE) closes off the
		// divider where it meets this border, the mirror image of the '╤'
		// junction at the top. (A plain, useTab == false frame has no divider at
		// all, so `tab` is already false here and every column is a plain '═'.)
		if (height >= 2) {
			plan.move(0, height - 1).drawChar('╚');
			for (int x = 1; x <= width - 2; x++)
				plan.move(x, height - 1).drawChar(x == dividerColumn && tab ? '╧' : '═');
			if (width > 1)
				plan.move(width - 1, height - 1).drawChar('╝');
		}

		// The tab itself: no border of its own on the top or left — it shares
		// the frame's own '╔'/'═' top border and '║' left side,
		// drawn above. It only owns its right edge (a single-line '\u2502' on
		// the text row, continuing as the full-height divider below) and its
		// bottom edge (a single-line '─' run, with a '╟' junction on
		// the left where it meets the frame's own double-line side, and a
		// '┘' corner on the right where it meets the divider) — the same
		// "text row, then a closing row" shape as the ASCII tab, just with box
		// characters instead of underscores and a diagonal.
		if (tab) {
			final int tabWidth = tabWidth();
			plan.move(1, 1).drawString(" " + title + " ");
			plan.move(dividerColumn, 1).drawChar('│');

			plan.move(0, 2).drawChar('╟');
			for (int x = 1; x < tabWidth + 1; x++)
				plan.move(x, 2).drawChar('─');
			plan.move(dividerColumn, 2).drawChar('┘');

			// The divider: a plain single-line '│', continuing from the
			// tab's own closing row down through the rest of the body, to
			// (not including) the bottom border — which closes it off with
			// its own '╧' junction, drawn above.
			for (int y = 3; y <= height - 2; y++)
				plan.move(dividerColumn, y).drawChar('│');
		}

		drawTitleOnBorderIfNeeded(plan, width);
	}

}
