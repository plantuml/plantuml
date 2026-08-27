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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.asciiverse.ADimension2D;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;

public class NewpageTile extends AbstractTile {

	// Small vertical space before and after the dashed separator
	private static final double MARGINY = 10;

	// Same widening as GroupingTile.ASCII_FRAME_MARGIN, kept as its own
	// constant rather than reused across classes: a newpage separator has no
	// frame to be a margin "around", it just happens to want the same 2-cell
	// breathing room past the outermost participant box so the dashed line
	// reads as a full-width divider rather than stopping flush on the boxes.
	private static final int ASCII_MARGIN = 2;

	private final Newpage newpage;
	private final TileArguments tileArguments;
	private final YGauge yGauge;

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	public NewpageTile(Newpage newpage, TileArguments tileArguments, YGauge currentY) {
		super(tileArguments.getStringBounder(), currentY);
		this.newpage = newpage;
		this.tileArguments = tileArguments;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private Component getComponent() {
		return tileArguments.getSkin().createComponentNewPage(newpage.getUsedStyles(), tileArguments.getSkinParam());
	}

	public void drawU(UGraphic ug) {
		// Like in Puma, the dashed separator is displayed at the bottom of the
		// page ending here and at the top of the page starting here: the pages
		// slightly overlap in PlayingSpaceWithParticipants
		if (((Context2D) ug).isBackground())
			return;

		// Self-translate prologue: absolute gauge position
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));

		final Component comp = getComponent();
		final Area area = Area.create(tileArguments.getBorder2() - tileArguments.getBorder1()
				- tileArguments.getXOrigin().getCurrentValue(), comp.getPreferredHeight(getStringBounder()));
		ug = ug.apply(new UTranslate(tileArguments.getBorder1(), MARGINY));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		return getComponent().getPreferredHeight(getStringBounder()) + 2 * MARGINY;
	}

	public void addConstraints() {
	}

	public Real getMinX() {
		return tileArguments.getXOrigin();
	}

	public Real getMaxX() {
		return tileArguments.getXOrigin();
	}

	public Event getEvent() {
		return newpage;
	}

	// ASCII counterpart of drawU()'s full-width dashed separator (drawn there
	// across tileArguments.getBorder1()..getBorder2()). Unlike GroupingTile,
	// this tile is never nested inside a frame that could impose its own
	// column span, so there is only one case, not a fallback: always spans
	// every participant of the diagram, the same "no children of its own"
	// convention GroupingTile.asciiFrameColumns() falls back to when a group
	// is empty (see its own comment for why posB..posD-1, not the life
	// columns, is the diagram's edge convention).
	private int[] asciiColumns() {
		int lo = Integer.MAX_VALUE;
		int hi = Integer.MIN_VALUE;
		for (LivingSpace ls : tileArguments.getLivingSpaces().values()) {
			lo = Math.min(lo, (int) ls.getAsciiPosB().getCurrentValue());
			hi = Math.max(hi, (int) ls.getAsciiPosD().getCurrentValue() - 1);
		}
		if (lo == Integer.MAX_VALUE)
			return new int[] { 0, 0 };

		return new int[] { lo - ASCII_MARGIN, hi + ASCII_MARGIN };
	}

	@Override
	public ADimension2D asciiDimension() {
		final int[] cols = asciiColumns();
		return new ADimension2D(cols[1] - cols[0] + 1, 1);
	}

	// Single dashed row spanning the resolved columns -- dotted like the
	// pixel version's dashed separator (see MARGINY's comment above and
	// drawU()), reusing InfinitePlan.drawHLine()'s own dash/dotted logic
	// rather than picking a character here, so this stays correct for both
	// ATXT and UTXT with no branch in this class (same reasoning as
	// AElseSeparator.asciiDraw()).
	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int[] cols = asciiColumns();
		final int width = cols[1] - cols[0] + 1;
		if (width > 0)
			plan.move(cols[0], 0).drawHLine(0, width - 1, 0, true);
	}

}
