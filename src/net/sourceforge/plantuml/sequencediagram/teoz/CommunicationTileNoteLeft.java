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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.AbstractMessage;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
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
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
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
		final Component comp = skin.createComponentNote(noteOnMessage.getUsedStyles(), NoteTile.getNoteComponentType(noteOnMessage.getNoteStyle()),
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

}
