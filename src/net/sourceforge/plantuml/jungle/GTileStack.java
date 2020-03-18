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
package net.sourceforge.plantuml.jungle;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GTileStack extends AbstractTextBlock implements GTile {

	private final List<GTile> tiles;
	private final double space;

	public GTileStack(List<GTile> tiles, double space) {
		this.tiles = tiles;
		this.space = space;
		if (tiles.size() == 0) {
			throw new IllegalArgumentException();
		}
	}

	public void drawU(UGraphic ug) {
		for (GTile tile : tiles) {
			tile.drawU(ug);
			final Dimension2D dim = tile.calculateDimension(ug.getStringBounder());
			ug = ug.apply(UTranslate.dy(dim.getHeight() + space));
		}
	}

	public GTileGeometry calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		double delta = 0;
		final List<Double> wests = new ArrayList<Double>();
		for (GTile tile : tiles) {
			final GTileGeometry dim = tile.calculateDimension(stringBounder);
			wests.add(delta + dim.getWestPositions().get(0));
			height += dim.getHeight();
			delta += dim.getHeight() + space;
			width = Math.max(width, dim.getWidth());
		}
		height += (tiles.size() - 1) * space;
		return new GTileGeometry(width, height, wests);
	}

}
