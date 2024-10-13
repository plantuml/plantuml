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

import java.util.List;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ETileRegexGroup extends ETile {

	private final List<String> elements;
	private final FontConfiguration fc;
	private final Style style;
	private final HColorSet colorSet;
	private final ISkinParam skinParam;

	public ETileRegexGroup(List<String> elements, FontConfiguration fc, Style style, HColorSet colorSet,
			ISkinParam skinParam) {
		this.skinParam = skinParam;
		this.elements = elements;
		this.fc = fc;
		this.style = style;
		this.colorSet = colorSet;
	}

	private double getPureH1(StringBounder stringBounder) {
		final double height = getTextDim(stringBounder).getHeight();
		return height / 2;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return getPureH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return getPureH1(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return getTextDim(stringBounder).getWidth() + 10;
	}

	private XDimension2D getTextDim(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (String element : elements) {
			final XDimension2D dim = stringBounder.calculateDimension(fc.getFont(), element);
			width = Math.max(width, dim.getWidth());
			height += dim.getHeight();
		}
		return new XDimension2D(width, height);
	}

	private XDimension2D getBoxDim(StringBounder stringBounder) {
		return getTextDim(stringBounder).delta(10, 0);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);
		final XDimension2D dimText = getTextDim(stringBounder);
		final XDimension2D dimBox = getBoxDim(stringBounder);
		final HColor lineColor = style.value(PName.LineColor).asColor(colorSet);
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(colorSet);

		final double posxBox = (dim.getWidth() - dimBox.getWidth()) / 2;

		final URectangle rect = URectangle.build(dimBox);
		ug.apply(new UTranslate(posxBox, 0)).apply(lineColor).apply(new UStroke(5, 5, 1)).draw(rect);

		double y = 0;
		for (String element : elements) {
			final UText utext = UText.build(element, fc);
			final XDimension2D dimElement = utext.calculateDimension(stringBounder);

			ug.apply(new UTranslate(5 + posxBox, y + dimElement.getHeight() - utext.getDescent(stringBounder)))
					.draw(utext);
			if (y > 0)
				drawHline(ug.apply(UStroke.withThickness(0.3)), y, 0, dimBox.getWidth());
			y += dimElement.getHeight();
		}

		if (posxBox > 0) {
			drawHlineDirected(ug, getH1(stringBounder), 0, posxBox, .5, 25);
			drawHlineDirected(ug, getH1(stringBounder), posxBox + dimBox.getWidth(), dim.getWidth(), .5, 25);
		}

	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
