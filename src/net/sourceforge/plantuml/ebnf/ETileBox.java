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
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColors;

public class ETileBox extends AbstractTextBlock implements ETile {

	private final String value;
	private final FontConfiguration fc;
	private final UText utext;

	public ETileBox(String value, FontConfiguration fc) {
		this.value = value;
		this.fc = fc;
		this.utext = new UText(value, fc);
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return XDimension2D.delta(getTextDim(stringBounder), 10);
	}

	private XDimension2D getTextDim(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fc.getFont(), value);
	}

	@Override
	public void drawU(UGraphic ug) {
		final XDimension2D dim = calculateDimension(ug.getStringBounder());
		final XDimension2D dimText = getTextDim(ug.getStringBounder());
		final URectangle rect = new URectangle(dim).rounded(10);
		ug.apply(HColors.BLACK).apply(new UStroke(1.5)).draw(rect);
		ug.apply(new UTranslate(5, 5 + dimText.getHeight() - utext.getDescent(ug.getStringBounder()))).draw(utext);
	}

	@Override
	public double linePos(StringBounder stringBounder) {
		final double height = calculateDimension(stringBounder).getHeight();
		return height / 2;
	}

}
