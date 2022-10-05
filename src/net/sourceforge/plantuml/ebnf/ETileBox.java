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

import net.sourceforge.plantuml.awt.geom.XDimension2D;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class ETileBox extends ETile {

	private final String value;
	private final FontConfiguration fc;
	private final Style style;
	private final UText utext;
	private final HColorSet colorSet;
	private final Symbol symbol;

	public ETileBox(String value, Symbol symbol, FontConfiguration fc, Style style, HColorSet colorSet) {
		this.symbol = symbol;
		this.value = value;
		this.fc = fc;
		this.utext = new UText(value, fc);
		this.style = style;
		this.colorSet = colorSet;
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		final double height = getTextDim(stringBounder).getHeight() + 10;
		return height / 2;
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return getH1(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return getTextDim(stringBounder).getWidth() + 10;
	}

	private XDimension2D getTextDim(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fc.getFont(), value);
	}

	@Override
	public void drawU(UGraphic ug) {
		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		final XDimension2D dimText = getTextDim(ug.getStringBounder());
		final HColor lineColor = style.value(PName.LineColor).asColor(colorSet);
		final HColor backgroundColor = style.value(PName.BackGroundColor).asColor(colorSet);

		if (symbol == Symbol.TERMINAL_STRING1 || symbol == Symbol.TERMINAL_STRING2) {
			final URectangle rect = new URectangle(dim);
			ug.apply(lineColor).apply(new UStroke(0.5)).draw(rect);
		} else {
			final URectangle rect = new URectangle(dim).rounded(10);
			ug.apply(lineColor).apply(backgroundColor.bg()).apply(new UStroke(1.5)).draw(rect);
		}

		ug.apply(new UTranslate(5, 5 + dimText.getHeight() - utext.getDescent(ug.getStringBounder()))).draw(utext);
	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
