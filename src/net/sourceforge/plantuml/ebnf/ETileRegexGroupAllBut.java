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

import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
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

public class ETileRegexGroupAllBut extends ETile {

	private final List<String> elements1;
	private final List<String> elements2;
	private final FontConfiguration fc;
	private final Style style;
	private final HColorSet colorSet;

	public ETileRegexGroupAllBut(List<String> elements, FontConfiguration fc, Style style, HColorSet colorSet,
			ISkinParam skinParam) {
		this.elements1 = elements.subList(0, 1);
		this.elements2 = elements.subList(1, elements.size());
		this.fc = fc;
		this.style = style;
		this.colorSet = colorSet;
	}

	private double getHalfHeight(StringBounder stringBounder) {
		final double height1 = getTextDim(elements1, stringBounder).getHeight();
		final double height2 = getTextDim(elements2, stringBounder).getHeight();
		return Math.max(height1, height2) / 2;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return getHalfHeight(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return getHalfHeight(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return getTextDim(elements1, stringBounder).getWidth() + getTextDim(elements2, stringBounder).getWidth() + 20;
	}

	private XDimension2D getTextDim(List<String> elements, StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (String element : elements) {
			final XDimension2D dim = stringBounder.calculateDimension(fc.getFont(), element);
			width = Math.max(width, dim.getWidth());
			height += dim.getHeight();
		}
		return new XDimension2D(width, height);
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dimTotal = calculateDimension(stringBounder);
		final XDimension2D dim1 = getTextDim(elements1, stringBounder);
		final XDimension2D dim2 = getTextDim(elements2, stringBounder);

		box(ug.apply(style.value(PName.LineColor).asColor(colorSet)), dimTotal, dim1.delta(10, 0), dim2.delta(10, 0));

		drawList(ug.apply(UTranslate.dy((dimTotal.getHeight() - dim1.getHeight()) / 2)), elements1);
		drawList(ug.apply(UTranslate.dx(10 + dim1.getWidth())), elements2);

//		if (posxBox > 0) {
//			drawHlineDirected(ug, getH1(stringBounder), 0, posxBox, .5, 25);
//			drawHlineDirected(ug, getH1(stringBounder), posxBox + dimBox.getWidth(), dim.getWidth(), .5, 25);
//		}

	}

	private void box(UGraphic ug, XDimension2D dimTotal, XDimension2D dim1, XDimension2D dim2) {
		ug = ug.apply(new UStroke(5, 5, 1));

		ug.apply(UTranslate.dx(dim1.getWidth())).draw(URectangle.build(dim2));

		final UPath path = UPath.none();
		path.moveTo(dim1.getWidth(), 0);
		path.lineTo(0, 0);
		path.lineTo(0, dim1.getHeight());
		path.lineTo(dim1.getWidth(), dim1.getHeight());

		ug.apply(UTranslate.dy((dimTotal.getHeight() - dim1.getHeight()) / 2)).draw(path);
	}

	private void drawList(UGraphic ug, List<String> elements) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double totalWidth = 10 + getTextDim(elements1, stringBounder).getWidth();
		double y = 0;
		for (String element : elements) {
			final UText utext = UText.build(element, fc);
			final XDimension2D dimElement = utext.calculateDimension(stringBounder);

			ug.apply(new UTranslate(5, y + dimElement.getHeight() - utext.getDescent(stringBounder))).draw(utext);
			if (y > 0)
				drawHline(ug.apply(UStroke.withThickness(0.3)), y, 0, totalWidth);
			y += dimElement.getHeight();
		}
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
