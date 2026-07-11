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
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class CommunicationTileSelfNoteRight extends AbstractTile {

	private final CommunicationTileSelf tile;
	private final Message message;
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

	public CommunicationTileSelfNoteRight(CommunicationTileSelf tile, Message message, Rose skin, ISkinParam skinParam,
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
		return tile.getMaxX();
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
		return tile.getMinX();
	}

	public Real getMaxX() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return getNotePosition(getStringBounder()).addFixed(dim.getWidth());
	}

	// ASCII counterpart: delegate to the inner tile, same shape as
	// CommunicationTileSelfNoteLeft's override.
	@Override
	public void asciiAddConstraints() {
		tile.asciiAddConstraints();
	}

	@Override
	public Real getAsciiMinX() {
		return tile.getAsciiMinX();
	}

	// Mirrors CommunicationTileSelfNoteLeft.getAsciiMinX(): tile.getAsciiMaxX()
	// is already the right anchor (the pixel getNotePosition() above likewise
	// uses tile.getMaxX() directly, no separate LivingSpace lookup needed).
	// Note box width still not reserved on the ASCII column solver (§14), same
	// known gap as every other note decorator.
	@Override
	public Real getAsciiMaxX() {
		final int boxWidth = new ANote(asciiNoteText()).marginLR(2, 2).asciiDimension().getWidth();
		final Real tileMax = tile.getAsciiMaxX();
		final Real noteMax = tileMax.addFixed(boxWidth + 1);
		return RealUtils.max(java.util.Arrays.asList(tileMax, noteMax));
	}

	// Same reasoning as CommunicationTileSelfNoteLeft.asciiDimension().
	@Override
	public ADimension2D asciiDimension() {
		final ADimension2D tileDim = tile.asciiDimension();
		final int noteHeight = new ANote(asciiNoteText()).asciiDimension().getHeight();
		return new ADimension2D(tileDim.getWidth(), Math.max(tileDim.getHeight(), noteHeight));
	}

	// ASCII counterpart of drawU(): draw the self-message loop first, then the
	// note box just after the anchor column -- mirrors
	// CommunicationTileSelfNoteLeft.asciiDraw(), on the other side.
	@Override
	public void asciiDraw(InfinitePlan plan) {
		tile.asciiDraw(plan);

		final AsciiBlock noteText = asciiNoteText();
		final AsciiBlock noteBox = plan.createNoteBox(noteText).marginLR(2, 2);
		final int anchorColumn = (int) tile.getAsciiMaxX().getCurrentValue();
		final int left = anchorColumn + 2;
		noteBox.asciiDraw(plan.move(left, 0));
	}

	private AsciiBlock asciiNoteText() {
		return noteOnMessage.getDisplay().marginLR(1, 3);
	}

}
