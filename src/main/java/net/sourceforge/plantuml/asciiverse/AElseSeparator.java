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

// The dashed divider drawn across a group frame at each `else`/section
// boundary (alt/else, ...), as an AsciiBlock: the ASCII counterpart of the
// pixel GROUPING_ELSE_TEOZ Component that GroupingTile.drawAllElses() draws
// spanning the frame's full width. Like AGroupFrame,
// its width is imposed from the outside — only the parent GroupingTile knows
// the frame's column span — so it takes an explicit width rather than
// computing one; the ElseTile itself owns only the divider's row height (its
// asciiDimension()) and the label text (ElseTile.asciiLabel()).
public final class AElseSeparator implements AsciiBlock {

	private final int width;
	private final String label;

	public AElseSeparator(int width, String label) {
		this.width = width;
		this.label = label == null ? "" : label;
	}

	@Override
	public ADimension2D asciiDimension() {
		return new ADimension2D(width, 1);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		// Dashed line across the frame interior only (columns 1..width-2),
		// leaving the frame's own side borders — drawn by AGroupFrame at
		// columns 0 and width-1 — untouched. Dotted, so it reads as a divider
		// rather than a second solid border; the dash character (ASCII '-' vs
		// Unicode '─') is chosen by the plan itself, so this stays
		// correct for both ATXT and UTXT with no branch here. The dashes are
		// drawn over the lifelines that cross this row (the later lifeline-fill
		// pass only writes into empty cells), which is the correct "divider
		// crosses the lifelines" look, for free.
		if (width > 2)
			plan.drawHLine(1, width - 2, 0, true);

		// Label near the left corner, wrapped in spaces so it reads cleanly
		// against the dashes — same minimal tab treatment as AGroupFrame's
		// title. Skipped when it would not fit inside the interior (same width
		// guard shape as AGroupFrame); its own width is not reserved on the
		// ASCII column solver yet, the same known gap as the frame title.
		if (label.isEmpty() == false && width > label.length() + 4)
			plan.move(2, 0).drawString(" " + label + " ");
	}

}
