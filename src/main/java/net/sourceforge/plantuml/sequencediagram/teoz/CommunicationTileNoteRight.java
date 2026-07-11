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
import net.sourceforge.plantuml.asciiverse.ANote;
import net.sourceforge.plantuml.asciiverse.AsciiBlock;
import net.sourceforge.plantuml.asciiverse.AsciiBlockMarginLR;
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class CommunicationTileNoteRight extends AbstractTile {

	private final Tile tile;
	private final AbstractMessage message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final LivingSpace livingSpace;
	private final Note noteOnMessage;
	private final YGauge yGauge;

	public Event getEvent() {
		return message;
	}

	private boolean isCreate() {
		return message.isCreate();
	}

	@Override
	public double getContactPointRelative() {
		return tile.getContactPointRelative();
	}

	public CommunicationTileNoteRight(Tile tile, AbstractMessage message, Rose skin, ISkinParam skinParam,
			LivingSpace livingSpace, Note noteOnMessage, YGauge currentY) {
		super(((AbstractTile) tile).getStringBounder(), currentY);
		this.tile = tile;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.noteOnMessage = noteOnMessage;
		this.livingSpace = livingSpace;
		// Propagate contact/origin from the wrapped tile so a following &
		// message can still find and align with the SAME contact line through
		// the note wrapper (min unchanged: the note doesn't move the arrow;
		// max anchored via addAtLeast per the CHAIN ANCHORING invariant in
		// YGauge, so this wrapper is itself safe to chain onto)
		final YGauge innerGauge = tile.getYGauge();
		this.yGauge = new YGauge(innerGauge.getMin(), innerGauge.getMin().addAtLeast(getPreferredHeight()),
				innerGauge.getContact(), innerGauge.getOrigin());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	@Override
	final protected void callbackY_internal(TimeHook y) {
		super.callbackY_internal(y);
		tile.callbackY(y);
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponentNote(noteOnMessage.getUsedStyles(),
				NoteTile.getNoteComponentType(noteOnMessage.getNoteStyle()),
				noteOnMessage.getSkinParamBackcolored(skinParam), noteOnMessage.getDisplay(),
				noteOnMessage.getColors());
		return comp;
	}

	private Real getNotePosition(StringBounder stringBounder) {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		if (isCreate()) {
			return livingSpace.getPosD(stringBounder);
		}
		final int level = livingSpace.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
		return livingSpace.getPosC(stringBounder).addFixed(level * CommunicationTile.LIVE_DELTA_SIZE);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(dim.getWidth(), dim.getHeight());
		// The wrapped tile self-translates to its own gauge; the note's prologue is
		// applied AFTERWARDS, to the note only (the wrapper's min is identical to the
		// inner tile's, so both land on the same row -- the note never moves the arrow)
		((UDrawable) tile).drawU(ug);
		final Real p = getNotePosition(stringBounder);
		ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));

		comp.drawU(ug.apply(UTranslate.dx(p.getCurrentValue())), area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return Math.max(tile.getPreferredHeight(), dim.getHeight());
	}

	public void addConstraints() {
		tile.addConstraints();
	}

	// ASCII counterpart: delegate the message's own constraint to the inner
	// tile, exactly like addConstraints() above delegates to tile.addConstraints().
	// The note's own width is not yet reserved on the ASCII column graph (see
	// ASCIIVERSE.md) — it can overlap a participant further right.
	@Override
	public void asciiAddConstraints() {
		tile.asciiAddConstraints();
	}

	// Delegate the range to the wrapped message, exactly like
	// asciiAddConstraints() delegates its constraint. The note box's own extra
	// width past the target column is not reflected here — same known gap as
	// everywhere else in this family (ASCIIVERSE.md): the note is not yet
	// reserved on the ASCII column solver.
	@Override
	public Real getAsciiMinX() {
		return tile.getAsciiMinX();
	}

	// Unlike getAsciiMinX() above, this one no longer just forwards to the
	// wrapped message: a partition's frame needs to widen to fit an attached
	// right note (ASCIIVERSE.md §31 follow-up — "notes fall inside the frame",
	// not past it, matching the pixel rendering), and the frame only ever asks
	// its children's own getAsciiMinX()/getAsciiMaxX() to know how far they
	// reach (GroupingTile.asciiChildrenMax()). The note box's own width still
	// isn't reserved on the ASCII column solver itself (§14) — it can still
	// overlap a participant further right — only this tile's own reported
	// extent grows, the same column arithmetic asciiDraw() below already uses
	// to place the box (targetColumn + 2, then the box's own width).
	@Override
	public Real getAsciiMaxX() {
		final int boxWidth = new ANote(asciiNoteText()).marginLR(2, 2).asciiDimension().getWidth();
		final Real noteMax = livingSpace.getAsciiLifeColumn().addFixed(boxWidth + 1);
		final Real tileMax = tile.getAsciiMaxX();
		if (tileMax == null)
			return noteMax;

		return RealUtils.max(java.util.Arrays.asList(tileMax, noteMax));
	}

	// ASCII counterpart of getPreferredHeight()/asciiDraw(): the Y footprint
	// this decorator needs is whichever is taller, the inner message (label
	// rows + arrow + blank, see CommunicationTile.asciiDimension(),
	// ASCIIVERSE.md §19) or the note box itself (a throwaway `new
	// ANote(text).asciiDimension()`, §18–§20: ANote computes its own size from
	// the text alone, no plan needed just to ask) — mirroring how
	// getPreferredHeight() above takes Math.max(tile's height, the pixel
	// note's height). No try/catch around tile.asciiDimension(): an inner
	// tile with no ASCII support crashes here rather than silently reporting
	// a made-up height (ASCIIVERSE.md §21 — the same "crash, don't mask it"
	// policy the orchestrator's asciiDraw() loop follows). Width is inherited
	// straight from the inner tile: unlike height, the note box's own width
	// still isn't reserved anywhere on the ASCII column solver (§14), so
	// reporting it here wouldn't yet be acted upon — same known gap as
	// before, not fixed by this change.
	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D tileDim = tile.asciiDimension();
		final int tileWidth = tileDim.getWidth();
		final int tileHeight = tileDim.getHeight();

		final int noteHeight = new ANote(asciiNoteText()).asciiDimension().getHeight();
		return new ADimension2D(tileWidth, Math.max(tileHeight, noteHeight));
	}

	// ASCII counterpart of drawU(): draw the inner message first (exactly
	// like drawU()'s ((UDrawable) tile).drawU(ug)), then the note itself as a
	// proper folded-corner box (InfinitePlan.createNoteBox(), §16/§18 in
	// ASCIIVERSE.md), placed right after the target's lifeline column. Real
	// multi-line notes now draw correctly top-to-bottom (§18), and the row
	// this decorator needs is correctly reported by asciiDimension() above
	// (§19) — but see that method's note: only the note box's height feeds
	// back into the Y-axis, not its width.
	@Override
	public void asciiDraw(InfinitePlan plan) {
		tile.asciiDraw(plan);

		final AsciiBlock noteText = asciiNoteText();
		final AsciiBlock noteBox = plan.createNoteBox(noteText).marginLR(2, 2);
		final int targetColumn = (int) livingSpace.getAsciiLifeColumn().getCurrentValue();
		final int left = targetColumn + 2;
		noteBox.asciiDraw(plan.move(left, 0));
	}

	private AsciiBlock asciiNoteText() {
		return noteOnMessage.getDisplay().marginLR(1, 3);
	}

	public Real getMinX() {
		return tile.getMinX();
	}

	public Real getMaxX() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return getNotePosition(getStringBounder()).addFixed(dim.getWidth());
	}

}
