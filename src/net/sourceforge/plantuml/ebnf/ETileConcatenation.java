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
package net.sourceforge.plantuml.ebnf;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileConcatenation extends AbstractTextBlock implements ETile {

	private final double marginx = 16;
	private final List<ETile> tiles = new ArrayList<>();

	@Override
	public void push(ETile tile) {
		tiles.add(0, tile);
	}

	@Override
	public double linePos(StringBounder stringBounder) {
		double result = 0;

		for (ETile tile : tiles)
			result = Math.max(result, tile.linePos(stringBounder));

		return result;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(HColors.BLACK);

		final double fullLinePos = linePos(stringBounder);
		double x = 0;
		drawHline(ug, fullLinePos, 0, x);
		for (int i = 0; i < tiles.size(); i++) {
			final ETile tile = tiles.get(i);
			final double linePos = tile.linePos(stringBounder);
			tile.drawU(ug.apply(new UTranslate(x, fullLinePos - linePos)));
			x += tile.calculateDimension(stringBounder).getWidth();
			if (i != tiles.size() - 1) {
				drawHline(ug, fullLinePos, x, x + marginx);
				x += marginx;
			}
		}

	}

	public static void drawHline(UGraphic ug, double y, double x1, double x2) {
		ug.apply(new UTranslate(x1, y)).draw(ULine.hline(x2 - x1));
	}

	public static void drawVline(UGraphic ug, double x, double y1, double y2) {
		ug.apply(new UTranslate(x, y1)).draw(ULine.vline(y2 - y1));
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;

		for (int i = 0; i < tiles.size(); i++) {
			final ETile tile = tiles.get(i);
			final XDimension2D dim = tile.calculateDimension(stringBounder);
			height = Math.max(height, dim.getHeight());
			width += dim.getWidth();
			if (i != tiles.size() - 1)
				width += marginx;
		}
		return new XDimension2D(width, height);
	}

}
