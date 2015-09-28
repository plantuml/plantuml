/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.Notes;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class NotesTile implements Tile {

	private final List<LivingSpace> noteLivingSpaces;
	private final Skin skin;
	private final ISkinParam skinParam;
	private final Notes notes;

	public Event getEvent() {
		return notes;
	}

	public NotesTile(List<LivingSpace> noteLivingSpaces, Notes notes, Skin skin, ISkinParam skinParam) {
		this.noteLivingSpaces = noteLivingSpaces;
		this.notes = notes;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	private Component getComponent(StringBounder stringBounder, int i) {
		final Note note = notes.get(i);
		final Component comp = skin.createComponent(getNoteComponentType(note.getStyle()), null,
				note.getSkinParamBackcolored(skinParam), note.getStrings());
		return comp;
	}

	private ComponentType getNoteComponentType(NoteStyle noteStyle) {
		if (noteStyle == NoteStyle.HEXAGONAL) {
			return ComponentType.NOTE_HEXAGONAL;
		}
		if (noteStyle == NoteStyle.BOX) {
			return ComponentType.NOTE_BOX;
		}
		return ComponentType.NOTE;
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		for (int i = 0; i < noteLivingSpaces.size(); i++) {
			final Component comp = getComponent(stringBounder, i);
			final Dimension2D dim = comp.getPreferredDimension(stringBounder);
			final double x = getX(stringBounder, i).getCurrentValue();
			final Area area = new Area(getUsedWidth(stringBounder, i), dim.getHeight());

			final UGraphic ug2 = ug.apply(new UTranslate(x, 0));
			comp.drawU(ug2, area, (Context2D) ug2);
		}

		// final Component comp = getComponent(stringBounder);
		// final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		// final double x = getX(stringBounder).getCurrentValue();
		// final Area area = new Area(getUsedWidth(stringBounder), dim.getHeight());
		//
		// ug = ug.apply(new UTranslate(x, 0));
		// comp.drawU(ug, area, (Context2D) ug);
	}

	private double getUsedWidth(StringBounder stringBounder, int i) {
		final Component comp = getComponent(stringBounder, i);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();
		// if (note.getPosition() == NotePosition.OVER_SEVERAL) {
		// final double x1 = livingSpace1.getPosB().getCurrentValue();
		// final double x2 = livingSpace2.getPosD(stringBounder).getCurrentValue();
		// final double w = x2 - x1;
		// if (width < w) {
		// return w;
		// }
		// }
		return width;
	}

	private Real getX(StringBounder stringBounder, int i) {
		final Note note = notes.get(i);
		final LivingSpace livingSpace1 = noteLivingSpaces.get(i);
		final NotePosition position = note.getPosition();
		final double width = getUsedWidth(stringBounder, i);
		if (position == NotePosition.LEFT) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width);
		} else if (position == NotePosition.RIGHT) {
			final int level = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final double dx = level * CommunicationTile.LIVE_DELTA_SIZE;
			return livingSpace1.getPosC(stringBounder).addFixed(dx);
		} else if (position == NotePosition.OVER_SEVERAL) {
			// final Real x1 = livingSpace1.getPosC(stringBounder);
			// final Real x2 = livingSpace2.getPosC(stringBounder);
			// return RealUtils.middle(x1, x2).addFixed(-width / 2);
			throw new UnsupportedOperationException(position.toString());
		} else if (position == NotePosition.OVER) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width / 2);
		} else {
			throw new UnsupportedOperationException(position.toString());
		}
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		double result = 0;
		for (int i = 0; i < noteLivingSpaces.size(); i++) {
			final Component comp = getComponent(stringBounder, i);
			final Dimension2D dim = comp.getPreferredDimension(stringBounder);
			result = Math.max(result, dim.getHeight());
		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		for (int i = 0; i < noteLivingSpaces.size() - 1; i++) {
			for (int j = i + 1; j < noteLivingSpaces.size(); j++) {
				final Real point1 = getX2(stringBounder, i);
				final Real point2 = getX(stringBounder, j);
				point2.ensureBiggerThan(point1);
			}
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		final List<Real> reals = new ArrayList<Real>();
		for (int i = 0; i < noteLivingSpaces.size(); i++) {
			reals.add(getX(stringBounder, i));
		}
		return RealUtils.min(reals);
	}

	private Real getX2(StringBounder stringBounder, int i) {
		return getX(stringBounder, i).addFixed(getUsedWidth(stringBounder, i));
	}

	public Real getMaxX(StringBounder stringBounder) {
		final List<Real> reals = new ArrayList<Real>();
		for (int i = 0; i < noteLivingSpaces.size(); i++) {
			reals.add(getX2(stringBounder, i));
		}
		return RealUtils.max(reals);
	}

}
