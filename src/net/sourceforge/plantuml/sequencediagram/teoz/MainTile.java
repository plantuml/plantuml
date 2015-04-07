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
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealMax;
import net.sourceforge.plantuml.real.RealMin;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class MainTile implements Tile {

	private final RealMin min = new RealMin();
	private final RealMax max = new RealMax();
	// private double height;

	private final List<Tile> tiles = new ArrayList<Tile>();

	public MainTile(SequenceDiagram diagram, Skin skin, Real omega, LivingSpaces livingSpaces, Real origin) {

		min.put(origin);
		max.put(omega);

		final ISkinParam skinParam = diagram.getSkinParam();
		final StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();

		final TileArguments tileArguments = new TileArguments(stringBounder, omega, livingSpaces, skin, skinParam,
				origin);

		tiles.addAll(TileBuilder.buildSeveral(diagram.events().iterator(), tileArguments, null));

		for (Tile tile : tiles) {
			// height += tile.getPreferredHeight(stringBounder);
			min.put(tile.getMinX(stringBounder));
			max.put(tile.getMaxX(stringBounder));
		}
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final LiveBoxFinder liveBoxFinder = new LiveBoxFinder(stringBounder);

		drawUInternal(liveBoxFinder, false);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, true);
		drawUInternal(interceptor, false);
	}

	public void drawForeground(UGraphic ug) {
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(ug, false);
		drawUInternal(interceptor, false);
	}

	private double drawUInternal(UGraphic ug, boolean trace) {
		final StringBounder stringBounder = ug.getStringBounder();
		double y = 0;
		double lastY = 0;
		final List<YPositionedTile> positionedTiles = new ArrayList<YPositionedTile>();
		for (Tile tile : tiles) {
			if (tile.getEvent().isParallel()) {
				y = lastY;
			}
			if (trace) {
				System.err.println("MainTile::drawUInternal tile=" + tile + " y=" + y);
			}
			positionedTiles.add(new YPositionedTile(tile, y));
			lastY = y;
			y += tile.getPreferredHeight(stringBounder);
		}
		for (YPositionedTile tile : positionedTiles) {
			tile.drawU(ug);
		}
		System.err.println("MainTile::drawUInternal finalY=" + y);
		return y;
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final LimitFinder limitFinder = new LimitFinder(stringBounder, true);
		final UGraphicInterceptorTile interceptor = new UGraphicInterceptorTile(limitFinder, false);
		final double finalY = drawUInternal(interceptor, true);
		final double result = Math.max(limitFinder.getMinMax().getDimension().getHeight(), finalY) + 10;
		System.err.println("MainTile::getPreferredHeight=" + result);
		return result;
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

	public Event getEvent() {
		return null;
	}

}
