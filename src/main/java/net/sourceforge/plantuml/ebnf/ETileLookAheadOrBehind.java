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
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.Style;

public class ETileLookAheadOrBehind extends ETile {

	private final ETile orig;
	private final HColorSet colorSet;
	private final double deltax1 = 4;
	private final double deltax2 = 8;
	private final double deltay = 6;
	private final Style style;
	private final UText supText;

	private final FontConfiguration fc;

	public ETileLookAheadOrBehind(ETile orig, FontConfiguration fc, Style style, HColorSet colorSet, String type) {
		this.style = style;
		this.orig = orig;
		this.fc = fc;
		this.colorSet = colorSet;
		this.supText = UText.build(type, fc);

	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return deltay + orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder) + deltay;
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		return orig.getWidth(stringBounder) + deltax1 + deltax2 + supText.calculateDimension(stringBounder).getWidth();
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final XDimension2D dim = calculateDimension(stringBounder);

		final HColor lineColor = style.value(PName.LineColor).asColor(colorSet);

		final URectangle rect = URectangle.build(dim).rounded(30);

		ug.apply(lineColor).apply(new UStroke(2, 3, 1)).draw(rect);
		final double posText = getH1(stringBounder) + supText.getDescent(stringBounder);

		ug.apply(new UTranslate(4, 2 + posText)).draw(supText);

		orig.drawU(ug.apply(new UTranslate(deltax1 + supText.calculateDimension(stringBounder).getWidth(), deltay)));

	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
