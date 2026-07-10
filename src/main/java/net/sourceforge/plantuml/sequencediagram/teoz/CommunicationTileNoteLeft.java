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

public class CommunicationTileNoteLeft extends AbstractTile {

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

	@Override
	public double getContactPointRelative() {
		return tile.getContactPointRelative();
	}

	public CommunicationTileNoteLeft(Tile tile, AbstractMessage message, Rose skin, ISkinParam skinParam,
			LivingSpace livingSpace, Note noteOnMessage, YGauge currentY) {
		super(((AbstractTile) tile).getStringBounder(), currentY);
		this.tile = tile;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.noteOnMessage = noteOnMessage;
		this.livingSpace = livingSpace;
		// See CommunicationTileNoteRight for why contact/origin are propagated
		// and max is built with addAtLeast rather than YGauge.create()
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
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		return livingSpace.getPosC(stringBounder).addFixed(-dim.getWidth());
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(dim.getWidth(), dim.getHeight());
		((UDrawable) tile).drawU(ug);
		final Real p = getNotePosition(stringBounder);
		if (YGauge.USE_ME)
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
	@Override
	public void asciiAddConstraints() {
		tile.asciiAddConstraints();
	}

	// Unlike getAsciiMaxX() below, this one no longer just forwards to the
	// wrapped message: a partition's frame needs to widen to fit an attached
	// left note (ASCIIVERSE.md §31 follow-up — "notes fall inside the frame",
	// not past it, matching the pixel rendering), and the frame only ever asks
	// its children's own getAsciiMinX()/getAsciiMaxX() to know how far they
	// reach (GroupingTile.asciiChildrenMin()). The note box's own width still
	// isn't reserved on the ASCII column solver itself (§14) — it can still
	// overlap a participant further left — only this tile's own reported
	// extent grows, the same column arithmetic asciiDraw() below already uses
	// to place the box (sourceColumn - width - 1).
	@Override
	public Real getAsciiMinX() {
		final int boxWidth = new ANote(asciiNoteText()).marginLR(2, 2).asciiDimension().getWidth();
		final Real noteMin = livingSpace.getAsciiLifeColumn().addFixed(-(boxWidth + 1));
		final Real tileMin = tile.getAsciiMinX();
		if (tileMin == null)
			return noteMin;

		return RealUtils.min(java.util.Arrays.asList(tileMin, noteMin));
	}

	@Override
	public Real getAsciiMaxX() {
		return tile.getAsciiMaxX();
	}

	// ASCII counterpart of getPreferredHeight()/asciiDraw(): same reasoning as
	// CommunicationTileNoteRight.asciiDimension() (ASCIIVERSE.md §19–§21) — the Y
	// footprint is Math.max(inner message's height, note box's height). No
	// try/catch around tile.asciiDimension(): an inner tile with no ASCII
	// support crashes here rather than silently reporting a made-up height
	// (§21). Width still just forwards the inner tile's, for the same reason
	// as CommunicationTileNoteRight: the note box's width isn't reserved on
	// the ASCII column solver (§14), so reporting a bigger one here wouldn't
	// be acted on.
	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D tileDim = tile.asciiDimension();
		final int tileWidth = tileDim.getWidth();
		final int tileHeight = tileDim.getHeight();

		final int noteHeight = new ANote(asciiNoteText()).asciiDimension().getHeight();
		return new ADimension2D(tileWidth, Math.max(tileHeight, noteHeight));
	}

	// ASCII counterpart of drawU(): draw the inner message first, then the
	// note itself as a proper folded-corner box (InfinitePlan.createNoteBox(),
	// §16/§18 in ASCIIVERSE.md), ending just before the source's lifeline
	// column. Real multi-line notes now draw correctly top-to-bottom (§18),
	// and the row this decorator needs is correctly reported by
	// asciiDimension() above (§19). Known gap, unchanged: if the column is too
	// small (e.g. the leftmost participant), the note box can land at a
	// negative x, which InfinitePlan does not yet support (see ASCIIVERSE.md).
	@Override
	public void asciiDraw(InfinitePlan plan) {
		tile.asciiDraw(plan);

		final AsciiBlock noteText = asciiNoteText();
		final AsciiBlock noteBox = plan.createNoteBox(noteText).marginLR(2, 2);
		final int sourceColumn = (int) livingSpace.getAsciiLifeColumn().getCurrentValue();
		final int width = noteBox.asciiDimension().getWidth();
		final int left = sourceColumn - width - 1;
		noteBox.asciiDraw(plan.move(left, 0));
	}

	private AsciiBlock asciiNoteText() {
		return noteOnMessage.getDisplay().marginLR(1, 3);
	}

	public Real getMinX() {
		return getNotePosition(getStringBounder());
	}

	public Real getMaxX() {
		return tile.getMaxX();
	}

}
