/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
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

public class GroupingTile implements TileWithCallbackY {

	private static final int EXTERNAL_MARGINX1 = 3;
	private static final int EXTERNAL_MARGINX2 = 9;
	private static final int MARGINX = 16;
	private static final int MARGINY = 10;
	private final List<Tile> tiles = new ArrayList<Tile>();
	private final Real min;
	private final Real max;
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
		// this.skinParam = tileArgumentsOriginal.getSkinParam();
		this.skinParam = tileArgumentsBachColorChanged.getSkinParam();

		final List<Real> min2 = new ArrayList<Real>();
		final List<Real> max2 = new ArrayList<Real>();

		final List<Tile> allElses = new ArrayList<Tile>();
		while (it.hasNext()) {
			final Event ev = it.next();
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.END) {
				break;
			}
			for (Tile tile : TileBuilder.buildOne(it, tileArgumentsOriginal, ev, this)) {
				tiles.add(tile);
				bodyHeight += tile.getPreferredHeight(stringBounder);
				if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.ELSE) {
					allElses.add(tile);
					continue;
				}
				min2.add(tile.getMinX(stringBounder).addFixed(-MARGINX));
				final Real m = tile.getMaxX(stringBounder);
				// max2.add(m == tileArgumentsOriginal.getOmega() ? m : m.addFixed(MARGINX));
				max2.add(m.addFixed(MARGINX));
			}
		}
		final Dimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		final double width = dim1.getWidth();
		if (min2.size() == 0) {
			min2.add(tileArgumentsOriginal.getOrigin());
		}
		this.min = RealUtils.min(min2);
		for (Tile anElse : allElses) {
			max2.add(anElse.getMaxX(stringBounder));
		}
		max2.add(this.min.addFixed(width + 16));
		this.max = RealUtils.max(max2);

	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(ComponentType.GROUPING_HEADER, null, skinParam, display);
		return comp;
	}

	private Dimension2D getPreferredDimensionIfEmpty(StringBounder stringBounder) {
		return getComponent(stringBounder).getPreferredDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Component comp = getComponent(stringBounder);
		final Dimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		final Area area = new Area(max.getCurrentValue() - min.getCurrentValue(), getTotalHeight(stringBounder));

		if (ug instanceof LiveBoxFinder == false) {
			comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), 0)), area, (Context2D) ug);
			drawAllElses(ug);
		}
		// ug.apply(new UChangeBackColor(HtmlColorUtils.LIGHT_GRAY)).draw(new URectangle(area.getDimensionToUse()));

		double h = dim1.getHeight() + MARGINY / 2;
		for (Tile tile : tiles) {
			ug.apply(new UTranslate(0, h)).draw(tile);
			h += tile.getPreferredHeight(stringBounder);
		}

	}

	private double getTotalHeight(StringBounder stringBounder) {
		final Dimension2D dimIfEmpty = getPreferredDimensionIfEmpty(stringBounder);
		return bodyHeight + dimIfEmpty.getHeight() + MARGINY / 2;
	}

	private void drawAllElses(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double totalHeight = getTotalHeight(stringBounder);
		final double suppHeight = getPreferredDimensionIfEmpty(stringBounder).getHeight() + MARGINY / 2;
		final List<Double> ys = new ArrayList<Double>();
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				ys.add(elseTile.getCallbackY() - y + suppHeight);
			}
		}
		ys.add(totalHeight);
		int i = 0;
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final Component comp = elseTile.getComponent(stringBounder);
				final Area area = new Area(max.getCurrentValue() - min.getCurrentValue(), ys.get(i + 1) - ys.get(i));
				comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), ys.get(i))), area, (Context2D) ug);
				i++;
			}
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
		return min.addFixed(-EXTERNAL_MARGINX1);
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max.addFixed(EXTERNAL_MARGINX2);
	}

	private double y;

	public void callbackY(double y) {
		this.y = y;
	}

	public static double fillPositionelTiles(StringBounder stringBounder, double y, List<Tile> tiles,
			final List<YPositionedTile> positionedTiles) {
		double lastY = y;
		for (Tile tile : tiles) {
			if (tile.getEvent().isParallel()) {
				y = lastY;
			}
			positionedTiles.add(new YPositionedTile(tile, y));
			if (tile instanceof GroupingTile) {
				final GroupingTile groupingTile = (GroupingTile) tile;
				fillPositionelTiles(stringBounder, y, groupingTile.tiles, new ArrayList<YPositionedTile>());
			}
			lastY = y;
			y += tile.getPreferredHeight(stringBounder);
		}
		return y;

	}

	// public double getStartY() {
	// return y + MARGINY;
	// }
}
