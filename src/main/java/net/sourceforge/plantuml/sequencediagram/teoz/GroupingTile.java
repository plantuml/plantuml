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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.asciiverse.ADimension2D;
import net.sourceforge.plantuml.asciiverse.AElseSeparator;
import net.sourceforge.plantuml.asciiverse.AGroupFrame;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Grouping;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class GroupingTile extends AbstractTile {

	public static final int EXTERNAL_MARGINX1 = 3;
	public static final int EXTERNAL_MARGINX2 = 9;
	// Vertical breathing room around a group frame, on each side. Legacy
	// materialized it as two ghost EmptyTile(4, ...) around every GroupingTile
	// (see TileBuilder.buildOne). Under USE_ME it is reserved in
	// getPreferredHeight() (both sides) and the TOP one is applied at draw time
	// via getFrameY() -- deliberately NOT by offsetting the gauge min, which
	// must stay a clean chaining point for parallel (&) siblings.
	public static final int EXTERNAL_MARGINY = 4;
	private static final int MARGINX = 16;
	// private static final int MARGINY = 10;
	private static final int MARGINY_MAGIC = 20;
	private List<Tile> tiles = new ArrayList<>();
	private final Real min;
	private final Real max;
	private final GroupingStart start;
	private GroupingLeaf end;
	private final YGauge yGauge;

	private final Rose skin;
	private final ISkinParam skinParam;
	private final Display display;

	private double bodyHeight;
	private final TileArguments tileArguments;

	public Event getEvent() {
		return start;
	}

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	public GroupingTile(Iterator<Event> it, GroupingStart start, TileArguments tileArgumentsBackColorChanged,
			TileArguments tileArgumentsOriginal, YGauge currentY) {
		super(tileArgumentsBackColorChanged.getStringBounder(), currentY);
		this.tileArguments = tileArgumentsOriginal;
		final StringBounder stringBounder = tileArgumentsOriginal.getStringBounder();
		this.start = start;
		this.display = start.getTitle().equals("group") ? Display.create(start.getComment())
				: Display.create(start.getTitle(), start.getComment());
		this.skin = tileArgumentsOriginal.getSkin();
		// this.skinParam = tileArgumentsOriginal.getSkinParam();
		this.skinParam = tileArgumentsBackColorChanged.getSkinParam();

		final List<Real> min2 = new ArrayList<>();
		final List<Real> max2 = new ArrayList<>();

		final List<Tile> allElses = new ArrayList<>();
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);

		// Chain anchoring for a whole & -parallel group (see YGAUGE.md /
		// CHAIN ANCHORING): a group following "& opt"/"& alt" must start at
		// the PREVIOUS tile's min (top-aligned, like CommunicationTile does
		// for a plain parallel message), not its max (which would stack it
		// below, sequentially). previousMax is kept aside so the outer chain's
		// max can still be pushed past whichever of the two members is taller.
		final Real previousMax = currentY.getMax();
		final boolean parallel = start.isParallel();
		// INVARIANT: the gauge's min is the CHAINING POINT, never the drawn
		// frame's top. Those two differ by EXTERNAL_MARGINY for a group (the
		// legacy leading EmptyTile(4)), and conflating them broke `&` groups:
		// a parallel sibling chains on the previous group's min, so if that min
		// were already margin-offset the sibling would inherit the offset AND
		// add its own -- drifting 4px lower per pair. The margin is applied at
		// DRAW time instead (see getFrameY() / drawU), so the min stays a clean
		// chaining point that parallel siblings can align on.
		final Real firstY = parallel ? currentY.getMin() : previousMax;

		// The body starts below the frame's top margin AND the header
		final double h = dim1.getHeight() + MARGINY_MAGIC / 2 + getExternalMarginY();
		currentY = YGauge.create(firstY.addAtLeast(h), 0);

		while (it.hasNext()) {
			final Event ev = it.next();
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.END) {
				this.end = (GroupingLeaf) ev;
				break;
			}

			for (Tile tile : TileBuilder.buildOne(it, tileArgumentsOriginal, ev, this, currentY)) {
				tiles.add(tile);
				if (YGauge.USE_ME)
					currentY = tile.getYGauge();
			}

		}

		tiles = mergeParallel(getStringBounder(), tiles);

		// bodyHeight drives both the frame's own drawn height (getTotalHeight)
		// and the space this GroupingTile reserves in the OUTER Y chain
		// (getPreferredHeight() -> this.yGauge below): it must count each
		// parallel (&) cluster ONCE (tallest-before-contact + tallest-after-
		// contact, like legacy TileParallel.getPreferredHeight()), not sum
		// every member's own height, or the group ends up taller than needed
		// whenever it contains a & message (see YGAUGE.md). Under legacy,
		// `tiles` (just reassigned above) is already merged into TileParallel
		// clusters. Under USE_ME, mergeParallel() is a no-op (Y positioning is
		// handled by the gauge chain instead), so a SEPARATE, unconditional
		// merge is used here purely to get correct height clustering, without
		// touching the flat `tiles` field used for drawing.
		final List<Tile> heightTiles = YGauge.USE_ME ? mergeParallelCore(getStringBounder(), tiles) : tiles;
		for (Tile tile : heightTiles)
			bodyHeight += tile.getPreferredHeight();

		for (Tile tile : tiles) {
			final Event ev = tile.getEvent();
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.ELSE) {
				allElses.add(tile);
				continue;
			}
			// Else tiles merged inside a TileParallel (par2) must also be processed
			// later, like plain else tiles: their min X is the min X of this very
			// GroupingTile, which is not computed yet
			if (tile instanceof TileParallel)
				allElses.addAll(((TileParallel) tile).getElseTiles());

			min2.add(tile.getMinX().addFixed(-MARGINX));
			final Real m = tile.getMaxX();
			// max2.add(m == tileArgumentsOriginal.getOmega() ? m : m.addFixed(MARGINX));
			max2.add(m.addFixed(MARGINX));
		}
		final double width = dim1.getWidth();
		if (min2.size() == 0)
			min2.add(tileArgumentsOriginal.getXOrigin());

		this.min = RealUtils.min(min2);
		for (Tile anElse : allElses)
			max2.add(anElse.getMaxX());

		max2.add(this.min.addFixed(width + 16));
		this.max = RealUtils.max(max2);
		// min == chaining point (NOT the frame top -- see firstY above).
		// getPreferredHeight() covers the frame plus BOTH margins, so the max is
		// simply min + getPreferredHeight().
		if (parallel) {
			// Like YGauge.createParallel: the group's own max must still
			// dominate the PREVIOUS sibling's max, so that whichever of the
			// two parallel groups is taller correctly pushes the next
			// sequential tile below the whole pair, not just below this one.
			final Real parallelMax = firstY.addAtLeast(getPreferredHeight());
			parallelMax.ensureBiggerThan(previousMax);
			this.yGauge = new YGauge(firstY, parallelMax);
		} else {
			this.yGauge = new YGauge(firstY, firstY.addAtLeast(getPreferredHeight()));
		}

	}

	// The 4px of breathing room above the drawn frame (legacy's leading ghost
	// EmptyTile). Zero under the legacy path, where the real EmptyTiles still
	// exist and do the job.
	private static double getExternalMarginY() {
		return YGauge.USE_ME ? EXTERNAL_MARGINY : 0;
	}

	// Absolute Y of the DRAWN frame's top edge: the gauge min (the chaining
	// point) plus the top margin. Everything that draws the frame or positions
	// something relative to it must go through this, never through the raw
	// gauge min.
	private double getFrameY() {
		return getYGauge().getMin().getCurrentValue() + getExternalMarginY();
	}

	protected Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(start.getUsedStyles(), ComponentType.GROUPING_HEADER_TEOZ, null,
				skinParam, display);
		return comp;
	}

	private XDimension2D getPreferredDimensionIfEmpty(StringBounder stringBounder) {
		return getComponent(stringBounder).getPreferredDimension(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Area area = getArea(stringBounder);

		final Component comp = getComponent(stringBounder);
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);

		if (YGauge.USE_ME) {
			if (((Context2D) ug).isBackground())
				drawBackground(ug, area);
			// The frame is drawn EXTERNAL_MARGINY below the gauge min (which is
			// the chaining point, not the frame top -- see getFrameY)
			comp.drawU(ug.apply(new UTranslate(minCurrentValueForDrawing(), getFrameY())), area, (Context2D) ug);
			drawNotes(ug.apply(UTranslate.dy(getFrameY())));
		} else {
			if (((Context2D) ug).isBackground()) {
				drawBackground(ug, area);
				return;
			}
			comp.drawU(ug.apply(UTranslate.dx(minCurrentValueForDrawing())), area, (Context2D) ug);
			drawAllElses(ug);
			drawNotes(ug);
		}

		double h = dim1.getHeight() + MARGINY_MAGIC / 2;
		for (Tile tile : tiles) {
			if (YGauge.USE_ME)
				((UDrawable) tile).drawU(ug);
			else
				((UDrawable) tile).drawU(ug.apply(UTranslate.dy(h)));
			final double preferredHeight = tile.getPreferredHeight();
			h += preferredHeight;
		}
	}

	protected Area getArea(final StringBounder stringBounder) {
		final Area area = Area.create(max.getCurrentValue() - min.getCurrentValue(), getTotalHeight(stringBounder));
		return area;
	}

	protected double minCurrentValueForDrawing() {
		return min.getCurrentValue();
	}

	// Frame extents (the group's own border), as opposed to getMinX()/getMaxX()
	// which additionally reserve EXTERNAL_MARGINX1/2 and the width of notes
	// attached to the group's "end" ("note left"/"note right" after `end`) --
	// that reservation is for the PARENT's layout only. Children that draw
	// something spanning the frame itself (the else divider line, the
	// background Blotter) must use these, not getMinX()/getMaxX(), or they
	// draw wider than the visible frame whenever a group-end note is present.
	final Real getFrameMinX() {
		return min;
	}

	final Real getFrameMaxX() {
		return max;
	}

	private void drawBackground(UGraphic ug, Area area) {
		final Style style = start.getUsedStyles()[0];
		final HColor back = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final double round = style.value(PName.RoundCorner).asDouble();
		if (YGauge.USE_ME) {
			// Under USE_ME, `ug` arrives UNTRANSLATED (each tile self-translates
			// via its own absolute gauge, unlike legacy where the caller
			// pre-translates to the group's TimeHook / frame top). So the
			// Blotter -- which draws its bands from a LOCAL y=0 -- must be fed a
			// `ug` explicitly translated to (frame left, frame top), not just an
			// x offset like the legacy branch below. drawCompBackground's ypos
			// formula mirrors drawAllElses's (frame-relative, via getFrameY()),
			// not the legacy getTimeHook()-relative one.
			drawCompBackground(ug.apply(UTranslate.dy(getFrameY())), area, back, round);
			return;
		}
		drawCompBackground(ug, area, back, round);

		final StringBounder stringBounder = ug.getStringBounder();

		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		double h = dim1.getHeight() + MARGINY_MAGIC / 2;
		for (Tile tile : tiles) {
			((UDrawable) tile).drawU(ug.apply(UTranslate.dy(h)));
			final double preferredHeight = tile.getPreferredHeight();
			h += preferredHeight;
		}

	}

	protected void drawCompBackground(UGraphic ug, Area area, final HColor back, final double round) {
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final Blotter blotter = new Blotter(dimensionToUse, back, round);

		for (Tile tile : tiles)
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final double ypos;
				if (YGauge.USE_ME)
					// NOT the same formula as legacy's `+ MARGINY_MAGIC / 2`: that
					// term only stays consistent in legacy because drawAllElses
					// (which draws the ACTUAL divider line there) uses the exact
					// same "+10" itself, so the two cancel out. Under USE_ME the
					// divider is drawn independently by ElseTile.drawU(), exactly
					// at `gauge.min` with NO offset -- so the color boundary here
					// must match that exactly, or the band sits 10px away from
					// the actual dashed separator (see YGAUGE.md).
					ypos = elseTile.getYGauge().getMin().getCurrentValue() - getFrameY();
				else
					ypos = elseTile.getTimeHook().getValue() - getTimeHook().getValue() + MARGINY_MAGIC / 2;
				HColor backElse = elseTile.getBackColorGeneral();
				// An else section without its own color inherits the group
				// background: it may come from the style, not only from the
				// inline #color of the command, so getBackColorGeneral() alone
				// is not enough
				if (backElse == null)
					backElse = back;
				blotter.addChange(ypos + 1, backElse);
			}

		blotter.closeChanges();
		// Under USE_ME, `ug` was already translated to (frame left, frame top)
		// by the caller (see drawBackground above); under legacy it arrives
		// pre-translated to the frame top by the OUTER caller of drawU itself.
		// Either way only the X offset is needed here.
		blotter.drawU(ug.apply(UTranslate.dx(min.getCurrentValue())));
	}

	final protected double getTotalHeight(StringBounder stringBounder) {
		final XDimension2D dimIfEmpty = getPreferredDimensionIfEmpty(stringBounder);
		return bodyHeight + dimIfEmpty.getHeight() + MARGINY_MAGIC / 2;
	}

	private void drawAllElses(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final List<Double> ys = new ArrayList<>();
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final double ypos;
				if (YGauge.USE_ME)
					// Relative to the FRAME's top, not the gauge min
					ypos = elseTile.getYGauge().getMin().getCurrentValue() - getFrameY() + MARGINY_MAGIC / 2;
				else
					ypos = elseTile.getTimeHook().getValue() - getTimeHook().getValue() + MARGINY_MAGIC / 2;
				ys.add(ypos);
			}
		}
		final double totalHeight = getTotalHeight(stringBounder);
		ys.add(totalHeight);
		int i = 0;
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final Component comp = elseTile.getComponent(stringBounder);
				final Area area = Area.create(max.getCurrentValue() - min.getCurrentValue(), ys.get(i + 1) - ys.get(i));
				comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), ys.get(i))), area, (Context2D) ug);
				i++;
			}
		}
	}

	@Override
	public double getPreferredHeight() {
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(getStringBounder());
		double result = dim1.getHeight() + bodyHeight + MARGINY_MAGIC;
		if (YGauge.USE_ME)
			// Legacy wrapped every group with two ghost EmptyTile(4, ...) spacers
			// (TileBuilder.buildOne, skipped under USE_ME): 4px BEFORE the frame
			// and 4px AFTER it. Both are reserved here, so the gauge spans the
			// frame plus its two margins and the next tile chains clear of it.
			// The TOP margin is additionally materialized at draw time (the frame
			// is drawn at getFrameY() == gauge min + EXTERNAL_MARGINY), which is
			// what actually moves the border down. Reserving without drawing the
			// offset leaves the frame flush against the previous tile -- and, right
			// after a `newpage`, flush against the page clip boundary, so the top
			// border bleeds into the previous page (see YGAUGE.md).
			result += 2 * EXTERNAL_MARGINY;
		return result;
	}

	public void addConstraints() {
		for (Tile tile : tiles)
			tile.addConstraints();

	}

	// ===================== ASCII (ASCIIVERSE.md) =====================
	//
	// A group (frame, alt, opt, loop, partition, ...) draws itself as a frame
	// box enclosing its stacked children, with its title on the top border
	// row — the same object-oriented "each object draws itself" pattern as
	// every other tile (§9), and structurally the ASCII counterpart of the
	// pixel drawU() above (frame component + children stacked below the
	// header). PartitionTile inherits all of this unchanged.
	//
	// One margin constant, the ASCII analogue of the pixel MARGINX: how many
	// cells the frame border sits outside the leftmost/rightmost column its
	// children actually touch, so arrows and lifelines run inside the frame,
	// not on its border.
	protected static final int ASCII_FRAME_MARGIN = 2;

	// The header is the frame's top border, plus (when the title fits) its
	// pentagon-style tab — delegated entirely to AGroupFrame.getBodyRowOffset()
	// (§28), since it alone decides whether the tab is drawn; the footer is a
	// single row (the tilde bottom border). The body in between is the children
	// stacked, each at its own asciiDimension() height.
	private static final int ASCII_FOOTER_ROWS = 1;

	// Builds a throwaway AGroupFrame (same pattern as ANote's size, read back
	// rather than recomputed elsewhere, §18/§20) purely to ask how many rows its
	// header occupies — needed by both asciiDimension() (to size the frame
	// before it's ever drawn) and asciiDraw()'s child-stacking loop. The height
	// passed to ADimension2D here is irrelevant (getBodyRowOffset() only reads
	// the width), so 0 is fine.
	private int asciiHeaderRows() {
		final int[] cols = asciiFrameColumns();
		final int width = cols[1] - cols[0] + 1;
		return new AGroupFrame(new ADimension2D(width, 0), asciiTitle(), asciiFrameHasTab()).getBodyRowOffset();
	}

	// Whether this group's ASCII frame uses the pentagon-style cut-corner tab
	// (true, every plain group/alt/loop/... kind) or stamps its title centered
	// directly on the top border instead (false). Overridden by PartitionTile:
	// a partition's pixel rendering (PartitionTile.getComponent()) never draws
	// a tab, only a plain rectangle with a centered title — see AGroupFrame's
	// useTab field for the actual shape difference.
	protected boolean asciiFrameHasTab() {
		return true;
	}

	@Override
	public void asciiAddConstraints() {
		// Exactly like addConstraints() above: recurse into the children so a
		// message nested inside the group still pushes its two participants
		// apart. The frame's own title width is not (yet) reserved on the
		// ASCII column solver (§14) — a very long group title could overflow
		// the frame past the participants; same class of known gap as notes.
		for (Tile tile : tiles)
			tile.asciiAddConstraints();
	}

	// The union of the children's own ascii ranges, expressed as composable
	// Real (not yet resolved columns) — exactly the same shape as the pixel
	// min/max computed from tile.getMinX()/getMaxX() in the constructor above
	// (RealUtils.min(min2)/RealUtils.max(max2)). Unlike the pixel version this
	// can't be precomputed in the constructor: the ASCII Real graph
	// (asciiPosB...) doesn't exist yet at construction time, only once
	// PlayingSpaceWithParticipants.asciiDraw() wires it up — so it's computed
	// lazily, on demand, each time it's asked. Children with no range of their
	// own (null, e.g. EmptyTile) are skipped; if none has any, null is
	// returned, same contract as the Tile default.
	private Real asciiChildrenMin() {
		final List<Real> mins = new ArrayList<>();
		for (Tile tile : tiles) {
			final Real m = tile.getAsciiMinX();
			if (m != null)
				mins.add(m);
		}
		if (mins.isEmpty())
			return null;

		return RealUtils.min(mins);
	}

	private Real asciiChildrenMax() {
		final List<Real> maxs = new ArrayList<>();
		for (Tile tile : tiles) {
			final Real m = tile.getAsciiMaxX();
			if (m != null)
				maxs.add(m);
		}
		if (maxs.isEmpty())
			return null;

		return RealUtils.max(maxs);
	}

	@Override
	public Real getAsciiMinX() {
		final Real childMin = asciiChildrenMin();
		if (childMin == null)
			return null;

		return childMin.addFixed(-ASCII_FRAME_MARGIN);
	}

	@Override
	public Real getAsciiMaxX() {
		final Real childMax = asciiChildrenMax();
		if (childMax == null)
			return null;

		return childMax.addFixed(ASCII_FRAME_MARGIN);
	}

	// The [left, right] resolved ASCII columns of the frame border itself,
	// read from getAsciiMinX()/getAsciiMaxX() once the ASCII RealLine has
	// compiled — the one place in this class where a Real is finally cast to
	// an int, since InfinitePlan only understands character columns. Falls
	// back to spanning every participant of the diagram when the group has no
	// child with any columns (e.g. an empty group), so it still draws a
	// sensible frame instead of crashing on a null range.
	protected int[] asciiFrameColumns() {
		final Real min = getAsciiMinX();
		final Real max = getAsciiMaxX();
		if (min != null && max != null)
			return new int[] { (int) min.getCurrentValue(), (int) max.getCurrentValue() };

		int lo = Integer.MAX_VALUE;
		int hi = Integer.MIN_VALUE;
		for (LivingSpace ls : getTileArguments().getLivingSpaces().values()) {
			final int c = (int) ls.getAsciiLifeColumn().getCurrentValue();
			if (c < lo)
				lo = c;
			if (c > hi)
				hi = c;
		}
		if (lo == Integer.MAX_VALUE)
			return new int[] { 0, 0 };

		return new int[] { lo - ASCII_FRAME_MARGIN, hi + ASCII_FRAME_MARGIN };
	}

	// Sum of the children's own ASCII heights — the body height, between the
	// header (top border) and footer (bottom border) rows. Reads each height
	// back off asciiDimension() rather than hardcoding it (the same rule as
	// PlayingSpaceWithParticipants' top-level loop, §19), and does NOT catch
	// UnsupportedOperationException: a child with no ASCII support crashes
	// here, naming itself, exactly like the orchestrator's own policy (§21).
	private int asciiBodyHeight() {
		int h = 0;
		for (Tile tile : tiles)
			h += tile.asciiDimension().getHeight();
		return h;
	}

	@Override
	public ADimension2D asciiDimension() {
		final int[] cols = asciiFrameColumns();
		final int width = cols[1] - cols[0] + 1;
		final int height = asciiHeaderRows() + asciiBodyHeight() + ASCII_FOOTER_ROWS;
		return new ADimension2D(width, height);
	}

	// ASCII counterpart of drawU(): delegate the frame box + title to an
	// AGroupFrame — the AsciiBlock counterpart of the pixel GROUPING_HEADER_TEOZ
	// Component that drawU() obtains from the skin and delegates to (see
	// ASCIIVERSE.md §26) — positioned at the frame's absolute left column, then
	// draw each child inside, stacked, exactly like drawU() translates each
	// child by the running height below the header. Children compute their own
	// absolute columns from getAsciiLifeColumn() (they ignore the plan's
	// horizontal translation for their arrows — see CommunicationTile.asciiDraw()),
	// so they are drawn with dx = 0 and only their row advanced; the frame, by
	// contrast, is drawn at the absolute columns asciiFrameColumns() resolved
	// to. The child-stacking stays here, in the tile, not in the frame block:
	// the pixel drawU() likewise stacks children itself below the header
	// Component rather than nesting them inside it.
	@Override
	public void asciiDraw(InfinitePlan plan) {
		final int[] cols = asciiFrameColumns();
		final int left = cols[0];
		final int width = cols[1] - cols[0] + 1;

		// The frame draws itself (border, sides, footer, and title tab) at its
		// absolute left column; the same instance is reused just below to know
		// where the body starts, rather than re-deriving that separately.
		final AGroupFrame frame = new AGroupFrame(asciiDimension(), asciiTitle(), asciiFrameHasTab());
		frame.asciiDraw(plan.move(left, 0));

		// Children stacked in the body, below the header. A normal child is
		// drawn at dx = 0 (it re-derives its own absolute columns, only the row
		// matters here); an ElseTile is the exception — its divider spans the
		// whole frame, whose columns only this parent knows, so the parent draws
		// it via an AElseSeparator (the counterpart of drawAllElses(), §26) at the
		// frame's absolute left column and full width. The ElseTile still owns its
		// own row height via asciiDimension().
		int y = frame.getBodyRowOffset();
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile)
				new AElseSeparator(width, ((ElseTile) tile).asciiLabel()).asciiDraw(plan.move(left, y));
			else
				tile.asciiDraw(plan.move(0, y));
			y += tile.asciiDimension().getHeight();
		}
	}

	// The group's title as a single flat line: for a plain "group"/partition
	// it is just the comment, otherwise "title comment" — mirroring how the
	// pixel `display` field is built in the constructor. Multi-line titles are
	// flattened for now, the same simplification message labels used before
	// Display became an AsciiBlock (§18); good enough for the single-word
	// partition/group titles the current tests use.
	protected String asciiTitle() {
		final StringBuilder sb = new StringBuilder();
		for (CharSequence cs : display) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(cs);
		}
		return sb.toString();
	}

	public Real getMinX() {
		return min.addFixed(-EXTERNAL_MARGINX1 - getNotesWidth(getStringBounder(), NotePosition.LEFT));
	}

	public Real getMaxX() {
		return max.addFixed(EXTERNAL_MARGINX2 + getNotesWidth(getStringBounder(), NotePosition.RIGHT));
	}

	// Notes attached to the group ("note left"/"note right" just after the end
	// keyword) are drawn like in Puma: at the top corner of the group frame, on
	// the requested side (see GroupingGraphicalElementHeader)
	private void drawNotes(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		double xRight = max.getCurrentValue();
		for (Component note : getNoteComponents(NotePosition.RIGHT)) {
			final XDimension2D dimNote = note.getPreferredDimension(stringBounder);
			note.drawU(ug.apply(UTranslate.dx(xRight)), Area.create(dimNote.getWidth(), dimNote.getHeight()),
					(Context2D) ug);
			xRight += dimNote.getWidth();
		}

		double xLeft = min.getCurrentValue();
		for (Component note : getNoteComponents(NotePosition.LEFT)) {
			final XDimension2D dimNote = note.getPreferredDimension(stringBounder);
			note.drawU(ug.apply(UTranslate.dx(xLeft - dimNote.getWidth())),
					Area.create(dimNote.getWidth(), dimNote.getHeight()), (Context2D) ug);
			xLeft -= dimNote.getWidth();
		}
	}

	private double getNotesWidth(StringBounder stringBounder, NotePosition position) {
		double result = 0;
		for (Component note : getNoteComponents(position))
			result += note.getPreferredDimension(stringBounder).getWidth();

		return result;
	}

	private List<Component> getNoteComponents(NotePosition position) {
		if (end == null)
			return Collections.emptyList();

		final List<Component> result = new ArrayList<>();
		for (Note noteOnMessage : end.getNoteOnMessages()) {
			// A "note left" is drawn on the left side of the frame, everything
			// else (right, or a position that defaulted to right) on the right
			final boolean isLeft = noteOnMessage.getPosition() == NotePosition.LEFT;
			if (isLeft != (position == NotePosition.LEFT))
				continue;

			final ISkinParam sk = noteOnMessage.getSkinParamBackcolored(skinParam);
			result.add(skin.createComponentNote(noteOnMessage.getUsedStyles(),
					noteOnMessage.getNoteStyle().getNoteComponentType(), sk, noteOnMessage.getDisplay(),
					noteOnMessage.getColors()));
		}
		return result;
	}

	public static TimeHook fillPositionelTiles(StringBounder stringBounder, TimeHook y, List<Tile> tiles,
			final List<CommonTile> local, List<CommonTile> full) {
		final double startY = y.getValue();
		for (Tile tile : mergeParallel(stringBounder, tiles)) {
			tile.callbackY(y);
			local.add((CommonTile) tile);
			full.add((CommonTile) tile);
			if (tile instanceof GroupingTile) {
				final GroupingTile groupingTile = (GroupingTile) tile;
				fillPositionalSubGroupTiles(stringBounder, y, full, groupingTile);
			}
			if (tile instanceof TileParallel) {
				final TileParallel tileParallel = (TileParallel) tile;
				fillPositionalParallelTiles(stringBounder, y, full, tileParallel);
			}
			y = new TimeHook(y.getValue() + tile.getPreferredHeight());
		}
		if (YGauge.USE_ME) {
			// callbackY() above ignores the accumulated `y` entirely under
			// USE_ME (substitutes each tile's own gauge min instead -- see
			// CommonTile.callbackY), so the summation loop's ONLY remaining
			// purpose is computing the returned finalY (total document/group
			// height). But mergeParallel() is a no-op under USE_ME, so that
			// summation still double-counts every & member's height, exactly
			// like the bodyHeight bug (see YGAUGE.md) -- just at whatever
			// level calls this method (PlayingSpace at the top, or a
			// GroupingTile's own body). Recompute it correctly here with the
			// same clustering used for bodyHeight, without touching the loop
			// above (its side effects -- callbackY, local/full population,
			// recursion -- must run per INDIVIDUAL flat tile regardless).
			double clusteredHeight = 0;
			for (Tile tile : mergeParallelCore(stringBounder, tiles))
				clusteredHeight += tile.getPreferredHeight();
			return new TimeHook(startY + clusteredHeight);
		}
		return y;

	}

	private static void fillPositionalSubGroupTiles(StringBounder stringBounder, TimeHook y, List<CommonTile> full,
			GroupingTile groupingTile) {
		final double headerHeight = groupingTile.getHeaderHeight(stringBounder);
		final ArrayList<CommonTile> local2 = new ArrayList<>();
		fillPositionelTiles(stringBounder, new TimeHook(y.getValue() + headerHeight), groupingTile.tiles, local2, full);
	}

	private static void fillPositionalParallelTiles(StringBounder stringBounder, TimeHook yArg, List<CommonTile> full,
			TileParallel tileParallel) {
		final double yPointAll = tileParallel.getContactPointRelative();
		for (Tile tile : tileParallel.getTiles()) {

			final double yPoint = tile.getContactPointRelative();
			final double adjustment = yPointAll - yPoint;
			TimeHook yAdjusted = new TimeHook(yArg.getValue() + adjustment);

			tile.callbackY(yAdjusted);
			full.add((CommonTile) tile);

			if (tile instanceof GroupingTile)
				fillPositionalSubGroupTiles(stringBounder, yAdjusted, full, (GroupingTile) tile);
		}
	}

	private double getHeaderHeight(StringBounder stringBounder) {
		return getPreferredDimensionIfEmpty(stringBounder).getHeight() + 10;
	}

	private static List<Tile> mergeParallel(StringBounder stringBounder, List<Tile> tiles) {
		if (YGauge.USE_ME)
			return tiles;

		return mergeParallelCore(stringBounder, tiles);
	}

	// The actual clustering logic, unconditional. Used directly (not through
	// mergeParallel) wherever a legacy-shaped, & -aware view of the tiles is
	// needed purely for computation (e.g. bodyHeight) even under USE_ME, where
	// Y positioning itself no longer goes through this merge.
	private static List<Tile> mergeParallelCore(StringBounder stringBounder, List<Tile> tiles) {
		TileParallel pending = null;
		tiles = removeEmptyCloseToParallel(tiles);
		final List<Tile> result = new ArrayList<>();
		for (Tile tile : tiles) {
			if (!isParallel(tile) || result.size() == 0) {
				result.add(tile);
				if (tile instanceof LifeEventTile == false)
					pending = null;
			} else if (pending == null) {
				pending = new TileParallel(stringBounder, null);
				moveRecentParallelTilesToPending(result, pending);
				pending.add(tile);
				result.add(pending);
			} else {
				moveRecentParallelTilesToPending(result, pending);
				pending.add(tile);
			}
		}
		return result;
	}

	private static void moveRecentParallelTilesToPending(List<Tile> result, TileParallel pending) {
		if (result.size() == 0)
			return;

		int capture = 1;
		while (result.get(result.size() - capture) instanceof LifeEventTile)
			capture++;

		if (result.get(result.size() - capture) == pending)
			capture--;

		for (int i = result.size() - capture; i < result.size(); i++)
			pending.add(result.get(i));

		for (int i = 1; i <= capture; i++)
			result.remove(result.size() - 1);
	}

	private static List<Tile> removeEmptyCloseToParallel(List<Tile> tiles) {
		final List<Tile> result = new ArrayList<>();
		for (Tile tile : tiles) {
			if (isParallel(tile))
				removeHeadEmpty(result);

			result.add(tile);
		}
		return result;

	}

	private static void removeHeadEmpty(List<Tile> tiles) {
		while (tiles.size() > 0 && tiles.get(tiles.size() - 1) instanceof EmptyTile)
			tiles.remove(tiles.size() - 1);

	}

	public static boolean isParallel(Tile tile) {
		return tile instanceof TileParallel == false && tile.getEvent().isParallel();
	}

	void addNewpageTiles(List<NewpageTile> newpages) {
		for (Tile tile : tiles) {
			if (tile instanceof GroupingTile)
				((GroupingTile) tile).addNewpageTiles(newpages);

			if (tile instanceof NewpageTile)
				newpages.add((NewpageTile) tile);
		}
	}
	
	public GroupingStart getGroupingStart() {
		return start;
	}

	public TileArguments getTileArguments() {
		return tileArguments;
	}

	public ISkinParam getSkinParam() {
		return skinParam;
	}



}
