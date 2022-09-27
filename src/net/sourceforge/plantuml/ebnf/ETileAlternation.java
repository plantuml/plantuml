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
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileAlternation extends AbstractTextBlock implements ETile {

	private final List<ETile> tiles = new ArrayList<>();
	private final double marginx = 12;

	@Override
	public void push(ETile tile) {
		tiles.add(0, tile);
	}

	@Override
	public double linePos(StringBounder stringBounder) {
		return tiles.get(0).linePos(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(HColors.BLACK);
		double y = 0;
		double lastLinePos = 0;

		final double a = 0;
		final double b = a + marginx;
		final double c = b + marginx;

		final XDimension2D fullDim = calculateDimension(stringBounder);

		final double r = fullDim.getWidth();
		final double q = r - marginx;
		final double p = q - marginx;

		for (int i = 0; i < tiles.size(); i++) {
			final ETile tile = tiles.get(i);
			final XDimension2D dim = tile.calculateDimension(stringBounder);
			lastLinePos = y + tile.linePos(stringBounder);
			tile.drawU(ug.apply(new UTranslate(c, y)));

			if (i == 0) {
				ETileConcatenation.drawHline(ug, lastLinePos, a, c);
				ETileConcatenation.drawHline(ug, lastLinePos, c + dim.getWidth(), r);
			} else if (i > 0 && i < tiles.size() - 1) {
				CornerCurved.createSW(marginx).drawU(ug.apply(new UTranslate(b, lastLinePos)));
				ETileConcatenation.drawHline(ug, lastLinePos, c + dim.getWidth(), p);
				CornerCurved.createSE(marginx).drawU(ug.apply(new UTranslate(q, lastLinePos)));
			} else if (i == tiles.size() - 1) {
				ETileConcatenation.drawHline(ug, lastLinePos, c + dim.getWidth(), p);

			}
			y += dim.getHeight() + 10;
		}
		final double linePos = linePos(stringBounder);

		final VLineCurved hlineIn = new VLineCurved(lastLinePos - linePos, marginx, CornerCurved.createNE(marginx),
				CornerCurved.createSW(marginx));
		hlineIn.drawU(ug.apply(new UTranslate(b, linePos)));

		final VLineCurved hlineOut = new VLineCurved(lastLinePos - linePos, marginx, CornerCurved.createNW(marginx),
				CornerCurved.createSE(marginx));
		hlineOut.drawU(ug.apply(new UTranslate(q, linePos)));

	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (ETile tile : tiles) {
			final XDimension2D dim = tile.calculateDimension(stringBounder);
			height += dim.getHeight();
			height += 10;
			width = Math.max(width, dim.getWidth());
		}
		width += 2 * 2 * marginx;
		return new XDimension2D(width, height);
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

}
