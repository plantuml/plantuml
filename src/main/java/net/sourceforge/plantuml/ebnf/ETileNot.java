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

import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class ETileNot extends ETile {

	private static final int MARGIN_X1 = 3;
	private static final int MARGIN_X2 = 8;
	private final ETile orig;
	private final FontConfiguration fc;
	private final Style style;
	private final HColorSet colorSet;
	private final ISkinParam skinParam;
	private final UText utext;

	public ETileNot(ETile orig, FontConfiguration fc, Style style, HColorSet colorSet, ISkinParam skinParam) {
		this.skinParam = skinParam;
		this.orig = orig;
		this.style = style;
		this.colorSet = colorSet;
		this.fc = fc;
		this.utext = UText.build("NOT", fc);
	}

	@Override
	public double getH1(StringBounder stringBounder) {
		return orig.getH1(stringBounder);
	}

	@Override
	public double getH2(StringBounder stringBounder) {
		return orig.getH2(stringBounder);
	}

	@Override
	public double getWidth(StringBounder stringBounder) {
		final XDimension2D dimText = utext.calculateDimension(stringBounder);
		return orig.getWidth(stringBounder) + dimText.getWidth() + MARGIN_X2;
	}

	@Override
	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		// final XDimension2D dim = calculateDimension(stringBounder);
		final XDimension2D dimText = utext.calculateDimension(stringBounder);

		ug.apply(new UTranslate(MARGIN_X1, getH1(stringBounder) + utext.getDescent(stringBounder) + 1)).draw(utext);

		orig.drawU(ug.apply(UTranslate.dx(dimText.getWidth() + MARGIN_X2)));

	}

	@Override
	public void push(ETile tile) {
		throw new UnsupportedOperationException();
	}

}
