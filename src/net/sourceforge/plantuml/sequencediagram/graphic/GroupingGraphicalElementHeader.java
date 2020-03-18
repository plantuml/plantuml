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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class GroupingGraphicalElementHeader extends GroupingGraphicalElement {

	private final Component comp;
	private double endY;
	private final boolean isParallel;
	private final List<Component> notes = new ArrayList<Component>();

	public GroupingGraphicalElementHeader(double currentY, Component comp, InGroupableList inGroupableList,
			boolean isParallel) {
		super(currentY, inGroupableList);
		this.comp = comp;
		this.isParallel = isParallel;
	}

	@Override
	public String toString() {
		return super.toString() + " " + (getInGroupableList() == null ? "no" : getInGroupableList().toString());
	}

	@Override
	final public double getPreferredWidth(StringBounder stringBounder) {
		double width = comp.getPreferredWidth(stringBounder);
		for (Component note : notes) {
			final Dimension2D dimNote = note.getPreferredDimension(stringBounder);
			width += dimNote.getWidth();
		}
		return width + 5;
	}

	@Override
	final public double getPreferredHeight(StringBounder stringBounder) {
		return comp.getPreferredHeight(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		if (isParallel) {
			return;
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final double x1 = getInGroupableList().getMinX(stringBounder);
		final double x2 = getInGroupableList().getMaxX(stringBounder) - getInGroupableList().getHack2();
		ug = ug.apply(new UTranslate(x1, getStartingY()));
		double height = comp.getPreferredHeight(stringBounder);
		if (endY > 0) {
			height = endY - getStartingY();
		} else {
			// assert false;
			return;
		}
		final Dimension2D dim = new Dimension2DDouble(x2 - x1, height);
		comp.drawU(ug, new Area(dim), context);
		for (Component note : notes) {
			final Dimension2D dimNote = note.getPreferredDimension(stringBounder);
			note.drawU(ug.apply(UTranslate.dx(x2 - x1)), new Area(dimNote), context);
		}
	}

	public void setEndY(double y) {
		this.endY = y;
	}

	public void addNotes(StringBounder stringBounder, Collection<Component> notes) {
		for (Component note : notes) {
			this.notes.add(note);
			getInGroupableList().changeHack2(note.getPreferredWidth(stringBounder));
		}
	}

}
