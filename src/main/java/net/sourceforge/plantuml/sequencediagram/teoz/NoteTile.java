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
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;

public class NoteTile extends AbstractTile implements Tile {

	private final LivingSpace livingSpace1;
	private final LivingSpace livingSpace2;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Note note;
	private final YGauge yGauge;

	public Event getEvent() {
		return note;
	}

	@Override
	public double getContactPointRelative() {
		return getComponent(getStringBounder()).getPreferredHeight(getStringBounder()) / 2;
	}

	public NoteTile(StringBounder stringBounder, LivingSpace livingSpace1, LivingSpace livingSpace2, Note note,
			Rose skin, ISkinParam skinParam, YGauge currentY) {
		super(stringBounder, currentY);
		this.livingSpace1 = livingSpace1;
		this.livingSpace2 = livingSpace2;
		this.note = note;
		this.skin = skin;
		this.skinParam = skinParam;
		this.yGauge = YGauge.create(currentY.getMax(), getPreferredHeight());
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponentNote(note.getUsedStyles(), getNoteComponentType(note.getNoteStyle()),
				note.getSkinParamBackcolored(skinParam), note.getDisplay(), note.getColors(), note.getPosition());
		return comp;
	}

	protected static ComponentType getNoteComponentType(NoteStyle noteStyle) {
		if (noteStyle == NoteStyle.HEXAGONAL)
			return ComponentType.NOTE_HEXAGONAL;

		if (noteStyle == NoteStyle.BOX)
			return ComponentType.NOTE_BOX;

		return ComponentType.NOTE;
	}

	public void drawU(UGraphic ug) {
		if (YGauge.USE_ME)
			ug = ug.apply(UTranslate.dy(getYGauge().getMin().getCurrentValue()));
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final double x = getX(stringBounder).getCurrentValue();
		final Area area = Area.create(getUsedWidth(stringBounder), dim.getHeight());

		ug = ug.apply(UTranslate.dx(x));
		comp.drawU(ug, area, (Context2D) ug);
	}

	private double getUsedWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final XDimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final double x1 = livingSpace1.getPosB(stringBounder).getCurrentValue();
			final double x2 = livingSpace2.getPosD(stringBounder).getCurrentValue();
			final double w = x2 - x1;
			if (width < w)
				return w;

		}
		return width;
	}

	private Real getX(StringBounder stringBounder) {
		final NotePosition position = note.getPosition();
		final double width = getUsedWidth(stringBounder);
		if (position == NotePosition.LEFT) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width);
		} else if (position == NotePosition.RIGHT) {
			final int level = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final double dx = level * CommunicationTile.LIVE_DELTA_SIZE;
			return livingSpace1.getPosC(stringBounder).addFixed(dx);
		} else if (position == NotePosition.OVER_SEVERAL) {
			final Real x1 = livingSpace1.getPosC(stringBounder);
			final Real x2 = livingSpace2.getPosC(stringBounder);
			return RealUtils.middle(x1, x2).addFixed(-width / 2);
		} else if (position == NotePosition.OVER) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width / 2);
		} else {
			throw new UnsupportedOperationException(position.toString());
		}
	}

	@Override
	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final XDimension2D dim = comp.getPreferredDimension(getStringBounder());
		return dim.getHeight();
	}

	public void addConstraints() {
		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double width = dim.getWidth();
	}

	@Override
	public Real getMinX() {
		final Real result = getX(getStringBounder());
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final Real x1 = livingSpace1.getPosB(getStringBounder());
			return RealUtils.min(result, x1);
		}
		return result;
	}

	@Override
	public Real getMaxX() {
		final Real result = getX(getStringBounder()).addFixed(getUsedWidth(getStringBounder()));
		if (note.getPosition() == NotePosition.OVER_SEVERAL) {
			final Real x2 = livingSpace2.getPosD(getStringBounder());
			return RealUtils.max(result, x2);
		}
		return result;
	}

}
