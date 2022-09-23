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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class Alternation extends AbstractTextBlock implements TextBlockBackcolored, ETile {

	private final List<String> values = new ArrayList<>();
	private final StyleBuilder styleBuilder;
	private final HColorSet colorSet;

	public Alternation(ISkinParam skinParam) {
		this.styleBuilder = skinParam.getCurrentStyleBuilder();
		this.colorSet = skinParam.getIHtmlColorSet();
	}

	public void alternation(Token token) {
		values.add(0, token.getData());
	}

	@Override
	public double linePos(StringBounder stringBounder) {
		final Style style = getStyleSignature().getMergedStyle(styleBuilder);
		final FontConfiguration fc = style.getFontConfiguration(colorSet);
		final ETile tile = new ETileBox(values.get(0), fc);
		return tile.linePos(stringBounder);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug = ug.apply(HColors.BLACK);
		final Style style = getStyleSignature().getMergedStyle(styleBuilder);
		final FontConfiguration fc = style.getFontConfiguration(colorSet);
		double y = 0;
		double lastLinePos = 0;

		final double b = 30 - 16;
		final double a = b - 8;
		final double c = b + 8;

		final XDimension2D fullDim = calculateDimension(stringBounder);

		final double q = fullDim.getWidth() - 14;
		final double p = q - 8;
		final double r = q + 8;

		for (int i = 0; i < values.size(); i++) {
			final String value = values.get(i);
			final ETile tile = new ETileBox(value, fc);
			final XDimension2D dim = tile.calculateDimension(stringBounder);
			lastLinePos = y + tile.linePos(stringBounder);
			tile.drawU(ug.apply(new UTranslate(30, y)));

			if (i == 0) {
				drawHline(ug, lastLinePos, 0, 30);
				drawHline(ug, lastLinePos, 30 + dim.getWidth(), fullDim.getWidth());
			} else if (i > 0 && i < values.size() - 1) {
				drawHline(ug, lastLinePos, c, 30);
				new CornerCurved(8).drawU(ug.apply(new UTranslate(b, lastLinePos)));
				drawHline(ug, lastLinePos, 30 + dim.getWidth(), p);
				new CornerCurved(-8).drawU(ug.apply(new UTranslate(q, lastLinePos)));

			} else if (i == values.size() - 1) {
				drawHline(ug, lastLinePos, c, 30);
				drawHline(ug, lastLinePos, 30 + dim.getWidth(), p);

			}
			y += dim.getHeight() + 10;
		}
		final double linePos = linePos(stringBounder);

		final HLineCurved hlineIn = new HLineCurved(lastLinePos - linePos, 8);
		hlineIn.drawU(ug.apply(new UTranslate(b, linePos)));

		final HLineCurved hlineOut = new HLineCurved(lastLinePos - linePos, -8);
		hlineOut.drawU(ug.apply(new UTranslate(q, linePos)));

	}

	private void drawHline(UGraphic ug, double y, double x1, double x2) {
		ug.apply(new UTranslate(x1, y)).draw(ULine.hline(x2 - x1));
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final Style style = getStyleSignature().getMergedStyle(styleBuilder);
		final FontConfiguration fc = style.getFontConfiguration(colorSet);
		double width = 0;
		double height = 0;
		for (String value : values) {
			final ETile tile = new ETileBox(value, fc);
			final XDimension2D dim = tile.calculateDimension(stringBounder);
			height += dim.getHeight() + 10;
			width = Math.max(width, dim.getWidth());
		}
		width += 60;
		return new XDimension2D(width, height);
	}

	private StyleSignatureBasic getStyleSignature() {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.activityDiagram, SName.activity);
	}

	@Override
	public HColor getBackcolor() {
		return null;
	}

}
