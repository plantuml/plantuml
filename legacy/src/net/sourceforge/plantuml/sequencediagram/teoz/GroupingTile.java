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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
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
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class GroupingTile extends AbstractTile {

	public static final int EXTERNAL_MARGINX1 = 3;
	public static final int EXTERNAL_MARGINX2 = 9;
	private static final int MARGINX = 16;
	// private static final int MARGINY = 10;
	private static final int MARGINY_MAGIC = 20;
	private List<Tile> tiles = new ArrayList<>();
	private final Real min;
	private final Real max;
	private final GroupingStart start;
	private final YGauge yGauge;

	private final Rose skin;
	private final ISkinParam skinParam;
	private final Display display;

	private double bodyHeight;

	public Event getEvent() {
		return start;
	}

	@Override
	public double getContactPointRelative() {
		return 0;
	}

	@Override
	public YGauge getYGauge() {
		return yGauge;
	}

	public GroupingTile(Iterator<Event> it, GroupingStart start, TileArguments tileArgumentsBackColorChanged,
			TileArguments tileArgumentsOriginal, YGauge currentY) {
		super(tileArgumentsBackColorChanged.getStringBounder(), currentY);
		final Real firstY = currentY.getMax();
		final StringBounder stringBounder = tileArgumentsOriginal.getStringBounder();
		this.start = start;
		this.display = start.getTitle().equals("group") ? Display.create(start.getComment())
				: Display.create(start.getTitle(), start.getComment());
		this.skin = tileArgumentsOriginal.getSkin();
		// this.skinParam = tileArgumentsOriginal.getSkinParam();
		this.skinParam = tileArgumentsBackColorChanged.getSkinParam();

		final List<Real> min2 = new ArrayList<>();
		final List<Real> max2 = new ArrayList<>();

		final List<Tile> allElses = new ArrayList<>();
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);

		final double h = dim1.getHeight() + MARGINY_MAGIC / 2;
		currentY = YGauge.create(currentY.getMax().addAtLeast(h), 0);

		while (it.hasNext()) {
			final Event ev = it.next();
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.END)
				break;

			for (Tile tile : TileBuilder.buildOne(it, tileArgumentsOriginal, ev, this, currentY)) {
				tiles.add(tile);
				if (YGauge.USE_ME)
					currentY = tile.getYGauge();
			}

		}

		tiles = mergeParallel(getStringBounder(), tiles);

		for (Tile tile : tiles) {
			bodyHeight += tile.getPreferredHeight();
			final Event ev = tile.getEvent();
			if (ev instanceof GroupingLeaf && ((Grouping) ev).getType() == GroupingType.ELSE) {
				allElses.add(tile);
				continue;
			}
			min2.add(tile.getMinX().addFixed(-MARGINX));
			final Real m = tile.getMaxX();
			// max2.add(m == tileArgumentsOriginal.getOmega() ? m : m.addFixed(MARGINX));
			max2.add(m.addFixed(MARGINX));
		}
		final double width = dim1.getWidth();
		if (min2.size() == 0)
			min2.add(tileArgumentsOriginal.getXOrigin());

		this.min = RealUtils.min(min2);
		for (Tile anElse : allElses)
			max2.add(anElse.getMaxX());

		max2.add(this.min.addFixed(width + 16));
		this.max = RealUtils.max(max2);
		this.yGauge = YGauge.create(firstY, getPreferredHeight());

	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(start.getUsedStyles(), ComponentType.GROUPING_HEADER_TEOZ, null,
				skinParam, display);
		return comp;
	}

	private XDimension2D getPreferredDimensionIfEmpty(StringBounder stringBounder) {
		return getComponent(stringBounder).getPreferredDimension(stringBounder);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final Area area = Area.create(max.getCurrentValue() - min.getCurrentValue(), getTotalHeight(stringBounder));

		final Component comp = getComponent(stringBounder);
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);

		if (YGauge.USE_ME) {
			comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), getYGauge().getMin().getCurrentValue())), area,
					(Context2D) ug);
		} else {
			if (((Context2D) ug).isBackground()) {
				drawBackground(ug, area);
				return;
			}
			comp.drawU(ug.apply(UTranslate.dx(min.getCurrentValue())), area, (Context2D) ug);
			drawAllElses(ug);
		}

		double h = dim1.getHeight() + MARGINY_MAGIC / 2;
		for (Tile tile : tiles) {
			if (YGauge.USE_ME)
				((UDrawable) tile).drawU(ug);
			else
				((UDrawable) tile).drawU(ug.apply(UTranslate.dy(h)));
			final double preferredHeight = tile.getPreferredHeight();
			h += preferredHeight;
		}
	}

	private void drawBackground(UGraphic ug, Area area) {
		final Style style = start.getUsedStyles()[0];
		final HColor back = style.value(PName.BackGroundColor).asColor(skinParam.getIHtmlColorSet());
		final double round = style.value(PName.RoundCorner).asDouble();
		final XDimension2D dimensionToUse = area.getDimensionToUse();
		final Blotter blotter = new Blotter(dimensionToUse, back, round);

		for (Tile tile : tiles)
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final double ypos = elseTile.getTimeHook().getValue() - getTimeHook().getValue() + MARGINY_MAGIC / 2;
				blotter.addChange(ypos + 1, elseTile.getBackColorGeneral());
			}

		blotter.closeChanges();
		blotter.drawU(ug.apply(UTranslate.dx(min.getCurrentValue())));

		final StringBounder stringBounder = ug.getStringBounder();

		final XDimension2D dim1 = getPreferredDimensionIfEmpty(stringBounder);
		double h = dim1.getHeight() + MARGINY_MAGIC / 2;
		for (Tile tile : tiles) {
			if (YGauge.USE_ME)
				((UDrawable) tile).drawU(ug);
			else
				((UDrawable) tile).drawU(ug.apply(UTranslate.dy(h)));
			final double preferredHeight = tile.getPreferredHeight();
			h += preferredHeight;
		}

	}

	private double getTotalHeight(StringBounder stringBounder) {
		final XDimension2D dimIfEmpty = getPreferredDimensionIfEmpty(stringBounder);
		return bodyHeight + dimIfEmpty.getHeight() + MARGINY_MAGIC / 2;
	}

	private void drawAllElses(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final List<Double> ys = new ArrayList<>();
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final double ypos;
				if (YGauge.USE_ME)
					ypos = elseTile.getYGauge().getMin().getCurrentValue() - getYGauge().getMin().getCurrentValue()
							+ MARGINY_MAGIC / 2;
				else
					ypos = elseTile.getTimeHook().getValue() - getTimeHook().getValue() + MARGINY_MAGIC / 2;
				ys.add(ypos);
			}
		}
		final double totalHeight = getTotalHeight(stringBounder);
		ys.add(totalHeight);
		int i = 0;
		for (Tile tile : tiles) {
			if (tile instanceof ElseTile) {
				final ElseTile elseTile = (ElseTile) tile;
				final Component comp = elseTile.getComponent(stringBounder);
				final Area area = Area.create(max.getCurrentValue() - min.getCurrentValue(), ys.get(i + 1) - ys.get(i));
				comp.drawU(ug.apply(new UTranslate(min.getCurrentValue(), ys.get(i))), area, (Context2D) ug);
				i++;
			}
		}
	}

	@Override
	public double getPreferredHeight() {
		final XDimension2D dim1 = getPreferredDimensionIfEmpty(getStringBounder());
		return dim1.getHeight() + bodyHeight + MARGINY_MAGIC;
	}

	public void addConstraints() {
		for (Tile tile : tiles)
			tile.addConstraints();

	}

	public Real getMinX() {
		return min.addFixed(-EXTERNAL_MARGINX1);
	}

	public Real getMaxX() {
		return max.addFixed(EXTERNAL_MARGINX2);
	}

	public static TimeHook fillPositionelTiles(StringBounder stringBounder, TimeHook y, List<Tile> tiles,
			final List<CommonTile> local, List<CommonTile> full) {
		for (Tile tile : mergeParallel(stringBounder, tiles)) {
			tile.callbackY(y);
			local.add((CommonTile) tile);
			full.add((CommonTile) tile);
			if (tile instanceof GroupingTile) {
				final GroupingTile groupingTile = (GroupingTile) tile;
				final double headerHeight = groupingTile.getHeaderHeight(stringBounder);
				final ArrayList<CommonTile> local2 = new ArrayList<>();
				fillPositionelTiles(stringBounder, new TimeHook(y.getValue() + headerHeight), groupingTile.tiles,
						local2, full);
			}
			y = new TimeHook(y.getValue() + tile.getPreferredHeight());
		}
		return y;

	}

	private double getHeaderHeight(StringBounder stringBounder) {
		return getPreferredDimensionIfEmpty(stringBounder).getHeight() + 10;
	}

	private static List<Tile> mergeParallel(StringBounder stringBounder, List<Tile> tiles) {
		if (YGauge.USE_ME)
			return tiles;

		TileParallel pending = null;
		tiles = removeEmptyCloseToParallel(tiles);
		final List<Tile> result = new ArrayList<>();
		for (Tile tile : tiles) {
			if (result.size() > 0 && isParallel(tile)) {
				if (pending == null) {
					pending = new TileParallel(stringBounder, null);
					final Tile tmp = result.get(result.size() - 1);
					if (tmp instanceof LifeEventTile) {
						pending.add(result.get(result.size() - 2));
						pending.add(tmp);
						// result.set(result.size() - 1, pending);
						result.set(result.size() - 2, pending);
						result.remove(result.size() - 1);
					} else {
						pending.add(tmp);
						result.set(result.size() - 1, pending);
					}
				}
				pending.add(tile);
			} else {
				result.add(tile);
				pending = null;
			}
		}
		return result;
	}

	private static List<Tile> removeEmptyCloseToParallel(List<Tile> tiles) {
		final List<Tile> result = new ArrayList<>();
		for (Tile tile : tiles) {
			if (isParallel(tile))
				removeHeadEmpty(result);

			result.add(tile);
		}
		return result;

	}

	private static void removeHeadEmpty(List<Tile> tiles) {
		while (tiles.size() > 0 && tiles.get(tiles.size() - 1) instanceof EmptyTile)
			tiles.remove(tiles.size() - 1);

	}

	public static boolean isParallel(Tile tile) {
		return tile instanceof TileParallel == false && tile.getEvent().isParallel();
	}

	void addYNewPages(Collection<Double> yNewPages) {
		for (Tile tile : tiles) {
			if (tile instanceof GroupingTile)
				((GroupingTile) tile).addYNewPages(yNewPages);

			if (tile instanceof NewpageTile) {
				final double y = ((NewpageTile) tile).getTimeHook().getValue();
				yNewPages.add(y);
			}
		}
	}
}
