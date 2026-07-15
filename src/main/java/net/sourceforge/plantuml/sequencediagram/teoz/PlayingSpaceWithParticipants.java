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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.asciiverse.AsciiBlock;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlockMemoized;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealOrigin;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;

public class PlayingSpaceWithParticipants extends TextBlockMemoized implements AsciiBlock {

	private final PlayingSpace playingSpace;

	private int pageIndex;

	public PlayingSpaceWithParticipants(PlayingSpace playingSpace) {
		this.playingSpace = Objects.requireNonNull(playingSpace);
	}

	@Override
	public XDimension2D calculateDimensionSlow(StringBounder stringBounder) {
		final double width = playingSpace.getMaxX(stringBounder).getCurrentValue()
				- playingSpace.getMinX(stringBounder).getCurrentValue();

		// getPreferredHeight() triggers the layout pass that positions the tiles,
		// so it must be called before getYMin()/getYMax()
		final double fullHeight = playingSpace.getPreferredHeight(stringBounder);
		final double pageHeight = getYMax(fullHeight) - getYMin();

		final int factor = playingSpace.isShowFootbox() ? 2 : 1;
		final double height = pageHeight + factor * playingSpace.getLivingSpaces().getHeadHeight(stringBounder);

		return new XDimension2D(width, height);
	}

	// Warning: both methods below rely on the tiles' YGauge being solved: they are
	// meaningful only after a layout pass, that is after
	// playingSpace.getPreferredHeight() has been called at least once.
	// Like in Puma (see PageSplitter), consecutive pages slightly overlap on the
	// newpage separator, so that the dashed line is visible at the bottom of the
	// page ending there and at the top of the page starting there.

	private double getYMin() {
		if (pageIndex == 0)
			return 0;

		return playingSpace.getNewpageTiles().get(pageIndex - 1).getYGauge().getMin().getCurrentValue();
	}

	private double getYMax(double fullHeight) {
		final List<NewpageTile> newpages = playingSpace.getNewpageTiles();
		if (pageIndex >= newpages.size())
			return fullHeight;

		final NewpageTile newpage = newpages.get(pageIndex);
		return Math.min(newpage.getYGauge().getMin().getCurrentValue() + newpage.getPreferredHeight(), fullHeight);
	}

	@Override
	public void asciiDraw(InfinitePlan plan) {
		// Minimal ASCII rendering, in the spirit of drawU(): the participant
		// heads, then the lifelines, then the messages drawn by their own tiles
		// (CommunicationTile.asciiDraw), reusing the Teoz tile tree as topology.
		final LivingSpaces livingSpaces = playingSpace.getLivingSpaces();
		final List<Participant> participants = new ArrayList<>(livingSpaces.participants());
		if (participants.size() == 0)
			return;

		// --- X layout: a dedicated ASCII Real graph, convention 1.0 = 1 char,
		// on its own RealLine, separate from the pixel xorigin. Chain each
		// participant's asciiPosB off a shared origin (exactly like the pixel
		// createMainTile() chains posB off xorigin), let the participants and
		// the tiles declare their ASCII constraints, then compile once. Every
		// injected delta is an integer, so the resolved columns are exact
		// integers (no quantization).
		final RealOrigin asciiXOrigin = RealUtils.createOrigin();
		Real asciiCurrent = asciiXOrigin.addAtLeast(0);
		for (Participant p : participants) {
			final LivingSpace ls = livingSpaces.get(p);
			ls.setAsciiPosB(asciiCurrent);
			asciiCurrent = ls.getAsciiPosD().addAtLeast(0);
		}
		livingSpaces.asciiAddConstraints();
		for (Tile tile : playingSpace.getTiles())
			tile.asciiAddConstraints();
		asciiXOrigin.compileNow();

		// Heads (top boxes), drawn by the participants themselves at their
		// resolved left column.
		for (Participant p : participants)
			p.asciiDraw(plan.move((int) livingSpaces.get(p).getAsciiLeftColumn().getCurrentValue(), 0));

		final int headBottom = 2;
		int y = headBottom + 1;

		// Messages, drawn by their own tiles (row 0 = label start, arrow row
		// right after — see CommunicationTile.asciiDraw()/asciiDimension()).
		// Y advances by each tile's own asciiDimension().getHeight() rather than
		// a flat per-message constant, so a multi-line label
		// gets the extra rows it needs instead of the next tile overwriting it.
		// Every top-level tile must have a real asciiDraw()/asciiDimension()
		// override to reach this loop — there is no catch
		// (UnsupportedOperationException)
		// here (anymore): a tile that hasn't been migrated to ASCII yet crashes
		// loudly instead of being silently skipped, which is the point (see
		// EmptyTile for the fix this policy forced: it needed a real, if trivial,
		// override rather than falling through to the AsciiBlock "not migrated"
		// default).
		int bottomY = y;
		for (Tile tile : playingSpace.getTiles()) {
			tile.asciiDraw(plan.move(0, y));
			final int height = tile.asciiDimension().getHeight();
			y += height;
			bottomY = y;
		}

		// Footer boxes, mirroring drawU()'s playingSpace.isShowFootbox() branch:
		// each participant draws itself a second time, right below the lifelines.
		if (playingSpace.isShowFootbox())
			for (Participant p : participants)
				p.asciiDraw(plan.move((int) livingSpaces.get(p).getAsciiLeftColumn().getCurrentValue(), bottomY));

		// Lifelines from the heads down to the last message, at each
		// participant's resolved lifeline column, only into empty cells so the
		// arrows already drawn are never overwritten.
		final char vLine = plan.getVLineChar();
		for (Participant p : participants) {
			final int lx = (int) livingSpaces.get(p).getAsciiLifeColumn().getCurrentValue();
			for (int yy = headBottom + 1; yy < bottomY; yy++)
				if (plan.getCharAt(lx, yy) == ' ')
					plan.move(lx, yy).drawChar(vLine);
			plan.move(lx, headBottom).muteLifelineBelowBox();
			plan.move(lx, bottomY).muteLifelineAboveBox();
		}

	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Context2D context = new SimpleContext2D(false);
		final LivingSpaces livingSpaces = playingSpace.getLivingSpaces();
		final double headHeight = livingSpaces.getHeadHeight(stringBounder);

		// getPreferredHeight() triggers the layout pass that positions the tiles,
		// so it must be called before getYMin()/getYMax()
		final double fullHeight = playingSpace.getPreferredHeight(stringBounder);
		final double ymin = getYMin();
		final double ymax = getYMax(fullHeight);
		final double pageHeight = ymax - ymin;

		// The body is translated so that the current page starts right below the
		// heads, and clipped so that the content of the other pages stays hidden.
		UGraphic ugBody = ug.apply(UTranslate.dy(headHeight - ymin));
		if (playingSpace.getNbPages() > 1) {
			final UClip clip = new UClip(-1000, ymin, Double.MAX_VALUE, pageHeight + 1);
			ugBody = ugBody.apply(clip);
		}

		playingSpace.drawBackground(ugBody);
		// Lifelines and liveboxes use absolute positions over the whole diagram:
		// they must be drawn with the full height, the clip trims them to the page
		livingSpaces.drawLifeLines(ugBody, fullHeight, context);

		livingSpaces.drawHeads(ug, context, VerticalAlignment.BOTTOM);
		if (playingSpace.isShowFootbox())
			livingSpaces.drawHeads(ug.apply(UTranslate.dy(pageHeight + headHeight)), context, VerticalAlignment.TOP);

		playingSpace.drawForeground(ugBody);
		// drawNewPages(ug.apply(UTranslate.dy(headHeight)));
	}

	public Real getMinX(StringBounder stringBounder) {
		return playingSpace.getMinX(stringBounder);
	}

	public int getNbPages() {
		return playingSpace.getNbPages();
	}

	public void setIndex(int index) {
		if (index != this.pageIndex) {
			this.pageIndex = index;
			invalidateDimensionCache();
		}
	}

	private List<Double> yNewPages() {
		return playingSpace.yNewPages();
	}

	private void drawNewPages(UGraphic ug) {
		ug = ug.apply(HColors.BLUE);
		for (Double change : yNewPages()) {
			if (change == 0 || change == Double.MAX_VALUE) {
				continue;
			}
			ug.apply(UTranslate.dy(change)).draw(ULine.hline(100));
		}
	}

}
