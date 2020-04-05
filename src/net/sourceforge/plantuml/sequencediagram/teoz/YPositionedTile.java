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

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class YPositionedTile {

	private final Tile tile;
	private final double y;

	public boolean inArea(double ymin, double ymax) {
		return y >= ymin && y < ymax;
	}

	public YPositionedTile(Tile tile, double y) {
		this.tile = tile;
		this.y = y;
		if (tile instanceof TileWithCallbackY) {
			((TileWithCallbackY) tile).callbackY(y);
		}
	}

	@Override
	public String toString() {
		return "y=" + y + " " + tile;
	}

	public void drawInArea(UGraphic ug) {
		// System.err.println("YPositionedTile::drawU y=" + y + " " + tile);
		ug.apply(UTranslate.dy(y)).draw(tile);
	}

	public boolean matchAnchorV2(String anchor) {
		final boolean result = tile.matchAnchorV1(anchor);
		return result;
	}

	public final double getY(StringBounder stringBounder) {
		final TileWithUpdateStairs communicationTile = (TileWithUpdateStairs) tile;
		return y + communicationTile.getYPoint(stringBounder);
	}

	public double getMiddleX(StringBounder stringBounder) {
		final double max = tile.getMaxX(stringBounder).getCurrentValue();
		final double min = tile.getMinX(stringBounder).getCurrentValue();
		return (min + max) / 2;
	}

}
