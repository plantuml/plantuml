/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
package net.sourceforge.plantuml.ebnf;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;

public class ETileConcatenation extends ETile {
    // ::remove folder when __HAXE__

	private final double marginx = 20;
	private final List<ETile> tiles = new ArrayList<>();

	@Override
	public void push(ETile tile) {
		tiles.add(0, tile);
	}

	public void pushLast(ETile tile) {
		tiles.add(tile);
	}

	public void overideFirst(ETile tile) {
		tiles.set(0, tile);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();

		final double fullLinePos = getH1(stringBounder);
		double x = 0;
		drawHline(ug, fullLinePos, 0, x);
		for (int i = 0; i < tiles.size(); i++) {
			final ETile tile = tiles.get(i);
			final double linePos = tile.getH1(stringBounder);
			tile.drawU(ug.apply(new UTranslate(x, fullLinePos - linePos)));
			x += tile.calculateDimension(stringBounder).getWidth();
			if (i != tiles.size() - 1) {
				drawHlineDirected(ug, fullLinePos, x, x + marginx, 0.5, 25);
				x += marginx;
			}
		}

	}

	@Override
	public double getH1(StringBounder stringBounder) {
		double result = 0;

		for (ETile tile : tiles)
			result = Math.max(result, tile.getH1(stringBounder));

		return result;
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		double result = 0;

		for (ETile tile : tiles)
			result = Math.max(result, tile.getH2(stringBounder));

		return result;
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		double width = 0;
		for (int i = 0; i < tiles.size(); i++) {
			final ETile tile = tiles.get(i);
			width += tile.getWidth(stringBounder);
			if (i != tiles.size() - 1)
				width += marginx;
		}
		return width;
	}

	public ETile getFirst() {
		return tiles.get(0);
	}

}
