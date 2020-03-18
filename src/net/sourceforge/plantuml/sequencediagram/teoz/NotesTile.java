/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class NotesTile extends AbstractTile implements Tile {

	private final LivingSpaces livingSpaces;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Notes notes;

	public Event getEvent() {
		return notes;
	}

	public NotesTile(LivingSpaces livingSpaces, Notes notes, Rose skin, ISkinParam skinParam) {
		this.livingSpaces = livingSpaces;
		this.notes = notes;
		this.skin = skin;
		this.skinParam = skinParam;
	}

	private Component getComponent(StringBounder stringBounder, Note note) {
		final Component comp = skin.createComponent(note.getUsedStyles(), getNoteComponentType(note.getNoteStyle()),
				null, note.getSkinParamBackcolored(skinParam), note.getStrings());
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

		for (Note note : notes) {
			final Component comp = getComponent(stringBounder, note);
			final Dimension2D dim = comp.getPreferredDimension(stringBounder);
			final double x = getX(stringBounder, note).getCurrentValue();
			final Area area = new Area(getUsedWidth(stringBounder, note), dim.getHeight());

			final UGraphic ug2 = ug.apply(UTranslate.dx(x));
			comp.drawU(ug2, area, (Context2D) ug2);
		}
	}

	private double getUsedWidth(StringBounder stringBounder, Note note) {
		final Component comp = getComponent(stringBounder, note);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final double width = dim.getWidth();
		return width;
	}

	private Real getXcenter(StringBounder stringBounder, Note note) {
		final LivingSpace livingSpace1 = livingSpaces.get(note.getParticipant());
		return livingSpace1.getPosC(stringBounder);
	}

	private Real getX(StringBounder stringBounder, Note note) {
		final LivingSpace livingSpace1 = livingSpaces.get(note.getParticipant());
		final NotePosition position = note.getPosition();
		final double width = getUsedWidth(stringBounder, note);
		if (position == NotePosition.LEFT) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width);
		} else if (position == NotePosition.RIGHT) {
			final int level = livingSpace1.getLevelAt(this, EventsHistoryMode.IGNORE_FUTURE_DEACTIVATE);
			final double dx = level * CommunicationTile.LIVE_DELTA_SIZE;
			return livingSpace1.getPosC(stringBounder).addFixed(dx);
		} else if (position == NotePosition.OVER_SEVERAL) {
			final LivingSpace livingSpace2 = livingSpaces.get(note.getParticipant2());
			final Real x1 = livingSpace1.getPosC(stringBounder);
			final Real x2 = livingSpace2.getPosC(stringBounder);
			return RealUtils.middle(x1, x2).addFixed(-width / 2);
		} else if (position == NotePosition.OVER) {
			return livingSpace1.getPosC(stringBounder).addFixed(-width / 2);
		} else {
			throw new UnsupportedOperationException(position.toString());
		}
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		double result = 0;
		for (Note note : notes) {
			final Component comp = getComponent(stringBounder, note);
			final Dimension2D dim = comp.getPreferredDimension(stringBounder);
			result = Math.max(result, dim.getHeight());
		}
		return result;
	}

	public void addConstraints(StringBounder stringBounder) {
		final List<Note> all = notes.asList();
		for (int i = 0; i < all.size() - 1; i++) {
			for (int j = i + 1; j < all.size(); j++) {
				final double center1 = getXcenter(stringBounder, all.get(i)).getCurrentValue();
				final double center2 = getXcenter(stringBounder, all.get(j)).getCurrentValue();
				if (center2 > center1) {
					final Real point1b = getX2(stringBounder, all.get(i));
					final Real point2 = getX(stringBounder, all.get(j));
					point2.ensureBiggerThan(point1b);
				} else {
					final Real point1 = getX(stringBounder, all.get(i));
					final Real point2b = getX2(stringBounder, all.get(j));
					point1.ensureBiggerThan(point2b);
				}
			}
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		final List<Real> reals = new ArrayList<Real>();
		for (Note note : notes) {
			reals.add(getX(stringBounder, note));
		}
		return RealUtils.min(reals);
	}

	private Real getX2(StringBounder stringBounder, Note note) {
		return getX(stringBounder, note).addFixed(getUsedWidth(stringBounder, note));
	}

	public Real getMaxX(StringBounder stringBounder) {
		final List<Real> reals = new ArrayList<Real>();
		for (Note note : notes) {
			reals.add(getX2(stringBounder, note));
		}
		return RealUtils.max(reals);
	}

}
