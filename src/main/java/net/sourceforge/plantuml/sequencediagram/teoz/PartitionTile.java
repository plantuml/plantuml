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

import java.util.Iterator;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.style.parser2.StyleQuery;

public class PartitionTile extends GroupingTile {

	// Vertical breathing room above (and below) the title text. Without it the
	// title sits flush against the frame's top border -- issue #2679 ("title
	// spacing/padding ... right now it looks like it's one pixel from the top
	// border"). A plain `group` does not have this problem because its title
	// lives in a separate pentagon-shaped header row, sized from the title's
	// own height (see ComponentRoseGroupingHeader); a partition has no such
	// header row -- its title is drawn directly over the frame -- so the gap
	// has to be reserved and applied here explicitly.
	private static final double TITLE_VPAD = 4;

	public PartitionTile(Iterator<Event> it, GroupingStart start, TileArguments tileArgumentsBackColorChanged,
			TileArguments tileArgumentsOriginal, YGauge currentY) {

		super(it, start, tileArgumentsBackColorChanged, tileArgumentsOriginal, currentY);
	}

	// The title, as a TextBlock, built from the SAME style (groupHeader) used
	// for a plain group's tab text -- so `GroupHeaderFontColor` and friends
	// affect a partition's title exactly as they do a group's. Shared between
	// getPreferredDimension() (which must reserve enough room for it) and
	// drawU() (which draws it), so the two can never disagree on its size.
	private TextBlock titleBlock() {
		final Style styleHeader = getGroupingStart().getUsedStyles()[1];
		final Display display = Display.create(getGroupingStart().getComment());
		return display.create(styleHeader.getFontConfiguration(getSkinParam().getIHtmlColorSet()),
				HorizontalAlignment.LEFT, getSkinParam());
	}

	@Override
	protected Component getComponent(StringBounder stringBounder) {

		return new Component() {

			@Override
			public StyleQuery getStyleQuery() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Style[] getUsedStyles() {
				throw new UnsupportedOperationException();
			}

			@Override
			public double getPreferredWidth(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			@Override
			public double getPreferredHeight(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			@Override
			public XDimension2D getPreferredDimension(StringBounder stringBounder) {
				// Was hardcoded to (10, 10), regardless of the actual title: too small
				// for a bigger font (the title could overlap the first message) and,
				// more visibly, leaving no room to apply TITLE_VPAD below. Reserving
				// the title's real height plus the padding on both sides fixes both.
				final XDimension2D titleDim = titleBlock().calculateDimension(stringBounder);
				return new XDimension2D(titleDim.getWidth(), titleDim.getHeight() + 2 * TITLE_VPAD);
			}

			@Override
			public void drawU(UGraphic ug, Area area, Context2D context) {
				// The frame's border/stroke reads from getUsedStyles()[0] ("group"),
				// exactly like GroupingTile's own outer rectangle does (see its
				// drawInternalU()/symbolContext) -- NOT [1] ("groupHeader") as before.
				// That mismatch was silently dropping `GroupBorderThickness`: per
				// FromSkinparamToStyle, that skinparam converts to LineThickness on
				// "group" only, never on "groupHeader", so a partition's border
				// thickness could not be changed at all while a plain group's could.
				// LineColor is unaffected by this switch: it converts to BOTH "group"
				// and "groupHeader" identically.
				final Style styleFrame = getGroupingStart().getUsedStyles()[0];
				final TextBlock title = titleBlock();
				final double border1 = getBorder1();
				final double delta = (getWidth() - title.calculateDimension(stringBounder).getWidth()) / 2;

				title.drawU(ug.apply(new UTranslate(border1 + delta, TITLE_VPAD)));

				// RoundCorner previously had no effect at all on a partition's frame
				// (unlike a group's, whose header tab and, via the fix in
				// drawCompBackground() below, whose background both honour it).
				final double round = styleFrame.value(PName.RoundCorner).asDouble();
				final URectangle rect = URectangle.build(area.getDimensionToUse()).rounded(round);

				ug = styleFrame.applyStrokeAndLineColor(ug, getSkinParam().getIHtmlColorSet());
				ug.apply(UTranslate.dx(border1)).draw(rect);

			}
		};
	}

	@Override
	protected Area getArea(final StringBounder stringBounder) {
		return Area.create(getWidth(), getTotalHeight(stringBounder));
	}

	// ===================== ASCII =====================
	//
	// A partition is not laid out like a plain group in ASCII, exactly as it
	// is not in pixel: the two overrides below are the ASCII counterparts of
	// the getComponent()/getArea()/getWidth() overrides above.

	// Pixel getComponent() draws only the comment ("p1"), never the "partition"
	// keyword — unlike GroupingTile, whose `display` field prepends the title.
	// Mirror that here so asciiTitle() returns just "p1": otherwise the tab
	// would carry "partition p1", which is both wrong (the pixel frame shows
	// "p1") and long enough that AGroupFrame.hasTab() would refuse to draw it
	// at all for a narrow frame — which is why the title was missing entirely.
	@Override
	protected String asciiTitle() {
		return getGroupingStart().getComment();
	}

	// A partition's frame is a plain rectangle with its title centered on the
	// top border (see AGroupFrame's useTab), never the pentagon-style tab every
	// other group kind draws — the ASCII counterpart of getComponent() above,
	// which draws only the title text and the rectangle, no tab shape at all.
	@Override
	protected boolean asciiFrameHasTab() {
		return false;
	}

	// Pixel getWidth()/getArea() make a partition span the whole diagram
	// (getBorder2() - getBorder1(), drawn at getBorder1()), not just the
	// columns its children touch — the visible difference between a partition
	// and a plain group frame. Mirror that in ASCII: span from the first
	// participant's left column to the last participant's right column, plus
	// the usual frame margin, instead of GroupingTile's children-based span.
	// The attached notes (a "note right" past the last participant, a "note
	// left" before the first) fall just outside these borders, exactly as they
	// sit outside the frame in the pixel rendering.
	@Override
	protected int[] asciiFrameColumns() {
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		for (LivingSpace ls : getTileArguments().getLivingSpaces().values()) {
			// posB is the box's left column, posD its exclusive right edge, so
			// posD - 1 is the box's last visible column.
			left = Math.min(left, (int) ls.getAsciiPosB().getCurrentValue());
			right = Math.max(right, (int) ls.getAsciiPosD().getCurrentValue() - 1);
		}
		left -= ASCII_FRAME_MARGIN;
		right += ASCII_FRAME_MARGIN;

		// A "note left"/"note right" attached to a message inside the partition
		// must land *inside* the frame, matching the pixel rendering (both images
		// Arnaud provided) — unlike a plain group, where notes fall just outside
		// (GroupingTile's own header/footer notes, drawNotes()). getAsciiMinX()/
		// getAsciiMaxX() (inherited from GroupingTile) already fold in any attached
		// note's width via CommunicationTileNoteLeft/Right's own overrides, so
		// merging them in here widens the full-diagram span above whenever a note
		// reaches further than the participants alone do.
		final Real childMin = getAsciiMinX();
		if (childMin != null)
			left = Math.min(left, (int) childMin.getCurrentValue());

		final Real childMax = getAsciiMaxX();
		if (childMax != null)
			right = Math.max(right, (int) childMax.getCurrentValue());

		return new int[] { left, right };
	}

	private double getWidth() {
		return getBorder2() - getBorder1();
	}

	private double getBorder2() {
		return getTileArguments().getBorder2();
	}

	private double getBorder1() {
		return getTileArguments().getBorder1();
	}

	@Override
	protected double minCurrentValueForDrawing() {
		return 0;
	}

	@Override
	protected void drawCompBackground(UGraphic ug, Area area, final HColor back, final double round) {
		// `round` used to be silently ignored here (unlike GroupingTile's own
		// background fill, which threads it through to its Blotter bands): a
		// partition's background stayed square-cornered even when RoundCorner
		// was set, while its border (see getComponent() above) was square
		// regardless. Both now honour the same value.
		final URectangle rect = URectangle.build(area.getDimensionToUse()).rounded(round);
		ug.apply(UTranslate.dx(getBorder1())).apply(back.bg()).draw(rect);
	}

}
