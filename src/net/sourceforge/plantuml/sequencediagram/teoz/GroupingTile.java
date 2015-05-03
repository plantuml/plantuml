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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealMax;
import net.sourceforge.plantuml.real.RealMin;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Grouping;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GroupingTile implements Tile {

	private static final int MARGINX = 16;
	private static final int MARGINY = 10;
	private final List<Tile> tiles = new ArrayList<Tile>();
	private final RealMin min = new RealMin();
	private final RealMax max = new RealMax();
	private final GroupingStart start;

	// private final double marginX = 20;

	private final Skin skin;
	private final ISkinParam skinParam;
	private final Display display;

	private double bodyHeight;

	public Event getEvent() {
		return start;
	}

	public GroupingTile(Iterator<Event> it, GroupingStart start, TileArguments tileArgumentsBachColorChanged,
			TileArguments tileArgumentsOriginal) {
		final StringBounder stringBounder = tileArgumentsOriginal.getStringBounder();
		this.start = start;
		this.display = start.getTitle().equals("group") ? Display.create(start.getComment()) : Display.create(
				start.getTitle(), start.getComment());
		this.skin = tileArgumentsOriginal.getSkin();
		this.skinParam = tileArgumentsBachColorChanged.getSkinParam();
		// this.max = min.addAtLeast(dim1.getWidth());

		while (it.hasNext()) {
			final Event ev = it.next();
			System.err.println("GroupingTile::ev=" + ev);
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.END) {
				break;
			}
			final Tile tile = TileBuilder.buildOne(it, tileArgumentsOriginal, ev, this);
			if (tile != null) {
				tiles.add(tile);
				min.put(tile.getMinX(stringBounder).addFixed(-MARGINX));
				final Real m = tile.getMaxX(stringBounder);
				max.put(m == tileArgumentsOriginal.getOmega() ? m : m.addFixed(MARGINX));
				bodyHeight += tile.getPreferredHeight(stringBounder);
			}
		}
		final Dimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		final double width = dim1.getWidth();
		System.err.println("width=" + width);
		if (min.size() == 0) {
			min.put(tileArgumentsOriginal.getOrigin());
			max.put(tileArgumentsOriginal.getOmega());
		}
		// max.ensureBiggerThan(min.addFixed(width));
		this.max.ensureBiggerThan(getMinX(stringBounder).addFixed(width + 16));
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(ComponentType.GROUPING_HEADER, null, skinParam, display);
		return comp;
	}

	public Dimension2D getPreferredDimensionIfEmpty(StringBounder stringBounder) {
		return getComponent(stringBounder).getPreferredDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Component comp = getComponent(stringBounder);
		final Dimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		final Area area = new Area(max.getCurrentValue() - min.getCurrentValue(), bodyHeight + dim1.getHeight()
				+ MARGINY / 2);

		if (ug instanceof LiveBoxFinder == false) {
			comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), 0)), area, (Context2D) ug);
		}
		// ug.apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY)).draw(new URectangle(area.getDimensionToUse()));

		double h = dim1.getHeight() + MARGINY / 2;
		for (Tile tile : tiles) {
			ug.apply(new UTranslate(0, h)).draw(tile);
			h += tile.getPreferredHeight(stringBounder);
		}

	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Dimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		return dim1.getHeight() + bodyHeight + MARGINY;
	}

	public void addConstraints(StringBounder stringBounder) {
		for (Tile tile : tiles) {
			tile.addConstraints(stringBounder);
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		return min;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max;
	}
}
