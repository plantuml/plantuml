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
import net.sourceforge.plantuml.asciiverse.InfinitePlan;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class CommunicationTileSelfNoteLeft extends AbstractTile {

	private final CommunicationTileSelf tile;
	private final AbstractMessage message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Note noteOnMessage;
	private final YGauge yGauge;

	public Event getEvent() {
		return message;
	}

	@Override
	public double getContactPointRelative() {
		return tile.getContactPointRelative();
	}

	public CommunicationTileSelfNoteLeft(CommunicationTileSelf tile, AbstractMessage message, Rose skin, ISkinParam skinParam,
																			  Note noteOnMessage, YGauge currentY) {
		super(((AbstractTile) tile).getStringBounder(), currentY);
		this.tile = tile;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.noteOnMessage = noteOnMessage;
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
		final Component comp = skin.createComponentNote(noteOnMessage.getUsedStyles(), ComponentType.NOTE,
				noteOnMessage.getSkinParamBackcolored(skinParam), noteOnMessage.getDisplay(),
				noteOnMessage.getColors());
		return comp;
	}

	private Real getNotePosition(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		//return livingSpace.getPosC(stringBounder).addFixed(-dim.getWidth());
		return tile.getMinX().addFixed(-dim.getWidth());
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = Area.create(dim.getWidth(), dim.getHeight());
		// The wrapped tile self-translates to its own gauge; the note's prologue is
		// applied AFTERWARDS, to the note only (the wrapper's min is identical to the
		// inner tile's, so both land on the same row -- the note never moves the arrow)
		tile.drawU(ug);
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

	public Real getMinX() {
		return getNotePosition(getStringBounder());
	}

	public Real getMaxX() {
		return tile.getMaxX();
	}

	// ASCII counterpart: delegate the message's own constraint to the inner
	// tile, exactly like addConstraints() above delegates to
	// tile.addConstraints(). Same shape as CommunicationTileNoteLeft's
	// override, just wrapping a CommunicationTileSelf instead of a plain
	// CommunicationTile.
	@Override
	public void asciiAddConstraints() {
		tile.asciiAddConstraints();
	}

	// Unlike CommunicationTileNoteLeft, there is no separate LivingSpace field
	// here to anchor the note on: tile.getAsciiMinX() already IS the right
	// anchor, exactly mirroring how the pixel getNotePosition() above uses
	// tile.getMinX() directly rather than a livingSpace lookup (for the
	// common, non-reverse-defined case this is simply the lifeline column,
	// same as CommunicationTileSelf.getAsciiMinX()). The note box's own width
	// still isn't reserved on the ASCII column solver (ASCIIVERSE.md §14) --
	// same known gap as every other note decorator.
	@Override
	public Real getAsciiMinX() {
		final int boxWidth = new ANote(asciiNoteText()).marginLR(2, 2).asciiDimension().getWidth();
		final Real tileMin = tile.getAsciiMinX();
		final Real noteMin = tileMin.addFixed(-(boxWidth + 1));
		return RealUtils.min(java.util.Arrays.asList(tileMin, noteMin));
	}

	@Override
	public Real getAsciiMaxX() {
		return tile.getAsciiMaxX();
	}

	// ASCII counterpart of getPreferredHeight()/asciiDraw(): same reasoning as
	// CommunicationTileNoteLeft.asciiDimension() (ASCIIVERSE.md §19–§21) -- the
	// Y footprint is Math.max(inner self-message's height, note box's
	// height). No try/catch around tile.asciiDimension(): the inner
	// CommunicationTileSelf now has real ASCII support (§33), so this simply
	// reads its height back rather than guarding against a still-missing one.
	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D tileDim = tile.asciiDimension();
		final int noteHeight = new ANote(asciiNoteText()).asciiDimension().getHeight();
		return new ADimension2D(tileDim.getWidth(), Math.max(tileDim.getHeight(), noteHeight));
	}

	// ASCII counterpart of drawU(): draw the self-message loop first, then the
	// note itself as a proper folded-corner box (InfinitePlan.createNoteBox(),
	// ASCIIVERSE.md §16/§18), ending just before the anchor column -- the same
	// "draw the inner tile, then the note" order as CommunicationTileNoteLeft,
	// anchored on tile.getAsciiMinX() rather than a separate LivingSpace.
	@Override
	public void asciiDraw(InfinitePlan plan) {
		tile.asciiDraw(plan);

		final AsciiBlock noteText = asciiNoteText();
		final AsciiBlock noteBox = plan.createNoteBox(noteText).marginLR(2, 2);
		final int anchorColumn = (int) tile.getAsciiMinX().getCurrentValue();
		final int width = noteBox.asciiDimension().getWidth();
		final int left = anchorColumn - width - 1;
		noteBox.asciiDraw(plan.move(left, 0));
	}

	private AsciiBlock asciiNoteText() {
		return noteOnMessage.getDisplay().marginLR(1, 3);
	}

}
